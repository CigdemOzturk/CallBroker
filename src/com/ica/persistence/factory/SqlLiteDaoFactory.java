package com.ica.persistence.factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ica.persistence.dao.sqllite.MySqlLiteHelper;
import com.ica.persistence.dao.sqllite.SqlLiteCallDao;
import com.ica.persistence.dao.sqllite.SqlLiteCallerDao;
import com.ica.persistence.dao.sqllite.SqlLiteCommandDao;
import com.ica.persistence.dao.sqllite.SqlLiteContactDao;
import com.ica.persistence.dao.sqllite.SqlLiteContactGroupDao;
import com.ica.persistence.dao.sqllite.SqlLitePolicyDao;
import com.ica.persistence.dao.sqllite.SqlLiteTimeStampDao;

import com.ica.persistence.interfaces.CallDao;
import com.ica.persistence.interfaces.CallerDao;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.persistence.interfaces.ContactDao;
import com.ica.persistence.interfaces.ContactGroupDao;
import com.ica.persistence.interfaces.PolicyDao;
import com.ica.persistence.interfaces.TimeStampDao;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteDaoFactory extends AbstractDaoFactory
{
	private MySqlLiteHelper helper = null;
	private SQLiteDatabase db = null;
	
	public SqlLiteDaoFactory(Context context)
	{
		helper = new MySqlLiteHelper(context);
	}
	
	@Override
	public void createConnection()
	{
		db = helper.getWritableDatabase();
	}
	
	@Override
	public void closeConnection()
	{
		db.close();
	}
	
	@Override
	public ContactDao getContactDao()
	{
		return new SqlLiteContactDao(db);
	}
	
	@Override
	public ContactGroupDao getContactGroupDao()
	{
		return new SqlLiteContactGroupDao(db);
	}
	
	@Override
	public CommandDao getAbstractCommandDao()
	{
		return new SqlLiteCommandDao(db);
	}
	
	@Override
	public PolicyDao getPolicyDao()
	{
		return new SqlLitePolicyDao(db);
	}
	
	@Override
	public TimeStampDao getTimeStampDao()
	{
		return new SqlLiteTimeStampDao(db);
	}
	
	@Override
	public CallerDao getCallerDao()
	{
		return new SqlLiteCallerDao(db);
	}

	@Override
	public CallDao getCallDao() 
	{
		return new SqlLiteCallDao(db);
	}
}
