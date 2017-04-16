package com.hungryhackers.buzzme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

/**
 * Created by YourFather on 09-04-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Long id = intent.getLongExtra("ID", -1);

        Reminder reminder = new Select().from(Reminder.class).where("id = ?", id).executeSingle();

        Intent i = new Intent(context, AlarmDetailsActivity.class);
        i.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(reminder.title)
                .setContentText(reminder.description)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(reminder.getId().intValue(), mBuilder.build());
    }
}
