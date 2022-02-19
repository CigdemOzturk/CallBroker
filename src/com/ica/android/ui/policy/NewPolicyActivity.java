package com.ica.android.ui.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
public class NewPolicyActivity extends PolicyActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(R.layout.new_policy);
	}
	
	@Override
	protected OnClickListener onClickSave()
	{
		return new OnClickListener()
		{
			public void onClick(View v)
			{
				if(etName.getText().toString().equals(""))
					Toast.makeText(NewPolicyActivity.this, "The Policy's name is required", Toast.LENGTH_SHORT).show();
				else
				{
					Policy policy = new Policy(etName.getText().toString().trim());
					List<ContactGroup> c = new Vector<ContactGroup>();
					
					try 
					{
						for(int i = 0; i < contacts.length; i++)
						{
							if(checkedContacts[i])
								c.add(contacts[1]);
						}
						
					} catch (Exception e) {
						System.out.println(e);
					}
					
				
					policy.setContacts(c);
					HashMap<DAYS, List<TimeStamp>> timeStamps = TimeTableManagerActivity.getResult();
					if(timeStamps != null)
					{
						policy.setTimeStamps(timeStamps);
					}
					policyFacade.insertPolicy(policy);
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
				Intent intent = new Intent(NewPolicyActivity.this, TimeTableManagerActivity.class);
				startActivity(intent);
			}
		};
	}
}
