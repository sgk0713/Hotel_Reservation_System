package pbl2.DTO;

public class DtoCleanedRoom {
	private int roomId;
	private int hotelId;
	private String cleaned;
	public DtoCleanedRoom(int roomId, int hotelId, String cleaned) {
		super();
		this.roomId = roomId;
		this.hotelId = hotelId;
		this.cleaned = cleaned;
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
	public String getCleaned() {
		return cleaned;
	}
	public void setCleaned(String cleaned) {
		this.cleaned = cleaned;
	}
	
}
