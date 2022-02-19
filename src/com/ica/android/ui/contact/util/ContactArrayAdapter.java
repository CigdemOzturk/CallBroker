package com.ica.android.ui.contact.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ica.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 
 * @author Cigdem
 *
 */
public class ContactArrayAdapter extends ArrayAdapter<AndroidContact> implements SectionIndexer
{
	private List<AndroidContact> contacts = null;
	private Context context = null;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	
	public ContactArrayAdapter(Context context, int textViewResourceId, List<AndroidContact> contacts)
	{
		super(context,  textViewResourceId, contacts);
		this.contacts = contacts;
		this.context = context;
		alphaIndexer = new HashMap<String, Integer>();
		int size = contacts.size();
		for(int i = size - 1; i >= 0; i--)
		{
			AndroidContact element = contacts.get(i);
			alphaIndexer.put(element.getName().substring(0, 1), i);
		}
		Set<String> keys = alphaIndexer.keySet();
		Iterator<String> it = keys.iterator();
		ArrayList<String> keyList = new ArrayList<String>();
		while(it.hasNext())
		{
			String key = it.next();
			keyList.add(key);
		}
		Collections.sort(keyList);
		sections = new String[keyList.size()];
		keyList.toArray(sections);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if(view == null)
		{
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.contact_list_view, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name_entry);
			viewHolder.number = (TextView) view.findViewById(R.id.number_entry);
			view.setTag(viewHolder);
		}
		AndroidContact contact = contacts.get(position);
		if(contact != null)
		{
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			viewHolder.name.setText(contact.getName());
			viewHolder.number.setText(contact.getPhoneNumber());
			view.setBackgroundColor(contact.getChecked() ? Color.DKGRAY : Color.BLACK);
		}
		return view;
	}
	
	public int getPositionForSection(int section)
	{
		return alphaIndexer.get(sections[section]);
	}
	
	public int getSectionForPosition(int position)
	{
		return 0;
	}
	
	// @return sections
	public Object[] getSections()
	{
		return sections;
	}
	
	static class ViewHolder
	{
		TextView name;
		TextView number;
	}
}
