package pbl2.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import pbl2.SqlHelper;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoCustomer;
import pbl2.dto.DtoReceipt;
import pbl2.dto.DtoRoom;

/*
 * <MODEL>
 * 4. 네모네모그리드마다의 정보 : 색상인덱스, 호실이름.
 * 5. 클릭시 나와야하는 구조 : 타이틀:호실이름 /내용: 호실정보 /기능:닫기 버튼.
 * 6. 클릭시 나와야하는 정보 :
 * 		- (이용중) 고객이름, 기간, 인원, 방타입, 요청사항, 사용내역.
 * 		- (예약됨) 예약 정보.
 * 7. 워크플로우 :
 * 		(main)
 * 		- 각 호실마다 실시간 상태를 받는다.
 * 		- 각 호실마다 실시간 상태를 gui에 출력한다.
 * 		(click)
 * 		- a. 클릭한 호실의 호실 번호 받기
 * 		- b. (if clicked == 이용중)
 * 			- 호실번호를 통해 데이터베이스에서 고객이름 기간 인원 방타입 요청사항 사용내역 받아오기
 * 			 (if clicked == 예약됨)
 * 			- 호실번호를 통해 데이터베이스에서 예약정보 받아오기.
 *		(정리하면, 필요한 메소드는 다음과 같다.)
 */

