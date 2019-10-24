package upsc.motivational.quotesforu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_splash );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent().hasExtra("notification")) {

          startActivity(new Intent(this,OwnQuotes.class));
            finish();

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 3000);
        }
    }
}
