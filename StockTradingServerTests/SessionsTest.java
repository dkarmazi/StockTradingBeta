/*
 * Ahmad Kouraiem
 */
package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.Sessions;

public class SessionsTest {
	Sessions tester = new Sessions();
	

	@Test
	public void testIsLoggedIn1() {
		tester.setSession("ahmadko@gmail.com");
		assertTrue(tester.isLoggedIn("ahmadko@gmail.com"));
	}

	@Test
	public void testIsLoggedIn2() {
		assertFalse(tester.isLoggedIn("ahmadko111@gmail.com"));
	}
	
	@Test
	public void testDeleteSession() {
		String sessionID = tester.setSession("ahmadko@gmail.com");
		tester.deleteSession(sessionID);
		assertFalse(tester.isLoggedIn("ahmadko@gmail.com"));
	}

}
