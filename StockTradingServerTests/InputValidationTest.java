/*
 * Ahmad Kouraiem
 */
package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.InputValidation;

public class InputValidationTest {

	InputValidation tester = new InputValidation();
	
	@Test
	public void testValidateEmail1() {
		assertTrue(tester.validateEmail("ahmadko@gmail.com", "").isVerified());
	}

	@Test
	public void testValidateEmail2() {
		assertFalse(tester.validateEmail("ahmadkogmail.com", "").isVerified());
	}
	
	
	
}
