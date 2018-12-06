package pbl2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

import pbl2.controller.ViewCustomer;
import pbl2.controller.ViewEmployee;
import pbl2.controller.ViewReservation;
import pbl2.controller.ViewRoomService;
import pbl2.dto.DtoBookedRoom;
import pbl2.dto.DtoCleanedRoom;
import pbl2.dto.DtoCustomer;
import pbl2.dto.DtoDish;
import pbl2.dto.DtoDishOrder;
import pbl2.dto.DtoEmployee;
import pbl2.dto.DtoFixture;
import pbl2.dto.DtoHk;
import pbl2.dto.DtoHotel;
import pbl2.dto.DtoMileage;
import pbl2.dto.DtoRcFixture;
import pbl2.dto.DtoReceipt;
import pbl2.dto.DtoRepair;
import pbl2.dto.DtoRoom;

public class MainActivity implements ActionListener{
	private String auth;
	private JFrame jf;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuLogout;
	private JPanel firstPan, secondPan, thirdPan, fourthPan, fifthPan, sixthPan;
	private JLabel label;
	private JTabbedPane jtp;
	private double width, height;
	static public ArrayList<DtoBookedRoom> bookedRoomList = new ArrayList<>();
	static public ArrayList<DtoCleanedRoom> cleanedRoomList = new ArrayList<>();
	static public ArrayList<DtoCustomer> customerList = new ArrayList<>();
	static public ArrayList<DtoDish> dishList = new ArrayList<>();
	static public ArrayList<DtoDishOrder> dishOrderList = new ArrayList<>();
	static public ArrayList<DtoEmployee> employeeList = new ArrayList<>();
	static public ArrayList<DtoFixture> fixtureList = new ArrayList<>();
	static public ArrayList<DtoHk> hkList = new ArrayList<>();
	static public ArrayList<DtoHotel> hotelList = new ArrayList<>();
	static public ArrayList<DtoMileage> mileageList = new ArrayList<>();
	static public ArrayList<DtoRcFixture> rcFixtureList = new ArrayList<>();
	static public ArrayList<DtoReceipt> receiptList = new ArrayList<>();
	static public ArrayList<DtoRepair> repairList = new ArrayList<>();
	static public ArrayList<DtoRoom> roomList = new ArrayList<>();
	
	private String[] tableList = {
			"TBLBOOKEDROOM"//0
			,"TBLCLEANEDROOM"//1
			,"TBLCUSTOMER"//2
			,"TBLDISH"//3
			,"TBLDISHORDER"//4
			,"TBLEMPLOYEE"//5
			,"TBLFIXTURE"//6
			,"TBLHOTEL"//7
			,"TBLMILEAGE"//8
			,"TBLRCFIXTURE"//9
			,"TBLRECEIPT"//10
			,"TBLREPAIR"//11
			,"TBLROOM"//12
			,"TBLHK"//13
			};
	static public SqlHelper sql;
	ResultSet rs;
	JProgressBar b;
	public MainActivity(String auth) {
		this.auth = auth;
		sql = new SqlHelper();
		
		getDataFromDB();
//		mainActivity();
	}
	
	Thread ct;
	private void getDataFromDB() {
        jf = new JFrame("DATA LOADING");
        jf.setLayout(null);
        jf.setSize(300, 100);
        
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 300, 100);
        label = new JLabel();
        
        b = new JProgressBar();
        b.setValue(0);
        b.setStringPainted(true);
        b.setBounds(0,0, 300, 30);
        label.setBounds(120,b.getHeight(),300,30);
        p.add(b);
        p.add(label);
        
