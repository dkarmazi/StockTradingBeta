package StockTradingServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("This is the Stock Trading Systems - Server");

		DatabaseConnector dc = new DatabaseConnector();

		String email = "dkarmazi@gwu.edu";
		String plainPass = "1234";

		//System.out.println(dc.checkIfUsernamePasswordMatch(email, plainPass));
		//dc.unsetActivationCodeAndTempPassword(49);

		//dc.forgotPassword(email);
		System.out.println(dc.checkIfUsernameTempPasswordActivationCodeMatch(email, "6vvv6ajnq1", "5dct0ktbvv").toString());
		
		// Logger log = new Logger();
		// log.logDatabaseActivity("dkarmazi", "Select * From Users");
	}

}