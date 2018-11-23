package pbl2.dto;

public class DtoRoom {
	private int roomId;
	private int hotelId;
	private int roomNumber;
	private int floor;
	private String viewType;
	private int price;
	private String roomType;
	private String bed;
	
	
	public DtoRoom(int roomId, int hotelId, int roomNumber, int floor, String viewType, int price, String roomType,
			String bed) {
		super();
		this.roomId = roomId;
		this.hotelId = hotelId;
		this.roomNumber = roomNumber;
		this.floor = floor;
		this.viewType = viewType;
		this.price = price;
		this.roomType = roomType;
		this.bed = bed;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getBed() {
		return bed;
	}
	public void setBed(String bed) {
		this.bed = bed;
	}
	
	
}
