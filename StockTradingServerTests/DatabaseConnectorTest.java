/*
 * Ahmad Kouraiem
 */

package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.BrokerageFirm;
import StockTradingServer.DatabaseConnector;
import StockTradingServer.Stock;
import StockTradingServer.Validator;

public class DatabaseConnectorTest {

	DatabaseConnector tester = new DatabaseConnector();
	
	@Test
	public void testSelectBrokerageFirm() {
		
		BrokerageFirm expected = new BrokerageFirm();
		
		expected.setId(11);
		expected.setName("Charles Schwap Brokerage");
		expected.setAddressStreet("2000 S Eads Street");
		expected.setAddressCity("Arlington");
		expected.setAddressState("VA");
		expected.setAddressZip("22002");
		expected.setLicenceNumber("12344222332");
		expected.setStatus(1);
		expected.setSupervisorEmail("manager@charlesschwap.com");
		
		BrokerageFirm result = tester.selectBrokerageFirm(11);
		
		assertEquals(expected.toString(), result.toString());
		
	}

	@Test
	public void testSelectStock() {
		
		Stock expected = new Stock();
		
		expected.setId(2);
		expected.setName("Google");
		expected.setAmount(1);
		expected.setPrice(29.5000);
		expected.setStatusId(1);

		Stock result = tester.selectStock(2);
		
		assertEquals(expected.toString(), result.toString());
		
	}

	@Test
	public void testVerifyUserEmail() {
		Validator result = tester.verifyUserEmail("ahmadko@gmail.com");
		assertTrue(result.isVerified());
	}

	@Test
	public void testCheckIfUsernamePasswordMatch() {
		Validator result = tester.checkIfUsernamePasswordMatch("ahmadko@gmail.com", "P@ssw0rd");
		assertTrue(result.isVerified());
	}

	@Test
	public void testIsUserPasswordExpired() {
		boolean result = tester.isUserPasswordExpired(10);
		assertTrue(result);
	}

	@Test
	public void testGetSuperEmail() {
		String result = tester.getSuperEmail(44);
		assertEquals("manager@charlesschwap.com", result);
	}

	@Test
	public void testIsUserExists() {
		boolean result = tester.isUserExists("ahmadko@gmail.com");
		assertTrue(result);
	}


}
