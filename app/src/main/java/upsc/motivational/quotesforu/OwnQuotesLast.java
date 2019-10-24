package upsc.motivational.quotesforu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class OwnQuotesLast extends AppCompatActivity {

    TextView quote,name;
    ImageView left,right,whatsapp,share;
    List<String> quotes,names;
    static int pos;
    AdView adView;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_quotes_last);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Bundle b= getIntent().getExtras();
        quotes = b.getStringArrayList("quotes");
        names = b.getStringArrayList("names");
        pos = b.getInt("pos");

        adView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.inter));

        quote = findViewById(R.id.quote);
        name=findViewById(R.id.name);
        left = findViewById(R.id.left);
        whatsapp = findViewById(R.id.whatsapp);
        share = findViewById(R.id.share);
        right = findViewById(R.id.right);


        quote.setText(quotes.get(pos));
        name.setText("\""+names.get(pos)+"\"");


       left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos>0) {
                    Toast.makeText(getApplicationContext(), "Prev.", Toast.LENGTH_SHORT).show();
                    pos--;
                    quote.setText(quotes.get(pos));
                    name.setText("\""+names.get(pos)+"\"");

                    if(pos%7==0)
                    {
                        interstitialAd.loadAd(adRequest);
                        showInter();
                    }
                    else if(pos%5==0)
                    {
                        adView.loadAd(adRequest);
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), "No More quotes.", Toast.LENGTH_SHORT).show();
                    pos=0;
                }
            }
        });


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "com.whatsapp";
                if (ShowDesign.isAppInstalled(OwnQuotesLast.this, str)) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,quotes.get(pos)+"\n\n"+"Download app for more \nhttps://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en");
                    intent.setPackage(str);
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent, "Share Quote via"));

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Whatsapp is not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,quotes.get(pos)+"\n\n"+"Download app for more \nhttps://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share quote via"));

            }
        });



        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pos<quotes.size()-1) {
                    Toast.makeText(getApplicationContext(), "Next.", Toast.LENGTH_SHORT).show();
                    pos++;
                    quote.setText(quotes.get(pos));
                    name.setText("\""+names.get(pos)+"\"");


                    if(pos%7==0)
                    {
                        interstitialAd.loadAd(adRequest);
                        showInter();
                    }
                    else if(pos%5==0)
                    {
                        adView.loadAd(adRequest);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "No More quotes.", Toast.LENGTH_SHORT).show();
                    pos=quotes.size()-1;
                }
            }
        });





    }

    public void showInter()
    {
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                if(interstitialAd.isLoaded())
                    interstitialAd.show();
            }
        });
    }
}
