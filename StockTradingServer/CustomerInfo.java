package StockTradingServer;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Dmitriy Karmazin
 * CSCI-6545
 */

public class CustomerInfo implements Serializable {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private double balance;
	private double pendingBalance;
	private int statusId;
	private int firmId;
	private ArrayList<Stock> stocks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getPendingBalance() {
		return pendingBalance;
	}

	public void setPendingBalance(double pendingBalance) {
		this.pendingBalance = pendingBalance;
	}

	public ArrayList<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(ArrayList<Stock> stocks) {
		this.stocks = stocks;
	}


	public Validator validate() {

		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vFirstName, vLastName, vEmail, vPhone, vBalance, vPendingBalance, vStatusId;

		Boolean verified = true;
		String status = "";

		// 1. first name
		vFirstName = iv.validateString(this.getFirstName(), "First Name");
		verified &= vFirstName.isVerified();
		status += vFirstName.getStatus();

		// 2. last name
		vLastName = iv.validateString(this.getLastName(), "Last Name");
		verified &= vLastName.isVerified();
		status += vLastName.getStatus();

		// 3. email
		vEmail = iv.validateEmail(this.getEmail(), "Email");
		verified &= vEmail.isVerified();
		status += vEmail.getStatus();

		// 4. phone
		vPhone = iv.validateString(this.getPhone(), "Phone");
		verified &= vPhone.isVerified();
		status += vPhone.getStatus();

		// 5 balance
		vBalance = iv.validateDoubleGeneralWithNull(this.getBalance(), "Balance");
		verified &= vBalance.isVerified();
		status += vBalance.getStatus();
		
		// 6 pending balance
		vPendingBalance = iv.validateDoubleGeneralWithNull(this.getPendingBalance(), "Pending Balance");
		verified &= vPendingBalance.isVerified();
		status += vPendingBalance.getStatus();

		// 7. status
		vStatusId = iv.validateIntGeneral(this.getStatusId(), "StatusId");
		verified &= vStatusId.isVerified();
		status += vStatusId.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}

	@Override
	public String toString() {
		String out = "CustomerInfo: ";
		String delimiter = " ";
		String endOfString = "\n";

		out += "ID: " + this.getId() + delimiter;
		out += "FistName: " + this.getFirstName() + delimiter;
		out += "LastName: " + this.getLastName() + delimiter;
		out += "Email: " + this.getEmail() + delimiter;
		out += "Phone: " + this.getPhone() + delimiter;
		out += "Balance: " + this.getBalance() + delimiter;
		out += "Pending Balance: " + this.getPendingBalance() + delimiter;
		out += "StatusId: " + this.getStatusId() + delimiter;

		out += endOfString;

		return out;
	}

}
