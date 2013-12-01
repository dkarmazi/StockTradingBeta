/*
 * Ahmad Kouraiem
 */

package StockTradingServerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import StockTradingServer.DataEncryptor;

public class DataEncryptorTest {

	@Test
	public void testDataEncryptor() {
		DataEncryptor tester = new DataEncryptor();
		
		tester.setIV("ABAFACAFAA5ABBAA");
		String encryptionKey = "0123456789abcdef";
		
		String plainText = "Plain Text to be tested";
		
		try {
			byte[] cypher = tester.encrypt(plainText, encryptionKey);
			assertEquals(plainText, tester.decrypt(cypher, encryptionKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
