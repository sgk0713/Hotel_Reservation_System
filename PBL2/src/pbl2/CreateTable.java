package pbl2;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class CreateTable {
	SqlHelper sql;
	public CreateTable() {
		sql = new SqlHelper();
		sql.open();
		dropTable();
		generateTable();
		
		String[] list = {
				"insertList_tblHotel",
				"insertList_tblFixture",
				"insertList_tblEmployee",
				"insertList_tblRoom",
				"insertList_tblCustomer",
				"insertList_tblMileage",
				"insertList_tblBookedRoom",
				"insertList_tblCleanedRoom",
				"insertList_tblDish",
				"insertList_tblDishOrder",
				"insertList_tblRcFixture",
				"insertList_tblReceipt",
				"insertList_tblRepair",
				"insertList_tblHk"
		};
		for(int i = 0; i< list.length; i++) {
			System.out.println(" ******* READING... < "+list[i]+" >... *******");
			insertValues(list[i]);
			System.out.println(" ******* COMPLETED. < "+list[i]+" >    *******\n\n");
		}
		
		System.out.println(" ******* READING... < tblDish_DATABASE >... *******");
		sql.query("UPDATE tblDish set DMIMAGE = EMPTY_BLOB()");
		String[] dishNames = new String[sql.getRowNumber("tblDish")];
		ResultSet rs = sql.query("select dmname from tbldish");
		int num = 0;
		try {
			while(rs.next()) {
				dishNames[num] = rs.getString(1);
				num++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		File blob;
		FileInputStream in;
		try {
			
			for(int i= 0;i < dishNames.length;i++) {
				String name = dishNames[i];
				sql.prstmt = sql.conn.prepareStatement("UPDATE tblDish set DMIMAGE = ? where dmname = '"+ name+"'");
				blob = new File(name+".png");
				
				//image resize
				Image image = ImageIO.read(blob);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				int newLength = 124;
				double ratio;
				int w;
				int h;
				if(imageWidth >= imageHeight){
					ratio = (double)newLength/(double)imageWidth;
				}else {
					ratio = (double)newLength/(double)imageHeight;
				}
				w = (int)(imageWidth * ratio);
				h = (int)(imageHeight * ratio);
				Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_FAST);
				BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = newImage.getGraphics();
				g.drawImage(resizeImage, 0, 0, null);
				g.dispose();
				ImageIO.write(newImage, "png", new File(name+".png"));
				
				//put image in DB
				blob = new File(name+".png");
				in = new FileInputStream(blob);
				sql.prstmt.setBinaryStream(1, in, (int)blob.length());
				sql.prstmt.executeUpdate();
				sql.conn.commit();
				System.out.println("tblDish #"+ (i+1) +"] VALUE INSERTED!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" ******* COMPLETED. < tblDish_DATABASE >    *******\n\n");
		sql.close();
	}
	
	private void insertValues(String fileName) {
		Path list = Paths.get(fileName);
		int count = 1, tmpcount = 0;
		String[] tmp = new String[200];

//		SqlHelper sql = new SqlHelper();
//		sql.open();
		try {
			BufferedReader reader = Files.newBufferedReader(list);
			String line = reader.readLine();
			
			while(line != null) {
				System.out.print(fileName + " #"+count+"]");
				if(!sql.insertValue(line)) {
					tmp[tmpcount] = "Query #" + count +" :: " + line;
					tmpcount++;
				}
				line = reader.readLine();
				count++;
			}
		}catch (Exception e) {
			System.out.println(fileName+"[#"+count+ "] FILE READ FAILED... (X)");
			count++;
		}

		sql.commit();
//		sql.close();
		
		//it will show up if there is any error
		for(int i = 0; i< tmp.length; i++) {
			if(tmp[i]==null) {
				break;
			}
			System.out.println("ERROR LIST : " + tmp[i]);
		}

		
	}

	private void dropTable() {
		Path list = Paths.get("dropList");
		int count = 1;

//		SqlHelper sql = new SqlHelper();
//		sql.open();
		try {
			BufferedReader reader = Files.newBufferedReader(list);
			String line = reader.readLine();
			
			while(line != null) {
				sql.query(line);
				System.out.println("DROPPED");
				line = reader.readLine();
				count++;
			}
		}catch (Exception e) {
			System.out.println("dropList[#"+count+ "] FILE READ FAILED... (X)");
		}
		
		sql.commit();
//		sql.close();
	}
	
	private void generateTable() {
		Path list = Paths.get("createList");
		int count = 1, tmpcount = 0;
		String[] tmp = new String[200];

//		SqlHelper sql = new SqlHelper();
//		sql.open();
		try {
			BufferedReader reader = Files.newBufferedReader(list);
			String line = reader.readLine();
			
			while(line != null) {
				System.out.print(count+"]");
				if(!sql.createTable(line)) {
					tmp[tmpcount] = "Query #" + count +" :: " + line;
					tmpcount++;
				}
				line = reader.readLine();
				count++;
			}
		}catch (Exception e) {
			System.out.println("createList[#"+count+ "] FILE READ FAILED... (X)");
			count++;
		}
		sql.commit();
//		sql.close();
		
		//it will show up if there is any error
		for(int i = 0; i< tmp.length; i++) {
			if(tmp[i]==null) {
				break;
			}
			System.out.println("ERROR LIST : " + tmp[i]);
		}
		
		
	}
}
