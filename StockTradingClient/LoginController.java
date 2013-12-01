
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
import javafx.scene.control.*;
import javafx.stage.Stage;

import StockTradingServer.Validator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
/**
 * FXML Controller class
 *
 * @author Sulochane
 */
public class LoginController implements Initializable {


    @FXML private TextField     UserEmail;
    @FXML private PasswordField Password;
    @FXML private Label Message;    

            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        UserEmail.requestFocus();
                    }
                }
                );
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
        UserEmail.setText("dkarmazi@gwu.edu");
        Password.setText("StocksTradingPassword");
    }
    
    private void ResetScreenAppearance()
    {
        Message.setText("");
    }
        
    @FXML
    public void Login(ActionEvent event) throws IOException
    {
        Validator loginStatus = Utility.AuthenticateUser( UserEmail.getText().trim(), Password.getText().trim() );
        
        if (loginStatus.isVerified())
        {

            System.out.println(loginStatus.getSession());
            Utility.setCurrentSessionId(loginStatus.getSession());
            Utility.SetCurrentUser(UserEmail.getText().trim());              

            
            Stage stage = new Stage();
            Parent root = null;

            if (Utility.IsFirstLogin(Utility.getCurrentUserID())
                    || Utility.IsPasswordExpired(Utility.getCurrentUserID()))
            {
                root = FXMLLoader.load(
                            MainController.class.getResource("PasswordChange.fxml")
                        );

                stage.setScene(new Scene(root));
                stage.setTitle("Stock Trading Platform");
                stage.setResizable(false);
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

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
                {
                    public void handle(WindowEvent we) 
                    {
                        // Unset the session id of the current user
                        Utility.unSetCurrentSessionId();
                        
                    }
                });

                stage.show();
            }
            
                   
            
            ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
        }
        else
        {            
            Message.setText(loginStatus.getStatus());
        }        
    }
    
    @FXML
    public void ResetPasswordHandler(ActionEvent event) throws IOException
    {
        Validator validator = Utility.ResetPasswordRequest(UserEmail.getText().trim());        
        Message.setText(validator.getStatus());        
    }
    
    @FXML
    public void RecoverPasswordHandler(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                                MainController.class.getResource("UserPasswordRecover.fxml")
                            );

        stage.setScene(new Scene(root));
        stage.setTitle("Stock Trading Platform");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
              
        stage.show();
        
        ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window        
    }
    
    @FXML
    public void ActivateAccountHandler(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                                MainController.class.getResource("UserActivation.fxml")
                            );

        stage.setScene(new Scene(root));
        stage.setTitle("Stock Trading Platform");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
              
        stage.show();
        
        ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
    }
}
