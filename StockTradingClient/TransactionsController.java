/**
 * @author Ahmad Kouraiem
 */
package StockTradingClient;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionsController implements Initializable {
	
	@FXML private TableView transactionsTable;
	@FXML private Button rollBackButton;
	@FXML private Button refresh;
	@FXML private Label lblErrorMessage;

	@FXML void handleRollBackButton(){
		
		if (transactionsTable.getSelectionModel().isEmpty()){
			lblErrorMessage.setText("No transaction selected.");
			lblErrorMessage.setVisible(true);
		}else{
			
			lblErrorMessage.setVisible(false);
			Transaction t = (Transaction) transactionsTable.getSelectionModel().getSelectedItem();
			int transID = t.getID();
			try{
		           ServerAuthRes results = Utility.serverInterface.rollBackTransaction(transID, Utility.getCurrentSessionId());
		           if (results.isHasAccess()) {
		                   boolean done = (boolean) results.getObject();
		                   if (done){
		           				lblErrorMessage.setText("Transaction rolled back successfully.");
		           				lblErrorMessage.setVisible(true);
		           				handleRefreshButton();
		                   }
		           }else{
		                   System.out.println("You are not allowed to perfom this action: selectStockAll");
		                   return;
		           }
		       }
		       catch (RemoteException e)
		       {
		           e.printStackTrace();
		       }
		}
	}
	
	@FXML void handleRefreshButton(){
		populateTable();		
	}
	
   @Override
   public void initialize(URL url, ResourceBundle rb) {
	  initTable();
	  populateTable();
   }  
	
   private void populateTable(){
	   transactionsTable.getItems().clear();

       ArrayList<Transaction> records = null;
       try
       {

           ServerAuthRes results = Utility.serverInterface.selectTransactionAll(Utility.getCurrentSessionId());

           if (results.isHasAccess()) {
                   records = (ArrayList<Transaction>) results.getObject();
           }else{
                   System.out.println("You are not allowed to perfom this action: selectStockAll");
                   return;
           }
       }
       catch (RemoteException e)
       {
           e.printStackTrace();
       }
       
       
       ObservableList<Transaction> data;
       data = FXCollections.observableArrayList();
       
       
       for(Transaction s : records)
       {
    	   data.add(s); 
       }

       transactionsTable.setItems(data);
       
       rollBackButton.setDisable(transactionsTable.getItems().isEmpty());
	  
   }
   
   private void initTable(){
	   
	   transactionsTable.setEditable(false);
       
       TableColumn culID = new TableColumn("ID");
       culID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("ID"));
       
       TableColumn culSellingBrokerID = new TableColumn("Selling Broker ID");
       
       culSellingBrokerID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("SellingBrokerID"));
       
       TableColumn culSellingCustomerID = new TableColumn("Selling Customer ID");
       culSellingCustomerID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("SellingCustomerID"));
       
       
       TableColumn culBuyingBrokerID = new TableColumn("Buying Broker ID");
       culBuyingBrokerID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("BuyingBrokerID"));
       
       TableColumn culBuyingCustomerID = new TableColumn("Buying Customer ID");
       culBuyingCustomerID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("BuyingCustomerID"));
       
       TableColumn culStockID = new TableColumn("Stock ID");
       culStockID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("StockID"));
       
       TableColumn culAmount = new TableColumn("Amount");
       culAmount.setMinWidth(100);
       culAmount.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("Amount"));
       
       
       TableColumn culPrice = new TableColumn("Price");
       culPrice.setMinWidth(100);
       culPrice.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("Price"));
       
       TableColumn culTime = new TableColumn("Time");
       culTime.setMinWidth(150);
       culTime.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("Time"));
       
       TableColumn culSessionID = new TableColumn("Session ID");
       culSessionID.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("SessionID"));
   

       TableColumn culsellingBrokerName = new TableColumn("Selling Broker Name");
       culsellingBrokerName.setMinWidth(150);
       culsellingBrokerName.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("sellingBrokerName"));
       
       TableColumn culsellingCustomerName = new TableColumn("Selling Customer Name");
       culsellingCustomerName.setMinWidth(150);
       culsellingCustomerName.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("sellingCustomerName"));
       
       TableColumn culbuyingBrokerName = new TableColumn("Buying Broker Name");
       culbuyingBrokerName.setMinWidth(150);
       culbuyingBrokerName.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("buyingBrokerName"));
       
       TableColumn culbuyingCustomerName = new TableColumn("Buying Customer Name");
       culbuyingCustomerName.setMinWidth(150);
       culbuyingCustomerName.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("buyingCustomerName"));
       
       TableColumn culstockName = new TableColumn("Stock Name");
       culstockName.setMinWidth(150);
       culstockName.setCellValueFactory(
               new PropertyValueFactory<Transaction, String>("stockName"));
       
       transactionsTable.getColumns().clear();
       transactionsTable.getColumns().add(culID);
       culID.setVisible(false);
       transactionsTable.getColumns().add(culSellingBrokerID);
       culSellingBrokerID.setVisible(false);
       transactionsTable.getColumns().add(culsellingBrokerName);
       transactionsTable.getColumns().add(culSellingCustomerID);
       culSellingCustomerID.setVisible(false);
       transactionsTable.getColumns().add(culsellingCustomerName);
       transactionsTable.getColumns().add(culBuyingBrokerID);
       culBuyingBrokerID.setVisible(false);
       transactionsTable.getColumns().add(culbuyingBrokerName);
       transactionsTable.getColumns().add(culBuyingCustomerID);
       culBuyingCustomerID.setVisible(false);
       transactionsTable.getColumns().add(culbuyingCustomerName);
       transactionsTable.getColumns().add(culStockID);
       culStockID.setVisible(false);
       transactionsTable.getColumns().add(culstockName);
       transactionsTable.getColumns().add(culAmount);
       transactionsTable.getColumns().add(culPrice);
       transactionsTable.getColumns().add(culTime);
       transactionsTable.getColumns().add(culSessionID);
       
   }

}
