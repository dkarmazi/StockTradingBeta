package StockTradingServer;

import java.io.Serializable;

public class Relationship implements Serializable  {

	private int id1;
	private int id2;
	private int extra;
	private int status;

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public int getExtra() {
		return extra;
	}

	public void setExtra(int extra) {
		this.extra = extra;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String out = "Relationship: ";
		String delimiter = " ";
		String endOfString = "";

		out += "ID1: " + this.getId1() + delimiter;
		out += "ID2: " + this.getId2() + delimiter;
		out += "Extra: " + this.getExtra() + delimiter;
		out += "Status: " + this.getStatus() + delimiter;

		out += endOfString;

		return out;
	}

	public Validator validate() {
		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vId1, vId2, vExtra, vStatus;

		Boolean verified = true;
		String status = "";

		// 1. validate ID1
		vId1 = iv.validateIntGeneral(this.getId1(), "ID1");
		verified &= vId1.isVerified();
		status += vId1.getStatus();

		// 2. validate ID2
		vId2 = iv.validateIntGeneral(this.getId2(), "ID2");
		verified &= vId2.isVerified();
		status += vId2.getStatus();

		// 3. extra
		vExtra = iv.validateIntGeneral(this.getExtra(), "Amount");
		verified &= vExtra.isVerified();
		status += vExtra.getStatus();

		// 4. status
		vStatus = iv.validateInt(this.getStatus(), "Status");
		verified &= vStatus.isVerified();
		status += vStatus.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}
}
