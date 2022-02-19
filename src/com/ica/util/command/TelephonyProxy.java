package com.ica.util.command;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.ica.android.service.Call;
import com.ica.android.service.Caller;
import com.ica.android.service.MachineLearning;
import com.ica.android.service.MyPhoneStateListener;
import com.ica.model.facade.CallImpl;
import com.ica.model.facade.CallerImpl;

/**
 * 
 * @author Cigdem
 * @param <MyPhoneStateListener>
 *
 */
@SuppressLint("SimpleDateFormat")
public class TelephonyProxy
{
	private static ITelephony telephony = null;
	
	private ITelephony getITelephonyManager(Context context) throws Exception
	{
		if(telephony == null)
		{
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			Class<?> c = Class.forName(telephonyManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephony = (ITelephony)m.invoke(telephonyManager);
		}
		return telephony;
	}
	
	public void ignoreCall(Context context)
	{
		MachineLearning(context);
		try
		{
			getITelephonyManager(context).silenceRinger();
		}
		catch(RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void rejectCall(Context context)
	{			
		MachineLearning(context);		
		try
		{
			getITelephonyManager(context).endCall();
		}
		catch(RemoteException e)
		{
			// TODO AUTO-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean countNumberOfCallsInLast(int timeCalled, int period)
	{
		int currentTime = Integer.parseInt(new SimpleDateFormat("HH:mm").format(new Date()).replaceFirst(":", ""));
		if (timeCalled <= currentTime && timeCalled >= (currentTime - period))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void MachineLearning(Context context)
	{
		// Caller instance to insert into db
		CallerImpl c = new CallerImpl(context);
				
		int currentTime = Integer.parseInt(new SimpleDateFormat("HH:mm").format(new Date()).replaceFirst(":", ""));
		

		String daypart = currentTime <= 1200 && currentTime >= 0600 ? "morning" 
				: currentTime <= 1700 && currentTime >= 1201 ? "afternoon" 
				: currentTime <= 2359 && currentTime >= 1701 ? "evening" : "night" ;
	
		c.insertCaller(new Caller(
									1, 
									MyPhoneStateListener.callerName, 
									MyPhoneStateListener.incomingNum, 
									new SimpleDateFormat("HH:mm").format(new Date()),
									new SimpleDateFormat("dd/M/yyyy").format(new Date())));	
		
		
		
		// Returns the list of records called today and matching the incoming number
		int inFifteen = 0;
		int inThirty = 0;
		
		for (Caller callers : c.findAllMatching(MyPhoneStateListener.incomingNum))
		{
			if( countNumberOfCallsInLast(Integer.parseInt(callers.getCallTimePeriod().replaceFirst(":", "")), 15) )
			{
				inFifteen++;
			}
			
			if (countNumberOfCallsInLast(Integer.parseInt(callers.getCallTimePeriod().replaceFirst(":", "")), 30))
			{
				inThirty++;
			}
		}

		
		CallImpl callimp = new CallImpl(context);
		
		callimp.insertCall(new Call(
										1, 
										MyPhoneStateListener.incomingNum,
										inFifteen,
										inThirty,
										daypart));
		
		MachineLearning ml = new MachineLearning();
		int classification = -1;
		
		for (Call calls : callimp.findCall(MyPhoneStateListener.incomingNum))
		{
			Log.d("DEBUG", "ID " + calls.getId() + " Fifteen: " + calls.getFifteen() + " Thirty: " + calls.getThirty() + " " + calls.getDayPart());
			classification = ml.classifie(calls.getFifteen(), calls.getThirty(), calls.getDayPart());
		}
									
		Log.d("DEBUG", "CLASSIFICATION ->>>>>>>>>>>>> " + classification);
		
		String phoneNumber = MyPhoneStateListener.incomingNum;
		phoneNumber = phoneNumber.substring(3,phoneNumber.length());
		String s1 = phoneNumber.substring(0, 4);
		String s2 = phoneNumber.substring(4, phoneNumber.length());
		phoneNumber = "0" + s1 + " " + s2;
		
		
		if (classification == 1)
		{
			// Move the contact to accept group
			callimp.removeFromGroup(phoneNumber);
			// Clear the Call_Frequencies table for this current caller
		}
	}
}





