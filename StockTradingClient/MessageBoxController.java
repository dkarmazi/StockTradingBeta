/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingClient;

import StockTradingCommon.Enumeration;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class MessageBoxController implements Initializable {

    @FXML private Label MessageBody;
    @FXML private ImageView Icon;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setMessageBodyLabel(String message)
    {
        MessageBody.setText(message);
    }
    
    public void setMessageIcon(Enumeration.MessageIcon icon)
    {
        Image messageIcon;
        String imageFile = "image" + System.getProperty("file.separator");

        switch (icon)
        {
            case INFORMATION:
                imageFile +=  "icon-info.png";
                messageIcon = new Image(imageFile);
                Icon.setImage(messageIcon);
                
                break;
                
            case WARNING:
                imageFile +=  "icon-warning.png";
                messageIcon = new Image(imageFile);
                Icon.setImage(messageIcon);
                
                break;
                
            case QUESTION:
                imageFile +=  "icon-question.png";
                messageIcon = new Image(imageFile);
                Icon.setImage(messageIcon);
                
                break;
                
            case ERROR:
                imageFile +=  "icon-error.png";
                messageIcon = new Image(imageFile);
                Icon.setImage(messageIcon);
                
                break;
                
            default:
                imageFile +=  "icon-info.png";
                messageIcon = new Image(imageFile);
                Icon.setImage(messageIcon);
                
                break;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // System.out.println("insdie controller :" + getMessage());
        //MessageBody.setText(getMessage());

    }    
    
    @FXML
    public void Close(ActionEvent event)
    {
        ((Node)(event.getSource())).getScene().getWindow().hide();  // hide the current window
    }
}
