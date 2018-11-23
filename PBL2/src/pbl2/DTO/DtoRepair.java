package pbl2.DTO;

import java.sql.Date;
import java.sql.Time;

public class DtoRepair {
	private int index;
	private int roomId;
	private String product;
	private Date date;
	private Time time;
	private String repaired;
	private int price;
	private String name;
	private String phone;
	private String email;
	private String description;
	public DtoRepair(int index, int roomId, String product, Date date, Time time, String repaired, int price,
			String name, String phone, String email, String description) {
		super();
		this.index = index;
		this.roomId = roomId;
		this.product = product;
		this.date = date;
		this.time = time;
		this.repaired = repaired;
		this.price = price;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.description = description;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public String getRepaired() {
		return repaired;
	}
	public void setRepaired(String repaired) {
		this.repaired = repaired;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
