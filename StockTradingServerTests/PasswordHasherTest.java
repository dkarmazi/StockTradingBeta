/*
 * Ahmad Kouraiem
 */
package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.PasswordHasher;

public class PasswordHasherTest {
	PasswordHasher tester = new PasswordHasher();
	@Test
	public void testSha512() {
		
		String result = tester.sha512("P@ssw0rd", "SALT");
		String expected = "1f5b48c758b54d65de6f03620f22334dda54158cd3ce86d70853774c8685670cec026f55dbbd16f9647e5ac5395bad2c12361a471b1581b84e130465f2ecb808";
		assertEquals(expected, result);
	}

}
