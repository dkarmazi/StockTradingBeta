

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
public class ViewOrdersController implements Initializable {


    @FXML private TableView BuyingOrdersTableView;
    @FXML private TableView SellingOrdersTableView;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBuyingTable();
        initSellingTable();
        populateBuyingOrders();
        populateSellingOrders();
    }    
    
    
    private void initBuyingTable()
    {
        BuyingOrdersTableView.setEditable(false);
          
        TableColumn stockName = new TableColumn("Stock Name");
        stockName.setPrefWidth(150);
        stockName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayStockName"));
        
        TableColumn firmName = new TableColumn("BrokerageFirm");
        firmName.setPrefWidth(150);
        firmName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayFirmName"));
        
        TableColumn customerName = new TableColumn("Customer Name");
        customerName.setPrefWidth(150);
        customerName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayCustomerName"));
        
        TableColumn stockPrice = new TableColumn("Asking Price");
        stockPrice.setPrefWidth(100);
        stockPrice.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("price"));
        
        TableColumn stockQuantity = new TableColumn("Quantity");
        stockQuantity.setPrefWidth(100);
        stockQuantity.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("amount"));
        
        BuyingOrdersTableView.getColumns().clear();
        BuyingOrdersTableView.getColumns().add(stockName);
        BuyingOrdersTableView.getColumns().add(firmName);
        BuyingOrdersTableView.getColumns().add(customerName);
        BuyingOrdersTableView.getColumns().add(stockPrice);
        BuyingOrdersTableView.getColumns().add(stockQuantity);
        

    }
    
    private void initSellingTable()
    {
  
        SellingOrdersTableView.setEditable(false);
        
        TableColumn stockName = new TableColumn("Stock Name");
        stockName.setPrefWidth(150);
        stockName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayStockName"));
        
        TableColumn firmName = new TableColumn("BrokerageFirm");
        firmName.setPrefWidth(150);
        firmName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayFirmName"));
        
        TableColumn customerName = new TableColumn("Customer Name");
        customerName.setPrefWidth(150);
        customerName.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("displayCustomerName"));
        
        TableColumn stockPrice = new TableColumn("Asking Price");
        stockPrice.setPrefWidth(100);
        stockPrice.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("price"));
        
        TableColumn stockQuantity = new TableColumn("Quantity");
        stockQuantity.setPrefWidth(100);
        stockQuantity.setCellValueFactory(
                new PropertyValueFactory<Stock, String>("amount"));
        
        
        SellingOrdersTableView.getColumns().clear();
        SellingOrdersTableView.getColumns().add(stockName);
        SellingOrdersTableView.getColumns().add(firmName);
        SellingOrdersTableView.getColumns().add(customerName);
        SellingOrdersTableView.getColumns().add(stockPrice);
        SellingOrdersTableView.getColumns().add(stockQuantity);

    }
    
    private void populateBuyingOrders()
    {
        Utility.PopulateBuyingOrders(BuyingOrdersTableView);
    }
    
    private void populateSellingOrders()
    {
        Utility.PopulateSellingOrders(SellingOrdersTableView);
    }
}
