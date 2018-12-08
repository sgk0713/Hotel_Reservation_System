package pbl2.dto;

import java.sql.Time;

public class DtoHk {
	private int employeeId;
	private String name;
	private Time time;
	private String state;
	private int floor;
	public DtoHk(int employeeId, String name, Time time, String state, int floor) {
		super();
		this.employeeId = employeeId;
		this.name = name;
		this.time = time;
		this.state = state;
		this.floor = floor;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
}
