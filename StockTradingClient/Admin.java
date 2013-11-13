/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import RMI.*;
import StockTradingCommon.Enumeration.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.DatabaseConnector;
import StockTradingServer.User;
import StockTradingServer.Validator;

import java.sql.SQLException;
import java.util.ArrayList;


public class Admin extends Application {
	
	public static String clientSessionID = null;
	
	private static final int PORT = 2019;
	private Stage stage;
	public static ServerInterface serverInterface;
	@Override
	public void start(Stage primaryStage) throws Exception 
        {

		if(serverInterface != null) {
			Utility.setServerInterface(serverInterface);
		} else {
			System.out.println("Server interface is not set");			
		}

		stage = primaryStage;

		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		// Resource resource = getClass().getResource("Broker.fxml");
		Scene scene = new Scene(root);
		stage.setTitle("Stock Trading Platform");
                stage.setResizable(false);
                stage.centerOnScreen();
                
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		try {
			// Make reference to SSL-based registry
			String host = (args.length < 1) ? "127.0.0.1" : args[0];
   //                        host = "128.164.92.161";
			Registry registry = LocateRegistry.getRegistry(InetAddress
					.getByName(host).getHostName(), PORT,
					new RMISSLClientSocketFactory());

			// "serverInterface" is the identifier that we'll use to refer
			// to the remote object that implements the "serverInterface"
			// interface

			serverInterface = (ServerInterface) registry
					.lookup("TradingServer");


			String email = "dkarmazi@gwu.edu";
			String plainPass = "1234";

			Validator v = serverInterface.checkIfUsernamePasswordMatch(email, plainPass);
			
			
//			//System.out.println(v.getSession());
//			
//			System.out.println()
//			
//			//System.out.println(serverInterface.selectBrokersAll(1, clientSessionID).toString());

			
			
			//ArrayList<StockTradingServer.BrokerageFirm> c =  serverInterface.selectBrokerageFirmsAll();
			//System.out.println(c.toString());
			 
			//System.out.println(mess	age.toString() + "\n");
			launch(args);

		} catch (Exception e) {
			System.out.println("Admin exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
