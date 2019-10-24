package upsc.motivational.quotesforu;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] name = {
            "Daily\nMotivation",
            "Swami\nVivekaNanda",
            "Upsc4u\nThoughts",
            "APJ Abdul\nKalam Quotes",
            "Upsc\nThoughts",
            "English\nMotivation",
            "Hindi\nMotivation",
            "Motivational\nPoem",
            "More\nMotivation",
            "IAS\nTopper\nImages"

    };

    Integer[] images = {
            R.drawable.daily1,
            R.drawable.svn,
            R.drawable.upsc4u1,
            R.drawable.apj2,
            R.drawable.upsc1,
            R.drawable.english1,
            R.drawable.hindi1,
            R.drawable.poem1,
            R.drawable.moremotivation,
            R.drawable.ias
    };

    ConnectionDetector cd;
    ImageView imageView;

    List<String> linkSet = new ArrayList<>();
    ProgressDialog pd;
    DatabaseReference db,versionDb;

    Date date = new Date();
    String sdate = date.toString();
    String[] time = sdate.split( " " );

    AlertDialog alert;
   String versionFromDb,versionFromApp;
   //int noti=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       //  test Addd MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        MobileAds.initialize(this,"ca-app-pub-1621029291855475~9318726326");

        //Bundle b = getIntent().getExtras();
        //noti = b.getInt("noti");

        versionFromApp = BuildConfig.VERSION_NAME;

        cd = new ConnectionDetector(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        /////firebase messaging..................
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("generalSix") //general
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


      db = FirebaseDatabase.getInstance().getReference().child("TodayMotivation");
      versionDb = FirebaseDatabase.getInstance().getReference();

      GifImageView today = findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cd.isConnected())
                    showAlert();
                else
                 Toast.makeText(getApplicationContext(),"Network Error...",Toast.LENGTH_SHORT).show();
            }
        });


        if(!getIntent().hasExtra("noti")) {
            versionDb.child("CurrentVersion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    versionFromDb = dataSnapshot.getValue(String.class);
                    showAlertVersion();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       final RecyclerView recyclerView = findViewById(R.id.recycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(new MyAdapterRecycle(MainActivity.this, images, name));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.rateUs:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en"));
                startActivity(intent);
                break;

            case R.id.aboutUs:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                break;

            case R.id.shareApp:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, "Best motivational app for UPSC Aspirants.\nclick on the below link and download this app.\n" +
                        "https://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en");
                share.setType("text/plain");
                startActivity(share);
                break;

            case R.id.contactUs:
                Intent in = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "teamupsc4u@gmail.com", null));
                in.putExtra(Intent.EXTRA_SUBJECT, "Supported Mail");
                startActivity(Intent.createChooser(in, "use service"));
                break;

            case R.id.notification:
                startActivity(new Intent(MainActivity.this,Notification.class));
                break;


            case R.id.checkForUpdate:
                 intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en"));
                startActivity(intent);

                break;

            case R.id.more:

                if(cd.isConnected())
                startActivity(new Intent(MainActivity.this, SponsActivity.class).putExtra("opt","SponsData"));

                else
                    Toast.makeText(getApplicationContext(),"Network Connection Error.",Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showAlertVersion()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!versionFromApp.equals(versionFromDb))
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Updates Available")
                            .setMessage("Update now and get Extra new Features.")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en"));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();

                }

            }
        },3000);
    }

    public  void showAlert()
    {
          pd.show();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linkSet.clear();
                for(DataSnapshot shot:dataSnapshot.getChildren())
                {
                    linkSet.add(shot.child("Link").getValue().toString());
                }

                setDate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_SHORT).show();
            }
        });




        AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_today,null,true);
        alertdialog.setView(view);


        imageView = view.findViewById(R.id.image);
        Button positive = view.findViewById(R.id.positive);
        Button negative = view.findViewById(R.id.negative);
        //Glide.with(MainActivity.this).load(link).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);


        alert = alertdialog.create();
        alert.setCancelable(false);



        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alert.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=upsc.motivational.quotesforu&hl=en"));
                startActivity(intent);
                alert.dismiss();
            }
        });
    }


    public void setDate()
    {

        if(!linkSet.isEmpty()) {
            pd.dismiss();
            switch (time[2]) {
                case "01":
                case "11":
                case "21":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(0)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);

                    break;
                case "02":
                case "12":
                case "22":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(1)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "03":
                case "13":
                case "23":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(2)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "04":
                case "14":
                case "24":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(3)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);

                    break;
                case "05":
                case "15":
                case "25":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(4)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "06":
                case "16":
                case "26":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(5)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "07":
                case "17":
                case "27":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(6)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "08":
                case "18":
                case "28":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(7)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "09":
                case "19":
                case "29":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(8)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                case "10":
                case "20":
                case "30":
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(9)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
                    break;
                default:
                    alert.show();
                    Glide.with(MainActivity.this).load(linkSet.get(5)).thumbnail(Glide.with(MainActivity.this).load(R.drawable.loadingsingleimage)).into(imageView);
            }
        }
        else
        {
            pd.setMessage("Error Loading data...");
            pd.show();
        }

    }




}