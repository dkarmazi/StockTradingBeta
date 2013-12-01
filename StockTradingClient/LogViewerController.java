/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class LogViewerController implements Initializable {

    @FXML private RadioButton LogActivityRadioButton;
    @FXML private RadioButton DBActivityRadioButton;
    @FXML private TextArea LogText;
    
    
    ToggleGroup LogTypeToggleGroup = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        LogActivityRadioButton.setToggleGroup(LogTypeToggleGroup);
        DBActivityRadioButton.setToggleGroup(LogTypeToggleGroup);
        
        LogActivityRadioButton.setSelected(true);
        
        ShowLog();
    }    
    
    @FXML
    private void ShowLog()
    {
       //LogText.setText(" ");
       LogText.clear();
       if (LogTypeToggleGroup.getSelectedToggle().equals(LogActivityRadioButton))
       {
           ShowLoginActivity();
       }
       else if (LogTypeToggleGroup.getSelectedToggle().equals(DBActivityRadioButton))
       {
           ShowDbActivity();
       }
       LogText.appendText(" ");
    }
    
    private void ShowLoginActivity()
    {
        LogText.setText(Utility.getAuditLogLoginActivity());
    }
    
    private void ShowDbActivity()
    {
        LogText.setText(Utility.getAuditLogDbActivity());
    }
}
