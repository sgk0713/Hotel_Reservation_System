package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import pbl2.SqlHelper;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoCustomer;
import pbl2.dto.DtoRoom;

public class ViewReservation implements ActionListener, KeyListener, MouseListener{
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == table) {
			if(table.getSelectedRowCount() == 1) {
				detailNameInput.setText(null);
				detailPhoneInput.setText(null);
				adultInput.setText(null);
				childInput.setText(null);
				detailNameInput.setEnabled(true);
				detailPhoneInput.setEnabled(true);
				adultInput.setEnabled(true);
				childInput.setEnabled(true);
				detailNameInput.setFocusable(true);
				detailNameInput.requestFocus();
			}else {
				detailNameInput.setText("객실선택 우선");
				detailPhoneInput.setText("객실선택 우선");
				adultInput.setText(null);
				childInput.setText(null);
				detailNameInput.setEnabled(false);
				detailPhoneInput.setEnabled(false);
				adultInput.setEnabled(false);
				childInput.setEnabled(false);
				detailNameInput.setFocusable(false);
			}
		}else if(e.getSource() == table2) {
			if(table2.getSelectedRowCount() == 1) {
				String tempRoomNum = (String) table2.getValueAt(table2.getSelectedRow(), 0);
				String tempName = (String) table2.getValueAt(table2.getSelectedRow(), 1);
				String tempEnter = (String) table2.getValueAt(table2.getSelectedRow(), 7);
				String tempState = (String) table2.getValueAt(table2.getSelectedRow(), 9);
				tempState = tempState.toUpperCase();
				detailRoomNum.setText(tempRoomNum);
				detailName.setText(tempName);
				String temp = "select cphone from tblbookedroom, tblroom, tblcustomer where brroomid = rroomid and brcustomerid=ccustomerid and brstate != 'CheckOut' and upper(cname) = upper('"+tempName+"') and rroomnumber = "+tempRoomNum+" and brdateenter = '"+tempEnter.substring(0, 10)+"'";
				try {
					rs = sql.query(temp);
					rs.next();
					detailPhone.setText(rs.getString(1));
				}catch (Exception e1) {
					e1.printStackTrace();
				}
				if(tempState.equals("CHECKIN")) {
					checkInBtn.setEnabled(false);
					checkOutBtn.setEnabled(true);
					noShowBtn.setEnabled(false);
				}else if(tempState.equals("BOOKING")){
					checkInBtn.setEnabled(true);
					checkOutBtn.setEnabled(false);
					noShowBtn.setEnabled(true);
				}				
			}else {
				detailRoomNum.setText(null);
				detailName.setText(null);
				detailPhone.setText(null);
				checkInBtn.setEnabled(false);
				checkOutBtn.setEnabled(false);
				noShowBtn.setEnabled(false);
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getSource() == dateEnterInput || e.getSource() == dateExitInput) {
			char c = e.getKeyChar();
			int len = ((JTextField)e.getSource()).getText().length();
			
			if(c == KeyEvent.VK_ENTER) {
				lookupBtn.doClick();
				return;
			}
			if(len < 10) {
				if(c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
					return;
				}
				if(!Character.isDigit(c)) {
					e.consume();
					return;
				}
				if(len == 4 || len == 7) {
					((JTextField)e.getSource()).setText(((JTextField)e.getSource()).getText() + "-");
				}
			}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
				e.consume();
				return;
			}
		}else if(e.getSource() == detailPhoneInput || e.getSource() == searchPhoneInput) {
			char c = e.getKeyChar();
			int len = ((JTextField)e.getSource()).getText().length();
			if(len < 13) {
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
					return;
				}
				if(!Character.isDigit(c)) {
					e.consume();
					return;
				}
				if(len == 3 || len == 8) {
					((JTextField)e.getSource()).setText(((JTextField)e.getSource()).getText() + "-");
				}
			}else if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
				e.consume();
				return;
			}
		}else if(e.getSource() == adultInput || e.getSource() == childInput) {
			char c = e.getKeyChar();
			int len = ((JTextField)e.getSource()).getText().length();
			if(len < 2) {
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
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == detailNameInput || e.getSource() == detailPhoneInput || e.getSource() == adultInput || e.getSource() == childInput) {
			if(detailNameInput.getText().length() > 0 && detailPhoneInput.getText().length() > 0 && adultInput.getText().length() > 0 && childInput.getText().length() >0) {
				regisBtn.setEnabled(true);
			}else {
				regisBtn.setEnabled(false);
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == lookupBtn) {
			detailNameInput.setText("객실선택 우선");
			detailPhoneInput.setText("객실선택 우선");
			adultInput.setText(null);
			childInput.setText(null);
			detailNameInput.setEnabled(false);
			detailPhoneInput.setEnabled(false);
			adultInput.setEnabled(false);
			childInput.setEnabled(false);
			detailNameInput.setFocusable(false);
			regisBtn.setEnabled(false);
			showRoomList();
		}else if(e.getSource() == searchBtn) {
			detailRoomNum.setText(null);
			detailName.setText(null);
			detailPhone.setText(null);
			checkInBtn.setEnabled(false);
			checkOutBtn.setEnabled(false);
			noShowBtn.setEnabled(false);
			showBookedRoomList();
		}else if(e.getSource() == regisBtn) {
			makeRoomBooked();
		}else if(e.getSource() == checkInBtn || e.getSource() == noShowBtn || e.getSource() == checkOutBtn) {
			String tempRoomNum = (String) table2.getValueAt(table2.getSelectedRow(), 0);
			String tempName = (String) table2.getValueAt(table2.getSelectedRow(), 1);
			String tempEnter = (String) table2.getValueAt(table2.getSelectedRow(), 7);
			String tempState = (String) table2.getValueAt(table2.getSelectedRow(), 9);
			tempState = tempState.toUpperCase();
			detailRoomNum.setText(tempRoomNum);
			detailName.setText(tempName);
			try {
				String temp = "select brbookid from tblbookedroom, tblroom, tblcustomer where brroomid = rroomid and brcustomerid=ccustomerid and brstate != 'CheckOut' and brstate != 'NoShow' and upper(cname) = upper('"+tempName+"') and rroomnumber = "+tempRoomNum+" and brdateenter = '"+tempEnter.substring(0, 10)+"'";
				rs = sql.query(temp);
				rs.next();
				int bookId = rs.getInt(1);
				
				String state = "";
				if(e.getSource() == checkInBtn) {
					state = "CheckIn";
				}else if(e.getSource() == checkOutBtn) {
					state = "CheckOut";
				}else if(e.getSource() == noShowBtn) {
					state = "NoShow";
				}
				
				temp = "update tblbookedroom set BRSTATE = '"+state+"' where brbookid = "+bookId;
				sql.query(temp);
				for(int i =0; i<bookedRoomList.size();i++) {
					if(bookedRoomList.get(i).getBookId() == bookId) {
						bookedRoomList.get(i).setState(state);
					}
				}
				pbl2.MainActivity.makeFirstPan();
				searchBtn.doClick();
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if(e.getSource() == resetBtn) {
			searchPhoneInput.setText(null);
			searchNameInput.setText(null);
			detailRoomNum.setText(null);
			detailName.setText(null);
			detailPhone.setText(null);
			checkInBtn.setEnabled(false);
			checkOutBtn.setEnabled(false);
			noShowBtn.setEnabled(false);
			clearModel();
			table2.setModel(model2);
		}
		
	}
	private ArrayList<DtoRoom> currRoomList;

	private ArrayList<DtoRoom> roomList;
	private ArrayList<DtoCustomer> customerList;
	private ArrayList<DtoBookedRoom> bookedRoomList;
	
	private int width, height;
	private DefaultTableModel model, model2;
	private JTable table;
	static public JTable table2;
	private JPanel mainPan, searchPan, bookPan, detailPan, detailPan2;
	private JLabel dateField, intervalField, adultField, childField, eaField, ea2Field, viewField, roomTypeField, bedField
					, searchNameField, searchPhoneField
					, detailNameField, detailPhoneField 
					, detailRoomNumTitle, detailRoomNum, detailNameTitle, detailName, detailPhoneTitle, detailPhone, detailReceiptTitle, detailReceipt, detailTotalPriceTitle, detailTotalPrice;
	private JTextField dateEnterInput, dateExitInput, adultInput, childInput
					, detailNameInput, detailPhoneInput;
	static public JTabbedPane regiSearchJtp;
	private String[] roomType = {"standard", "deluxe", "superior", "premium"};
	private String[] viewType = {"ocean", "city", "mountain"};
	private String[] bedType = {"single", "double", "twin", "triple"};
	private JCheckBox[] viewCheckBox;
	private JCheckBox[] bedCheckBox;
	private JCheckBox[] roomTypeCheckBox;
	private JButton lookupBtn, regisBtn, checkInBtn, checkOutBtn, noShowBtn, resetBtn;
	static public JButton searchBtn;
	static public JTextField searchNameInput, searchPhoneInput;
	private JScrollPane scroll, scroll2;
	private SqlHelper sql;
	private ResultSet rs;
	private Date dateEnter = null, dateExit = null;
	private java.util.Date currDate;
	
	public ViewReservation(ArrayList<DtoCustomer> customerList, ArrayList<DtoRoom> roomList, ArrayList<DtoBookedRoom> bookedRoomList, int width, int height) {
		this.customerList = customerList;
		this.roomList = roomList;
		this.bookedRoomList = bookedRoomList;
		currRoomList = new ArrayList<>();
		
		this.width = width-30;
		this.height = height;
		sql = pbl2.MainActivity.sql;
		makePan();
	}
	
	private void makePan() {
		mainPan = new JPanel();
		mainPan.setLayout(null);
		mainPan.setSize(width, height);
		regiSearchJtp = new JTabbedPane();
		
		int searchAreaWidth = (int)(width*0.25);
		int resultAreaWidth = (int)(width*0.60);
		regiSearchJtp.setBounds(0, 0, searchAreaWidth, height);
		
		bookPan = new JPanel();
		makeBookPan();
		
		searchPan = new JPanel();
		makeSearchPan();
		
		regiSearchJtp.addTab("예약", null, bookPan, "객실을 예약합니다");
		regiSearchJtp.addTab("조회", null, searchPan, "객실을 조회합니다");
		regiSearchJtp.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				clearModel();
				if(regiSearchJtp.getSelectedIndex() == 0) {
					scroll.setViewportView(table);
					scroll2.setViewportView(detailPan);
				}else {
					searchBtn.doClick();
					scroll.setViewportView(table2);
					scroll2.setViewportView(detailPan2);
				}
			}
		});
		makeTableModel();
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table2 = new JTable(model2);
		table2.setDefaultEditor(Object.class, null);
		table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scroll = new JScrollPane();
		scroll.setViewportView(table);
		scroll.setBounds(searchAreaWidth, 10, resultAreaWidth, height-20);
		scroll2 = new JScrollPane();
		scroll2.setBounds(searchAreaWidth+resultAreaWidth, 10, width-searchAreaWidth-resultAreaWidth, height-20);
		
		detailPan = new JPanel();
		makeDetailPan();
		detailPan2 = new JPanel();		
		makeDetailPan2();
		scroll2.setViewportView(detailPan);
		
		table.addMouseListener(this);
		table2.addMouseListener(this);
		table2.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(table2.getSelectedRowCount() != 1) {
					detailRoomNum.setText(null);
					detailName.setText(null);
					detailPhone.setText(null);
					checkInBtn.setEnabled(false);
					checkOutBtn.setEnabled(false);
					noShowBtn.setEnabled(false);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(table2.getSelectedRowCount() == 1) {
					String tempRoomNum = (String) table2.getValueAt(table2.getSelectedRow(), 0);
					String tempName = (String) table2.getValueAt(table2.getSelectedRow(), 1);
					String tempEnter = (String) table2.getValueAt(table2.getSelectedRow(), 7);
					String tempState = (String) table2.getValueAt(table2.getSelectedRow(), 9);
					tempState = tempState.toUpperCase();
					detailRoomNum.setText(tempRoomNum);
					detailName.setText(tempName);
					String temp = "select cphone from tblbookedroom, tblroom, tblcustomer where brroomid = rroomid and brcustomerid=ccustomerid and brstate != 'CheckOut' and upper(cname) = upper('"+tempName+"') and rroomnumber = "+tempRoomNum+" and brdateenter = '"+tempEnter.substring(0, 10)+"'";
					try {
						rs = sql.query(temp);
						rs.next();
						detailPhone.setText(rs.getString(1));
					}catch (Exception e1) {
						e1.printStackTrace();
					}
					if(tempState.equals("CHECKIN")) {
						checkInBtn.setEnabled(false);
						checkOutBtn.setEnabled(true);
						noShowBtn.setEnabled(false);
					}else if(tempState.equals("BOOKING")){
						checkInBtn.setEnabled(true);
						checkOutBtn.setEnabled(false);
						noShowBtn.setEnabled(true);
					}				
				}else {
					detailRoomNum.setText(null);
					detailName.setText(null);
					detailPhone.setText(null);
					checkInBtn.setEnabled(false);
					checkOutBtn.setEnabled(false);
					noShowBtn.setEnabled(false);
				}
			}
		});
		
		mainPan.add(regiSearchJtp);
		mainPan.add(scroll, BorderLayout.CENTER);
		mainPan.add(scroll2);
	}
	
	private void makeSearchPan() {
		searchNameField = new JLabel("예약자 이름");
		searchNameInput = new JTextField();
		
		searchPhoneField = new JLabel("전화번호");
		searchPhoneInput = new JTextField();
		
		searchBtn = new JButton("조회");
		resetBtn = new JButton("초기화");

		int x = 20;
		int space = 10;
		int y = 10;
//		int fieldW = 50;
		int inputW = 150;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/22);
		searchPan.setLayout(null);
		searchNameField.setBounds(x, y, inputW, h);
		searchNameInput.setBounds(x+space, y+=weight, inputW, h);
		searchPhoneField.setBounds(x, y+=weight, inputW, h);
		searchPhoneInput.setBounds(x+space, y+=weight, inputW, h);
		searchPhoneInput.addKeyListener(this);
		searchBtn.setBounds((regiSearchJtp.getWidth()-20)/2-inputW/2, y+=weight*2, inputW, weight+h);
		resetBtn.setBounds((regiSearchJtp.getWidth()-20)/2-inputW/2, y+=weight*2, inputW, weight+h);
		searchBtn.addActionListener(this);
		resetBtn.addActionListener(this);
		
		searchPan.add(searchNameField);
		searchPan.add(searchNameInput);
		searchPan.add(searchPhoneField);
		searchPan.add(searchPhoneInput);
		searchPan.add(searchBtn);
		searchPan.add(resetBtn);
		
	}
	
	private void makeBookPan() {
		dateField = new JLabel("날짜");
		dateEnterInput = new JTextField();
		intervalField = new JLabel("~");
		dateExitInput = new JTextField();
		
		roomTypeField = new JLabel("객실 타입");
		viewField = new JLabel("뷰 타입");
		bedField = new JLabel("침대 타입");
	
		roomTypeCheckBox = new JCheckBox[roomType.length];
		viewCheckBox = new JCheckBox[viewType.length];
		bedCheckBox = new JCheckBox[bedType.length];
		for(int i = 0; i< roomType.length; i++) {
			roomTypeCheckBox[i] = new JCheckBox(roomType[i]);
		}
		for(int i = 0; i< viewType.length; i++) {
			viewCheckBox[i] = new JCheckBox(viewType[i]);
		}
		for(int i = 0; i< bedType.length; i++) {
			bedCheckBox[i] = new JCheckBox(bedType[i]);
		}
		
		lookupBtn = new JButton("검색");
		
		dateEnterInput.addKeyListener(this);
		dateExitInput.addKeyListener(this);
		
		int x = 20;
		int space = 10;
		int y = 10;
		int fieldW = 50;
		int inputW = 150;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/24);
		bookPan.setLayout(null);
		dateField.setBounds(x, y, fieldW, h);
		dateEnterInput.setBounds(x+space, y+=weight, fieldW+50, h);
		intervalField.setBounds(dateEnterInput.getX()+space, y+=weight, 10, h);
		dateExitInput.setBounds(intervalField.getX()+intervalField.getWidth()+space, y, fieldW+50, h);
		
		roomTypeField.setBounds(x, y+=weight, fieldW, weight);
		for(int i = 0; i< roomTypeCheckBox.length;i++) {
			roomTypeCheckBox[i].setBounds(x+space, y+=weight, inputW, h);
		}
		viewField.setBounds(x, y+=weight, fieldW, weight);
		for(int i = 0; i< viewCheckBox.length;i++) {
			viewCheckBox[i].setBounds(x+space, y+=weight, inputW, h);
		}
		bedField.setBounds(x, y+=weight, fieldW, weight);
		for(int i = 0; i< bedCheckBox.length;i++) {
			bedCheckBox[i].setBounds(x+space, y+=weight, inputW, h);
		}
		
		lookupBtn.setBounds((regiSearchJtp.getWidth()-20)/2-inputW/2, y+=weight*2, inputW, weight+h);
		lookupBtn.addActionListener(this);
		
		bookPan.add(dateField);
		bookPan.add(dateEnterInput);
		bookPan.add(intervalField);
		bookPan.add(dateExitInput);
		bookPan.add(roomTypeField);
		for(int i = 0; i< roomTypeCheckBox.length;i++) {
			bookPan.add(roomTypeCheckBox[i]);
			roomTypeCheckBox[i].setSelected(true);
		}
		bookPan.add(viewField);
		for(int i = 0; i< viewCheckBox.length;i++) {
			bookPan.add(viewCheckBox[i]);
			viewCheckBox[i].setSelected(true);
		}
		bookPan.add(bedField);
		for(int i = 0; i< bedCheckBox.length;i++) {
			bookPan.add(bedCheckBox[i]);
			bedCheckBox[i].setSelected(true);
		}
		bookPan.add(lookupBtn);
	}
	
	private void makeDetailPan() {
		detailNameField = new JLabel("예약자 이름");
		detailNameInput = new JTextField();
		
		detailPhoneField = new JLabel("전화번호");
		detailPhoneInput = new JTextField();
		
		adultField = new JLabel("성인");
		adultInput = new JTextField();
		eaField = new JLabel("명");
		childField = new JLabel("어린이");
		childInput = new JTextField();
		ea2Field = new JLabel("명");
		
		regisBtn = new JButton("예약하기");
		
		detailNameInput.addKeyListener(this);
		detailPhoneInput.addKeyListener(this);
		adultInput.addKeyListener(this);
		childInput.addKeyListener(this);
		
		int x = 20;
		int space = 10;
		int y = 10;
		int fieldW = 50;
		int inputW = 100;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/20);
		detailPan.setLayout(null);
		detailNameField.setBounds(x, y, inputW, h);
		detailNameInput.setBounds(x+space, y+=weight, inputW, h);
		
		detailPhoneField.setBounds(x, y+=weight, fieldW, h);
		detailPhoneInput.setBounds(x+space, y+=weight, inputW, h);
		
		adultField.setBounds(x, y+=weight, fieldW, h);
		adultInput.setBounds(x+adultField.getWidth(), y, fieldW-20, h);
		eaField.setBounds(adultInput.getX()+adultInput.getWidth(), y, fieldW, h);
		childField.setBounds(x, y+=weight, fieldW, h);
		childInput.setBounds(x+childField.getWidth(), y, fieldW-20, h);
		ea2Field.setBounds(childInput.getX()+childInput.getWidth(), y, fieldW, h);
		regisBtn.setBounds(x-20+(scroll2.getWidth()-10)/2-inputW/2, y+=weight*2, inputW, weight+h);
		regisBtn.setEnabled(false);
		regisBtn.addActionListener(this);
		
		detailNameInput.setText("객실선택 우선");
		detailPhoneInput.setText("객실선택 우선");
		adultInput.setText(null);
		childInput.setText(null);
		detailNameInput.setEnabled(false);
		detailPhoneInput.setEnabled(false);
		adultInput.setEnabled(false);
		childInput.setEnabled(false);
		detailNameInput.setFocusable(false);
		
		
		detailPan.add(detailNameField);
		detailPan.add(detailNameInput);
		detailPan.add(detailPhoneField);
		detailPan.add(detailPhoneInput);
		detailPan.add(adultField);
		detailPan.add(adultInput);
		detailPan.add(eaField);
		detailPan.add(childField);
		detailPan.add(childInput);
		detailPan.add(ea2Field);
		detailPan.add(regisBtn);
	}
	
	private void makeDetailPan2() {
		detailRoomNumTitle = new JLabel("방 번호");
		detailRoomNum = new JLabel();
		detailNameTitle = new JLabel("고객명");
		detailName = new JLabel();
		detailPhoneTitle = new JLabel("고객 전화번호");
		detailPhone = new JLabel();
		
		checkInBtn = new JButton("CheckIn");
		checkOutBtn = new JButton("CheckOut");
		noShowBtn = new JButton("No-Show");
		
		int x = 20;
		int space = 10;
		int y = 10;
		int fieldW = 50;
		int inputW = 150;
		int h = 20;
		int weight = ((regiSearchJtp.getHeight())/20);
		detailPan2.setLayout(null);
		
		detailRoomNumTitle.setBounds(x, y, inputW, h);
		detailRoomNum.setBounds(detailRoomNumTitle.getX()+space, y+=weight, inputW, h);
		
		detailNameTitle.setBounds(x, y+=weight, inputW, h);
		detailName.setBounds(detailNameTitle.getX()+space, y+=weight, inputW, h);
		
		detailPhoneTitle.setBounds(x, y+=weight, inputW, h);
		detailPhone.setBounds(detailPhoneTitle.getX()+space, y+=weight, inputW, h);
		
		checkInBtn.setBounds(x-20+(scroll2.getWidth()-10)/2-inputW/2, y+=weight*2, inputW, weight+h);
		checkOutBtn.setBounds(checkInBtn.getX(), checkInBtn.getY()+checkInBtn.getHeight()+h, inputW, weight+h);
		noShowBtn.setBounds(checkInBtn.getX(), checkOutBtn.getY()+checkOutBtn.getHeight()+h, inputW, weight+h);
		
		checkInBtn.setEnabled(false);
		checkOutBtn.setEnabled(false);
		noShowBtn.setEnabled(false);
		
		checkInBtn.addActionListener(this);
		checkOutBtn.addActionListener(this);
		noShowBtn.addActionListener(this);
		
		detailPan2.add(detailRoomNumTitle);
		detailPan2.add(detailRoomNum);
		detailPan2.add(detailNameTitle);
		detailPan2.add(detailName);
		detailPan2.add(detailPhoneTitle);
		detailPan2.add(detailPhone);
		detailPan2.add(checkInBtn);
		detailPan2.add(checkOutBtn);
		detailPan2.add(noShowBtn);
	}

	
	private void showRoomList() {
		dateEnter = null;
		dateExit = null;
		try {
			java.util.Date date = new java.util.Date();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.set(Calendar.HOUR, 00);
			rightNow.set(Calendar.HOUR_OF_DAY, 00);
			rightNow.set(Calendar.MINUTE, 00);
			rightNow.set(Calendar.SECOND, 00);
			rightNow.set(Calendar.MILLISECOND, 00);
			currDate = rightNow.getTime();
			if(!dateEnterInput.getText().isEmpty()) {
				rightNow.setTime(Date.valueOf(dateEnterInput.getText()));
				rightNow.set(Calendar.HOUR, 00);
				rightNow.set(Calendar.HOUR_OF_DAY, 00);
				rightNow.set(Calendar.MINUTE, 00);
				rightNow.set(Calendar.SECOND, 00);
				rightNow.set(Calendar.MILLISECOND, 00);
		        java.util.Date typedEnterDate = rightNow.getTime();
		        if(typedEnterDate.after(currDate) || typedEnterDate.equals(currDate)) {
					dateEnter = Date.valueOf(dateEnterInput.getText());
				}
			}
			if(dateEnter != null && !dateExitInput.getText().isEmpty()) {
				rightNow.setTime(Date.valueOf(dateExitInput.getText()));
				rightNow.set(Calendar.HOUR, 00);
				rightNow.set(Calendar.HOUR_OF_DAY, 00);
				rightNow.set(Calendar.MINUTE, 00);
				rightNow.set(Calendar.SECOND, 00);
				rightNow.set(Calendar.MILLISECOND, 00);
		        java.util.Date typedExitDate = rightNow.getTime();
		        if(typedExitDate.after(dateEnter)) {
		        	dateExit = Date.valueOf(dateExitInput.getText());
				}
			}
		}catch (Exception e) {
		}
		
		if(dateEnter == null || dateExit == null) {
			JOptionPane.showMessageDialog(null, "날짜를 다시 확인해주세요.", "날짜 오류", JOptionPane.WARNING_MESSAGE);
		}else {
			String findRoomQuery = "select * from tblroom where rroomid in (select rroomid from tblroom minus select brroomid from tblbookedroom where (brdateenter <= "
					+ "'"+dateEnter+"'"
					+ " and brdateexit < "
					+ "'"+dateEnter+"' and brstate = 'Booking' or brstate = 'CheckIn') or "
					+ "(brdateenter < '"+dateExit+"' and brdateexit >= '"+dateExit+"' and brstate = 'Booking' or brstate = 'CheckIn'))";
			System.out.println(findRoomQuery);
			for(int i = 0 ; i< bedCheckBox.length; i++) {
				if(!bedCheckBox[i].isSelected()) {
					findRoomQuery += " and rbed != '" + bedCheckBox[i].getText()+"'";
				}
			}
			for(int i = 0 ; i< viewCheckBox.length; i++) {
				if(!viewCheckBox[i].isSelected()) {
					findRoomQuery += " and rviewtype != '" + viewCheckBox[i].getText()+"'";
				}
			}
			for(int i = 0 ; i< roomTypeCheckBox.length; i++) {
				if(!roomTypeCheckBox[i].isSelected()) {
					findRoomQuery += " and rroomtype != '" + roomTypeCheckBox[i].getText()+"'";
				}
			}
			try {
				rs = sql.query(findRoomQuery);
				currRoomList.clear();
				while(rs.next()) {
					currRoomList.add(new DtoRoom(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			updateTableView();
			
		}
	}
	
	private void makeRoomBooked() {
		String tempName = detailNameInput.getText();
		String tempPhone = detailPhoneInput.getText();
		String tempAdult = adultInput.getText();
		String tempChild = childInput.getText();
		
		try {
			for(int i = 0; i<customerList.size(); i++) {
				if(tempName.toUpperCase().equals(customerList.get(i).getName().toUpperCase())){
					if(tempPhone.equals(customerList.get(i).getPhone())) {
						rs = sql.query("select max(brbookid) from tblbookedroom");
						rs.next();
						int bookId = rs.getInt(1)+1;
						int tempRoomNum = Integer.parseInt((String)table.getValueAt(table.getSelectedRow(), 0));
						rs = sql.query("select rroomid, rfloor from tblroom where rroomnumber = "+tempRoomNum);
						rs.next();
						int roomId = rs.getInt(1);
						int floorNum = rs.getInt(2);
						
						if(tempAdult == null) {
							tempAdult = "0";
						}
						if(tempChild == null) {
							tempChild = "0";
						}
						if(Integer.parseInt(tempAdult) + Integer.parseInt(tempChild) < 1) {
							JOptionPane.showMessageDialog(null, "1명 이상 입실가능합니다.", "예약실패 메시지", JOptionPane.WARNING_MESSAGE);
							return;
						}
						
						String insertQuery = "INSERT INTO tblBookedRoom (brBookID,brRoomID,brCustomerID,brDateEnter,brDateExit,brAdult,brChildren,brHkID,brState) VALUES "
								+ "("+bookId+","+roomId+","+customerList.get(i).getCustomerId()+",to_date('"+dateEnter+"','YYYY-MM-DD'),to_date('"+dateExit+"','YYYY-MM-DD'),"+tempAdult+","+tempChild+",1003,'Booking')";
						sql.query(insertQuery);
						sql.commit();
						bookedRoomList.add(new DtoBookedRoom(bookId, roomId, customerList.get(i).getCustomerId(), dateEnter, dateExit, Integer.valueOf(tempAdult), Integer.valueOf(tempChild), 1003, "Booking"));
						pbl2.MainActivity.makeFirstPan();
						lookupBtn.doClick();
						JOptionPane.showMessageDialog(null, "예약이 정상적으로 완료되었습니다.", "예약완료 메시지", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			}
			JOptionPane.showMessageDialog(null, "회원정보가 없습니다.", "예약실패 메시지", JOptionPane.WARNING_MESSAGE);
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void showBookedRoomList() {
		clearModel();
		String name = null;
		String phone = null;
		if(!searchNameInput.getText().isEmpty()) {	//이름 입력 받음
			name = searchNameInput.getText().toUpperCase();
		}
		if (!searchPhoneInput.getText().isEmpty()) {	//전화번호 입력 받음
			phone = searchPhoneInput.getText();
		}
		String findBookedRoomQuery = "select "
				+ "rroomnumber, cname, bradult, brchildren, rroomtype, rviewtype, rbed, brdateenter, brdateexit, brstate "
				+ "from "
				+ "tblbookedroom, tblroom, tblcustomer "
				+ "where brroomid = rroomid and brcustomerid=ccustomerid and brstate != 'CheckOut' and brstate != 'NoShow'";
		
		if(name == null && phone == null) {
		}else if(name == null) {
			JOptionPane.showMessageDialog(null, "이름을 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
			return;
		}else if(phone == null) {
			JOptionPane.showMessageDialog(null, "전화번호를 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
			return;
		}else {
			findBookedRoomQuery += " and upper(cname) = '"+name+"' and cphone = '"+phone+"'";
		}
		
		rs = sql.query(findBookedRoomQuery);
		try {
			String[] temp = new String[10];
			while(rs.next()) {
				for(int i = 0;i< temp.length;i++) {
					temp[i] = rs.getString(i+1);
				}
				model2.addRow(temp);
			}
			table2.setModel(model2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void makeTableModel() {
		String[] colName = {"호실", "방 타입", "뷰 타입", "침대 타입", "층수", "가격"};
		model = new DefaultTableModel(colName, 0);
		String[] colName2 = {"호실", "예약자명", "어른", "어린이", "방 타입", "뷰 타입", "침대 타입", "입실 날짜", "퇴실 날짜", "예약 상태"};			
		model2 = new DefaultTableModel(colName2, 0);
	}
	
	
	private void addDataAtModel1(DtoRoom dto) {
		String[] data = new String[6];
		data[0] = String.valueOf(dto.getRoomNumber());
		data[1] = dto.getRoomType();
		data[2] = dto.getViewType();
		data[3] = dto.getBed();
		data[4] = String.valueOf(dto.getFloor());
		data[5] = String.valueOf(dto.getPrice());
		model.addRow(data);
	}
	
	private void clearModel() {
		for(int i = model.getRowCount()-1; i>=0; i--) {
			model.removeRow(i);
		}
		for(int i = model2.getRowCount()-1; i>=0; i--) {
			model2.removeRow(i);
		}
	}
	
	private void updateTableView() {
		clearModel();
		for(int i = 0; i< currRoomList.size();i++) {
			addDataAtModel1(currRoomList.get(i));
		}
		table.setModel(model);
	}
	
	public JPanel getPanel() {
		return mainPan;
	}
}
