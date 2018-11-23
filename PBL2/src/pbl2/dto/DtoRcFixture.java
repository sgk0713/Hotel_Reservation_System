package pbl2.dto;

import java.sql.Date;

public class DtoRcFixture {
	private int indexId;
	private int fixtureId;
	private String name;
	private int quantity;
	private Date date;
	private String phone;
	
	public DtoRcFixture(int indexId, int fixtureId, String name, int quantity, Date date, String phone) {
		super();
		this.indexId = indexId;
		this.fixtureId = fixtureId;
		this.name = name;
		this.quantity = quantity;
		this.date = date;
		this.phone = phone;
	}
	public int getIndexId() {
		return indexId;
	}
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
