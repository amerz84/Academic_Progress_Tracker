package com.example.studentprogresstracker.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.studentprogresstracker.R;

import static com.example.studentprogresstracker.utilities.Constants.ASSESSMENT_ID_KEY;
import static com.example.studentprogresstracker.utilities.Constants.COURSE_ID_KEY;

public class Receiver extends BroadcastReceiver {

    static int notificationId;
    int courseId, assessmentId;
    String channel_id = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("notify_key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);

        intent.getIntExtra(COURSE_ID_KEY, courseId);
        intent.getIntExtra(ASSESSMENT_ID_KEY, assessmentId);

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_custom)
                .setContentText(intent.getStringExtra("notify_key"))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(notificationId++, notification);

    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification channel";
            String description = "notification channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            //Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
