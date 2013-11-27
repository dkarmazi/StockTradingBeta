/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import RMI.ServerInterface;
import StockTradingCommon.Enumeration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
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

/**
 * FXML Controller class
 *
 * @author Sulochane, Kouraiem
 */
public class MainController implements Initializable {
    
    // Only for Admin
    @FXML private Button BrokerageFirm;
    @FXML private Button Broker;
    @FXML private Button Stock;    
    
    // Only for Broker
    @FXML private Button Customer;
    @FXML private Button SellOrder;
    @FXML private Button BuyOrder;
    
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
    		JOptionPane.showMessageDialog(null, "You don't have enough permission to access this page.");
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

        // Hide
        Customer.setVisible(false);
        BuyOrder.setVisible(false);
        SellOrder.setVisible(false);        
    }
    
    private void SetScreenForBroker()
    {      
        // Hide
        BrokerageFirm.setVisible(false);
        Broker.setVisible(false);
        Stock.setVisible(false);

        // Show
        Customer.setVisible(true);
        BuyOrder.setVisible(true);
        SellOrder.setVisible(true);        
    }
    
    private void SetScreenForHackerDemo()
    {        
        // Hide
        BrokerageFirm.setVisible(true);
        Broker.setVisible(true);
        Stock.setVisible(true);

        Customer.setVisible(true);
        BuyOrder.setVisible(true);
        SellOrder.setVisible(true);   
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
    
    
}
