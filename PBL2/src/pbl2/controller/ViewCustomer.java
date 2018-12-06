package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import pbl2.dto.DtoCustomer;
import pbl2.dto.DtoMileage;

public class ViewCustomer implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == searchIdRadio) {
			typeFlag = 0;
			searchInput.setText(null);
			searchField.setText("ID : ");
			searchPhoneField.setVisible(false);
			searchPhoneInput.setVisible(false);
			searchPhoneInput.setEnabled(false);
		}else if(e.getSource() == searchNameRadio) {
			typeFlag = 1;
			searchInput.setText(null);
			searchPhoneField.setVisible(true);
			searchPhoneInput.setVisible(true);
			searchPhoneInput.setEnabled(true);
			searchField.setText("이름 : ");
			searchPhoneField.setText("전화번호  : ");
			searchPhoneInput.setText(null);
		}else if(e.getSource() == searchBtn) {
			currList.clear();
			searchData();
			updateTableView();
		}else if(e.getSource() == addBtn) {
			addData();
			updateTableView();
		}
	}

	private ArrayList<DtoCnM> list;
	private ArrayList<DtoCnM> currList;
	private int width, height, typeFlag;
	private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private DefaultTableModel model;
	private JTable table;
	private JPanel mainPan, searchPan, registerPan;
	private JLabel nameField, phoneField, emailField, addressField, cardnumberField, commentField, searchField, searchPhoneField;
	private JTextField nameInput, phoneInput, emailInput, addressInput, cardnumberInput, commentInput, searchInput, searchPhoneInput;
	private JTabbedPane regiSearchJtp;
	private ButtonGroup searchTypeGroup;
	private JRadioButton searchIdRadio, searchNameRadio;
	private JButton searchBtn, addBtn;
	private ModifyDialog md;
	private JScrollPane scroll;
	
	public ViewCustomer(ArrayList<DtoCustomer> clist, ArrayList<DtoMileage> mlist, int width, int height) {
		list = new ArrayList<>();
		currList = new ArrayList<>();
		combineCnM(clist, mlist);
		this.width = width-30;
		this.height = height;
		makePan();
	}
	
	private void combineCnM(ArrayList<DtoCustomer> clist, ArrayList<DtoMileage> mlist) {
		for(int i = 0; i <clist.size(); i++) {
			DtoCustomer temp = clist.get(i);
			if (!(temp.getName() == null)) {
				int id = temp.getCustomerId();
				String name = temp.getName();
				String phone = temp.getPhone();
				String email = temp.getEmail();
				String address = temp.getAddress();
				String card = temp.getCardNumber();
				String comment = temp.getComment();
				int mileage = 0;
				for(int j = 0; j<mlist.size() ; j++) {
					if (mlist.get(j).getCustomerId() == id)
						mileage = mlist.get(j).getMileage();
				}
				list.add(new DtoCnM(id, name, phone, email, address, card, comment, mileage));
			}
		}
	}
	
	
	private void makePan() {
		mainPan = new JPanel();
		mainPan.setLayout(null);
		mainPan.setSize(width, height);
		regiSearchJtp = new JTabbedPane();
		
		int searchAreaHeight = (int)(height*0.45);
		regiSearchJtp.setBounds(0, 0, width, searchAreaHeight);
		
		searchPan = new JPanel();
		makeSearchPan();
		
		registerPan = new JPanel();
		makeRegisPan();
		
		regiSearchJtp.addTab("조회", null, searchPan, "고객을 조회합니다");
		regiSearchJtp.addTab("등록", null, registerPan, "고객을 등록합니다");
		regiSearchJtp.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				clearTableView();
			}
		});
		makeTableModel();
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					if(table.getSelectedRowCount()==1) {
						int id = Integer.valueOf((String) table.getModel().getValueAt(table.getSelectedRow(), 0));
						DtoCnM temp = null;
						for(int i =0; i<list.size();i++) {
							if(list.get(i).getCustomerId() == id) {
								temp = list.get(i);
								break;
							}
						}
						JFrame jf = new JFrame();
						if(md != null) {
							md.dispose();
						}
						md = new ModifyDialog(jf, "수정", temp);
						md.setVisible(true);
					}
				}
				
			}
		});
		scroll = new JScrollPane(table);
		scroll.setBounds(10, searchAreaHeight, width-20, height-searchAreaHeight-20);
		mainPan.add(regiSearchJtp);
		mainPan.add(scroll, BorderLayout.CENTER);
	}
	
	class DtoCnM extends DtoCustomer {
		private int mileage;
		public DtoCnM(int customerId, String name, String phone, String email, String address, String cardNumber,
				String comment, int mileage) {
			super(customerId, name, phone, email, address, cardNumber ,comment);
			this.mileage = mileage;
		}
		
		public int getMileage() {
			return mileage;
		}
		
		public void setMileage(int mileage) {
			this.mileage = mileage;
		}
	}
	
	class ModifyDialog extends JDialog{
		private static final long serialVersionUID = 739480446049992367L;
		JLabel id, name, name2, phone, email, address, card, comment, info;
		JTextField idInput, nameInput, nameInput2, phoneInput, aInput, cardInput, emailInput, commentInput;
	    JButton modiButton=new JButton("수정");
	    JButton cancelButton = new JButton("취소");
	    JButton cancel2Button = new JButton("취소");
	    JButton deleteButton = new JButton("삭제");
	    ModifyDialog(JFrame frame, String title, DtoCnM temp){
	        super(frame,title);
	        this.setLayout(null);
	        this.setSize(240, 420);
	        this.setLocationRelativeTo(null);
	        JTabbedPane tabPan = new JTabbedPane();
	        tabPan.setBounds(0, 0, this.getWidth(), this.getHeight()-20);
	        JPanel p1 = new JPanel();
	        JPanel p2 = new JPanel();
	        p1.setLayout(null);
	        p2.setLayout(null);
	        id   		= new JLabel("ID   :");
	        name 		= new JLabel("이름  :");
	        name2 		= new JLabel("이름  :");
	        phone		= new JLabel("연락처 :");
	        email 		= new JLabel("E-Mail  :");
	        address		= new JLabel("주소 : ");
	        card		= new JLabel("카드#:");
	        comment  	= new JLabel("코멘트  :");
	        info		= new JLabel("<html><div style='text-align:center'>'ID'와 '이름'을<br/>정확히 기입해주세요.</div></html>");
	        idInput 	= new JTextField();
	        nameInput 	= new JTextField();
	        nameInput2 	= new JTextField();
	        phoneInput = new JTextField();
	        phoneInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = phoneInput.getText().length();
					if(len < 13) {
						if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
							return;
						}
						if(!Character.isDigit(c)) {
							e.consume();
							return;
						}
						if(len == 3 || len == 8) {
							phoneInput.setText(phoneInput.getText() + "-");
						}
					}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
						e.consume();
						return;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	        emailInput = new JTextField();
	        aInput 	= new JTextField();
	        cardInput   = new JTextField();
	        cardInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = cardInput.getText().length();
					if(len < 19) {
						if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
							return;
						}
						if(!Character.isDigit(c)) {
							e.consume();
							return;
						}
						if(len == 4 || len == 9 || len == 14) {
							cardInput.setText(cardInput.getText() + "-");
						}
					}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
						e.consume();
						return;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	        commentInput 	= new JTextField();
	        
	        int x = tabPan.getWidth()/2 - 90;
	        int y = 30;
	        int w = 50;
	        int inputW = 100;
	        int h = 20;
	        int weight = 40;
	        
	        id.setBounds(x, y, w, h);
	        name.setBounds(x, y, w, h);
	        idInput.setBounds(id.getX()+id.getWidth()+10, y, inputW, h);
	        nameInput.setBounds(name.getX()+name.getWidth()+10, y, inputW, h);

	        phone.setBounds(x, y+=weight, w, h);
	        phoneInput.setBounds(phone.getX()+phone.getWidth()+10, y, inputW, h);
	        
	        name2.setBounds(x, y, w, h);
	        nameInput2.setBounds(name2.getX()+name2.getWidth()+10, y, inputW, h);
	        
	        email.setBounds(x, y+=weight, w, h);
	        emailInput.setBounds(email.getX()+email.getWidth()+10, y, inputW, h);

	        address.setBounds(x, y+=weight, w, h);
	        aInput.setBounds(address.getX()+address.getWidth()+10, y, inputW, h);

	        card.setBounds(x, y+=weight, w, h);
	        info.setBounds(x+30, y, w+inputW, h*3);
	        cardInput.setBounds(card.getX()+card.getWidth()+10, y, inputW, h);
	        
	        comment.setBounds(x, y+=weight, w, h);
	        commentInput.setBounds(comment.getX()+comment.getWidth()+10, y, inputW, h);
	        
	        modiButton.setBounds((tabPan.getWidth()-20)/2-(w+inputW)/2, y+=weight, w+inputW, h);
	        deleteButton.setBounds((tabPan.getWidth()-20)/2-(w+inputW)/2, y, w+inputW, h);
	        cancelButton.setBounds((tabPan.getWidth()-20)/2-(w+inputW)/2, y+=30, w+inputW, h);
	        cancel2Button.setBounds((tabPan.getWidth()-20)/2-(w+inputW)/2, y, w+inputW, h);
	        deleteButton.setEnabled(false);
	        
	        modiButton.setBorderPainted(true);
	        modiButton.setBorder(BorderFactory.createLineBorder(new Color(72, 22, 243)));
	        modiButton.setForeground(new Color(72, 22, 243));
	        deleteButton.setBorderPainted(true);
	        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(207,207,207)));
			deleteButton.setForeground(new Color(207,207,207));
	       
	        nameInput.setText(temp.getName());
	        phoneInput.setText(temp.getPhone());
	        emailInput.setText(temp.getEmail());
	        aInput.setText(temp.getAddress());
	        cardInput.setText(temp.getCardNumber());
	        commentInput.setText(temp.getComment());
	        
	        //삭제용
	        idInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}
				@Override
				public void keyReleased(KeyEvent e) {
					if(idInput.getText().equals(String.valueOf(temp.getCustomerId())) && nameInput2.getText().equals(temp.getName())) {
						deleteButton.setEnabled(true);
					    deleteButton.setBorder(BorderFactory.createLineBorder(new Color(251,33,31)));
					    deleteButton.setForeground(new Color(251,33,31));
					    
					}else {
						deleteButton.setEnabled(false);
						deleteButton.setBorder(BorderFactory.createLineBorder(new Color(207,207,207)));
						deleteButton.setForeground(new Color(207,207,207));
					}
				}
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	        nameInput2.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}
				@Override
				public void keyReleased(KeyEvent e) {
					if(idInput.getText().equals(String.valueOf(temp.getCustomerId())) && nameInput2.getText().equals(temp.getName())) {
						deleteButton.setEnabled(true);
						deleteButton.setBorder(BorderFactory.createLineBorder(new Color(251,33,31)));
					    deleteButton.setForeground(new Color(251,33,31));
					}else {
						deleteButton.setEnabled(false);
						deleteButton.setBorder(BorderFactory.createLineBorder(new Color(207,207,207)));
						deleteButton.setForeground(new Color(207,207,207));
					}
				}
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	        modiButton.addActionListener(new ActionListener(){
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(nameInput.getText().equals(temp.getName()) && phoneInput.getText().equals(temp.getPhone()) 
	            			&& emailInput.getText().equals(temp.getEmail()) && aInput.getText().equals(temp.getAddress()) 
	            			&& cardInput.getText().equals(temp.getCardNumber()) && commentInput.getText().equals(temp.getComment())){
	            		JOptionPane.showConfirmDialog(null, "수정사항이 없습니다.", "확인", JOptionPane.PLAIN_MESSAGE);
	            		System.out.println("no change");
	            		System.out.println();
	            		dispose();
	            	}else {
	            		String tempName = null;
	            		if(!nameInput.getText().isEmpty()) {
	            			tempName = nameInput.getText();
	            		}
	            		
	            		String tempPhone = null;
	            		if(!phoneInput.getText().isEmpty()) {
	            			tempPhone = phoneInput.getText();
	            		}
	            		
	            		String tempEmail = null;
	            		if(!emailInput.getText().isEmpty()) {
	            			tempEmail = emailInput.getText();
	            			if(!validateEmail(tempEmail)) {
	            				tempEmail = "wrong";
	            			}
	            		}
	            		
	            		String tempaddress = null;
	            		if(!aInput.getText().isEmpty()) {
	            			tempaddress = aInput.getText();
	            		}
	            		
	            		String tempcn = null;
	            		if(!cardInput.getText().isEmpty()) {
	            			tempcn = cardInput.getText();
	            		}
	            		
	            		String tempcmt = null;
	            		if(!commentInput.getText().isEmpty()) {
	            			tempcmt = commentInput.getText();
	            		}
	            		if(tempEmail.equals("wrong")) {
	            			JOptionPane.showMessageDialog(null, "이메일 형식을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
	            			return;
	            		}
	            		if(tempName == null || tempPhone == null || tempEmail == null || tempaddress == null || tempcn == null) {
	            			JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
	            			return;
	            		}else {
	            			int result = JOptionPane.showConfirmDialog(null, "이름    : " + tempName 
	        															+"\n연락처    : " + tempPhone
	        															+"\nE-Mail    : " + tempEmail
	        															+"\n주소   : " + tempaddress
	        															+"\n카드번호	 : " + tempcn
	        															+"\ncomment    : " + tempcmt, "확인", JOptionPane.OK_CANCEL_OPTION);
	            		
	                        if (result == 0) { //OK=0 , Cancel=2 리턴
	                    		temp.setName(tempName);
	                    		temp.setPhone(tempPhone);
	                    		temp.setEmail(tempEmail);
	                    		temp.setAddress(tempaddress);
	                    		temp.setCardNumber(tempcn);
	                    		temp.setComment(tempcmt);
	                    		for(int i = 0; i < list.size();i++) {
	    							if(list.get(i).getCustomerId() == temp.getCustomerId()) {
	    								list.set(i, temp);
	    								break;
	    							}
	    						}
	                    		pbl2.MainActivity.sql.query("UPDATE TBLCUSTOMER set CNAME = '"+temp.getName()+"', CPHONE = '"+ temp.getPhone()
	                    		+"', CEMAIL = '" + temp.getEmail() + "', CADDRESS = '" + temp.getAddress() +"', CCARDNUMBER = '" + temp.getCardNumber() +"', CCOMMENT = '" + temp.getComment() 
	                    		+ "' where CCUSTOMERID = " + temp.getCustomerId());
	    						pbl2.MainActivity.sql.commit();
	                    		dispose();
	                    		searchBtn.doClick();
	                        }
	            		}
	            	}
	            }
	        });
	        
	        deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "삭제", JOptionPane.OK_CANCEL_OPTION);
					if(result == 0) {
						for(int i = 0; i < list.size();i++) {
							if(list.get(i).getCustomerId() == temp.getCustomerId()) {
								list.remove(i);
								break;
							}
						}
						pbl2.MainActivity.sql.query("delete from tblMileage where mCustomerID = "+temp.getCustomerId());
						pbl2.MainActivity.sql.query("delete from tblCustomer where cCustomerID = "+temp.getCustomerId());
						pbl2.MainActivity.sql.commit();
						dispose();
						searchBtn.doClick();
					}
				}
			});
	        cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
	        cancel2Button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

	        p1.add(name);
	        p1.add(phone);
	        p1.add(email);
	        p1.add(address);
	        p1.add(card);
	        p1.add(comment);
	        p1.add(nameInput);
	        p1.add(phoneInput);
	        p1.add(emailInput);
	        p1.add(aInput);
	        p1.add(cardInput);
	        p1.add(commentInput);
	        p1.add(modiButton);
	        p1.add(cancelButton);
	        p2.add(info);
	        p2.add(id);
	        p2.add(idInput);
	        p2.add(name2);
	        p2.add(nameInput2);
	        p2.add(deleteButton);
	        p2.add(cancel2Button);
	        tabPan.addTab("수정", null, p1, "정보를 수정합니다.");
	        tabPan.addTab("삭제", null, p2, "정보를 삭제합니다.");
	        this.add(tabPan);
	        
	    }
	}


	
	
	private void makeSearchPan() {
		searchTypeGroup = new ButtonGroup();
		searchIdRadio = new JRadioButton("ID로 찾기");
		searchNameRadio = new JRadioButton("이름으로 찾기");
		searchIdRadio.addActionListener(this);
		searchNameRadio.addActionListener(this);
		searchTypeGroup.add(searchIdRadio);
		searchTypeGroup.add(searchNameRadio);
		searchIdRadio.setSelected(true);
		
		searchField = new JLabel("ID : ");
		searchInput = new JTextField();
		searchInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(typeFlag == 0) {
					if(!Character.isDigit(c)) {
						e.consume();
						return;
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		searchPhoneField = new JLabel("전화번호  : " );
		searchPhoneInput = new JTextField();
		searchPhoneInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				int len = searchPhoneInput.getText().length();
				if(len < 4) {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
						return;
					}
					if(!Character.isDigit(c)) {
						e.consume();
						return;
					}
				}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		searchBtn = new JButton("조회");
				
		int totalFieldWidth = 260;
		int x = regiSearchJtp.getWidth()/2-totalFieldWidth/2;
		int space = 10;
		int y = 10;
		int fieldW = 70;
		int inputW = 200;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/7);
		searchPan.setLayout(null);
		
		searchIdRadio.setBounds(x, y, fieldW+80, h);
		searchNameRadio.setBounds(searchIdRadio.getX()+searchIdRadio.getWidth()+space, y, fieldW+80, h);
		
		searchField.setBounds(x, y+=weight, fieldW, h);
		searchInput.setBounds(searchField.getX()+searchField.getWidth()+space, y, inputW, h);
		
		searchPhoneField.setBounds(x, y+=weight, fieldW, h);
		searchPhoneInput.setBounds(searchPhoneField.getX()+searchPhoneField.getWidth()+space, y, inputW, h);
		
		searchBtn.setBounds(regiSearchJtp.getWidth()/2-totalFieldWidth/6, y+=weight*2, totalFieldWidth/3, weight);
		searchBtn.addActionListener(this);
		
		searchPan.add(searchIdRadio);
		searchPan.add(searchNameRadio);
		searchPan.add(searchField);
		searchPan.add(searchInput);
		searchPan.add(searchPhoneField);
		searchPan.add(searchPhoneInput);
		searchPan.add(searchBtn);
		searchIdRadio.doClick();
	}
	
	private void makeRegisPan() {
		nameField = new JLabel("이름 : ");
		nameInput = new JTextField();
		
		phoneField = new JLabel("연락처 : ");
		phoneInput = new JTextField();
		phoneInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				int len = phoneInput.getText().length();
				if(len < 13) {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
						return;
					}
					if(!Character.isDigit(c)) {
						e.consume();
						return;
					}
					if(len == 3 || len == 8) {
						phoneInput.setText(phoneInput.getText() + "-");
					}
				}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		emailField = new JLabel("E-Mail:  ");	
		emailInput = new JTextField();
		
		addressField = new JLabel("주소 : ");
		addressInput = new JTextField();
		
		cardnumberField = new JLabel("카드 번호 : ");
		cardnumberInput = new JTextField();
		cardnumberInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				int len = cardnumberInput.getText().length();
				if(len < 19) {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
						return;
					}
					if(!Character.isDigit(c)) {
						e.consume();
						return;
					}
					if(len == 4 || len == 9 || len == 14) {
						cardnumberInput.setText(cardnumberInput.getText() + "-");
					}
				}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		commentField = new JLabel("코멘트 : ");
		commentInput = new JTextField();
		
		addBtn = new JButton("등록");
		
		int totalFieldWidth = 260;
		int x = regiSearchJtp.getWidth()/2-totalFieldWidth/2;
		int space = 10;
		int y = 10;
		int fieldW = 80;
		int inputW = 170;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/9);
		registerPan.setLayout(null);
		nameField.setBounds(x, y, fieldW, h);
		nameInput.setBounds(nameField.getX()+nameField.getWidth()+space, y, inputW, h);
		
		phoneField.setBounds(x, y+=weight, fieldW, h);
		phoneInput.setBounds(phoneField.getX()+phoneField.getWidth()+space, y, inputW, h);
		
		emailField.setBounds(x, y+=weight, fieldW, h);
		emailInput.setBounds(emailField.getX()+emailField.getWidth()+space, y, inputW, h);
		
		addressField.setBounds(x, y+=weight, fieldW, h);
		addressInput.setBounds(addressField.getX()+addressField.getWidth()+space, y, inputW, h);

		cardnumberField.setBounds(x, y+=weight, fieldW, h);
		cardnumberInput.setBounds(cardnumberField.getX()+cardnumberField.getWidth()+space, y, inputW, h);
		
		commentField.setBounds(x, y+=weight, fieldW, h);
		commentInput.setBounds(commentField.getX()+commentField.getWidth()+space, y, inputW, h);
		
		addBtn.setBounds(regiSearchJtp.getWidth()/2-totalFieldWidth/6, y+=weight, totalFieldWidth/3, weight);
		addBtn.addActionListener(this);
		
		registerPan.add(nameField);
		registerPan.add(nameInput);
		registerPan.add(phoneField);
		registerPan.add(phoneInput);
		registerPan.add(emailField);
		registerPan.add(emailInput);
		registerPan.add(addressField);
		registerPan.add(addressInput);
		registerPan.add(addressField);
		registerPan.add(cardnumberField);
		registerPan.add(cardnumberInput);
		registerPan.add(commentField);
		registerPan.add(commentInput);
		registerPan.add(addBtn);
	}
	
	private void makeTableModel() {
//		String[] colName = {"ID", "이름", "연락처", "E-Mail", "주소", "카드번호", "코멘트"};
		String[] colName = {"ID", "이름", "연락처", "E-Mail", "주소", "카드번호", "마일리지", "코멘트"};
		model = new DefaultTableModel(colName, 0);
		
	}
	
	public void addData() {			//add Customer data to list & SQL
		ResultSet rs = pbl2.MainActivity.sql.query("select max(ccustomerID) from tblCustomer");
		int id = 0;
		try {
			rs.next();
			id = rs.getInt(1)+1;
		} catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		String name = null;
		if(!nameInput.getText().isEmpty()) {
			name = nameInput.getText();
		}
		
		String phone = null;
		if(!phoneInput.getText().isEmpty()) {
				phone = phoneInput.getText();
		}
		
		String email = null;
		if (!emailInput.getText().isEmpty()) {
			email = emailInput.getText();
			if(!validateEmail(email)) {
				email = "wrong";
			}
		}
		
		String address = null;
		if (!addressInput.getText().isEmpty()) {
			address = addressInput.getText();
		}
		
		String cardnumber = null;
		if (!cardnumberInput.getText().isEmpty()) {
			cardnumber = cardnumberInput.getText();
		}
		
		String comment = null;
		if (!commentInput.getText().isEmpty()) {
			comment = commentInput.getText();
		}
		
		int mileage = 0;

		System.out.println("name:" + name);
		System.out.println("phone : " + phone);
		System.out.println("E-Mail : " + email);
		System.out.println("address : " + address);
		System.out.println("cardnumber : " + cardnumber);
		System.out.println("comment : " + comment);
		System.out.println("mileage : "+mileage);
		
		if(email.equals("wrong")) {
			JOptionPane.showMessageDialog(null, "이메일 형식을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(name == null || phone == null || email == null || address == null || cardnumber == null ) {
			JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
			return;
		}else {
			int result = JOptionPane.showConfirmDialog(null, "이름    : " + name 
															+"\n연락처    : " + phone
															+"\nE-Mail    : " + email
															+"\n주소   : " + address
															+"\n카드번호	 : " + cardnumber
															+"\ncomment    : " + comment, "확인", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) { //OK=0 , Cancel=2 리턴
        		DtoCnM dto = new DtoCnM(id, name, phone, email, address, cardnumber, comment, mileage);
        		list.add(dto);
        		String query = "INSERT INTO tblCustomer (cCustomerID,cName,cPhone,cEmail,cAddress,cCardNumber,cComment) VALUES ("+id+",'"+name+"','"+phone+"','"+email+"','"+address+"','"+cardnumber+"', '"+comment+"')";
        		pbl2.MainActivity.sql.query(query);
        		query = "INSERT INTO tblMileage (mCustomerID, mMileage) VALUES ("+id+",'"+mileage+"')";
        		pbl2.MainActivity.sql.query(query);
        		currList.add(dto);
        		updateTableView();
        	}
		}
	}

	private void addDataAtModel(DtoCnM dto) {
		String[] data = new String[8];
		data[0] = String.valueOf(dto.getCustomerId());
		data[1] = dto.getName();
		data[2] = dto.getPhone();
		data[3] = dto.getEmail();
		data[4] = dto.getAddress();
		data[5] = String.valueOf(dto.getCardNumber());
		data[6] = String.valueOf(dto.getMileage());
		if(String.valueOf(dto.getComment()).equals("null")) {
			data[7] = "-";
		}else {
			data[7] = String.valueOf(dto.getComment());
		}
		model.addRow(data);
	}
	
	private void clearModel() {
		for(int i = model.getRowCount()-1; i>=0; i--) {
			model.removeRow(i);
		}
	}
	
	private void clearTableView() {
		currList.clear();
		updateTableView();
	}
	
	private void updateTableView() {
		clearModel();
		for(int i = 0; i< currList.size();i++) {
			addDataAtModel(currList.get(i));
		}
		table.setModel(model);
	}
	private void searchData() {		//search data of customer by ID or 이름
		int id = -1;
		String name = null;
		String phone = null;
		if(typeFlag == 0) { //ID으로 찾기
			if(!searchInput.getText().isEmpty()) {
				id = Integer.valueOf(searchInput.getText());
				for(int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i).getCustomerId()); ///////
					if(list.get(i).getCustomerId() == id) {
						currList.add(list.get(i));
					}
				}
			}
		}else { //이름으로 찾기+전화번호 뒷자리로 찾기
			if(!searchInput.getText().isEmpty()) {	//이름 입력 받음
				name = searchInput.getText().toUpperCase();
			}
			if (!searchPhoneInput.getText().isEmpty()) {	//전화번호 입력 받음
				phone = searchPhoneInput.getText();
				if(phone.length()!=4) {
					JOptionPane.showMessageDialog(null, "이름 입력 또는 전화번호 뒷번호 4자리를 입력해주세요", "알림", JOptionPane.YES_OPTION);
					return;
				}
			}
			
			DtoCnM tempDto = null;
			for(int i = 0; i < list.size(); i++) {
				tempDto = list.get(i);
				if(name == null && phone == null) {
					currList.add(tempDto);
				}else if(name != null && phone != null) {
					if(tempDto.getName().toUpperCase().equals(name) && tempDto.getPhone().endsWith(phone)) {
						currList.add(tempDto);
					}					
				}else if(name != null) {
					if(tempDto.getName().toUpperCase().equals(name)) {
						currList.add(tempDto);
					}
				}else if(phone != null) {
					if(tempDto.getPhone().endsWith(phone)) {
						currList.add(tempDto);
					}
				}
			}
		}
	}
	private boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
	}
	
	
	public JPanel getPanel() {
		return mainPan;
	}
}