package com.ica.persistence.dao.sqllite;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ica.android.service.Call;
import com.ica.android.service.Caller;
import com.ica.android.service.MyPhoneStateListener;
import com.ica.persistence.interfaces.CallDao;
import com.ica.persistence.interfaces.CallerDao;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteCallDao extends SqlLiteGenericDao<Call> implements CallDao
{
	public SqlLiteCallDao(SQLiteDatabase db) 
	{
		super(db);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getTableName() 
	{
		return DatabaseDefinition.TABLE_CALL_FREQUENCY;
	}

	@Override
	protected Call toObject(Cursor cursor) 
	{
		Long id 		= cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CALL_FREQUENCY_ID));
		int fifteen 	= cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.CALL_FREQUENCY_FIFTEEN));
		int thirty 		= cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.CALL_FREQUENCY_THIRTY));
		String daypart 	= cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CALL_FREQUENCY_DAYPART));
		 
		return new Call(id, MyPhoneStateListener.incomingNum, fifteen, thirty, daypart);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public List<Call> findCall(String number) 
	{
		List<Call> list = new ArrayList<Call>();
		Cursor cursor2 = db.rawQuery("SELECT id FROM Callers WHERE caller_number = '" + number + "'", null);
		cursor2.moveToFirst();
		String id = Long.toString(cursor2.getLong(cursor2.getColumnIndex(DatabaseDefinition.CALLER_ID)));
		Cursor cursor = db.rawQuery("SELECT * FROM " + getTableName() + " WHERE id = " + id, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				list.add(toObject(cursor));
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	@Override
	public long insert(ContentValues values) 
	{
		// Check if the number is already in db
		Cursor cursor = db.rawQuery("SELECT id FROM " + DatabaseDefinition.TABLE_CALLERS + " where caller_number = '" + MyPhoneStateListener.incomingNum + "'", null);
				
		int rows = cursor.getCount();
		if (rows > 0)
		{
			cursor.moveToFirst();
			String id = Long.toString(cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CALLER_ID)));
			
			db.delete(DatabaseDefinition.TABLE_CALLERS, DatabaseDefinition.CALLER_NUMBER + "='" + MyPhoneStateListener.incomingNum + "'", null);
			db.delete(getTableName(), DatabaseDefinition.CALL_FREQUENCY_ID + " = " + id, null);	
		}
	
		long insertedID = db.insert(getTableName(), null, values);
		ContentValues val = new ContentValues();
		val.put(DatabaseDefinition.CALLER_ID, insertedID);
		val.put(DatabaseDefinition.CALLER_NUMBER, MyPhoneStateListener.incomingNum);
		return db.insert(DatabaseDefinition.TABLE_CALLERS, null, val);
	}

	@Override
	public void removeFromGroup(String phoneNumber) 
	{
		// Remove from the group
		Cursor cursor = db.rawQuery("SELECT "  + DatabaseDefinition.CONTACT_COLUMN_ID  + " FROM " + DatabaseDefinition.TABLE_CONTACT + " where " + DatabaseDefinition.CONTACT_PHONE_NUMBER + " = '" + phoneNumber + "'", null);
		int rows = cursor.getCount();
		if (rows > 0)
		{
			cursor.moveToFirst();
			String id = Long.toString(cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CONTACT_COLUMN_ID)));
			db.delete(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, DatabaseDefinition.CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT + " = " + id, null);			
		}
		
		// Delete from Call frequencies
		Cursor cursor2 = db.rawQuery("SELECT " + DatabaseDefinition.CALLER_ID + " FROM " + DatabaseDefinition.TABLE_CALLERS +  " where " + DatabaseDefinition.CALLER_NUMBER  + " = '" + MyPhoneStateListener.incomingNum + "'", null);
		int rows2 = cursor2.getCount();
		cursor2.moveToFirst();
		String id2 = Long.toString(cursor2.getLong(cursor2.getColumnIndex(DatabaseDefinition.CALLER_ID)));
		db.delete(DatabaseDefinition.TABLE_CALL_HISTORY, DatabaseDefinition.CALL_HISTORY_CALLER_NUMBER + " = '" + MyPhoneStateListener.incomingNum + "'", null);
		db.delete(DatabaseDefinition.TABLE_CALLERS, DatabaseDefinition.CALLER_ID + "= " + id2, null);
		db.delete(getTableName(), DatabaseDefinition.CALL_FREQUENCY_ID + " = " + id2, null);
		

//		Add to urgents list
		
		//String groupName = "Urgents";
		//addToGroup(id, groupName);
	}

	@Override
	public void addToGroup(String id, String groupName) 
	{
		Cursor cursor = db.rawQuery("SELECT "  +  DatabaseDefinition.CONTACT_GROUP_COLUMN_ID + " FROM " + DatabaseDefinition.TABLE_CONTACT_GROUP + " where " + DatabaseDefinition.CONTACT_GROUP_COLUMN_NAME + " = '" + groupName + "'", null);
		cursor.moveToFirst();
		String group_id = Long.toString(cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CONTACT_GROUP_COLUMN_ID)));
		ContentValues values = new ContentValues();
		values.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_COLUMN_ID_CONTACT, id);
		values.put(DatabaseDefinition.CONTACT_CONTACT_GROUP_ID_CONTACT_GROUP, group_id);
		db.insert(DatabaseDefinition.TABLE_CONTACT_CONTACT_GROUP, null, values);
		
	}
}
