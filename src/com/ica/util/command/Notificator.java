package com.ica.util.command;

import java.util.Random;

import com.ica.android.ui.iCaActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Cigdem
 *
 */
public class Notificator 
{
	private static Notificator instance;
	private Context context;
	private NotificationManager notificationManager;
	
	private Notificator(Context context)
	{
		this.context = context;
		notificationManager = (NotificationManager)context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public static Notificator getInstace(Context context)
	{
		if(instance == null)
		{
			instance = new Notificator(context);
		}
		return instance;
	}
	
	public void createNotification(String title, String titleDetail, String descriptionDetail, int imageId)
	{
		int id = new Random().nextInt(100);
		Notification notification = new Notification(imageId, title, System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), id,  new Intent(context.getApplicationContext(), iCaActivity.class), 0);
		
		notification.setLatestEventInfo(context.getApplicationContext(), titleDetail, descriptionDetail, contentIntent);
		notificationManager.notify(id, notification);
	}
}
