package upsc.motivational.quotesforu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;


public class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.ViewHolder> implements RewardedVideoAdListener {
    private Activity activity;
    private List<String> link;
    private List<String> key;
    private String optName;
    private ConnectionDetector cd;
    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;
    private String inter ="ca-app-pub-1621029291855475/6738710731";
   private String video  ="ca-app-pub-1621029291855475/7862415678";

    //private String inter ="ca-app-pub-3940256099942544/1033173712";
     //private String video  ="ca-app-pub-3940256099942544/5224354917";
    private View v;




    GalleryGridAdapter(Activity activity, List<String> link, String optName, List<String> keys) {
        this.link=link;
        this.activity=activity;
        this.optName=optName;
        this.key=keys;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(optName.equals("Poem"))
          v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_poem, parent, false);
        else
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final  int position) {

        Glide.with(activity).load(link.get(position)).thumbnail(Glide.with(activity).load(R.drawable.loadingsingleimage)).into(holder.image);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        rewardedVideoAd.setRewardedVideoAdListener(this);

        interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(inter);

        cd = new ConnectionDetector(activity);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(cd.isConnected()) {
                    //Toast.makeText(activity, optName + " " + key.get(position), Toast.LENGTH_SHORT).show();

                    if(position==11 || position==23)
                        loadRewardedVideo();

                    else if(position==4 || position==14 || position==29) {
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        showAd();
                    }

                    Intent in = new Intent(activity, ShowDesign.class);
                    in.putExtra("pos", position);
                    in.putExtra("optName", optName);
                        in.putStringArrayListExtra("listLinks", (ArrayList<String>)link);
                        activity.startActivity(in);

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
        return link.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item);
        }

    }

    public void loadRewardedVideo()
    {
        rewardedVideoAd.loadAd(video,new AdRequest.Builder().build());
    }

    public void showAd()
    {
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                if(interstitialAd.isLoaded())
                    interstitialAd.show();
            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {

        if(rewardedVideoAd.isLoaded())
            rewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

}
