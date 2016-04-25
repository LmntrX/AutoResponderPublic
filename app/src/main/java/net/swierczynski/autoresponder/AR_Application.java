package net.swierczynski.autoresponder;

import android.app.Application;
import android.content.Context;

/***
 * Created by livin on 25/4/16.
 */
public class AR_Application extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
