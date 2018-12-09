package pbl2.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import pbl2.controller.ViewEmployee.ModifyDialog;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoCleanedRoom;
import pbl2.dto.DtoDish;
import pbl2.dto.DtoEmployee;
import pbl2.dto.DtoFixture;
import pbl2.dto.DtoHk;
import pbl2.dto.DtoRcFixture;
import pbl2.dto.DtoRoom;

public class ViewHouseKeeper implements ActionListener {

	private ArrayList<DtoEmployee> employeeList;
	private ArrayList<DtoFixture> fixtureList;
	private ArrayList<DtoCleanedRoom> cleanedRoomList;
	private ArrayList<DtoRcFixture> rcFixtureList;
	private ArrayList<DtoRoom> roomList;
	private ArrayList<DtoHk> hkList;
	private JPanel mainPan;
	private JButton AddComplainButton;
	private JScrollPane arrangeHK, HKstatus, Fixturestatus;
	private DefaultTableModel HKModel, FixModel, ArrangeModel, tempModel;
	private JTable Fixtable, HKtable, ArrangeTable;
	private JLabel roomNumberLabel, roomnum, complaintext;
	private JTextField complaininput;
	private ModifyDialog md;
	private JComboBox<Integer> roomNumberCombo;
	private int width, height;

	public ViewHouseKeeper(ArrayList<DtoEmployee> employeeList, ArrayList<DtoFixture> fixtureList,
			ArrayList<DtoRcFixture> rcFixtureList, ArrayList<DtoCleanedRoom> cleanedRoomList,
			ArrayList<DtoRoom> roomList, ArrayList<DtoHk> hkList, int width, int height) {
		this.employeeList = employeeList;
		this.fixtureList = fixtureList;
		this.rcFixtureList = rcFixtureList;
		this.cleanedRoomList = cleanedRoomList;
		this.roomList = roomList;
		this.hkList = hkList;
		this.width = width - 30;
		this.height = height - 30;
		makePan();
	}

	private void makePan() {
		int searchAreaHeight = (int) (height * 0.5);
		int searchAreaHeight1 = (int) (height * 0.25);
		int searchAreaWidth = (int) (width * 0.5);
		mainPan = new JPanel();
		mainPan.setLayout(null);
		mainPan.setSize(width, height);

		makeHKstatusModel();

		for (int i = 0; i < hkList.size(); i++) {
			addDataHKstatusModel(hkList.get(i));
		}

		makeFixturestatusModel();

		for (int i = 0; i < rcFixtureList.size(); i++) {
			addDataFixturestatusModel(rcFixtureList.get(i), fixtureList.get(i));
		}

		makearrangeModel();

		AddComplainButton = new JButton("추가");

		Fixtable = new JTable(FixModel);
		Fixtable.setDefaultEditor(Object.class, null);
		Fixtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Fixtable.addMouseListener(new MouseListener() {
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
				if (e.getClickCount() == 2) {
					if (Fixtable.getSelectedRowCount() == 1) {
						int row = Fixtable.getSelectedRow();
						int id = Integer.valueOf((String) Fixtable.getModel().getValueAt(Fixtable.getSelectedRow(), 0));
						DtoFixture temp = null;
						DtoRcFixture temp1 = null;
						for (int i = 0; i < fixtureList.size(); i++) {
							if (fixtureList.get(i).getFixtureId() == id) {
								temp = fixtureList.get(i);
								temp1 = rcFixtureList.get(i);
								break;
							}
						}
						JFrame jf = new JFrame();
						if (md != null) {
							md.dispose();
						}
						md = new ModifyDialog(jf, "수정", temp, temp1, row);
						md.setVisible(true);
					}
				}

			}
		});

