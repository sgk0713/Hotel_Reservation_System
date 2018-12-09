package pbl2.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import pbl2.SqlHelper;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoCustomer;
import pbl2.dto.DtoEmployee;
import pbl2.dto.DtoReceipt;
import pbl2.dto.DtoRoom;

public class ViewRoom implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	private int width, height, num_floor, num_room, showroomareawidth, showroomareaheight, infofanareaheight;// (num_floor)X(num_room) for making panels,
	// roomNum
	private String[] data = new String[2];
	private JTabbedPane showroomJtp;
	private JPanel mainPan;
	private JTable table;
	private JScrollPane scrollRoom[];
	private JPanel floorPan[];
	private DefaultTableModel model;
	private ArrayList<DtoBookedRoom> bookedRoomList;
	private ArrayList<DtoRoom> roomList;
	private ArrayList<DtoCustomer> customerList;
	private ArrayList<DtoReceipt> receiptList;
	public SqlHelper sql = pbl2.MainActivity.sql;
	private ResultSet rs;

	private JPanel infoPan, roomInfoPan, reservationInfoPan, checkinInfoPan;
	private JScrollPane receiptPan;
	private TitledBorder infoBorder = new TitledBorder(new LineBorder(Color.BLACK), "객실 정보");
	private TitledBorder receiptBorder = new TitledBorder(new LineBorder(Color.BLACK), "이용 금액 정보");

	private int roomId;

	public ViewRoom(ArrayList<DtoBookedRoom> bookedRoomList, ArrayList<DtoRoom> roomList,
			ArrayList<DtoCustomer> customerList, ArrayList<DtoReceipt> receiptList, int width, int height) {
		this.bookedRoomList = bookedRoomList;
		this.roomList = roomList;
		this.customerList = customerList;
		this.receiptList = receiptList;
		this.width = width - 30;
		this.height = height;
		makePan();
	}

	private void makePan() {
		updateState();
		pbl2.controller.ViewRoomService.makeCombo();
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
		
		showroomareawidth = (int) (width * 0.75);
		showroomareaheight = height;
		infofanareaheight = (int) (height * 0.55);

		showroomJtp = new JTabbedPane();
		showroomJtp.setBounds(0, 0, showroomareawidth, showroomareaheight);
		// add Panel for each other
		for (int i = 0; i < num_floor; i++) {
			floorPan[i] = new JPanel();
			floorPan[i].setLayout(null);
			scrollRoom[i] = new JScrollPane(floorPan[i]);
			scrollRoom[i].setSize(new Dimension(showroomareawidth, showroomareaheight));
			makefloorPan(i);
			scrollRoom[i].setViewportView(floorPan[i]);
			showroomJtp.addTab((i + 1) + "층", null, scrollRoom[i], (i + 1) + "층 객실정보");
		}

		infoPan = new JPanel();
		infoPan.setBorder(infoBorder);
		infoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, showroomJtp.getHeight()-40);

		roomInfoPan = new JPanel(new GridLayout(5, 1, 0, 15));
		reservationInfoPan = new JPanel(new GridLayout(12, 1, 0, 15));
		checkinInfoPan = new JPanel(new GridLayout(12, 1, 0, 15));

		roomInfoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, showroomareaheight);
		reservationInfoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, infofanareaheight);
		checkinInfoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, infofanareaheight);

		makeTableModel();
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		infoPan.add(roomInfoPan);
		infoPan.add(reservationInfoPan);
		infoPan.add(checkinInfoPan);

		mainPan.add(infoPan);
		mainPan.add(showroomJtp);

		receiptPan = new JScrollPane(table);
		receiptPan.setBounds(showroomareawidth, infofanareaheight + 25, width - showroomareawidth,
				height - infofanareaheight - 38);
		receiptPan.setBorder(receiptBorder);
		receiptPan.setVisible(false);
		mainPan.add(receiptPan);

	}

	private void makefloorPan(int idx) {
		try {
			int floor = idx + 1;
			ResultSet rs = sql.query("select count(*) from tblroom where rfloor = " + floor);
			rs.next();
			num_room = rs.getInt(1);
			String[] roomNumArr = new String[num_room];
			rs = sql.query("select rroomnumber from tblroom where rfloor = " + floor + " order by rroomnumber asc");
			int ct = 0;
			while (rs.next()) {
				roomNumArr[ct] = rs.getString(1);
				ct++;
			}
			int numItem = 5;// default item count in a row
			int w = (int) (((scrollRoom[idx].getWidth() - 10) * 0.65) / numItem);
			int h = (int) (w * 1.55);
			int space = ((scrollRoom[idx].getWidth() - 10) - (w * numItem)) / (numItem + 1);
			int x = space;
			int y = space;
			int totalHeight = 2 * space + h;

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String dateStr = format.format(date);
			date = format.parse(format.format(date));

			for (int i = 0; i < num_room; i++) {
				if (i != 0 && i % numItem == 0) {
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
				JLabel roomNumLabel = new JLabel(roomNum + "호", SwingConstants.CENTER);
				statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
				roomNumLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

				colorLabel.setOpaque(true);
				colorLabel.setBackground(new Color(50, 200, 50));

				pan.setBounds(x, y, w, h);
				pan.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				statusLabel.setBounds(0, (int) (h * 0.15), w, 20);
				int colorW = (int) (w * 0.7);
				colorLabel.setBounds((int) (pan.getWidth() / 2.0 - colorW / 2.0),
						statusLabel.getY() + statusLabel.getHeight() + 10, colorW, (int) (colorW / 15.0));
				roomNumLabel.setBounds(0, (int) (h * 0.70), w, 30);

				String q = "select rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit, brcustomerid from tblRoom, tblbookedroom where rroomid = brroomid and rroomnumber = "
						+ roomNum + " and brdateenter <= '" + dateStr + "' and brdateexit >= '" + dateStr + "'";
				rs = sql.query(q);
				String roomIdStr = null;
				String state = null;
				Date dateEnter = null;
				Date dateExit = null;
				String customerId = null;
				while (rs.next()) {
					roomIdStr = rs.getString(1);
					state = rs.getString(4);
					dateEnter = rs.getDate(5);
					dateExit = rs.getDate(6);
					customerId = rs.getString(7);
				}
				if (dateExit != null) {
					if (dateEnter.equals(date) && state.toUpperCase().equals("BOOKING")) {
						statusLabel.setText("입실예약");
						colorLabel.setBackground(new Color(255, 185, 0));
					} else if (dateExit.equals(date) && state.toUpperCase().equals("CHECKIN")) {
						statusLabel.setText("이용중");
						colorLabel.setBackground(new Color(255, 0, 0));
					} else if (state.toUpperCase().equals("CHECKOUT") || state.toUpperCase().equals("NOSHOW")){
						statusLabel.setText("입실가능");
						colorLabel.setBackground(new Color(50, 200, 50));
					}else {
						statusLabel.setText("이용중");
						colorLabel.setBackground(new Color(255, 0, 0));
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
						if(e.getButton() == 3) {
							System.out.println("left");
							if(statusLabel.getText().equals("이용중") || statusLabel.getText().equals("입실예약")) {
								pbl2.MainActivity.jtp.setSelectedIndex(1);
								pbl2.controller.ViewReservation.regiSearchJtp.setSelectedIndex(1);
								try {
									String q = "select brcustomerid from tblRoom, tblbookedroom where rroomid = brroomid and rroomnumber = "
											+ roomNum + " and brdateenter <= '" + dateStr + "' and brdateexit >= '" + dateStr + "'";
									ResultSet rs = sql.query(q);
									rs.next();
									String cid = rs.getString(1); 
									rs = sql.query("select cname, cphone from tblcustomer where ccustomerid = "+cid);
									String ccname = null;
									String ccphone = null;
									while(rs.next()) {
										ccname = rs.getString(1);
										ccphone = rs.getString(2);
									}
									pbl2.controller.ViewReservation.searchNameInput.setText(ccname);
									pbl2.controller.ViewReservation.searchPhoneInput.setText(ccphone);
									pbl2.controller.ViewReservation.searchBtn.doClick();
									pbl2.controller.ViewReservation.table2.setRowSelectionInterval(0, 0);
									pbl2.controller.ViewReservation.table2.requestFocus();
								}catch (Exception e4) {
									e4.printStackTrace();
								}
							}
						}else {
							try {
								ResultSet rs = sql.query("select rroomid from tblRoom where rRoomNumber = " + roomNum);
								rs.next();
								roomId = rs.getInt(1);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
		
							if (statusLabel.getText().equals("입실가능")) { // available status
								roomInfoPan.removeAll();
								if (roomInfoPan.isVisible()) {
									roomInfoPan.setVisible(false);
								}
								receiptBorder = new TitledBorder(new LineBorder(Color.BLACK), "이용 금액 정보");
								receiptPan.setBorder(receiptBorder);
								makeRoomInfo(roomId);
							} else if (statusLabel.getText().equals("이용중")) { // checkin status
								checkinInfoPan.removeAll();
								if (checkinInfoPan.isVisible()) {
									checkinInfoPan.setVisible(false);
								}
								receiptBorder = new TitledBorder(new LineBorder(Color.BLACK), roomNum + "호 이용 금액 정보");
								receiptPan.setBorder(receiptBorder);
								makeCheckinInfo(roomId);
								updateTableView(roomId);
							} else if (statusLabel.getText().equals("입실예약")) { // booked status
								reservationInfoPan.removeAll();
								if (reservationInfoPan.isVisible()) {
									reservationInfoPan.setVisible(false);
								}
								receiptBorder = new TitledBorder(new LineBorder(Color.BLACK), "이용 금액 정보");
								receiptPan.setBorder(receiptBorder);
								makeReservationInfo(roomId);
							}
						}

					}
				});

				pan.add(statusLabel);
				pan.add(colorLabel);
				pan.add(roomNumLabel);
				floorPan[idx].add(pan);

				x += w;
				x += space;
			}
			floorPan[idx].setPreferredSize(new Dimension(scrollRoom[idx].getWidth() - 30, totalHeight));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeRoomInfo(int roomId) { // checkout 상태, 예약가능 상태일 때 보이기.
		infoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, showroomareaheight-38);
		roomId--;
		int roomNum = roomList.get(roomId).getRoomNumber();
		String viewType = roomList.get(roomId).getViewType();
		String roomType = roomList.get(roomId).getRoomType();
		String bed = roomList.get(roomId).getBed();
		int rprice = roomList.get(roomId).getPrice();

		JLabel roominfotitle = new JLabel(roomNum + " 호  ");
		roominfotitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		roomInfoPan.add(roominfotitle);

		JLabel viewtypelb = new JLabel("뷰 타입 : " + viewType);
		viewtypelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		JLabel roomtypelb = new JLabel("객실 타입 : " + roomType);
		roomtypelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		JLabel bedtypelb = new JLabel("침대 타입 : " + bed);
		bedtypelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		JLabel rpricelb = new JLabel("객실 이용금액 : " + rprice + " KRW");

		roomInfoPan.add(viewtypelb);
		roomInfoPan.add(roomtypelb);
		roomInfoPan.add(bedtypelb);
		rpricelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		roomInfoPan.add(rpricelb);

		roomInfoPan.setVisible(true);
		reservationInfoPan.setVisible(false);
		checkinInfoPan.setVisible(false);
		receiptPan.setVisible(false);
	}

	private void makeCheckinInfo(int roomId) { // check-in 상태일 때 보이기.
		infoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, infofanareaheight);
		int sprice = 0; // service
		int rprice = 0; // room

		JLabel[] checkinInfo = new JLabel[9];
		String[] infoString = { "호실: ", "고객명: ", "입실일자: ", "퇴실일자: ", "성인 인원: ", "아동 인원: ", "뷰 타입: ", "객실 타입: ",
				"침대 타입: " };

		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(date);
			rs = sql.query(
					"select rRoomNumber ,cName, brDateEnter, brDatEexit, brAdult, brChildren, rViewType, rRoomType, rBed from tblRoom, tblbookedroom, tblcustomer where rroomid = "
							+ roomId + " and rroomid = brroomid and ccustomerid = brcustomerid and brdateenter <= '"+dateStr+"' and brdateexit >= '"+dateStr+"'");
			while (rs.next()) {
				for (int i = 0; i < checkinInfo.length; i++) {
					if (i == 0) {
						checkinInfo[i] = new JLabel(infoString[i] + String.valueOf(rs.getObject(i + 1) + " 호"));
						checkinInfo[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
					} else {
						checkinInfo[i] = new JLabel(infoString[i] + String.valueOf(rs.getObject(i + 1)));
						checkinInfo[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
					}
				}
			}
			rs = sql.query("select rcprice from tblReceipt, tblBookedroom where " + roomId
					+ " = brroomid and rcCustomerId = brcustomerId and rcdate>= brdateEnter");
			while (rs.next()) {
				sprice += rs.getInt(1);
			}

			rs = sql.query("select rprice from tblRoom where rRoomId =" + roomId);
			rs.next();
			rprice = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < checkinInfo.length; i++) {
			checkinInfoPan.add(checkinInfo[i]);
		}
		JLabel rpricelb = new JLabel("객실 이용금액 : " + rprice + " KRW");
		JLabel spricelb = new JLabel("추가 이용금액 : " + sprice + " KRW");
		JLabel tpricelb = new JLabel("총 이용금액 : " + (rprice + sprice) + " KRW");

		rpricelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		checkinInfoPan.add(rpricelb);
		spricelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		checkinInfoPan.add(spricelb);
		tpricelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		checkinInfoPan.add(tpricelb);

		roomInfoPan.setVisible(false);
		reservationInfoPan.setVisible(false);
		checkinInfoPan.setVisible(true);
		receiptPan.setVisible(true);
	}

	private void makeReservationInfo(int roomId) {// booked 상태 일 때 보이기
		infoPan.setBounds(showroomareawidth, 25, width - showroomareawidth, showroomareaheight-38);
		int rprice = 0; // room
		JLabel[] reservationInfo = new JLabel[9];
		String[] infoString = { "호실: ", "고객명: ", "입실일자: ", "퇴실일자: ", "성인 인원: ", "아동 인원: ", "뷰 타입: ", "객실 타입: ",
				"침대 타입: " };
		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(date);
			rs = sql.query(
					"select rRoomNumber ,cName, brDateEnter, brDatEexit, brAdult, brChildren, rViewType, rRoomType, rBed from tblRoom, tblbookedroom, tblcustomer where rroomid = "
							+ roomId + " and rroomid = brroomid and ccustomerid = brcustomerid and brdateenter = '"+dateStr+"'");
			rs.next();
			for (int i = 0; i < reservationInfo.length; i++) {
				reservationInfo[i] = new JLabel(infoString[i] + String.valueOf(rs.getObject(i + 1)));
				reservationInfo[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
			}

			rs = sql.query("select rprice from tblRoom where rRoomId =" + roomId);
			rs.next();
			rprice = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < reservationInfo.length; i++) {
			reservationInfoPan.add(reservationInfo[i]);
		}

		JLabel rpricelb = new JLabel("객실 이용금액 : " + rprice + " KRW");
		rpricelb.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		reservationInfoPan.add(rpricelb);

		roomInfoPan.setVisible(false);
		reservationInfoPan.setVisible(true);
		checkinInfoPan.setVisible(false);
		receiptPan.setVisible(false);
	}

	private void makeTableModel() {
		String[] colName = { "이용 내용", "금액" };
		model = new DefaultTableModel(colName, 0);

	}

	private void addDataAtModel(int roomId) {
		try {
			rs = sql.query("select rcCustomerId, rcStatement , rcprice from tblReceipt, tblBookedroom where " + roomId
					+ " = brroomid and rcCustomerId = brcustomerId and rcdate>= brdateEnter");
			while (rs.next()) {
				data[0] = rs.getString(2);
				data[1] = String.valueOf(rs.getInt(3));
				model.addRow(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.setModel(model);
	}

	private void clearModel() {
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}

	private void updateTableView(int roomId) {
		clearModel();
		addDataAtModel(roomId);
		table.setModel(model);
	}

	private void updateState() {
		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(date);

			date = format.parse(format.format(date));

			String q = "select brbookid, rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit from tblRoom, tblbookedroom where rroomid = brroomid and brdateexit < '"
					+ dateStr + "'";
			System.out.println("dateStr:"+dateStr);
			rs = sql.query(q);
			String bookId = null;
			String roomId = null;
			String state = null;
			Date dateEnter = null;
			Date dateExit = null;
			ArrayList<String> ar = new ArrayList<>();
			ar.clear();
			while (rs.next()) {
				ar.add(rs.getString(1));
			}
			for (int z = 0; z < ar.size(); z++) {
				sql.query("update tblbookedroom set brstate = 'CheckOut' where brbookid = " + ar.get(z));
			}
			q = "select brbookid, rroomid, rroomnumber, rfloor, brstate, brdateenter, brdateexit from tblRoom, tblbookedroom where rroomid = brroomid and brdateenter < '"
					+ dateStr + "' and brdateexit > '" + dateStr + "'";
			System.out.println(q);
			rs = sql.query(q);
			bookId = null;
			roomId = null;
			state = null;
			dateEnter = null;
			dateExit = null;
			ar.clear();
			while (rs.next()) {
				ar.add(rs.getString(1));
				
			}
			for (int z = 0; z < ar.size(); z++) {
				sql.query("update tblbookedroom set brstate = 'CheckIn' where brbookid = " + ar.get(z));
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