/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package StockTradingClient;

import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.*;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.control.*;

/**
 * @date    :   Oct 21, 2013
 * @author  :   Hirosh Wickramasuriya
 */
public class  Utility 
{
   // private static int brokerageFirmID = -1;     // when a broker logs in his Brokerage Firm id should be here
    private static int brokerID = -1;            // when a broker logs in his id should be here
    private static User currentUser = new User();                  // when a user logs in, current user details set here 
    
    private static ServerInterface serverInterface = null;
   //  private static DatabaseConnector dbConnector = new DatabaseConnector(); 
    
    public static void setServerInterface(ServerInterface _serverInterface) {
    	Utility.serverInterface = _serverInterface;
    }
    
    /*public static int getBrokerID() {
        return brokerID;
    }

    public static void setBrokerID(int brokerID) {
        Utility.brokerID = brokerID;
    }*/
    public static int getCurrentUserID()
    {
        return currentUser.getId();
    }
    public static int getCurrentUserFirmID() {
        return currentUser.getBrokerFirmId();
    }

    //public static void setBrokerageFirmID(int brokerageFirmID) {
    //    Utility.brokerageFirmID = brokerageFirmID;
    //}
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static int getCurrentUserRole() {
        return currentUser.getRoleId();
    }
    //public static void setCurrentUserRole(int currentUserRole) {
    //    currentUser.setRoleId(currentUserRole);
    //}
    public static String getCurrentUserEmail()
    {
        return currentUser.getEmail();
    }
    /*public static int getCurrentUser_BrokerID() {
        return currentUser.get;
    }*/
    public static int getCurrentUser_BrokerageFirmID() {
        return currentUser.getBrokerFirmId();
    }
    
    public  Utility()
    {
        //  dbConnector = new StockTradingServer.DatabaseConnector();
    }
    
    public static boolean isValidDate(String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        boolean isValid = false;
        Date testDate = null;
        try
        {
            testDate = simpleDateFormat.parse(date);
            if (!simpleDateFormat.format(testDate).equals(date)) 
            {
                isValid = false;
            }
            else 
            {
                isValid = true;
            }            
        }
        catch (ParseException ex)
        {
             isValid = false;
        }
        finally
        {
            return isValid;
        } 
    }
    public static boolean isNumber(String number)
    {
        return number.matches("\\d+(\\.\\d+)?"); 
    }
    public static String FormatNumber(String number)
    {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(number);
    }
    public static String FormatNumber(int number)
    {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(number);
    }
    public static String FormatNumber(double number)
    {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(number);
    }
        
