package com.ica.android.ui;

import com.ica.R;
import com.ica.android.ui.contact.ContactsGroupManagementActivity;
import com.ica.android.ui.policy.PolicyManagementActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 
 * @author Cigdem
 *
 */
public class iCaActivity extends TabActivity
{
	// Based on TabActivity Template from Android Developers website
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent;
		
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, PolicyManagementActivity.class);
		
		// Initialise a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("policy").setIndicator(res.getText(R.string.policy_tab_name), res.getDrawable(R.drawable.ic_tab_policies)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this,  ContactsGroupManagementActivity.class);
		spec = tabHost.newTabSpec("ContactGroup").setIndicator(res.getText(R.string.contact_group_tab_name), res.getDrawable(R.drawable.ic_tab_contacts_groups)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
}
