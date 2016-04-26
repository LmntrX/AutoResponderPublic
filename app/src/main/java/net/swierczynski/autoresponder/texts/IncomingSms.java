package net.swierczynski.autoresponder.texts;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import net.swierczynski.autoresponder.AR_Application;
import net.swierczynski.autoresponder.R;
import net.swierczynski.autoresponder.preferences.UserPreferences;

/***
 * Created by livin on 24/4/16.
 */
public class IncomingSms extends BroadcastReceiver {

    String phoneNumber="", message="";

    Context mCtx;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        mCtx = context;

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                assert pdusObj != null;
                for (Object aPdusObj : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);


                } // end for loop
            } // bundle is null

            check(phoneNumber,message);

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void notifyUser(String no){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+no));
        PendingIntent pendingIntent = PendingIntent.getService(AR_Application.getContext(),0,callIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(AR_Application.getContext());
        notificationBuilder.setSmallIcon(R.drawable.icon)
                .setContentTitle("Missed Urgent Call")
                .setContentText(no)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) AR_Application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(45,notification);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void check(String phoneNumber, String message) {


            if (UserPreferences.readLastNotifiedNumber(mCtx).equals(phoneNumber)  && UserPreferences.readAutoRespondToTextsState(mCtx)){
                if (message.contains("yes") || message.contains("Yes") || message.contains("YES")){
                    Vibrator v = (Vibrator) mCtx.getSystemService(Context.VIBRATOR_SERVICE);
                    //v.vibrate(10000);
                    notifyUser(phoneNumber);
                    v.vibrate(new long[]{1000,1000,1000,1000},-1);
                    /*v.vibrate(new long[]{75, 25, 75, 25, 75, 25, 75, 525, 75, 25, 75, 25, 75, 25, 75, 25, 75,
                            25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75,
                            525, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75,
                            25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 525, 75, 25, 75, 25, 75, 25, 75, 25, 75,
                            25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75,
                            525, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75,
                            25, 75, 225},-1);*/

                    UserPreferences.writeLastNotifiedNumber(mCtx,"-1");
                }
            }
    }
}
