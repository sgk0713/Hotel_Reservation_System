package pbl2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {

	public Connection conn;
	
	//Variables for JDBC
	private final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String IP_ADDRESS = "127.0.0.1";
	private final String PORT = "59161"; //general port number is 1521.
	private final String DB_URL = "jdbc:oracle:thin:@"+IP_ADDRESS+":"+PORT+":xe";
	//DB user, password
	private String user = "system"; //user
	private String pass = "oracle"; //password
	public Statement stmt;
	public PreparedStatement prstmt;
	
	
	public SqlHelper() {
		try {
            Class.forName(JDBC_DRIVER);
        } catch(Exception e) {
        	System.out.println("SqlHelper() ERROR :\n\tL " + e.getMessage());
        }
	}
	
	public boolean open(){
		try {
			conn = DriverManager.getConnection(DB_URL, user, pass);
			System.out.println(" (====)  DATABASE    CONNECTION SUCCEED!! (====)");
			stmt = conn.createStatement();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("    (X)  DATABASE    CONNECTION FAILED... (X)");
			System.out.println("SqlHelper.open() SQL ERROR :\n\tL " + e.getMessage());
			return false;
		}catch (Exception e) {
			System.out.println("SqlHelper.open() ERROR :\n\tL " + e.getMessage());
			return false;
		}
	}
	
	public boolean open(String user, String pass){
		this.user = user;
		this.pass = pass;
		
		try {
			conn = DriverManager.getConnection(DB_URL, user, pass);
			System.out.println(" (====)  DATABASE    CONNECTION SUCCEED!! (====)");
			stmt = conn.createStatement();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("    (X)  DATABASE    CONNECTION FAILED... (X)");
			System.out.println("SqlHelper.open(user, pass) SQL ERROR :\n\tL " + e.getMessage());
			return false;
		}catch (Exception e) {
			System.out.println("SqlHelper.open(user, pass) ERROR :\n\tL " + e.getMessage());
			return false;
		}
	}
	
	public boolean close() {
		try {
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();				
			}
			System.out.println(" (==/==) DATABASE DISCONNECTION SUCCEED!! (==/==)");
			return true;
        } catch (SQLException e) {
        	System.out.println("SqlHelper.close() ERROR :\n\tL " + e.getMessage());
            return false;
        }
	}

	
	public ResultSet query(String sql) {
		ResultSet rs = null;
		try {
			if(stmt != null) {
				rs = stmt.executeQuery(sql);
			}else {
				System.out.println("USE open() method first...");
			}
		} catch (SQLException e) {
			System.out.println("SqlHelper.query() ERROR :\n\tL " + e.getMessage());
			return null;
		}
		return rs;
	}
	
	public int getColumnNumber(String tableName) {
		try {
			String query = "select count(column_name) from cols where table_name = '" + tableName.toUpperCase() + "'";
			ResultSet rs = query(query);
			rs.next();
			int count = rs.getInt(1);
			return count;
		} catch (SQLException e) {
			System.out.println("SqlHelper.getColumnNumber() ERROR :\n\tL " + e.getMessage());
			return 0;
		}	
	}
	
	public int getRowNumber(String tableName) {
		try {
			String query = "select count(*) from " + tableName.toUpperCase();
			ResultSet rs = query(query);
			rs.next();
			int count = rs.getInt(1);
			return count;
		} catch (SQLException e) {
			System.out.println("SqlHelper.getRowNumber() ERROR :\n\tL " + e.getMessage());
			return 0;
		}	
	}
	
	public String[] getColumnName(String tableName) {
		try {
			String query = "select column_name from cols where table_name = '" + tableName.toUpperCase() + "'";
			int num = getColumnNumber(tableName);
			if(num == 0)
				return null;
			String[] tables = new String[num];
			ResultSet rs = query(query);
			for(int i = 0; rs.next(); i++) {
				tables[i] = rs.getString(1);
			}
			return tables;
		}catch (SQLException e) {
			System.out.println("SqlHelper.getColumnName() ERROR :\n\tL " + e.getMessage());
			return null;
		}
	}
	
	public boolean createTable(String sql) {
		try {
			if(stmt != null) {
				stmt.executeUpdate(sql);
				System.out.println(" TABLE CREATED!!");
				
				return true;
			}else {
				System.out.println("USE open() method first...");
				return false;
			}
		}catch (Exception e) {
			System.out.println("SqlHelper.createTable() ERROR :\n\tL " + e.getMessage());
			
		}
		return false;
	}
	
	public boolean dropTable(String tbl) {
		try {
			if(stmt != null) {
				stmt.executeUpdate("drop table "+ tbl);
				System.out.println("TABLE '"+ tbl +"' DROPPED!!");
				return true;
			}else {
				System.out.println("USE open() method first...");
				return false;
			}
		}catch (Exception e) {
			System.out.println("SqlHelper.dropTable() ERROR :\n\tL " + e.getMessage());
			return false;
		}
	}
	
	public boolean insertValue(String sql) {
		try {
			if(stmt != null) {
				stmt.executeUpdate(sql);
				System.out.println("VALUE INSERTED!!");
				
				return true;
			}else {
				System.out.println("USE open() method first...");
				return false;
			}
		}catch (Exception e) {
			System.out.println("SqlHelper.insertVale() ERROR :\n\tL " + e.getMessage());
		}
		return false;
	}
	
	
	public boolean commit() {
		try {
			conn.commit();
			System.out.println("     (O) COMMIT SUCCEED!! (O)");
			return true;
		} catch (SQLException e) {
			System.out.println("     (X) COMMIT FAILED... (X)");
			System.out.println("SqlHelper.commit() ERROR :\n\tL " + e.getMessage());
			return false;
		}
	}
	
	public boolean isExist(String tbl) {
		try {
			ResultSet rs = query("select * from "+ tbl);
			if(rs==null) {
				return false;
			}else {
				return true;
			}
		}catch (Exception e) {
			System.out.println("isExist() fail");
			System.out.println("SqlHelper.isExist() ERROR :\n\tL " + e.getMessage());
		}
		return false;
	}

}
