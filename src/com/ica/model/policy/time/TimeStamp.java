package com.ica.model.policy.time;

import java.util.Calendar;

import com.ica.model.Identifiable;

/**
 * 
 * @author Cigdem
 *
 */
public class TimeStamp extends Identifiable
{
	private int fromHour = -1;
	private int fromMinute = -1;
	private int toHour = -1;
	private int toMinute = -1;
	
	public TimeStamp(int fromHour, int fromMinute, int toHour, int toMinute)
	{
		super();
		this.fromHour = fromHour;
		this.fromMinute = fromMinute;
		this.toHour = toHour;
		this.toMinute = toMinute;
	}
	
	public boolean contains(Calendar time)
	{
		if((fromHour == -1) || (toHour == -1) || (fromMinute == -1) || (toMinute == -1))
		{
			return false;
		}
		if(time.get(Calendar.HOUR_OF_DAY) >= fromHour)
		{
			if(time.get(Calendar.MINUTE) >= fromMinute)
			{
				if(time.get(Calendar.HOUR_OF_DAY) < toHour)
					return true;
				else if(time.get(Calendar.HOUR_OF_DAY) == toHour)
					return time.get(Calendar.MINUTE) <= toMinute;
			}
			return false;
		}
		return false;
	}
	
	public int getFromHour()
	{
		return fromHour;
	}
	
	public int getFromMinute()
	{
		return fromMinute;
	}
	
	public int getToHour()
	{
		return toHour;
	}
	
	public int getToMinute()
	{
		return toMinute;
	}
	
	public String toString()
	{
		return fromHour+":"+fromMinute+" => "+toHour+":"+toMinute;
	}
}
