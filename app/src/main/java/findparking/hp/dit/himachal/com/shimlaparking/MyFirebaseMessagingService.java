package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by kuush on 6/22/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static NotificationManager mNotificationManager;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());



        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Calling method to generate notification
      //  sendNotification(remoteMessage.getNotification().getBody());
       Notification noti = new Notification();
            noti = setCustomViewNotification(remoteMessage.getNotification().getBody());
        noti.defaults |= Notification.DEFAULT_LIGHTS;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        noti.defaults |= Notification.DEFAULT_SOUND;

        noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        mNotificationManager.notify(0, noti);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {


        Intent intent = new Intent(this, Main_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentTitle("Himachal Parking");
               // .setContentText(messageBody);




        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.notification : R.mipmap.ic_launcher;
    }

    private Notification setCustomViewNotification(String Message) {



        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(this, ResultActivity.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_custom_remote);
        expandedView.setTextViewText(R.id.text_view, Message);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Custom View").build();

        notification.bigContentView = expandedView;

        return notification;
    }
}
