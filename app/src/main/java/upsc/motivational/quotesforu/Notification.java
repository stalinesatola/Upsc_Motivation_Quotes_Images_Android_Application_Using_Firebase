package upsc.motivational.quotesforu;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification extends AppCompatActivity {

    TextView notification;
    AdRequest adRequest;
    AdView adView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        adView = findViewById(R.id.adView);
        notification = findViewById(R.id.notification);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading notification...");
        pd.show();
        pd.setCancelable(false);
        notification.setText("");


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.dismiss();

                String note = dataSnapshot.child("Note").getValue(String.class);
                notification.setText(note);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        pd.dismiss();
        finish();
    }
}
