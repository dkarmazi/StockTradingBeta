package StockTradingServer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.Vector;

// Ahmad Kouraiem
public class Sessions {

	private Vector<Integer> loggedUsers = new Vector();
	private Vector<String> sessionIDs = new Vector();
	
	public String generateSessionID(){
    	return UUID.randomUUID().toString();
	}
	
	public boolean isLoggedIn (String Email){
		int userID;
		DatabaseConnector db = new DatabaseConnector();
		
		userID = db.getUserIDByEmail(Email);
		
		if (userID>0){
			if (loggedUsers.contains(userID)){
        		return true;
        	}
		}
		return false;
	}

	public String setSession(String Email){
		if (isLoggedIn(Email)){
			return null;
		}
		
		String sessionID = generateSessionID();
		
		DatabaseConnector db = new DatabaseConnector();
		
		int userID = db.getUserIDByEmail(Email);
		if (userID>0){
			loggedUsers.add(userID);
			sessionIDs.add(sessionID);
	        return sessionID;
		}else{
			return null;
		}
	}

	public int getUserIDfromSession(String sessionID){
		int userID = 0;
		
		if (sessionIDs.contains(sessionID)){
			userID = loggedUsers.get(sessionIDs.indexOf(sessionID));
		}
		
		return userID;
	}

	public boolean deleteSession(String sessionID){
		if (sessionIDs.contains(sessionID)){
			int userIndex = sessionIDs.indexOf(sessionID);
			sessionIDs.remove(userIndex);
			loggedUsers.remove(userIndex);
			return true;
		}
		return false;
	}
}
