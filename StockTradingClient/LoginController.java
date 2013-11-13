
package StockTradingClient;

import StockTradingCommon.Enumeration;
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

import StockTradingServer.Security;
import StockTradingServer.User;
import StockTradingServer.Validator;
import javafx.application.Platform;
import javafx.stage.Modality;
/**
 * FXML Controller class
 *
 * @author Sulochane
 */
public class LoginController implements Initializable {


    @FXML private TextField     UserEmail;
    @FXML private PasswordField Password;
    @FXML private Label Message;
    
    @FXML private Button RecoverPassword;
    @FXML private Button ActivateAccount;
            
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
        UserEmail.setText("hirosh@gwmail.gwu.edu");
        Password.setText("admin");
    }   
    @FXML
    public void Login(ActionEvent event) throws IOException
    {
        //Security secure = new Security();
        //System.out.println(secure.CreateRandomPassword());
        Validator loginStatus = Utility.AuthenticateUser( UserEmail.getText().trim(), Password.getText().trim() );
        
        if (loginStatus.isVerified())
        {
            //TODO: Get the userid  by calling the int userid = loginStatus.getCode(); 
            // then Get the userInfo by the passing the userid
            // Then check the role whether is admin or not.
            
            /*if (UserEmail.getText().trim().equalsIgnoreCase("admin"))
            {
                Utility.setCurrentUserRole(Enumeration.UserRole.USER_ROLE_ADMIN);
            }
            else
            {
                Utility.setCurrentUserRole(Enumeration.UserRole.USER_ROLE_BROKER);
            }*/
            
            // TODO Capture the proper user ids
            //User currentUser = Utility.GetUserLimited(UserEmail.getText().trim());
            
            Utility.SetCurrentUser(UserEmail.getText().trim());
                    
            // TODO 
            // Check the status to see whether the user account has been locked.
            int userStatus = 0;
            
            Stage stage = new Stage();
            Parent root = null;
            switch (userStatus)
            {
                case 0: // active user  
                    
        
                    root = FXMLLoader.load(
                                MainController.class.getResource("Main.fxml")
                            );

                    stage.setScene(new Scene(root));
                    stage.setTitle("Stock Trading Platform");
                    //stage.initModality(Modality.NONE);
                    //stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );            
                    stage.show(); 
                    
                    break;
                    
                case 1: // password expired
                    
                    root = FXMLLoader.load(
                                MainController.class.getResource("PasswordChange.fxml")
                            );

                    stage.setScene(new Scene(root));
                    stage.setTitle("Stock Trading Platform");
                    stage.initModality(Modality.WINDOW_MODAL);
                    //stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );            
                    stage.show(); 
                    
                    break ;
                    
              /*  case 2: // account locked
                    
                    root = FXMLLoader.load(
                                MainController.class.getResource("UserActivation.fxml")
                            );

                    stage.setScene(new Scene(root));
                    stage.setTitle("Stock Trading Platform");
                    stage.initModality(Modality.WINDOW_MODAL);
                    //stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );            
                    stage.show();                     
                    break;*/
                
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
        // TODO: Reset password
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
        stage.initModality(Modality.WINDOW_MODAL);
              
        stage.show();
        
        ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
    }
}
