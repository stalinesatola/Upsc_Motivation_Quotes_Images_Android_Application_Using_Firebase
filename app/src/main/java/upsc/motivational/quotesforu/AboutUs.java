package upsc.motivational.quotesforu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity{

    String OneMan = "https://www.youtube.com/channel/UCG2fxIwiM97YzsAJDcDODrg";
    String Upscinsta = "https://instagram.com/upsc4u?igshid=38moyc54sjiq";
    String UpscFacebook = "https://www.facebook.com/UPSC4U/";
    String UpscWebsite = "https://www.upsc4u.com/";
    String apurvaInsta = "https://instagram.com/upsc_motivation_ias?igshid=1a6wjrowj4we7";

    String DevloperGoogle = "https://play.google.com/store/search?q=pub%3AGiri%20Softs&c=apps";

    Intent intent;

    TextView t11,t12,t21,t22;
    LinearLayout b11,b12,b21,b22,b23,writer1,writer2;
    Button joinUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        t11 = findViewById(R.id.txt11);
        t12 = findViewById(R.id.txt12);
        t21 = findViewById(R.id.txt21);
        t22 = findViewById(R.id.txt22);


        b11 = findViewById(R.id.btn11);
        b12 = findViewById(R.id.btn12);
        b21 = findViewById(R.id.btn21);
        b22 = findViewById(R.id.btn22);
        b23 = findViewById(R.id.btn23);
        joinUs = findViewById(R.id.joinUs);

        writer1 = findViewById(R.id.writer1);
        writer2 = findViewById(R.id.writer2);

        t22.setText("UPSC4U is a motivational youtube channel and instagram page for Upsc Aspirants.\n"+
                "UPSC4U instagram page is followed by many upsc officers and aspirants.\n"+
                "All motivational thoughts are written by Aditya Kumar Mishra. I hope this app helps you for everyday motivation for study and remember your dreams. Thank you(RDM).");


       b11.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               intent = new Intent(Intent.ACTION_VIEW);
               intent.setData( Uri.parse(DevloperGoogle));
               startActivity(intent);


           }
       });

        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","mailtomr.giri@gmail.com",null));
                in.putExtra(Intent.EXTRA_SUBJECT,"Supported Mail");
                startActivity(Intent.createChooser(in,"use service"));

            }
        });

        b21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(Upscinsta));
                startActivity(intent);
            }
        });

        b22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(UpscFacebook));
                startActivity(intent);
            }
        });
        b23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(UpscWebsite));
                startActivity(intent);
            }
        });

        joinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(OneMan));
                startActivity(intent);
            }
        });


        writer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(Upscinsta));
                startActivity(intent);
            }
        });

        writer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(apurvaInsta));
                startActivity(intent);
            }
        });

    }


}
