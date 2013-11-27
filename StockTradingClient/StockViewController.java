/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingClient;

import StockTradingServer.Stock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Hirosh Wickramasuriya <hirosh@gwmail.gwu.edu>
 */
public class StockViewController implements Initializable {

    public class StockView
    {
        
    }

    @FXML private TableView stocksTableView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        populateStocksAvailableForSession();
    }    
    
    private void initTable()
    {
        stocksTableView.setEditable(false);
        
        TableColumn stockName = new TableColumn("Stock Name");
        stockName.setMinWidth(350);
        stockName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("name"));
        
        TableColumn stockPrice = new TableColumn("Price");
        stockPrice.setMinWidth(150);
        stockPrice.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("price"));
        
        
        stocksTableView.getColumns().clear();
        stocksTableView.getColumns().add(stockName);
        stocksTableView.getColumns().add(stockPrice);

    }
    
    private void populateStocksAvailableForSession()
    {
        Utility.PopulateStocksForTradingSession(stocksTableView);
    }
}
