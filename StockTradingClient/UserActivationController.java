
package StockTradingClient;

import StockTradingServer.Validator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya
 */
public class UserActivationController implements Initializable {

    
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
    }   
    
    private void ResetScreenAppearance()
    {
        Message.setText("");
    }
    
    @FXML
    private void ActivateAccount(ActionEvent event) throws IOException
    {

        //if the account activation successful, redirect to the main screen (or change password if expired).
        Validator validator = Utility.UnlockAccountRequest(UserEmail.getText().trim()
                                                        , Password.getText()
                                                        , ActivationCode.getText());
        if (validator.isVerified())
        {
            // Account activate - show the main window
            
            Utility.setCurrentSessionId(validator.getSession());
            Utility.SetCurrentUser(UserEmail.getText().trim());
            
            Stage stage = new Stage();
            Parent root = null;
            if (Utility.IsPasswordExpired(Utility.getCurrentUserID()))
            {
                root = FXMLLoader.load(
                            MainController.class.getResource("PasswordChange.fxml")
                        );

                stage.setScene(new Scene(root));
                stage.setTitle("Stock Trading Platform");
                stage.initModality(Modality.WINDOW_MODAL);
                
                stage.show(); 
            }
            else
            {
                root = FXMLLoader.load(
                            MainController.class.getResource("Main.fxml")
                        );

                stage.setScene(new Scene(root));
                stage.setTitle("Stock Trading Platform");
    
                stage.show();
            }                        

            ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
        }
        else
        {
            Message.setText(validator.getStatus());
        }
    }
}