    public static void PopulateStatus(ChoiceBox choiceBox)
    {
        //StockTradingServer.DatabaseConnector dbConnector = new StockTradingServer.DatabaseConnector();     
        
        ArrayList<StatusesOptions> statuses = null;
		try {
			statuses = serverInterface.selectAllStatuses();
			//System.out.println("asdasds" + statuses.toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
        choiceBox.getItems().add(new KeyValuePair("-1", "Select Status"));

        for(StatusesOptions s : statuses)
        {
            choiceBox.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getName() ));
        }
        choiceBox.getSelectionModel().selectFirst();
    }
    public static void SelectKey(ComboBox<KeyValuePair> comboBox, String key)
    {
        for (KeyValuePair item : comboBox.getItems()) 
        {
            if (item.getKey() != null)
            {
                if (item.getKey().equals(key))
                {
                    //comboBox.getSelectionModel().select(item);
                    comboBox.setValue(item);
                    break;
                }
            }
        }
    }

    public static void SelectKey(ComboBox<KeyValuePair> comboBox, int key)
    {
        SelectKey(comboBox, Integer.toString(key));
    }

    
    // Stock
    public static Validator AddStock(Stock stock)
    {
        //return dbConnector.insertNewStock(stock);     
        Validator validator = null;
        try
        {
            validator = serverInterface.insertNewStock(stock);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return validator;
    }
    public static Validator UpdateStock(Stock stock)
    {
        //return dbConnector.updateStock(stock.getId(), stock);    
        Validator validator = null;
        try
        {
            validator = serverInterface.insertNewStock(stock);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }  
        return validator;
    }
    public static void PopulateStocks(ComboBox comboBox)
    {
        comboBox.getItems().clear();
        
        ArrayList<Stock> records = null;        
        try
        {
            records = serverInterface.selectStockAll();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }  
        comboBox.getItems().add(new KeyValuePair("-1", "[Select Stock]"));

        for(Stock s : records)
        {
            comboBox.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getName() ));
        }
        comboBox.getSelectionModel().selectFirst();
    }
    
    public static void PopulateStocks(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<Stock> records = null;
        try
        {
            records = serverInterface.selectStockAll();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        for(Stock s : records)
        {
            listView.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getName() ));
        }
        listView.getSelectionModel().selectFirst();
    }
    public static Stock GetStockInfo(int stockId)
    {
        Stock stock = null;
        try
        {
            stock = serverInterface.selectStock(stockId);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return stock;
    }
    
    // Broker
    public static Validator AddBroker(User broker)
    {
        Validator validator = null;
        
        try
        {
            validator = serverInterface.insertNewBroker(broker);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Validator UpdateBroker(User broker)
    {
        Validator validator = null;
        
        try
        {
            validator = serverInterface.updateBroker(broker.getId(), broker); 
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static void PopulateBrokers(int brokerageFirm, ListView listView)
    {        
        listView.getItems().clear();
        ArrayList<User> records = null;
        if (brokerageFirm ==0)
        {
            int statusID = 0;

            try
            {
                records = serverInterface.selectBrokersAll(statusID)  ;   
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                records = serverInterface.selectBrokersAllbyFirm(brokerageFirm);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        for (User s : records)
        {
            listView.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getFirstName() + " " + s.getLastName()));
        }
    } 
    public static User GetBrokerInfo(int brokerId)
    {
        User user = null;
     
        try
        {
            user = serverInterface.selectBrokerUser(brokerId);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    
    // Brokerage Firm
    public static Validator AddBrokerageFirm(BrokerageFirm brokerageFirm)
    {
        Validator validator = null;        
        try
        {
            validator = serverInterface.insertNewBrokerageFirm(brokerageFirm);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Validator UpdateBrokerageFirm(BrokerageFirm brokerageFirm)
    {
        Validator validator = null;
        
        try
        {
            validator = serverInterface.updateBrokerageFirm(brokerageFirm.getId(), brokerageFirm); 
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }    
    public static void PopulateBrokerageFirms(ComboBox comboBox)
    {
        comboBox.getItems().clear();
          
        ArrayList<BrokerageFirm> records = null;
        try
        {
            records = serverInterface.selectBrokerageFirmsAll();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        comboBox.getItems().add(new KeyValuePair(null, "[All Brokerage Firms]"));

        for(BrokerageFirm s : records)
        {
            comboBox.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getName() ));
        }
        comboBox.getSelectionModel().selectFirst();
    }    
    public static void PopulateBrokerageFirms(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<BrokerageFirm> records = null;       
                
        try
        {
            records = serverInterface.selectBrokerageFirmsAll();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        for(BrokerageFirm s : records)
        {
            listView.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getName() ));
        }
    }
    
    // Customer
    public static CustomerInfo GetCustomerInfo(int customerId)
    {
        CustomerInfo customerInfo = null;
        
        try
        {
            customerInfo = serverInterface.selectCustomerInfo(customerId);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return customerInfo;
    }    
    public static Validator AddCustomer(CustomerInfo customer)
    {
        customer.setFirmId(getCurrentUser_BrokerageFirmID());
        Validator validator = null;
        
        try
        {
            validator = serverInterface.insertNewCustomerInfo(customer);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Validator UpdateCustomer(CustomerInfo customer)
    {
        customer.setFirmId(getCurrentUser_BrokerageFirmID());
        Validator validator = null;
        
        try
        {
            validator = serverInterface.updateCustomerInfo(customer.getId(), customer);  
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static void PopulateCustomers(ComboBox comboBox)
    {
        ArrayList<CustomerInfo> records = null;
        
         try
        {
            records = serverInterface.selectCustomerInfoAll();   
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
                
        comboBox.getItems().clear();
        comboBox.getItems().add(new KeyValuePair("-1", "[Select Customer]"));

        for(CustomerInfo s : records)
        {
            comboBox.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getFirstName() + " " + s.getLastName()));
        }
        comboBox.getSelectionModel().selectFirst();
    }
    public static void PopulateCustomers(ListView listView, int brokerageFirmID)
    {
        ArrayList<CustomerInfo> records = null;        
        try
        {
            records = serverInterface.selectCustomersByFirm(brokerageFirmID);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        listView.getItems().clear();
        
        for(CustomerInfo s : records)
        {
            listView.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getFirstName() + " " + s.getLastName()));
        }
    } 
    public static void PopulateCustomersOfFirm(ComboBox comboBox)
    {
        ArrayList<CustomerInfo> records = null;
        try
        {
            records = serverInterface.selectCustomersByFirm(getCurrentUser_BrokerageFirmID()); 
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        comboBox.getItems().clear();
        comboBox.getItems().add(new KeyValuePair(null, "[Select Customer]"));
        
        for(CustomerInfo s : records)
        {
            comboBox.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getFirstName() + " " + s.getLastName()));
        }
        comboBox.getSelectionModel().selectFirst();
    }
    // Buying Order
    public static Validator AddBuyingOrder(Order order)
    {
        order.setTypeId(Enumeration.OrderType.BUYING_ORDER);
        order.setBrokerId(getCurrentUserID());
        
        Validator validator = null;
        
        try
        {
            validator = serverInterface.insertNewOrder(order);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;        
    }
    public static Validator UpdateBuyingOrder(Order order)
    {
        order.setTypeId(Enumeration.OrderType.BUYING_ORDER);
        order.setBrokerId(getCurrentUserID());
        
        Validator validator = null;
        
        try
        {
            validator = serverInterface.updateOrder(order.getOrderId(), order);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Order GetBuyingOrder(int orderID)
    {
        Order order = null;
        
        try
        {
            order = serverInterface.selectOrder(orderID);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return order;
    }
    public static void PopulateBuyingOrdersByBrokerageFirm(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<Order> records = null; 
        try
        {
            records = serverInterface.selectOrdersByFirmByType(
                                                    getCurrentUser_BrokerageFirmID()
                                                    , Enumeration.OrderType.BUYING_ORDER); 
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        for(Order s : records)
        {
            listView.getItems().add(    new KeyValuePair(
                                                    Integer.toString(s.getOrderId())
                                                    , s.getDisplaySummary()  )
                                    );
        }
    }
    public static void PopulateBuyingOrders(ListView listView)
    {
        listView.getItems().clear();
        //TODO:
//        ArrayList<Order> records = dbConnector.selectBuyingOrdersActiveOnly();        
//        for(Order s : records)
//        {
//            listView.getItems().add(new KeyValuePair(Integer.toString(s.getOrderId()),Integer.toString(s.getOrderId()) ));
//        }
    }
        
    // Selling Order
    public static Validator AddSellingOrder(Order order)
    {
        order.setTypeId(Enumeration.OrderType.SELLING_ORDER);
        order.setBrokerId(getCurrentUserID());
        
        Validator validator = null;
        
        try
        {
            validator = serverInterface.insertNewOrder(order);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Validator UpdateSellingOrder(Order order)
    {
        order.setTypeId(Enumeration.OrderType.SELLING_ORDER);
        order.setBrokerId(getCurrentUserID());
        Validator validator = null;
        
        try
        {
            validator = serverInterface.updateOrder(order.getOrderId(), order);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;        
    }
    public static Order GetSellingOrder(int orderID)
    {        
        Order order = null;
        
        try
        {
            order = serverInterface.selectOrder(orderID);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return order;
        
    }
    public static void PopulateSellingOrdersByBrokerageFirm(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<Order> records =  null;

        
        try
        {
            records = serverInterface.selectOrdersByFirmByType(
                                                    getCurrentUser_BrokerageFirmID()
                                                    , Enumeration.OrderType.SELLING_ORDER);    
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        
        for(Order s : records)
        {
            listView.getItems().add(    new KeyValuePair(
                                         Integer.toString(s.getOrderId())
                                         , s.getDisplaySummary()  )
                                    );
        }
    }
    public static void PopulateSellingOrders(ListView listView)
    {
        //TODO
//        listView.getItems().clear();
//        ArrayList<Order> records = dbConnector.selectSellingOrdersActiveOnly();        
//        for(Order s : records)
//        {
//            listView.getItems().add(new KeyValuePair(Integer.toString(s.getOrderId()),Integer.toString(s.getOrderId()) ));
//        }
    }
    
    //User
    public static Validator AuthenticateUser(String userName, String password)
    {
        Validator status = new Validator();
        status.setVerified(false);
        if ( (userName.equalsIgnoreCase("admin")) && (password.equalsIgnoreCase("admin")) )
        {
            status.setVerified(true);
            setCurrentUser( GetUser("admin"));
        }
        else if ( (userName.equalsIgnoreCase("broker")) && (password.equalsIgnoreCase("broker")) )
        {
            status.setVerified(true);
            setCurrentUser(GetUser("broker" ));
        }
        return status;
    }
    public static User GetUser(String userEmail)
    {
        //TODO :Get the user info
        User user = new User();
        user.setId(44);
        if (userEmail.trim().equalsIgnoreCase("admin"))
        {
            user.setBrokerFirmId(-1);
            user.setRoleId(Enumeration.UserRole.USER_ROLE_ADMIN);

            user.setFirstName("Admin");
            user.setLastName("Administrator");
            user.setEmail("admin@admin.com");
        }
        else if (userEmail.trim().equalsIgnoreCase("broker"))
        {
            user.setBrokerFirmId(11);
            user.setRoleId(Enumeration.UserRole.USER_ROLE_BROKER);

            user.setFirstName("Brook");
            user.setLastName("Broker");
            user.setEmail("brook@broker.com");            
        }
                
        return user;
    }
}