		HKtable = new JTable(HKModel);
		HKtable.setDefaultEditor(Object.class, null);
		HKtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		HKtable.addMouseListener(new MouseListener() {
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
				if (e.getClickCount() == 2) {
					if (HKtable.getSelectedRowCount() == 1) {
						int row = HKtable.getSelectedRow();
						int id = Integer.valueOf((String) HKtable.getModel().getValueAt(HKtable.getSelectedRow(), 1));
						DtoHk temp = null;
						for (int i = 0; i < hkList.size(); i++) {
							if (hkList.get(i).getEmployeeId() == id) {
								temp = hkList.get(i);
								break;
							}
						}
						JFrame jf = new JFrame();
						if (md != null) {
							md.dispose();
						}
						md = new ModifyDialog(jf, "수정", temp, row);
						md.setVisible(true);
					}
				}

			}
		});

		ArrangeTable = new JTable(ArrangeModel);
		ArrangeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ArrangeTable.setDefaultEditor(Object.class, null);
		ArrangeTable.addMouseListener(new MouseListener() {
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
				if (e.getClickCount() == 2) {
					if (ArrangeTable.getSelectedRowCount() == 1) {
						int row = ArrangeTable.getSelectedRow();
						int id = Integer
								.valueOf((String) ArrangeTable.getModel().getValueAt(ArrangeTable.getSelectedRow(), 0));
						DtoHk temp = null;
						for (int i = 0; i < hkList.size(); i++) {
							if (hkList.get(i).getEmployeeId() == id) {
								temp = hkList.get(i);
								break;
							}
						}
						JFrame jf = new JFrame();
						if (md != null) {
							md.dispose();
						}
						md = new ModifyDialog(jf, "수정", row);
						md.setVisible(true);
					}
				}

			}
		});

		// 직원의 현재 상태
		HKstatus = new JScrollPane(HKtable);

		// 비품상태
		Fixturestatus = new JScrollPane(Fixtable);
		// 배치하기
		arrangeHK = new JScrollPane(ArrangeTable);

		arrangeHK.setBounds(0, 0, searchAreaWidth, searchAreaHeight * 2 - searchAreaHeight1);
		HKstatus.setBounds(searchAreaWidth, 0, searchAreaWidth, searchAreaHeight);
		Fixturestatus.setBounds(searchAreaWidth, searchAreaHeight, searchAreaWidth, searchAreaHeight);

		AddComplainButton.setBounds(searchAreaWidth - 90, searchAreaHeight * 2 - 50, 70, 30);
		AddComplainButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int roomNum = Integer.valueOf(roomNumberCombo.getSelectedItem().toString());
				String msg = "\n방번호 : " + roomNum + "\n";
				msg += "\n요청사항 : " + complaininput.getText() + "\n\n";
				int result = JOptionPane.showConfirmDialog(null, msg,
						roomNumberCombo.getSelectedItem().toString() + "호 입력하기", JOptionPane.OK_CANCEL_OPTION);
				if (result == 0) { // OK=0 , Cancel=2 리턴
					String[] data = new String[4];
					data[0] = String.valueOf(roomNum);
					data[1] = complaininput.getText();
					data[2] = "해결 중";
					data[3] = "미배치";
					ArrangeModel.addRow(data);
					complaininput.setText(null);
				}
			}
		});

		roomNumberCombo = new JComboBox<>();
		for (int i = 0; i < roomList.size(); i++) {
			roomNumberCombo.addItem(roomList.get(i).getRoomNumber());
		}

		complaintext = new JLabel("요청사항 ");
		complaininput = new JTextField();
		roomnum = new JLabel("호실:");
		roomnum.setBounds(10, searchAreaHeight * 2 - searchAreaHeight1 + 10, 70, 30);
		roomNumberCombo.setBounds(80, searchAreaHeight * 2 - searchAreaHeight1 + 10, 100, 30);
		complaintext.setBounds(10, searchAreaHeight * 2 - searchAreaHeight1 + 60, 70, 30);
		complaininput.setBounds(80, searchAreaHeight * 2 - searchAreaHeight1 + 60, 300, 100);

		mainPan.add(complaininput);
		mainPan.add(complaintext);
		mainPan.add(roomnum);
		mainPan.add(roomNumberCombo);
		mainPan.add(AddComplainButton);
		mainPan.add(arrangeHK);
		mainPan.add(HKstatus);
		mainPan.add(Fixturestatus);
	}

	class ModifyDialog extends JDialog {

		JLabel roomnum, complain, status, hk, namelabel, time, hkstatus, hkfloor, fixQuantity, fixDate, fixPhone,
				statusInput;
		JTextField roomnumInput, complainInputDialog, hkInput, namelabelinput, timeInput, hkstatusInput, hkfloorInput,
				fixQuantityInput, fixDateInput, fixPhoneInput, namelabelInput;
		JComboBox<Integer> room;
		JButton modiButton = new JButton("수정");
		JButton cancelButton = new JButton("삭제");

		// edit complain model
		ModifyDialog(JFrame frame, String title, int row) {
			super(frame, title);
			this.setLayout(null);
			this.setSize(350, 300);
			this.setLocationRelativeTo(null);
			JPanel p1 = new JPanel();
			p1.setLayout(null);
			p1.setBounds(0, 0, 340, 290);
			roomnum = new JLabel("호실         :");
			complain = new JLabel("요청사항 :");
			status = new JLabel("상태         :");
			hk = new JLabel("담당         :");

			room = new JComboBox<>();
			complainInputDialog = new JTextField();
			statusInput = new JLabel();
			hkInput = new JTextField();

			int curRoomNum = Integer.valueOf((String) ArrangeModel.getValueAt(row, 0));
			String curComplain = (String) ArrangeModel.getValueAt(row, 1);
			String curStatus = (String) ArrangeModel.getValueAt(row, 2);
			String curHk = (String) ArrangeModel.getValueAt(row, 3);

			for (int i = 0; i < roomList.size(); i++) {
				if (roomList.get(i).getRoomNumber() == curRoomNum) {
					curRoomNum = i;
				}
				room.addItem(roomList.get(i).getRoomNumber());
			}
			room.setSelectedIndex(curRoomNum);
			complainInputDialog.setText(curComplain);
			statusInput.setText(curStatus);
			hkInput.setText(curHk);

			int x = 10;
			int y = 0;
			roomnum.setBounds(x, y += 30, 90, 30);
			complain.setBounds(x, y += 40, 90, 50);
			status.setBounds(x, y += 60, 90, 30);
			hk.setBounds(x, y += 40, 90, 30);
			x += 70;
			y = 0;
			room.setBounds(x, y += 30, 100, 30);
			complainInputDialog.setBounds(x, y += 40, 200, 50);
			statusInput.setBounds(x + 5, y += 60, 70, 30);
			hkInput.setBounds(x, y += 40, 70, 30);
			modiButton.setBounds(p1.getWidth() / 2 - 80, p1.getHeight() - 70, 70, 30);
			modiButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int editedroom = Integer.valueOf(room.getSelectedItem().toString());
					String msg = "\n방번호 : " + editedroom + "\n";
					msg += "\n요청사항 : " + complainInputDialog.getText() + "\n\n";
					int result = JOptionPane.showConfirmDialog(null, msg, room.getSelectedItem().toString() + "호 수정하기",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == 0) { // OK=0 , Cancel=2 리턴
//						String[] data = new String[4];
//						data[0] = String.valueOf(editedroom);
//						data[1] = complainInput.getText();
//						data[2] = statusInput.getText();
//						data[3] = hkInput.getText();
						ArrangeModel.setValueAt(String.valueOf(editedroom), row, 0);
						ArrangeModel.setValueAt(complainInputDialog.getText(), row, 1);
						ArrangeModel.setValueAt(statusInput.getText(), row, 2);
						ArrangeModel.setValueAt(hkInput.getText(), row, 3);
//						ArrangeModel.removeRow(row);
//						ArrangeModel.addRow(data);
						dispose();
					}
				}

			});
			cancelButton.setBounds(p1.getWidth() / 2 + 10, p1.getHeight() - 70, 70, 30);
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int result = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제", JOptionPane.OK_CANCEL_OPTION);
					if (result == 0) {
						ArrangeModel.removeRow(row);
						dispose();
					}

				}

			});
			p1.add(roomnum);
			p1.add(complain);
			p1.add(status);
			p1.add(hk);
			p1.add(room);
			p1.add(complainInputDialog);
			p1.add(statusInput);
			p1.add(hkInput);
			p1.add(modiButton);
			p1.add(cancelButton);
			this.add(p1);
		}

		// edit housekeeper status model
		ModifyDialog(JFrame frame, String title, DtoHk dto, int row) {
			super(frame, title);
			this.setLayout(null);
			this.setSize(350, 300);
			this.setLocationRelativeTo(null);
			JPanel p2 = new JPanel();
			p2.setLayout(null);
			p2.setBounds(0, 0, 340, 290);
			namelabel = new JLabel("이름      :");
			time = new JLabel("배치시간 :");
			hkstatus = new JLabel("상태      :");
			hkfloor = new JLabel("층      :");
			namelabelinput = new JTextField(dto.getName());
			timeInput = new JTextField(String.valueOf(dto.getTime()));
			hkstatusInput = new JTextField(dto.getState());
			hkfloorInput = new JTextField();
			int x = 10;
			int y = 0;
			namelabel.setBounds(x, y += 40, 90, 30);
			time.setBounds(x, y += 40, 90, 30);
			hkstatus.setBounds(x, y += 40, 90, 30);
			hkfloor.setBounds(x, y += 40, 90, 30);
			x += 70;
			y = 0;
			namelabelinput.setBounds(x, y += 40, 70, 30);
			timeInput.setBounds(x, y += 40, 90, 30);
			hkstatusInput.setBounds(x, y += 40, 70, 30);
			hkfloorInput.setBounds(x, y += 40, 70, 30);
			modiButton.setBounds(p2.getWidth() / 2 - 35, p2.getHeight() - 70, 70, 30);
			modiButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String namelabeltemp = null;
					if (!namelabelinput.getText().isEmpty()) {
						namelabeltemp = namelabelinput.getText();
					}
					String hkstatusInputtemp = null;
					if (!hkstatusInput.getText().isEmpty())
						hkstatusInputtemp = hkstatusInput.getText();
					int floortemp = -1;
					if (!hkfloorInput.getText().isEmpty())
						floortemp = Integer.valueOf(hkfloorInput.getText());

					Time timeInputtemp = null;
					try {
						if (!timeInput.getText().isEmpty())
							timeInputtemp = Time.valueOf(timeInput.getText());
					} catch (Exception e1) {
					}
					if (namelabeltemp == null || timeInputtemp == null || hkstatusInputtemp == null
							|| floortemp == -1) {
						JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.", "확인", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane.showConfirmDialog(null,
								"이름    : " + namelabeltemp + "\n시간    : " + timeInputtemp + "\n상태    : "
										+ hkstatusInputtemp + "\n층  : " + floortemp,
								"확인", JOptionPane.OK_CANCEL_OPTION);

						if (result == 0) { // OK=0 , Cancel=2 리턴
							dto.setName(namelabeltemp);
							dto.setTime(timeInputtemp);
							dto.setState(hkstatusInputtemp);
							dto.setFloor(floortemp);
							for (int i = 0; i < hkList.size(); i++) {
								if (hkList.get(i).getEmployeeId() == dto.getEmployeeId()) {
									hkList.set(i, dto);
									break;
								}
							}
							HKModel.removeRow(row);
							addDataHKstatusModel(dto);
//							pbl2.MainActivity.sql.query("UPDATE tblHk set hkName = '"+dto.getName()+"', hkTime = '"+ dto.getTime()
//							+"', hkState = '" + dto.getState() + "', hkFloor = '" + dto.getFloor() + "where hkEmployeeID = " + dto.getEmployeeId());
//							pbl2.MainActivity.sql.commit();
//							dispose();
						}
					}

				}
			});
			p2.add(namelabel);
			p2.add(time);
			p2.add(hkstatus);
			p2.add(namelabelinput);
			p2.add(timeInput);
			p2.add(hkstatusInput);
			p2.add(hkfloor);
			p2.add(hkfloorInput);
			p2.add(modiButton);
			this.add(p2);
		}

		// edit fixture model
		ModifyDialog(JFrame frame, String title, DtoFixture dto1, DtoRcFixture dto, int row) {
			super(frame, title);
			this.setLayout(null);
			this.setSize(350, 300);
			this.setLocationRelativeTo(null);
			JPanel p2 = new JPanel();
			p2.setLayout(null);
			p2.setBounds(0, 0, 340, 290);
			namelabel = new JLabel("이름      :");
			fixQuantity = new JLabel("재고      :");
			fixDate = new JLabel("날짜      :");
			fixPhone = new JLabel("번호      :");
			namelabelInput = new JTextField(dto.getName());
			fixQuantityInput = new JTextField(String.valueOf(dto.getQuantity()));
			fixDateInput = new JTextField(String.valueOf(dto.getDate()));
			fixPhoneInput = new JTextField(String.valueOf(dto.getPhone()));
			int x = 10;
			int y = 0;
			namelabel.setBounds(x, y += 40, 90, 30);
			fixQuantity.setBounds(x, y += 40, 90, 30);
			fixDate.setBounds(x, y += 40, 90, 30);
			fixPhone.setBounds(x, y += 40, 90, 30);
			x += 70;
			y = 0;
			namelabelInput.setBounds(x, y += 40, 70, 30);
			fixQuantityInput.setBounds(x, y += 40, 90, 30);
			fixDateInput.setBounds(x, y += 40, 70, 30);
			fixPhoneInput.setBounds(x, y += 40, 120, 30);
			modiButton.setBounds(p2.getWidth() / 2 - 35, p2.getHeight() - 70, 70, 30);

			fixQuantityInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = fixQuantityInput.getText().length();
					if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
						return;
					}
					if (!Character.isDigit(c)) {
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
			fixDateInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = fixDateInput.getText().length();
					if (len < 10) {
						if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
							return;
						}
						if (!Character.isDigit(c)) {
							e.consume();
							return;
						}
						if (len == 4 || len == 7) {
							fixDateInput.setText(fixDateInput.getText() + "-");
						}
					} else if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
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
			fixPhoneInput.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					int len = fixPhoneInput.getText().length();
					if (len < 13) {
						if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
							return;
						}
						if (!Character.isDigit(c)) {
							e.consume();
							return;
						}
						if (len == 3 || len == 8) {
							fixPhoneInput.setText(fixPhoneInput.getText() + "-");
						}
					} else if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE || e.getKeyCode() != KeyEvent.VK_DELETE) {
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
			modiButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String namelabeltemp = null;
					if (!namelabelInput.getText().isEmpty()) {
						namelabeltemp = namelabelInput.getText();
					}
					int fixQuantitytemp = -1;
					if (!fixQuantityInput.getText().isEmpty()) {
						fixQuantitytemp = Integer.valueOf(fixQuantityInput.getText());
					}
					Date fixDatetemp = null;
					if (!fixDateInput.getText().isEmpty())
						fixDatetemp = Date.valueOf(fixDateInput.getText());

					String fixPhonetemp = null;
					if (!fixPhoneInput.getText().isEmpty())
						fixPhonetemp = (fixPhoneInput.getText());
					if (namelabeltemp == null || fixDatetemp == null || fixPhonetemp == null || fixQuantitytemp == -1) {
						JOptionPane.showMessageDialog(null, "입력란을 확인해주세요.", "확인", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane
								.showConfirmDialog(null,
										"이름    : " + namelabeltemp + "\n재고    : " + fixQuantitytemp + "\n날짜    : "
												+ fixDatetemp + "\n전화 : " + fixPhonetemp,
										"확인", JOptionPane.OK_CANCEL_OPTION);

						if (result == 0) { // OK=0 , Cancel=2 리턴
							dto.setName(namelabeltemp);
							dto.setQuantity(fixQuantitytemp);
							dto.setDate(fixDatetemp);
							dto.setPhone(fixPhonetemp);
							int i = 0;
							for (i = 0; i < rcFixtureList.size(); i++) {
								if (rcFixtureList.get(i).getFixtureId() == dto.getFixtureId()) {
									rcFixtureList.set(i, dto);
									break;
								}
							}
							
							FixModel.setValueAt(dto.getName(), Fixtable.getSelectedRow(), 1);
							FixModel.setValueAt(dto.getQuantity(), Fixtable.getSelectedRow(), 2);
							FixModel.setValueAt(dto.getDate(), Fixtable.getSelectedRow(), 3);
							FixModel.setValueAt(dto.getPhone(), Fixtable.getSelectedRow(), 4);
							pbl2.MainActivity.sql
									.query("UPDATE TBLRCFIXTURE set rcfName = '" + dto.getName() + "', rcfQuantity = "
											+ dto.getQuantity() + ", rcfDate = '" + dto.getDate() + "', rcfPhone = '"
											+ dto.getPhone() + "' where rcfFixtureID = " + dto.getFixtureId());
							pbl2.MainActivity.sql.commit();
							dispose();
							ResultSet rs = pbl2.MainActivity.sql.query("select * from TBLRCFIXTURE");
							try {
								while (rs.next()) {
									System.out.println("FixtureID: " + rs.getInt("rcfFixtureID"));
									System.out.println("Name: " + rs.getString("rcfName"));
									System.out.println("Quantity: " + rs.getInt("rcfQuantity"));
									System.out.println("Phone: " + rs.getString("rcfPhone"));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							pbl2.MainActivity.sql.query("UPDATE tblHk set hkName = '"+dto.getName()+"', hkTime = '"+ dto.getTime()
//							+"', hkState = '" + dto.getState() + "', hkFloor = '" + dto.getFloor() + "where hkEmployeeID = " + dto.getEmployeeId());
//							pbl2.MainActivity.sql.commit();
//							dispose();
						}
					}

				}
			});
			p2.add(namelabel);
			p2.add(fixQuantity);
			p2.add(fixDate);
			p2.add(fixPhone);
			p2.add(namelabelInput);
			p2.add(fixQuantityInput);
			p2.add(fixDateInput);
			p2.add(fixPhoneInput);
			p2.add(modiButton);
			this.add(p2);
		}

	}

	private void makearrangeModel() {
		String[] colName = { "객실", "요청사항", "상태", "배치" };
		ArrangeModel = new DefaultTableModel(colName, 0);
	}

	private void makeHKstatusModel() {
		String[] colName = { "이름", "ID", "담당 층", "최근 배치시간", "상태", };
		HKModel = new DefaultTableModel(colName, 0);
	}

	private void makeFixturestatusModel() {
		String[] colName = { "ID", "이름", "재고", "마지막 입고날짜", "업체 연락처" };
		FixModel = new DefaultTableModel(colName, 0);
	}

	private void addDataHKstatusModel(DtoHk dto) {
		String[] data = new String[5];
		data[0] = dto.getName();
		data[1] = String.valueOf(dto.getEmployeeId());
		data[2] = String.valueOf(dto.getFloor());
		data[3] = String.valueOf(dto.getTime());
		data[4] = dto.getState();
		HKModel.addRow(data);
	}

	private void addDataFixturestatusModel(DtoRcFixture dto, DtoFixture dto1) {
		String[] data = new String[5];
		data[0] = String.valueOf(dto.getFixtureId());
		data[1] = String.valueOf(dto.getName());
		data[2] = String.valueOf(dto.getQuantity());
		data[3] = String.valueOf(dto.getDate());
		data[4] = String.valueOf(dto.getPhone());
		FixModel.addRow(data);
	}

	public JPanel getPanel() {
		return mainPan;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
