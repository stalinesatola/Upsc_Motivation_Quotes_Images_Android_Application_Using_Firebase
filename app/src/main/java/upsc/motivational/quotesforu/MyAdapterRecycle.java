package upsc.motivational.quotesforu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MyAdapterRecycle extends RecyclerView.Adapter<MyAdapterRecycle.ViewHolder>{

    private Activity activity;
    private Integer[] images;
    private  String[] name;
    private ConnectionDetector cd;
    private InterstitialAd interstitialAd;

    MyAdapterRecycle(Activity activity, Integer[] images, String[] name) {
        this.activity = activity;
        this.images = images;
        this.name = name;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final  int position) {
        cd = new ConnectionDetector(activity);
        Glide.with(activity).load(images[position]).thumbnail(Glide.with(activity).load(R.drawable.loadingsingleimage)).into(holder.image);
        holder.textView.setText(name[position]);


        interstitialAd = new InterstitialAd(activity);
        //interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
       interstitialAd.setAdUnitId("ca-app-pub-1621029291855475/6738710731");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cd.isConnected()) {

                    if(position==0)
                    {
                        activity.startActivity(new Intent(activity,OwnQuotes.class));
                    }
                    else if(position==8)
                    {
                        activity.startActivity(new Intent(activity,SponsActivity.class).putExtra("opt","MoreMotivation"));
                    }
                    else
                    {
                        if(position==1 || position==3 || position==5 ||position==7) {
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            showAd();
                        }
                        Intent in = new Intent(activity, Gallery_Show.class);
                        in.putExtra("pos", position);
                        //in.putExtra("arr",images);
                        activity.startActivity(in);
                    }
                }
                else
                {
                    Toast.makeText(activity,"Network Connection Error.",Toast.LENGTH_SHORT).show();

                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.txt);
        }

    }

    void showAd()
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
