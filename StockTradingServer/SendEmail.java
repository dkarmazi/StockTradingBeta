/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingServer;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class helps to send out email notifications to the users
 * 
 * 
 * @author Sulochane, edited by Dmitriy Karmazin
 */
public class SendEmail {

	private static String username = "vpupkin747@gmail.com";
	private static String password = "12345678Vasya";
	private static String fromAddress = "system@team6545.com";

	/**
	 * This function sends an email to a given recipient with given subject and message
	 * @param recepientAddress
	 * @param messageSubject
	 * @param messageBody
	 */
	public static void sendEmailNotification(String recepientAddress,
			String messageSubject, String messageBody) {

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recepientAddress));
			message.setSubject(messageSubject);
			message.setText(messageBody);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
