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

	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll()
			throws RemoteException;

	public BrokerageFirm selectBrokerageFirm(int idToSelect)
			throws RemoteException;

	public Validator insertNewBrokerageFirm(BrokerageFirm newFirm)
			throws RemoteException;

	public Validator updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate) throws RemoteException;

	public ArrayList<User> selectBrokersAll(int pStatusId)
			throws RemoteException;

	public ArrayList<User> selectBrokersAllbyFirm(int pFirmId)
			throws RemoteException;

	public User selectBrokerUser(int idToSelect) throws RemoteException;

	public Validator insertNewBroker(User newUser) throws RemoteException;

	public Validator updateBroker(int idToUpdate, User user)
			throws RemoteException;

	public ArrayList<Stock> selectStockAll() throws RemoteException;

	public Stock selectStock(int idToSelect) throws RemoteException;

	public Validator insertNewStock(Stock newStock) throws RemoteException;

	public Validator updateStock(int idToUpdate, Stock stock)
			throws RemoteException;

	public ArrayList<Order> selectOrdersAll() throws RemoteException;

        public ArrayList<Order> selectOrdersByFirmByType(int firmId, int orderType) 
                        throws RemoteException;
                
	public Order selectOrder(int idToSelect) throws RemoteException;

	public Validator insertNewOrder(Order newOrder) throws RemoteException;

	public Validator updateOrder(int idToUpdate, Order order)
			throws RemoteException;

	public ArrayList<CustomerInfo> selectCustomerInfoAll()
			throws RemoteException;

        public ArrayList<CustomerInfo> selectCustomersByFirm(int firmId)
			throws RemoteException;
                
	public CustomerInfo selectCustomerInfo(int idToSelect)
			throws RemoteException;

	public Validator insertNewCustomerInfo(CustomerInfo newCustomer)
			throws RemoteException;

	public Validator updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate) throws RemoteException;

	public ArrayList<StatusesOptions> selectAllStatuses()
			throws RemoteException;
        
}
