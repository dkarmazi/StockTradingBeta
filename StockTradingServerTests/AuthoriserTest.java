/*
 * Ahmad Kouraiem
 */

package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.Authoriser;
import StockTradingServer.Sessions;

public class AuthoriserTest {

	@Test
	public void testIsAllowed() {
		Authoriser tester = new Authoriser();
		
		Sessions sessions = new Sessions();
		String sessionID; 
		String Action;
		
		sessions.setSession("11111@111.com");
		sessionID = "10";
		Action = "insertNewBroker";
		
		boolean results = tester.isAllowed(sessions, sessionID, Action);
		assertFalse(results);
	}

}
