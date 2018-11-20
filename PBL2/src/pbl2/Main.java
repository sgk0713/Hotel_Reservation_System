package pbl2;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		/*
		 * READ ME!! Before start
		 * CHECK id, password, and port number of database
		 * IN DbConnection Class
		 */
		
		new CreateTable();
		
		/*
		  EXAMPLE!!
		  
		SqlHelper sql = new SqlHelper();
		ResultSet rs = null;
		sql.open("system", "oracle");
		rs = sql.query("SELECT speed from pc");		 
		try {
			if(rs != null) {
				while(rs.next()) {
					System.out.println("avg(speed): " + rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.close();
		
		*/
	}

}
