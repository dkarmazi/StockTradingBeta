package RMI;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;

import StockTradingServer.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.DatabaseConnector;
import StockTradingServer.Order;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.User;
import StockTradingServer.Validator;


public class TradingServer extends UnicastRemoteObject implements
		ServerInterface {
	private static final int PORT = 2019;
	// we will be reusing this class throughout all the methors
	// while this TradingServer is alive
	public DatabaseConnector dbCon = new DatabaseConnector();

	private TradingServer() throws Exception {
		super(PORT, new RMISSLClientSocketFactory(),
				new RMISSLServerSocketFactory());
	}

	@Override
	public String sayHello() {
		System.out.println("request from client");
		return "Hello World! using my keys";
	}

	@Override
	public BrokerageFirm getHello() {
		return this.dbCon.getHello();
	}

	@Override
	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll() {
		return this.dbCon.selectBrokerageFirmsAll();
	}

	@Override
	public BrokerageFirm selectBrokerageFirm(int idToSelect) {
		return this.dbCon.selectBrokerageFirm(idToSelect);
	}

	@Override
	public Validator insertNewBrokerageFirm(BrokerageFirm newFirm) {
		return this.dbCon.insertNewBrokerageFirm(newFirm);
	}

	@Override
	public Validator updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate) throws RemoteException {
		return this.dbCon.updateBrokerageFirm(idToUpdate, firmToUpdate);
	}

	@Override
	public ArrayList<User> selectBrokersAll(int pStatusId)
			throws RemoteException {
		return this.dbCon.selectBrokersAll(pStatusId);
	}

	@Override
	public ArrayList<User> selectBrokersAllbyFirm(int pFirmId)
			throws RemoteException {
		return this.dbCon.selectBrokersAllbyFirm(pFirmId);
	}

	@Override
	public User selectBrokerUser(int idToSelect) throws RemoteException {
		return this.dbCon.selectBrokerUser(idToSelect);
	}

	@Override
	public Validator insertNewBroker(User newUser) throws RemoteException {
		return this.dbCon.insertNewBroker(newUser);
	}

	@Override
	public Validator updateBroker(int idToUpdate, User user)
			throws RemoteException {
		return this.dbCon.updateBroker(idToUpdate, user);
	}

	@Override
	public ArrayList<Stock> selectStockAll() throws RemoteException {
		return this.dbCon.selectStockAll();
	}

	@Override
	public Stock selectStock(int idToSelect) throws RemoteException {
		return this.dbCon.selectStock(idToSelect);
	}

	@Override
	public Validator insertNewStock(Stock newStock) throws RemoteException {
		return this.dbCon.insertNewStock(newStock);
	}

	@Override
	public Validator updateStock(int idToUpdate, Stock stock)
			throws RemoteException {
		return this.dbCon.updateStock(idToUpdate, stock);
	}

	@Override
	public ArrayList<Order> selectOrdersAll() throws RemoteException {
		return this.dbCon.selectOrdersAll();
	}
        
        @Override
	public ArrayList<Order> selectOrdersByFirmByType(int firmId, int orderType) 
                        throws RemoteException {
		return this.dbCon.selectOrdersByFirmByType(firmId,  orderType);
	}
	
        @Override
	public Order selectOrder(int idToSelect) throws RemoteException {
		return this.dbCon.selectOrder(idToSelect);
	}

	@Override
	public Validator insertNewOrder(Order newOrder) throws RemoteException {
		return this.dbCon.insertNewOrder(newOrder);
	}

	@Override
	public Validator updateOrder(int idToUpdate, Order order)
			throws RemoteException {
		return this.dbCon.updateOrder(idToUpdate, order);
	}

	@Override
	public ArrayList<CustomerInfo> selectCustomerInfoAll()
			throws RemoteException {
		return this.dbCon.selectCustomerInfoAll();
	}

        @Override
	public ArrayList<CustomerInfo> selectCustomersByFirm(int firmId)
			throws RemoteException {
		return this.dbCon.selectCustomersByFirm(firmId);
	}
        
	@Override
	public CustomerInfo selectCustomerInfo(int idToSelect)
			throws RemoteException {
		return this.dbCon.selectCustomerInfo(idToSelect);
	}

	@Override
	public Validator insertNewCustomerInfo(CustomerInfo newCustomer)
			throws RemoteException {
		return this.dbCon.insertNewCustomerInfo(newCustomer);
	}

	@Override
	public Validator updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate) throws RemoteException {
		return this.dbCon.updateCustomerInfo(idToUpdate, customerToUpdate);
	}

	@Override
	public ArrayList<StatusesOptions> selectAllStatuses()
			throws RemoteException {
		return this.dbCon.selectAllStatuses();
	}

	public static void main(String args[]) {

		// Create and install a security manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			// Create SSL-based registry
			Registry registry = LocateRegistry.createRegistry(PORT,
					new RMISSLClientSocketFactory(),
					new RMISSLServerSocketFactory());

			TradingServer obj = new TradingServer();

			// Bind this object instance to the name "TradingServer"
			registry.bind("TradingServer", obj);

			System.out.println("Trading Server bound in registry");
		} catch (Exception e) {
			System.out.println("TradingServer err: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
