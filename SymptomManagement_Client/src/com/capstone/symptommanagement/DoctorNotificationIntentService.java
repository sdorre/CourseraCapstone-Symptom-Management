package com.capstone.symptommanagement;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class DoctorNotificationIntentService extends IntentService {

	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public DoctorNotificationIntentService() {
		super("DoctorNotificationIntentService");
	}

	public static final String TAG = "DoctorNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		
		//GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		//String messageType = gcm.getMessageType(intent);
		
		String title = extras.getString("title");
		String message = extras.getString("message");
		sendNotification(title, message);
		
		Log.i(TAG, "Received: " + extras.toString());
	
		DoctorNotificationReceiver.completeWakefulIntent(intent);

	}

	private void sendNotification(String title, String msg) {

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		//PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		//		new Intent(this, DoctorHomeActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(android.R.drawable.stat_sys_warning)
				.setContentTitle(title)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		//mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());
		Log.d(TAG, "Notification sent successfully.");
	}

}