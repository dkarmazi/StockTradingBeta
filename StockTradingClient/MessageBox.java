/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingClient;

import java.io.IOException;
import java.util.HashSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import StockTradingCommon.Enumeration;
/**
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class MessageBox{

    public MessageBox(String message) {
        this.title = "Stock Trading Platform";
        this.message = message;
    }

    public MessageBox(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public MessageBox(String title, String message, Enumeration.MessageIcon iconType) {
        this.message = message;
        this.title = title;
        this.iconType = iconType;
    }
    
    public MessageBox(String message, Enumeration.MessageIcon iconType) {
        this.message = message;
        this.title = "Stock Trading Platform";
        this.iconType = iconType;
    }
    
    private String message;
    private String title;
    private Enumeration.MessageIcon iconType;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Enumeration.MessageIcon getIconType() {
        return iconType;
    }
    
    public void Show(ActionEvent event)throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MessageBox.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        MessageBoxController msgController = (MessageBoxController)fxmlLoader.getController();
        //msgController.setMessage(this.getMessage()); This doesn't work
        msgController.setMessageBodyLabel(this.getMessage());
        msgController.setMessageIcon(this.getIconType());

        Scene scene = new Scene(root);
        Stage stage = new Stage(StageStyle.UTILITY);

        stage.setScene(scene);
        stage.setTitle(getTitle());
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(  ((Node)event.getSource()).getScene().getWindow() );
        
        stage.show();
    }
}
