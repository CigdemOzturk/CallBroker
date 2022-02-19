package com.ica.util.command;

import android.content.Context;
import android.media.AudioManager;

/**
 * 
 * @author Cigdem
 *
 */

// I need to find a way to remove the dependency between AbstractCommand and Context
public class AudioManagerAdaptor 
{
	private static AudioManager audioManager = null;
	
	public static enum RING_MODE
	{
		RINGING_MODE,
		SILENT_MODE,
		VIBRATE_MODE
	}
	
	private AudioManager getAudioManager(Context context)
	{
		if(audioManager == null)
			audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			return audioManager;
	}
	
	public void setRingMode(Context context, RING_MODE ringMode)
	{
		switch(ringMode)
		{
		case RINGING_MODE:
			getAudioManager(context).setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			getAudioManager(context).setStreamVolume(AudioManager.STREAM_RING, getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_SHOW_UI);
			break;
		case VIBRATE_MODE:
			getAudioManager(context).setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			break;
		case SILENT_MODE:
			getAudioManager(context).setRingerMode(AudioManager.RINGER_MODE_SILENT);
			break;
		}
	}
	
	public void setRingVolume(Context context, int vol)
	{
		if(vol < 0)
			vol = 0;
		else if(vol > getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC))
			vol = getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		getAudioManager(context).setStreamVolume(AudioManager.STREAM_RING, vol, AudioManager.FLAG_SHOW_UI);
	}
}
