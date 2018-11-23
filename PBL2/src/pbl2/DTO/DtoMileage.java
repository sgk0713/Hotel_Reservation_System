package pbl2.DTO;

public class DtoMileage {
	private int customerId;
	private int mileage;
	
	public DtoMileage(int customerId, int mileage) {
		super();
		this.customerId = customerId;
		this.mileage = mileage;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	
	
}
