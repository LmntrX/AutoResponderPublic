package net.swierczynski.autoresponder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Nirmal on 3/24/2016.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ImageView imgView = (ImageView)findViewById(R.id.sp);
        imgView.setBackgroundResource(R.drawable.splashpic);

        AnimationDrawable frameAnimation =
                (AnimationDrawable) imgView.getBackground();

      frameAnimation.start();


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);

                       Intent intent=new Intent(getApplicationContext(),AutoResponder.class);
                      startActivity(intent);
                    // After 5 seconds redirect to another intent


                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();


    }
    protected void onDestroy() {

        super.onDestroy();

    }
}
