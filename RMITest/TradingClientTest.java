/*
 * Ahmad Kouraiem
 */
package RMITest;

import static org.junit.Assert.*;

import org.junit.Test;

import RMI.TradingClient;

public class TradingClientTest {

	@Test(expected = Exception.class)
	public void testMain() {
		TradingClient tester = new TradingClient();
		String [] args = null;
		args[1] = "10.0.0.1";
		tester.main(args);
	}

}
