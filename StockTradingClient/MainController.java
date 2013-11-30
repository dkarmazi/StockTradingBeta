/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.Transaction;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Hirosh, Kouraiem
 */
public class MainController implements Initializable {
    
    // Only for Admin
    @FXML private Button BrokerageFirm;
    @FXML private Button Broker;
    @FXML private Button Stock;    
    @FXML private Button Administrator;
    @FXML private Button TradingSession;
    @FXML private Button Transactions;
    @FXML private Button LogView;
    
    // Only for Broker
    @FXML private Button Customer;
    @FXML private Button SellOrder;
    @FXML private Button BuyOrder;
    @FXML private Button StockView;
    @FXML private Button ViewSessionInfo;
    
    // Common to all users
    @FXML private Button ChangePassword;    
    @FXML private ToggleButton HackMode;
    
    @FXML
    private void handleButtonAction_btnAddBrokerageFirm(ActionEvent event) throws IOException{

    	
    	if (!Utility.checkBrokerageFirmWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("BrokerFirm.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("Brokerage Firms");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();

    }
    
    @FXML
    private void handleButtonAction_btnAddAdmin(ActionEvent event) throws IOException{
        if (!Utility.checkBrokerWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("Admin.fxml"));
            //MainController.class.getResource("../StockTradingClient.MessageBox/Info.fxml"));
        
        stage.setScene(new Scene(root));
        stage.setTitle("Administrators");
        stage.initModality(Modality.NONE);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnAddBroker(ActionEvent event) throws IOException{

    	if (!Utility.checkBrokerWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("Broker.fxml"));
            //MainController.class.getResource("../StockTradingClient.MessageBox/Info.fxml"));
        
        stage.setScene(new Scene(root));
        stage.setTitle("Brokers");
        stage.initModality(Modality.NONE);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnAddStock(ActionEvent event) throws IOException{
        
    	if (!Utility.checkStockWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("Stock.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("Stocks");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnAddCustomer(ActionEvent event) throws IOException{
        
    	if (!Utility.checkCustomerWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		
                /*MessageBox msgBox = new MessageBox("Permision Denied"
                        , "You do not have sufficient permission to access this page."
                        , Enumeration.MessageIcon.WARNING
                );
                msgBox.Show(event);*/
                return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("Customer.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("Customers");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
        
    @FXML
    private void handleButtonAction_btnAddSellOrder(ActionEvent event) throws IOException{
        

    	if (!Utility.checkSellingOrderWindowPermission()){
    		JOptionPane.showMessageDialog (null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("SellOrder.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("Selling Orders");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
            
    @FXML
    private void handleButtonAction_btnAddBuyOrder(ActionEvent event) throws IOException{
        
    	if (!Utility.checkBuyingOrderWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("BuyOrder.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("Buying Orders");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnStockView(ActionEvent event) throws IOException{
        
    	if (!Utility.checkStockViewPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("StockView.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("View Stocks");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnLogView(ActionEvent event) throws IOException{
        
    	if (!Utility.checkLogViewPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("LogViewer.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setTitle("View System Log");
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
    
    @FXML
    private void handleButtonAction_btnHackMode(ActionEvent event) throws IOException
    {
        if (HackMode.isSelected())
        {
            HackMode.setText("Hack Mode On");
            SetScreenForHackerDemo();
        }
        else
        {
            HackMode.setText("Hack Mode Off");
            SetScreenForUserRole();
        }
    }
    
    @FXML
    private void handleButtonAction_btnChangePassword(ActionEvent event)  throws IOException
    {
        ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
            MainController.class.getResource("PasswordChange.fxml"));
 
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Change Password");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
        
        
    } 
    
    private void SetScreenForAdmin()
    {      
        // Show
        BrokerageFirm.setVisible(true);
        Broker.setVisible(true);
        Stock.setVisible(true);
        Administrator.setVisible(true);
        TradingSession.setVisible(true);
        Transactions.setVisible(true);        
        LogView.setVisible(true);
        
        // Hide
        Customer.setVisible(false);
        BuyOrder.setVisible(false);
        SellOrder.setVisible(false);  
        StockView.setVisible(false);
        ViewSessionInfo.setVisible(false);
    }
    
    private void SetScreenForBroker()
    {      
        // Hide
        BrokerageFirm.setVisible(false);
        Broker.setVisible(false);
        Stock.setVisible(false);
        Administrator.setVisible(false);
        TradingSession.setVisible(false);
        Transactions.setVisible(false);
        LogView.setVisible(false);
        
        // Show
        Customer.setVisible(true);
        BuyOrder.setVisible(true);
        SellOrder.setVisible(true);     
        StockView.setVisible(true);
        ViewSessionInfo.setVisible(true);
    }
    
    private void SetScreenForHackerDemo()
    {        
        // Hide
        BrokerageFirm.setVisible(true);
        Broker.setVisible(true);
        Stock.setVisible(true);
        Administrator.setVisible(true);
        TradingSession.setVisible(true);
        Transactions.setVisible(true);
        LogView.setVisible(true);

        Customer.setVisible(true);
        BuyOrder.setVisible(true);
        SellOrder.setVisible(true);  
        StockView.setVisible(true);
        ViewSessionInfo.setVisible(true);
    }
    
    private void SetScreenForUserRole()
    {
        if (Utility.getCurrentUserRole() == Enumeration.UserRole.USER_ROLE_ADMIN)
        {
            SetScreenForAdmin();                    
        }
        else if (Utility.getCurrentUserRole() == Enumeration.UserRole.USER_ROLE_BROKER)
        {
            SetScreenForBroker();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        SetScreenForUserRole();         
    }
    
    @FXML
    public void handleTradingSessionButton(ActionEvent event) throws IOException{
    	
    	if (!Utility.checkTradingSessionWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                                MainController.class.getResource("TradingSession.fxml")
                            );

        stage.setScene(new Scene(root));
        stage.setTitle("Trading Session Manager");
        stage.setResizable(false);
        stage.initModality(Modality.NONE);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
                {
                    public void handle(WindowEvent we) 
                    {
                        try {
							Utility.serverInterface.endTradingSession(Utility.getCurrentSessionId());
						} catch (RemoteException e) {
							e.printStackTrace();
						}
                    }
                });
        stage.show();
    	
    }
    
    @FXML
    public void handleViewSessionInfoButton(ActionEvent event) throws IOException{

    	 Stage stage = new Stage();
         Parent root = FXMLLoader.load(
                                 MainController.class.getResource("ViewSessionInfo.fxml")
                             );

         stage.setScene(new Scene(root));
         stage.setTitle("Trading Session Info");
         stage.setResizable(false);
         stage.initModality(Modality.NONE);
         stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
               
         stage.show();
     	
    }
    
    @FXML
    public void handleTransactionButton(ActionEvent event) throws IOException{
    	
    	if (!Utility.checkTransactionWindowPermission()){
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
    		return;
    	}
    	
    	boolean isThereTradingSession = (boolean) Utility.serverInterface.isThereActiveTradingSession(Utility.getCurrentSessionId()).getObject(); 
    	if (!isThereTradingSession){
    		JOptionPane.showMessageDialog(null, "No active trading session.");
    		return;
    	}
    	 Stage stage = new Stage();
         Parent root = FXMLLoader.load(
                                 MainController.class.getResource("Transactions.fxml")
                             );

         stage.setScene(new Scene(root));
         stage.setTitle("Trading Session Transactions");
         stage.setResizable(true);
         stage.initModality(Modality.NONE);
         stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
               
         stage.show();
     	
    }
    /*
    public boolean isSessionSet(ActionEvent event) throws IOException
    {
        //Utility.setCurrentSessionId(null);
        if (Utility.getCurrentSessionId() == null)
        {
            MessageBox msgBox = new MessageBox("Invalid Session"
                            , "Your session is not initialized. Please log again."
                            , Enumeration.MessageIcon.ERROR
                    );
            msgBox.Show(event);

            return false;
        }
        else
        {
            return true;
        }
    }*/
}
