package pbl2.dto;

public class DtoHotel {
	private int hotelId;
	private String name;
	private String address;
	private String email;
	private String phone;
	private int rank;
	private String web;
	public DtoHotel(int hotelId, String name, String address, String email, String phone, int rank, String web) {
		super();
		this.hotelId = hotelId;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.rank = rank;
		this.web = web;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	
	
}
