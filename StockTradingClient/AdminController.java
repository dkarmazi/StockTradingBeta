

package StockTradingClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import StockTradingCommon.Enumeration;
import StockTradingServer.*;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya
 */
public class AdminController implements Initializable {

    @FXML private TextField AdminFirstName;
    @FXML private TextField AdminLastName;
    @FXML private TextField AdminSSN;
    @FXML private TextField Email;
    @FXML private TextField Password1; 
    @FXML private TextField Password2; 
    
    @FXML private ChoiceBox<KeyValuePair> StatusChoiceBox = new ChoiceBox<>();   
    @FXML private ListView<KeyValuePair> AdministratorsListView = new ListView<>();
    
    @FXML private Label Message;
    @FXML private Label Password;
    @FXML private Label PasswordClassification;
    @FXML private Label PasswordGradeVeryWeek;
    @FXML private Label PasswordGradeWeek;
    @FXML private Label PasswordGradeGood;
    @FXML private Label PasswordGradeStrong;
    
    @FXML private Button btnAdd;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnRandomPassword;    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SetPasswordBarometerDefaultColor();
        
        Utility.PopulateStatus(StatusChoiceBox);

        PopulateAdministrators();
        
        SetScreenModeAddNew();
    }    
    
    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        
        Password1.visibleProperty().setValue(true);
        Password2.visibleProperty().setValue(true);
        Password.visibleProperty().setValue(true);        

        clearScreen();
        
        SetScreenModeAddNew();
    }
    
    @FXML
    public void ShowDetails()
    {
        clearScreen();
        
        if (AdministratorsListView.getItems().isEmpty() || AdministratorsListView.getSelectionModel().getSelectedItem() == null)
        {
            return;
        }
        
        // Hide passwords
        //Password1.visibleProperty().setValue(false);
        //Password2.visibleProperty().setValue(false);
        //Password.visibleProperty().setValue(false);
        
        KeyValuePair keyValue = AdministratorsListView.getSelectionModel().getSelectedItem();       
       
        UserAdmin admin = Utility.GetAdminInfo(Integer.parseInt( keyValue.getKey()));
        
        AdminFirstName.setText(admin.getFirstName());
        AdminLastName.setText(admin.getLastName());
        Email.setText(admin.getEmail());
        AdminSSN.setText(admin.getSsn());
        
        StatusChoiceBox.getSelectionModel().select(admin.getStatusId());
        
        Message.setText(null);
        
        SetScreenModeEdit();        
    }
    
    @FXML 
    private void handleSaveButtonAction(ActionEvent event) 
    {       
        KeyValuePair keyValue =    AdministratorsListView.getSelectionModel().getSelectedItem();
        
        UserAdmin admin = new UserAdmin();
        if (keyValue.getKey() == null)
        {
            Message.setText("Cannot find the Admin ID.");
            return;
        }
        admin.setId(Integer.parseInt(keyValue.getKey()));        
        admin.setFirstName(AdminFirstName.getText());
        admin.setLastName(AdminLastName.getText());
        admin.setSsn(AdminSSN.getText());
        admin.setEmail(Email.getText());
        admin.setPassword(Password1.getText());
        admin.setStatusId(Integer.parseInt(StatusChoiceBox.getValue().getKey()));
        admin.setRoleId(Enumeration.UserRole.USER_ROLE_ADMIN);

        
        Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);
        Validator validator = Utility.UpdateAdmin(admin);
        if (validator.isVerified())
        {
            PopulateAdministrators();
            Message.setText(Enumeration.Database.DB_UPDATE_SUCCESS);
        }
        else
        {
            Message.setText(validator.getStatus());
        } 
        AdministratorsListView.getSelectionModel().select(keyValue);
    }
    
    @FXML 
    private void handleAddButtonAction(ActionEvent event) 
    {
        if (!Password1.getText().equals(Password2.getText()))
        {
            Message.setText("Password confirmation does not match.");
            return;
        }

        UserAdmin admin = new UserAdmin();
        
        admin.setFirstName(AdminFirstName.getText());
        admin.setLastName(AdminLastName.getText());
        admin.setSsn(AdminSSN.getText());
        admin.setEmail(Email.getText());
        admin.setPassword(Password1.getText());
        admin.setStatusId(Integer.parseInt(StatusChoiceBox.getValue().getKey()));
        admin.setRoleId(Enumeration.UserRole.USER_ROLE_ADMIN);

        Message.setText(Enumeration.Database.DB_REQUEST_INITIATED);
        Validator validator = Utility.AddAdmin(admin);
        if (validator.isVerified())
        {
            PopulateAdministrators();
            Message.setText(Enumeration.Database.DB_INSERT_SUCCESS);
        }
        else
        {
            Message.setText(validator.getStatus());
        }       
    }
    
    @FXML 
    private void handleRandomPasswordAction(ActionEvent event) 
    {/*
        Security security = new Security();
        Password1.setText(security.CreateRandomPassword());
        Password2.setText( Password1.getText());
        handlePasswordClassification(event);
        //Email.setText( Password1.getText());
    */}
    
    @FXML
    private void handlePasswordClassification(ActionEvent event)
    { /*
        SetPasswordBarometerDefaultColor();
        String activeGrade = "-fx-border-color:#000000;";
        if (
                Password1.getText().equals( Password2.getText()) &&
                !Password2.getText().trim().equals("")
            )
        {
            switch  (Utility.GradePassword(Password2.getText()))
            {
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYWEAK:
                    activeGrade += "-fx-background-color: #FF0000;";
                    PasswordGradeVeryWeek.setStyle(activeGrade);
                    break;
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK:
                    activeGrade += "-fx-background-color: #FF6666;";
                    PasswordGradeWeek.setStyle(activeGrade);
                    break;
                    
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_GOOD:
                    activeGrade += "-fx-background-color: #FF6600;";
                    PasswordGradeGood.setStyle(activeGrade);
                    break;
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_STRONG:
                    activeGrade += "-fx-background-color: #669900;";
                    PasswordGradeStrong.setStyle(activeGrade);
                    break;
                /*case Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYSTRONG:
                    activeGrade += "-fx-background-color: #FF0000;";
                    PasswordGrade??.setStyle(activeGrade);
                    break; *   
                    
                    
            }
            //PasswordClassification.setText("Password Strength is : " + passwordClassifier.GradePassword(Password2.getText()));
        }
        else
        {
            Message.setText("Check your password. Password should be non-empty and the confirmation should be matched.");
            
        }*/
    }
    
    @FXML
    private void handleResetPassword(ActionEvent event)
    {
        

            KeyValuePair keyValue = AdministratorsListView.getSelectionModel().getSelectedItem();       
            String newPasswordMsg = "";
            int brokerId = Integer.parseInt( keyValue.getKey());
            Validator validator =Utility.ChangePassword(brokerId, Password1.getText(), Password2.getText());
            if (validator.isVerified())
            {
                newPasswordMsg = "\nPassword is reset to " + Password2.getText();
            }
            Message.setText(validator.getStatus() + newPasswordMsg);
                    

    }
    
    public void PopulateAdministrators()
    {
        clearScreen();
        
        Utility.PopulateAdministrators(AdministratorsListView);
        SetScreenModeAddNew();
    }
        
    private void clearScreen()
    {
        AdminFirstName.clear();  
        AdminLastName.clear();  
        AdminSSN.clear();  
        Email.clear();  
        Password1.clear();
        Password2.clear();
        
        Message.setText(null);
        
        SetPasswordBarometerDefaultColor();
    }
    
    private void SetPasswordBarometerDefaultColor()
    {
        String defultBackground = "-fx-background-color: #B2B2B2; -fx-border-color: #8E8E8E;";
        PasswordGradeVeryWeek.setStyle(defultBackground);
        PasswordGradeWeek.setStyle(defultBackground);
        PasswordGradeGood.setStyle(defultBackground);
        PasswordGradeStrong.setStyle(defultBackground);
    }
    
    private void SetScreenModeAddNew()
    {
        btnAdd.disableProperty().set(false);    // Add Enabled
        btnSave.disableProperty().set(true);    // Save Disabled
        
        StatusChoiceBox.getSelectionModel().selectFirst();
        AdministratorsListView.getSelectionModel().select(null);
    }
    
    private void SetScreenModeEdit()
    {
        btnAdd.disableProperty().set(true);         // Add Disabled
        btnSave.disableProperty().set(false);       // Save Enabled
    }
}
