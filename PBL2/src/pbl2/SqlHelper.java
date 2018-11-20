package pbl2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {

	private Connection conn;
	
	//JDBC를 사용하기 위한 변수 선언.
	private final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String IP_ADDRESS = "127.0.0.1";
	private final String PORT = "59161"; //오라클 포트번호는 1521 이지만 맥에서 docker 사용으로 포트우회
	private final String DB_URL = "jdbc:oracle:thin:@"+IP_ADDRESS+":"+PORT+":xe";
	//DB 접속 아이디, 비밀번호.
	private String user = "system"; //아이디
	private String pass = "oracle"; //비밀번호
	private Statement stmt;
	
	
	public SqlHelper() {
		try {
            Class.forName(JDBC_DRIVER);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("SQLException : " + e);
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
			System.out.println("SQLExceptoin: " + e);
			return false;
		}catch (Exception e) {
			System.out.println("Exception: " + e);
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
			System.out.println("SQLExceptoin: " + e);
			return false;
		}catch (Exception e) {
			System.out.println("Exception: " + e);
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
            System.out.println("Exception: " + e);
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
			return null;
		}
		return rs;
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
			System.out.println("Exception : " + e);
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
			System.out.println("Exception : " + e);
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
			System.out.println("Exception : " + e);
		}
		return false;
	}
	
	
	public boolean commit() {
		try {
			conn.commit();
			System.out.println("     (O) COMMIT SUCCEED!! (O)");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("     (X) COMMIT FAILED... (X)");
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
		}
		return false;
	}

}
