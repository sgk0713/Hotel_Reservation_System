package pbl2.dto;

import java.sql.Blob;

public class DtoDish {
	private int dishId;
	private String name;
	private int price;
	private Blob image;
	public DtoDish(int dishId, String name, int price, Blob image) {
		super();
		this.dishId = dishId;
		this.name = name;
		this.price = price;
		this.image = image;
	}
	public int getDishId() {
		return dishId;
	}
	public void setDishId(int dishId) {
		this.dishId = dishId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	
}
