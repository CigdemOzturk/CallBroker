package com.ica.persistence.dao.sqllite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ica.model.command.Command;
import com.ica.model.command.CommandContent;
import com.ica.model.command.CommandType;
import com.ica.model.contact.ContactGroup;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.util.command.AudioManagerAdaptor.RING_MODE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Cigdem
 *
 */
public class SqlLiteCommandDao extends SqlLiteGenericDao<Command> implements CommandDao
{
	private enum COMMAND_TYPE
	{
		ACCEPT_CALL, REJECT_CALL, IGNORE_CALL, SEND_SMS, RING_MODE, UNKNOWN
	};
	
	public SqlLiteCommandDao(SQLiteDatabase db)
	{
		super(db);
	}
	
	private COMMAND_TYPE toCommandType(String type)
	{

			if(type.equals("AcceptCall"))
			{
				return COMMAND_TYPE.ACCEPT_CALL;
			}
			else if(type.equals("RejectCall"))
			{
				return COMMAND_TYPE.REJECT_CALL;
			}
			else if(type.equals("IgnoreCall"))
			{
				return COMMAND_TYPE.IGNORE_CALL;
			}
			else if(type.equals("SendSms"))
			{
				return COMMAND_TYPE.SEND_SMS;
			}
			else if(type.equals("RingMode"))
			{
				return COMMAND_TYPE.RING_MODE;
			}
			else
				return COMMAND_TYPE.UNKNOWN;
	}
	
	@Override
	protected String getTableName()
	{
		return DatabaseDefinition.TABLE_COMMAND;
	}
	
	@ Override
	protected Command toObject(Cursor cursor)
	{
		Command command = null;
		String type = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.COMMAND_TYPE));
		Long id = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.COMMAND_COLUMN_ID));
		switch(toCommandType(type))
		{
		case ACCEPT_CALL:
			command = new Command(CommandType.ACCEPT_CALL);
			command.setId(id);
			break;
		case REJECT_CALL:
			command = new Command(CommandType.REJECT_CALL);
			command.setId(id);
			break;
		case IGNORE_CALL:
			command = new Command(CommandType.IGNORE_CALL);
			command.setId(id);
			break;
		case SEND_SMS:
			// Lazy assignment
			Long idContactGroup = cursor.getLong(cursor.getColumnIndex(DatabaseDefinition.COMMAND_ID_CONTACT_GROUP_RECEIVER));
			HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
			Cursor name = db.query(DatabaseDefinition.TABLE_CONTACT_GROUP, null, DatabaseDefinition.CONTACT_GROUP_COLUMN_ID + " = " + idContactGroup, null, null, null, null);
			if(name.moveToFirst())
			{
				String contactGroupName = name.getString(name.getColumnIndex(DatabaseDefinition.CONTACT_GROUP_COLUMN_NAME));
				ContactGroup contactGroup = new ContactGroup(contactGroupName);
				contactGroup.setId(idContactGroup);
				content.put(CommandContent.CONTACT_GROUP, contactGroup);
				content.put(CommandContent.TEXT_MESSAGE, cursor.getString(cursor.getColumnIndex(DatabaseDefinition.COMMAND_TEXT_MESSAGE)));
			}
			name.close();
			command = new Command(CommandType.SEND_SMS, content);
			command.setId(id);
			break;
		case RING_MODE:
			String mode = cursor.getString(cursor.getColumnIndex(DatabaseDefinition.COMMAND_RING_MODE));
			HashMap<CommandContent, Object> content1 = new HashMap<CommandContent, Object>();
			if(mode.equalsIgnoreCase("Silent"))
			{
				content1.put(CommandContent.RING_MODE, RING_MODE.SILENT_MODE);
			}else if(mode.equalsIgnoreCase("Vibrate"))
			{
				content1.put(CommandContent.RING_MODE, RING_MODE.VIBRATE_MODE);
			}else if(mode.equalsIgnoreCase("Ringing"))
			{
				content1.put(CommandContent.RING_MODE, RING_MODE.RINGING_MODE);
			}
			command = new Command(CommandType.RING_MODE, content1);
			command.setId(id);
			break;
			default:
				command = null;
		}
		return command;
	}
	
	public List<Command> findAllInContactGroup(long id)
	{
		List<Command> res = new ArrayList<Command>();
		Cursor cursor = db.query(true, getTableName(), null, DatabaseDefinition.COMMAND_ID_CONTACT_GROUP + " = " + id, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				res.add(toObject(cursor));
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return res;
	}
	
	public void updateFromContactGroup(Long idContactGroup, List<Command> commands)
	{
		db.delete(getTableName(), DatabaseDefinition.COMMAND_ID_CONTACT_GROUP + " = " + idContactGroup, null);
		for(Command command : commands)
		{
			ContentValues commandValues = new ContentValues();
			commandValues.put(DatabaseDefinition.COMMAND_ID_CONTACT_GROUP, idContactGroup);
			switch(command.getType())
			{
			case ACCEPT_CALL:
				commandValues.put(DatabaseDefinition.COMMAND_TYPE, "AcceptCall");
				break;
			case IGNORE_CALL:
				commandValues.put(DatabaseDefinition.COMMAND_TYPE, "IgnoreCall");
				break;
			case REJECT_CALL:
				commandValues.put(DatabaseDefinition.COMMAND_TYPE, "RejectCall");
				break;
			case SEND_SMS:
				commandValues.put(DatabaseDefinition.COMMAND_TYPE, "SendSms");
				commandValues.put(DatabaseDefinition.COMMAND_TEXT_MESSAGE, (String)command.getContent().get(CommandContent.TEXT_MESSAGE));
				commandValues.put(DatabaseDefinition.COMMAND_ID_CONTACT_GROUP_RECEIVER, ((ContactGroup)command.getContent().get(CommandContent.CONTACT_GROUP)).getId());
				break;
			case RING_MODE:
				RING_MODE type = (RING_MODE) command.getContent().get(CommandContent.RING_MODE);
				switch(type)
				{
				case RINGING_MODE:
					commandValues.put(DatabaseDefinition.COMMAND_TYPE, "RingMode");
					commandValues.put(DatabaseDefinition.COMMAND_RING_MODE, "Ringing");
					break;
				case SILENT_MODE:
					commandValues.put(DatabaseDefinition.COMMAND_TYPE, "RingMode");
					commandValues.put(DatabaseDefinition.COMMAND_RING_MODE, "Silent");
					break;
				case VIBRATE_MODE:
					commandValues.put(DatabaseDefinition.COMMAND_TYPE, "RingMode");
					commandValues.put(DatabaseDefinition.COMMAND_RING_MODE, "Vibrate");
					break;
				}
				break;
			}
			db.insert(getTableName(), null, commandValues);
		}
	}
	
	public void deleteAllFromContactGroup(long id)
	{
		db.delete(DatabaseDefinition.TABLE_COMMAND, DatabaseDefinition.COMMAND_ID_CONTACT_GROUP + " = " + id, null);
	}
}
