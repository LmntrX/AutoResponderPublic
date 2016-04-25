package net.swierczynski.autoresponder.texts;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void check(String phoneNumber, String message) {
            if (UserPreferences.readLastNotifiedNumber(mCtx).equals(phoneNumber)  && UserPreferences.readAutoRespondToTextsState(mCtx)){
                if (message.contains("yes") || message.contains("Yes") || message.contains("YES")){
                    Vibrator v = (Vibrator) mCtx.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    v.vibrate(new long[]{75, 25, 75, 25, 75, 25, 75, 525, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 525, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 225, 75, 25, 75, 25, 75, 25, 75, 225},-1);
                    UserPreferences.writeLastNotifiedNumber(mCtx,"-1");
                }
            }
    }
}
