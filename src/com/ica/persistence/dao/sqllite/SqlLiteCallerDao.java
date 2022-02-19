package com.ica.persistence.dao.sqllite;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ica.android.service.Caller;
import com.ica.persistence.interfaces.CallerDao;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteCallerDao extends SqlLiteGenericDao<Caller> implements CallerDao
{
	public SqlLiteCallerDao(SQLiteDatabase db) 
	{
		super(db);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getTableName() 
	{
		return DatabaseDefinition.TABLE_CALL_HISTORY;
	}

	@Override
	protected Caller toObject(Cursor cursor) 
	{
		Long id 				= cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.CALL_HISTORY_CALLER_ID));
		String name 			= cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CALL_HISTORY_CALLER_NAME));
		String phoneNumber 		= cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CALL_HISTORY_CALLER_NUMBER));
		String callTimePeriod 	= cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CALL_HISTORY_CALLER_TIME_PERIOD));
		String callDate 		= cursor.getString(cursor.getColumnIndex(DatabaseDefinition.CALL_HISTORY_CALLER_DATE)); 
		return new Caller(id, name, phoneNumber, callTimePeriod, callDate);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public List<Caller> findAllMatching(String number) 
	{
		List<Caller> list = new ArrayList<Caller>();
		//Cursor cursor = db.rawQuery("SELECT * FROM " + getTableName(), null);
		Cursor cursor = db.rawQuery("SELECT * FROM " + getTableName() + " WHERE caller_date ='" + new SimpleDateFormat("dd/M/yyyy").format(new Date()) + "' and caller_number = '" + number + "'", null);
		
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
		return db.insert(getTableName(), null, values);
	}

	@Override
	public void updateContact(long id) 
	{
		// TODO Auto-generated method stub
	}
	
}
