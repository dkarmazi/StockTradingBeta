package StockTradingServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

//Ahmad Kouraiem
public class Authoriser {
	
	public boolean isAllowed (Sessions sessions, String sessionID, String Action) {
		int actionID;
		int userID;
		userID = sessions.getUserIDfromSession(sessionID);
		
		DatabaseConnector db = new DatabaseConnector();
		actionID = db.getActionID(Action);
		
		boolean result = db.isUserAllowedToDoAction(userID, actionID);
		if (result){
			System.out.println("UserID: " + userID + ", Session ID: " + sessionID + " is allowed to invoke: " + Action);
		}else{
			System.out.println("UserID: " + userID + ", Session ID: " + sessionID + " is NOT allowed to invoke: " + Action);
		}
		
		return result;
		
	}


}
