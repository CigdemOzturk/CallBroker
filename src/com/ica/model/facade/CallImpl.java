package com.ica.model.facade;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;


import com.ica.android.service.Call;
import com.ica.android.service.Caller;

import com.ica.persistence.dao.sqllite.DatabaseDefinition;
import com.ica.persistence.factory.AbstractDaoFactory;
import com.ica.persistence.factory.DaoType;
import com.ica.persistence.interfaces.CallDao;
import com.ica.persistence.interfaces.CallerDao;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.persistence.interfaces.ContactGroupDao;

/**
 * @author Cigdem
 */
public class CallImpl implements CallInterface
{
	private static CallImpl instance = null;
	
	private ContactGroupDao contactGroupDao = null;
	private CallDao callDao = null;
	private CommandDao commandDao = null;
	
	public CallImpl(Context context)
	{
		AbstractDaoFactory factory = AbstractDaoFactory.getFactory(DaoType.SQL_LITE, context);
		factory.createConnection();
		contactGroupDao = factory.getContactGroupDao();
		callDao = factory.getCallDao();
		commandDao = factory.getAbstractCommandDao();
	}
	//@return instance
		public static CallImpl getInstance(Context context)
		{
			if(instance == null)
			{
				instance = new CallImpl(context);
			}
			return instance;
		}

	@Override
	public boolean insertCall(Call call) 
	{
		ContentValues callerValues = new ContentValues();
		callerValues.put(DatabaseDefinition.CALL_FREQUENCY_FIFTEEN, call.getFifteen());	
		callerValues.put(DatabaseDefinition.CALL_FREQUENCY_THIRTY, call.getThirty());
		callerValues.put(DatabaseDefinition.CALL_FREQUENCY_DAYPART,  call.getDayPart());
		callDao.insert(callerValues);
		return true;
	}

	public List<Call> findCall(String number)
	{
		return callDao.findCall(number);
	}
	
	@Override
	public void removeFromGroup(String phoneNumber) 
	{
		callDao.removeFromGroup(phoneNumber);
	}
	@Override
	public void addToGroup(String phoneNumber)
	{
		// TODO Auto-generated method stub
		
	}


}
