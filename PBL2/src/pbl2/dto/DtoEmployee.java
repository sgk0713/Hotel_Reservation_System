package pbl2.dto;
import java.sql.Date;

public class DtoEmployee {
	private int employeeId;
	private int hotelId;
	private String name;
	private String department;
	private String gender;
	private Date dateEnter;
	private String position;
	private int salary;
	private String state;
	public DtoEmployee(int employeeId, int hotelId, String name, String department, String gender, Date dateEnter,
			String position, int salary) {
		super();
		this.employeeId = employeeId;
		this.hotelId = hotelId;
		this.name = name;
		this.department = department;
		this.gender = gender;
		this.dateEnter = dateEnter;
		this.position = position;
		this.salary = salary;
	}
	public DtoEmployee(int employeeId, int hotelId, String name, String department, String gender, Date dateEnter,
			String position, int salary, String state) {
		super();
		this.employeeId = employeeId;
		this.hotelId = hotelId;
		this.name = name;
		this.department = department;
		this.gender = gender;
		this.dateEnter = dateEnter;
		this.position = position;
		this.salary = salary;
		this.state = state;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDateEnter() {
		return dateEnter;
	}
	public void setDateEnter(Date dateEnter) {
		this.dateEnter = dateEnter;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
