package StockTradingServer;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable {
	private int orderId;
	private int typeId;
	private int brokerId;
	private int customerId;
	private int stockId;
	private int amount;
	private double price;
	private int statusId;
	private String displaySummary;
        private String displayCustomerName;
        private String displayStockName;
        private String displayFirmName;
        
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDisplaySummary() {
		return displaySummary;
	}

	public void setDisplaySummary(String displaySummary) {
		this.displaySummary = displaySummary;
	}

        public String getDisplayCustomerName() {
            return displayCustomerName;
        }

        public void setDisplayCustomerName(String displayCustomerName) {
            this.displayCustomerName = displayCustomerName;
        }

        public String getDisplayStockName() {
            return displayStockName;
        }

        public void setDisplayStockName(String displayStockName) {
            this.displayStockName = displayStockName;
        }

        public String getDisplayFirmName() {
            return displayFirmName;
        }

        public void setDisplayFirmName(String displayFirmName) {
            this.displayFirmName = displayFirmName;
        }
        
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Validator validate() {

		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vTypeId, vBrokerId, vCustomerId, vStockId, vAmount, vPrice, vStatusId;

		Boolean verified = true;
		String status = "";

		// 1. validate orderId
		vTypeId = iv.validateIntGeneral(this.getTypeId(), "Type");
		verified &= vTypeId.isVerified();
		status += vTypeId.getStatus();

		// 2. broker id
		vBrokerId = iv.validateIntGeneral(this.getBrokerId(), "Broker");
		verified &= vBrokerId.isVerified();
		status += vBrokerId.getStatus();

		// 3. customer id
		vCustomerId = iv.validateIntGeneral(this.getCustomerId(), "Customer");
		verified &= vCustomerId.isVerified();
		status += vCustomerId.getStatus();

		// 4. stock id
		vStockId = iv.validateIntGeneral(this.getStockId(), "Stock");
		verified &= vStockId.isVerified();
		status += vStockId.getStatus();

		// 5. amount
		vAmount = iv.validateDoubleGeneral(this.getAmount(), "Amount");
		verified &= vAmount.isVerified();
		status += vAmount.getStatus();

		// 6. price
		vPrice = iv.validateDoubleGeneral(this.getPrice(), "Price");
		verified &= vPrice.isVerified();
		status += vPrice.getStatus();

		// 7. status id
		vStatusId = iv.validateIntGeneral(this.getStatusId(), "Status Id");
		verified &= vStatusId.isVerified();
		status += vStatusId.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}

	@Override
	public String toString() {
		String out = "Order: ";
		String delimiter = " ";
		String endOfString = "\n";

		out += "OrderId: " + this.getOrderId() + delimiter;
		out += "TypeId: " + this.getTypeId() + delimiter;
		out += "BrokerId: " + this.getBrokerId() + delimiter;
		out += "CustomerId: " + this.getCustomerId() + delimiter;
		out += "StockId: " + this.getStockId() + delimiter;
		out += "Amount: " + this.getAmount() + delimiter;
		out += "Price: " + this.getPrice() + delimiter;
		out += "StatusId: " + this.getStatusId() + delimiter;
		//out += "Display summary: " + this.getDisplaySummary() + delimiter;
		
		out += endOfString;

		return out;
	}
}
