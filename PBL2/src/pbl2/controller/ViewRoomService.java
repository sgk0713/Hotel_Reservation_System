package pbl2.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pbl2.SqlHelper;
import pbl2.dto.DtoDish;

public class ViewRoomService {
	SqlHelper sql;
	private JPanel mainPan;
	private ArrayList<DtoDish> list;
	public ViewRoomService() {
		mainPan = new JPanel();
		mainPan.setLayout(null);
		this.sql = pbl2.MainActivity.sql;
		list = pbl2.MainActivity.dishList;

		try {
			
			JLabel[] s = new JLabel[list.size()];
			JLabel[] text = new JLabel[list.size()];
			mainPan.setBounds(0,0, 800, 800);
			
			int x = 0;
			int y = 0;
			int space = 11;
			for(int i = 0; i<list.size();i++) {
				System.out.println(list.get(i).getName());
				Blob blob = list.get(i).getImage();
				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				int nFileSize;
				nFileSize = (int)blob.length();
				byte[] buf = new byte[nFileSize];
				in.read(buf, 0, nFileSize);	
				in.close();
				
				ImageIcon ic = new ImageIcon(buf);
				s[i] = new JLabel();
				text[i] = new JLabel();
				
				s[i].setBounds(x,  y, 124, 124);
				s[i].setIcon(ic);
				text[i].setBounds(s[i].getX(), s[i].getY()+s[i].getHeight(), 124, 10);
				text[i].setText(list.get(i).getName() + " : " + list.get(i).getPrice());
				x = s[i].getX()+s[i].getWidth()+space;
				
				
				
				mainPan.add(s[i]);
				mainPan.add(text[i]);
			}
			
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JPanel getPanel() {
		return mainPan;
	}
}
