package StockTradingServer;

import java.io.Serializable;

import StockTradingCommon.Enumeration;

public class Authenticator implements Serializable {

	public Validator checkIfUsernamePasswordMatch(String email, String plainPass) {
		// validate input
		Validator result = validateEmailAndPlainInput(email, plainPass);
		if (!result.isVerified()) {
			return result;
		}

		DatabaseConnector db = new DatabaseConnector();

		// validate email
		result = db.verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		InputValidation iv = new InputValidation();
		PasswordHasher hasher = new PasswordHasher();

		// get this user limited info from the database
		User user = db.selectUserByEmailLimited(email);

		String dbHash = user.getPassword();
		String dbSalt = user.getSalt();
		int statusId = user.getStatusId();
		int falseLogins = user.getFalseLogins();
		int id = user.getId();

		// 1. check if this user is active
		if (statusId != Enumeration.User.USER_STATUSID_ACTIVE) {
			result.setVerified(false);
			result.setStatus("Error, cannot login, this user account has been locked");
			return result;
		}

		String plainHash = hasher.sha512(plainPass, dbSalt);

		// 2. if entered password is correct, return true with welcome message
		if (plainHash.equals(dbHash)) {

			db.updateDatabaseIntField("USERS", "ID", "FALSELOGINS", id, 0);
			db.unsetActivationCodeAndTempPassword(id);
			result.setVerified(true);
			result.setStatus("Welcome to Stocks Trading System");

			return result;
		} else {
			// 3. else record the failed login attempt
			int newFalseLogins = falseLogins + 1;
			db.updateDatabaseIntField("USERS", "ID", "FALSELOGINS", id,
					newFalseLogins);

			// if we reached the max of failed logins, lock the account, sent an
			// email
			if (newFalseLogins == Enumeration.User.USER_MAX_LOGIN_ATTEMPTS) {
				// lock
				db.updateDatabaseIntField("USERS", "ID", "STATUSID", id,
						Enumeration.User.USER_STATUSID_LOCKED);
				
				// Code goes here
				db.setActivationCode(id);

				// generate activation code, send an email

				
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

	public Validator validateEmailPasswordCodeInput(String email, String plainPass, String plainCode) {
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
	public Validator checkIfUsernamePasswordActivationCodeMatch(String email, String plainPass, String plainCode) {
		// validate input
		Validator result = validateEmailPasswordCodeInput(email, plainPass, plainCode);
		if (!result.isVerified()) {
			return result;
		}

		DatabaseConnector db = new DatabaseConnector();

		// validate email
		result = db.verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		PasswordHasher hasher = new PasswordHasher();
		User u = db.selectUserByEmailLimited(email);
		String plainPassHashed = hasher.sha512(plainPass, u.getSalt());
		String plainCodeHashed = hasher.sha512(plainCode, u.getActivationCodeSalt());
		
		if(plainPassHashed.equals(u.getPassword()) && plainCodeHashed.equals(u.getActicationCode())) {
			// clean the codes
			db.unsetActivationCodeAndTempPassword(u.getId());

			// reset false logins
			db.updateDatabaseIntField("USERS", "ID", "FALSELOGINS", u.getId(), 0);
			
			// unlock the user
			db.updateDatabaseIntField("USERS", "ID", "STATUSID", u.getId(),
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
	public Validator checkIfUsernameTempPasswordActivationCodeMatch(String email, String plainTempPass, String plainCode) {
		// validate input
		Validator result = validateEmailPasswordCodeInput(email, plainTempPass, plainCode);
		if (!result.isVerified()) {
			return result;
		}

		DatabaseConnector db = new DatabaseConnector();

		// validate email
		result = db.verifyUserEmail(email);
		if (!result.isVerified()) {
			return result;
		}

		PasswordHasher hasher = new PasswordHasher();
		User u = db.selectUserByEmailLimited(email);
		String plainTempPassHashed = hasher.sha512(plainTempPass, u.getTempPasswordSalt());
		String plainCodeHashed = hasher.sha512(plainCode, u.getActivationCodeSalt());
		
		if(plainTempPassHashed.equals(u.getTempPassword()) && plainCodeHashed.equals(u.getActicationCode())) {
			// clean the codes
			db.unsetActivationCodeAndTempPassword(u.getId());
			
			// unlock
			db.updateDatabaseIntField("USERS", "ID", "STATUSID", u.getId(),
					Enumeration.User.USER_STATUSID_ACTIVE);
			
			// reset false logins
			db.updateDatabaseIntField("USERS", "ID", "FALSELOGINS", u.getId(), 0);

			
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
			
		if(!v.isVerified()) {
			return v;
		}

		// validate email
		DatabaseConnector db = new DatabaseConnector();
		v = db.verifyUserEmail(email);
		if (!v.isVerified()) {
			return v;
		}
	
		// check user status
		User u = db.selectUserByEmailLimited(email);
		
		
		if (u.getStatusId() != Enumeration.User.USER_STATUSID_ACTIVE) {
			v.setVerified(false);
			v.setStatus("Error, this user account has been locked");
			return v;
		}
		
		Authenticator auth = new Authenticator();
		db.setActivationCodeAndTempPassword(u.getId());
		
		v.setVerified(true);
		v.setStatus("Activation code and temporary passwod had been sent to your mail box");

		return v;
	}
	
	
	
	
	
}
