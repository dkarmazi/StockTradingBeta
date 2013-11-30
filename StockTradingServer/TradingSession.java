
/**
 * @author Ahmad Kouraiem
 */
package StockTradingServer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

public class TradingSession implements Serializable {
	
	private Timestamp startTime;
	private Timestamp endTime;
	private int limitUp;
	private int limitDown;
	private Vector<Integer> availableStocksID;
	private Vector<Integer> availableFirmsID;
	private Vector<String> availableStocksNames;
	private Vector<String> availableFirmsNames;
	private int Active;

	
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public int getLimitUp() {
		return limitUp;
	}
	public void setLimitUp(int limitUp) {
		this.limitUp = limitUp;
	}
	public int getLimitDown() {
		return limitDown;
	}
	public void setLimitDown(int limitDown) {
		this.limitDown = limitDown;
	}
	public Vector<Integer> getAvailableStocksID() {
		return availableStocksID;
	}
	public void setAvailableStocksID(Vector<Integer> availableStocks) {
		this.availableStocksID = availableStocks;
	}
	public Vector<Integer> getAvailableFirmsID() {
		return availableFirmsID;
	}
	public void setAvailableFirmsID(Vector<Integer> availableFirms) {
		this.availableFirmsID = availableFirms;
	}
	public int getActive() {
		return Active;
	}
	public void setActive(int active) {
		Active = active;
	}
	public Vector<String> getAvailableStocksNames() {
		return availableStocksNames;
	}
	public void setAvailableStocksNames(Vector<String> availableStocksNames) {
		this.availableStocksNames = availableStocksNames;
	}
	public Vector<String> getAvailableFirmsNames() {
		return availableFirmsNames;
	}
	public void setAvailableFirmsNames(Vector<String> availableFirmsNamess) {
		this.availableFirmsNames = availableFirmsNamess;
	}
	

}
