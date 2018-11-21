package pbl2;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateTable {
	public CreateTable() {
		dropTable();
		generateTable();
		
		String[] list = {
				"insertList_tblHotel",
				"insertList_tblCustomer",
				"insertList_tblBookedRoom",
				"insertList_tblCleanedRoom",
				"insertList_tblDish",
				"insertList_tblDishOrder",
				"insertList_tblEmployee",
				"insertList_tblFixture",
				"insertList_tblMileage",
				"insertList_tblRcFixture",
				"insertList_tblReceipt",
				"insertList_tblRepair",
				"insertList_tblRoom"
		};
		for(int i = 0; i< list.length; i++) {
			System.out.println(" ******* READING... < "+list[i]+" >... *******");
			insertValues(list[i]);
			System.out.println(" ******* COMPLETED. < "+list[i]+" >    *******\n\n");
		}
	}
	
	private void insertValues(String fileName) {
		Path list = Paths.get(fileName);
		int count = 1, tmpcount = 0;
		String[] tmp = new String[200];

		SqlHelper sql = new SqlHelper();
		sql.open();
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
		sql.close();
		
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

		SqlHelper sql = new SqlHelper();
		sql.open();
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
		sql.close();
	}
	
	private void generateTable() {
		Path list = Paths.get("createList");
		int count = 1, tmpcount = 0;
		String[] tmp = new String[200];

		SqlHelper sql = new SqlHelper();
		sql.open();
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
		sql.close();
		
		//it will show up if there is any error
		for(int i = 0; i< tmp.length; i++) {
			if(tmp[i]==null) {
				break;
			}
			System.out.println("ERROR LIST : " + tmp[i]);
		}
		
		
	}
}
