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

		Authenticator auth = new Authenticator();
		Validator v = auth.checkIfUsernamePasswordMatch(email, plainPass);
		
		System.out.println(v.toString());
		
		
		
		
		// System.out.println(dc.selectBrokersAllbyFirm(11).toString());
		// Logger log = new Logger();
		// log.logDatabaseActivity("dkarmazi", "Select * From Users");
	}

}