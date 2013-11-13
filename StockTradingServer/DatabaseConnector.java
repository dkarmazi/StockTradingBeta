package StockTradingServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import StockTradingCommon.Enumeration;

import java.text.DecimalFormat;
import java.util.HashSet;

/*
 * Dmitriy Karmazin, Ahmad Kouraiem
 * CSCI-6545
 */

public class DatabaseConnector {
	private Connection con = null;

	// connect to DB
	public DatabaseConnector() {
		// Basic configuration - to be moved to config file
		String url = "jdbc:mysql://192.30.164.204:3306/repo6545";
		String user = "repo6545";
		String password = "MF4@2163G!8d2L4";
		try {

			Connection con = DriverManager.getConnection(url, user, password);
			setCon(con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public BrokerageFirm getHello() {

		BrokerageFirm brokerageFirm = new BrokerageFirm();

		try {

			PreparedStatement st = null;
			String query = "SELECT * FROM BROKERAGE_FIRM_INFO WHERE ID = ?;";

			st = this.con.prepareStatement(query);
			st.setInt(1, 12);

			ResultSet res = st.executeQuery();

			res.next();

			int id = res.getInt(1);
			String name = res.getString(2);
			String addressStreet = res.getString(3);
			String addressCity = res.getString(4);
			String addressState = res.getString(5);
			String addressZip = res.getString(6);
			String licenceNumber = res.getString(7);
			int status = res.getInt(8);

			brokerageFirm.setId(id);
			brokerageFirm.setName(name);
			brokerageFirm.setAddressStreet(addressStreet);
			brokerageFirm.setAddressCity(addressCity);
			brokerageFirm.setAddressState(addressState);
			brokerageFirm.setAddressZip(addressZip);
			brokerageFirm.setLicenceNumber(licenceNumber);
			brokerageFirm.setStatus(status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return brokerageFirm;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	/*
	 * This function returns an array list of the brokerage firms
	 */
	public ArrayList<BrokerageFirm> selectBrokerageFirmsAll() {
		ArrayList<BrokerageFirm> brokerageFirms = new ArrayList<BrokerageFirm>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM BROKERAGE_FIRM_INFO;";

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {

				int id = res.getInt(1);
				String name = res.getString(2);
				String addressStreet = res.getString(3);
				String addressCity = res.getString(4);
				String addressState = res.getString(5);
				String addressZip = res.getString(6);
				String licenceNumber = res.getString(7);
				int status = res.getInt(8);

				BrokerageFirm brokerageFirm = new BrokerageFirm();
				brokerageFirm.setId(id);
				brokerageFirm.setName(name);
				brokerageFirm.setAddressStreet(addressStreet);
				brokerageFirm.setAddressCity(addressCity);
				brokerageFirm.setAddressState(addressState);
				brokerageFirm.setAddressZip(addressZip);
				brokerageFirm.setLicenceNumber(licenceNumber);
				brokerageFirm.setStatus(status);

				brokerageFirms.add(brokerageFirm);

			}
		} catch (SQLException ex) {
			// Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			// lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return brokerageFirms;
	}

	/*
	 * This function returns a single brokerage firm based on a given id MySQL
	 * injection protection
	 */
	public BrokerageFirm selectBrokerageFirm(int idToSelect) {
		BrokerageFirm brokerageFirm = new BrokerageFirm();
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM BROKERAGE_FIRM_INFO WHERE ID = ?;";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, idToSelect);

			ResultSet res = st.executeQuery();

			res.next();

			int id = res.getInt(1);
			String name = res.getString(2);
			String addressStreet = res.getString(3);
			String addressCity = res.getString(4);
			String addressState = res.getString(5);
			String addressZip = res.getString(6);
			String licenceNumber = res.getString(7);
			int status = res.getInt(8);
			String supervisorEmail = res.getString(9);

			brokerageFirm.setId(id);
			brokerageFirm.setName(name);
			brokerageFirm.setAddressStreet(addressStreet);
			brokerageFirm.setAddressCity(addressCity);
			brokerageFirm.setAddressState(addressState);
			brokerageFirm.setAddressZip(addressZip);
			brokerageFirm.setLicenceNumber(licenceNumber);
			brokerageFirm.setStatus(status);
			brokerageFirm.setSupervisorEmail(supervisorEmail);

		} catch (SQLException ex) {
			// Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			// lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return brokerageFirm;
	}

	/*
	 * This function adds a new brokerage firm to the database from the given
	 * class instance MySQL injection checked
	 */
	public Validator insertNewBrokerageFirm(BrokerageFirm newFirm) {
		// validate input
		Validator v = newFirm.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "INSERT INTO BROKERAGE_FIRM_INFO "
				+ " (NAME, ADDRESSSTREET, ADDRESSCITY, ADDRESSSTATE, ADDRESSZIP, LICENCENUMBER, STATUSID, SUPER_EMAIL)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newFirm.getName());
			st.setString(2, newFirm.getAddressStreet());
			st.setString(3, newFirm.getAddressCity());
			st.setString(4, newFirm.getAddressState());
			st.setString(5, newFirm.getAddressZip());
			st.setString(6, newFirm.getLicenceNumber());
			st.setInt(7, newFirm.getStatus());
			st.setString(8, newFirm.getSupervisorEmail());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

			// rs = st.getGeneratedKeys();
			// if (rs.next()) {
			// System.out.println(rs.getLong(1));
			// } else {
			// throw new SQLException(
			// "Creating user failed, no generated key obtained.");
			// }

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This function updates a specified brokerage firm with provided
	 * information from the brokerage class
	 */
	public Validator updateBrokerageFirm(int idToUpdate,
			BrokerageFirm firmToUpdate) {
		// validate input
		Validator v = firmToUpdate.validate();
		if (!v.isVerified()) {
			return v;
		}

		InputValidation iv = new InputValidation();
		Validator v2 = iv.validateIntGeneral(idToUpdate, "idToUpdate");

		if (!v2.isVerified()) {
			return v2;
		}

		PreparedStatement st = null;

		String query = "UPDATE BROKERAGE_FIRM_INFO " + " SET " + " NAME = ?"
				+ ", ADDRESSSTREET = ?" + ", ADDRESSCITY = ?"
				+ ", ADDRESSSTATE = ?" + ", ADDRESSZIP = ?"
				+ ", LICENCENUMBER = ?" + ", STATUSID = ? "
				+ ", SUPER_EMAIL = ? " + " WHERE ID = ?;";

		try {

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, firmToUpdate.getName());
			st.setString(2, firmToUpdate.getAddressStreet());
			st.setString(3, firmToUpdate.getAddressCity());
			st.setString(4, firmToUpdate.getAddressState());
			st.setString(5, firmToUpdate.getAddressZip());
			st.setString(6, firmToUpdate.getLicenceNumber());
			st.setInt(7, firmToUpdate.getStatus());
			st.setString(8, firmToUpdate.getSupervisorEmail());
			st.setInt(9, idToUpdate);

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Update failed");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return v;
	}

	/*
	 * This function returns all broker users 0 - all 1,2 - with certain status
	 */
	public ArrayList<User> selectBrokersAll(int pStatusId) {
		ArrayList<User> usersAll = new ArrayList<User>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM USERS WHERE ROLEID = 2";

		if (pStatusId != Enumeration.Broker.BROKER_SELECT_PARAM_EMPTY) {
			query += " AND STATUSID = \"" + pStatusId + "\"";
		}

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {

				int id = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				byte[] ssn = res.getBytes(5);
				String password = res.getString(6);
				String salt = res.getString(7);
				int roleId = res.getInt(8);
				int statusId = res.getInt(9);
				int brokerFirmId = res.getInt(10);

				// Decrypt sensitive data
				String iv = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_IV;
				String key = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_KEY;

				DataEncryptor de = new DataEncryptor();
				de.setIV(iv);

				String decryptedSsn = "";
				try {
					decryptedSsn = de.decrypt(ssn, key);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					decryptedSsn = "err";
				}
				// End sensitive data decryption

				User user = new User();
				user.setId(id);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setSsn(decryptedSsn);
				user.setPassword(password);
				user.setSalt(salt);
				user.setRoleId(roleId);
				user.setStatusId(statusId);
				user.setBrokerFirmId(brokerFirmId);

				usersAll.add(user);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return usersAll;
	}

	/*
	 * This function returns all broker users for a given firm 0 - all 1,2 -
	 * with certain status
	 */
	public ArrayList<User> selectBrokersAllbyFirm(int pFirmId) {
		ArrayList<User> usersAll = new ArrayList<User>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM USERS WHERE ROLEID = 2";

		if (pFirmId != Enumeration.Broker.BROKER_SELECT_PARAM_EMPTY) {
			query += " AND FIRMID = \"" + pFirmId + "\"";
		}

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {

				int id = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				byte[] ssn = res.getBytes(5);
				String password = res.getString(6);
				String salt = res.getString(7);
				int roleId = res.getInt(8);
				int statusId = res.getInt(9);
				int brokerFirmId = res.getInt(10);

				// Decrypt sensitive data
				String iv = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_IV;
				String key = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_KEY;

				DataEncryptor de = new DataEncryptor();
				de.setIV(iv);

				String decryptedSsn = "";
				try {
					decryptedSsn = de.decrypt(ssn, key);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					decryptedSsn = "err";
				}
				// End sensitive data decryption

				User user = new User();
				user.setId(id);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setSsn(decryptedSsn);
				user.setPassword(password);
				user.setSalt(salt);
				user.setRoleId(roleId);
				user.setStatusId(statusId);
				user.setBrokerFirmId(brokerFirmId);

				usersAll.add(user);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return usersAll;
	}

	/*
	 * This function returns a broker for a given userid
	 */
	public User selectBrokerUser(int idToSelect) {
		User user = new User();

		PreparedStatement st = null;
		String query = "SELECT * FROM USERS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, idToSelect);

			ResultSet res = st.executeQuery();

			res.next();

			int id = res.getInt(1);
			String firstName = res.getString(2);
			String lastName = res.getString(3);
			String email = res.getString(4);
			byte[] ssn = res.getBytes(5);
			String password = res.getString(6);
			String salt = res.getString(7);
			int roleId = res.getInt(8);
			int statusId = res.getInt(9);
			int brokerFirmId = res.getInt(10);

			// Decrypt sensitive data
			String iv = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_IV;
			String key = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_KEY;

			DataEncryptor de = new DataEncryptor();
			de.setIV(iv);

			String decryptedSsn = "";
			try {
				decryptedSsn = de.decrypt(ssn, key);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				decryptedSsn = "err";
			}
			// End sensitive data decryption

			user.setId(id);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setSsn(decryptedSsn);
			user.setPassword(password);
			user.setSalt(salt);
			user.setRoleId(roleId);
			user.setStatusId(statusId);
			user.setBrokerFirmId(brokerFirmId);

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return user;
	}

	/*
	 * This function inserts a new broker MySQL injection checked
	 */
	public Validator insertNewBroker(User newUser) {
		// validate input
		Validator v = newUser.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "INSERT INTO USERS (FIRSTNAME, LASTNAME, EMAIL, SSN, PASSWORD, SALT, ROLEID, STATUSID, FIRMID) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

		try {

			// Password hashing
			PasswordHasher ph = new PasswordHasher();
			String salt = ph.generateSalt();
			String passwordHashed = ph.sha512(newUser.getPassword(), salt);
			// end password hashing

			// Sensitive data encryption
			String iv = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_IV;
			String key = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_KEY;
			DataEncryptor de = new DataEncryptor();
			de.setIV(iv);

			byte[] ssnCipher = null;

			try {
				ssnCipher = de.encrypt(newUser.getSsn(), key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// end encryption

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newUser.getFirstName());
			st.setString(2, newUser.getLastName());
			st.setString(3, newUser.getEmail());
			st.setBytes(4, ssnCipher);
			st.setString(5, passwordHashed);
			st.setString(6, salt);
			st.setInt(7, newUser.getRoleId());
			st.setInt(8, newUser.getStatusId());
			st.setInt(9, newUser.getBrokerFirmId());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

			rs = st.getGeneratedKeys();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	public Validator updateBroker(int idToUpdate, User user) {
		// validate input
		Validator v = user.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "UPDATE USERS SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, SSN = ?, PASSWORD = ?, SALT = ?, ROLEID = ?, STATUSID = ?, FIRMID = ? WHERE ID = ?";

		try {

			// Password hashing
			PasswordHasher ph = new PasswordHasher();
			String salt = ph.generateSalt();
			String passwordHashed = ph.sha512(user.getPassword(), salt);
			// end password hashing

			// Sensitive data encryption
			String iv = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_IV;
			String key = StockTradingCommon.Enumeration.Broker.BROKER_ENCRYPT_KEY;
			DataEncryptor de = new DataEncryptor();
			de.setIV(iv);

			byte[] ssnCipher = null;

			try {
				ssnCipher = de.encrypt(user.getSsn(), key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// end encryption

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setBytes(4, ssnCipher);
			st.setString(5, passwordHashed);
			st.setString(6, salt);
			st.setInt(7, user.getRoleId());
			st.setInt(8, user.getStatusId());
			st.setInt(9, user.getBrokerFirmId());
			st.setInt(10, idToUpdate);

			int affectedRows = st.executeUpdate();

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This function returns an arraylist of all the active stocks in the system
	 */
	public ArrayList<Stock> selectStockAll() {
		ArrayList<Stock> stocksAll = new ArrayList<Stock>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM STOCKS WHERE STATUSID = 1;";

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {

				int id = res.getInt(1);
				String name = res.getString(2);
				int amount = res.getInt(3);
				double price = res.getDouble(4);
				int statusId = res.getInt(5);

				Stock stock = new Stock();
				stock.setId(id);
				stock.setName(name);
				stock.setAmount(amount);
				stock.setPrice(price);
				stock.setStatusId(statusId);

				stocksAll.add(stock);

			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stocksAll;
	}

	/*
	 * This method returns a stock given a stock id
	 */
	public Stock selectStock(int idToSelect) {
		Stock stock = new Stock();

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM STOCKS WHERE id = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, idToSelect);

			ResultSet res = st.executeQuery();

			res.next();

			int id = res.getInt(1);
			String name = res.getString(2);
			int amount = res.getInt(3);
			double price = res.getDouble(4);
			int statusId = res.getInt(5);

			stock.setId(id);
			stock.setName(name);
			stock.setAmount(amount);
			stock.setPrice(price);
			stock.setStatusId(statusId);

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stock;
	}

	/*
	 * This function inserts a new stock into the database MySQL injection
	 * checked
	 */
	public Validator insertNewStock(Stock newStock) {
		Validator v = newStock.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "INSERT INTO STOCKS (NAME, AMOUNT, PRICE, STATUSID) VALUES (?, ?, ?, ?)";

		try {

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newStock.getName());
			st.setInt(2, newStock.getAmount());
			st.setDouble(3, newStock.getPrice());
			st.setInt(4, newStock.getStatusId());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This function updates a stock in the database MySQL injections checked
	 */
	public Validator updateStock(int idToUpdate, Stock stock) {
		Validator v = stock.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;

		String query = "UPDATE STOCKS SET NAME = ?, AMOUNT = ?, PRICE = ?, STATUSID = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, stock.getName());
			st.setInt(2, stock.getAmount());
			st.setDouble(3, stock.getPrice());
			st.setInt(4, stock.getStatusId());
			st.setInt(5, idToUpdate);

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This functions returns an array list of all the orders
	 */
	public ArrayList<Order> selectOrdersAll() {
		ArrayList<Order> ordersAll = new ArrayList<Order>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM ORDERS WHERE STATUSID = 1";

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {
				int orderId = res.getInt(1);
				int typeId = res.getInt(2);
				int brokerId = res.getInt(3);
				int customerId = res.getInt(4);
				int stockId = res.getInt(5);
				int amount = res.getInt(6);
				double price = res.getDouble(7);
				Timestamp dateIssued = res.getTimestamp(8);
				Timestamp dateExpiration = res.getTimestamp(9);
				int statusId = res.getInt(10);

				Order order = new Order();
				order.setOrderId(orderId);
				order.setTypeId(typeId);
				order.setBrokerId(brokerId);
				order.setCustomerId(customerId);
				order.setStockId(stockId);
				order.setAmount(amount);
				order.setPrice(price);
				order.setDateIssued(dateIssued);
				order.setDateExpiration(dateExpiration);
				order.setStatusId(statusId);

				ordersAll.add(order);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return ordersAll;
	}

	/*
	 * This functions returns an array list of all the orders belongs to a
	 * particular firm
	 */
	public ArrayList<Order> selectOrdersByFirmByType(int firmId, int orderType) {
		ArrayList<Order> ordersAll = new ArrayList<Order>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT O.*, S.NAME, C.FIRSTNAME, C.LASTNAME "
				+ " FROM ORDERS O " + " INNER JOIN HAS_FIRM_BROKERS HFB"
				+ " ON (O.BROKERID = HFB.BROKERID)" + " INNER JOIN STOCKS S"
				+ " ON (O.STOCKID = S.ID)" + " INNER JOIN CUSTOMER_INFO C"
				+ " ON (O.CUSTOMERID = C.ID)" + " WHERE HFB.FIRMID = " + firmId
				// + " AND O.STATUSID = 1"
				+ " AND O.TYPEID = " + orderType;

		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {
				int orderId = res.getInt(1);
				int typeId = res.getInt(2);
				int brokerId = res.getInt(3);
				int customerId = res.getInt(4);
				int stockId = res.getInt(5);
				int amount = res.getInt(6);
				double price = res.getDouble(7);
				Timestamp dateIssued = res.getTimestamp(8);
				Timestamp dateExpiration = res.getTimestamp(9);
				int statusId = res.getInt(10);
				String stockName = res.getString("NAME");
				String customer = res.getString("FIRSTNAME") + " "
						+ res.getString("LASTNAME");
				String displaySummary = stockName + "["
						+ new DecimalFormat("#,##0").format(amount) + " @ "
						+ new DecimalFormat("#,##0.00").format(price) + "] ::"
						+ customer;

				Order order = new Order();
				order.setOrderId(orderId);
				order.setTypeId(typeId);
				order.setBrokerId(brokerId);
				order.setCustomerId(customerId);
				order.setStockId(stockId);
				order.setAmount(amount);
				order.setPrice(price);
				order.setDateIssued(dateIssued);
				order.setDateExpiration(dateExpiration);
				order.setStatusId(statusId);
				order.setDisplaySummary(displaySummary);

				ordersAll.add(order);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return ordersAll;
	}

	/*
	 * This function returns a particular order
	 */
	public Order selectOrder(int idToSelect) {
		Order order = new Order();

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM ORDERS WHERE ORDERID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, idToSelect);
			ResultSet res = st.executeQuery();

			res.next();

			int orderId = res.getInt(1);
			int typeId = res.getInt(2);
			int brokerId = res.getInt(3);
			int customerId = res.getInt(4);
			int stockId = res.getInt(5);
			int amount = res.getInt(6);
			double price = res.getDouble(7);
			Timestamp dateIssued = res.getTimestamp(8);
			Timestamp dateExpiration = res.getTimestamp(9);
			int statusId = res.getInt(10);

			order.setOrderId(orderId);
			order.setTypeId(typeId);
			order.setBrokerId(brokerId);
			order.setCustomerId(customerId);
			order.setStockId(stockId);
			order.setAmount(amount);
			order.setPrice(price);
			order.setDateIssued(dateIssued);
			order.setDateExpiration(dateExpiration);
			order.setStatusId(statusId);

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return order;
	}

	/*
	 * This function inserts a new order to the database
	 */
	public Validator insertNewOrder(Order newOrder) {
		Validator v = newOrder.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "INSERT INTO ORDERS (TYPEID, BROKERID, CUSTOMERID, STOCKID, AMOUNT, PRICE, DATEISSUED, DATEEXPIRATION, STATUSID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, newOrder.getTypeId());
			st.setInt(2, newOrder.getBrokerId());
			st.setInt(3, newOrder.getCustomerId());
			st.setInt(4, newOrder.getStockId());
			st.setInt(5, newOrder.getAmount());
			st.setDouble(6, newOrder.getPrice());
			st.setTimestamp(7, newOrder.getDateIssued());
			st.setTimestamp(8, newOrder.getDateExpiration());
			st.setInt(9, newOrder.getStatusId());

			System.out.println(st.toString());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This function updates an order
	 */
	public Validator updateOrder(int idToUpdate, Order order) {
		Validator v = order.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "UPDATE ORDERS SET TYPEID = ?, BROKERID = ?, CUSTOMERID = ?, STOCKID = ?, AMOUNT = ?, PRICE = ?, DATEISSUED = ?, DATEEXPIRATION = ?, STATUSID = ? WHERE ORDERID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, order.getTypeId());
			st.setInt(2, order.getBrokerId());
			st.setInt(3, order.getCustomerId());
			st.setInt(4, order.getStockId());
			st.setInt(5, order.getAmount());
			st.setDouble(6, order.getPrice());
			st.setTimestamp(7, order.getDateIssued());
			st.setTimestamp(8, order.getDateExpiration());
			st.setInt(9, order.getStatusId());
			st.setInt(10, idToUpdate);

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	/*
	 * This function returns an array list of all active customers of a given
	 * brokerage firm
	 */
	public ArrayList<CustomerInfo> selectCustomersByFirm(int firmId) {
		ArrayList<CustomerInfo> customersOfFirm = new ArrayList<CustomerInfo>();
		Statement st = null;
		// ResultSet rs = null;

		String query = "SELECT * FROM CUSTOMER_INFO C ";
		query += " INNER JOIN HAS_FIRM_CUSTOMERS HFC";
		query += " ON (HFC.CUSTOMERID = C.ID)";
		query += " WHERE HFC.FIRMID = " + firmId;
		query += ";";
		try {
			st = this.con.createStatement();
			ResultSet res = st.executeQuery(query);

			while (res.next()) {

				int id = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				String phone = res.getString(5);
				int statusId = res.getInt(6);

				CustomerInfo customer = new CustomerInfo();
				customer.setId(id);
				customer.setFirstName(firstName);
				customer.setLastName(lastName);
				customer.setEmail(email);
				customer.setPhone(phone);
				customer.setStatusId(statusId);

				customersOfFirm.add(customer);

			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return customersOfFirm;
	}

	/*
	 * This function returns an array list of all active customers
	 */
	public ArrayList<CustomerInfo> selectCustomerInfoAll() {
		ArrayList<CustomerInfo> customerInfoAll = new ArrayList<CustomerInfo>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM CUSTOMER_INFO WHERE STATUSID = 1;";

		// try {
		// st = this.con.createStatement();
		// ResultSet res = st.executeQuery(query);
		//
		// while (res.next()) {
		//
		// int id = res.getInt(1);
		// String firstName = res.getString(2);
		// String lastName = res.getString(3);
		// String email = res.getString(4);
		// String phone = res.getString(5);
		// int statusId = res.getInt(6);
		//
		// CustomerInfo customer = new CustomerInfo();
		// customer.setId(id);
		// customer.setFirstName(firstName);
		// customer.setLastName(lastName);
		// customer.setEmail(email);
		// customer.setPhone(phone);
		// customer.setStatusId(statusId);
		//
		// customerInfoAll.add(customer);
		//
		// }
		// } catch (SQLException ex) {
		// Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
		// lgr.log(Level.WARNING, ex.getMessage(), ex);
		// }

		return customerInfoAll;
	}

	// /*
	// * This function returns an array list of all active customers
	// */
	// public ArrayList<CustomerInfo> selectCustomerInfoByFirm(int firmId) {
	// ArrayList<CustomerInfo> customerInfoAll = new ArrayList<CustomerInfo>();
	// Statement st = null;
	// ResultSet rs = null;
	// String query = "";
	//
	// try {
	// st = this.con.createStatement();
	// ResultSet res = st.executeQuery(query);
	//
	// while (res.next()) {
	//
	// int id = res.getInt(1);
	// String firstName = res.getString(2);
	// String lastName = res.getString(3);
	// String email = res.getString(4);
	// String phone = res.getString(5);
	// int statusId = res.getInt(6);
	//
	// CustomerInfo customer = new CustomerInfo();
	// customer.setId(id);
	// customer.setFirstName(firstName);
	// customer.setLastName(lastName);
	// customer.setEmail(email);
	// customer.setPhone(phone);
	// customer.setStatusId(statusId);
	//
	// customerInfoAll.add(customer);
	//
	// }
	// } catch (SQLException ex) {
	// Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
	// lgr.log(Level.WARNING, ex.getMessage(), ex);
	// }
	//
	// return customerInfoAll;
	// }

	public CustomerInfo selectCustomerInfo(int idToSelect) {
		CustomerInfo customer = new CustomerInfo();

		PreparedStatement st = null;
		String query = "SELECT * FROM CUSTOMER_INFO WHERE id = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, idToSelect);

			ResultSet res = st.executeQuery();

			res.next();

			int id = res.getInt(1);
			String firstName = res.getString(2);
			String lastName = res.getString(3);
			String email = res.getString(4);
			String phone = res.getString(5);
			int statusId = res.getInt(6);

			customer.setId(id);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setEmail(email);
			customer.setPhone(phone);
			customer.setStatusId(statusId);
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return customer;
	}

	public Validator insertNewCustomerInfo(CustomerInfo newCustomer) {
		Validator v = newCustomer.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "INSERT INTO CUSTOMER_INFO (FIRSTNAME, LASTNAME, EMAIL, PHONE, STATUSID) VALUES (?, ?, ?, ?, ?)";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newCustomer.getFirstName());
			st.setString(2, newCustomer.getLastName());
			st.setString(3, newCustomer.getEmail());
			st.setString(4, newCustomer.getPhone());
			st.setInt(5, newCustomer.getStatusId());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

			rs = st.getGeneratedKeys();
			rs.next();
			rs.getLong(1);
			// System.out.println(rs.getLong(1));

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	public Validator updateCustomerInfo(int idToUpdate,
			CustomerInfo customerToUpdate) {
		Validator v = customerToUpdate.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "UPDATE CUSTOMER_INFO SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PHONE = ?, STATUSID = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, customerToUpdate.getFirstName());
			st.setString(2, customerToUpdate.getLastName());
			st.setString(3, customerToUpdate.getEmail());
			st.setString(4, customerToUpdate.getPhone());
			st.setInt(5, customerToUpdate.getStatusId());
			st.setInt(6, idToUpdate);

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Success");

		return v;
	}

	public ArrayList<StatusesOptions> selectAllStatuses() {

		ArrayList<StatusesOptions> statusesList = new ArrayList<StatusesOptions>();

		Connection con = this.con;
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM DIC_STATUSES ORDER BY PRIORITY;";

		try {
			st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			while (res.next()) {

				int id = res.getInt(1);
				String name = res.getString(2);

				StatusesOptions status = new StatusesOptions();
				status.setId(id);
				status.setName(name);

				statusesList.add(status);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return statusesList;

	}

	/*
	 * This function returns a limited user information for a given email
	 */
	public User selectUserByEmailLimited(String emailToSelect) {
		User user = new User();

		PreparedStatement st = null;

		String query = "SELECT PASSWORD, SALT, STATUSID, FALSELOGINS, ID, ROLEID, ACTIVATIONCODE, ACTIVATIONCODESALT, TEMPPASSWORD, TEMPPASSWORDSALT FROM USERS WHERE EMAIL = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setString(1, emailToSelect);

			ResultSet res = st.executeQuery();

			if (res.next()) {
				String password = res.getString(1);
				String salt = res.getString(2);
				int statusId = res.getInt(3);
				int falseLogins = res.getInt(4);
				int id = res.getInt(5);
				int roleId = res.getInt(6);
				String acticationCode = res.getString(7);
				String activationCodeSalt = res.getString(8);
				String tempPassword = res.getString(9);
				String tempPasswordSalt = res.getString(10);

				user.setPassword(password);
				user.setSalt(salt);
				user.setStatusId(statusId);
				user.setFalseLogins(falseLogins);
				user.setActicationCode(acticationCode);
				user.setActivationCodeSalt(activationCodeSalt);
				user.setTempPassword(tempPassword);
				user.setTempPasswordSalt(tempPasswordSalt);

				user.setId(id);
				user.setRoleId(roleId);
			} else {

			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return user;
	}

	/*
	 * UNIVERSAL method to update one integer field in the database useful to
	 * update single field statuses
	 */
	public void updateDatabaseIntField(String tableName, String idFieldName,
			String fieldName, int targetId, int targetValue) {

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "UPDATE " + tableName + " SET " + fieldName
				+ " = ? WHERE " + idFieldName + " = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, targetValue);
			st.setInt(2, targetId);

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

	}

	public Validator verifyUserEmail(String emailToSelect) {
		Validator v = new Validator();
		v.setVerified(false);
		v.setStatus("Error, the system could not resolve the provided combination of username and password.");

		PreparedStatement st = null;
		String query = "SELECT ID FROM USERS WHERE EMAIL = ?";

		try {
			st = con.prepareStatement(query);
			st.setString(1, emailToSelect);

			ResultSet res = st.executeQuery();

			if (res.next()) {
				v.setVerified(true);
				v.setStatus("");
				return v;
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return v;
	}

	/**
	 * This method sets an activation code for this user
	 * 
	 * @param userId
	 */
	public String setActivationCode(int userId) {
		String out = "";
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET ACTIVATIONCODE = ?, ACTIVATIONCODESALT = ? WHERE ID = ?";

		try {
			PasswordHasher ph = new PasswordHasher();

			// generate activation code with salt
			String aSalt = ph.generateSalt();

			// generate temporary password with salt
			String activationCode = ph.generateRandomString();
			out = activationCode;

			// email the activation code
			System.out.println("Activation code: " + activationCode);

			// hashing
			String activationCodeHashed = ph.sha512(activationCode, aSalt);

			// store information to db
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, activationCodeHashed);
			st.setString(2, aSalt);
			st.setInt(3, userId);

			int affectedRows = st.executeUpdate();

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			// if (affectedRows == 0) {}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return out;
	}

	/**
	 * This function unsets the activation code
	 * 
	 * @param userId
	 */
	public void unsetActivationCode(int userId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET ACTIVATIONCODE = \"\", ACTIVATIONCODESALT = \"\" WHERE ID = ?";

		try {

			// store information to db
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, userId);

			int affectedRows = st.executeUpdate();

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			// if (affectedRows == 0) {}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
	}

	/**
	 * This function generates and stores temporary and activation codes to the
	 * database.
	 * 
	 * @param userId
	 */
	public void setActivationCodeAndTempPassword(int userId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET ACTIVATIONCODE = ?, ACTIVATIONCODESALT = ?, TEMPPASSWORD = ?, TEMPPASSWORDSALT = ? WHERE ID = ?";

		try {
			PasswordHasher ph = new PasswordHasher();

			// generate activation code with salt
			String aSalt = ph.generateSalt();
			String tSalt = ph.generateSalt();

			// generate temporary password with salt
			String activationCode = ph.generateRandomString();
			String tempPassword = ph.generateRandomString();

			// hashing
			String activationCodeHashed = ph.sha512(activationCode, aSalt);
			String tempPasswordHashed = ph.sha512(tempPassword, tSalt);

			// store information to db
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, activationCodeHashed);
			st.setString(2, aSalt);
			st.setString(3, tempPasswordHashed);
			st.setString(4, tSalt);
			st.setInt(5, userId);

			int affectedRows = st.executeUpdate();

			User u = selectBrokerUser(userId);

			// send activation code to the supervisor
			String superSubject = Enumeration.Strings.ACCOUNT_FRGTN_SUPER_SUBJECT;
			String superMessage = "Broker "
					+ u.getFirstName()
					+ " "
					+ u.getLastName()
					+ " requested password recovery. Please provide him/her with with the following activation code: "
					+ activationCode;

			SendEmail.sendEmailNotification(getSuperEmail(u.getId()),
					superSubject, superMessage);

			// send temp password to the broker
			String brokerSubject = Enumeration.Strings.ACCOUNT_FRGTN_BROKER_SUBJECT;
			String brokerMessage = "Dear "
					+ u.getFirstName()
					+ " "
					+ u.getLastName()
					+ ", your temporary password is: "
					+ tempPassword
					+ "\n\nIn order to access your account, you will need an activation code which had been sent to your supervisor.";
			SendEmail.sendEmailNotification(u.getEmail(), brokerSubject,
					brokerMessage);

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			// if (affectedRows == 0) {}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
	}

	public void unsetActivationCodeAndTempPassword(int userId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET ACTIVATIONCODE = \"\", ACTIVATIONCODESALT = \"\", TEMPPASSWORD = \"\", TEMPPASSWORDSALT = \"\" WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, userId);

			int affectedRows = st.executeUpdate();

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			// if (affectedRows == 0) {}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
	}

	public Validator checkIfUsernamePasswordMatch(String email, String plainPass) {
		// 1. validate input
		Validator result = validateEmailAndPlainInput(email, plainPass);
		if (!result.isVerified()) {
			return result;
		}

		// 2. validate email
		result = verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		InputValidation iv = new InputValidation();
		PasswordHasher hasher = new PasswordHasher();

		// get this user limited info from the database
		User user = selectUserByEmailLimited(email);

		String dbHash = user.getPassword();
		String dbSalt = user.getSalt();
		int statusId = user.getStatusId();
		int falseLogins = user.getFalseLogins();
		int id = user.getId();

		// 3. check if this user is active
		if (statusId != Enumeration.User.USER_STATUSID_ACTIVE) {
			result.setVerified(false);
			result.setStatus("Error, cannot login, this user account has been locked");
			return result;
		}

		String plainHash = hasher.sha512(plainPass, dbSalt);

		// 4. if entered password is correct, return true with welcome message
		if (plainHash.equals(dbHash)) {

			updateDatabaseIntField("USERS", "ID", "FALSELOGINS", id, 0);
			unsetActivationCodeAndTempPassword(id);
			result.setVerified(true);
			result.setStatus("Welcome to Stocks Trading System");

			return result;
		} else {
			// 5. else record the failed login attempt
			int newFalseLogins = falseLogins + 1;
			updateDatabaseIntField("USERS", "ID", "FALSELOGINS", id,
					newFalseLogins);

			// if we reached the max of failed logins, lock the account, sent an
			// email
			if (newFalseLogins == Enumeration.User.USER_MAX_LOGIN_ATTEMPTS) {
				// lock
				updateDatabaseIntField("USERS", "ID", "STATUSID", id,
						Enumeration.User.USER_STATUSID_LOCKED);

				// generate activation code
				String activationCode = setActivationCode(id);

				// send email with activation code
				SendEmail.sendEmailNotification(email,
						Enumeration.Strings.ACCOUNT_LOCKED_SUBJECT,
						Enumeration.Strings.ACCOUNT_LOCKED_MESSAGE
								+ activationCode);

				result.setVerified(false);
				result.setStatus("Error, exceeded the maximum number of login attempts, this user account has been locked");
				return result;
			} else {
				result.setVerified(false);
				result.setStatus("Error, the system could not resolve the provided combination of username and password.");
				return result;
			}
		}

	}

	public Validator validateEmailAndPlainInput(String email, String plainPass) {
		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vEmail, vPlain;
		Boolean verified = true;
		String status = "";

		// 1. email
		vEmail = iv.validateEmail(email, "Email");
		verified &= vEmail.isVerified();
		status += vEmail.getStatus();

		// 2. plain
		vPlain = iv.validateString(plainPass, "Password");
		verified &= vPlain.isVerified();
		status += vPlain.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}

	public Validator validateEmailPasswordCodeInput(String email,
			String plainPass, String plainCode) {
		InputValidation iv = new InputValidation();
		Validator vResult = new Validator();
		Validator vEmail, vPlain, vCode;
		Boolean verified = true;
		String status = "";

		// 1. email
		vEmail = iv.validateEmail(email, "Email");
		verified &= vEmail.isVerified();
		status += vEmail.getStatus();

		// 2. plain
		vPlain = iv.validateString(plainPass, "Password");
		verified &= vPlain.isVerified();
		status += vPlain.getStatus();

		// 2. code
		vPlain = iv.validateString(plainCode, "Activation Code");
		verified &= vPlain.isVerified();
		status += vPlain.getStatus();

		vResult.setVerified(verified);
		vResult.setStatus(status);

		return vResult;
	}

	// activation of locked account
	public Validator checkIfUsernamePasswordActivationCodeMatch(String email,
			String plainPass, String plainCode) {
		// validate input
		Validator result = validateEmailPasswordCodeInput(email, plainPass,
				plainCode);
		if (!result.isVerified()) {
			return result;
		}

		// validate email
		result = verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		PasswordHasher hasher = new PasswordHasher();
		User u = selectUserByEmailLimited(email);
		String plainPassHashed = hasher.sha512(plainPass, u.getSalt());
		String plainCodeHashed = hasher.sha512(plainCode,
				u.getActivationCodeSalt());

		if (plainPassHashed.equals(u.getPassword())
				&& plainCodeHashed.equals(u.getActicationCode())) {
			// clean the codes
			unsetActivationCodeAndTempPassword(u.getId());

			// reset false logins
			updateDatabaseIntField("USERS", "ID", "FALSELOGINS", u.getId(), 0);

			// unlock the user
			updateDatabaseIntField("USERS", "ID", "STATUSID", u.getId(),
					Enumeration.User.USER_STATUSID_ACTIVE);

			result.setVerified(true);
			result.setStatus("Account unlocked");
			return result;
		} else {
			result.setVerified(false);
			result.setStatus("Authentication failed");
			return result;
		}
	}

	// forgotten password
	public Validator checkIfUsernameTempPasswordActivationCodeMatch(
			String email, String plainTempPass, String plainCode) {
		// validate input
		Validator result = validateEmailPasswordCodeInput(email, plainTempPass,
				plainCode);
		if (!result.isVerified()) {
			return result;
		}

		// validate email
		result = verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		PasswordHasher hasher = new PasswordHasher();
		User u = selectUserByEmailLimited(email);
		String plainTempPassHashed = hasher.sha512(plainTempPass,
				u.getTempPasswordSalt());
		String plainCodeHashed = hasher.sha512(plainCode,
				u.getActivationCodeSalt());

		if (plainTempPassHashed.equals(u.getTempPassword())
				&& plainCodeHashed.equals(u.getActicationCode())) {
			// clean the codes
			unsetActivationCodeAndTempPassword(u.getId());

			// unlock
			updateDatabaseIntField("USERS", "ID", "STATUSID", u.getId(),
					Enumeration.User.USER_STATUSID_ACTIVE);

			// reset false logins
			updateDatabaseIntField("USERS", "ID", "FALSELOGINS", u.getId(), 0);

			result.setVerified(true);
			result.setStatus("Account unlocked");
			return result;
		} else {
			result.setVerified(false);
			result.setStatus("Authentication failed");
			return result;
		}
	}

	public Validator forgotPassword(String email) {
		Validator v = new Validator();
		InputValidation iv = new InputValidation();

		// check input
		v = iv.validateEmail(email, "Email");

		if (!v.isVerified()) {
			return v;
		}

		// validate email
		v = verifyUserEmail(email);
		if (!v.isVerified()) {
			v.setStatus("Activation code and temporary password had been sent to your mail box");
			return v;
		}

		User u = selectUserByEmailLimited(email);

		setActivationCodeAndTempPassword(u.getId());

		v.setVerified(true);
		v.setStatus("Activation code and temporary passwod had been sent to your mail box");

		return v;
	}

	public int getUserIDByEmail(String Email) {
		int userID = -1;
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("select ID from USERS where EMAIL='" + Email + "'");
			rs = st.getResultSet();
			if (rs.next()) {
				userID = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userID;
	}

	@SuppressWarnings("deprecation")
	public boolean isUserPasswordExpired(int UserID) {
		boolean result = false;

		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("SELECT * from USERPASSWORDHISTORY where USERID="
					+ UserID + " order by SETON DESC");
			rs = st.getResultSet();
			if (rs.next()) {
				Timestamp setOnDate = rs.getTimestamp("SETON");
				Timestamp expireOnDate = setOnDate;
				java.util.Date date = new java.util.Date();
				Timestamp currentDate = new Timestamp(date.getTime());
				int setOnMonth = setOnDate.getMonth();
				expireOnDate.setMonth(setOnMonth + 3);
				if (expireOnDate.before(currentDate)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean passwordHasBeenAlreadyUsed(int userID, String newPassword,
			int numberOfPasswordsToLookUp) {
		boolean result = false;
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("SELECT * from USERPASSWORDHISTORY where USERID="
					+ userID + " order by SETON DESC");
			rs = st.getResultSet();
			for (int count = 0; count < numberOfPasswordsToLookUp; count++) {
				if (rs.next()) {
					if (newPassword.equals(rs.getString("PASSWORD"))) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isFirstLoginEver(int UserID) {

		boolean result = false;

		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("select count(*) from USERPASSWORDHISTORY where USERID ="
					+ UserID);
			rs = st.getResultSet();
			if (rs.next()) {
				if (rs.getInt(1) == 1) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getSuperEmail(int UserID) {
		String superEmail = null;

		int firmID;
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("SELECT FIRMID FROM USERS where ID = " + UserID);
			rs = st.getResultSet();
			if (rs.next()) {
				firmID = rs.getInt("FIRMID");

				st = con.createStatement();
				st.executeQuery("SELECT SUPER_EMAIL FROM BROKERAGE_FIRM_INFO where ID = "
						+ firmID);
				rs = st.getResultSet();

				if (rs.next()) {
					superEmail = rs.getString("SUPER_EMAIL");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return superEmail;
	}

	public boolean isUserExists(String Email) {
		boolean result = false;
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			st.executeQuery("select ID from USERS where EMAIL='" + Email + "'");
			rs = st.getResultSet();
			if (rs.next()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Validator updateUserPassword(int userId, String plainPass,
			String plainPassConfirm) {
		Validator v = new Validator();

		// validate input
		if (!plainPass.equals(plainPassConfirm)) {
			v.setVerified(false);
			v.setStatus("Passwords don't match");
			return v;
		}

		// validate input
		InputValidation iv = new InputValidation();
		v = iv.validateString(plainPass, "Password");
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET PASSWORD = ?, SALT = ? WHERE ID = ?";

		try {

			// Password hashing
			PasswordHasher ph = new PasswordHasher();
			String salt = ph.generateSalt();
			String passwordHashed = ph.sha512(plainPass, salt);
			// end password hashing

			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, passwordHashed);
			st.setString(2, salt);
			st.setInt(3, userId);

			int affectedRows = st.executeUpdate();
			
			User u = selectBrokerUser(userId);

			// log to DB
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			LoggerCustom.logLoginActivity(u.getEmail(), "password update");
			
			// email notification
			String messageBody = "Dear " + u.getFirstName() + " " + u.getLastName() + "\n\nYour password had been recently changed.";
			SendEmail.sendEmailNotification(u.getEmail(), "Password changed", messageBody);
			
			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the table");
				return v;
			}

			v.setVerified(true);
			v.setStatus("Password updated");

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return v;
	}

}
