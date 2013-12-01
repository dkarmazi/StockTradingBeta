/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StockTradingClient;

import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author Sulochane, not anymore, Dmitriy Karmazin is in control
 */
public class BuyOrderController implements Initializable {               

	private ServerInterface si = (ServerInterface) Utility.serverInterface;
	private String clientSessionID = Utility.getCurrentSessionId();
	private int brokerId;
	private int brokerageFirmId;
	private int orderTypeId = 2;

	private int lBound;
	private int uBound;
	private String startTime;

	

    @FXML private Label SessionStart;
    @FXML private Label SessionEnd;
    @FXML private Label SessionParams;
    @FXML private Label Message;
    @FXML private TextField StockQuantity;
    @FXML private TextField StockPrice;
    @FXML private ListView<KeyValuePair> PendingOrdersListView = new ListView<>();  
    @FXML private ComboBox<KeyValuePair> CustomerComboBox = new ComboBox<>();
    @FXML private ChoiceBox<KeyValuePair> StockChoiceBox = new ChoiceBox<>();
    @FXML private Button btnAdd;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    

    
    
    /**
     * START SCREEN
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    	brokerId = Utility.getCurrentUserID();
    	brokerageFirmId = Utility.getCurrentUser_BrokerageFirmID();
    	
    	
    	try {
			ServerAuthRes sar = si.getTradingSessionInfo(clientSessionID);
		
			if(sar.isHasAccess()) {
				TradingSession tradingSess = (TradingSession) sar.getObject();
				lBound = tradingSess.getLimitDown();
				uBound = tradingSess.getLimitUp();
				startTime = tradingSess.getStartTime().toString();
			} else {
				Message.setText("Authorization failed");
				return;
			}    	
    	} catch (RemoteException e) {
			Message.setText("Authorization failed");
			return;
		}
    	
    	
    	// SESSION PARAMETERS
    	String sessStart = "Start: " + startTime;
    	String sessParams = "Price increase: " + uBound + "% | Price decrease " + lBound + "%";
    	
    	SessionStart.setText(sessStart);
    	SessionParams.setText(sessParams);
    	    	
    	PopulatePendingOrders(orderTypeId);
    	PopulateCustomers();
    	SetScreenModeAddNew();
    }

    
    
    
    
    public void onCustomerSelected() {
    	StockChoiceBox.setDisable(false);
    	int customerId = Integer.parseInt(CustomerComboBox.getValue().getKey());
    	PopulateStocks(customerId);
    }
    
    
    
    public void onAddButtonClick() {
    	// raw input
    	String rCustomerId, rStockId, rAmount, rPrice;
		rCustomerId = CustomerComboBox.getValue().getKey();
		rStockId = StockChoiceBox.getValue().getKey();
		rAmount = StockQuantity.getText();
		rPrice = StockPrice.getText();

		// initial input validation
		if (!isNumeric(rCustomerId) || !isNumeric(rStockId) || !isNumeric(rAmount)) {
			Message.setText("Quantity can be integer only");
			return;
		}
		
		// initial input validation
		if (!isDouble(rPrice)) {
			Message.setText("Price can be numeric only");
			return;
		}
		
		// get proper formating
		int iCustomerId = Integer.parseInt(rCustomerId);
		int iStockId = Integer.parseInt(rStockId);
		int iAmount = Integer.parseInt(rAmount);
		double iPrice = Double.parseDouble(rPrice);
				
		
		Order o = new Order();
		o.setTypeId(orderTypeId);
		o.setBrokerId(brokerId);
		o.setCustomerId(iCustomerId);
		o.setStockId(iStockId);
		o.setAmount(iAmount);
		o.setPrice(iPrice);
		o.setStatusId(1);
				
		try {
			ServerAuthRes sar = si.placeBuyingOrder(o, lBound, uBound, clientSessionID);

			if(sar.isHasAccess()) {
				Validator v = (Validator) sar.getObject();
				
				if(!v.isVerified()) {
					Message.setText(v.getStatus());
					return;
				} else {
					Message.setText("Record added");
					SetScreenModeAddNew();
					return;
				}
				
			} else {
				Message.setText("Authorization failed");
				return;				
			}
		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    
    
	public void PopulatePendingOrders(int typeId) {

		ServerAuthRes sar;
		
		try {
			sar = si.selectOrdersByFirmByType(brokerageFirmId, typeId, clientSessionID);

			if(sar.isHasAccess()) {
				ArrayList<Order> allPendingOrders = (ArrayList<Order>) sar.getObject();			 
				for (Order order : allPendingOrders) {
					PendingOrdersListView.getItems().add(
							new KeyValuePair(Integer.toString(order.getOrderId()),
									order.getDisplaySummary()));
				}				
			} else {
				Message.setText("Authorization failed");
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void PopulateCustomers() {
		try {
			ServerAuthRes sar = si.selectCustomersByFirm(brokerageFirmId, clientSessionID);

			if(sar.isHasAccess()) {
				ArrayList<CustomerInfo> allCustomers = (ArrayList<CustomerInfo>) sar.getObject();
				
				CustomerComboBox.getItems().clear();
		        
		        for(CustomerInfo c : allCustomers) {
		        	CustomerComboBox.getItems().add(new KeyValuePair(Integer.toString(c.getId()), c.getFirstName() + " " + c.getLastName()));
		        }
			} else {
				Message.setText("Authorization failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void PopulateStocks(int customerId) {
		try {
			ServerAuthRes sar = si.selectCustomerStocks(customerId, clientSessionID);
			

			if(sar.isHasAccess()) {
              ArrayList<Stock> allStocks = (ArrayList<Stock>) sar.getObject();
				
              StockChoiceBox.getItems().clear();
		        
		        for(Stock c : allStocks) {
		        	StockChoiceBox.getItems().add(new KeyValuePair(Integer.toString(c.getId()), c.getName()));
		        }
			} else {
				Message.setText("Authorization failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
		
		
		
		
		
		
		
	
    
    
    
    
    
    
    
    
    
    @FXML
    private void handleClearButtonAction(ActionEvent event) {

    	StockQuantity.setText(null);
    	StockPrice.setText(null);
    	PendingOrdersListView.getSelectionModel().clearSelection();
    	CustomerComboBox.getSelectionModel().clearSelection();
    	StockChoiceBox.getSelectionModel().clearSelection();
    	btnSave.setDisable(true);    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @FXML 
    private void handleAddButtonAction(ActionEvent event) throws Exception
    {
    }
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) throws Exception
    {            
    
    }
    
    @FXML
    private void handleShowCustomerInfo()
    {
    }
    
    @FXML
    private void handleShowStockInfo()
    {
    }
    
    @FXML
    public void ShowDetails()
    {
    }
    
    private void PopulateStocks()
    {
    } 
    private void PopulateSellOrders()
    {
    }
    
    
    
    
    
    
    /**
	 * SERVICE FUNCTIONS
	 */
	public static boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    
    
    private void SetScreenModeAddNew() {
    	PopulatePendingOrders(orderTypeId);
    	StockChoiceBox.setDisable(true);
    	btnSave.setDisable(true);    	
    }
    
    private void SetScreenModeEdit() {
    	CustomerComboBox.setDisable(true);
    	StockChoiceBox.setDisable(true);
    	btnAdd.setDisable(true);
    }    
}
