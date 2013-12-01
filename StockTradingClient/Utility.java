
package StockTradingClient;

import RMI.ServerInterface;
import StockTradingCommon.Enumeration;
import StockTradingServer.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

/**
 * @date    :   Oct 21, 2013
 * @author  :   Hirosh Wickramasuriya
 * @author  :   Ahmad Kouraiem
 */
public class  Utility 
{
   // private static int brokerageFirmID = -1;          // when a broker logs in his Brokerage Firm id should be here
    private static int brokerID = -1;                   // when a broker logs in his id should be here
    private static User currentUser = new User();       // when a user logs in, current user details set here 
    private static String currentSessionId = null;      // keep the current users session id
    private static boolean passwordRecoverMode = false;

    public static ServerInterface serverInterface = null;
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
    public static void SetCurrentUser(User user) {
        currentUser = user;
    }
    public static int getCurrentUserRole() {
        return currentUser.getRoleId();
    }
    public static String getCurrentSessionId() {
        return currentSessionId;
    }
    public static void setCurrentSessionId(String currentSessionId) {
        Utility.currentSessionId = currentSessionId;
    }
    public static void unSetCurrentSessionId()
    {
        try
        {
           serverInterface.logOut(getCurrentSessionId());
           currentSessionId = null;
        }
        catch (RemoteException e) 
        {
            e.printStackTrace();
        }   
    }
    
    public static boolean isPasswordRecoverMode() 
    {
        return passwordRecoverMode;
    }

    public static void setPasswordRecoverMode(boolean passwordRecoverMode) 
    {
        Utility.passwordRecoverMode = passwordRecoverMode;
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
			//statuses = serverInterface.selectAllStatuses(getCurrentSessionId());
			
			ServerAuthRes results = serverInterface.selectAllStatuses(getCurrentSessionId());
			if (results.isHasAccess()) {
				statuses = (ArrayList<StatusesOptions>) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectAllStatuses");
				return;
			}
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
           // validator = serverInterface.insertNewStock(stock, getCurrentSessionId());
            ServerAuthRes results = serverInterface.insertNewStock(stock, getCurrentSessionId());

            if (results.isHasAccess()) {
            	validator = (Validator) results.getObject();
            }else{
            	JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewStock");
            	return null;
            }
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

			ServerAuthRes results = serverInterface.updateStock(stock.getId(), stock, getCurrentSessionId());
			
			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewStock");
				return null;
			}
        	
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
            //records = serverInterface.selectStockAll(getCurrentSessionId());

			ServerAuthRes results = serverInterface.selectStockAll
                        (
                                Enumeration.Status.ALL
                                , getCurrentSessionId()
                        );
			
			if (results.isHasAccess()) {
				records = (ArrayList<Stock>) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
				return;
			}
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
    
