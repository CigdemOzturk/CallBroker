package com.ica.persistence.dao.sqllite;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class DatabaseDefinition 
{
	// Database entities description
	// Contact
	public static final String TABLE_CONTACT = "Contact";
	public static final String CONTACT_COLUMN_ID = "_id";
	public static final String CONTACT_COLUMN_NAME = "name";
	public static final String CONTACT_PHONE_NUMBER = "phoneNumber";
	
	// ContactGroup
	public static final String TABLE_CONTACT_GROUP = "ContactGroup";
	public static final String CONTACT_GROUP_COLUMN_ID = "_id";
	public static final String CONTACT_GROUP_COLUMN_NAME ="name";
	
	// Contact_ContactGroup
	public static final String TABLE_CONTACT_CONTACT_GROUP = "Contact_ContactGroup";
	public static final String CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT = "idContact";
	public static final String CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP = "idContactGroup";
	
	// Command [Inheritance strategy: all-in-one-table]
	public static final String TABLE_COMMAND = "Command";
	public static final String COMMAND_COLUMN_ID = "_id";
	public static final String COMMAND_ID_CONTACT_GROUP = "idContactGroup";
	public static final String COMMAND_ID_CONTACT_GROUP_RECEIVER = "idContactGroupReceiver";
	public static final String COMMAND_TEXT_MESSAGE = "textMessage";
	public static final String COMMAND_RING_MODE = "ringMode"; // "Silent", "Vibrate", "Ringing"
	public static final String COMMAND_TYPE = "type"; // "AcceptCall", "RejectCall", "IgnoreCall", "SendSms", "RingMode"
	
	// Policy
	public static final String TABLE_POLICY = "Policy";
	public static final String POLICY_COLUMN_ID = "_id";
	public static final String POLICY_COLUMN_NAME = "name";
	public static final String POLICY_ACTIVE = "active";
	
	// Policy_ContactGroup
	public static final String TABLE_POLICY_CONTACT_GROUP = "Policy_ContactGroup";
	public static final String POLICY_CONTACT_GROUP_COLUMN_ID_POLICY = "idPolicy";
	public static final String POLICY_CONTACT_GROUP_ID_CONTACT_GROUP = "idContactGroup";
	
	// TimeStamp -Weak entity-
	public static final String TABLE_TIME_STAMP = "TimeStamp";
	public static final String TIME_STAMP_COLUMN_ID = "_id"; // Primary key
	public static final String TIME_STAMP_COLUMN_ID_POLICY = "idPolicy";
	public static final String TIME_STAMP_COLUMN_DAY = "day"; // [0...6]
	public static final String TIME_STAMP_COLUMN_FROM_HOUR = "fromHour";
	public static final String TIME_STAMP_COLUMN_FROM_MINUTE = "fromMinute";
	public static final String TIME_STAMP_COLUMN_TO_HOUR = "toHour";
	public static final String TIME_STAMP_COLUMN_TO_MINUTE = "toMinute";
	
	// Caller History
	public static final String TABLE_CALL_HISTORY = "Call_History";
	public static final String CALL_HISTORY_CALLER_ID = "call_id"; // Primary key
	public static final String CALL_HISTORY_CALLER_NAME = "caller_name";
	public static final String CALL_HISTORY_CALLER_NUMBER = "caller_number";
	public static final String CALL_HISTORY_CALLER_TIME_PERIOD = "caller_time_period";
	public static final String CALL_HISTORY_CALLER_DATE = "caller_date";
	
	// Call
	public static final String TABLE_CALL_FREQUENCY = "Call_Frequencies";
	public static final String CALL_FREQUENCY_ID = "id";
	public static final String CALL_FREQUENCY_FIFTEEN = "fifteen";
	public static final String CALL_FREQUENCY_THIRTY = "thirty";
	public static final String CALL_FREQUENCY_DAYPART = "daypart";
	
	public static final String TABLE_CALLERS = "Callers";
	public static final String CALLER_ID = "id";
	public static final String CALLER_NUMBER = "caller_number";
	
	
	public static final String DATABASE_NAME = "iCa.db";
	public static final int DATABASE_VERSION = 1;
	
	// Creation tables
	public static final String CREATE_TABLE_POLICY = "CREATE TABLE POLICY (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, active INTEGER NOT NULL);";
	public static final String CREATE_TABLE_CONTACT_GROUP = "CREATE TABLE ContactGroup (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);";
	public static final String CREATE_TABLE_POLICY_CONTACT_GROUP = "CREATE TABLE Policy_ContactGroup (idPolicy INTEGER, idContactGroup INTEGER, PRIMARY KEY(idPolicy, idContactGroup), FOREIGN KEY(idContactGroup) REFERENCES ContactGroup(_id), FOREIGN KEY(idPolicy) REFERENCES Policy(_id));";
	public static final String CREATE_TABLE_COMMAND = "CREATE TABLE Command (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, textMessage TEXT, ringMode TEXT, idContactGroup INTEGER, idContactGroupReceiver INTEGER, FOREIGN KEY (idContactGroup) REFERENCES ContactGroup (_id), FOREIGN KEY(idContactGroupReceiver) REFERENCES ContectGroup(_id));";
	public static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, phoneNumber TEXT NOT NULL);";
	public static final String CREATE_TABLE_CONTACT_CONTACT_GROUP = "CREATE TABLE Contact_ContactGroup (idContact INTEGER, idContactGroup INTEGER, PRIMARY KEY (idContact, idContactGroup), FOREIGN KEY (idContactGroup) REFERENCES ContactGroup (_id), FOREIGN KEY (idContact) REFERENCES Contact (_id));";
	public static final String CREATE_TABLE_TIMESTAMP = "CREATE TABLE TimeStamp (_id INTEGER PRIMARY KEY AUTOINCREMENT, day INTEGER NOT NULL, fromHour INTEGER NOT NULL, fromMinute INTEGER NOT NULL, toHour INTEGER NOT NULL, toMINUTE INTEGER NOT NULL, idPolicy INTEGER, FOREIGN KEY(idPolicy) REFERENCES Policy (_id), CHECK (day >= 0 AND day <= 6));";
	public static final String CREATE_TABLE_CALL_HISTORY = "CREATE TABLE Call_History ( call_id INTEGER PRIMARY KEY AUTOINCREMENT, caller_name TEXT NOT NULL, caller_number TEXT NOT NULL, caller_time_period  TEXT NOT NULL, caller_date TEXT NOT NULL);";
	public static final String CREATE_TABLE_CALL_FREQUENCY = "CREATE TABLE Call_Frequencies( id INTEGER PRIMARY KEY AUTOINCREMENT, fifteen INTEGER NOT NULL, thirty INTEGER NOT NULL, daypart  TEXT NOT NULL);";
	public static final String CREATE_TABLE_CALLERS = "CREATE TABLE Callers( id INTEGER PRIMARY KEY AUTOINCREMENT, caller_number TEXT NOT NULL);";
}
