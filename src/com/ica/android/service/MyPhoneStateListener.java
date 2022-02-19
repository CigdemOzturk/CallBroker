package com.ica.android.service;

/**
 * 
 * @author Cigdem
 *
 */

import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.ica.R;
import com.ica.model.contact.ContactGroup;
import com.ica.model.facade.PolicyFacadeImpl;
import com.ica.model.facade.PolicyFacadeInterface;
import com.ica.util.command.Notificator;

public class MyPhoneStateListener extends PhoneStateListener
{
		private Context context;
		private PolicyFacadeInterface policyFacade;
		private Notificator notificator;
		private static boolean locker = true;
		
		
		// Calling these to save into database in the event of reject
		public static String incomingNum = "";
		public static String callerName = "";
		
		
		public MyPhoneStateListener(Context context)
		{
			this.context = context;
			notificator = Notificator.getInstace(context.getApplicationContext());
			policyFacade = PolicyFacadeImpl.getInstance(context);
		}
		
		private String getNameFromPhoneNumber(String phoneNumber, Context context)
		{
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
			Cursor cursor = context.getContentResolver().query(uri, new String[] {PhoneLookup.DISPLAY_NAME}, null, null, null);
			if(cursor.moveToFirst())
			{
				String res = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
				cursor.close();
				return res;
			}
			cursor.close();
			return phoneNumber;
		}
		
		public void onCallStateChanged(int state, String incomingNumber)
		{		
			switch(state)
			{
			case TelephonyManager.CALL_STATE_IDLE:
				Log.d("DEBUG", "IDLE");
				locker = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.d("DEBUG", "OFFHOOK");
				locker = true;
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				if(locker)
				{
					locker = false;
					Log.d("DEBUG", "RINGING");
					String name = getNameFromPhoneNumber(incomingNumber, context);
					List<ContactGroup> matched = policyFacade.test(incomingNumber);
					System.out.printf("Incoming call {0}", incomingNumber);
					
					incomingNum = incomingNumber;
					callerName = name;

					if(matched.isEmpty())
						locker = true;
					for(ContactGroup contactGroup : matched)
					{
						contactGroup.executeCommand(context.getApplicationContext());
						notificator.createNotification(name + " called ", name + " [" + incomingNumber + " ] called", "Contact Group" + contactGroup.getName() + " matched", R.drawable.ic_tab_policy);						
					}
				}
				break;
			}
		}
}