public class ViewRoom implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	private int width, height, num_floor, num_room, roomNum;// (num_floor)X(num_room) for making panels,
																		// roomNum
	private String roomState;
	private DefaultTableModel model;
	private JTabbedPane showroomJtp;
	private JPanel mainPan, statusPan;
	private JScrollPane scrollRoom[];
	private JPanel floorPan[];
	private JPanel roomPan[][];
	private JLabel table_label[][];
	private ArrayList<DtoBookedRoom> bookedRoomList;
	private ArrayList<DtoRoom> roomList;
	private ArrayList<DtoCustomer> customerList;
	private ArrayList<DtoReceipt> receiptList;
	private SqlHelper sql = pbl2.MainActivity.sql;
	private ResultSet rs;
	
	public ViewRoom(ArrayList<DtoBookedRoom> bookedRoomList, ArrayList<DtoRoom> roomList, ArrayList<DtoCustomer> customerList, ArrayList<DtoReceipt> receiptList, int width, int height) {
		this.bookedRoomList = bookedRoomList;
		this.roomList = roomList;
		this.customerList = customerList;
		this.receiptList = receiptList;
		this.width = width-30;
		this.height = height;
		makePan();
	}

	private void makePan() {
		updateState();
		mainPan = new JPanel();
		mainPan.setLayout(null);
		mainPan.setSize(width, height);
		
		
		try {
			rs = sql.query("select count(distinct rfloor) from tblroom");
			rs.next();
			num_floor = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scrollRoom = new JScrollPane[num_floor];
		floorPan = new JPanel[num_floor];
		
		int showroomareawidth = (int) (width * 0.75);
		int showroomareaheight = height;
		showroomJtp = new JTabbedPane();
		showroomJtp.setBounds(0, 0, showroomareawidth, showroomareaheight);
		//add Panel for each other
		for (int i = 0; i < num_floor; i++) {
			floorPan[i] = new JPanel();
			floorPan[i].setLayout(null);
			scrollRoom[i] = new JScrollPane(floorPan[i]);
			scrollRoom[i].setSize(new Dimension(showroomareawidth, showroomareaheight));
			makefloorPan(i);
			scrollRoom[i].setViewportView(floorPan[i]);
			showroomJtp.addTab((i + 1) + "층", null, scrollRoom[i], (i + 1) + "층 객실정보");
		}
		mainPan.add(showroomJtp);
	}

	private void makefloorPan(int idx){
		try {
			int floor = idx+1;
			ResultSet rs = sql.query("select count(*) from tblroom where rfloor = "+floor);
			rs.next(); 
			num_room = rs.getInt(1);
			String[] roomNumArr = new String[num_room];
			rs = sql.query("select rroomnumber from tblroom where rfloor = "+floor+" order by rroomnumber asc");
			int ct =0;
			while(rs.next()) {
				roomNumArr[ct] = rs.getString(1);
				ct++;
			}
			int numItem = 5;//default item count in a row			
			int w = (int)(((scrollRoom[idx].getWidth()-10)*0.65)/numItem);
			int h = (int)(w*1.55);
			int space = ((scrollRoom[idx].getWidth()-10)-(w*numItem))/(numItem+1);
			int x = space;
			int y = space;
			int totalHeight = 2*space+h;
			
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String dateStr = format.format(date);
			date = format.parse(format.format(date));
			
		
			for (int i = 0; i < num_room; i++) {
				if(i != 0 && i%numItem==0) {
					x = space;
					y += h;
					y += space;
					totalHeight += h;
					totalHeight += space;
				}

				JPanel pan = new JPanel();
				pan.setLayout(null);
				
				JLabel statusLabel = new JLabel("입실가능", SwingConstants.CENTER);
				JLabel colorLabel = new JLabel();
				String roomNum = roomNumArr[i];
				JLabel roomNumLabel = new JLabel(roomNum+"호", SwingConstants.CENTER);
				statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
				roomNumLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

				colorLabel.setOpaque(true);
				colorLabel.setBackground(new Color(50, 200, 50));
				
				pan.setBounds(x, y, w, h);
				pan.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				statusLabel.setBounds(0, (int)(h*0.15), w, 20);
				int colorW = (int)(w*0.7);
				colorLabel.setBounds((int)(pan.getWidth()/2.0-colorW/2.0), statusLabel.getY()+statusLabel.getHeight()+10, colorW, (int)(colorW/15.0));
				roomNumLabel.setBounds(0, (int)(h*0.70), w, 30);
				
				String q = "select rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit from tblRoom, tblbookedroom where rroomid = brroomid and rroomnumber = "+roomNum+" and brdateenter <= '"+dateStr+"' and brdateexit >= '"+dateStr+"'";
				rs = sql.query(q);
				String roomId =null;
				String state = null;
				Date dateEnter = null;
				Date dateExit = null;
				while(rs.next()) {		
					roomId = rs.getString(1);
					state = rs.getString(4);
					dateEnter = rs.getDate(5);
					dateExit = rs.getDate(6);
				}
				if(dateExit != null) {
					if(dateEnter.equals(date) && state.toUpperCase().equals("BOOKING")) {
						statusLabel.setText("입실예약");
						colorLabel.setBackground(new Color(255,185,0));
					}else if(dateExit.equals(date) && state.toUpperCase().equals("CHECKIN")) {
						statusLabel.setText("이용중");
						colorLabel.setBackground(new Color(255,0,0));
					}else {
						statusLabel.setText("이용중");
						colorLabel.setBackground(new Color(255,0,0));
					}
				}
				
				pan.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(null, roomNum+"호 클릭됨!!", roomNum+"호", JOptionPane.PLAIN_MESSAGE);
					}
				});
				
				pan.add(statusLabel);
				pan.add(colorLabel);
				pan.add(roomNumLabel);
				floorPan[idx].add(pan);
				
				x += w;
				x += space;
			}
			floorPan[idx].setPreferredSize(new Dimension(scrollRoom[idx].getWidth()-30, totalHeight));
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makestatusPan() {
		// TO-DO (GUI)
	}

	private void makeTableModel() {
		String[] colName = { "ID", "hotelID", "roomNumber", "floor", "viewType", "price", "roomType", "bed" };
		model = new DefaultTableModel(colName, 0);
	}

	// Function
	private void showstateInfo() {
	}

	private void roomInfo() {
	}

	private void broomInfo() {
	}

	private void reservationInfo() {
	}

	private void serchroom() {

	}
	
	private void updateState() {
		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String dateStr = format.format(date);
			
			date = format.parse(format.format(date));
			
			String q = "select brbookid, rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit from tblRoom, tblbookedroom where rroomid = brroomid and brdateexit < '"+dateStr+"'";
			rs = sql.query(q);
			String bookId = null;
			String roomId =null;
			String state = null;
			Date dateEnter = null;
			Date dateExit = null;
			ArrayList<String> ar = new ArrayList<>();
			while(rs.next()) {
				ar.add(rs.getString(1));
			}
			for(int z = 0; z < ar.size(); z++) {
				sql.query("update tblbookedroom set brstate = 'CheckOut' where brbookid = "+ar.get(z));
			}
			q = "select brbookid, rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit from tblRoom, tblbookedroom where rroomid = brroomid and brdateenter < '"+dateStr+"' and brdateexit > '"+dateStr+"'";
			rs = sql.query(q);
			bookId = null;
			roomId =null;
			state = null;
			dateEnter = null;
			dateExit = null;
			while(rs.next()) {
				ar.add(rs.getString(1));
			}
			for(int z = 0; z < ar.size(); z++) {
				sql.query("update tblbookedroom set brstate = 'CheckIn' where brbookid = "+ar.get(z));
			}
			sql.commit();
			System.out.println("update tblBookedRoom state complete!");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JPanel getPanel() {
		return mainPan;
	}

}
