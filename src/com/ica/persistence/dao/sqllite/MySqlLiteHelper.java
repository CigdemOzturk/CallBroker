package com.ica.persistence.dao.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Cigdem
 */

public class MySqlLiteHelper extends SQLiteOpenHelper
{
	public MySqlLiteHelper(Context context)
	{
	super(context, DatabaseDefinition.DATABASE_NAME, null, DatabaseDefinition.DATABASE_VERSION);
	}
	
	@ Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DatabaseDefinition.CREATE_TABLE_POLICY);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CONTACT_GROUP);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_POLICY_CONTACT_GROUP);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_COMMAND);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CONTACT);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CONTACT_CONTACT_GROUP);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_TIMESTAMP);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CALL_HISTORY);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CALL_FREQUENCY);
		db.execSQL(DatabaseDefinition.CREATE_TABLE_CALLERS);
	}
	
	@ Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(MySqlLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_TIME_STAMP);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_CONTACT);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_COMMAND);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_CONTACT_GROUP);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_POLICY);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.TABLE_CALL_HISTORY);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.CREATE_TABLE_CALL_FREQUENCY);
		db.execSQL("DROP TABLE IF EXIST " + DatabaseDefinition.CREATE_TABLE_CALLERS);
		onCreate(db);
	}
}
