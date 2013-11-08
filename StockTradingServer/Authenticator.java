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

				// generate activation code, send an email
				// Code goes here

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

}
