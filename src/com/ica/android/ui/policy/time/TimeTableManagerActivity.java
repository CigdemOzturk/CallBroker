package com.ica.android.ui.policy.time;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ica.R;
import com.ica.model.policy.time.TimeStamp;
import com.ica.util.time.TimeUtils;
import com.ica.util.time.TimeUtils.DAYS;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * 
 * @author Cigdem
 *
 */
public class TimeTableManagerActivity extends Activity
{
	private List<TimeStamp> timeStampList;
	private ArrayAdapter<TimeStamp> arrayAdapter;
	private ListView lv = null;
	private DAYS currentDay = DAYS.MONDAY;
	private static HashMap<DAYS, List<TimeStamp>> timeTable = null;
	private TextView title;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table_setter);
		title = (TextView) findViewById(R.id.dayName);
		title.setText(TimeUtils.DayToString(currentDay));
		lv = (ListView) findViewById(R.id.lvTimeStamps);
		final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		timeStampList = new Vector<TimeStamp>();
		if(timeTable != null)
		{
			timeStampList.addAll(timeTable.get(currentDay));
		}
		else
		{
			timeTable = new HashMap<DAYS, List<TimeStamp>>();
			for(DAYS day : DAYS.values())
			{
				timeTable.put(day, new Vector<TimeStamp>());
			}
			timeStampList = new Vector<TimeStamp>();
		}
		ImageButton bNextDay = (ImageButton) findViewById(R.id.bNextDay);
		bNextDay.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				lv.startAnimation(myFadeOutAnimation);
				timeTable.get(currentDay).clear();
				timeTable.get(currentDay).addAll(timeStampList);
				currentDay = TimeUtils.getNextDay(currentDay);
				title.setText(TimeUtils.DayToString(currentDay));
				timeStampList.clear();
				timeStampList.addAll(timeTable.get(currentDay));
				arrayAdapter.notifyDataSetChanged();
				lv.startAnimation(myFadeInAnimation);
			}
		});
		ImageButton bPreviousDay = (ImageButton) findViewById(R.id.bPreviousDay);
		bPreviousDay.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				lv.startAnimation(myFadeOutAnimation);
				timeTable.get(currentDay).clear();
				timeTable.get(currentDay).addAll(timeStampList);
				currentDay = TimeUtils.getPreviousDay(currentDay);
				title.setText(TimeUtils.DayToString(currentDay));
				timeStampList.clear();
				timeStampList.addAll(timeTable.get(currentDay));
				arrayAdapter.notifyDataSetChanged();
				lv.startAnimation(myFadeInAnimation);
			}
		});
		ImageButton bAddTimeStamp = (ImageButton) findViewById(R.id.bAddTimeStamp);
		bAddTimeStamp.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				final Dialog dialog = new Dialog(TimeTableManagerActivity.this);
				dialog.setContentView(R.layout.time_stamp_dialog);
				dialog.setTitle("Pick a TimeStamp");
				dialog.setCancelable(true);
				final TimePicker fromPicker = (TimePicker) dialog.findViewById(R.id.fromPicker);
				final TimePicker toPicker = (TimePicker) dialog.findViewById(R.id.toPicker);
				Button saveTimeStamp = (Button) dialog.findViewById(R.id.bSaveTimeStamp);
				saveTimeStamp.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Calendar from = new GregorianCalendar();
						from.set(Calendar.HOUR_OF_DAY, fromPicker.getCurrentHour());
						from.set(Calendar.MINUTE, fromPicker.getCurrentMinute());
						Calendar to = new GregorianCalendar();
						to.set(Calendar.HOUR_OF_DAY, toPicker.getCurrentHour());
						to.set(Calendar.MINUTE, toPicker.getCurrentMinute());
						if(from.after(to))
							Toast.makeText(TimeTableManagerActivity.this, "\"To\" must be after \"From\"",  Toast.LENGTH_SHORT).show();
						else
						{
							timeStampList.add(new TimeStamp(from.get(Calendar.HOUR_OF_DAY), from.get(Calendar.MINUTE), to.get(Calendar.HOUR_OF_DAY), to.get(Calendar.MINUTE)));
							arrayAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});
		arrayAdapter = new ArrayAdapter<TimeStamp>(this, android.R.layout.simple_list_item_1, timeStampList); // Android.R
		lv.setAdapter(arrayAdapter);
		registerForContextMenu(lv);
		arrayAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onStop()
	{
		timeTable.get(currentDay).clear();
		timeTable.get(currentDay).addAll(timeStampList);
		Toast.makeText(this, "Time Filter saved", Toast.LENGTH_SHORT).show();
		super.onStop();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu,  v,  menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.time_stamp_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		TimeStamp timeStamp = (TimeStamp) arrayAdapter.getItem(info.position);
		switch (item.getItemId())
		{
			case R.id.deleteTimeStamp:
				timeStampList.remove(timeStamp);
				timeTable.get(currentDay).remove(timeStamp);
				arrayAdapter.notifyDataSetChanged();
					return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	public static HashMap<DAYS, List<TimeStamp>> getResult()
	{
		return timeTable;
	}
	
	public static void clearResult()
	{
		timeTable = null;
	}
	
	// @param timeTable
	public static void setTimeTable(HashMap<DAYS, List<TimeStamp>> timeFilter)
	{
		timeTable = timeFilter;
	}
}
