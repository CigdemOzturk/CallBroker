package com.ica.persistence.factory;

import android.content.Context;

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
public abstract class AbstractDaoFactory 
{
	private static AbstractDaoFactory instance = null;
	
	public abstract void createConnection();
	public abstract void closeConnection();
	public abstract ContactGroupDao getContactGroupDao();
	public abstract ContactDao getContactDao();
	public abstract CommandDao getAbstractCommandDao();
	public abstract TimeStampDao getTimeStampDao();
	public abstract PolicyDao getPolicyDao();
	public abstract CallerDao getCallerDao();
	public abstract CallDao getCallDao();
	
	public static AbstractDaoFactory getFactory(DaoType type, Context context)
	{
		switch(type)
		{
		case SQL_LITE:
			if(instance == null)
				instance = new SqlLiteDaoFactory(context);
			break;
			default:
				instance = null;
				break;
		}
		return instance;
	}
}
