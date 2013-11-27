/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingCommon;

/**
 * @date : Oct 21, 2013
 * @author : Hirosh Wickramasuriya
 */
public class Enumeration {

	public static class Database {
		public final static String DB_REQUEST_INITIATED = "Please Wait, your requst is in progress...";

		public final static String DB_INSERT_SUCCESS = "Record inserted successfully.";
		public final static String DB_INSERT_FAILURE = "Failed to insert a record.";

		public final static String DB_UPDATE_SUCCESS = "Record updated successfully.";
		public final static String DB_UPDATE_FAILURE = "Failed to update a record.";
	}

	public static class UserRole {
		public final static int USER_ROLE_ADMIN = 1;
		public final static int USER_ROLE_BROKER = 2;
	}

	public static class BrokerageFirm {
		public final static int BROKERAGE_FIRM_STRING_LENGTH = 128;
		public final static int BROKERAGE_FIRM_STATUS_ACTIVE_ID = 1;
		public final static int BROKERAGE_FIRM_STATUS_INACTIVE_ID = 2;
                public final static int BROKERAGE_FIRM_ID_FOR_ADMIN = 19; // id of the dummy brokerage firm

	}

	public static class OrderType {
		public final static int BUYING_ORDER = 1;
		public final static int SELLING_ORDER = 2;
	}

	public static class Broker {
		public final static int BROKER_SELECT_PARAM_EMPTY = 0;
		public final static String BROKER_ENCRYPT_IV = "ABAFACAFAA5ABBAA";
		public final static String BROKER_ENCRYPT_KEY = "0123456789abcdef";
	}

	public static class InputValidation {
		public final static String INPUT_VALIDATION_INVAILD_DOUBLE_FORMAT = "Invalid number format, should be #,##.0.00";
	}

	public static class User {
		public final static int USER_STATUSID_ACTIVE = 1;
		public final static int USER_STATUSID_LOCKED = 3;

		public final static int USER_MAX_LOGIN_ATTEMPTS = 3;

	}

	public static class Security {
		public final static int PASSWORD_MIN_LENGTH = 8;
		public final static int PASSWORD_DEFAULT_LENGTH = 12;
		public final static int PASSWORD_MIN_LOWER = 2;
		public final static int PASSWORD_MIN_UPPER = 2;
		public final static int PASSWORD_MIN_DIGIT = 2;
		public final static int PASSWORD_MIN_SPECL = 0;

	}

	public static class PasswordGrade {
		public final static int PASSWORD_STRENGTH_VERYWEAK = 1;
		public final static int PASSWORD_STRENGTH_WEAK = 2;
		public final static int PASSWORD_STRENGTH_GOOD = 3;
		public final static int PASSWORD_STRENGTH_STRONG = 4;
		public final static int PASSWORD_STRENGTH_VERYSTRONG = 5;
	}

	public static class Strings {
		public final static String ACCOUNT_LOCKED_SUBJECT = "Stocks Trading Account locked";
		public final static String ACCOUNT_LOCKED_MESSAGE = "Due to exceeded number of unsuccessful attempts to login, your account has been locked. Please use the following activation code along with your current password to unlock your account.\n\nActivation code: ";
		public final static String ACCOUNT_FRGTN_SUPER_SUBJECT = "Attention Supervisor: Stocks Trading Account Password Recovery";
		public final static String ACCOUNT_FRGTN_BROKER_SUBJECT = "Stocks Trading Account Password Recovery";

	}
        
        public static class Password        {
            public final static int PASSWORD_HISTORY_COUNT = 3;
        }
        
        public static class Status
        {
            public final static int ALL = 0;
            public final static int ACTIVE = 1;
            public final static int INACTIVE = 2;
        }
}
