package upsc.motivational.quotesforu;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class ShowDesign extends AppCompatActivity {

    static int pos;
    ImageView left,whatsapp,share,download,right;
    OutputStream outputStream;
    ImageView image=null;
    List list = new ArrayList<>();
    String optName;
    AdView adView;
    InterstitialAd interstitialAd;
    AdRequest adRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_design);
       setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        check();



        Bundle extra = getIntent().getExtras();
        optName = extra.getString("optName");

        if(optName.equals("Poem"))
            setContentView(R.layout.activity_poem_last);
        pos = extra.getInt("pos");
        list = extra.getStringArrayList("listLinks");

        image = findViewById(R.id.image);
        left = findViewById(R.id.left);
        whatsapp = findViewById(R.id.whatsapp);
        share = findViewById(R.id.share);
        download=findViewById(R.id.download);
        right = findViewById(R.id.right);


        adView = findViewById(R.id.adView);
       adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.inter));


        Glide.with(ShowDesign.this).load(list.get(pos)).thumbnail(Glide.with(ShowDesign.this).load(R.drawable.loadingsingleimage)).into(image);




        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(pos>0) {
                  Toast.makeText(getApplicationContext(), "Prev.", Toast.LENGTH_SHORT).show();
                  pos--;
                  Glide.with(ShowDesign.this).load(list.get(pos)).thumbnail(Glide.with(ShowDesign.this).load(R.drawable.loadingsingleimage)).into(image);


                  if(pos%7==0)
                  {
                      interstitialAd.loadAd(adRequest);
                      showInter();
                  }
                  else if(pos%5==0)
                      adView.loadAd(adRequest);


              }
                else {
                  Toast.makeText(getApplicationContext(), "No More images.", Toast.LENGTH_SHORT).show();
                  pos=0;
              }
            }
        });


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 String str = "com.whatsapp";
                 if (ShowDesign.isAppInstalled(ShowDesign.this, str)) {

                     BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                     Bitmap bitmap = drawable.getBitmap();

                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                     byte[] toByteArray = byteArrayOutputStream.toByteArray();

                     Uri parse = Uri.parse(MediaStore.Images.Media.insertImage(ShowDesign.this.getContentResolver(), BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length), "", null));

                     Intent intent = new Intent("android.intent.action.SEND");
                     intent.putExtra("android.intent.extra.STREAM", parse);
                     intent.putExtra(Intent.EXTRA_TEXT,"Best motivational app for UPSC Aspirants.\nclick on the below link and download this app.\nhttps://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en");
                     intent.setType("image/*");
                     intent.setPackage(str);
                     ShowDesign.this.startActivity(Intent.createChooser(intent, "Share image via..."));

                 } else {
                     Toast.makeText(getApplicationContext(), "Whatsapp is not Installed", Toast.LENGTH_SHORT).show();

                 }
             }
             catch (Exception e) {
                 Toast.makeText(getApplicationContext(), "Image is not loaded yet...", Toast.LENGTH_SHORT).show();
             }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    byte[] toByteArray = byteArrayOutputStream.toByteArray();

                    Uri parse = Uri.parse(MediaStore.Images.Media.insertImage(ShowDesign.this.getContentResolver(), BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length), "", null));

                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.STREAM", parse);
                    intent.putExtra(Intent.EXTRA_TEXT,"Best motivational app for UPSC Aspirants.\nclick on the below link and download this app.\nhttps://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en");
                    intent.setType("image/*");
                    ShowDesign.this.startActivity(Intent.createChooser(intent, "Share image via"));
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Image is not loaded yet...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath() + "/MotivationalQuotes/");
                    dir.mkdir();

                    File file = new File(dir, System.currentTimeMillis() + ".png");
                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Downloaded into MotivationalQuotes.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Image is not loaded yet...", Toast.LENGTH_SHORT).show();
                }
            }

        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pos<list.size()-1) {
                    Toast.makeText(getApplicationContext(), "Next.", Toast.LENGTH_SHORT).show();
                    pos++;
                    Glide.with(ShowDesign.this).load(list.get(pos)).thumbnail(Glide.with(ShowDesign.this).load(R.drawable.loadingsingleimage)).into(image);


                    if(pos%7==0)
                    {
                        interstitialAd.loadAd(adRequest);
                        showInter();
                    }
                    else if(pos%5==0)
                        adView.loadAd(adRequest);


                }
                else {
                    Toast.makeText(getApplicationContext(), "No More images.", Toast.LENGTH_SHORT).show();
                    pos=list.size()-1;
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

    public static boolean isAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }




    //permision

    public void check() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestStoragePermission();

            }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            Toast.makeText(this, "Permission needed to save status images.", Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 0) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        } else if (iArr.length == 1 && iArr[0] == 0) {
            Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, ShowDesign.class));
            //finish();
        } else {
            Toast.makeText(this, "Storage permission required\nto save status image.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