        jf.add(p);
        jf.setLocationRelativeTo(null);//make the frame as center
        jf.setVisible(true);
        ct = new Thread(new Runnable() {
			String txt = ".";
			@Override
			public void run() {
				try {
					while(true) {
						if(txt.length()==5) {
							txt = "";
						}
						label.setText("Calculating"+txt);
						Thread.sleep(400);
						txt+=".";
					}
				} catch (InterruptedException e) {
				}
			}
		});
        ct.start();
        fill();
        
    } 
  
    // function to increase progress 
    private void fill() { 
    	Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int max = 0;
				int start = 0;
				sql.open();
				String temp = "SELECT COUNT(*) FROM ";
				for(int i = 0; i< tableList.length; i++) {
					rs = sql.query(temp+tableList[i]);
					try {
						if(rs != null) {
							while(rs.next()) {
								max += rs.getInt("count(*)");
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				ct.interrupt();
				label.setText("0 / "+max);
		        b.setVisible(true);
		        int cur = 0;
		        try { 
	                temp = "SELECT * from ";
	                for(int i = 0 ; i < tableList.length; i++) {
	                	rs = sql.query(temp+tableList[i]);
	                	while(rs.next()) {
	                		if(i == 0) {//"TBLBOOKEDROOM"//0
	                			bookedRoomList.add(new DtoBookedRoom(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDate(4), rs.getDate(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getString(9)));
	                		}else if(i == 1) {//"TBLCLEANEDROOM"//1
	                			cleanedRoomList.add(new DtoCleanedRoom(rs.getInt(1), rs.getInt(2), rs.getString(3)));
	                		}else if(i == 2) {//"TBLCUSTOMER"//2
	                			customerList.add(new DtoCustomer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
	                		}else if(i == 3) {//"TBLDISH"//3
	                			dishList.add(new DtoDish(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getBlob(4)));
	                		}else if(i == 4) {//"TBLDISHORDER"//4
	                			dishOrderList.add(new DtoDishOrder(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDate(4), rs.getTime(5), rs.getInt(6), rs.getString(7)));
	                		}else if(i == 5) {//"TBLEMPLOYEE"//5
	                			employeeList.add(new DtoEmployee(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6), rs.getString(7), rs.getInt(8)));
	                		}else if(i == 6) {//"TBLFIXTURE"//6
	                			fixtureList.add(new DtoFixture(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
	                		}else if(i == 7) {//"TBLHOTEL"//7
	                			hotelList.add(new DtoHotel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7)));
	                		}else if(i == 8) {//"TBLMILEAGE"//8
	                			mileageList.add(new DtoMileage(rs.getInt(1), rs.getInt(2)));
	                		}else if(i == 9) {//"TBLRCFIXTURE"//9
	                			rcFixtureList.add(new DtoRcFixture(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getDate(5), rs.getString(6)));
	                		}else if(i == 10) {//"TBLRECEIPT"//10
	                			receiptList.add(new DtoReceipt(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getTime(4), rs.getString(5), rs.getInt(6)));
	                		}else if(i == 11) {//"TBLREPAIR"//11
	                			repairList.add(new DtoRepair(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDate(4), rs.getTime(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)));
	                		}else if(i == 12) {//"TBLROOM"//12
	                			roomList.add(new DtoRoom(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8)));
	                		}else if(i == 13) {//"TBLHK"//13
	                			hkList.add(new DtoHk(rs.getInt(1), rs.getString(2), rs.getTime(3), rs.getString(4)));
	                		}
	                		start++;
	                		cur = (int)(((float)start/max)*100.0);
	                		b.setValue(cur);
	                		label.setText(start+" / " + max);
	                		Thread.sleep(10);
	                	}
	                }
	                if(start >= max) {
	                	jf.setVisible(false);
	                	mainActivity();
	                }
		        }catch (Exception e) {
		        	e.printStackTrace();
		        } 
				
			}
		});
    	t.start();
    } 

	private void mainActivity() {
		
		jf = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth() * 0.8;
		height = width * 0.6;
		jf.setSize((int) width, (int) height);	
		System.out.println("height : "+ (int)height + ", jf.getHeight() : " + jf.getHeight());
		height -= 90;//remove tab size;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("2 Jo SulNong-Tang");
		
		jtp = new JTabbedPane();
		jtp.setBounds(0, 0, (int)width, (int)height);
		System.out.println("jtp.width : "+ jtp.getWidth()+"jtp.height : "+ jtp.getHeight());
		
		makeMenu();
		makePanWithAuth(auth);
		
		jf.add(jtp);
		jf.setLocationRelativeTo(null);// make the frame as center
		jf.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == menuLogout) {
			jf.setVisible(false);
			sql.close();
			new LoginActivity();
		}
	}
	
	private void makeMenu() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		jf.setJMenuBar(menuBar);
		menu = new JMenu("options");
		menu.setMnemonic('O');
		menuLogout = new JMenuItem("LogOut");
		menuLogout.addActionListener(this);
		menu.add(menuLogout);
		
		menuBar.add(menu);
	}
	
	private void makePanWithAuth(String auth) {
		if(auth.equals("cus")) {
			makeSecondPan();
		}else if(auth.equals("emp")) {
			makeFirstPan();
			makeSecondPan();
			makeFourthPan();
			makeFifthPan();			
		}else{//adm
			makeFirstPan();
			makeSecondPan();
			makeThirdPan();
			makeFourthPan();
			makeFifthPan();
			makeSixthPan();
		}
	}

	private void makeFirstPan() {
		firstPan = new JPanel();
		jtp.addTab("객실", null, firstPan, "현재 객실의 상태를 보여줍니다.");
		JPanel listPan = new JPanel();
		JLabel listLabel = new JLabel("<html>x:"+jtp.getX()+"<br> y:"+jtp.getY()+"</html>");
		firstPan.setLayout(null);
		System.out.println((int)jf.getHeight());
		System.out.println(height);
		listPan.setBounds(0, 0, (int)(jtp.getWidth()*0.05), jtp.getHeight());
		listPan.add(listLabel);
		listLabel.setForeground(Color.white);
		listPan.setBackground(Color.BLACK);
		firstPan.add(listPan);
		
		JPanel[] t = new JPanel[10];
		JLabel[] p = new JLabel[10];
		double temp = 0.05;
		for(int i = 1; i < 10; i++) {
			p[i-1] = new JLabel();
			p[i-1].setText("<html>"+(i-1)+"<br>"+(int)jtp.getHeight()+"-"+i+"*10"+"<br>="+(int)(jtp.getHeight()-i*10)+"</html>");
			p[i-1].setForeground(Color.WHITE);
			t[i-1] = new JPanel();
			t[i-1].setBounds((int)(Math.floor(jtp.getWidth()*temp)), 0, (int)(jtp.getWidth()*0.05), (int)(jtp.getHeight()-i*10));
			t[i-1].add(p[i-1]);
			t[i-1].setBackground(Color.black);
			firstPan.add(t[i-1]);
			temp += 0.05;
		}
		
	}

	private void makeSecondPan() {
		secondPan = new ViewReservation(jtp.getWidth(), jtp.getHeight()).getPanel();
		jtp.addTab("예약", null, secondPan, "캘린더창으로 이동합니다.");
	}

	private void makeThirdPan() {
		thirdPan = new ViewEmployee(employeeList, jtp.getWidth(), jtp.getHeight()).getPanel();
		jtp.addTab("직원", null, thirdPan, "직원관리창으로 이동합니다.");
	}

	private void makeFourthPan() {
		fourthPan = new ViewRoomService().getPanel();
		jtp.addTab("룸서비스", null, fourthPan, "룸서비스창으로 이동합니다.");
	}

	private void makeFifthPan() {
		fifthPan = new JPanel();
		jtp.addTab("하우스키퍼", null, fifthPan, "하우스키퍼창으로 이동합니다.");
	}
	private void makeSixthPan() {
		sixthPan = new ViewCustomer(customerList, mileageList, jtp.getWidth(), jtp.getHeight()).getPanel();
		jtp.addTab("고객", null, sixthPan, "고객창으로 이동합니다.");
	}
	
	
}
