/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.CustomerInfo;
import StockTradingServer.Relationship;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.Validator;

/**
 * FXML Controller class
 * 
 * @author Dmitriy Karmazin
 */
public class CustomerStocksController implements Initializable {

	// get meta data for this user
	private ServerInterface si = (ServerInterface) Utility.serverInterface;
	private String clientSessionID = Utility.getCurrentSessionId();
	private int brokerageFirmId = Utility.getCurrentUser_BrokerageFirmID();
	private int customerId;

	@FXML
	private Button btnAdd;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private Label Message;
	@FXML
	private ListView<KeyValuePair> CustomerStocksListView = new ListView<>();
	@FXML
	private ChoiceBox<KeyValuePair> CustomerStocksChoiceBox = new ChoiceBox<>();
	@FXML
	private ChoiceBox<KeyValuePair> CustomerStocksStatusChoiceBox = new ChoiceBox<>();
	@FXML
	private TextField CustomerStockAmount;
	@FXML
	private Label screenTitleBar;

	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
		String rStockId, rAmount, rStatus;

		rStockId = CustomerStocksChoiceBox.getValue().getKey();
		rAmount = CustomerStockAmount.getText();
		rStatus = CustomerStocksStatusChoiceBox.getValue().getKey();

		// initial input validation
		if (!isNumeric(rStockId) || !isNumeric(rAmount) || !isNumeric(rStatus)) {
			Message.setText("Form fields cannot be empty or non-numeric");
			return;
		}

		Relationship r = new Relationship();
		r.setId1(customerId);
		r.setId2(Integer.parseInt(rStockId));
		r.setExtra(Integer.parseInt(rAmount));
		r.setStatus(Integer.parseInt(rStatus));

