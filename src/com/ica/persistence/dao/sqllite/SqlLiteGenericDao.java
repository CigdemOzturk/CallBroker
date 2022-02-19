package com.ica.persistence.dao.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ica.persistence.interfaces.GenericDao;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class SqlLiteGenericDao<T> implements GenericDao<T> 
{
	protected SQLiteDatabase db = null;
	
	public SqlLiteGenericDao (SQLiteDatabase db)
	{
		this.db = db;
	}
	
	protected abstract String getTableName();
	protected abstract T toObject(Cursor cursor);
	
	public long insert(ContentValues values)
	{
		return db.insert(getTableName(),  null,  values);
	}
	
	public boolean delete(long id)
	{
		return db.delete(getTableName(), "_id + " + id, null) > 0;
	}
	
	public T find(long id)
	{
		Cursor cursor = db.query(true,  getTableName(), null, "_id = " + id, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			return toObject(cursor);
		}
		cursor.close();
		return null;
	}
	
	public List<T> findAll()
	{
		List<T> res = new ArrayList<T>();
		Cursor cursor = db.query(true, getTableName(), null, null, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				res.add(toObject(cursor));
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return res;
	}
	
	public boolean update(long id, ContentValues values)
	{
		return db.update(getTableName(),  values,  "_id = " + id, null) > 0;
	}
}
