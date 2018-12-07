package pbl2.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import pbl2.SqlHelper;
import pbl2.controller.ViewEmployee.ModifyDialog;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoDish;
import pbl2.dto.DtoDishOrder;
import pbl2.dto.DtoEmployee;
import pbl2.dto.DtoRoom;

public class ViewRoomService{
	private JTable table, tempTable;
	private int width, height, totalPrice;
	private ArrayList<DtoDish> dishList;
	private ArrayList<DtoDishOrder> dishOrderList;
	private ArrayList<DtoBookedRoom> bookedRoomList;
	private ArrayList<DtoRoom> roomList;

	private JLabel roomNumberLabel, totalPriceLabel;
	private JComboBox<Integer> roomNumberCombo;
	private DefaultTableModel model, tempModel;
	private JButton orderButton, clearButton;
	private JPanel mainPan, orderPan, menuPan;
	private JScrollPane orderListScroll, menuScroll, tempOrderScroll;
	private SqlHelper sql = pbl2.MainActivity.sql;
	private QuantityDialog qd;
	
	public ViewRoomService(ArrayList<DtoDish> dishList, ArrayList<DtoDishOrder> dishOrderList,  ArrayList<DtoBookedRoom> bookedRoomList, ArrayList<DtoRoom> roomList, int width, int height) {
		this.dishList = dishList;
		this.dishOrderList = dishOrderList;
		this.bookedRoomList = bookedRoomList;
		this.roomList = roomList;
		this.width = width-30;
		this.height = height-30;
		makePan();
	}
	
	private void makePan() {
		mainPan = new JPanel();
		mainPan.setLayout(null);
		mainPan.setSize(width, height);
	
		orderPan = new JPanel();
		makeDefaultModel();
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);	
		orderListScroll = new JScrollPane(table);
		int orderPanWidth =  (int)(width * 0.4);
		int orderListScrollPanWidth = width-orderPanWidth;
		orderPan.setBounds(10, 10, orderPanWidth, height);
		orderListScroll.setBounds(orderPan.getWidth()+10, 10, orderListScrollPanWidth-30, height);
		makeOrderPan();
		updateOrderListView();
		
