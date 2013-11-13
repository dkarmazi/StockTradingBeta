package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;

import StockTradingServer.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.Order;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.User;
import StockTradingServer.Validator;

public interface ServerInterface extends Remote {

	String sayHello() throws RemoteException;

	
	public BrokerageFirm getHello() throws RemoteException;

	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll(String clientSessionID)
			throws RemoteException;

	public BrokerageFirm selectBrokerageFirm(int idToSelect, String clientSessionID)
			throws RemoteException;

	public Validator insertNewBrokerageFirm(BrokerageFirm newFirm, String clientSessionID)
			throws RemoteException;

	public Validator updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate, String clientSessionID) throws RemoteException;

	public ArrayList<User> selectBrokersAll(int pStatusId, String clientSessionID)
			throws RemoteException;

	public ArrayList<User> selectBrokersAllbyFirm(int pFirmId, String clientSessionID)
			throws RemoteException;

	public User selectBrokerUser(int idToSelect, String clientSessionID) throws RemoteException;

	public Validator insertNewBroker(User newUser, String clientSessionID) throws RemoteException;

	public Validator updateBroker(int idToUpdate, User user, String clientSessionID)
			throws RemoteException;

	public ArrayList<Stock> selectStockAll(String clientSessionID) throws RemoteException;

	public Stock selectStock(int idToSelect, String clientSessionID) throws RemoteException;

	public Validator insertNewStock(Stock newStock, String clientSessionID) throws RemoteException;

	public Validator updateStock(int idToUpdate, Stock stock, String clientSessionID)
			throws RemoteException;

	public ArrayList<Order> selectOrdersAll(String clientSessionID) throws RemoteException;

        public ArrayList<Order> selectOrdersByFirmByType(int firmId, int orderType, String clientSessionID) 
                        throws RemoteException;
                
	public Order selectOrder(int idToSelect, String clientSessionID) throws RemoteException;

	public Validator insertNewOrder(Order newOrder, String clientSessionID) throws RemoteException;

	public Validator updateOrder(int idToUpdate, Order order, String clientSessionID)
			throws RemoteException;

	public ArrayList<CustomerInfo> selectCustomerInfoAll(String clientSessionID)
			throws RemoteException;

	public ArrayList<CustomerInfo> selectCustomersByFirm(int firmId, String clientSessionID)
			throws RemoteException;
                
	public CustomerInfo selectCustomerInfo(int idToSelect, String clientSessionID)
			throws RemoteException;

	public Validator insertNewCustomerInfo(CustomerInfo newCustomer, String clientSessionID)
			throws RemoteException;

	public Validator updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate, String clientSessionID) throws RemoteException;

	public ArrayList<StatusesOptions> selectAllStatuses(String clientSessionID)
			throws RemoteException;
	
	
	public Validator checkIfUsernamePasswordMatch(String email, String plainPass)
			throws RemoteException;

	public Validator checkIfUsernamePasswordActivationCodeMatch(String email, String plainPass, String plainCode) 
			throws RemoteException;

	public Validator checkIfUsernameTempPasswordActivationCodeMatch(String email, String plainTempPass, String plainCode) 
			throws RemoteException;

	public Validator forgotPassword(String email) throws RemoteException;
        
}
