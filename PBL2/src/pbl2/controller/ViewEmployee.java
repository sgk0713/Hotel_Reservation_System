package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import pbl2.dto.DtoEmployee;

public class ViewEmployee implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == searchIdRadio) {
			typeFlag = 0;
			searchInput.setText(null);
			searchField.setText("ID : ");
			searchGenderField.setVisible(false);
			searchMaleRadio.setEnabled(false);
			searchMaleRadio.setVisible(false);
			searchFemaleRadio.setEnabled(false);
			searchFemaleRadio.setVisible(false);
			searchBothRadio.setEnabled(false);
			searchBothRadio.setVisible(false);
		}else if(e.getSource() == searchNameRadio) {
			typeFlag = 1;
			searchInput.setText(null);
			searchField.setText("이름 : ");
			searchGenderField.setVisible(true);
			searchMaleRadio.setEnabled(true);
			searchMaleRadio.setVisible(true);
			searchFemaleRadio.setEnabled(true);
			searchFemaleRadio.setVisible(true);
			searchBothRadio.setEnabled(true);
			searchBothRadio.setVisible(true);
			searchBothRadio.setSelected(true);
		}else if(e.getSource() == searchBtn) {
			currList.clear();
			searchData();
			updateTableView();
		}else if(e.getSource() == addBtn) {
			addData();
			updateTableView();
		}else if(e.getSource() == departCombo) {
			if(departCombo.getSelectedItem().equals("직접 입력")){
				departInput.setVisible(true);
				departInput.setEnabled(true);
			}else {
				departInput.setText(null);
				departInput.setVisible(false);
				departInput.setEnabled(false);
			}
		}else if(e.getSource() == posiCombo) {
			if(posiCombo.getSelectedItem().equals("직접 입력")){
				posiInput.setVisible(true);
				posiInput.setEnabled(true);
			}else {
				posiInput.setText(null);
				posiInput.setVisible(false);
				posiInput.setEnabled(false);
			}
		}
	}

	private ArrayList<DtoEmployee> list;
	private ArrayList<DtoEmployee> currList;
	private int width, height, typeFlag;
	private DefaultTableModel model;
	private JTable table;
	private JPanel mainPan, searchPan, registerPan;
	private JLabel nameField, departmentField, genderField, dateEnterField, positionField, salaryField, searchField, searchGenderField;
	private JTextField nameInput, departInput, dateInput, posiInput, salaryInput, searchInput;
	private JTabbedPane regiSearchJtp;
	private ButtonGroup genderGroup, searchGenderGroup, searchTypeGroup;
	private JRadioButton maleRadio, femaleRadio, searchMaleRadio, searchFemaleRadio, searchBothRadio, searchIdRadio, searchNameRadio;
	private JComboBox<String> departCombo, posiCombo;
	private JButton searchBtn, addBtn;
	private JScrollPane scroll;
	private ModifyDialog md;
	private String[] departList = {"FrontOffice", "Restaurant", "HumanResource", "RoomManagement", "직접 입력"};
	private String[] posiList = {"Manager", "HouseKeeper", "Cashier", "Clerk", "직접 입력"};
	
	public ViewEmployee(ArrayList<DtoEmployee> list, int width, int height) {
		this.list = list;
		currList = new ArrayList<>();
		this.width = width-30;
		this.height = height;
		makePan();
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
		
		regiSearchJtp.addTab("조회", null, searchPan, "직원을 조회합니다");
		regiSearchJtp.addTab("등록", null, registerPan, "직원을 등록합니다");
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
						DtoEmployee temp = null;
						for(int i =0; i<list.size();i++) {
							if(list.get(i).getEmployeeId() == id) {
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
	
	class ModifyDialog extends JDialog{
		JLabel id, name, name2, depart, posi, date, sal, info;
		JTextField idInput, nameInput, nameInput2, dateInput, salInput, dInput, pInput;
		JComboBox<String> departC, posiC;
	    JButton modiButton=new JButton("수정");
	    JButton cancelButton = new JButton("취소");
	    JButton cancel2Button = new JButton("취소");
	    JButton deleteButton = new JButton("삭제");
	    ModifyDialog(JFrame frame, String title, DtoEmployee dto){
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
	        depart 		= new JLabel("부서  :");
	        posi 		= new JLabel("직급  :");
	        date 		= new JLabel("입사일:");
	        sal  		= new JLabel("급여  :");
	        info		= new JLabel("<html><div style='text-align:center'>'ID'와 '이름'을<br/>정확히 기입해주세요.</div></html>");
	        idInput 	= new JTextField();
	        nameInput 	= new JTextField();
	        nameInput2 	= new JTextField();
	        dInput = new JTextField();
	        pInput   = new JTextField();
	        departC = new JComboBox<>();
	        for(int i = 0 ; i<departList.length;i++) {
	        	departC.addItem(departList[i]);
	        }
	        posiC = new JComboBox<>();
	        for(int i = 0 ; i<posiList.length;i++) {
	        	posiC.addItem(posiList[i]);
	        }
	        dateInput 	= new JTextField();
	        salInput 	= new JTextField();
	        
	        
	        int x = tabPan.getWidth()/2 - 90;
	        int y = 30;
	        int w = 50;
	        int inputW = 100;
	        int h = 20;
	        int weight = 50;
	        
	        id.setBounds(x, y, w, h);
	        name.setBounds(x, y, w, h);
	        idInput.setBounds(id.getX()+id.getWidth()+10, y, inputW, h);
	        nameInput.setBounds(name.getX()+name.getWidth()+10, y, inputW, h);
	        
	        depart.setBounds(x, y+=weight, w, h);
	        name2.setBounds(x, y, w, h);
	        departC.setBounds(depart.getX()+depart.getWidth()+10, y, inputW, h);
	        nameInput2.setBounds(name2.getX()+name2.getWidth()+10, y, inputW, h);
	        departC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == departC) {
						if(departC.getSelectedItem().equals("직접 입력")){
							dInput.setVisible(true);
							dInput.setEnabled(true);
						}else {
							dInput.setText(null);
							dInput.setVisible(false);
							dInput.setEnabled(false);
						}
					}
				}
			});
	        dInput.setBounds(depart.getX()+depart.getWidth()+10, y+25, inputW, h);
	        dInput.setEnabled(false);
	        dInput.setVisible(false);
	        
	        posi.setBounds(x, y+=weight, w, h);
	        posiC.setBounds(posi.getX()+posi.getWidth()+10, y, inputW, h);
	        posiC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == posiC) {
						if(posiC.getSelectedItem().equals("직접 입력")){
							pInput.setVisible(true);
							pInput.setEnabled(true);
						}else {
							pInput.setText(null);
							pInput.setVisible(false);
							pInput.setEnabled(false);
						}
					}
				}
			});
	        pInput.setBounds(posi.getX()+posi.getWidth()+10, y+25, inputW, h);
	        pInput.setEnabled(false);
	        pInput.setVisible(false);
	        
	        date.setBounds(x, y+=weight, w, h);
	        info.setBounds(x+30, y, w+inputW, h*3);
	        dateInput.setBounds(date.getX()+date.getWidth()+10, y, inputW, h);
	        
	        sal.setBounds(x, y+=weight, w, h);
	        salInput.setBounds(sal.getX()+sal.getWidth()+10, y, inputW, h);
	        
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
	       
	        nameInput.setText(dto.getName());
	        departC.setSelectedItem(dto.getDepartment());
	        posiC.setSelectedItem(dto.getPosition());
	        dateInput.setText(dto.getDateEnter()+"");
	        salInput.setText(dto.getSalary()+"");
	        dateInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = dateInput.getText().length();
					if(len < 10) {
						if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
							return;
						}
						if(!Character.isDigit(c)) {
							e.consume();
							return;
						}
						if(len == 4 || len == 7) {
							dateInput.setText(dateInput.getText() + "-");
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
	        idInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}
				@Override
				public void keyReleased(KeyEvent e) {
					if(idInput.getText().equals(String.valueOf(dto.getEmployeeId())) && nameInput2.getText().equals(dto.getName())) {
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
					if(idInput.getText().equals(String.valueOf(dto.getEmployeeId())) && nameInput2.getText().equals(dto.getName())) {
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
	            	if(nameInput.getText().equals(dto.getName()) && departC.getSelectedItem().equals(dto.getDepartment()) 
	            			&& posiC.getSelectedItem().equals(dto.getPosition()) && dateInput.getText().equals(dto.getDateEnter()+"") 
	            			&& salInput.getText().equals(dto.getSalary()+"")){
	            		JOptionPane.showConfirmDialog(null, "수정사항이 없습니다.", "확인", JOptionPane.PLAIN_MESSAGE);
	            		System.out.println("no change");
	            		System.out.println();
	            		dispose();
	            	}else {
	            		String tempName = null;
	            		if(!nameInput.getText().isEmpty()) {
	            			tempName = nameInput.getText();
	            		}
	            		
	            		String tempDepart = null;
	            		if(departC.getSelectedItem() == "직접 입력") {
	            			if(!dInput.getText().isEmpty())
	            				tempDepart = dInput.getText();
	            		}else {
	            			tempDepart = departC.getSelectedItem().toString();
	            		}
	            		
	            		String tempPosi = null;
	            		if(posiC.getSelectedItem() == "직접 입력") {
	            			if(!pInput.getText().isEmpty())
	            				tempPosi = pInput.getText();
	            		}else {
	            			tempPosi = posiC.getSelectedItem().toString();
	            		}
	            		Date tempDate = null;
	            		try {
	            			if(!dateInput.getText().isEmpty() && (Date.valueOf(dateInput.getText()).getTime() <= new Date(new java.util.Date().getTime()).getTime())) {
	            				tempDate = Date.valueOf(dateInput.getText());
	            			}
	            		}catch (Exception e1) {
	            		}
	            		int tempSalary=-1;
	            		if(!salInput.getText().isEmpty()) {
	            			tempSalary = Integer.parseInt(salInput.getText());
	            		}
	            		if(tempName == null || tempDepart == null || tempDate == null || tempPosi == null || tempSalary == -1) {
	            			JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
	            			return;
	            		}else {
	            			int result = JOptionPane.showConfirmDialog(null, "이름    : " + tempName 
	            															+"\n부서    : " + tempDepart
	            															+"\n직급    : " + tempPosi
	            															+"\n입사일  : " + tempDate
	            															+"\n급여    : " + tempSalary, "확인", JOptionPane.OK_CANCEL_OPTION);
	                        if (result == 0) { //OK=0 , Cancel=2 리턴
	                    		dto.setName(tempName);
	                    		dto.setDepartment(tempDepart);
	                    		dto.setPosition(tempPosi);
	                    		dto.setDateEnter(tempDate);
	                    		dto.setSalary(tempSalary);
	                    		for(int i = 0; i < list.size();i++) {
	    							if(list.get(i).getEmployeeId() == dto.getEmployeeId()) {
	    								list.set(i, dto);
	    								break;
	    							}
	    						}
	                    		pbl2.MainActivity.sql.query("UPDATE TBLEMPLOYEE set ENAME = '"+dto.getName()+"', EDEPARTMENT = '"+ dto.getDepartment()
	                    		+"', EDATEENTER = '" + dto.getDateEnter() + "', EPOSITION = '" + dto.getPosition() +"', ESALARY = " + dto.getSalary() + "where EEMPLOYEEID = " + dto.getEmployeeId());
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
							if(list.get(i).getEmployeeId() == dto.getEmployeeId()) {
								list.remove(i);
								break;
							}
						}
						pbl2.MainActivity.sql.query("delete from tblEmployee where eemployeeID = "+dto.getEmployeeId());
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
	        p1.add(depart);
	        p1.add(posi);
	        p1.add(date);
	        p1.add(sal);
	        p1.add(nameInput);
	        p1.add(departC);
	        p1.add(dInput);
	        p1.add(posiC);
	        p1.add(pInput);
	        p1.add(dateInput);
	        p1.add(salInput);
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
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		searchGenderField = new JLabel("성별 : ");
		searchMaleRadio = new JRadioButton("남");
		searchFemaleRadio = new JRadioButton("여");
		searchBothRadio = new JRadioButton("무관");
		searchGenderGroup = new ButtonGroup();
		searchGenderGroup.add(searchMaleRadio);
		searchGenderGroup.add(searchFemaleRadio);
		searchGenderGroup.add(searchBothRadio);
		searchBothRadio.setSelected(true);
		
		searchBtn = new JButton("조회");
				
		int totalFieldWidth = 260;
		int x = regiSearchJtp.getWidth()/2-totalFieldWidth/2;
		int space = 10;
		int y = 10;
		int fieldW = 50;
		int inputW = 200;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/7);
		searchPan.setLayout(null);
		
		searchIdRadio.setBounds(x, y, fieldW+80, h);
		searchNameRadio.setBounds(searchIdRadio.getX()+searchIdRadio.getWidth()+space, y, fieldW+80, h);
		
		searchField.setBounds(x, y+=weight, fieldW, h);
		searchInput.setBounds(searchField.getX()+searchField.getWidth()+space, y, inputW, h);
		
		searchGenderField.setBounds(x, y+=weight, fieldW, h);
		searchMaleRadio.setBounds(searchGenderField.getX()+searchGenderField.getWidth()+space, y, fieldW, h);
		searchFemaleRadio.setBounds(searchMaleRadio.getX()+searchMaleRadio.getWidth()+space, y, fieldW, h);
		searchBothRadio.setBounds(searchFemaleRadio.getX()+searchFemaleRadio.getWidth()+space, y, fieldW+10, h);
		
		searchBtn.setBounds(regiSearchJtp.getWidth()/2-totalFieldWidth/6, y+=weight*2, totalFieldWidth/3, weight);
		searchBtn.addActionListener(this);
		
		searchPan.add(searchIdRadio);
		searchPan.add(searchNameRadio);
		searchPan.add(searchField);
		searchPan.add(searchInput);
		searchPan.add(searchGenderField);
		searchPan.add(searchMaleRadio);
		searchPan.add(searchFemaleRadio);
		searchPan.add(searchBothRadio);
		searchPan.add(searchBtn);
		searchIdRadio.doClick();
	}
	
	private void makeRegisPan() {
		nameField = new JLabel("이름 : ");
		nameInput = new JTextField();
		
		genderField = new JLabel("성별 : ");
		maleRadio = new JRadioButton("남");
		femaleRadio = new JRadioButton("여");
		genderGroup = new ButtonGroup();
		genderGroup.add(maleRadio);
		genderGroup.add(femaleRadio);
		
		departmentField = new JLabel("부서 : ");
		departCombo = new JComboBox<>();
		for(int i = 0; i<departList.length;i++) {
			departCombo.addItem(departList[i]);
		}
		departInput = new JTextField();
		departInput.setVisible(false);
		
		
		positionField = new JLabel("직급 : ");
		posiCombo = new JComboBox<>();
		for(int i = 0; i<posiList.length;i++) {
			posiCombo.addItem(posiList[i]);
		}
				
		posiInput = new JTextField();
		posiInput.setVisible(false);
		
		
		dateEnterField = new JLabel("입사일 : ");
		dateInput = new JTextField();
		
		salaryField = new JLabel("급여 : ");
		salaryInput = new JTextField();
		
		addBtn = new JButton("등록");
		
		departCombo.addActionListener(this);
		posiCombo.addActionListener(this);
		
		int totalFieldWidth = 260;
		int x = regiSearchJtp.getWidth()/2-totalFieldWidth/2;
		int space = 10;
		int y = 10;
		int fieldW = 50;
		int inputW = 200;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/9);
		registerPan.setLayout(null);
		nameField.setBounds(x, y, fieldW, h);
		nameInput.setBounds(nameField.getX()+nameField.getWidth()+space, y, inputW, h);
		
		genderField.setBounds(x, y+=weight, fieldW, h);
		maleRadio.setBounds(genderField.getX()+genderField.getWidth()+space, y, fieldW, h);
		femaleRadio.setBounds(maleRadio.getX()+maleRadio.getWidth()+space, y, fieldW, h);
		
		departmentField.setBounds(x, y+=weight, fieldW, h);
		departCombo.setBounds(departmentField.getX()+departmentField.getWidth()+space, y, inputW/2, h);
		departInput.setBounds(departCombo.getX()+departCombo.getWidth()+space, y, inputW/2-space, h);
		
		positionField.setBounds(x, y+=weight, fieldW, h);
		posiCombo.setBounds(positionField.getX()+positionField.getWidth()+space, y, inputW/2, h);
		posiInput.setBounds(posiCombo.getX()+posiCombo.getWidth()+space, y, inputW/2-space, h);
		
		dateEnterField.setBounds(x, y+=weight, fieldW, h);
		dateInput.setBounds(dateEnterField.getX()+dateEnterField.getWidth()+space, y, inputW, h);
		dateInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				int len = dateInput.getText().length();
				if(len < 10) {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
						return;
					}
					if(!Character.isDigit(c)) {
						e.consume();
						return;
					}
					if(len == 4 || len == 7) {
						dateInput.setText(dateInput.getText() + "-");
					}
				}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
					e.consume();
					return;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		salaryField.setBounds(x, y+=weight, fieldW, h);
		salaryInput.setBounds(salaryField.getX()+salaryField.getWidth()+space, y, inputW, h);
		
		addBtn.setBounds(regiSearchJtp.getWidth()/2-totalFieldWidth/6, y+=weight, totalFieldWidth/3, weight);
		addBtn.addActionListener(this);
		
		registerPan.add(nameField);
		registerPan.add(nameInput);
		registerPan.add(genderField);
		registerPan.add(maleRadio);
		registerPan.add(femaleRadio);
		registerPan.add(departmentField);
		registerPan.add(departCombo);
		registerPan.add(departInput);
		registerPan.add(positionField);
		registerPan.add(posiCombo);
		registerPan.add(posiInput);
		registerPan.add(dateEnterField);
		registerPan.add(dateInput);
		registerPan.add(salaryField);
		registerPan.add(salaryInput);
		registerPan.add(addBtn);
	}
	
	private void makeTableModel() {
		String[] colName = {"ID", "이름", "성별", "부서", "직급", "입사일", "급여"};
		model = new DefaultTableModel(colName, 0);
		
	}
	
	private void removeData(int idx) {
		model.removeRow(idx);
		updateTableView();
	}
	
	private void addData() {
		ResultSet rs = pbl2.MainActivity.sql.query("select max(eemployeeid) from tblemployee");
		int id= 0;
		try {
			rs.next();
			id = rs.getInt(1)+1;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		int hotelId = 200;
		
		String name = null;
		if(!nameInput.getText().isEmpty()) {
			name = nameInput.getText();
		}
		
		String depart = null;
		if(departCombo.getSelectedItem() == "직접 입력") {
			if(!departInput.getText().isEmpty())
				depart = departInput.getText();
		}else {
			depart = departCombo.getSelectedItem().toString();
		}
		
		String gender = null;
		if(maleRadio.isSelected()) {
			gender = "M";
		}else if(femaleRadio.isSelected()) {
			gender = "F";
		}
		
		Date date = null;
		try {
			if(!dateInput.getText().isEmpty() && (Date.valueOf(dateInput.getText()).getTime() <= new Date(new java.util.Date().getTime()).getTime())) {
				date = Date.valueOf(dateInput.getText());
			}
		}catch (Exception e) {
		}
		
		String position = null;
		if(posiCombo.getSelectedItem() == "직접 입력") {
			if(!posiInput.getText().isEmpty())
				position = posiInput.getText();
		}else {
			position = posiCombo.getSelectedItem().toString();
		}
		
		int salary=-1;
		if(!salaryInput.getText().isEmpty()) {
			salary = Integer.parseInt(salaryInput.getText());
		}
		
		if(name == null || depart == null || gender == null || date == null || position == null || salary == -1) {
			JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.","확인", JOptionPane.WARNING_MESSAGE);
			return;
		}else {
			int result = JOptionPane.showConfirmDialog(null, "이름    : " + name 
															+"\n성별    : " + gender
															+"\n부서    : " + depart
															+"\n직급    : " + position
															+"\n입사일  : " + date
															+"\n급여    : " + salary, "확인", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) { //OK=0 , Cancel=2 리턴
        		DtoEmployee dto = new DtoEmployee(id, hotelId, name, depart, gender, date, position, salary);
        		String query = "INSERT INTO tblEmployee (eEmployeeID, eHotelID, eName, eDepartment, eGender, eDateEnter, ePosition, eSalary) "
        				+ "VALUES ("
        				+ id + ", 200, '"+name+"', '"+depart+"', '"+gender+"', '"+date+"', '"+position+"',"+ salary
        				+ ")";
        		pbl2.MainActivity.sql.query(query);
        		list.add(dto);
        		currList.add(dto);
        		updateTableView();
            }
		}
	}
	
	private void addDataAtModel(DtoEmployee dto) {
		String[] data = new String[7];
		data[0] = String.valueOf(dto.getEmployeeId());
		data[1] = dto.getName();
		data[2] = dto.getGender();
		data[3] = dto.getDepartment();
		data[4] = dto.getPosition();
		data[5] = String.valueOf(dto.getDateEnter());
		data[6] = String.valueOf(dto.getSalary());
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
	
	private void searchData() {
		int id = -1;
		String name = null;
		String gender = null;
		if(typeFlag == 0) {
			if(!searchInput.getText().isEmpty()) {
				id = Integer.valueOf(searchInput.getText());
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i).getEmployeeId() == id) {
						currList.add(list.get(i));
					}
				}
			}
		}else {
			if(!searchInput.getText().isEmpty()) {
				name = searchInput.getText();
			}
			if(searchMaleRadio.isSelected()) {
				gender = "M";
			}else if(searchFemaleRadio.isSelected()) {
				gender = "F";
			}else {
				gender = null;
			}
			DtoEmployee tempDto = null;
			for(int i = 0; i < list.size(); i++) {
				tempDto = list.get(i);
				if(name == null) {
					if(gender == null) {
						currList.add(tempDto);
					}else if(tempDto.getGender().equals(gender)) {
						currList.add(tempDto);
					}
				}else if(tempDto.getName().equals(name)) {
					if(gender == null) {
						currList.add(tempDto);
					}else if(tempDto.getGender().equals(gender)) {
						currList.add(tempDto);
					}
				}
			}
		
		}
		updateTableView();
	}
	public JPanel getPanel() {
		return mainPan;
	}
}