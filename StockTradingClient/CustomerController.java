/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.CustomerInfo;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.Validator;

/**
 * FXML Controller class
 * 
 * @author Sulochane - not anymore, now Dmitriy Karmazin
 */
public class CustomerController implements Initializable {

	
	// get meta data for this user
	private ServerInterface si = (ServerInterface) Utility.serverInterface;
	private String clientSessionID = Utility.getCurrentSessionId();
	private int brokerageFirmId = Utility.getCurrentUserFirmID();
	
	

	@FXML
	private TextField CustomerFirstName;
	@FXML
	private TextField CustomerLastName;
	@FXML
	private TextField CustomerEmail;
	@FXML
	private TextField CustomerPhone;
	@FXML
	private TextField CustomerBalance;
	@FXML
	private ChoiceBox<KeyValuePair> StatusChoiceBox = new ChoiceBox<>();
	@FXML
	private ListView<KeyValuePair> CustomerListView = new ListView<>();
	@FXML
	private Label Message;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnCustomerStocks;
	
	
	/**
	 * SHOW STOCKS SCREEN
	 */
	@FXML
	private void handleCustomerStocksButtonAction(ActionEvent event) {
		try {

			// get customer id for which the button has been clicked
			int customerId = (int) btnCustomerStocks.getUserData();
			
	        Stage stage = new Stage();
	        Parent root = FXMLLoader.load(MainController.class.getResource("CustomerStocks.fxml"));
	        root.setUserData(customerId);
	        	        
	        stage.setScene(new Scene(root));
	        stage.setTitle("Customers");
	        stage.initModality(Modality.NONE);
	        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );

	        stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
	
	

	@FXML
	private void handleClearButtonAction(ActionEvent event) {
		ClearScreen();
		SetScreenModeAddNew();
	}

	/**
	 * ADD BUTTON HANDLER
	 * This function will add a new customer to the database;
	 * @param event
	 */
	@FXML
	private void handleAddButtonAction(ActionEvent event) {
		CustomerInfo customer = new CustomerInfo();

		// getting data from UI
		customer.setFirstName(CustomerFirstName.getText());
		customer.setLastName(CustomerLastName.getText());
		customer.setEmail(CustomerEmail.getText());
		customer.setPhone(CustomerPhone.getText());
		// handle casting of balance
		double balance = 0;
		if (!CustomerBalance.getText().isEmpty()) {
			balance = Double.parseDouble(CustomerBalance.getText());
		}
		customer.setBalance(balance);
		customer.setPendingBalance(0);
		
		// get status from dropbox
		if (StatusChoiceBox.getValue().getKey() != null) {
			customer.setStatusId(Integer.parseInt(StatusChoiceBox.getValue()
					.getKey()));
		}
		customer.setFirmId(brokerageFirmId);
		// end getting data from UI
		
		// update message to the user
		Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);

		// start procedure
		try {
			// get the main auth object, perform action
			ServerAuthRes sar = si.insertNewCustomerInfo(customer,
					clientSessionID);

			// if this user is eligible to invoke needed function
			if (sar.isHasAccess()) {
				// perform action
				Validator v = (Validator) sar.getObject();

				if (v.isVerified()) {
					 Utility.PopulateCustomers(CustomerListView,
					 brokerageFirmId);
					 Message.setText(Enumeration.Database.DB_INSERT_SUCCESS);
				} else {
					Message.setText(v.getStatus());
				}
			} else {
				Message.setText("Access denied: prohibited function invokation");
			}

		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * SAVE BUTTON HANDLER
	 * @param event
	 */
	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
		KeyValuePair keyValue = CustomerListView.getSelectionModel()
				.getSelectedItem();

		if (keyValue.getKey() == null) {
			Message.setText("Cannot find the Customer ID.");
			return;
		}
		
		

		CustomerInfo customer = new CustomerInfo();
		// getting data from UI
		customer.setId(Integer.parseInt(keyValue.getKey()));
		customer.setFirstName(CustomerFirstName.getText());
		customer.setLastName(CustomerLastName.getText());
		customer.setEmail(CustomerEmail.getText());
		customer.setPhone(CustomerPhone.getText());
		// handle casting of balance
		double balance = 0;
		if (!CustomerBalance.getText().isEmpty()) {
			balance = Double.parseDouble(CustomerBalance.getText());
		}
		customer.setBalance(balance);
		customer.setPendingBalance(0);		
		// get status from dropbox
		if (StatusChoiceBox.getValue().getKey() != null) {
			customer.setStatusId(Integer.parseInt(StatusChoiceBox.getValue()
					.getKey()));
		}
		customer.setFirmId(brokerageFirmId);
		// end getting data from UI

		Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);

		// start procedure
		try {
			// get the main auth object, perform action
			ServerAuthRes sar = si.updateCustomerInfo(customer.getId(), customer, clientSessionID);

			// if this user is eligible to invoke needed function
			if (sar.isHasAccess()) {
				// perform action
				Validator v = (Validator) sar.getObject();

				if (v.isVerified()) {
					 Utility.PopulateCustomers(CustomerListView,
					 brokerageFirmId);
					 Message.setText(Enumeration.Database.DB_INSERT_SUCCESS);
					 CustomerListView.getSelectionModel().select(keyValue);
				} else {
					Message.setText(v.getStatus());
				}
			} else {
				Message.setText("Access denied: prohibited function invokation");
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	private void handleAddNewButtonAction(ActionEvent event) {
		ClearScreen();
		SetScreenModeAddNew();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		Message.setText("broker"+Utility.getCurrentUserFirmID());
		
		Utility.PopulateStatus(StatusChoiceBox);		
		Utility.PopulateCustomers(CustomerListView,
				Utility.getCurrentUser_BrokerageFirmID());
		// PopulateBrokers();
		
		btnCustomerStocks.setDisable(true);

		SetScreenModeAddNew();
	}
	
	/**
	 * This method populates UI fields for selected customer
	 */
	@FXML
	public void ShowDetails() {
		ClearScreen();

		if (CustomerListView.getItems().isEmpty()
				|| CustomerListView.getSelectionModel().getSelectedItem() == null) {
			return;
		}

		KeyValuePair keyValue = CustomerListView.getSelectionModel()
				.getSelectedItem();

		CustomerInfo customer = Utility.GetCustomerInfo(Integer
				.parseInt(keyValue.getKey()));

		CustomerFirstName.setText(customer.getFirstName());
		CustomerLastName.setText(customer.getLastName());
		CustomerEmail.setText(customer.getEmail());
		CustomerPhone.setText(customer.getPhone());
		CustomerBalance.setText(""+customer.getBalance());
		StatusChoiceBox.getSelectionModel().select(customer.getStatusId());
		
		btnCustomerStocks.setDisable(false);
		btnCustomerStocks.setUserData(customer.getId());
		
		Message.setText(null);
		SetScreenModeEdit();
	}

	/**
	 * This method clears entered UI values
	 */
	private void ClearScreen() {
		CustomerFirstName.clear();
		CustomerLastName.clear();
		CustomerEmail.clear();
		CustomerPhone.clear();
		CustomerBalance.clear();
		btnCustomerStocks.setDisable(true);
		
		Message.setText(null);
	}

	/**
	 * Prepares screen for mode add new broker
	 */
	private void SetScreenModeAddNew() {
		btnAdd.disableProperty().set(false); // Add Enabled
		btnSave.disableProperty().set(true); // Save Disabled

		StatusChoiceBox.getSelectionModel().selectFirst();
		CustomerListView.getSelectionModel().select(null);
		btnCustomerStocks.setDisable(true);
		
	}

	/**
	 * Prepares screen for mode edit new broker
	 */
	private void SetScreenModeEdit() {
		btnAdd.disableProperty().set(true); // Add Disabled
		btnSave.disableProperty().set(false); // Save Enabled
	}
}
