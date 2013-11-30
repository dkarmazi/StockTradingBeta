/**
 * @author Ahmad Kouraiem
 */
package StockTradingServer;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {

	private int ID;
	private int SellingBrokerID;
	private int SellingCustomerID;
	private int BuyingBrokerID;
	private int BuyingCustomerID;
	private int StockID;
	private int Amount;
	private double Price;
	private Timestamp Time;
	private int SessionID;
	
	private String sellingBrokerName;
	private String sellingCustomerName;
	private String buyingBrokerName;
	private String buyingCustomerName;
	private String stockName;
	
	
	
	public String getSellingBrokerName() {
		return sellingBrokerName;
	}
	public void setSellingBrokerName(String sellingBrokerName) {
		this.sellingBrokerName = sellingBrokerName;
	}
	public String getSellingCustomerName() {
		return sellingCustomerName;
	}
	public void setSellingCustomerName(String sellingCustomerName) {
		this.sellingCustomerName = sellingCustomerName;
	}
	public String getBuyingBrokerName() {
		return buyingBrokerName;
	}
	public void setBuyingBrokerName(String buyingBrokerName) {
		this.buyingBrokerName = buyingBrokerName;
	}
	public String getBuyingCustomerName() {
		return buyingCustomerName;
	}
	public void setBuyingCustomerName(String buyingCustomerName) {
		this.buyingCustomerName = buyingCustomerName;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public int getSellingBrokerID() {
		return SellingBrokerID;
	}
	public void setSellingBrokerID(int sellingBrokerID) {
		SellingBrokerID = sellingBrokerID;
	}
	public int getSellingCustomerID() {
		return SellingCustomerID;
	}
	public void setSellingCustomerID(int sellingCustomerID) {
		SellingCustomerID = sellingCustomerID;
	}
	public int getBuyingBrokerID() {
		return BuyingBrokerID;
	}
	public void setBuyingBrokerID(int buyingBrokerID) {
		BuyingBrokerID = buyingBrokerID;
	}
	public int getBuyingCustomerID() {
		return BuyingCustomerID;
	}
	public void setBuyingCustomerID(int buyingCustomerID) {
		BuyingCustomerID = buyingCustomerID;
	}
	public int getStockID() {
		return StockID;
	}
	public void setStockID(int stockID) {
		StockID = stockID;
	}
	public int getAmount() {
		return Amount;
	}
	public void setAmount(int amount) {
		Amount = amount;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public Timestamp getTime() {
		return Time;
	}
	public void setTime(Timestamp time) {
		Time = time;
	}
	public int getSessionID() {
		return SessionID;
	}
	public void setSessionID(int sessionID) {
		SessionID = sessionID;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String toString(){
		return "SellingBrokerID: " + SellingBrokerID + ", SellingCustomerID: " + SellingCustomerID + ", BuyingBrokerID: " + BuyingBrokerID + ", BuyingCustomerID: " + BuyingCustomerID + ", StockID: " + StockID + ", Amount: " + Amount + ", Price: " + Price + ", Time: " + Time + ", SessionID: " + SessionID;
	}
	
}
