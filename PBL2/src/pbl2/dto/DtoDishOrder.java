package pbl2.dto;

import java.sql.Time;
import java.sql.Date;
public class DtoDishOrder {
	private int dishOrderId;
	private int dishId;
	private int bookId;
	private Date date;
	private Time orderTime;
	private int quantity;
	private String delivered;
	public DtoDishOrder(int dishOrderId, int dishId, int bookId, Date date, Time orderTime, int quantity,
			String delivered) {
		super();
		this.dishOrderId = dishOrderId;
		this.dishId = dishId;
		this.bookId = bookId;
		this.date = date;
		this.orderTime = orderTime;
		this.quantity = quantity;
		this.delivered = delivered;
	}
	public int getDishOrderId() {
		return dishOrderId;
	}
	public void setDishOrderId(int dishOrderId) {
		this.dishOrderId = dishOrderId;
	}
	public int getDishId() {
		return dishId;
	}
	public void setDishId(int dishId) {
		this.dishId = dishId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Time orderTime) {
		this.orderTime = orderTime;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDelivered() {
		return delivered;
	}
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}
}
