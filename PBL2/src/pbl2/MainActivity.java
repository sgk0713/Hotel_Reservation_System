package pbl2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

public class MainActivity implements ActionListener{
	private String auth;
	private JFrame jf;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuLogout;
	private JPanel firstPan, secondPan, thirdPan, fourthPan, fifthPan;
	private JTabbedPane jtp;
	private double width, height;

	public MainActivity(String auth) {
		this.auth = auth;
		mainActivity();
	}

	private void mainActivity() {
		jf = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth() * 0.8;
		height = width * 0.6;
		jf.setSize((int) width, (int) height);		
		height-=90;//remove tab size;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("2 Jo SulNong-Tang");
		
		jtp = new JTabbedPane();
		jtp.setBounds(0, 0, (int)width, (int)height);
		
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
		}
	}

	private void makeFirstPan() {
		firstPan = new JPanel();
		jtp.addTab("객실", null, firstPan, "현재 객실의 상태를 보여줍니다");
		JPanel listPan = new JPanel();
		JLabel listLabel = new JLabel("체크인 / 체크아웃 리스트");
		firstPan.setLayout(null);
		System.out.println((int)jf.getHeight());
		System.out.println(height);
		listPan.setBounds(0, 0, (int)(jf.getWidth()*0.05), (int)height);
		System.out.println("height!#!@#!" + (int)height);
		listPan.add(listLabel);
		listPan.setBackground(Color.BLACK);
		firstPan.add(listPan);
		
		JPanel[] t = new JPanel[10];
		JLabel[] p = new JLabel[10];
		double temp = 0.05;
		for(int i = 1; i < 10; i++) {
			p[i-1] = new JLabel();
			p[i-1].setText("<html>"+(i-1)+"<br>"+(int)height+"-"+i+"*10"+"<br>="+(int)(height-i*10)+"</html>");
			p[i-1].setForeground(Color.WHITE);
			t[i-1] = new JPanel();
			t[i-1].setBounds((int)(Math.floor(jf.getWidth()*temp)), 0, (int)(jf.getWidth()*0.05), (int)(height-i*10));
			t[i-1].add(p[i-1]);
			t[i-1].setBackground(Color.black);
			firstPan.add(t[i-1]);
			temp += 0.05;
		}
		
	}

	private void makeSecondPan() {
		secondPan = new JPanel();
		jtp.addTab("예약", null, secondPan, "캘린더창으로 이동합니다");
	}

	private void makeThirdPan() {
		thirdPan = new JPanel();
		jtp.addTab("직원", null, thirdPan, "직원관리창으로 이동합니다");
	}

	private void makeFourthPan() {
		fourthPan = new JPanel();
		jtp.addTab("룸서비스", null, fourthPan, "룸서비스창으로 이동합니다");
	}

	private void makeFifthPan() {
		fifthPan = new JPanel();
		jtp.addTab("하우스키퍼", null, fifthPan, "하우스키퍼창으로 이동합니다");
	}
	
}
