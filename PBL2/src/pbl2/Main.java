package pbl2;

public class Main {
	static SqlHelper sql;
	public static void main(String[] args) {
		/*
		 * READ ME!! Before start
		 * CHECK id, password, and port number of database
		 * In SqlHelper Class
		 */
		
//		new CreateTable();//this method drops and puts all data on your database; make this as comment if you don't want to
		new LoginActivity();
//		new MainActivity("adm");
//		LogWriteHelper em = new LogWriteHelper("insertList_tblEmployee");
//		em.append("INSERT INTO tblEmployee (eEmployId, eHotelId, eName, eDepartment, eGender, eDateEnter, ePosition, eSalary) VALUES ()");
		
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
