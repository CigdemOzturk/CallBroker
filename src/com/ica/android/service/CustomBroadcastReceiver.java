package com.ica.android.service;

/**
 * 
 * @author Cigdem
 *
 */

import java.util.List;

import com.ica.R;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;
import com.ica.model.facade.CallerImpl;
import com.ica.model.facade.PolicyFacadeImpl;
import com.ica.model.facade.PolicyFacadeInterface;
import com.ica.util.command.Notificator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CustomBroadcastReceiver extends BroadcastReceiver
{
	private MyPhoneStateListener phoneListener;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(phoneListener == null)
		{
			phoneListener = new MyPhoneStateListener(context);
			TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
}
