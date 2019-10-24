package upsc.motivational.quotesforu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SponsActivity extends AppCompatActivity {

    String show="blank";
    static List<String> imageLink =new ArrayList<>();
    static List<String> desc = new ArrayList<>();
    static List<String> clickTxt = new ArrayList<>();
    static List<String> webLink = new ArrayList<>();

    ProgressDialog pd;


    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String opt;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spons);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b = getIntent().getExtras();
        opt = b.getString("opt");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(opt.equals("SponsData"))
            toolbar.setTitle("More You May Like");
        else
            toolbar.setTitle("One Man One Hundred Talent");


        pd = new ProgressDialog(SponsActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        recyclerView = findViewById(R.id.recycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(opt);
        databaseReference.child("State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                show = dataSnapshot.getValue(String.class);

                next();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void next()
    {

        if(show.equals("OFF") || show.equals("blank"))
        {

            pd.dismiss();
            //Toast.makeText(getApplicationContext(),show,Toast.LENGTH_SHORT).show();


            AlertDialog.Builder alertdialog = new AlertDialog.Builder(SponsActivity.this);
            LayoutInflater inflater = SponsActivity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.alert_dialog,null,true);
            alertdialog.setView(view);
            TextView title = view.findViewById(R.id.errorTitle);
            TextView message = view.findViewById(R.id.errorMessage);
             Button positive = view.findViewById(R.id.positive);

            title.setText("Welcome");
            message.setText("The service is disabled Now");
            positive.setText("Ok");

             final AlertDialog alert = alertdialog.create();
            alert.setCancelable(false);
            alert.show();

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
        else
        {
            //Toast.makeText(getApplicationContext(),show,Toast.LENGTH_SHORT).show()

            databaseReference.child("ON").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    imageLink.clear();
                    desc.clear();
                    clickTxt.clear();
                    webLink.clear();
                    for(DataSnapshot shot : dataSnapshot.getChildren()) {
                        //show = dataSnapshot.getValue().toString();
                        imageLink.add(shot.child("Image").getValue().toString());
                        desc.add(shot.child("Desc").getValue().toString());
                        clickTxt.add(shot.child("Click").getValue().toString());
                        webLink.add(shot.child("Link").getValue().toString());

                    }
                    show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    public void show () {
        if (!imageLink.isEmpty()) {
            pd.dismiss();
            recyclerView.setAdapter(new MyAdapterSpons(SponsActivity.this, imageLink, desc, clickTxt,webLink,opt));
        } else {
            pd.setTitle("Error Loading... ");
            pd.setMessage("Try Again Later");
        }
    }



    class MyAdapterSpons extends RecyclerView.Adapter<MyAdapterSpons.ViewHolder>{

        private Activity activity;
        List<String> imageLink ;
        List<String> desc;
        List<String> clickTxt;
        List<String> webLink ;
        String opt;

        MyAdapterSpons(Activity activity,List<String> imageLink,List<String> desc ,List<String> clickTxt,List<String> webLink,String opt) {
            this.activity = activity;
            this.imageLink = imageLink;
            this.desc = desc;
            this.clickTxt = clickTxt;
            this.webLink = webLink;
            this.opt = opt;


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v;
            if(opt.equals("SponsData"))
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_spons, parent, false);
            else
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_more, parent, false);

            return new MyAdapterSpons.ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final  int position) {
            Glide.with(activity).load(imageLink.get(position)).thumbnail(Glide.with(activity).load(R.drawable.loadingsingleimage)).into(holder.image);
            holder.desc.setText(desc.get(position));
            holder.clickTxt.setText(clickTxt.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(webLink.get(position)));
                    startActivity(intent);


                }
            });



        }

        @Override
        public int getItemCount() {
            return imageLink.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView desc;
            TextView clickTxt;


            ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.img);
                desc = itemView.findViewById(R.id.desc);
                clickTxt = itemView.findViewById(R.id.clicktxt);
            }

        }
    }

}
