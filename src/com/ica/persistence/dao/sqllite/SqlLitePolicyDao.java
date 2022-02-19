package com.ica.persistence.dao.sqllite;

import com.ica.model.policy.Policy;
import com.ica.persistence.interfaces.PolicyDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLitePolicyDao extends SqlLiteGenericDao<Policy> implements PolicyDao
{
	public SqlLitePolicyDao(SQLiteDatabase db)
	{
		super(db);
	}
	
	@Override
	protected String getTableName()
	{
		return DatabaseDefinition.TABLE_POLICY;
	}
	
	@Override
	protected Policy toObject(Cursor cursor)
	{
		Long id = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.POLICY_COLUMN_ID));
		String name = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.POLICY_COLUMN_NAME));
		boolean active = cursor.getInt(cursor.getColumnIndex(DatabaseDefinition.POLICY_ACTIVE)) > 0;
		
		Policy policy = new Policy(name);
		policy.setId(id);
		policy.setActive(active);
		return policy;
	}
	
	public Policy findActivePolicy()
	{
		Cursor cursor = db.query(true, getTableName(), null, DatabaseDefinition.POLICY_ACTIVE + " = 1", null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			Policy p = toObject(cursor);
			cursor.close();
			return p;
		}
		cursor.close();
		return null;
	}
	
	public void setActivePolicy(long id)
	{
		ContentValues values1 = new ContentValues();
		values1.put(DatabaseDefinition.POLICY_ACTIVE, 0);
		db.update(getTableName(), values1, DatabaseDefinition.POLICY_ACTIVE + " =1 ", null);
		
		ContentValues values2 = new ContentValues();
		values2.put(DatabaseDefinition.POLICY_ACTIVE, 1);
		db.update(getTableName(), values2, DatabaseDefinition.POLICY_COLUMN_ID + " = " + id, null);
	}
	
	@Override
	public boolean delete(long id)
	{
		db.delete(DatabaseDefinition.TABLE_POLICY_CONTACT_GROUP, DatabaseDefinition.POLICY_CONTACT_GROUP_COLUMN_ID_POLICY + " = " + id, null);
		return super.delete(id);
	}
}
