/*
 * Ahmad Kouraiem
 */
package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.PasswordClassifier;

public class PasswordClassifierTest {
	PasswordClassifier tester = new PasswordClassifier();
	
	@Test
	public void testGradePassword1() {
		int result = tester.GradePassword("P@1111");
		assertEquals(1, result);
	}

	@Test
	public void testGradePassword2() {
		int result = tester.GradePassword("P@ssw0rd");
		assertEquals(2, result);
	}
	
	@Test
	public void testGradePassword4() {
		int result = tester.GradePassword("P@ssw0rd1");
		assertEquals(4, result);
	}
}
