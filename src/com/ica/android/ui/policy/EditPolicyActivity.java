package com.ica.android.ui.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ica.R;
import com.ica.android.ui.policy.time.TimeTableManagerActivity;
import com.ica.model.contact.ContactGroup;
import com.ica.model.policy.Policy;
import com.ica.model.policy.time.TimeStamp;
import com.ica.util.time.TimeUtils.DAYS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 * @author Cigdem
 *
 */
public class EditPolicyActivity extends PolicyActivity
{
	private Long idPolicy;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(R.layout.edit_policy);
		Bundle extras = getIntent().getExtras();
		idPolicy = (Long) extras.get("policyId");
		Policy policy = policyFacade.findPolicy(idPolicy);
		etName.setText(policy.getName());
		List<ContactGroup> cg = policyFacade.findContactGroupsOfPolicy(idPolicy);
		for(ContactGroup contactGroup : cg)
			checkedContacts[checkedPos.get(contactGroup.getId())] = true;
		TimeTableManagerActivity.setTimeTable(policyFacade.findTimeStampsOfPolicy(idPolicy));
	}
	
	@Override
	protected OnClickListener onClickSave()
	{
		return new OnClickListener()
		{
			public void onClick(View v)
			{
				if(etName.getText().toString().equals(""))
					Toast.makeText(EditPolicyActivity.this, "The Policy's name is required", Toast.LENGTH_SHORT).show();
				else
				{
					List<ContactGroup> contactGroups = new ArrayList<ContactGroup>();
					for(int i = 0; i < contacts.length; i++)
					{
						if(checkedContacts[i])
							contactGroups.add(contacts[i]);
					}
					String newName = etName.getText().toString().trim();
					HashMap<DAYS, List<TimeStamp>> timeStamps = TimeTableManagerActivity.getResult();
					policyFacade.updatePolicy(idPolicy, newName, contactGroups, timeStamps);
					TimeTableManagerActivity.clearResult();
					setResult(RESULT_OK, null);
					finish();
				}
			}
		};
	}
	
	@Override
	protected OnClickListener onClickTimeTable()
	{
		return new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(EditPolicyActivity.this, TimeTableManagerActivity.class);
				startActivity(intent);
			}
		};
	}
}
