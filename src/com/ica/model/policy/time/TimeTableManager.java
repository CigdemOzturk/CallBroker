package com.ica.model.policy.time;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ica.model.Identifiable;
import com.ica.util.time.TimeUtils;
import com.ica.util.time.TimeUtils.DAYS;

/**
 * 
 * @author Cigdem
 *
 */
public class TimeTableManager extends Identifiable
{
	private HashMap<DAYS, List<TimeStamp>> timeTable;
	
	public TimeTableManager()
	{
		super();
		timeTable = new HashMap<DAYS, List<TimeStamp>>();
		for(DAYS day : DAYS.values())
		{
			timeTable.put(day, new Vector<TimeStamp>());
		}
	}
	
	public boolean contains(Calendar time) 
	{
		if(timeTable.size() == 0)
		{
			return true;
		}
		DAYS day = TimeUtils.calendarToDay(time);
		if(day == DAYS.UNKNOWN)
		{
			return false;
		}
		if(timeTable.get(day).size() == 0)
		{
			return true;
		}
		boolean res = false;
		for(TimeStamp timeStamp : timeTable.get(day))
		{
			boolean temp = timeStamp.contains(time);
			res = res || temp;
		}
		return res;
	}
	
	public List<TimeStamp> getTimeStamps(DAYS day)
	{
		return timeTable.get(day);
	}
		
	public void setTimeTable(HashMap<DAYS, List<TimeStamp>> timeTable)
	{
		this.timeTable = timeTable;
	}
}
