package StockTradingClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import StockTradingServer.Validator;
/**
 * FXML Controller class
 *
 * @author Hirosh Sulochane Wickramasuriya
 */
public class UserPasswordRecover implements Initializable {

    
    @FXML private PasswordField Password;
    @FXML private TextField ActivationCode;
    @FXML private TextField UserEmail;
    
    @FXML private Label Message;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserEmail.textProperty().addListener
        (
            new ChangeListener<String>()
            {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
                {
                    ResetScreenAppearance();
                }
            }
        );
        ActivationCode.textProperty().addListener
        (
            new ChangeListener<String>()
            {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
                {
                    ResetScreenAppearance();
                }
            }
        );
        Password.textProperty().addListener
        (
            new ChangeListener<String>()
            {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
                {
                    ResetScreenAppearance();
                }
            }
        );
    }   
    
    private void ResetScreenAppearance()
    {
        Message.setText("");
    }
        
    @FXML
    private void PasswordRecoveryHandle(ActionEvent event) throws IOException
    {

        //if the password recovery successful, redirect to the change password screen.
        Validator validator = Utility.RecoverPasswordRequest(UserEmail.getText().trim()
                                                    , Password.getText()
                                                    , ActivationCode.getText());
        if (validator.isVerified())
        {
            // Password activation success
            
            Utility.setCurrentSessionId(validator.getSession());
            Utility.SetCurrentUser(UserEmail.getText().trim());
            Utility.setPasswordRecoverMode(true);
            
            Stage stage = new Stage();
            Parent root =FXMLLoader.load(
            MainController.class.getResource("PasswordChange.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("Stock Trading Platform");
            //stage.initModality(Modality.NONE);
            //stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );            
            stage.show(); 

            ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window*/
        }
        else
        {
            Message.setText(validator.getStatus());
        }
    }
}
