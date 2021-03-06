
package StockTradingClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import StockTradingServer.*;
import StockTradingCommon.Enumeration;


/**
 * FXML Controller class
 *
 * @author Sulochane
 */
public class BrokerFirmController implements Initializable {

    
    @FXML private TextField BrokerageFirmName;    
    @FXML private TextField BrokerageFirmLicenseNumber;    
    @FXML private TextField SupervisorEmail;  
    @FXML private TextField AddressStreet;        
    @FXML private TextField AddressState;        
    @FXML private TextField AddressCity;            
    @FXML private TextField AddressZip;  
    
    @FXML private ChoiceBox<KeyValuePair> StatusChoiceBox = new ChoiceBox<KeyValuePair>();
    
    @FXML private ListView<KeyValuePair> BrokerageFirmListView = new ListView<KeyValuePair>();
    @FXML private Label Message;
    
    @FXML private Button btnAdd;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    
    @FXML 
    private void handleAddButtonAction(ActionEvent event) {    

        StockTradingServer.BrokerageFirm brokerageFirm = new StockTradingServer.BrokerageFirm();
        
        brokerageFirm.setName(BrokerageFirmName.getText());
        brokerageFirm.setLicenceNumber(BrokerageFirmLicenseNumber.getText());
        brokerageFirm.setSupervisorEmail(SupervisorEmail.getText());
        brokerageFirm.setAddressStreet(AddressStreet.getText());
        brokerageFirm.setAddressCity(AddressCity.getText());
        brokerageFirm.setAddressState(AddressState.getText());
        brokerageFirm.setAddressZip(AddressZip.getText());
        
        
        if(StatusChoiceBox.getValue().getKey() != null)
        {
            brokerageFirm.setStatus( Integer.parseInt(StatusChoiceBox.getValue().getKey()));
        }

        Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);
        Validator validator =  Utility.AddBrokerageFirm(brokerageFirm);
        
        if (validator.isVerified())
        {
            PopulateBrokerageFirms();
            Message.setText(Enumeration.Database.DB_INSERT_SUCCESS);
        }
        else
        {
            Message.setText(validator.getStatus());
        }     
    }
        
    @FXML
    private void handleSaveButtonAction(ActionEvent event) 
    {        
        StockTradingServer.BrokerageFirm brokerageFirm = new StockTradingServer.BrokerageFirm();
        KeyValuePair keyValueBrokerageFirmSelected =    BrokerageFirmListView.getSelectionModel().getSelectedItem();
        brokerageFirm.setId(Integer.parseInt(keyValueBrokerageFirmSelected.getKey()));
        brokerageFirm.setName(BrokerageFirmName.getText());
        brokerageFirm.setLicenceNumber(BrokerageFirmLicenseNumber.getText());
        brokerageFirm.setSupervisorEmail(SupervisorEmail.getText());
        brokerageFirm.setAddressStreet(AddressStreet.getText());
        brokerageFirm.setAddressCity(AddressCity.getText());
        brokerageFirm.setAddressState(AddressState.getText());
        brokerageFirm.setAddressZip(AddressZip.getText());
        
        
        if(StatusChoiceBox.getValue().getKey() != null)
        {
            brokerageFirm.setStatus( Integer.parseInt(StatusChoiceBox.getValue().getKey()));
        }     
        
        Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);
        Validator validator = Utility.UpdateBrokerageFirm(brokerageFirm);
        
        if (validator.isVerified())
        {
            PopulateBrokerageFirms();
            Message.setText(Enumeration.Database.DB_UPDATE_SUCCESS);
        }
        else
        {
            Message.setText(validator.getStatus());
        }
    }
    
    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        
        clearScreen();       
        
        SetScreenModeAddNew();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        PopulateStatus();
        PopulateBrokerageFirms();
        SetScreenModeAddNew();
    }
    
    private void PopulateStatus()
    {
        //Utility utility = new Utility();
        Utility.PopulateStatus(StatusChoiceBox);        
    }   
    
    private void PopulateBrokerageFirms()
    {
        clearScreen();
        Utility.PopulateBrokerageFirms(BrokerageFirmListView);
        SetScreenModeAddNew();
    }
    
    @FXML
    private void ShowDetails()
    {
        clearScreen();
        
        if (BrokerageFirmListView.getItems().isEmpty() || BrokerageFirmListView.getSelectionModel().getSelectedItem() == null)
        {
            return;
        }
        KeyValuePair brokerageFirmKeyValue = BrokerageFirmListView.getSelectionModel().getSelectedItem();
        	
       
        BrokerageFirm brokerageFirm = Utility.GetBrokerageFirmInfo(Integer.parseInt( brokerageFirmKeyValue.getKey()));
        
        //brokerageFirm.setBrokerageFirm(Integer.parseInt( brokerageFirm.getKey()));
        
        BrokerageFirmName.setText(brokerageFirm.getName());    
        BrokerageFirmLicenseNumber.setText(brokerageFirm.getLicenceNumber());   
        SupervisorEmail.setText(brokerageFirm.getSupervisorEmail());
        AddressStreet.setText(brokerageFirm.getAddressStreet());        
        AddressState.setText(brokerageFirm.getAddressState());        
        AddressCity.setText(brokerageFirm.getAddressCity()); 
        AddressZip.setText(brokerageFirm.getAddressZip()); 
        
        StatusChoiceBox.getSelectionModel().select(brokerageFirm.getStatus());
        
        SetScreenModeEdit();
    }
    private void clearScreen()
    {
        BrokerageFirmName.clear();  
        BrokerageFirmLicenseNumber.clear();  
        SupervisorEmail.clear();  
        AddressStreet.clear();  
        AddressState.clear();
        AddressCity.clear();
        AddressZip.clear();
        
        StatusChoiceBox.getSelectionModel().selectFirst();
        
        Message.setText(null);
    }
    
    private void SetScreenModeAddNew()
    {
        btnAdd.disableProperty().set(false);    // Add Enabled
        btnSave.disableProperty().set(true);    // Save Disabled
        
        StatusChoiceBox.getSelectionModel().selectFirst();
        BrokerageFirmListView.getSelectionModel().select(null);
    }
    
    private void SetScreenModeEdit()
    {
        btnAdd.disableProperty().set(true);    // Add Disabled
        btnSave.disableProperty().set(false);    // Save Enabled
    }
}