		try {
			ServerAuthRes sar = si.updateHasCustomerStocks(r, clientSessionID);

			if (sar.isHasAccess()) {
				Validator v = (Validator) sar.getObject();

				if (v.isVerified()) {
					Message.setText("Record saved");
				} else {
					Message.setText(v.getStatus());
				}
			} else {
				Message.setText("Error, authorization failed");
			}
			SetScreenModeAddNew();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleAddButtonAction(ActionEvent event) {
		String rStockId, rAmount, rStatus;

		rStockId = getChoiceBoxSelection(CustomerStocksChoiceBox);
		rAmount = CustomerStockAmount.getText();
		rStatus = getChoiceBoxSelection(CustomerStocksStatusChoiceBox);

		// initial input validation
		if (!isNumeric(rStockId) || !isNumeric(rAmount) || !isNumeric(rStatus)) {
			Message.setText("Form fields cannot be empty or non-numeric");
			return;
		}

		Relationship r = new Relationship();
		r.setId1(customerId);
		r.setId2(Integer.parseInt(rStockId));
		r.setExtra(Integer.parseInt(rAmount));
		r.setStatus(Integer.parseInt(rStatus));

		try {
			ServerAuthRes sar = si.insertHasCustomerStocks(r, clientSessionID);

			if (sar.isHasAccess()) {
				Validator v = (Validator) sar.getObject();

				if (v.isVerified()) {
					Message.setText("Record saved");
				} else {
					Message.setText(v.getStatus());
				}
			} else {
				Message.setText("Error, authorization failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SetScreenModeAddNew();
	}

	
	
	private String getChoiceBoxSelection(ChoiceBox<KeyValuePair> cbx) {
		String out = "";
		
		if(cbx == null || cbx.getValue() == null || cbx.getValue().getKey() == null) {
			return out;
		} else {
			out = cbx.getValue().getKey();
			return out;			
		}
	}
	
	
	
	
	@FXML
	private void handleClearButtonAction(ActionEvent event) {
		ClearScreen();
	}

	/**
	 * This method populates UI fields for selected customer stock
	 */
	@FXML
	public void ShowDetails() {

		if (CustomerStocksListView.getItems().isEmpty()
				|| CustomerStocksListView.getSelectionModel().getSelectedItem() == null) {
			return;
		}

		KeyValuePair keyValue = CustomerStocksListView.getSelectionModel()
				.getSelectedItem();
		int stockId = Integer.parseInt(keyValue.getKey());

		try {
			ServerAuthRes sar = si.selectHasCustomerStocks(customerId, stockId,
					clientSessionID);
			if (sar.isHasAccess()) {
				Relationship r = (Relationship) sar.getObject();

				SelectKey(CustomerStocksChoiceBox, "" + r.getId2());
				CustomerStocksChoiceBox.setDisable(true);
				CustomerStockAmount.setText("" + r.getExtra());
				SelectKey(CustomerStocksStatusChoiceBox, "" + r.getStatus());

			} else {
				Message.setText("Unauthorized action");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SetScreenModeEdit();
	}

	/**
	 * STARTING THE SCREEN
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// get data from previous screen
		customerId = CustomerController.getCustomerId();
		screenTitleBar.setText("Customer stocks for "
				+ CustomerController.getsCustomerInfo());

		SetScreenModeAddNew();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Populate all stocks
	 */
	public void populateAllStocks() {

		CustomerStocksChoiceBox.getItems().clear();

		try {
			ServerAuthRes sar = si.selectAllStocks(clientSessionID);

			if (sar.isHasAccess()) {
				ArrayList<Stock> allStocks = (ArrayList<Stock>) sar.getObject();

				for (Stock s : allStocks) {
					CustomerStocksChoiceBox.getItems().add(
							new KeyValuePair(Integer.toString(s.getId()), s
									.getName()));
				}

			} else {
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate statuses
	 */
	public void populateAllStatuses() {

		CustomerStocksStatusChoiceBox.getItems().clear();

		try {

			ArrayList<StatusesOptions> allStatuses = (ArrayList<StatusesOptions>) si
					.selectAllStatuses(clientSessionID).getObject();

			for (StatusesOptions s : allStatuses) {
				CustomerStocksStatusChoiceBox.getItems().add(
						new KeyValuePair(Integer.toString(s.getId()), s
								.getName()));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void populateAllExistingStocks() {
		CustomerStocksListView.getItems().clear();

		try {
			ServerAuthRes sar = si.selectCustomerStocks(this.customerId,
					clientSessionID);

			if (sar.isHasAccess()) {
				ArrayList<Stock> stocks = (ArrayList<Stock>) sar.getObject();

				for (Stock stock : stocks) {
					CustomerStocksListView.getItems().add(
							new KeyValuePair(Integer.toString(stock.getId()),
									stock.getName()));
				}

			} else {
				Message.setText("Authentication failed");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void SelectKey(ChoiceBox<KeyValuePair> comboBox, String key) {
		for (KeyValuePair item : comboBox.getItems()) {
			if (item.getKey() != null) {
				if (item.getKey().equals(key)) {
					comboBox.setValue(item);
					break;
				}
			}
		}
	}

	/**
	 * This method clears entered UI values
	 */
	private void ClearScreen() {
		btnAdd.setDisable(false);
		btnSave.setDisable(true);
		CustomerStocksListView.getSelectionModel().clearSelection();
		CustomerStocksChoiceBox.getSelectionModel().clearSelection();
		CustomerStocksChoiceBox.setDisable(false);
		CustomerStocksStatusChoiceBox.getSelectionModel().clearSelection();
		CustomerStockAmount.clear();
		Message.setText(null);
	}

	/**
	 * Prepares screen for mode add new broker
	 */
	private void SetScreenModeAddNew() {
		btnAdd.setDisable(false);
		btnSave.setDisable(true);
		populateAllExistingStocks();
		populateAllStocks();
		populateAllStatuses();
		CustomerStockAmount.clear();		
	}

	/**
	 * Prepares screen for mode edit new broker
	 */
	private void SetScreenModeEdit() {
		btnAdd.setDisable(true);
		btnSave.setDisable(false);
		Message.setText(null);
		CustomerStocksChoiceBox.setDisable(true);
	}

	
	
	/**
	 * SERVICE FUNCTIONS
	 */
	public static boolean isNumeric(String str) {		
		try {
			int d = Integer.parseInt(str);
			if(d > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException nfe) {
			return false;
		}
		
	}
}
