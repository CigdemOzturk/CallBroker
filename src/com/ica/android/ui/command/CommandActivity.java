package com.ica.android.ui.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ica.R;
import com.ica.model.facade.ContactFacadeImpl;
import com.ica.model.facade.ContactFacadeInterface;
import com.ica.model.command.Command;
import com.ica.model.command.CommandContent;
import com.ica.model.command.CommandType;
import com.ica.model.contact.ContactGroup;
import com.ica.util.command.AudioManagerAdaptor.RING_MODE;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

/**
 * 
 * @author Cigdem
 *
 */
public class CommandActivity extends Activity
{
	private ContactFacadeInterface contactFacade;
	private Command sendSmsCommand = null;
	private Dialog sendSmsDialog;
	private ContactGroup selectedContactGroup = null;
	private AutoCompleteTextView etContactGroup = null;
	private boolean editMode = false;
	private List<Command> commands = null;
	private Long idContactGroup;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactFacade = ContactFacadeImpl.getInstance(getApplicationContext());
		setContentView(R.layout.edit_command);
		Bundle extras = getIntent().getExtras();
		idContactGroup = (Long) extras.get("contactGroupId");
		commands = contactFacade.findCommandsOfContactGroup(idContactGroup);
		Button save = (Button) findViewById(R.id.bSaveCommand);
		save.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				getCommandDefined();
				contactFacade.updateCommandsOfCotactGroup(idContactGroup, commands);
				setResult(RESULT_OK, null);
				finish();
			}
		});
		sendSmsDialog = createSendSmsDialog();
		ToggleButton sendSms = (ToggleButton) findViewById(R.id.bSendSms);
		sendSms.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked && !editMode)
				{
					sendSmsDialog.show();
				}
				else if(isChecked && editMode)
				{
					editMode = false;
				}
				else
				{
					sendSmsCommand = null;
				}
			}
		});
		setCommandDefinition();
	}
		
	private Dialog createSendSmsDialog()
	{
		Dialog dialog = new Dialog(CommandActivity.this);
		dialog.setContentView(R.layout.send_sms);
		dialog.setTitle(getResources().getString(R.string.send_sms_name));
		Button saveSms = (Button) dialog.findViewById((R.id.bSaveSms));
		final EditText body = (EditText) dialog.findViewById(R.id.etBodySms);
		etContactGroup = (AutoCompleteTextView) dialog.findViewById(R.id.tvContactsGroups);
		List<ContactGroup> contacts = contactFacade.findAllContactGroups();
		List<ContactGroup> arrayContacts = new ArrayList<ContactGroup>(contacts);
		etContactGroup.setAdapter(new ArrayAdapter<ContactGroup>(CommandActivity.this, android.R.layout.simple_dropdown_item_1line, arrayContacts)); // android.R
		etContactGroup.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId)
			{
				selectedContactGroup = (ContactGroup) etContactGroup.getAdapter().getItem(pos);
			}
		});
		saveSms.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
				content.put(CommandContent.CONTACT_GROUP, selectedContactGroup);
				content.put(CommandContent.TEXT_MESSAGE, body.getText().toString());
				sendSmsCommand = new Command(CommandType.SEND_SMS, content);
				sendSmsDialog.dismiss();
			}
		});
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rRadioGroup);
		ImageView image = (ImageView) findViewById(R.id.unselectRingMode);
		image.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				radioGroup.clearCheck();
			}
		});
		return dialog;
	}
	
	private void getCommandDefined()
	{
		RadioButton acceptCall = (RadioButton) findViewById(R.id.rAcceptCall);
		RadioButton ignoreCall = (RadioButton) findViewById(R.id.rIgnoreCall);
		RadioButton rejectCall = (RadioButton) findViewById(R.id.rRejectCall);
		ToggleButton sendSms = (ToggleButton) findViewById(R.id.bSendSms);
		RadioButton ringingMode = (RadioButton) findViewById(R.id.rRingMode);
		RadioButton silentMode = (RadioButton) findViewById(R.id.rSilentMode);
		RadioButton vibrateMode = (RadioButton) findViewById(R.id.rVibrateMode);
		List<Command> commands = new ArrayList<Command>();
		if(acceptCall.isChecked())
			commands.add(new Command(CommandType.ACCEPT_CALL));
		if(ignoreCall.isChecked())
			commands.add(new Command(CommandType.IGNORE_CALL));
		if(rejectCall.isChecked())
			commands.add(new Command(CommandType.REJECT_CALL));
		if(sendSms.isChecked())
		{
			if(sendSmsCommand != null)
			{
				if(sendSmsCommand.getContent().containsKey(CommandContent.CONTACT_GROUP) && (sendSmsCommand.getContent().get(CommandContent.CONTACT_GROUP) != null))
					commands.add(sendSmsCommand);
			}
			else{
				for(Command c : this.commands)
					if(c.getType() == CommandType.SEND_SMS)
					{
						HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
						content.put(CommandContent.CONTACT_GROUP, (ContactGroup) c.getContent().get(CommandContent.CONTACT_GROUP));
						content.put(CommandContent.TEXT_MESSAGE, (String) c.getContent().get(CommandContent.TEXT_MESSAGE));
						sendSmsCommand = new Command(CommandType.SEND_SMS, content);
					}
				if(sendSmsCommand != null)
					commands.add(sendSmsCommand);
			}
		}
		if(ringingMode.isChecked())
		{
			HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
			content.put(CommandContent.RING_MODE, RING_MODE.RINGING_MODE);
			commands.add(new Command(CommandType.RING_MODE, content));
		}
		if(silentMode.isChecked())
		{
			HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
			content.put(CommandContent.RING_MODE, RING_MODE.SILENT_MODE);
			commands.add(new Command(CommandType.RING_MODE, content));
		}
		if(vibrateMode.isChecked())
		{
			HashMap<CommandContent, Object> content = new HashMap<CommandContent, Object>();
			content.put(CommandContent.RING_MODE, RING_MODE.VIBRATE_MODE);
			commands.add(new Command(CommandType.RING_MODE, content));
		}
		this.commands = commands;
	}
	
	private void setCommandDefinition()
	{
		RadioButton acceptCall = (RadioButton) findViewById(R.id.rAcceptCall);
		RadioButton rejectCall = (RadioButton) findViewById(R.id.rRejectCall);
		RadioButton ignoreCall = (RadioButton) findViewById(R.id.rIgnoreCall);
		ToggleButton sendSms = (ToggleButton) findViewById(R.id.bSendSms);
		RadioButton ringingMode = (RadioButton) findViewById(R.id.rRingMode);
		RadioButton silentMode = (RadioButton) findViewById(R.id.rSilentMode);
		RadioButton vibrateMode = (RadioButton) findViewById(R.id.rVibrateMode);
		
		for(Command c : commands)
		{
			switch(c.getType())
			{
			case ACCEPT_CALL:
				acceptCall.setChecked(true);
				break;
			case IGNORE_CALL:
				ignoreCall.setChecked(true);
				break;
			case REJECT_CALL:
				rejectCall.setChecked(true);
				break;
			case RING_MODE:
				HashMap<CommandContent, Object> content = c.getContent();
				if(content != null)
				{
					switch((RING_MODE) content.get(CommandContent.RING_MODE))
					{
					case RINGING_MODE:
						ringingMode.setChecked(true);
						break;
					case SILENT_MODE:
						silentMode.setChecked(true);
						break;
					case VIBRATE_MODE:
						vibrateMode.setChecked(true);
						break;
					}
				}
				break;
			case SEND_SMS:
				sendSmsCommand = c;
				HashMap<CommandContent, Object> content2 = c.getContent();
				if(content2 != null)
				{
					((EditText) sendSmsDialog.findViewById(R.id.etBodySms)).setText((String) content2.get(CommandContent.TEXT_MESSAGE));
					selectedContactGroup = (ContactGroup) content2.get(CommandContent.CONTACT_GROUP);
					etContactGroup.setText(selectedContactGroup.getName());
					editMode = true;
					sendSms.setChecked(true);
				}
				break;
			}
		}
	}
}
