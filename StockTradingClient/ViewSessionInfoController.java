/**
 * @author Ahmad Kouraiem
 */
package StockTradingClient;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import StockTradingServer.ServerAuthRes;
import StockTradingServer.TradingSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class ViewSessionInfoController implements Initializable {

	@FXML private Label noTradingSession;
	
	@FXML private Label sessionStartedAt;
	@FXML private Label sessionStartedAtValue;
	@FXML private Label	limitUp;
	@FXML private Label limitUpValue;
	@FXML private Label limitDown;
	@FXML private Label limitDownValue;
	@FXML private Label availableStock;
	@FXML private Label availableFirms;
	
	
	@FXML private ListView<KeyValuePair> availableStocksListView = new ListView<KeyValuePair>();
	@FXML private ListView<KeyValuePair> availableFirmsListView = new ListView<KeyValuePair>();
	
	private void prepareInterface() throws RemoteException{
		ServerAuthRes AuthRes = Utility.serverInterface.isThereActiveTradingSession(Utility.getCurrentSessionId());
		if (AuthRes.isHasAccess()){
			boolean thereIsSession = (boolean) AuthRes.getObject();
			if (thereIsSession){
				noTradingSession.setVisible(false);
				
				ServerAuthRes SAR = Utility.serverInterface.getTradingSessionInfo(Utility.getCurrentSessionId());
				if (SAR.isHasAccess()){
					TradingSession TS = (TradingSession) SAR.getObject();
					sessionStartedAtValue.setText(TS.getStartTime().toString());
					limitUpValue.setText(TS.getLimitUp()+"%");
					limitDownValue.setText(TS.getLimitDown()+"%");
					
					availableStocksListView.getItems().clear();
					for (String s : TS.getAvailableStocksNames()){
						KeyValuePair e = new KeyValuePair("1", s);
						availableStocksListView.getItems().add(e);
					}
					
					availableFirmsListView.getItems().clear();
					for (String s : TS.getAvailableFirmsNames()){
						KeyValuePair e = new KeyValuePair("1", s);
						availableFirmsListView.getItems().add(e);
					}
					
				}else{
					System.out.println("NOT ENOUGH ACCESS.");
				}
				
				
				
			}else{
				sessionStartedAt.setVisible(false);
				sessionStartedAtValue.setVisible(false);
				limitUp.setVisible(false);
				limitUpValue.setVisible(false);
				limitDown.setVisible(false);
				limitDownValue.setVisible(false);
				availableStock.setVisible(false);
				availableFirms.setVisible(false);
				availableStocksListView.setVisible(false);
				availableFirmsListView.setVisible(false);
			}
		}else{
			System.out.println("User not allowed to invoke this method: isThereActiveTradingSession.");
			return;
		}
		 
	}
	
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {    	
    	try {
			prepareInterface();
//	    	Utility.PopulateStocks(allStocksListView);
//	    	Utility.PopulateBrokerageFirms(allFirmListView);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
}
