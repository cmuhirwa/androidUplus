package info.androidhive.uplus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import info.androidhive.uplus.activity.HomeActivity;

/**
 * Created by RwandaFab on 8/16/2017.
 */

public class FirebaseMessagingService  extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {

        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setContentTitle("UPLUS");
        notification.setContentText(message);
        notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon));
        notification.setSmallIcon(R.drawable.icon);
        notification.setContentIntent(pendingIntent);
        notification.setTicker("UPLUS");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("UPLUS");
        notification.setStyle(new NotificationCompat.BigTextStyle()
        .bigText(message));
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,notification.build());
    }
}
