package RMI;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.mail.Session;

import StockTradingServer.Authoriser;
import StockTradingServer.ServerAuthRes;
import StockTradingServer.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.DatabaseConnector;
import StockTradingServer.Order;
import StockTradingServer.PasswordClassifier;
import StockTradingServer.Sessions;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.User;
import StockTradingServer.Validator;

public class TradingServer extends UnicastRemoteObject implements
		ServerInterface {
	private static final int PORT = 2019;
	public Sessions tradingSessions = new Sessions();
	public Authoriser RefMonitor = new Authoriser();
	
	
	
	// we will be reusing this class throughout all the methors
	// while this TradingServer is alive
	public DatabaseConnector dbCon = new DatabaseConnector();
        public PasswordClassifier passwordClassifier = new PasswordClassifier();
        
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
	public ServerAuthRes selectBrokerageFirmsAll(String clientSessionID) {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, clientSessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectBrokerageFirmsAll());
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	
//	@Override
//	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll(String sessionID) {
//		
//		return this.dbCon.selectBrokerageFirmsAll();
//	}

	@Override
	public ServerAuthRes selectBrokerageFirm(int idToSelect, String sessionID) {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectBrokerageFirm(idToSelect));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes insertNewBrokerageFirm(BrokerageFirm newFirm, String sessionID) {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.insertNewBrokerageFirm(newFirm));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.updateBrokerageFirm(idToUpdate, firmToUpdate));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectBrokersAll(int pStatusId, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectBrokersAll(pStatusId));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectBrokersAllbyFirm(int pFirmId, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectBrokersAllbyFirm(pFirmId));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectBrokerUser(int idToSelect, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectBrokerUser(idToSelect));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes insertNewBroker(User newUser, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.insertNewBroker(newUser));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes updateBroker(int idToUpdate, User user, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.updateBroker(idToUpdate, user));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectStockAll(String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectStockAll());
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectStock(int idToSelect, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectStock(idToSelect));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes insertNewStock(Stock newStock, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.insertNewStock(newStock));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes updateStock(int idToUpdate, Stock stock, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.updateStock(idToUpdate, stock));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectOrdersAll(String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectOrdersAll());
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectOrdersByFirmByType(int firmId, int orderType, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectOrdersByFirmByType(firmId, orderType));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectOrder(int idToSelect, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectOrder(idToSelect));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes insertNewOrder(Order newOrder, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.insertNewOrder(newOrder));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes updateOrder(int idToUpdate, Order order, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.updateOrder(idToUpdate, order));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectCustomerInfoAll(String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectCustomerInfoAll());
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectCustomersByFirm(int firmId, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectCustomersByFirm(firmId));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectCustomerInfo(int idToSelect, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectCustomerInfo(idToSelect));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes insertNewCustomerInfo(CustomerInfo newCustomer, String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.insertNewCustomerInfo(newCustomer));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate, String sessionID) throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.updateCustomerInfo(idToUpdate, customerToUpdate));
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
	}

	@Override
	public ServerAuthRes selectAllStatuses(String sessionID)
			throws RemoteException {
		String action = Thread.currentThread().getStackTrace()[1].getMethodName();
		boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
		ServerAuthRes auth = new ServerAuthRes();
		if (allowed){
			auth.setObject(this.dbCon.selectAllStatuses());
		}else{
			auth.setObject(null);
		}
		auth.setHasAccess(allowed);
		return auth;
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
        
        @Override
        public int GradePassword(String password) throws RemoteException
        {
            return this.passwordClassifier.GradePassword(password);
        }
        
    @Override
    public ServerAuthRes logOut(String sessionID) throws RemoteException{
    	String action = Thread.currentThread().getStackTrace()[1].getMethodName();
    	boolean allowed = RefMonitor.isAllowed(tradingSessions, sessionID, action);
    	ServerAuthRes auth = new ServerAuthRes();
    	if (allowed){
    		auth.setObject(tradingSessions.deleteSession(sessionID));
    	}else{
    		auth.setObject(null);
    	}
    	auth.setHasAccess(allowed);
    	return auth;
    }
    
    @Override
    public boolean checkPermission (String methodName, String clientSessionID) throws RemoteException{
    	return RefMonitor.isAllowed(tradingSessions, clientSessionID, methodName);
    }
    
    
}
