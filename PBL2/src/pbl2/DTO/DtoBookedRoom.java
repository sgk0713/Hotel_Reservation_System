package pbl2.DTO;

import java.sql.Date;

public class DtoBookedRoom {
	private int bookId;
	private int roomId;
	private int customerId;
	private Date dateEnter;
	private Date dateExit;
	private int adult;
	private int children;
	private int hkId;
	public DtoBookedRoom(int bookId, int roomId, int customerId, Date dateEnter, Date dateExit, int adult, int children,
			int hkId) {
		super();
		this.bookId = bookId;
		this.roomId = roomId;
		this.customerId = customerId;
		this.dateEnter = dateEnter;
		this.dateExit = dateExit;
		this.adult = adult;
		this.children = children;
		this.hkId = hkId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public Date getDateEnter() {
		return dateEnter;
	}
	public void setDateEnter(Date dateEnter) {
		this.dateEnter = dateEnter;
	}
	public Date getDateExit() {
		return dateExit;
	}
	public void setDateExit(Date dateExit) {
		this.dateExit = dateExit;
	}
	public int getAdult() {
		return adult;
	}
	public void setAdult(int adult) {
		this.adult = adult;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public int getHkId() {
		return hkId;
	}
	public void setHkId(int hkId) {
		this.hkId = hkId;
	}
}
