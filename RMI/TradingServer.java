package RMI;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;

import javax.mail.Session;

import StockTradingServer.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.DatabaseConnector;
import StockTradingServer.Order;
import StockTradingServer.Sessions;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.User;
import StockTradingServer.Validator;

public class TradingServer extends UnicastRemoteObject implements
		ServerInterface {
	private static final int PORT = 2019;
	Sessions tradingSessions = new Sessions();
	
	
	
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
	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll(String sessionID) {
		
		return this.dbCon.selectBrokerageFirmsAll();
	}

	@Override
	public BrokerageFirm selectBrokerageFirm(int idToSelect, String sessionID) {
		return this.dbCon.selectBrokerageFirm(idToSelect);
	}

	@Override
	public Validator insertNewBrokerageFirm(BrokerageFirm newFirm, String sessionID) {
		return this.dbCon.insertNewBrokerageFirm(newFirm);
	}

	@Override
	public Validator updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate, String sessionID) throws RemoteException {
		return this.dbCon.updateBrokerageFirm(idToUpdate, firmToUpdate);
	}

	@Override
	public ArrayList<User> selectBrokersAll(int pStatusId, String sessionID)
			throws RemoteException {
		return this.dbCon.selectBrokersAll(pStatusId);
	}

	@Override
	public ArrayList<User> selectBrokersAllbyFirm(int pFirmId, String sessionID)
			throws RemoteException {
		return this.dbCon.selectBrokersAllbyFirm(pFirmId);
	}

	@Override
	public User selectBrokerUser(int idToSelect, String sessionID) throws RemoteException {
		return this.dbCon.selectBrokerUser(idToSelect);
	}

	@Override
	public Validator insertNewBroker(User newUser, String sessionID) throws RemoteException {
		return this.dbCon.insertNewBroker(newUser);
	}

	@Override
	public Validator updateBroker(int idToUpdate, User user, String sessionID)
			throws RemoteException {
		return this.dbCon.updateBroker(idToUpdate, user);
	}

	@Override
	public ArrayList<Stock> selectStockAll(String sessionID) throws RemoteException {
		return this.dbCon.selectStockAll();
	}

	@Override
	public Stock selectStock(int idToSelect, String sessionID) throws RemoteException {
		return this.dbCon.selectStock(idToSelect);
	}

	@Override
	public Validator insertNewStock(Stock newStock, String sessionID) throws RemoteException {
		return this.dbCon.insertNewStock(newStock);
	}

	@Override
	public Validator updateStock(int idToUpdate, Stock stock, String sessionID)
			throws RemoteException {
		return this.dbCon.updateStock(idToUpdate, stock);
	}

	@Override
	public ArrayList<Order> selectOrdersAll(String sessionID) throws RemoteException {
		return this.dbCon.selectOrdersAll();
	}

	@Override
	public ArrayList<Order> selectOrdersByFirmByType(int firmId, int orderType, String sessionID)
			throws RemoteException {
		return this.dbCon.selectOrdersByFirmByType(firmId, orderType);
	}

	@Override
	public Order selectOrder(int idToSelect, String sessionID) throws RemoteException {
		return this.dbCon.selectOrder(idToSelect);
	}

	@Override
	public Validator insertNewOrder(Order newOrder, String sessionID) throws RemoteException {
		return this.dbCon.insertNewOrder(newOrder);
	}

	@Override
	public Validator updateOrder(int idToUpdate, Order order, String sessionID)
			throws RemoteException {
		return this.dbCon.updateOrder(idToUpdate, order);
	}

	@Override
	public ArrayList<CustomerInfo> selectCustomerInfoAll(String sessionID)
			throws RemoteException {
		return this.dbCon.selectCustomerInfoAll();
	}

	@Override
	public ArrayList<CustomerInfo> selectCustomersByFirm(int firmId, String sessionID)
			throws RemoteException {
		return this.dbCon.selectCustomersByFirm(firmId);
	}

	@Override
	public CustomerInfo selectCustomerInfo(int idToSelect, String sessionID)
			throws RemoteException {
		return this.dbCon.selectCustomerInfo(idToSelect);
	}

	@Override
	public Validator insertNewCustomerInfo(CustomerInfo newCustomer, String sessionID)
			throws RemoteException {
		return this.dbCon.insertNewCustomerInfo(newCustomer);
	}

	@Override
	public Validator updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate, String sessionID) throws RemoteException {
		return this.dbCon.updateCustomerInfo(idToUpdate, customerToUpdate);
	}

	@Override
	public ArrayList<StatusesOptions> selectAllStatuses(String sessionID)
			throws RemoteException {
		return this.dbCon.selectAllStatuses();
	}

	@Override
	public Validator checkIfUsernamePasswordMatch(String email, String plainPass)
			throws RemoteException {

		// if logged in, set session
		Validator v = this.dbCon.checkIfUsernamePasswordMatch(email, plainPass);
		if(v.isVerified()) {
			v.setSession(tradingSessions.setSession(email));
		}		
		return v;
	}

	@Override
	public Validator checkIfUsernamePasswordActivationCodeMatch(String email,
			String plainPass, String plainCode) throws RemoteException {
		
		// if logged in, set session
		Validator v = this.dbCon.checkIfUsernamePasswordActivationCodeMatch(email,
				plainPass, plainCode);
		if(v.isVerified()) {
			v.setSession(tradingSessions.setSession(email));
		}		
		return v;

	}

	@Override
	public Validator checkIfUsernameTempPasswordActivationCodeMatch(
			String email, String plainTempPass, String plainCode)
			throws RemoteException {

		// if logged in, set session
		Validator v = this.dbCon.checkIfUsernameTempPasswordActivationCodeMatch(email,
				plainTempPass, plainCode);

		if(v.isVerified()) {
			v.setSession(tradingSessions.setSession(email));
		}		
		return v;

	}

	@Override
	public Validator forgotPassword(String email) throws RemoteException {
		return this.dbCon.forgotPassword(email);
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

	@Override
	public User selectUserByEmailLimited(String emailToSelect)
			throws RemoteException {
		return this.dbCon.selectUserByEmailLimited(emailToSelect);
	}

	@Override
	public boolean isUserPasswordExpired(int UserID) throws RemoteException {
		// TODO Auto-generated method stub
		return this.dbCon.isUserPasswordExpired(UserID);
	}

	@Override
	public boolean passwordHasBeenAlreadyUsed(int userID, String newPassword,
			int numberOfPasswordsToLookUp) throws RemoteException {
		return this.dbCon.passwordHasBeenAlreadyUsed(userID, newPassword, numberOfPasswordsToLookUp);
	}

	@Override
	public boolean isFirstLoginEver(int UserID) throws RemoteException {
		return this.dbCon.isFirstLoginEver(UserID);
	}

	@Override
	public boolean isUserExists(String Email) throws RemoteException {
		return this.dbCon.isUserExists(Email);
	}

	@Override
	public Validator updateUserPassword(int userId, String plainPass,
			String plainPassConfirm) throws RemoteException {
		return this.dbCon.updateUserPassword(userId, plainPass, plainPassConfirm);
	}

}
