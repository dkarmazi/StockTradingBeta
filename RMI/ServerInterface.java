package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;

import StockTradingServer.ServerAuthRes;
import StockTradingServer.BrokerageFirm;
import StockTradingServer.CustomerInfo;
import StockTradingServer.Order;
import StockTradingServer.StatusesOptions;
import StockTradingServer.Stock;
import StockTradingServer.TradingSession;
import StockTradingServer.User;
import StockTradingServer.UserAdmin;
import StockTradingServer.Validator;

public interface ServerInterface extends Remote {

	String sayHello() throws RemoteException;

	
	public BrokerageFirm getHello() throws RemoteException;

	
	public User selectUserByEmailLimited(String emailToSelect) throws RemoteException;

//	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll(String clientSessionID)
//			throws RemoteException;

        public ServerAuthRes selectAdministratorsAll(int pStatusId, String clientSessionID)
			throws RemoteException;
        
        public ServerAuthRes selectAdminUser(int idToSelect, String clientSessionID) 
                        throws RemoteException;
        
        public ServerAuthRes insertNewAdmin(UserAdmin newUser, String sessionID) 
                        throws RemoteException;
        
        public ServerAuthRes updateAdmin(int idToUpdate, UserAdmin user, String sessionID)
			throws RemoteException;
        

	public ServerAuthRes selectBrokerageFirmsAll(String clientSessionID) throws RemoteException;
	
	public ServerAuthRes selectBrokerageFirmsByStatus(int status, String clientSessionID) throws RemoteException;
        
	public ServerAuthRes selectBrokerageFirm(int idToSelect, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes insertNewBrokerageFirm(BrokerageFirm newFirm, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate, String clientSessionID) throws RemoteException;

	public ServerAuthRes selectBrokersAll(int pStatusId, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectBrokersAllbyFirm(int pFirmId, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectBrokerUser(int idToSelect, String clientSessionID) throws RemoteException;

	public ServerAuthRes insertNewBroker(User newUser, String clientSessionID) throws RemoteException;

	public ServerAuthRes updateBroker(int idToUpdate, User user, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectStockAll(int pStatusId, String clientSessionID) throws RemoteException;

	public ServerAuthRes selectStock(int idToSelect, String clientSessionID) throws RemoteException;

	public ServerAuthRes insertNewStock(Stock newStock, String clientSessionID) throws RemoteException;

	public ServerAuthRes updateStock(int idToUpdate, Stock stock, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectOrdersAll(String clientSessionID) throws RemoteException;

        public ServerAuthRes selectOrdersByFirmByType(int firmId, int orderType, String clientSessionID) 
                        throws RemoteException;
                
	public ServerAuthRes selectOrder(int idToSelect, String clientSessionID) throws RemoteException;

	public ServerAuthRes insertNewOrder(Order newOrder, String clientSessionID) throws RemoteException;

	public ServerAuthRes updateOrder(int idToUpdate, Order order, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectCustomerInfoAll(String clientSessionID)
			throws RemoteException;

	public ServerAuthRes selectCustomersByFirm(int firmId, String clientSessionID)
			throws RemoteException;
                
	public ServerAuthRes selectCustomerInfo(int idToSelect, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes insertNewCustomerInfo(CustomerInfo newCustomer, String clientSessionID)
			throws RemoteException;

	public ServerAuthRes updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate, String clientSessionID) throws RemoteException;

	public ServerAuthRes selectAllStatuses(String clientSessionID)
			throws RemoteException;
	
	
	public Validator checkIfUsernamePasswordMatch(String email, String plainPass)
			throws RemoteException;

	public Validator checkIfUsernamePasswordActivationCodeMatch(String email, String plainPass, String plainCode) 
			throws RemoteException;

	public Validator checkIfUsernameTempPasswordActivationCodeMatch(String email, String plainTempPass, String plainCode) 
			throws RemoteException;

	public Validator forgotPassword(String email) throws RemoteException;

	public boolean isUserPasswordExpired(int UserID) throws RemoteException;

	public boolean passwordHasBeenAlreadyUsed(int userID, String newPassword,
			int numberOfPasswordsToLookUp) throws RemoteException;

	public boolean isFirstLoginEver(int UserID) throws RemoteException;

	public boolean isUserExists(String Email) throws RemoteException;

	public Validator updateUserPassword(int userId, String plainPass,
			String plainPassConfirm) throws RemoteException;
        
	public int GradePassword(String password) throws RemoteException;
	
	public ServerAuthRes logOut (String sessionID) throws RemoteException;

	public boolean checkPermission (String methodName, String clientSessionID) throws RemoteException;
        
        public String getLoginActivity() throws RemoteException;

        public String getDatabaseActivity() throws RemoteException;

        public ServerAuthRes startTradingSession (TradingSession TS, String clientSessionID) throws RemoteException;
	
	public ServerAuthRes endTradingSession (String clientSessionID) throws RemoteException;
	
	public ServerAuthRes isThereActiveTradingSession (String clientSessionID) throws RemoteException;
	
	public ServerAuthRes getTradingSessionInfo (String clientSessionID) throws RemoteException;
	
	public ServerAuthRes selectTransactionAll(String clientSessionID) throws RemoteException;
	
	public ServerAuthRes rollBackTransaction(int transID, String clientSessionID) throws RemoteException;
}
