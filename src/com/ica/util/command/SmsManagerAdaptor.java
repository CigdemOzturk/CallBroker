package com.ica.util.command;

import java.util.List;

import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Cigdem
 *
 */
public class SmsManagerAdaptor 
{
	private SmsManager smsManager = null;
	private PendingIntent sentPI = null;
	private PendingIntent deliveredPI = null;
	
	// @return smsManager
	private SmsManager getSmsManager(Context context)
	{
		if(smsManager == null)
		{
			smsManager = SmsManager.getDefault();createIntents(context);
		}
		return smsManager;
	}
	
	private void createIntents(final Context context)
	{
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		
		
		sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
		deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
		
		// When the SMS has been sent
		context.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1){
				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					android.widget.Toast.makeText(context.getApplicationContext(),  "SMS sent", android.widget.Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					android.widget.Toast.makeText(context.getApplicationContext(), "Generic failure", android.widget.Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					android.widget.Toast.makeText(context.getApplicationContext(), "No service", android.widget.Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					android.widget.Toast.makeText(context.getApplicationContext(), "Null PDU", android.widget.Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					android.widget.Toast.makeText(context.getApplicationContext(), "Radio off", android.widget.Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));
		
		// When the SMS has been delivered
		context.registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1){
				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					android.widget.Toast.makeText(context.getApplicationContext(), "Sms delivered", android.widget.Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					android.widget.Toast.makeText(context.getApplicationContext(), "Sms not delivered", android.widget.Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));
	}
	
	public void sendSms(Context context, ContactGroup receiver, String body)
	{
		List<Contact> contacts = receiver.getContacts();
		for(Contact contact : contacts)
		{
			getSmsManager(context).sendTextMessage(contact.getPhoneNumber().replace("+", "00"), null, body, sentPI, deliveredPI);
			Log.w("DEBUG", "Message: " + body + " SENT to" + contact.getPhoneNumber());
		}
	}
}
