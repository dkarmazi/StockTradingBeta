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
public class SellOrderController implements Initializable {

	private ServerInterface si = (ServerInterface) Utility.serverInterface;
	private String clientSessionID = Utility.getCurrentSessionId();
	private int brokerId;
	private int brokerageFirmId;
	private int orderTypeId = 1;

	private int lBound;
	private int uBound;
	private String startTime;


    @FXML private ListView<KeyValuePair> PendingOrdersListView = new ListView<>();  
    @FXML private ComboBox<KeyValuePair> CustomerComboBox = new ComboBox<>();
    @FXML private ChoiceBox<KeyValuePair> StockChoiceBox = new ChoiceBox<>();
    @FXML private Label SessionStart;
    @FXML private Label SessionParams;
    @FXML private Label Message;
    @FXML private TextField StockQuantity;
    @FXML private TextField StockPrice;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
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
    	    	
    	PopulateCustomers();
    	SetScreenModeAddNew();
    }

    
    
    
    
	public void onCustomerSelected() {
		String sCustomerId = getComboBoxSelection(CustomerComboBox);
		if (isNumeric(sCustomerId)) {
			int customerId = Integer.parseInt(sCustomerId);
			PopulateStocks(customerId);
			StockChoiceBox.setDisable(false);
		}
	}    
    
    
    public void onAddButtonClick() {
    	// raw input
    	String rCustomerId, rStockId, rAmount, rPrice;
    	
		rCustomerId = getComboBoxSelection(CustomerComboBox);
		rStockId = getChoiceBoxSelection(StockChoiceBox);
		rAmount = StockQuantity.getText();
		rPrice = StockPrice.getText();

		// initial input validation
		if (!isNumeric(rCustomerId) || !isNumeric(rStockId) || !isNumeric(rAmount)) {
			Message.setText("Form fields cannot be empty or non-numeric");
			return;
		}
		
		// initial input validation
		if (!isDouble(rPrice)) {
			Message.setText("Price field cannot be non-numeric");
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
			ServerAuthRes sar = si.placeSellingOrder(o, lBound, uBound, clientSessionID);

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
		PendingOrdersListView.getItems().clear();
		
		try {
			ServerAuthRes sar = si.selectOrdersByFirmByType(brokerageFirmId, typeId, clientSessionID);

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
		CustomerComboBox.getItems().clear();

		try {
			ServerAuthRes sar = si.selectCustomersByFirm(brokerageFirmId, clientSessionID);

			if(sar.isHasAccess()) {
				ArrayList<CustomerInfo> allCustomers = (ArrayList<CustomerInfo>) sar.getObject();
						        
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
        StockChoiceBox.getItems().clear();

        try {
			ServerAuthRes sar = si.selectCustomerStocksLimited(customerId, clientSessionID);
			
			if(sar.isHasAccess()) {
              ArrayList<Stock> allStocks = (ArrayList<Stock>) sar.getObject();
		        
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
    	SetScreenModeAddNew();
    }
    
        
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) throws Exception { 
    	if (PendingOrdersListView.getItems().isEmpty() || PendingOrdersListView.getSelectionModel().getSelectedItem() == null) {
    		return;
    	}
    	String rOrderId = PendingOrdersListView.getSelectionModel().getSelectedItem().getKey();
    	if (!isNumeric(rOrderId)) {
    		return;
    	}
		// get proper formating
		int orderId = Integer.parseInt(rOrderId);
		
		try {
			ServerAuthRes sar = si.deleteOrder(orderId, clientSessionID);

			if(sar.isHasAccess()) {
				Validator v = (Validator) sar.getObject();
				
				if(!v.isVerified()) {
					Message.setText(v.getStatus());
					SetScreenModeAddNew();
					return;
				} else {
					Message.setText("Record deleted");
					SetScreenModeAddNew();
					return;
				}				
			} else {
				Message.setText("Authorization failed");
				return;				
			}		
		} catch (RemoteException e) {
			e.printStackTrace();
			SetScreenModeAddNew();
		}
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
	public void ShowDetails() {

		if (PendingOrdersListView.getItems().isEmpty()
				|| PendingOrdersListView.getSelectionModel().getSelectedItem() == null) {
			return;
		}

		KeyValuePair keyValue = PendingOrdersListView.getSelectionModel()
				.getSelectedItem();
		int orderId = Integer.parseInt(keyValue.getKey());

		try {
			ServerAuthRes sar = si.selectOrder(orderId, clientSessionID);
			if (sar.isHasAccess()) {
				Order o = (Order) sar.getObject();

				SelectKey(CustomerComboBox, "" + o.getCustomerId());
				SelectKey(StockChoiceBox, "" + o.getDisplayStockName());
				
				StockChoiceBox.getItems().clear();
				StockChoiceBox.getItems().add(new KeyValuePair(Integer.toString(o.getStockId()), o.getDisplayStockName()));				
				StockChoiceBox.getSelectionModel().selectFirst();

				StockQuantity.setText(""+o.getAmount());
				StockPrice.setText(""+o.getPrice());
			} else {
				Message.setText("Unauthorized action");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SetScreenModeEdit();
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

	public static boolean isDouble(String str) {
		try {
			double d = Double.parseDouble(str);
			if(d > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
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
	
	private String getComboBoxSelection(ComboBox<KeyValuePair> cbx) {
		String out = "";
		
		if(cbx == null || cbx.getValue() == null || cbx.getValue().getKey() == null) {
			return out;
		} else {
			out = cbx.getValue().getKey();
			return out;			
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

	public static void SelectKey(ComboBox<KeyValuePair> comboBox, String key) {
		for (KeyValuePair item : comboBox.getItems()) {
			if (item.getKey() != null) {
				if (item.getKey().equals(key)) {
					comboBox.setValue(item);
					break;
				}
			}
		}
	}

	
	
	
	
    private void SetScreenModeAddNew() {
    	PopulatePendingOrders(orderTypeId);
    	CustomerComboBox.getSelectionModel().clearSelection();
    	CustomerComboBox.setDisable(false);
    	StockChoiceBox.getItems().clear();
    	StockChoiceBox.setDisable(true);
    	StockPrice.setText(null);
    	StockQuantity.setText(null);    	
    	btnAdd.setDisable(false);
    	btnDelete.setDisable(true);
    }
    
    private void SetScreenModeEdit() {
    	CustomerComboBox.setDisable(true);
    	StockChoiceBox.setDisable(true);
    	btnAdd.setDisable(true);
    	btnDelete.setDisable(false);
    }    
}