        public static void PopulateStocksActiveOnly(ComboBox comboBox)
    {
        comboBox.getItems().clear();
        
        ArrayList<Stock> records = null;        
        try
        {
            //records = serverInterface.selectStockAll(getCurrentSessionId());

			ServerAuthRes results = serverInterface.selectStockAll
                        (
                                Enumeration.Status.ACTIVE
                                , getCurrentSessionId()
                        );
			
			if (results.isHasAccess()) {
				records = (ArrayList<Stock>) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
				return;
			}
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
            ServerAuthRes results = serverInterface.selectStockAll
                            (
                                    Enumeration.Status.ALL
                                    , getCurrentSessionId()
                            );

            if (results.isHasAccess()) {
                    records = (ArrayList<Stock>) results.getObject();
            }else{
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
                    return;
            }
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
    
    public static void PopulateStocksActiveOnly(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<Stock> records = null;
        try
        {
            ServerAuthRes results = serverInterface.selectStockAll
                (
                    Enumeration.Status.ACTIVE
                    , getCurrentSessionId()
                );

            if (results.isHasAccess()) {
                    records = (ArrayList<Stock>) results.getObject();
            }else{
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
                    return;
            }
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
            //stock = serverInterface.selectStock(stockId, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectStock(stockId, getCurrentSessionId());

			if (results.isHasAccess()) {
				stock = (Stock) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStock");
				return null;
			}
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return stock;
    }
    public static void PopulateStocksForTradingSession(TableView tableView)
    {
        tableView.getItems().clear();

        ArrayList<Stock> records = null;
        try
        {

            ServerAuthRes results = serverInterface.selectStockAll
                    (
                            Enumeration.Status.ACTIVE
                            ,getCurrentSessionId()
                    );

            if (results.isHasAccess()) {
                    records = (ArrayList<Stock>) results.getObject();
            }else{
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
                    return;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        ObservableList<Stock> data;
        data = FXCollections.observableArrayList();
        
        
        for(Stock s : records)
        {
            data.add(s); 
        }

        tableView.setItems(data);
    }
    
    // Administrator
    public static void PopulateAdministrators(ListView listView)
    {        
        listView.getItems().clear();
        ArrayList<User> records = null;
        
        int statusID = 0;

        try
        {
            //records = serverInterface.selectBrokersAll(statusID, getCurrentSessionId())  ;
            ServerAuthRes results = serverInterface.selectAdministratorsAll(statusID, getCurrentSessionId());

            if (results.isHasAccess()) 
            {
                    records = (ArrayList<User>) results.getObject();
            }
            else
            {
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectAdministratorsAll");
                    return;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        for (User s : records)
        {
            listView.getItems().add(new KeyValuePair(Integer.toString(s.getId()), s.getFirstName() + " " + s.getLastName()));
        }
    } 
    public static UserAdmin GetAdminInfo(int adminId)
    {
        UserAdmin user = null;
     
        try
        {
            ServerAuthRes results = serverInterface.selectAdminUser(adminId, getCurrentSessionId());

            if (results.isHasAccess()) 
            {
                    user = (UserAdmin) results.getObject();
            }
            else
            {
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectAdminUser ");
                    return null;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    public static Validator AddAdmin(UserAdmin admin)
    {
        Validator validator = null;
        admin.setBrokerFirmId(Enumeration.BrokerageFirm.BROKERAGE_FIRM_ID_FOR_ADMIN);
        try
        {
            ServerAuthRes results = serverInterface.insertNewAdmin(admin, getCurrentSessionId());

            if (results.isHasAccess()) 
            {
                validator = (Validator) results.getObject();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewAdmin");
                return null;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    public static Validator UpdateAdmin(UserAdmin admin)
    {
        Validator validator = null;
        admin.setBrokerFirmId(Enumeration.BrokerageFirm.BROKERAGE_FIRM_ID_FOR_ADMIN);
        try
        {
            ServerAuthRes results = serverInterface.updateAdmin(admin.getId(), admin, getCurrentSessionId());

            if (results.isHasAccess()) 
            {
                validator = (Validator) results.getObject();
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateAdmin");
                return null;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return validator;
    }
    

    
    // Broker
    public static Validator AddBroker(User broker)
    {
        Validator validator = null;
        
        try
        {
            //validator = serverInterface.insertNewBroker(broker, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.insertNewBroker(broker, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewBroker");
				return null;
			}
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
            //validator = serverInterface.updateBroker(broker.getId(), broker, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.updateBroker(broker.getId(), broker, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateBroker");
				return null;
			}
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
                //records = serverInterface.selectBrokersAll(statusID, getCurrentSessionId())  ;
            	ServerAuthRes results = serverInterface.selectBrokersAll(statusID, getCurrentSessionId());

    			if (results.isHasAccess()) {
    				records = (ArrayList<User>) results.getObject();
    			}else{
    				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectBrokersAll");
    				return;
    			}
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
                //records = serverInterface.selectBrokersAllbyFirm(brokerageFirm, getCurrentSessionId());
            	ServerAuthRes results = serverInterface.selectBrokersAllbyFirm(brokerageFirm, getCurrentSessionId());

    			if (results.isHasAccess()) {
    				records = (ArrayList<User>) results.getObject();
    			}else{
    				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectBrokersAllbyFirm");
    				return;
    			}

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
           // user = serverInterface.selectBrokerUser(brokerId, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectBrokerUser(brokerId, getCurrentSessionId());

			if (results.isHasAccess()) {
				user = (User) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectBrokerUser ");
				return null;
			}
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    
    // Brokerage Firm
    public static BrokerageFirm GetBrokerageFirmInfo(int firmId)
    {
        BrokerageFirm brokerageFirm = null;
        try
        {
            //brokerageFirm = serverInterface.selectBrokerageFirm(firmId, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectBrokerageFirm(firmId, getCurrentSessionId());

			if (results.isHasAccess()) {
				brokerageFirm = (BrokerageFirm) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectBrokerageFirm");
				//return null;
			}
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return brokerageFirm;
    }
    public static Validator AddBrokerageFirm(BrokerageFirm brokerageFirm)
    {
        Validator validator = null;        
        try
        {
            //validator = serverInterface.insertNewBrokerageFirm(brokerageFirm, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.insertNewBrokerageFirm(brokerageFirm, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewBrokerageFirm");
				return null;
			}
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
//            validator = serverInterface.updateBrokerageFirm(brokerageFirm.getId(), brokerageFirm, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.updateBrokerageFirm(brokerageFirm.getId(), brokerageFirm, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateBrokerageFirm");
				return null;
			}

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
           // records = (ArrayList<BrokerageFirm>) serverInterface.selectBrokerageFirmsAll(getCurrentSessionId()).getObject();
        	ServerAuthRes results = serverInterface.selectBrokerageFirmsByStatus(
                        Enumeration.BrokerageFirm.BROKERAGE_FIRM_STATUS_ACTIVE_ID,
                        getCurrentSessionId());

			if (results.isHasAccess()) {
				records = (ArrayList<BrokerageFirm>) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectBrokerageFirmsAll");
				return;
			}
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
            records = (ArrayList<BrokerageFirm>) serverInterface.selectBrokerageFirmsAll(getCurrentSessionId()).getObject();
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
    public static void PopulateBrokerageFirmsActive(ListView listView)
    {
        listView.getItems().clear();
        ArrayList<BrokerageFirm> records = null;       
                
        try
        {
            records = (ArrayList<BrokerageFirm>) serverInterface.selectBrokerageFirmsByStatus
                    ( 
                            Enumeration.BrokerageFirm.BROKERAGE_FIRM_STATUS_ACTIVE_ID
                            ,getCurrentSessionId()
                    ).getObject();
            
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
           // customerInfo = serverInterface.selectCustomerInfo(customerId, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectCustomerInfo(customerId, getCurrentSessionId());

			if (results.isHasAccess()) {
				customerInfo = (CustomerInfo) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectCustomerInfo");
				return null;
			}
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
           // validator = serverInterface.insertNewCustomerInfo(customer, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.insertNewCustomerInfo(customer, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewCustomerInfo");
				return null;
			}
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
            //validator = serverInterface.updateCustomerInfo(customer.getId(), customer, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.updateCustomerInfo(customer.getId(), customer, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateCustomerInfo");
				return null;
			}

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
            //records = serverInterface.selectCustomerInfoAll(getCurrentSessionId());
        	 ServerAuthRes results = serverInterface.selectCustomerInfoAll(getCurrentSessionId());

			 if (results.isHasAccess()) {
				 records = (ArrayList<CustomerInfo>) results.getObject();
			 }else{
			 	JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectCustomerInfoAll");
			 	return;
			 }

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
            //records = serverInterface.selectCustomersByFirm(brokerageFirmID, getCurrentSessionId());
       	 	ServerAuthRes results = serverInterface.selectCustomersByFirm(brokerageFirmID, getCurrentSessionId());
		
			 if (results.isHasAccess()) {
				 records = (ArrayList<CustomerInfo>) results.getObject();
			 }else{
			 	JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectCustomersByFirm");
			 	return;
			 }

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
            //records = serverInterface.selectCustomersByFirm(getCurrentUser_BrokerageFirmID(), getCurrentSessionId());
       	 	ServerAuthRes results = serverInterface.selectCustomersByFirm(getCurrentUser_BrokerageFirmID(), getCurrentSessionId());
    		
			 if (results.isHasAccess()) {
				 records = (ArrayList<CustomerInfo>) results.getObject();
			 }else{
			 	JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectCustomersByFirm");
			 	return;
			 }

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
            //validator = serverInterface.insertNewOrder(order, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.insertNewOrder(order, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewOrder");
				return null;
			}

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
           // validator = serverInterface.updateOrder(order.getOrderId(), order, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.updateOrder(order.getOrderId(), order, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateOrder");
				return null;
			}

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
            //order = serverInterface.selectOrder(orderID, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectOrder(orderID, getCurrentSessionId());

			if (results.isHasAccess()) {
				order = (Order) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectOrder");
				return null;
			}
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
//            records = serverInterface.selectOrdersByFirmByType(
//                                                    getCurrentUser_BrokerageFirmID()
//                                                    , Enumeration.OrderType.BUYING_ORDER
//                                                    , getCurrentSessionId());
       	 	ServerAuthRes results = serverInterface.selectOrdersByFirmByType(
								                  getCurrentUser_BrokerageFirmID()
								                  , Enumeration.OrderType.BUYING_ORDER
								                  , getCurrentSessionId());
    		
			 if (results.isHasAccess()) {
				 records = (ArrayList<Order>) results.getObject();
			 }else{
			 	JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectOrdersByFirmByType");
			 	return;
			 }

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

    public static void PopulateBuyingOrders(TableView tableView)
    {
        tableView.getItems().clear();

        ArrayList<Order> records = null;
        try
        {

            ServerAuthRes results = serverInterface.selectOrderDetailsByType
                    (
                            Enumeration.OrderType.BUYING_ORDER
                            ,getCurrentSessionId()
                    );

            if (results.isHasAccess()) {
                    records = (ArrayList<Order>) results.getObject();
            }else{
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
                    return;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        ObservableList<Order> data;
        data = FXCollections.observableArrayList();
        
        
        for(Order s : records)
        {
            data.add(s); 
        }

        tableView.setItems(data);
    }
        
    // Selling Order
    public static Validator AddSellingOrder(Order order)
    {
        order.setTypeId(Enumeration.OrderType.SELLING_ORDER);
        order.setBrokerId(getCurrentUserID());
        
        Validator validator = null;
        
        try
        {
            //validator = serverInterface.insertNewOrder(order, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.insertNewOrder(order, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: insertNewOrder");
				return null;
			}

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
            //validator = serverInterface.updateOrder(order.getOrderId(), order, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.updateOrder(order.getOrderId(), order, getCurrentSessionId());

			if (results.isHasAccess()) {
				validator = (Validator) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: updateOrder");
				return null;
			}

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
            //order = serverInterface.selectOrder(orderID, getCurrentSessionId());
        	ServerAuthRes results = serverInterface.selectOrder(orderID, getCurrentSessionId());

			if (results.isHasAccess()) {
				order = (Order) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectOrder");
				return null;
			}

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
//            records = serverInterface.selectOrdersByFirmByType(
//                                                    getCurrentUser_BrokerageFirmID()
//                                                    , Enumeration.OrderType.SELLING_ORDER
//                                                    , getCurrentSessionId());    
       	 	ServerAuthRes results = serverInterface.selectOrdersByFirmByType(
									                  getCurrentUser_BrokerageFirmID()
									                  , Enumeration.OrderType.SELLING_ORDER
									                  , getCurrentSessionId());

			if (results.isHasAccess()) {
				records = (ArrayList<Order>) results.getObject();
			}else{
				JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectOrdersByFirmByType");
				return;
			}
        
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

    public static void PopulateSellingOrders(TableView tableView)
    {
        tableView.getItems().clear();

        ArrayList<Order> records = null;
        try
        {

            ServerAuthRes results = serverInterface.selectOrderDetailsByType
                    (
                            Enumeration.OrderType.SELLING_ORDER
                            ,getCurrentSessionId()
                    );

            if (results.isHasAccess()) {
                    records = (ArrayList<Order>) results.getObject();
            }else{
                    JOptionPane.showMessageDialog(null, "You are not allowed to perfom this action: selectStockAll");
                    return;
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        ObservableList<Order> data;
        data = FXCollections.observableArrayList();
        
        
        for(Order s : records)
        {
            data.add(s); 
        }

        tableView.setItems(data);
    }

    
    //User
    public static Validator AuthenticateUser(String userName, String password)
    {
        Validator validator = new Validator();
        
        /*validator.setVerified(false);
        if ( (userName.equalsIgnoreCase("admin")) && (password.equalsIgnoreCase("admin")) )
        {
            validator.setVerified(true);
            setCurrentUser( GetUser("admin"));
        }
        else if ( (userName.equalsIgnoreCase("broker")) && (password.equalsIgnoreCase("broker")) )
        {
            validator.setVerified(true);
            setCurrentUser(GetUser("broker" ));
        }*/
        try
        {
            validator = serverInterface.checkIfUsernamePasswordMatch(userName, password);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }        

        
        return validator;
    }
    
    public static void SetCurrentUser(String userEmail)
    {
        
        User user = GetUserLimited(userEmail);
        user.setEmail(userEmail);
        Utility.SetCurrentUser(user);

    }

    public static User GetUserLimited(String userEmail)
    {
        User user = new User();

        try
        {
            user = serverInterface.selectUserByEmailLimited(userEmail);               
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        
        
        /*user.setId(44);
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
        }*/
                
        return user;
    }

    public static Validator RecoverPasswordRequest(String email, String tempPassword, String activationCode)
    {
        Validator validator = new Validator();
        
        try
        {
            validator = serverInterface.checkIfUsernameTempPasswordActivationCodeMatch(email, tempPassword, activationCode);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }  
        return validator;
    }
    
    public static Validator ResetPasswordRequest(String email)
    {
        Validator validator = new Validator();
        try
        {
            validator = serverInterface.forgotPassword(email);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return validator;
    }
    
    public static Validator UnlockAccountRequest(String email, String currentPassword, String activationCode)
    {
        Validator validator = new Validator();
        try
        {
            validator = serverInterface.checkIfUsernamePasswordActivationCodeMatch(email, currentPassword, activationCode);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return validator;
    }
    
    public static Validator ChangePassword(int userid, String newPassword1, String newPassword2)
    {
        Validator validator = new Validator();
        try
        {
            validator = (Validator) serverInterface.updateUserPassword(userid, newPassword1, newPassword2, getCurrentSessionId()).getObject();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return validator;
    }
    
    public static boolean IsPasswordExpired(int userId)
    {
        boolean status = false;
        try
        {
            status = serverInterface.isUserPasswordExpired(userId);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        
        return status;
    }
    
    public static int GradePassword(String password)
    {
        int passwordGrade = 0;
        try
        {
            passwordGrade = serverInterface.GradePassword(password);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return passwordGrade;
    }
    
    public static boolean HasPasswordUsedBefore(int userId, String password)
    {
        boolean status = false;
        try
        {
            status = serverInterface.passwordHasBeenAlreadyUsed(userId 
                                                     , password
                                                     , Enumeration.Password.PASSWORD_HISTORY_COUNT);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return status;
    }
    
    public static boolean IsFirstLogin(int userId)
    {
        boolean status = false;
        try
        {
            status = serverInterface.isFirstLoginEver(userId);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } 
        return status;
    }
    
    public static boolean checkBrokerageFirmWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("insertNewBrokerageFirm", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("updateBrokerageFirm", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectBrokerageFirmsAll", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectBrokerageFirm", getCurrentSessionId());
    	
    	return results;
    }
    
    
    public static boolean checkBrokerWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("updateBroker", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("insertNewBrokerageFirm", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectBrokerageFirmsAll", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectBrokerUser", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectBrokersAll", getCurrentSessionId());
    	
    	return results;
    }
    
    public static boolean checkStockWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("insertNewStock", getCurrentSessionId());
        results = results && serverInterface.checkPermission("updateStock", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStock", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStockAll", getCurrentSessionId());
    	
    	return results;
    }    
    
    public static boolean checkCustomerWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("insertNewCustomerInfo", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomerInfoAll", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("updateCustomerInfo", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomerInfo", getCurrentSessionId());
    	
    	return results;
    } 
    
    public static boolean checkSellingOrderWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("insertNewOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("updateOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomerInfo", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStock", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomersByFirm", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStockAll", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectOrdersByFirmByType", getCurrentSessionId());
    	
    	return results;
    } 
    
    public static boolean checkBuyingOrderWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("insertNewOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("updateOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomerInfo", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStock", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectOrder", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomerInfo", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectAllStatuses", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectCustomersByFirm", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectStockAll", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("selectOrdersByFirmByType", getCurrentSessionId());
    	
    	return results;
    }   
    
    public static boolean checkTradingSessionWindowPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("startTradingSession", getCurrentSessionId());
    	results = results && serverInterface.checkPermission("endTradingSession", getCurrentSessionId());
    	
    	return results;
    } 
    
    public static boolean checkStockViewPermission() throws RemoteException{
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("selectStock", getCurrentSessionId());
        results = results && serverInterface.checkPermission("selectStockAll", getCurrentSessionId());

    	
    	return results;
    }   
    
    public static boolean checkLogViewPermission() throws RemoteException
    {
    	boolean results = true;
    	
    	results = results && serverInterface.checkPermission("getLoginActivity", getCurrentSessionId());
        results = results && serverInterface.checkPermission("getDatabaseActivity", getCurrentSessionId());
        
        return results;
    } 
    
    public static boolean checkTransactionWindowPermission() throws RemoteException{
        boolean results = true;
        
        results = results && serverInterface.checkPermission("rollBackTransaction", getCurrentSessionId());
        results = results && serverInterface.checkPermission("selectTransactionAll", getCurrentSessionId());

        return results;
    }
    
    public static boolean checkOrderDetailsViewPermission() throws RemoteException{
        boolean results = true;
        
        results = results && serverInterface.checkPermission("selectOrderDetailsByType", getCurrentSessionId());

        return results;
    }
    public static String getAuditLogLoginActivity()
    {
        String logContent = null;
        try
        {
            return serverInterface.getLoginActivity();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            logContent = "Error occurred, cannot retrieve the loging activity log.";
        }
        return logContent;
    }
    
    public static String getAuditLogDbActivity()
    {
        String logContent = null;
        try
        {
            return serverInterface.getDatabaseActivity();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            logContent = "Error occurred, cannot retrieve the Database activity log.";
        }
        return logContent;
    }
}
