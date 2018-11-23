package pbl2.DTO;

public class DtoFixture {
	private int fixtureId;
	private int hotelId;
	private String name;
	private String type;
	private int quantity;
	public DtoFixture(int fixtureId, int hotelId, String name, String type, int quantity) {
		super();
		this.fixtureId = fixtureId;
		this.hotelId = hotelId;
		this.name = name;
		this.type = type;
		this.quantity = quantity;
	}
	public int getFixtureId() {
		return fixtureId;
	}
	public void setFixtureId(int fixtureId) {
		this.fixtureId = fixtureId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

}
