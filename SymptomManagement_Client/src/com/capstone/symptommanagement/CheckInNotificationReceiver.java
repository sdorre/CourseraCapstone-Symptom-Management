package com.capstone.symptommanagement;

import java.text.DateFormat;
import java.util.Date;

import com.capstone.symptommanagement.ui.CheckInActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class CheckInNotificationReceiver extends BroadcastReceiver {
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;
	private static final String TAG = "CheckInNotificationReceiver";

	// Notification Text Elements
	private final CharSequence tickerText = "Check In Reminder !";
	private final CharSequence contentTitle = "Check In Reminder";
	private final CharSequence contentText = "You had to do a new Check In";

	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	
	// Notification Sound and Vibration on Arrival
	private Uri soundURI = Uri
			.parse("android.resource://com.capstone.symptommanagement/"
					+ R.raw.alarm_rooster);
	private long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public void onReceive(Context context, Intent intent) {

		mNotificationIntent = new Intent(context, CheckInActivity.class);
		mNotificationIntent.putExtra("username", intent.getExtras().getString("username"));
		mNotificationIntent.putExtra("password", intent.getExtras().getString("password"));
		
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setTicker(tickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setContentText(contentText).setContentIntent(mContentIntent)
				.setSound(soundURI).setVibrate(mVibratePattern);

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		
		Log.i(TAG,"Sending notification at:" + DateFormat.getDateTimeInstance().format(new Date()));

	}
}
