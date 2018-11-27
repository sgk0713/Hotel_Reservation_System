package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import pbl2.dto.DtoEmployee;

public class ViewEmployee implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == searchBtn) {
			
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

	private ArrayList<Object> list;
	private int width, height;
	private DefaultTableModel model;
	private JTable table;
	private JPanel mainPan, searchPan, registerPan;
	private JLabel nameField, departmentField, genderField, dateEnterField, positionField, salaryField;
	private JTextField nameInput, departInput, dateInput, posiInput, salaryInput;
	private JTabbedPane regiSearchJtp;
	private ButtonGroup genderGroup;
	private JRadioButton maleRadio, femaleRadio;
	private JComboBox<String> departCombo, posiCombo;
	private JButton searchBtn, addBtn;
	private JScrollPane scroll;
	
	public ViewEmployee(ArrayList<Object> list, int width, int height) {
		this.list = list;
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
				clearModel();
				updateTableView();
			}
		});
		makeTableModel();
		table = new JTable(model);
		scroll = new JScrollPane(table);
		
		
		scroll.setBounds(10, searchAreaHeight, width-20, height-searchAreaHeight-20);
		mainPan.add(regiSearchJtp);
		mainPan.add(scroll, BorderLayout.CENTER);
	}
	
	private void makeSearchPan() {
	
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
		departCombo.addItem("객실팀");
		departCombo.addItem("식당팀");
		departCombo.addItem("직접 입력");
		departInput = new JTextField();
		departInput.setVisible(false);
		
		
		positionField = new JLabel("직급 : ");
		posiCombo = new JComboBox<>();
		posiCombo.addItem("하우스키퍼 매니저ㅁ;ㅣㄹㄴㅇㅁㄴㅇㄹㅁㅇㄹㅁㅇㄹ");
		posiCombo.addItem("하우스키퍼 매니저");
		posiCombo.addItem("하우스키퍼");
		posiCombo.addItem("직접 입력");		
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
	public void makeTableModel() {
		String[] colName = {"이름", "성별", "부서", "직급", "입사일", "급여"};
		model = new DefaultTableModel(colName, 0);
	}
	
	public void clearModel() {
		for(int i = model.getRowCount()-1; i>=0; i--) {
			model.removeRow(i);
		}
	}
	
	public void removeData(int idx) {
		model.removeRow(idx);
		updateTableView();
	}
	
	public void addData() {
		new DtoEmployee(1, 1, "aa", "인사", "남", new Date(20200), "대리", 10220);
		int id = list.size()+1;
		int hotelId = 200;
		String name = nameInput.getText();
		String depart = null;
		if(departCombo.getSelectedItem() == "직접 입력") {
			if(!departInput.getText().isEmpty())
				depart = departInput.getText();
		}else {
			depart = departCombo.getSelectedItem().toString();
		}
		String gender = null;
		if(maleRadio.isSelected()) {
			gender = maleRadio.getText();
		}else if(femaleRadio.isSelected()) {
			gender = femaleRadio.getText();
		}
		Date date = null;
		if(!dateInput.getText().isEmpty()) {
			//
		}else {
			date = new Date(new java.util.Date().getTime());			
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
			System.out.println(name);
			System.out.println(depart);
			System.out.println(gender);
			System.out.println(date);
			System.out.println(position);
			System.out.println(salary);
			JOptionPane.showMessageDialog(null, "빈칸을 채워주세요.","확인", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		System.out.println("name:" + name);
		System.out.println("depart : " + depart);
		System.out.println("gender : " + gender);
		System.out.println("date : " + date);
		System.out.println("position : " + position);
		System.out.println("salary : " + salary);
		return;
//		DtoEmployee dto = new DtoEmployee(id, hotelId, name, depart, gender, date, position, salary);
//		String[] data = new String[6];
//		data[0] = dto.getName();
//		data[1] = dto.getGender();
//		data[2] = dto.getDepartment();
//		data[3] = dto.getPosition();
//		data[4] = String.valueOf(dto.getDateEnter());
//		data[5] = String.valueOf(dto.getSalary());
//		model.addRow(data);
	}
	
	public void updateTableView() {
		table.setModel(model);
	}
	
	public JPanel getPanel() {
		return mainPan;
	}
}