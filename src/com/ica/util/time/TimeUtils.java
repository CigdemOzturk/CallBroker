package com.ica.util.time;
import android.annotation.SuppressLint;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Cigdem
 *
 */
public class TimeUtils 
{
	public static enum DAYS
	{
		
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY,
		SUNDAY,
		UNKNOWN
	}
	
	public static String DayToString(DAYS day)
	{
		String d = "";
		switch(day)
		{
		case MONDAY:
			d = "Monday";
			break;
		case TUESDAY:
			d = "Tuesday";
			break;
		case WEDNESDAY:
			d = "Wednesday";
			break;
		case THURSDAY:
			d = "Thursday";
			break;
		case FRIDAY:
			d = "Friday";
			break;
		case SATURDAY:
			d = "Saturday";
			break;
		case SUNDAY:
			d = "Sunday";
			break;
		default:
			d = "Unknown";		
		}
		
		return d;
	}
	
	@SuppressLint("SimpleDateFormat") public static DAYS calendarToDay(Calendar time)
	{

		if(time == null)
		{
			return null;
		}
		
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy");
		String day = formatter.format(today);
		
		
		if(day.startsWith("Mon"))
		{
			return DAYS.MONDAY;
		}
		else if(day.startsWith("Tue"))
		{
			return DAYS.TUESDAY;
		}
		else if(day.startsWith("Wed"))
		{
			return DAYS.WEDNESDAY;
		}
		else if(day.startsWith("Thu"))
		{
			return DAYS.THURSDAY;
		}
		else if(day.startsWith("Fri"))
		{
			return DAYS.FRIDAY;
		}
		else if(day.startsWith("Sat"))
		{
			return DAYS.SATURDAY;
		}
		else if(day.startsWith("Sun"))
		{
			return DAYS.SUNDAY;
		}
		else
			return DAYS.UNKNOWN;
	}
	
	public static DAYS getNextDay(DAYS day)
	{
		
		switch(day)
		{
		case MONDAY:
			return DAYS.TUESDAY;
		case TUESDAY:
			return DAYS.WEDNESDAY;
		case WEDNESDAY:
			return DAYS.THURSDAY;
		case THURSDAY:
			return DAYS.FRIDAY;
		case FRIDAY:
			return DAYS.SATURDAY;
		case SATURDAY:
			return DAYS.SUNDAY;
		case SUNDAY:
			return DAYS.MONDAY;
		default:
			return DAYS.UNKNOWN;		
		}
	}
	
	public static DAYS getPreviousDay(DAYS day)
	{
		switch(day)
		{
		case FRIDAY:
			return DAYS.THURSDAY;
		case THURSDAY:
			return DAYS.WEDNESDAY;
		case WEDNESDAY:
			return DAYS.TUESDAY;
		case SATURDAY:
			return DAYS.FRIDAY;
		case MONDAY:
			return DAYS.SUNDAY;
		case TUESDAY:
			return DAYS.MONDAY;
		case SUNDAY:
			return DAYS.SATURDAY;
		default:
			return DAYS.UNKNOWN;		
		}
	}
	
	public static DAYS intToDay(int day)
	{
		switch(day)
		{
		case 0:
			return DAYS.MONDAY;
		case 1:
			return DAYS.TUESDAY;
		case 2:
			return DAYS.WEDNESDAY;
		case 3:
			return DAYS.THURSDAY;
		case 4:
			return DAYS.FRIDAY;
		case 5:
			return DAYS.SATURDAY;
		case 6:
			return DAYS.SUNDAY;
		default:
			return DAYS.UNKNOWN;
		}
	}
}
