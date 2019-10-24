package upsc.motivational.quotesforu;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Gallery_Show extends AppCompatActivity {


    FirebaseDatabase fb;
    DatabaseReference db;
    List<String> links = new ArrayList<>();
    List<String> code = new ArrayList<>();
    int opt = -1;
    ProgressDialog pd;
    RecyclerView recyclerView;
    String optName;
    Toolbar toolbar;
    AdView adView;
    GridLayoutManager gridLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


            fb = FirebaseDatabase.getInstance();
            db = fb.getReference();

            pd = new ProgressDialog(Gallery_Show.this);
            pd.setMessage("Loading...");


            recyclerView = findViewById(R.id.recycle);
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

            Bundle bundle = getIntent().getExtras();
            opt = bundle.getInt("pos");

        if (opt == 1){
            optName = "SVNQuotes";
            toolbar.setTitle("Swami VivekaNand Quotes");
        }
        else if (opt == 2){
                optName = "UpscForU";
                toolbar.setTitle("UPSC4U Quotes");
            }


            else if (opt == 3){
                optName = "Legends";
                toolbar.setTitle("APJ Abdul Kalam Quotes");

            }


        else if (opt == 4){
            optName = "Upsc";
            toolbar.setTitle("UPSC Thoughts");
        }


        else if(opt==5){
                optName = "English";
                toolbar.setTitle("English Motivation");

            }

            else if(opt==6){
                optName = "Hindi";
                toolbar.setTitle("Hindi Motivation");
            }

            else if(opt==7) {
            optName = "Poem";
            toolbar.setTitle("Motivational Poem");
        }

        else if(opt==8){
            optName = "MoreMotivation";
            toolbar.setTitle("More Motivation");
        }

        else if(opt==9){
            optName = "IasLegends";
            toolbar.setTitle("IAS Toppers");
        }

        recyclerView.setLayoutManager(gridLayoutManager);

                pd.show();
                db = db.child(optName);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        links.clear();


                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            links.add(shot.child("Link").getValue().toString());
                            code.add(shot.getKey());
                            show();

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Network Connection Error.", Toast.LENGTH_LONG).show();
                    }
                });
            }


        public void show () {
            if (!links.isEmpty()) {
                pd.dismiss();
                recyclerView.setAdapter(new GalleryGridAdapter(Gallery_Show.this, links, optName, code));
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Network Connection Error.", Toast.LENGTH_LONG).show();
            }
        }

}
