package tr.com.beinplanner.zms.dao;

public class Custom_ZmsStockMonth {

	
	private int month;
	private int year;
	private int stockInCount;
	private double stockInPrice;
	private int stockOutCount;
	private double stockOutPrice;
	private long productId;
	
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getStockInCount() {
		return stockInCount;
	}
	public void setStockInCount(int stockInCount) {
		this.stockInCount = stockInCount;
	}
	
	public int getStockOutCount() {
		return stockOutCount;
	}
	public void setStockOutCount(int stockOutCount) {
		this.stockOutCount = stockOutCount;
	}
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public double getStockInPrice() {
		return stockInPrice;
	}
	public void setStockInPrice(double stockInPrice) {
		this.stockInPrice = stockInPrice;
	}
	public double getStockOutPrice() {
		return stockOutPrice;
	}
	public void setStockOutPrice(double stockOutPrice) {
		this.stockOutPrice = stockOutPrice;
	}
	
	
	
	
}
