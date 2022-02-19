package com.ica.persistence.dao.sqllite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ica.model.policy.time.TimeStamp;
import com.ica.persistence.interfaces.TimeStampDao;
import com.ica.util.time.TimeUtils;
import com.ica.util.time.TimeUtils.DAYS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteTimeStampDao extends SqlLiteGenericDao<TimeStamp> implements TimeStampDao
{
	public SqlLiteTimeStampDao(SQLiteDatabase db)
	{
		super(db);
	}
	
	@Override
	protected String getTableName()
	{
		return DatabaseDefinition.TABLE_TIME_STAMP;
	}
	
	@Override
	protected TimeStamp toObject(Cursor cursor)
	{
		Long id = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_ID));
		int fromHour = cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_HOUR));
		int toHour = cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_TO_HOUR));
		int fromMinute = cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_FROM_MINUTE));
		int toMinute = cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_TO_MINUTE));
		
		TimeStamp timeStamp = new TimeStamp(fromHour, fromMinute, toHour, toMinute);
		timeStamp.setId(id);
		return timeStamp;
	}
	
	public HashMap<DAYS, List<TimeStamp>> findAllInPolicy(long id)
	{
		HashMap<DAYS, List<TimeStamp>> res = new HashMap<DAYS, List<TimeStamp>>();
			
		for(DAYS day : DAYS.values())
		{
			res.put(day, new ArrayList<TimeStamp>());
		}
		Cursor cursor = db.query(true, getTableName(), null, DatabaseDefinition.TIME_STAMP_COLUMN_ID_POLICY + " = " + id, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				DAYS day = TimeUtils.intToDay(cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.TIME_STAMP_COLUMN_DAY)));
				TimeStamp ts = new TimeStamp(cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
				res.get(day).add(ts);
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return res;
	}
	
	public boolean deleteAllFromPolicy(long id)
	{
		return db.delete(getTableName(), DatabaseDefinition.TIME_STAMP_COLUMN_ID_POLICY + " = " + id, null) != -1;
	}
}
