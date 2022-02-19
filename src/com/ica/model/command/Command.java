package com.ica.model.command;

import java.util.HashMap;

import android.content.Context;

import com.ica.model.Identifiable;
import com.ica.model.contact.ContactGroup;
import com.ica.util.command.AudioManagerAdaptor;
import com.ica.util.command.AudioManagerAdaptor.RING_MODE;
import com.ica.util.command.SmsManagerAdaptor;
import com.ica.util.command.TelephonyProxy;

/**
 * 
 * @author Cigdem
 *
 */
public class Command extends Identifiable
{
	CommandType type;
	private HashMap<CommandContent, Object> content;
	
	private static TelephonyProxy telephonyProxy = null;
	private static AudioManagerAdaptor audioManagerAdaptor = null;
	private static SmsManagerAdaptor smsManagerAdaptor = null;
	
	static
	{
		telephonyProxy = new TelephonyProxy();
		audioManagerAdaptor = new AudioManagerAdaptor();
		smsManagerAdaptor = new SmsManagerAdaptor();
	}
	
	public Command(CommandType type)
	{
		this.type = type;
	}

	public Command(CommandType type, HashMap<CommandContent, Object> commandContent)
	{
		this(type);
		content = commandContent;
	}
	
	public void execute(Context context)
	{
		switch(type){
			case ACCEPT_CALL:
				break;
			case IGNORE_CALL:
				telephonyProxy.ignoreCall(context);
				break;
			case REJECT_CALL:
				telephonyProxy.rejectCall(context);
				break;
			case RING_MODE:
				audioManagerAdaptor.setRingMode(context, (RING_MODE)content.get(CommandContent.RING_MODE));
				break;
			case SEND_SMS:
				smsManagerAdaptor.sendSms(context, (ContactGroup)content.get(CommandContent.CONTACT_GROUP), (String)content.get(CommandContent.TEXT_MESSAGE));
				break;
		}
	}
	
	public CommandType getType()
	{
		return type;
	}
	
	public HashMap<CommandContent, Object> getContent()
	{
		return content;
	}
}
