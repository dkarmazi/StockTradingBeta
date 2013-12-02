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
import java.util.Vector;
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

        private String encryption_iv = null;
        private String encryption_key = null;
        
	// connect to DB
	public DatabaseConnector() {                
		try {
                        String url = "jdbc:mysql://" + ReaderConfig.getDbHostIp() 
                               + ":" + ReaderConfig.getDbPort()
                               + "/" + ReaderConfig.getDbSchema();

                       String user = ReaderConfig.getDbUser();
                       String password = ReaderConfig.getDbPassword();                   
                    

			Connection con = DriverManager.getConnection(url, user, password);
			setCon(con);
                        
                        // Set the encyrption parameters from the config file
                        encryption_iv = ReaderConfig.getEncryptionIV();
                        encryption_key = ReaderConfig.getEncryptionKey();
                        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
                        System.out.println("Error : Filed to establish the Database connection.");
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
	 * This function returns all admin users 0 - all 1,2 - with certain status
	 */
	public ArrayList<User> selectAdministratorsAll(int pStatusId) {
		ArrayList<User> usersAll = new ArrayList<User>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM USERS WHERE ROLEID = 1";

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
				String iv = encryption_iv;
				String key = encryption_key;

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
	public UserAdmin selectAdminUser(int idToSelect) {
		UserAdmin user = new UserAdmin();

		PreparedStatement st = null;
		String query = "SELECT * FROM USERS WHERE ID = ? AND ROLEID = 1";

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
			String iv = encryption_iv;
			String key = encryption_key;

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
	public Validator insertNewAdmin(UserAdmin newUser) {
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
			String iv = encryption_iv;
			String key = encryption_key;
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

	public Validator updateAdmin(int idToUpdate, UserAdmin user) {
		// validate input
            
                user.setBrokerFirmId(-1);
		Validator v = user.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "UPDATE USERS SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, SSN = ?, ROLEID = ?, STATUSID = ?, FIRMID = ? WHERE ID = ?";

		try {

			// Password hashing
			PasswordHasher ph = new PasswordHasher();
			String salt = ph.generateSalt();
			String passwordHashed = ph.sha512(user.getPassword(), salt);
			// end password hashing

			// Sensitive data encryption
			String iv = encryption_iv;
			String key = encryption_key;
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

			st.setInt(5, user.getRoleId());
			st.setInt(6, user.getStatusId());
			st.setInt(7, user.getBrokerFirmId());
			st.setInt(8, idToUpdate);

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
	 * This function returns an array list of the brokerage firms by status
	 */
	public ArrayList<BrokerageFirm> selectBrokerageFirmsByStatus(int statusId) {
		ArrayList<BrokerageFirm> brokerageFirms = new ArrayList<BrokerageFirm>();
		ResultSet rs = null;
                PreparedStatement st = null;
                
		String query = "SELECT * FROM BROKERAGE_FIRM_INFO WHERE STATUSID = ?;"; 

		try {
                        st = this.con.prepareStatement(query);
			st.setInt(1, statusId);
                    
			ResultSet res = st.executeQuery();

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
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM USERS WHERE ROLEID = 2";

		if (pStatusId != Enumeration.Broker.BROKER_SELECT_PARAM_EMPTY) {
			query += " AND STATUSID = \"" + pStatusId + "\"";
		}

		try {
			st = this.con.prepareStatement(query);
			ResultSet res = st.executeQuery();

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
				String iv = encryption_iv;
				String key = encryption_key;

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
				String iv = encryption_iv;
				String key = encryption_key;

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
			String iv = encryption_iv;
			String key = encryption_key;

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
			String iv = encryption_iv;
			String key = encryption_key;
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

		//String query = "UPDATE USERS SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, SSN = ?, PASSWORD = ?, SALT = ?, ROLEID = ?, STATUSID = ?, FIRMID = ? WHERE ID = ?";
                String query = "UPDATE USERS SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, SSN = ?, ROLEID = ?, STATUSID = ?, FIRMID = ? WHERE ID = ?";
		try {

			// Password hashing
			PasswordHasher ph = new PasswordHasher();
			String salt = ph.generateSalt();
			String passwordHashed = ph.sha512(user.getPassword(), salt);
			// end password hashing

			// Sensitive data encryption
			String iv = encryption_iv;
			String key = encryption_key;
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
			//st.setString(5, passwordHashed);
			//st.setString(6, salt);
			st.setInt(5, user.getRoleId());
			st.setInt(6, user.getStatusId());
			st.setInt(7, user.getBrokerFirmId());
			st.setInt(8, idToUpdate);

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
	public ArrayList<Stock> selectStockAll(int pStatusId) {
		ArrayList<Stock> stocksAll = new ArrayList<Stock>();
		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "SELECT * FROM STOCKS  ";
		if (pStatusId != 0) {
			query += " WHERE STATUSID = \"" + pStatusId + "\"";
		}
		try {
			st = this.con.prepareStatement(query);
			ResultSet res = st.executeQuery();

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

			if (res.next()){

				int id = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				String phone = res.getString(5);
				double balance = res.getDouble(6);
				double pendingbalance = res.getDouble(7);
				int statusId = res.getInt(8);
	
				
				
				customer.setId(id);
				customer.setFirstName(firstName);
				customer.setLastName(lastName);
				customer.setEmail(email);
				customer.setPhone(phone);
				customer.setBalance(balance);
				customer.setPendingBalance(pendingbalance);
				customer.setStatusId(statusId);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		
                return customer;
	}

	/**
	 * This method inserts a new customer
	 */
	public Validator insertNewCustomerInfo(CustomerInfo newCustomer) {
		Validator v = newCustomer.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null, st2 = null;
		ResultSet rs = null, rs2 = null;
		long newId = 0;
		String query = "INSERT INTO CUSTOMER_INFO (FIRSTNAME, LASTNAME, EMAIL, PHONE, BALANCE, PENDINGBALANCE, STATUSID) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String query2 = "INSERT INTO HAS_FIRM_CUSTOMERS (FIRMID, CUSTOMERID) VALUES (?, ?)";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newCustomer.getFirstName());
			st.setString(2, newCustomer.getLastName());
			st.setString(3, newCustomer.getEmail());
			st.setString(4, newCustomer.getPhone());
			st.setDouble(5, newCustomer.getBalance());
			st.setDouble(6, newCustomer.getPendingBalance());
			st.setInt(7, newCustomer.getStatusId());

			int affectedRows = st.executeUpdate();
			rs = st.getGeneratedKeys();
			rs.next();
			int insertId = rs.getInt(1);
			

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			
			// insert firm
			st2 = this.con.prepareStatement(query2,
					Statement.RETURN_GENERATED_KEYS);			
			st2.setInt(1, newCustomer.getFirmId());
			st2.setInt(2, insertId);
			int affectedRows2 = st2.executeUpdate();
			
	
			if (affectedRows == 0 || affectedRows2 == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		v.setVerified(true);
		v.setStatus("Record " + newId + " inserted");

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
		String query = "UPDATE CUSTOMER_INFO SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PHONE = ?, BALANCE = ?, PENDINGBALANCE = ?, STATUSID = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, customerToUpdate.getFirstName());
			st.setString(2, customerToUpdate.getLastName());
			st.setString(3, customerToUpdate.getEmail());
			st.setString(4, customerToUpdate.getPhone());
			st.setDouble(5, customerToUpdate.getBalance());
			st.setDouble(6, customerToUpdate.getPendingBalance());
			st.setInt(7, customerToUpdate.getStatusId());
			st.setInt(8, idToUpdate);

			int affectedRows = st.executeUpdate();
			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the record");
				return v;
			}
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());


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

		String query = "SELECT PASSWORD, SALT, STATUSID, FALSELOGINS, ID, ROLEID, ACTIVATIONCODE, ACTIVATIONCODESALT, TEMPPASSWORD, TEMPPASSWORDSALT, FIRMID FROM USERS WHERE EMAIL = ?";

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
				int firmId = res.getInt(11);

				user.setPassword(password);
				user.setSalt(salt);
				user.setStatusId(statusId);
				user.setFalseLogins(falseLogins);
				user.setActicationCode(acticationCode);
				user.setActivationCodeSalt(activationCodeSalt);
				user.setTempPassword(tempPassword);
				user.setTempPasswordSalt(tempPasswordSalt);
				user.setBrokerFirmId(firmId);
				
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

			LoggerCustom.logLoginActivity(email, "Login Successful");

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

				LoggerCustom.logLoginActivity(email, "Account locked");

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

			LoggerCustom.logLoginActivity(email, "Account unlocked");

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

			LoggerCustom.logLoginActivity(email,
					"Forgotten password recovery check passed");

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

		LoggerCustom.logLoginActivity(email, "User forgot password");

		User u = selectUserByEmailLimited(email);

		setActivationCodeAndTempPassword(u.getId());

		v.setVerified(true);
		v.setStatus("Activation code and temporary passwod had been sent to your mail box");

		return v;
	}

	public int getUserIDByEmail(String Email) {
		int userID = -1;

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT ID from USERS WHERE EMAIL = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setString(1, Email);
			rs = st.executeQuery();

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
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * from USERPASSWORDHISTORY where USERID = ? order by SETON DESC";

		try {
			st = this.con.prepareStatement(query);			
			st.setInt(1, UserID);
			rs = st.executeQuery();
			

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
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM USERPASSWORDHISTORY WHERE USERID = ? ORDER BY SETON DESC";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, userID);
			rs = st.executeQuery();
			
			for (int count = 0; count < numberOfPasswordsToLookUp; count++) {
				if (rs.next()) {
					PasswordHasher ph = new PasswordHasher();
					String salt = rs.getString("SALT");
					String passwordHashed = ph.sha512(newPassword, salt);
					if (passwordHashed.equals(rs.getString("PASSWORD"))) {
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

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT COUNT(*) FROM USERPASSWORDHISTORY WHERE USERID = ?";

		try {
			
			st = this.con.prepareStatement(query);
			st.setInt(1, UserID);
			rs = st.executeQuery();

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
		PreparedStatement st1 = null;
		PreparedStatement st2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		String query1 = "SELECT FIRMID FROM USERS where ID = ?";
		String query2 = "SELECT SUPER_EMAIL FROM BROKERAGE_FIRM_INFO where ID = ?";		
		
		try {
			
			st1 = this.con.prepareStatement(query1);			
			st1.setInt(1, UserID);
			rs1 = st1.executeQuery();

			if (rs1.next()) {
				firmID = rs1.getInt("FIRMID");

				st2 = this.con.prepareStatement(query2);
				st2.setInt(1, firmID);
				rs2 = st2.executeQuery();
				
				if (rs2.next()) {
					superEmail = rs2.getString("SUPER_EMAIL");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return superEmail;
	}

	public boolean isUserExists(String Email) {
		boolean result = false;

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT ID FROM USERS WHERE EMAIL = ?";

		try {
			st = this.con.prepareStatement(query);			
			st.setString(1, Email);
			rs = st.executeQuery();

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

		// validate input step 1
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

		// check if the password is recent
		if (passwordHasBeenAlreadyUsed(userId, plainPass, 3)) {
			v.setVerified(false);
			v.setStatus("Our records indicate that this password had alerady been used in the recent past");
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
			String messageBody = "Dear " + u.getFirstName() + " "
					+ u.getLastName()
					+ "\n\nYour password had been recently changed.";
			SendEmail.sendEmailNotification(u.getEmail(), "Password changed",
					messageBody);

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
	
	public int getActionID(String Action){
		int actionID = -1;

		PreparedStatement st = null;
		ResultSet rs = null;
		
		String query = "SELECT * from ACTIONS WHERE ACTIONS.DESC = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setString(1, Action);
			rs = st.executeQuery();

			if (rs.next()) {
				actionID = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionID;
	}
	
	public boolean isUserAllowedToDoAction (int userID, int actionID){
		boolean result = false;

		//find the user role:
		int UserRoleID = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT ROLEID from USERS where ID= ?";
		try{
			st = this.con.prepareStatement(query);
			st.setInt(1, userID);
			rs = st.executeQuery();
 	        rs = st.getResultSet();
	        result = rs.next();	        
	        if (!result){
	        	return result;
	        }	        
	        UserRoleID = rs.getInt("ROLEID");
		}catch (Exception e) {
			e.printStackTrace();
		}

		//find if the user role got the right to perform the action
		query = "SELECT * FROM ROLERIGHTS where (ROLEID = ? and ACTIONID = ?)";
		try{
			st = this.con.prepareStatement(query);
			st.setInt(1, UserRoleID);
			st.setInt(2, actionID);
			rs = st.executeQuery();
 	        rs = st.getResultSet();
	        result = rs.next();
	        return result;
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return result;
	}
        

    public int insertNewTradingSession(TradingSession TS){
		PreparedStatement st = null;
		ResultSet rs = null;
		int TSID = -1;
		String query = "INSERT INTO TRADINGSESSIONS (START, LIMIT_UP, LIMIT_DOWN, ACTIVE) VALUES (?, ?, ?, ?)";
		
		try {

			st = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			st.setTimestamp(1, TS.getStartTime());
			st.setInt(2, TS.getLimitUp());
			st.setInt(3, TS.getLimitDown());
			st.setInt(4, TS.getActive());
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()){
				TSID = rs.getInt(1);
			}
			
			for (int i=0; i < TS.getAvailableStocksID().size(); i++){
				query = "INSERT INTO HAS_TS_STOCKS (TS_ID, STOCK_ID) VALUES (?, ?)";
				st = this.con.prepareStatement(query);
				st.setInt(1, TSID);
				st.setInt(2, TS.getAvailableStocksID().get(i));
				
				st.executeUpdate();
				
			}
			
			for (int i=0; i < TS.getAvailableFirmsID().size(); i++){
				query = "INSERT INTO HAS_TS_BROKERAGEFIRMS (TS_ID, FIRM_ID) VALUES (?, ?)";
				st = this.con.prepareStatement(query);
				st.setInt(1, TSID);
				st.setInt(2, TS.getAvailableFirmsID().get(i));
				
				st.executeUpdate();
				
			}
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}


		return TSID;
	
	}
	
	public boolean endTradingSession(int tradingSessionID){
		boolean result = false;
		
		PreparedStatement st = null;
		

		String query = "UPDATE TRADINGSESSIONS SET END_TIME=?, ACTIVE=? WHERE ID=?";
		
		try {

			st = this.con.prepareStatement(query);
			
			java.util.Date date = new java.util.Date();
	    	Timestamp currentDate = new Timestamp(date.getTime());
	    	
	    	st.setTimestamp(1, currentDate);
	    	st.setInt(2, 0);
	    	st.setInt(3, tradingSessionID);
			
			st.executeUpdate();
			result = true;

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
		
		
		return result;
	}
	
	public TradingSession getTradingSessionInfo(int tradingSessionID){
		TradingSession TS = new TradingSession();
		
		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "SELECT * FROM TRADINGSESSIONS WHERE ID = ?";
		
		try {

			st = this.con.prepareStatement(query);
	    	st.setInt(1, tradingSessionID);
			
			rs = st.executeQuery();
			
			if (rs.next()){
				TS.setStartTime(rs.getTimestamp("START"));
				TS.setLimitUp(rs.getInt("LIMIT_UP"));
				TS.setLimitDown(rs.getInt("LIMIT_DOWN"));
			}
			
			query = "SELECT STOCK_ID FROM HAS_TS_STOCKS WHERE TS_ID = ?";
			st = this.con.prepareStatement(query);
	    	st.setInt(1, tradingSessionID);
			
	    	Vector <Integer> stocksID = new Vector<Integer>();
	    	Vector <String> stocksNames = new Vector<String>();
	    	int stockID;
			rs = st.executeQuery();
			while (rs.next()){
				stockID = rs.getInt("STOCK_ID");
				stocksID.add(stockID);
				stocksNames.add(getStockNameByID(stockID));
			}
			TS.setAvailableStocksID(stocksID);
			TS.setAvailableStocksNames(stocksNames);
			
			
			query = "SELECT FIRM_ID FROM HAS_TS_BROKERAGEFIRMS WHERE TS_ID = ?";
			st = this.con.prepareStatement(query);
	    	st.setInt(1, tradingSessionID);
			
	    	Vector <Integer> firmsID = new Vector<Integer>();
	    	Vector <String> firmsNames = new Vector<String>();
	    	int firmID;
			rs = st.executeQuery();
			while (rs.next()){
				firmID = rs.getInt("FIRM_ID");
				firmsID.add(firmID);
				firmsNames.add(getBrokerageFirmNameByID(firmID));
			}
			TS.setAvailableFirmsID(firmsID);
			TS.setAvailableFirmsNames(firmsNames);
			
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
		
		
		return TS;
	}

	private String getStockNameByID(int stockID) {
		String stockName="";

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * from STOCKS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, stockID);
			rs = st.executeQuery();

			if (rs.next()) {
				stockName = rs.getString("NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockName;

	}
	
	private String getBrokerageFirmNameByID(int firmID) {
		String stockName="";

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * from BROKERAGE_FIRM_INFO WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, firmID);
			rs = st.executeQuery();

			if (rs.next()) {
				stockName = rs.getString("NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockName;

	}
	
	public int isThereMatch(Order newOrder){
		int matchID = -1;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM ORDERS_M WHERE ( (TYPEID = ?)";
				query += " AND (CUSTOMERID != ?)";
				query += " AND (STOCKID = ?)";
				query += " AND (AMOUNT = ?)";
				query += " AND (PRICE = ?))";
				query += " ORDER BY ORDERID";

		try {
			st = this.con.prepareStatement(query);
			
			if (newOrder.getTypeId() == 1){
				st.setInt(1, 2);
			}else{
				st.setInt(1, 1);
			}
			
			st.setInt(2, newOrder.getCustomerId());
			st.setInt(3, newOrder.getStockId());
			st.setInt(4, newOrder.getAmount());
			st.setDouble(5, newOrder.getPrice());
			
			rs = st.executeQuery();
			//System.out.println(st.toString());
			if (rs.next()) {
				matchID = rs.getInt("ORDERID");
				LoggerCustom.logLoginActivity("Matching Engine", "Order match found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return matchID;
	}

	
	public boolean insertNewTransaction(Transaction newTransaction) {

		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "INSERT INTO TRANSACTIONS ("
				+ "SELLINGBROKERID, "
				+ "SELLINGCUSTOMERID, "
				+ "BUYINGBROKERID, "
				+ "BUYINGCUSTOMERID, "
				+ "STOCKID, "
				+ "AMOUNT, "
				+ "PRICE, "
				+ "TRANSACTIONS.TIME, "
				+ "SESSIONID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			st = this.con.prepareStatement(query);
			
			st.setInt(1, newTransaction.getSellingBrokerID());
			st.setInt(2, newTransaction.getSellingCustomerID());
			st.setInt(3, newTransaction.getBuyingBrokerID());
			st.setInt(4, newTransaction.getBuyingCustomerID());
			st.setInt(5, newTransaction.getStockID());
			st.setInt(6, newTransaction.getAmount());
			st.setDouble(7, newTransaction.getPrice());
			st.setTimestamp(8, newTransaction.getTime());
			st.setInt(9, newTransaction.getSessionID());
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res ==1 ){
				return true;
			}else{
				return false;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
			return false;
		}
	}
	
	public boolean updateCustomerBalance(int customerID, double newBalance){
		PreparedStatement st = null;
		

		String query = "UPDATE CUSTOMER_INFO SET BALANCE = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			
			st.setDouble(1, newBalance);
			st.setInt(2, customerID);
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res ==1 ){
				return true;
			}else{
				return false;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
			return false;
		}
			
	}

	public boolean updateCustomerPendingbalance(int customerID, double newPendingbalance){
		PreparedStatement st = null;
		

		String query = "UPDATE CUSTOMER_INFO SET PENDINGBALANCE = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			
			st.setDouble(1, newPendingbalance);
			st.setInt(2, customerID);
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res ==1 ){
				return true;
			}else{
				return false;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
			return false;
		}
			
	}

	
	public int selectCustomerStockAmount(int customerID, int stockID){
		int amount = -1;
		
		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "SELECT * FROM HAS_CUSTOMERS_STOCKS WHERE CUSTOMERID = ? AND STOCKID = ?";

		try {
			st = this.con.prepareStatement(query);
			
			st.setInt(1, customerID);
			st.setInt(2, stockID);
			
			rs = st.executeQuery();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (rs.next()){
				amount = rs.getInt("AMOUNT");
			}	
			

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
		
		return amount;
	}

	public boolean updateCustomerStockAmount(int customerID, int stockID, int newAmount){
		boolean result = false;
		
		PreparedStatement st = null;
		
		String query = "UPDATE HAS_CUSTOMERS_STOCKS SET AMOUNT = ? WHERE STOCKID = ? and CUSTOMERID = ?;";

		try {
			st = this.con.prepareStatement(query);
			
			st.setInt(1, newAmount);
			st.setInt(2, stockID);
			st.setInt(3, customerID);
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res == 1){
				result = true;
			}	
			

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
		
		return result;
	}

	public boolean makeTransaction(Order newOrder, int matchedOrderID, int tradingSessionID){
		boolean result = false;

		//1-Creating new transaction:
		Order matchedOrder = selectOrder(matchedOrderID);
		Transaction newTransaction = new Transaction();
		
		if (newOrder.getTypeId() == 1){
			//newOrder is the selling order:
			newTransaction.setSellingBrokerID(newOrder.getBrokerId());
			newTransaction.setSellingCustomerID(newOrder.getCustomerId());
			
			//matchedOrder is the buying order:
			newTransaction.setBuyingBrokerID(matchedOrder.getBrokerId());
			newTransaction.setBuyingCustomerID(matchedOrder.getCustomerId());
		}else{
			//matchedOrder is the selling order:
			newTransaction.setSellingBrokerID(matchedOrder.getBrokerId());
			newTransaction.setSellingCustomerID(matchedOrder.getCustomerId());
			
			//newOrder is the buying order:
			newTransaction.setBuyingBrokerID(newOrder.getBrokerId());
			newTransaction.setBuyingCustomerID(newOrder.getCustomerId());
		}
		
		newTransaction.setStockID(newOrder.getStockId());
		newTransaction.setAmount(newOrder.getAmount());
		newTransaction.setPrice(newOrder.getPrice());
		
		java.util.Date date = new java.util.Date();
		Timestamp currentDate = new Timestamp(date.getTime());
		
		newTransaction.setTime(currentDate);
		newTransaction.setSessionID(tradingSessionID);
		
		result = insertNewTransaction(newTransaction);
		
		//2-Delete the matched order:
		deleteOrder(matchedOrderID);
		
		//3-Update Customers Info:
		int sellingCustomerID = newTransaction.getSellingCustomerID();
		int buyingCustomerID = newTransaction.getBuyingCustomerID();
		int stockID = newTransaction.getStockID();
		int transactionAmount = newTransaction.getAmount();
		double price = newTransaction.getPrice();
		
		//3-1-Update Selling Customer Balance:
		double newBalance = selectCustomerInfo(sellingCustomerID).getBalance() + (transactionAmount * price);
		updateCustomerBalance(sellingCustomerID, newBalance);

		//3-2-Update Buying Customer Balance:
		newBalance = selectCustomerInfo(buyingCustomerID).getBalance() - (transactionAmount * price);
		double newPendingbalance = selectCustomerInfo(buyingCustomerID).getPendingBalance() - (transactionAmount * price); 
		updateCustomerBalance(buyingCustomerID, newBalance);
		updateCustomerPendingbalance(buyingCustomerID, newPendingbalance);
		
		//3-3-Update Selling Customer Stocks:
		int ownedAmount = selectCustomerStockAmount(sellingCustomerID, stockID);
		int newAmount = ownedAmount - transactionAmount;
		updateCustomerStockAmount(sellingCustomerID, stockID, newAmount);
		
		//3-4-Update Buying Customer Stocks:
		ownedAmount = selectCustomerStockAmount(buyingCustomerID, stockID);
		newAmount = ownedAmount + transactionAmount;
		updateCustomerStockAmount(buyingCustomerID, stockID, newAmount);
		
		//4-Log the transaction:
		LoggerCustom.logLoginActivity("Matching Enging", "Transaction completed - " + newTransaction.toString());
		
		return result;
	}

	public Transaction selectTransaction(int transactionID) {
		Transaction trans = new Transaction();

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM TRANSACTIONS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, transactionID);
			
			rs = st.executeQuery();

			if (rs.next()){
				
				int SellingBrokerID = rs.getInt("SELLINGBROKERID");
				int SellingCustomerID  = rs.getInt("SELLINGCUSTOMERID");
				int BuyingBrokerID = rs.getInt("BUYINGBROKERID");
				int BuyingCustomerID = rs.getInt("BUYINGCUSTOMERID");
				int StockID = rs.getInt("STOCKID");
				int Amount = rs.getInt("AMOUNT");
				double Price = rs.getDouble("PRICE");
				Timestamp Time = rs.getTimestamp("TIME");
				int SessionID = rs.getInt("SESSIONID");

				
				String sellingBrokerName;
				String sellingCustomerName;
				String buyingBrokerName;
				String buyingCustomerName;
				String stockName;

				User user = selectBrokerUser(SellingBrokerID);
				sellingBrokerName = user.getFirstName() + " " + user.getLastName();
				
				user = selectBrokerUser(BuyingBrokerID);
				buyingBrokerName = user.getFirstName() + " " + user.getLastName();
				
				CustomerInfo customer = selectCustomerInfo(SellingCustomerID);
				sellingCustomerName = customer.getFirstName() + " " + customer.getLastName();
				
				customer = selectCustomerInfo(BuyingCustomerID);
				buyingCustomerName = customer.getFirstName() + " " + customer.getLastName();
				
				stockName = selectStock(StockID).getName();
				
				trans.setSellingBrokerID(SellingBrokerID);
				trans.setSellingCustomerID(SellingCustomerID);
				trans.setBuyingBrokerID(BuyingBrokerID);
				trans.setBuyingCustomerID(BuyingCustomerID);
				trans.setStockID(StockID);
				trans.setAmount(Amount);
				trans.setPrice(Price);
				trans.setTime(Time);
				trans.setSessionID(SessionID);
				trans.setBuyingBrokerName(buyingBrokerName);
				trans.setBuyingCustomerName(buyingCustomerName);
				trans.setSellingBrokerName(sellingBrokerName);
				trans.setSellingCustomerName(sellingCustomerName);
				trans.setStockName(stockName);
			}



		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return trans;
	}

	public boolean deleteTransaction(int transactionID){
		
		PreparedStatement st = null;
		

		String query = "DELETE FROM TRANSACTIONS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			
			st.setInt(1, transactionID);
			
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res ==1 ){
				return true;
			}else{
				return false;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
			return false;
		}
	}

	public boolean rollBackTransaction(int transactionID){
		boolean result = false;
		
		Transaction trans = selectTransaction(transactionID);
		
		//1-Update Customers Info:
		int sellingCustomerID = trans.getSellingCustomerID();
		int buyingCustomerID = trans.getBuyingCustomerID();
		int stockID = trans.getStockID();
		int transactionAmount = trans.getAmount();
		double price = trans.getPrice();
		
		//1-1-Update Selling Customer Balance:
		double newBalance = selectCustomerInfo(sellingCustomerID).getBalance() - (transactionAmount * price);
		updateCustomerBalance(sellingCustomerID, newBalance);

		//1-2-Update Buying Customer Balance:
		newBalance = selectCustomerInfo(buyingCustomerID).getBalance() + (transactionAmount * price);
		updateCustomerBalance(buyingCustomerID, newBalance);
		
		//1-3-Update Selling Customer Stocks:
		int ownedAmount = selectCustomerStockAmount(sellingCustomerID, stockID);
		int newAmount = ownedAmount + transactionAmount;
		updateCustomerStockAmount(sellingCustomerID, stockID, newAmount);
		
		//1-4-Update Buying Customer Stocks:
		ownedAmount = selectCustomerStockAmount(buyingCustomerID, stockID);
		newAmount = ownedAmount - transactionAmount;
		updateCustomerStockAmount(buyingCustomerID, stockID, newAmount);
		
		//4-Log the transaction:
		LoggerCustom.logLoginActivity("Matching Engine", "Transaction rollback completed: " + trans.toString());
		
		//5-Remove the transaction:
		result = deleteTransaction(transactionID);
		
		return result;
	}
	
	public boolean flushPendingOrders(){
		boolean result = false;
		
		PreparedStatement st = null;
		
		String query = "TRUNCATE TABLE ORDERS_M";

		try {
			st = this.con.prepareStatement(query);
			int res = st.executeUpdate();
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			if (res == 1){
				result = true;
			}	
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
		
		return result;
	}
	
	public boolean calculateStockClosingPrice(int tradingSessionID, int stockID){
		boolean result = false;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM TRANSACTIONS where SESSIONID = ? and STOCKID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, tradingSessionID);
			st.setInt(2, stockID);
			
			rs = st.executeQuery();

			int stocksAmount = 0;
			double totalValue = 0;
			
			boolean found = false;
			while (rs.next()){
				found = true;
				stocksAmount += rs.getInt("AMOUNT");
				totalValue += rs.getInt("AMOUNT") * rs.getDouble("PRICE");
			}

			if (found){
				double closingPrice = totalValue / stocksAmount;
				result = updateStockPrice(stockID, closingPrice);
				if (result){
					LoggerCustom.logLoginActivity("Matching Engine", "Stock Price updated: StockID: " + stockID + ", new price: " + closingPrice);
				}
			}
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return result;
	}

	public boolean updateStockPrice (int stockID, double newPrice){
		PreparedStatement st = null;
		

		String query = "UPDATE STOCKS SET PRICE = ? WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			
			st.setDouble(1, newPrice);
			st.setInt(2, stockID);
			
			int res = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (res ==1 ){
				return true;
			}else{
				return false;
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
			return false;
		}

	}

	public boolean calculateClosingPrices(int tradingSessionID){
		boolean result = false;
	
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM HAS_TS_STOCKS WHERE TS_ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, tradingSessionID);
			
			
			rs = st.executeQuery();

			int stockID; 
			while (rs.next()){
				stockID = rs.getInt("STOCK_ID");
				calculateStockClosingPrice(tradingSessionID, stockID);
				result = true;
			}
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return result;
	}

	public TradingSession selectTradingSession(int tradingSessionID){
		TradingSession tradingSession = new TradingSession();
	
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM TRADINGSESSIONS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, tradingSessionID);
			
			rs = st.executeQuery();

			 if (rs.next()){
				 Timestamp start = rs.getTimestamp(2);
				 Timestamp end = rs.getTimestamp(3);
				 int limitUp = rs.getInt(4);
				 int limitDown = rs.getInt(5);
				 int active = rs.getInt(6);
				 
				 tradingSession.setStartTime(start);
				 tradingSession.setEndTime(end);
				 tradingSession.setLimitUp(limitUp);
				 tradingSession.setLimitDown(limitDown);
				 tradingSession.setActive(active);
			 }
			
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return tradingSession;
	}
	
	public ArrayList<Transaction> selectTransactionAll(int tradingSessionID) {
        ArrayList<Transaction> transactionAll = new ArrayList<Transaction>();
        PreparedStatement st = null;

        String query = "SELECT * FROM TRANSACTIONS WHERE SESSIONID = ?";

        try {
                st = this.con.prepareStatement(query);
                st.setInt(1, tradingSessionID);
                
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                	Transaction trans = new Transaction();
                	
                	
                	int id = rs.getInt("ID");
                	int SellingBrokerID = rs.getInt("SELLINGBROKERID");
    				int SellingCustomerID  = rs.getInt("SELLINGCUSTOMERID");
    				int BuyingBrokerID = rs.getInt("BUYINGBROKERID");
    				int BuyingCustomerID = rs.getInt("BUYINGCUSTOMERID");
    				int StockID = rs.getInt("STOCKID");
    				int Amount = rs.getInt("AMOUNT");
    				double Price = rs.getDouble("PRICE");
    				Timestamp Time = rs.getTimestamp("TIME");
    				int SessionID = rs.getInt("SESSIONID");
    				
    				User user = selectBrokerUser(SellingBrokerID);
    				String sellingBrokerName = user.getFirstName() + " " + user.getLastName();
    				
    				user = selectBrokerUser(BuyingBrokerID);
    				String buyingBrokerName = user.getFirstName() + " " + user.getLastName();
    				
    				CustomerInfo customer = selectCustomerInfo(SellingCustomerID);
    				String sellingCustomerName = customer.getFirstName() + " " + customer.getLastName();
    				
    				customer = selectCustomerInfo(BuyingCustomerID);
    				String buyingCustomerName = customer.getFirstName() + " " + customer.getLastName();
    				
    				String stockName = selectStock(StockID).getName();
    				
    				

    				trans.setID(id);
    				trans.setSellingBrokerID(SellingBrokerID);
    				trans.setSellingCustomerID(SellingCustomerID);
    				trans.setBuyingBrokerID(BuyingBrokerID);
    				trans.setBuyingCustomerID(BuyingCustomerID);
    				trans.setStockID(StockID);
    				trans.setAmount(Amount);
    				trans.setPrice(Price);
    				trans.setTime(Time);
    				trans.setSessionID(SessionID);
    				trans.setBuyingBrokerName(buyingBrokerName);
    				trans.setBuyingCustomerName(buyingCustomerName);
    				trans.setSellingBrokerName(sellingBrokerName);
    				trans.setSellingCustomerName(sellingCustomerName);
    				trans.setStockName(stockName);

    				transactionAll.add(trans);
                }
        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
        }

        return transactionAll;
	}        

	public Validator insertHasCustomerStocks(Relationship r) {
		Validator v = r.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "INSERT INTO HAS_CUSTOMERS_STOCKS (CUSTOMERID, STOCKID, AMOUNT, STATUSID) VALUES (?, ?, ?, ?)";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, r.getId1());
			st.setInt(2, r.getId2());
			st.setInt(3, r.getExtra());
			st.setInt(4, r.getStatus());
			
			int affectedRows = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			v.setVerified(false);
			v.setStatus("Record already exists, select it on the left pane");
			return v;			
		}

		v.setVerified(true);
		v.setStatus("Record inserted");

		return v;
	}

	
	public Validator updateHasCustomerStocks(Relationship r) {
		Validator v = r.validate();
		if (!v.isVerified()) {
			return v;
		}

		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "UPDATE HAS_CUSTOMERS_STOCKS SET STATUSID = ?, AMOUNT = ? WHERE CUSTOMERID = ? AND STOCKID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, r.getStatus());
			st.setInt(2, r.getExtra());
			st.setInt(3, r.getId1());
			st.setInt(4, r.getId2());
			
			int affectedRows = st.executeUpdate();		
			
			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update record");
				return v;
			}
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

		} catch (SQLException ex) {
			v.setVerified(false);
			v.setStatus("Update failed");
			return v;			
		}

		v.setVerified(true);
		v.setStatus("Record updated");

		return v;
	}

	public ArrayList<Stock> selectCustomerStocks(int customerId) {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		PreparedStatement st = null;
		String query = "SELECT STOCKS.*, HAS_CUSTOMERS_STOCKS.AMOUNT FROM STOCKS, HAS_CUSTOMERS_STOCKS WHERE STOCKS.ID = HAS_CUSTOMERS_STOCKS.STOCKID AND HAS_CUSTOMERS_STOCKS.CUSTOMERID = ? ORDER BY STOCKS.NAME";
		
		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Stock s = new Stock();
				s.setId(res.getInt(1));
				s.setName(res.getString(2));
				s.setPrice(res.getInt(4));
				s.setStatusId(res.getInt(5));
				s.setAmount(res.getInt(6));

				stocks.add(s);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stocks;
	}

	public ArrayList<Stock> selectAllStocks() {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		PreparedStatement st = null;
		String query = "SELECT * FROM STOCKS WHERE STATUSID = 1 ORDER BY NAME";
		
		try {
			st = this.con.prepareStatement(query);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Stock s = new Stock();
				s.setId(res.getInt(1));
				s.setName(res.getString(2));
				s.setPrice(res.getInt(4));
				s.setStatusId(res.getInt(5));
				stocks.add(s);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stocks;
	}
	
	public Relationship selectHasCustomerStocks(int customerId, int stockId) {
		Relationship r = new Relationship();
		PreparedStatement st = null;
		String query = "SELECT HAS_CUSTOMERS_STOCKS.* FROM HAS_CUSTOMERS_STOCKS WHERE CUSTOMERID = ? AND STOCKID = ?";
		
		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			st.setInt(2, stockId);
			ResultSet res = st.executeQuery();

			res.next();
			r.setId1(res.getInt(1));
			r.setId2(res.getInt(2));
			r.setExtra(res.getInt(3));
			r.setStatus(res.getInt(4));
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return r;
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

		String query = "INSERT INTO ORDERS_M (TYPEID, BROKERID, CUSTOMERID, STOCKID, AMOUNT, PRICE, STATUSID) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, newOrder.getTypeId());
			st.setInt(2, newOrder.getBrokerId());
			st.setInt(3, newOrder.getCustomerId());
			st.setInt(4, newOrder.getStockId());
			st.setInt(5, newOrder.getAmount());
			st.setDouble(6, newOrder.getPrice());
			st.setInt(7, newOrder.getStatusId());

			int affectedRows = st.executeUpdate();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not insert into the table");
				return v;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			v.setVerified(false);
			v.setStatus("Failed to insert into table");
			return v;
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
		String query = "SELECT * FROM ORDERS_M";
		
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
				int statusId = res.getInt(8);

				Order order = new Order();
				order.setOrderId(orderId);
				order.setTypeId(typeId);
				order.setBrokerId(brokerId);
				order.setCustomerId(customerId);
				order.setStockId(stockId);
				order.setAmount(amount);
				order.setPrice(price);
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
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String query = "SELECT O.*, S.NAME, CI.FIRSTNAME, CI.LASTNAME"
                     + " FROM ORDERS_M as O, USERS as U, CUSTOMER_INFO as CI, STOCKS as S"
				     + " WHERE O.BROKERID = U.ID"
				      + " AND O.CUSTOMERID = CI.ID"
				      + " AND O.STOCKID = S.ID"
				      + " AND O.TYPEID = ?"
				      + " AND U.FIRMID = ?";
		
		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, orderType);
			st.setInt(2, firmId);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int orderId = res.getInt(1);
				int typeId = res.getInt(2);
				int brokerId = res.getInt(3);
				int customerId = res.getInt(4);
				int stockId = res.getInt(5);
				int amount = res.getInt(6);
				double price = res.getDouble(7);
				int statusId = res.getInt(8);				
				String displaySummary = res.getString(9) + "["
						+ new DecimalFormat("#,##0").format(amount) + " @ "
						+ new DecimalFormat("#,##0.00").format(price) + "] ::"
						+ res.getString(10) + " " + res.getString(11);

				Order order = new Order();
				order.setOrderId(orderId);
				order.setTypeId(typeId);
				order.setBrokerId(brokerId);
				order.setCustomerId(customerId);
				order.setStockId(stockId);
				order.setAmount(amount);
				order.setPrice(price);
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
	 * This functions returns an array list of all the pending orders
	 */
	public ArrayList<Order> selectOrderDetailsByType( int orderType) {
		ArrayList<Order> ordersAll = new ArrayList<Order>();
		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "SELECT O.*, S.NAME AS STOCK_NAME, C.FIRSTNAME, C.LASTNAME , F.NAME AS FIRM_NAME"
				+ " FROM ORDERS_M O " 
                                + " INNER JOIN USERS U"
				+ " ON (O.BROKERID = U.ID)" 
                                + " INNER JOIN BROKERAGE_FIRM_INFO F"
                                + " ON (U.FIRMID = F.ID)" 
                                + " INNER JOIN STOCKS S"
				+ " ON (O.STOCKID = S.ID)" 
                                + " INNER JOIN CUSTOMER_INFO C"
				+ " ON (O.CUSTOMERID = C.ID)" 
                                + " WHERE O.TYPEID = ? ";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, orderType);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int orderId = res.getInt("ORDERID");
                                
				int typeId = res.getInt("TYPEID");
				int brokerId = res.getInt("BROKERID");
				int customerId = res.getInt("CUSTOMERID");
				int stockId = res.getInt("STOCKID");
				int amount = res.getInt("AMOUNT");
				double price = res.getDouble("PRICE");
                                String stockName = res.getString("STOCK_NAME");
				String customer = res.getString("FIRSTNAME") + " "
						+ res.getString("LASTNAME");
                                String firmName = res.getString("FIRM_NAME");
                                
				Order order = new Order();
                                
				order.setOrderId(orderId);
				order.setTypeId(typeId);
				order.setBrokerId(brokerId);
				order.setCustomerId(customerId);
				order.setStockId(stockId);
				order.setAmount(amount);
				order.setPrice(price);
                                order.setDisplayCustomerName(customer);
                                order.setDisplayStockName(stockName);
                                order.setDisplayFirmName(firmName);


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
		String query = "SELECT O.*, CONCAT(CI.FIRSTNAME, ' ', CI.LASTNAME), S.NAME"
                     + " FROM ORDERS_M as O, CUSTOMER_INFO as CI,  STOCKS as S"
				     + " WHERE O.CUSTOMERID = CI.ID"
				     + " AND O.STOCKID = S.ID"
				     + " AND O.ORDERID = ?";

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
			int statusId = res.getInt(8);
	        String displayCustomerName = res.getString(9);
	        String displayStockName = res.getString(10);
			
			order.setOrderId(orderId);
			order.setTypeId(typeId);
			order.setBrokerId(brokerId);
			order.setCustomerId(customerId);
			order.setStockId(stockId);
			order.setAmount(amount);
			order.setPrice(price);
			order.setStatusId(statusId);
			order.setDisplayCustomerName(displayCustomerName);
			order.setDisplayStockName(displayStockName);

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
		} catch (Exception e) {
			return null;
		}

		return order;
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

		String query = "UPDATE ORDERS_M SET TYPEID = ?, BROKERID = ?, CUSTOMERID = ?, STOCKID = ?, AMOUNT = ?, PRICE = ?, STATUSID = ? WHERE ORDERID = ?";

		try {
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, order.getTypeId());
			st.setInt(2, order.getBrokerId());
			st.setInt(3, order.getCustomerId());
			st.setInt(4, order.getStockId());
			st.setInt(5, order.getAmount());
			st.setDouble(6, order.getPrice());
			st.setInt(7, order.getStatusId());
			st.setInt(8, idToUpdate);

			int affectedRows = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not update the table");
				return v;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			v.setVerified(false);
			v.setStatus("Could not update the table");
			return v;
		}

		v.setVerified(true);
		v.setStatus("Success");
		return v;
	}

	/*
	 * This function updates an order
	 */
	public Validator deleteOrder(int idToDelete) {
		Validator v = new Validator();
		PreparedStatement st = null;
		ResultSet rs = null;

		String query = "DELETE FROM ORDERS_M WHERE ORDERID = ?";

		try {
			Order o = selectOrder(idToDelete);
			
			st = this.con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, idToDelete);

			int affectedRows = st.executeUpdate();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());

			if (affectedRows == 0) {
				v.setVerified(false);
				v.setStatus("Could not delete record");
				return v;
			} else {
				// return pending balance
				double pendingBalance = -1 * o.getPrice() * o.getAmount();
				CustomerInfo c = selectCustomerInfo(o.getCustomerId());
				lockAmountOnCustomerAccount(c, pendingBalance);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			v.setVerified(false);
			v.setStatus("Could not delete record");
			return v;
		}

		v.setVerified(true);
		v.setStatus("Success");
		return v;
	}
	
	
	/**
	 * Place selling order
	 */
	public Validator placeSellingOrder(Order o, int lBoundPercent, int uBoundPercent, int tradingSessionID) {
		Validator v = new Validator();
		
		// 0. Input validation
		Validator check0 = o.validate();
		if(!check0.isVerified()) {
			v.setVerified(false);
			v.setStatus(check0.getStatus());
			return v;			
		}
		
		// 0.5 check if this firm has access to this session
		int firmId = selectBrokerageFirmForBroker(o.getBrokerId());
		boolean check05 = checkBrokerageFirmTransactionPermission(tradingSessionID, firmId);
		if(!check05) {
			v.setVerified(false);
			v.setStatus("Error. Your Brokerage Firm cannot participate in this Trading Session");
			return v;
		}
		
		// 1. check if broker has access to customer
		Validator check1 = hasBrokerCustomer(o.getBrokerId(), o.getCustomerId());
		if(!check1.isVerified()) {
			v.setVerified(false);
			v.setStatus(check1.getStatus());
			return v;
		}

		// 2. check if this customer possesses the stock
		boolean check2 = isHasCustomerStock(o.getCustomerId(), o.getStockId());
		if(!check2) {
			v.setVerified(false);
			v.setStatus("This customer does not have selected stock");
			return v;
		}
		
		// 3. check if this customer possesses enough amount
		int curAmount = selectHasCustomerStockAmount(o.getCustomerId(), o.getStockId());
		int pendingAmount = getCustomerStockQuantityPending(o.getCustomerId(), o.getStockId());
		int availableAmount = curAmount - pendingAmount;
		
		if(o.getAmount() > availableAmount) {
			v.setVerified(false);
			v.setStatus("This customer does not have enough amount of selected stock");
			return v;
		}
		
		// 4. check if the price is in bounds
		Stock s = selectStock(o.getStockId());
		Validator check4 = priceInBounds(s.getPrice(), o.getPrice(), lBoundPercent, uBoundPercent);
		if(!check4.isVerified()) {
			v.setVerified(false);
			v.setStatus(check4.getStatus());
			return v;
		}
		
		// 5. check if we can match the order right away
		int matchedOrderId = isThereMatch(o);
		if(matchedOrderId == -1) {
			// no match, insert order to db
			v = insertNewOrder(o);
			return v;
		} else {
			// there is a match
			makeTransaction(o, matchedOrderId, tradingSessionID);
			v.setVerified(true);
			v.setStatus("Order has been matched");
			return v;
		}
	}

	
	/**
	 * Returns false with message in case broker is not associated with customer
	 */
	public Validator hasBrokerCustomer(int brokerId, int customerId) {
		Validator v = new Validator();
		
		// get firmId for this broker
		int firmId = selectBrokerageFirmForBroker(brokerId);
		if(firmId == 0) {
			v.setVerified(false);
			v.setStatus("Could not identify firm for this broker");
			return v;
		}

		// check if this firm can manage this customer
		if(!isHasFirmCustomer(firmId, customerId)) {
			v.setVerified(false);
			v.setStatus("This company cannot manage this customer");
			return v;
		}

		v.setVerified(true);
		return v;	
	}
	
	/**
	 * Returns brokerage firmId for this customer
	 */
	public int selectBrokerageFirmForBroker(int brokerId) {
		int firmId = 0;		
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT FIRMID FROM USERS WHERE ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, brokerId);
			ResultSet res = st.executeQuery();

			res.next();
			firmId = res.getInt(1);

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			return firmId;
		
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Returns true if this firm and customer relationship exists
	 */
	public boolean isHasFirmCustomer(int firmId, int customerId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM HAS_FIRM_CUSTOMERS WHERE FIRMID = ? AND CUSTOMERID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, firmId);
			st.setInt(2, customerId);
			ResultSet res = st.executeQuery();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
			if(res.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Returns true if this customer has this stock in posession
	 */
	public boolean isHasCustomerStock(int customerId, int stockId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM HAS_CUSTOMERS_STOCKS WHERE CUSTOMERID = ? AND STOCKID = ? AND STATUSID = 1";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			st.setInt(2, stockId);
			ResultSet res = st.executeQuery();

			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
			if(res.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Returns amount this customer has of this stock
	 */
	public int selectHasCustomerStockAmount(int customerId, int stockId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT AMOUNT FROM HAS_CUSTOMERS_STOCKS WHERE CUSTOMERID = ? AND STOCKID = ? AND STATUSID = 1";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			st.setInt(2, stockId);
			ResultSet res = st.executeQuery();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
			if(res.next()) {
				return res.getInt(1);
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns false with message in case the target price is out of specified bounds
	 */
	public Validator priceInBounds(double curPrice, double targetPrice, int lBoundPercent, int uBoundPercent) {
		Validator v = new Validator();

		double lBound = curPrice * (1 - ( lBoundPercent / 100.00 ));
		double uBound = curPrice * (1 + ( uBoundPercent / 100.00 ));

		if(targetPrice < lBound) {
			v.setVerified(false);
			v.setStatus("Price cannot be lower than " + String.format("%.2f", lBound));
			return v;
		}

		if(targetPrice > uBound) {
			v.setVerified(false);
			v.setStatus("Price cannot be higher than " + String.format("%.2f", uBound));
			return v;
		}

		v.setVerified(true);
		return v;
	}	
	
	
	/**
	 * Place buying order
	 */
	public Validator placeBuyingOrder(Order o, int lBoundPercent, int uBoundPercent, int tradingSessionID) {
		Validator v = new Validator();
	
		// 0. Input validation
		Validator check0 = o.validate();
		if(!check0.isVerified()) {
			v.setVerified(false);
			v.setStatus(check0.getStatus());
			return v;			
		}

		// 0.5 check if this firm has access to this session
		int firmId = selectBrokerageFirmForBroker(o.getBrokerId());
		boolean check05 = checkBrokerageFirmTransactionPermission(tradingSessionID, firmId);
		if(!check05) {
			v.setVerified(false);
			v.setStatus("Error. Your Brokerage Firm cannot participate in this Trading Session");
			return v;
		}

		// 1. check if broker has access to customer
		Validator check1 = hasBrokerCustomer(o.getBrokerId(), o.getCustomerId());
		if(!check1.isVerified()) {
			v.setVerified(false);
			v.setStatus(check1.getStatus());
			return v;
		}

		// 2. check if the price is in bounds
		Stock s = selectStock(o.getStockId());
		Validator check2 = priceInBounds(s.getPrice(), o.getPrice(), lBoundPercent, uBoundPercent);
		if(!check2.isVerified()) {
			v.setVerified(false);
			v.setStatus(check2.getStatus());
			return v;
		}

		// 3. check if this customer has enough money to buy this amount of this stock
		CustomerInfo customer = selectCustomerInfo(o.getCustomerId());
		
		double customerBalance = customer.getBalance() - customer.getPendingBalance();
		double requiredBalance = o.getPrice() * o.getAmount();

		if(customerBalance < requiredBalance) {
			v.setVerified(false);
			v.setStatus("Insufficient balance to place this order");
			return v;			
		}
		
		// 4. lock money
		Validator check7 = lockAmountOnCustomerAccount(customer, requiredBalance);
		if(!check7.isVerified()) {
			v.setVerified(false);
			v.setStatus("Locking money failed");
			return v;
		}

		// 5. check if we can match the order right away
		int matchedOrderId = isThereMatch(o);
		if(matchedOrderId == -1) {
			// no match, insert order to db
			v = insertNewOrder(o);
			return v;
		} else {
			// there is a match
			makeTransaction(o, matchedOrderId, tradingSessionID);
			v.setVerified(true);
			v.setStatus("Order has been matched");
			return v;
		}					
	}

	
	public Validator lockAmountOnCustomerAccount(CustomerInfo c, double amount) {

		double pendingSoFar = c.getPendingBalance();
		pendingSoFar += amount;
		c.setPendingBalance(pendingSoFar);

		return updateCustomerInfo(c.getId(), c);
	}
	
	public int getActiveTradingSessionID(){
		int tradingSessionID = -1;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM TRADINGSESSIONS where ACTIVE = 1";

		try {
			st = this.con.prepareStatement(query);
			
			rs = st.executeQuery();

			 if (rs.next()){
				 tradingSessionID = rs.getInt("ID");
			 }
			
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
	
		
		return tradingSessionID;
	}
	
	
	
	public int getCustomerStockQuantityPending(int customerId, int stockId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT sum(O.AMOUNT) "
				     + " FROM ORDERS_M as O"
				     + " WHERE O.CUSTOMERID = ?"
				      + " AND O.STOCKID = ?"
				      + " AND O.TYPEID = 1";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			st.setInt(2, stockId);
			ResultSet res = st.executeQuery();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
			if(res.next()) {
				return res.getInt(1);
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean checkBrokerageFirmTransactionPermission(int tsId, int firmId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String query = "SELECT ID FROM HAS_TS_BROKERAGEFIRMS WHERE FIRM_ID = ? AND TS_ID = ?";

		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, firmId);
			st.setInt(2, tsId);
			ResultSet res = st.executeQuery();
			
			StockTradingServer.LoggerCustom logger = new StockTradingServer.LoggerCustom();
			logger.logDatabaseActivity(st.toString());
			
			if(res.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<Stock> selectCustomerStocksLimited(int customerId, int tsId) {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		PreparedStatement st = null;
		String query = "SELECT STOCKS.*, HAS_CUSTOMERS_STOCKS.AMOUNT"
				     + " FROM STOCKS, HAS_CUSTOMERS_STOCKS , HAS_TS_STOCKS"
				     + " WHERE STOCKS.ID = HAS_CUSTOMERS_STOCKS.STOCKID "
				     + " AND HAS_TS_STOCKS.STOCK_ID = STOCKS.ID"
				     + " AND HAS_CUSTOMERS_STOCKS.CUSTOMERID = ?"
				     + " AND HAS_TS_STOCKS.TS_ID = ?"
				     + " ORDER BY STOCKS.NAME";
		
		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, customerId);
			st.setInt(2, tsId);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Stock s = new Stock();
				s.setId(res.getInt(1));
				s.setName(res.getString(2));
				s.setPrice(res.getInt(4));
				s.setStatusId(res.getInt(5));
				s.setAmount(res.getInt(6));

				stocks.add(s);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stocks;
	}

	public ArrayList<Stock> selectBuyerCustomerStocksLimited(int tsId) {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		PreparedStatement st = null;
		String query = "SELECT STOCKS.*"
				+ " FROM STOCKS, HAS_TS_STOCKS"
				+ " WHERE HAS_TS_STOCKS.STOCK_ID = STOCKS.ID"
				+ " AND HAS_TS_STOCKS.TS_ID = ?"
				+ " ORDER BY STOCKS.NAME";
		
		try {
			st = this.con.prepareStatement(query);
			st.setInt(1, tsId);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Stock s = new Stock();
				s.setId(res.getInt(1));
				s.setName(res.getString(2));
				s.setPrice(res.getInt(4));
				s.setStatusId(res.getInt(5));

				stocks.add(s);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(DatabaseConnector.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}

		return stocks;
	}
	
	
	
	
	
}
