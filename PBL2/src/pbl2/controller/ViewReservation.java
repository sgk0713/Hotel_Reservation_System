package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;


import pbl2.dto.DtoEmployee;
import pbl2.dto.DtoRoom;

public class ViewReservation implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == lookupBtn) {
			showRoomList();
		}else if(e.getSource() == searchBtn) {
			showBookedRoomList();
		}
	}

	private ArrayList<DtoRoom> currList;
	private int width, height;
	private DefaultTableModel model, model2;
	private JTable table, table2;
	private JPanel mainPan, searchPan, bookPan, detailPan, detailPan2;
	private JLabel dateField, intervalField, adultField, childField, eaField, ea2Field, viewField, roomTypeField, bedFiled
					, searchNameField, searchPhoneField
					, detailNameField, detailPhoneField
					, detail2NameField, detail;
	private JTextField dateEnterInput, dateExitInput, adultInput, childInput
					, searchNameInput, searchPhoneInput
					, detailNameInput, detailPhoneInput;
	private JTabbedPane regiSearchJtp;
	private String[] roomType = {"스탠다드", "디럭스", "스위트"};
	private String[] viewType = {"오션", "시티", "마운틴"};
	private String[] bedType = {"싱글", "더블", "트윈", "트리플"};
	private JCheckBox[] viewCheckBox;
	private JCheckBox[] bedCheckBox;
	private JCheckBox[] roomTypeCheckBox;
	private JButton lookupBtn, searchBtn, regisBtn, checkInBtn, checkOutBtn;
	private JScrollPane scroll, scroll2;
	
	public ViewReservation(int width, int height) {
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
				if(regiSearchJtp.getSelectedIndex() == 0) {
					scroll.setViewportView(table);
				}else {
					scroll.setViewportView(table2);
				}
				clearTableView();
			}
		});
		makeTableModel();
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);
		table2 = new JTable(model2);
		table2.setDefaultEditor(Object.class, null);
		
		scroll = new JScrollPane();
		scroll.setViewportView(table);
		scroll.setBounds(searchAreaWidth, 10, resultAreaWidth, height-20);
		
		detailPan = new JPanel();
		detailPan2 = new JPanel();
		scroll2 = new JScrollPane();
		scroll2.setBounds(searchAreaWidth+resultAreaWidth, 10, width-searchAreaWidth-resultAreaWidth, height-20);
		makeDetailPan();
		makeDetailPan2();
		scroll2.setViewportView(detailPan);
		
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
		searchBtn.setBounds((regiSearchJtp.getWidth()-20)/2-inputW/2, y+=weight*2, inputW, weight+h);
		searchBtn.addActionListener(this);
		
		searchPan.add(searchNameField);
		searchPan.add(searchNameInput);
		searchPan.add(searchPhoneField);
		searchPan.add(searchPhoneInput);
		searchPan.add(searchBtn);
		
	}
	
	private void makeBookPan() {
		dateField = new JLabel("날짜");
		dateEnterInput = new JTextField();
		intervalField = new JLabel("~");
		dateExitInput = new JTextField();
		
		roomTypeField = new JLabel("객실 타입");
		viewField = new JLabel("뷰 타입");
		bedFiled = new JLabel("침대 타입");
	
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
		bedFiled.setBounds(x, y+=weight, fieldW, weight);
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
		bookPan.add(bedFiled);
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
		
		int x = detailPan.getX() + 20;
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
		
		int x = detailPan.getX() + 20;
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
	
	private void showRoomList() {
		
	}
	
	private void showBookedRoomList() {
		
	}
	
	private void makeTableModel() {
		String[] colName = {"호실", "방 타입", "뷰 타입", "침대 타입", "층수"};
		model = new DefaultTableModel(colName, 0);
		String[] colName2 = {"호실", "예약자명", "어른", "어린이", "방 타입", "뷰 타입", "침대 타입", "예약 번호", "입실 날짜", "퇴실 날짜"};			
		model2 = new DefaultTableModel(colName2, 0);
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
		for(int i = model2.getRowCount()-1; i>=0; i--) {
			model2.removeRow(i);
		}
	}
	
	private void clearTableView() {
		currList.clear();
		updateTableView();
	}
	
	private void updateTableView() {
		clearModel();
		for(int i = 0; i< currList.size();i++) {
//			addDataAtModel(currList.get(i));
		}
		table.setModel(model);
	}
	
	public JPanel getPanel() {
		return mainPan;
	}
}
