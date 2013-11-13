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

/**
 * FXML Controller class
 *
 * @author Sulochane
 */
public class UserActivationController implements Initializable {

    
    @FXML private PasswordField Password;
    @FXML private TextField ActivationCode;
    @FXML private TextField UserEmail;
    
    @FXML private Label Message;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private void ActivateAccount(ActionEvent event) throws IOException
    {
        // TODO
        //if the account activation successful, redirect to the main screen.
        if (true)
        {
            // Account activate - show the main window
            Stage stage = new Stage();
            Parent root =FXMLLoader.load(
            MainController.class.getResource("Main.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("Stock Trading Platform");
            //stage.initModality(Modality.NONE);
            //stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );            
            stage.show(); 

            ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
        }
    }
}
