package pbl2.dto;

import java.sql.Date;
import java.sql.Time;

public class DtoReceipt {
	private int receiptId;
	private int customerId;
	private Date date;
	private Time time;
	private String statement;
	private int price;
	public DtoReceipt(int receiptId, int customerId, Date date, Time time, String statement, int price) {
		super();
		this.receiptId = receiptId;
		this.customerId = customerId;
		this.date = date;
		this.time = time;
		this.statement = statement;
		this.price = price;
	}
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	

}
