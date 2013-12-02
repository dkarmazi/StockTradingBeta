/**
 * @author Ahmad Kouraiem
 */
package StockTradingClient;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.Vector;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.TradingSession;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


public class TradingSessionController implements Initializable {

	
	@FXML private TextField limitUp;
	@FXML private TextField limitDown;
	
	
	@FXML private Button selectStock;
	@FXML private Button unselectStock;
	@FXML private Button selectFirm;
	@FXML private Button unselectFirm;
	@FXML private Button startTradingSession;
	@FXML private Button endTradingSession;
	@FXML private Label lblErrorMessage;
	
	
	@FXML private ListView<KeyValuePair> allStocksListView = new ListView<KeyValuePair>();
	@FXML private ListView<KeyValuePair> availableStocksListView = new ListView<KeyValuePair>();
	
	@FXML private ListView<KeyValuePair> allFirmListView = new ListView<KeyValuePair>();
	@FXML private ListView<KeyValuePair> availableFirmListView = new ListView<KeyValuePair>();
	
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	try{
	    	ServerAuthRes AuthRes = Utility.serverInterface.isThereActiveTradingSession(Utility.getCurrentSessionId());
			if (AuthRes.isHasAccess()){
				boolean thereIsSession = (boolean) AuthRes.getObject();
				if (!thereIsSession){
			    	Utility.PopulateStocksActiveOnly(allStocksListView);
			    	Utility.PopulateBrokerageFirmsActive(allFirmListView);
			    	availableStocksListView.getItems().clear();
			    	availableFirmListView.getItems().clear();
				}else{
					ServerAuthRes SAR = Utility.serverInterface.getTradingSessionInfo(Utility.getCurrentSessionId());
					if (SAR.isHasAccess()){
						
						TradingSession TS = (TradingSession) SAR.getObject();
						
						limitUp.setText(Integer.toString(TS.getLimitUp()));
						limitDown.setText(Integer.toString(TS.getLimitDown()));
						
						availableStocksListView.getItems().clear();
						for (String s : TS.getAvailableStocksNames()){
							KeyValuePair e = new KeyValuePair("1", s);
							availableStocksListView.getItems().add(e);
						}
						
						availableFirmListView.getItems().clear();
						for (String s : TS.getAvailableFirmsNames()){
							KeyValuePair e = new KeyValuePair("1", s);
							availableFirmListView.getItems().add(e);
						}
						
					   	endTradingSession.setDisable(false);
				    	
				    	limitUp.setDisable(true);
				    	limitDown.setDisable(true);
				    	
				    	allStocksListView.setDisable(true);
				    	availableStocksListView.setDisable(true);
				    	allFirmListView.setDisable(true);
				    	availableFirmListView.setDisable(true);
				    	
				    	selectStock.setDisable(true);
				    	unselectStock.setDisable(true);
				    	selectFirm.setDisable(true);
				    	unselectFirm.setDisable(true);
				    	
				    	startTradingSession.setDisable(true);
				 
						
					}else{
						System.out.println("NOT ENOUGH ACCESS.");
					}
				}
			}
    	}catch(Exception e){
    		return;
    	}
    }
    
    private boolean validInput(){
    	boolean result = true;
    	
    	try{
    		
    		String rLimitUp = limitUp.getText();
    		String rLimitDown = limitDown.getText();

    		if(!isNumeric(rLimitDown) || !isNumeric(rLimitUp)) {
    			return false;
    		}

    		if (availableStocksListView.getItems().size() == 0){ 
    			return false;
    		}
    		if (availableFirmListView.getItems().size() == 0){
    			return false;
    		}
    	}catch(Exception e){
    		return false;
    	}
    	
    	return result;
    }

    
	public static boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str);
			if (d > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

    
    
    
    @FXML
    private void handleSelectStockButton(ActionEvent event){
    	KeyValuePair s = allStocksListView.getSelectionModel().getSelectedItem();
    	allStocksListView.getSelectionModel().clearSelection();
    	availableStocksListView.getItems().add(s);
    	allStocksListView.getItems().remove(s);
    }
    
    @FXML
    private void handleUnselectStockButton(ActionEvent event){
    	KeyValuePair s = availableStocksListView.getSelectionModel().getSelectedItem();
    	availableStocksListView.getSelectionModel().clearSelection();
    	allStocksListView.getItems().add(s);
    	availableStocksListView.getItems().remove(s);
    }
    
    @FXML
    private void handleSelectFirmButton(ActionEvent event){
    	KeyValuePair s = allFirmListView.getSelectionModel().getSelectedItem();
    	allFirmListView.getSelectionModel().clearSelection();
    	availableFirmListView.getItems().add(s);
    	allFirmListView.getItems().remove(s);
    	
    }
    
    @FXML
    private void handleUnselectFirmButton(ActionEvent event){
    	KeyValuePair s = availableFirmListView.getSelectionModel().getSelectedItem();
    	availableFirmListView.getSelectionModel().clearSelection();
    	allFirmListView.getItems().add(s);
    	availableFirmListView.getItems().remove(s);
    }

    @FXML
    private void handlestartTradingSessionButton(ActionEvent event) throws RemoteException{
    	
    	if (!validInput()){
    		lblErrorMessage.setText("Not valid input.");
    		lblErrorMessage.setVisible(true);
    		return;
    	}
    	
    	lblErrorMessage.setVisible(false);
    	
    	TradingSession TS = new TradingSession();
    	java.util.Date date = new java.util.Date();
    	Timestamp currentDate = new Timestamp(date.getTime());
    	TS.setStartTime(currentDate);
    	TS.setLimitUp(Integer.valueOf(limitUp.getText()));
    	TS.setLimitDown(Integer.valueOf(limitDown.getText()));
    	TS.setActive(1);
    	
    	Vector<Integer> sa = new Vector<Integer>();
    	sa.clear();
    	
    	ObservableList<KeyValuePair> res = (ObservableList<KeyValuePair>) availableStocksListView.getItems();
    	for (KeyValuePair r : res){
    		sa.add(Integer.valueOf(r.getKey()));
    	}
    	TS.setAvailableStocksID(sa);
    	
    	Vector<Integer> fa = new Vector<Integer>();
    	
    	res = (ObservableList<KeyValuePair>) availableFirmListView.getItems();
    	for (KeyValuePair r : res){
    		fa.add(Integer.valueOf(r.getKey()));
    	}
    	TS.setAvailableFirmsID(fa);
    	
   	
    	Utility.serverInterface.startTradingSession(TS, Utility.getCurrentSessionId());
    	
    	endTradingSession.setDisable(false);
    	
    	limitUp.setDisable(true);
    	limitDown.setDisable(true);
    	
    	allStocksListView.setDisable(true);
    	availableStocksListView.setDisable(true);
    	allFirmListView.setDisable(true);
    	availableFirmListView.setDisable(true);
    	
    	selectStock.setDisable(true);
    	unselectStock.setDisable(true);
    	selectFirm.setDisable(true);
    	unselectFirm.setDisable(true);
    	
    	startTradingSession.setDisable(true);
    }
    
    @FXML
    private void handleendTradingSessionButton(ActionEvent event) throws RemoteException{
    	
    	Utility.serverInterface.endTradingSession(Utility.getCurrentSessionId());
    	
    	endTradingSession.setDisable(true);
    	
    	limitUp.setDisable(false);
    	limitDown.setDisable(false);
    	
    	allStocksListView.setDisable(false);
    	availableStocksListView.setDisable(false);
    	allFirmListView.setDisable(false);
    	availableFirmListView.setDisable(false);
    	
    	selectStock.setDisable(false);
    	unselectStock.setDisable(false);
    	selectFirm.setDisable(false);
    	unselectFirm.setDisable(false);
    	
    	startTradingSession.setDisable(false);
    	
    	Utility.PopulateStocksActiveOnly(allStocksListView);
    	Utility.PopulateBrokerageFirmsActive(allFirmListView);
    	availableStocksListView.getItems().clear();
    	availableFirmListView.getItems().clear();
    	limitUp.setText("");
    	limitDown.setText("");
    }
    
}
