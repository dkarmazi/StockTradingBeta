
package StockTradingClient;

import StockTradingCommon.Enumeration;
import StockTradingServer.PasswordClassifier;
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
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya
 */
public class PasswordChangeController implements Initializable {

    @FXML private Label     UserEmail;
    @FXML private PasswordField PasswordOld;
    @FXML private PasswordField PasswordNew;
    @FXML private PasswordField PasswordConfirm;
    @FXML private Label Message;
    @FXML private Label Hint;
    @FXML private Label PasswordGradeVeryWeek;
    @FXML private Label PasswordGradeWeek;
    @FXML private Label PasswordGradeGood;
    @FXML private Label PasswordGradeStrong;
    @FXML private Button ChangePassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Hint.setText("Hint : Enter your new password more than eight characters with mix of upper case letters, "
                    + "lower case letters, digits and special characters.");
        ResetScreenAppearance();
        
        PasswordConfirm.textProperty().addListener
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
        
        PasswordNew.textProperty().addListener
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
        UserEmail.setText(Utility.getCurrentUserEmail());
        SetPasswordBarometerDefaultColor();
        ChangePassword.setDisable(true);
        Message.setText("");
    }
    
    private void SetPasswordBarometerDefaultColor()
    {
        String defultBackground = "-fx-background-color: #B2B2B2; -fx-border-color: #8E8E8E;";
        PasswordGradeVeryWeek.setStyle(defultBackground);
        PasswordGradeWeek.setStyle(defultBackground);
        PasswordGradeGood.setStyle(defultBackground);
        PasswordGradeStrong.setStyle(defultBackground);       
    }    
    
    @FXML 
    public void CheckPasswordStrength(ActionEvent event) throws IOException
    {
        SetPasswordBarometerDefaultColor();
        String activeGrade = "-fx-border-color:#000000;";       

        if (
                PasswordNew.getText().equals( PasswordConfirm.getText()) &&
                !PasswordConfirm.getText().trim().equals("")
            )
        {
            PasswordClassifier passwordClassifier = new PasswordClassifier();
            
            switch (passwordClassifier.GradePassword(PasswordConfirm.getText()))
            {
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYWEAK:
                    activeGrade += "-fx-background-color: #FF0000;";
                    PasswordGradeVeryWeek.setStyle(activeGrade);

                    ChangePassword.setDisable(true);
                    break;
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK:
                    activeGrade += "-fx-background-color: #FF6666;";
                    PasswordGradeWeek.setStyle(activeGrade);

                    ChangePassword.setDisable(true);
                    break;

                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_GOOD:
                    activeGrade += "-fx-background-color: #FF6600;";
                    PasswordGradeGood.setStyle(activeGrade);

                    ChangePassword.setDisable(false);
                    break;
                case Enumeration.PasswordGrade.PASSWORD_STRENGTH_STRONG:
                    activeGrade += "-fx-background-color: #669900;";
                    PasswordGradeStrong.setStyle(activeGrade);

                    ChangePassword.setDisable(false);
                    break;
                /*case Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYSTRONG:
                    activeGrade += "-fx-background-color: #FF0000;";
                    PasswordGrade??.setStyle(activeGrade);
                    * 
                    ChangePassword.setDisable(false);
                    break; */   
            }
        }
        else
        {
            Message.setText("Your new password is empty or do not match with the confirmation.");
        }

    }
    
    @FXML 
    public void ChangePassword(ActionEvent event) throws IOException
    {        
        // TODO
        
        //if the password change successful, redirect to the main screen.
        if (true)
        {
            // password changed - show the main window
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
