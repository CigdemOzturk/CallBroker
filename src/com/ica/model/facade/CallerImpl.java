package com.ica.model.facade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.ica.android.service.Caller;
import com.ica.model.command.Command;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;
import com.ica.persistence.dao.sqllite.DatabaseDefinition;
import com.ica.persistence.factory.AbstractDaoFactory;
import com.ica.persistence.factory.DaoType;
import com.ica.persistence.interfaces.CallerDao;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.persistence.interfaces.ContactDao;
import com.ica.persistence.interfaces.ContactGroupDao;

/**
 * 
 * @author Cigdem
 *
 */
public class CallerImpl implements CallerInterface
{
	private static CallerImpl instance = null;
	
	private ContactGroupDao contactGroupDao = null;
	private CallerDao callerDao = null;
	private CommandDao commandDao = null;
	
	public CallerImpl(Context context)
	{
		AbstractDaoFactory factory = AbstractDaoFactory.getFactory(DaoType.SQL_LITE, context);
		factory.createConnection();
		contactGroupDao = factory.getContactGroupDao();
		callerDao = factory.getCallerDao();
		commandDao = factory.getAbstractCommandDao();
	}
	//@return instance
		public static CallerImpl getInstance(Context context)
		{
			if(instance == null)
			{
				instance = new CallerImpl(context);
			}
			return instance;
		}

	@Override
	public boolean insertCaller(Caller caller) 
	{
		ContentValues callerValues = new ContentValues();
		callerValues.put(DatabaseDefinition.CALL_HISTORY_CALLER_NAME, caller.getName());
		callerValues.put(DatabaseDefinition.CALL_HISTORY_CALLER_NUMBER, caller.getPhoneNumber());	
		callerValues.put(DatabaseDefinition.CALL_HISTORY_CALLER_TIME_PERIOD, caller.getCallTimePeriod());
		callerValues.put(DatabaseDefinition.CALL_HISTORY_CALLER_DATE,  caller.getCallDate());
		callerDao.insert(callerValues);
		return true;
	}

	@Override
	public boolean deleteContact(long id) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	public List<Caller> findAllMatching(String number)
	{
		return callerDao.findAllMatching(number);
	}


}
