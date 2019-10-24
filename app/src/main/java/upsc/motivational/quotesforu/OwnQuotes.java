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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnQuotes extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    Toolbar toolbar;
    Button send;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    String show="blank",sendStatus="blank";
    static List<String> quotes = new ArrayList<>();
    static List<String> names = new ArrayList<>();


    public static final int ITEM_PER_AD = 6;
    private List<Object> recycleItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_quotes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        send = findViewById(R.id.button);

        pd = new ProgressDialog(OwnQuotes.this);
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);

        toolbar.setTitle("Quotes List");

        recyclerView = findViewById(R.id.recycle);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sendStatus.equals("OFF") || sendStatus.equals("blank")) {
                    alertfun("mail");
                } else {
                    pd.dismiss();
                    Intent in = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "userquotes@gmail.com", null));
                    in.putExtra(Intent.EXTRA_SUBJECT, "My Quotation is here.");
                    in.putExtra(Intent.EXTRA_TEXT, "Name: " + "\n\n" + "My Quote:");
                    startActivity(Intent.createChooser(in, "use service"));
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("OwnQuotes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                show = dataSnapshot.child("Opt").getValue(String.class);
                sendStatus = dataSnapshot.child("Mail").getValue(String.class);

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
            alertfun("opt");
        }
        else
        {
            //Toast.makeText(getApplicationContext(),show,Toast.LENGTH_SHORT).show()

            databaseReference.child("ON").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recycleItems.clear();
                    names.clear();

                    for(DataSnapshot shot : dataSnapshot.getChildren()) {
                        //show = dataSnapshot.getValue().toString();
                        recycleItems.add(shot.child("Quote").getValue().toString());
                        names.add(shot.child("Name").getValue().toString());

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

    public void alertfun(final String temp)
    {
        pd.dismiss();
        //Toast.makeText(getApplicationContext(),show,Toast.LENGTH_SHORT).show();


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(OwnQuotes.this);
        LayoutInflater inflater = OwnQuotes.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null,true);
        alertdialog.setView(view);
        TextView title = view.findViewById(R.id.errorTitle);
        TextView message = view.findViewById(R.id.errorMessage);
        Button positive = view.findViewById(R.id.positive);

        title.setText("Welcome");
        message.setText("Maintenance is going on try after some time.");
        positive.setText("Ok");

        final AlertDialog alert = alertdialog.create();
        alert.setCancelable(false);
        alert.show();

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp.equals("opt"))
                finish();
                else
                    alert.dismiss();
            }
        });

    }

    public void show () {
        if (!recycleItems.isEmpty()) {

            quotes.addAll((ArrayList)recycleItems);

            getBannerAds();
            loadBannerAd();
            pd.dismiss();
            recyclerView.setAdapter(new MyAdapterOwnQuotes(OwnQuotes.this, recycleItems,quotes, names));
        } else {
            pd.setTitle("Error Loading... ");
            pd.setMessage("Try Again Later");
        }
    }

    public void getBannerAds()
    {
        for(int i=0;i<recycleItems.size();i+=ITEM_PER_AD)
        {
            final AdView adView = new AdView(OwnQuotes.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.banner));
            recycleItems.add(i,adView);
        }
    }

    public void loadBannerAd()
    {
        for(int i=0;i<recycleItems.size();i++)
        {
            Object item = recycleItems.get(i);
            if(item instanceof AdView)
            {
                final AdView adView= (AdView)item;
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
    }



    class MyAdapterOwnQuotes extends RecyclerView.Adapter implements RewardedVideoAdListener {

        private Activity activity;
        List<Object> quotes ;
        List<String> names;
        List<String> quotesList;
        List<Integer> quotePos = new ArrayList<>();
        String opt;
        private RewardedVideoAd rewardedVideoAd;
        private InterstitialAd interstitialAd;
        private String inter ="ca-app-pub-1621029291855475/6738710731";
         private String video  ="ca-app-pub-1621029291855475/7862415678";

        //private String inter ="ca-app-pub-3940256099942544/1033173712";
        //private String video  ="ca-app-pub-3940256099942544/5224354917";
        private ConnectionDetector cd;

        private static final int ITEM_QUOTE=0;
        private static final int ITEM_BANNER_AD=1;

        AdView adView;
        AdRequest adRequest;

        MyAdapterOwnQuotes(Activity activity,List<Object> quotes,List<String> quotesList,List<String> names) {
            this.activity = activity;
            this.quotes = quotes;
            this.names = names;
            this.quotesList = quotesList;

            //quotesList = (List)quotes;


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            switch (viewType) {
                case ITEM_QUOTE:
                    View quote;
                    quote = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_ownquotes_list, parent, false);
                    return new QuotesHolder(quote);

                case ITEM_BANNER_AD:

                    default:
                        View v;
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycle_ads, parent, false);
                        return new BannerViewHolder(v);

            }




        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final  int position) {


            final int viewType = getItemViewType(position);
            switch (viewType)
            {
                case ITEM_QUOTE:
                    QuotesHolder quotesHolder = (QuotesHolder)holder;
                    quotesHolder.quote.setText(quotes.get(position).toString());
                    quotePos.add(position);
                    break;
                case ITEM_BANNER_AD:

                    default:
                        BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
                        AdView adView = (AdView) quotes.get(position);

                        ViewGroup adCardView =(ViewGroup) bannerViewHolder.itemView;
                        if(adCardView.getChildCount()>0)
                        {
                            adCardView.removeAllViews();
                        }
                        if(adView.getParent()!=null)
                        {
                            ((ViewGroup) adView.getParent()).removeView(adView);
                        }

                        adCardView.addView(adView);

            }


            cd = new ConnectionDetector(activity);
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
            rewardedVideoAd.setRewardedVideoAdListener(this);

            interstitialAd = new InterstitialAd(activity);
            interstitialAd.setAdUnitId(inter);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if (viewType == ITEM_QUOTE) {

                        int pos = quotePos.indexOf(position);
                        if (cd.isConnected()) {
                            //Toast.makeText(activity, optName + " " + key.get(position), Toast.LENGTH_SHORT).show();

                            if (pos == 11  || pos == 35 || pos == 65 ||pos==105)
                                loadRewardedVideo();

                            else if (pos == 4 || pos == 14 || pos == 29 || pos == 44 || pos == 54 ||pos==72 ||pos==88|| pos==100) {
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                showAd();
                            }

                            Intent in = new Intent(activity, OwnQuotesLast.class);
                            in.putStringArrayListExtra("quotes", (ArrayList<String>) quotesList);
                            in.putStringArrayListExtra("names", (ArrayList<String>) names);
                            in.putExtra("pos", pos);
                            activity.startActivity(in);

                        } else {
                            Toast.makeText(activity, "Network Connection Error.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return quotes.size();
        }


        @Override
        public int getItemViewType(int position) {
            if(position%OwnQuotes.ITEM_PER_AD==0)
                return ITEM_BANNER_AD;
            else
                return ITEM_QUOTE;
        }


        class QuotesHolder extends RecyclerView.ViewHolder {

            TextView quote;


            QuotesHolder(View itemView) {
                super(itemView);
                quote = itemView.findViewById(R.id.quote);

            }

        }

        class BannerViewHolder extends RecyclerView.ViewHolder
        {
            BannerViewHolder(View itemView)
            {
                super(itemView);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class).putExtra("noti",1));
        finish();
    }
}
