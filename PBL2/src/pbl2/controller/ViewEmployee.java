package pbl2.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
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
		scroll = new JScrollPane(table);
		scroll.setBounds(10, searchAreaHeight, width-20, height-searchAreaHeight-20);
		mainPan.add(regiSearchJtp);
		mainPan.add(scroll, BorderLayout.CENTER);
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
		int id = list.size()+1;
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
			gender = maleRadio.getText();
		}else if(femaleRadio.isSelected()) {
			gender = femaleRadio.getText();
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
		

		System.out.println("name:" + name);
		System.out.println("depart : " + depart);
		System.out.println("gender : " + gender);
		System.out.println("date : " + date);
		System.out.println("position : " + position);
		System.out.println("salary : " + salary);
		
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
				gender = maleRadio.getText();
			}else if(searchFemaleRadio.isSelected()) {
				gender = femaleRadio.getText();
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