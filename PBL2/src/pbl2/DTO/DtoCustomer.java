package pbl2.DTO;

public class DtoCustomer {
	private int customerId;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String cardNumber;
	private String comment;
	public DtoCustomer(int customerId, String name, String phone, String email, String address, String cardNumber,
			String comment) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.cardNumber = cardNumber;
		this.comment = comment;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
