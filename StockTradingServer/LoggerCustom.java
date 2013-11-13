package StockTradingServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class LoggerCustom {
	private static String dbLogFileName = "logDbActivity.txt";
	private static String loginLogFileName = "logLoginActivity.txt";
	
	private String username = "dkarmazi";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void logDatabaseActivity(String query) {
		String delimiter = " | ";
		String endOfEntry = "\n\n\n";

		try {
			java.util.Date date = new java.util.Date();
			String timestamp = new Timestamp(date.getTime()).toString();

			String data = timestamp + delimiter + this.getUsername() + delimiter + query;
			
			PasswordHasher h = new PasswordHasher();
			
			data = data + delimiter + h.sha512(data, "acas") + endOfEntry;

			File file = new File(dbLogFileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public static void logLoginActivity(String email, String activity) {
		String delimiter = " | ";
		String endOfEntry = "\n\n\n";

		try {
			java.util.Date date = new java.util.Date();
			String timestamp = new Timestamp(date.getTime()).toString();

			String data = timestamp + delimiter + email + delimiter + activity;
			
			PasswordHasher h = new PasswordHasher();
			
			data = data + delimiter + h.sha512(data, "acas") + endOfEntry;

			File file = new File(loginLogFileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	

}