		mainPan.add(orderPan);
		mainPan.add(orderListScroll);
	}
	
	private void makeOrderPan() {
		orderPan.setLayout(null);

		menuPan = new JPanel();
		menuPan.setLayout(null);
		menuScroll = new JScrollPane(menuPan);
		menuScroll.setBounds(orderPan.getX(), 0, orderPan.getWidth()-30, orderPan.getWidth()-100);
		fillMenu();
		menuScroll.setViewportView(menuPan);
		tempTable = new JTable(tempModel);
		tempTable.setDefaultEditor(Object.class, null);
		tempOrderScroll = new JScrollPane(tempTable);
		tempOrderScroll.setBounds(menuScroll.getX(), menuScroll.getY()+menuScroll.getHeight()+20, (int)(menuScroll.getWidth()*0.6), orderListScroll.getHeight()-20-menuScroll.getHeight());
		
		
		roomNumberLabel = new JLabel("방번호 : ");
		roomNumberCombo = new JComboBox<>();
		for(int i=0; i<roomList.size();i++) {
			roomNumberCombo.addItem(roomList.get(i).getRoomNumber());
		}
		
		totalPriceLabel = new JLabel("합계 : " + totalPrice + " 원");
		clearButton = new JButton("초기화하기");
		orderButton = new JButton("주문하기");
		
		
		roomNumberLabel.setBounds(tempOrderScroll.getX()+tempOrderScroll.getWidth()+40, tempOrderScroll.getY()+10, 60, 20);
		roomNumberCombo.setBounds(roomNumberLabel.getX()+roomNumberLabel.getWidth(), roomNumberLabel.getY(), (menuScroll.getX()+menuScroll.getWidth())-(roomNumberLabel.getX()+roomNumberLabel.getWidth())+5, 20);
		if(roomNumberCombo.getWidth() < 80) {
			roomNumberCombo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		}else {
			roomNumberCombo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		}
		totalPriceLabel.setBounds(roomNumberLabel.getX(), roomNumberLabel.getY()+roomNumberLabel.getHeight()+10, 200, 20);
		int tempheight = orderListScroll.getHeight()-totalPriceLabel.getY()-totalPriceLabel.getHeight()-10;
		tempheight/=2;
		tempheight-=10;
		clearButton.setBounds(tempOrderScroll.getX()+tempOrderScroll.getWidth()+40, totalPriceLabel.getY()+totalPriceLabel.getHeight()+10, menuScroll.getWidth()-tempOrderScroll.getWidth()-40, tempheight);
		orderButton.setBounds(clearButton.getX(), clearButton.getY()+clearButton.getHeight()+10, clearButton.getWidth(), tempheight);
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearTempTableView();
				totalPrice = 0;
				updatePrice();
			}
		});
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(totalPrice == 0) {
					JOptionPane.showConfirmDialog(null, "메뉴를 선택해주세요.", "알림", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				int roomNum = Integer.valueOf(roomNumberCombo.getSelectedItem().toString());
				String msg = "\n방번호 : "+ roomNum +"\n\n";
				
				for(int i = 0; i< tempModel.getRowCount();i++) {
					msg += tempModel.getValueAt(i, 0).toString() + " : " + tempModel.getValueAt(i, 1).toString() + " 개\n";
				}
				msg += "\n합계 : " + totalPrice + " 원\n\n";
				msg += "주문하시겠습니까?";
				int result = JOptionPane.showConfirmDialog(null, msg, roomNumberCombo.getSelectedItem().toString() +"호 주문하기", JOptionPane.OK_CANCEL_OPTION);
				if (result == 0) { // OK=0 , Cancel=2 리턴
					for(int i = 0; i< tempModel.getRowCount();i++) {
						addData(String.valueOf(tempModel.getValueAt(i, 0)), Integer.valueOf((String) tempModel.getValueAt(i, 1)), roomNum, Integer.valueOf((String) tempModel.getValueAt(i, 2)));
					}
					clearButton.doClick();
				}
			}
		});
		tempTable.addMouseListener(new MouseListener() {
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
					if(tempTable.getSelectedRowCount()==1) {
						tempTable.requestFocus();
						int selected = tempTable.getSelectedRow();
						int tempPrice = Integer.valueOf((String) tempModel.getValueAt(selected, 2));
						totalPrice -= tempPrice;
						updatePrice();
						tempModel.removeRow(selected);
						tempTable.setModel(tempModel);
					}
				}
			}
		});
		orderPan.add(roomNumberLabel);
		orderPan.add(roomNumberCombo);
		orderPan.add(totalPriceLabel);
		orderPan.add(clearButton);
		orderPan.add(orderButton);
		orderPan.add(tempOrderScroll);
		orderPan.add(menuScroll);
	}
	
	private void fillMenu() {
		try {
			JLabel[] imgs = new JLabel[dishList.size()];
			JLabel[] text = new JLabel[dishList.size()];
			int itemNum = 3;//default item count in a row
			int space = (menuScroll.getWidth()-(124*itemNum))/(itemNum+1);
			if(space < 20) {
				itemNum = 2;
			}else if(space >= 80) {
				itemNum = 4;
			}
			space = (menuScroll.getWidth()-(124*itemNum))/(itemNum+1);
			int weight = 50;
			int x = space;
			int y = weight;
			int w = 124;
			int h = 26;
			int menuHeight = 140;
			int totalHeight = y+menuHeight+weight;
			for(int i = 0; i<dishList.size();i++) {
				if(i != 0 && i%itemNum==0) {
					x = space;
					y += menuHeight;
					y += weight;
					totalHeight += menuHeight;
					totalHeight += weight;
				}
				Blob blob = dishList.get(i).getImage();
				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize;
				nFileSize = (int)blob.length();
				byte[] buf = new byte[nFileSize];
				in.read(buf, 0, nFileSize);	
				in.close();
				
				ImageIcon ic = new ImageIcon(buf);
				imgs[i] = new JLabel("", SwingConstants.CENTER);
				text[i] = new JLabel("", SwingConstants.CENTER);
				String dname = dishList.get(i).getName();
				
				text[i].setFont(new Font("Serif", Font.BOLD, 12));
				
				imgs[i].setBounds(x, y, w, w);
				
				imgs[i].setIcon(ic);
				text[i].setBounds(imgs[i].getX(), imgs[i].getY()+imgs[i].getHeight(), w, h);
				text[i].setText("<html><div style='text-align:center'>"+dname + "<br/>" + dishList.get(i).getPrice() + " WON</div></html>");
				
				x += w;
				x += space;
				
				DtoDish dto = dishList.get(i);
				imgs[i].addMouseListener(new MouseListener() {
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
						if(qd != null){
							qd.dispose();
						}
						qd = new QuantityDialog(pbl2.MainActivity.jf, dname+" 주문", dto);
						qd.setVisible(true);
					}
				});
				text[i].addMouseListener(new MouseListener() {
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
						if(qd != null){
							qd.dispose();
						}
						qd = new QuantityDialog(pbl2.MainActivity.jf, dname+" 주문", dto);
						qd.setVisible(true);
					}
				});
				menuPan.add(imgs[i]);
				menuPan.add(text[i]);
			}
			menuPan.setPreferredSize(new Dimension(menuScroll.getWidth()-20, totalHeight));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	class QuantityDialog extends JDialog implements ActionListener{
		private static final long serialVersionUID = -6839259281116503161L;
		JLabel countLabel;
		int count= 1;
		JButton plusButton = new JButton("+");
		JButton minusButton = new JButton("-");
	    JButton addMenuButton =new JButton("추가");
	    JButton cancelButton = new JButton("취소");
	    DtoDish dto;
		QuantityDialog(JFrame frame, String title, DtoDish dto){
	        super(frame,title);
	        this.dto = dto;
	        this.setLayout(null);
	        this.setSize(210, 130);
	        this.setLocationRelativeTo(null);
	        countLabel = new JLabel(count+"", SwingConstants.CENTER);
	        plusButton.addActionListener(this);
	        minusButton.addActionListener(this);
	        addMenuButton.addActionListener(this);
	        cancelButton.addActionListener(this);
	        
	        minusButton.setBounds(50, 30, 20, 20);
	        countLabel.setBounds(0, 22, 210, 40);
	        countLabel.setBackground(Color.red);
	        countLabel.setFont(new Font("Serif", Font.BOLD, 20));
	        plusButton.setBounds(140, minusButton.getY(), minusButton.getWidth(), minusButton.getHeight());
	        
	        cancelButton.setBounds(25, 65, 70, 30);
	        addMenuButton.setBounds(cancelButton.getX()+cancelButton.getWidth()+20, cancelButton.getY(), cancelButton.getWidth(), cancelButton.getHeight());
	        
	        add(countLabel);
	        add(plusButton);
	        add(minusButton);
	        add(addMenuButton);
	        add(cancelButton);
	    }
		@Override
		public void actionPerformed(ActionEvent e) {
	    	if(e.getSource() == plusButton) {
	    		if(count<10) {
					count++;
					countLabel.setText(count+"");
				}
	    	}else if(e.getSource() == minusButton) {
	    		if(count>1) {
					count--;
					countLabel.setText(count+"");
				}
	    	}else if(e.getSource() == addMenuButton) {
	    		dispose();
	    		addDataOrderModel2(dto, count);
	    		updatePrice();
	    	}else if(e.getSource() == cancelButton) {
	    		dispose();
	    	}
		}
	}

	private void updatePrice() {
		totalPriceLabel.setText("합계 : " + totalPrice + " 원");
	}
	private void makeDefaultModel() {
		String[] colName1 = {"주문번호", "방번호", "메뉴","주문날짜", "주문시간", "주문수량", "총 가격","배달현황"};
		model = new DefaultTableModel(colName1, 0);
		String[] colName2 = {"메뉴", "주문수량", "총 가격"};
		tempModel = new DefaultTableModel(colName2, 0);
	}
	
	private void clearModel() {
		for(int i = model.getRowCount()-1; i>=0; i--) {
			model.removeRow(i);
		}
	}
	
	private void updateOrderListView() {
		clearModel();
		for(int i = 0;i< dishOrderList.size();i++) {
			if(dishOrderList.get(i).getDelivered().equals("X")) {
				addDataOrderModel(dishOrderList.get(i));
			}
		}
		table.setModel(model);
	}
	
	private void clearTempTableView() {
		for(int i = tempModel.getRowCount()-1; i>=0; i--) {
			tempModel.removeRow(i);
		}
		tempTable.setModel(tempModel);
	}
	
	private void addDataOrderModel(DtoDishOrder dto) {
		String[] orderdata = new String[8];
		for(int i=0;i<dishList.size();i++) {
			if(dto.getDishId()==dishList.get(i).getDishId()) {
				for(int j=0;j<bookedRoomList.size();j++) {
					if(dto.getBookId() == bookedRoomList.get(j).getBookId()) {
						for(int k=0;k<roomList.size();k++) {
							if(bookedRoomList.get(j).getRoomId() == roomList.get(k).getRoomId()) {
								orderdata[0] = String.valueOf(dto.getDishOrderId());
								orderdata[1] = String.valueOf(roomList.get(k).getRoomNumber());
								orderdata[2] = dishList.get(i).getName();
								orderdata[3] = String.valueOf(dto.getDate());
								orderdata[4] = String.valueOf(dto.getOrderTime());
								orderdata[5] = String.valueOf(dto.getQuantity());
								orderdata[6] = String.valueOf(dishList.get(i).getPrice() * dto.getQuantity());
								orderdata[7] = dto.getDelivered();
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		model.addRow(orderdata);
	}
	
	private void addDataOrderModel2(DtoDish dto, int quantity) {
		String[] orderdata = new String[3];
		orderdata[0] = dto.getName();
		orderdata[1] = String.valueOf(quantity);
		orderdata[2] = String.valueOf(dto.getPrice()*quantity);
		totalPrice += dto.getPrice()*quantity;
		tempModel.addRow(orderdata);
	}
	
	
	private void addData(String menuName, int quantity, int roomNumber, int sum) {
		ResultSet rs = sql.query("select max(dodishorderid) from tbldishorder");
		int doDishOrderID = -1;
		int doDishID = -1;
		int doBookID = -1;
		Date doDate = new Date(new java.util.Date().getTime());
		Time doOrderTime = new Time(new java.util.Date().getTime());
		int doQuantity = quantity;
		String doDelivered = "X";
		try {
			rs.next();
			doDishOrderID = rs.getInt(1)+1;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for(int i = 0; i< dishList.size();i++) {
			if(dishList.get(i).getName().equals(menuName)) {
				doDishID = dishList.get(i).getDishId();
				break;
			}
		}
		for(int i = 0; i< roomList.size();i++) {
			if(roomList.get(i).getRoomNumber() == roomNumber) {
				for(int j = 0; j< bookedRoomList.size();j++) {
					if(bookedRoomList.get(j).getRoomId()==roomList.get(i).getRoomId()) {
						doBookID = bookedRoomList.get(i).getBookId();
						break;
					}
				}
				break;
			}
		}
		
		
		if(doBookID == -1) {
			JOptionPane.showConfirmDialog(null, "사용중인 방이 아닙니다.", "알림", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		String[] orderdata = new String[8];
		orderdata[0] = String.valueOf(doDishOrderID);
		orderdata[1] = String.valueOf(roomNumber);
		orderdata[2] = menuName;
		orderdata[3] = String.valueOf(doDate);
		orderdata[4] = String.valueOf(doOrderTime);
		orderdata[5] = String.valueOf(quantity);
		orderdata[6] = String.valueOf(sum);
		orderdata[7] = doDelivered;
		model.addRow(orderdata);
		table.setModel(model);
		
		String q = "INSERT INTO tblDishOrder (doDishOrderID, doDishID, doBookID, doDate, doOrderTime, doQuantity, doDelivered) VALUES ("
												+doDishOrderID+", "+ doDishID + ", " + doBookID + ", '" + doDate +"', CURRENT_TIMESTAMP, "+doQuantity+", '"+ doDelivered+"')";
		sql.query(q);
		sql.commit();
	}
	
	public JPanel getPanel() {
		return mainPan;
	}
	
	
}
