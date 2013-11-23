package StockTradingServer;

import java.io.Serializable;

public class ServerAuthRes implements Serializable {

	private boolean hasAccess = false;
	private Object object;

	public boolean isHasAccess() {
		return hasAccess;
	}

	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
