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
		String plainPass = "123";

                
                //ReaderXml configReader = new ReaderXml();
                
		
		//System.out.println(dc.checkIfUsernamePasswordActivationCodeMatch(email, plainPass, "74f2qm78db"));
		//System.out.println(dc.checkIfUsernamePasswordMatch(email, plainPass));
		//dc.forgotPassword(email);

		
		//System.out.println(dc.updateUserPassword(49, "123", "123").getStatus());
                
		//System.out.println(LoggerCustom.getDatabaseActivity());
		//dc.forgotPassword(email);
		//System.out.println(dc.checkIfUsernameTempPasswordActivationCodeMatch(email, "65q0s2je5t", "1b8md4fj3j").toString());
		
		// Logger log = new Logger();
		// log.logDatabaseActivity("dkarmazi", "Select * From Users");
	}

}