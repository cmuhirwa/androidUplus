package info.androidhive.uplus.activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import info.androidhive.uplus.R;

import static android.R.attr.data;

/**
 * Created by delaroy on 2/13/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<String> mDataset=new ArrayList<String>();
    private ArrayList<String> mImage=new ArrayList<String>();
    private ArrayList<String> mAmount=new ArrayList<String>();
    private ArrayList<String> mType=new ArrayList<String>();
    Context ctx;
    Uri uri;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
       // public CardView mCardView;
        public TextView mTextView, moneyTextView, textView2, mMoneyOut;
        public ImageView imageView;
        public RelativeLayout relativeLayout;// mCardView;
        public MyViewHolder(View v){
            super(v);

            //mCardView       = (RelativeLayout) v.findViewById(R.id.card1);
            mTextView       = (TextView) v.findViewById(R.id.txtMember);
            moneyTextView   = (TextView) v.findViewById(R.id.textView3);
            textView2       = (TextView) v.findViewById(R.id.textView2);
            //mMoneyOut     = (TextView) v.findViewById(R.id.outAmount);
            imageView       = (ImageView) v.findViewById(R.id.imageView);
            relativeLayout  = (RelativeLayout) v.findViewById(R.id.memberCard);

        }

    }

    public RecyclerAdapter(ArrayList<String> mDataset, ArrayList<String> mImage,ArrayList<String> mAmount,ArrayList<String> mType, Context ctx){
        this.mDataset   = mDataset;
        this.mImage     = mImage;
        this.mAmount    = mAmount;
        this.mType      = mType;
        this.ctx        = ctx;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position){
        holder.mTextView.setText(mDataset.get(position));
        holder.moneyTextView.setText(currencyConverter(mAmount.get(position)));
        holder.textView2.setText(mType.get(position));
        //holder.mMoneyOut.setText("100");

        uri = Uri.parse(mImage.get(position));
        // Context context = holder.imageView.getContext();

        if(isNetworkAvailable())
        {
            if(mImage.get(position)!="")
            {
                Picasso.with(ctx).load(uri).into(holder.imageView,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        Picasso.with(ctx).load(uri).into(holder.imageView);
                    }

                    @Override
                    public void onError() {
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        int color1 = generator.getRandomColor();
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(textTobeConverted(mDataset.get(position)), color1);
                        holder.imageView.setImageDrawable(drawable);
                    }
                });
            }
            else
            {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(textTobeConverted(mDataset.get(position)), color1);
                holder.imageView.setImageDrawable(drawable);
            }
        }
        else
        {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(textTobeConverted(mDataset.get(position)), color1);
            holder.imageView.setImageDrawable(drawable);
        }

        /*
        Picasso.with(context).load(uri).into(holder.imageView);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(textTobeConverted(mDataset.get(position)), color1);
        holder.imageView.setImageDrawable(drawable);
        */

//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ctx, mDataset.get(position), Toast.LENGTH_LONG).show();
//                //creating a popup menu
//                /*
//                PopupMenu popupMenu = new PopupMenu(ctx, holder.relativeLayout);
//                //inflating menu from xml resource
//                popupMenu.inflate(R.menu.member_menu);
//
//
//
//                //PopupMenu popupMenu = new PopupMenu(ctx, holder.mCardView);
//                //popupMenu.getMenuInflater().inflate(R.menu.member_menu, popupMenu.getMenu());
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(ctx, mDataset.get(position) +" " + item.getTitle(), Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                });
//
//                popupMenu.show();
//                */
//            }
//        });
    }

    public String currencyConverter(String data)
    {
        Locale locale = new Locale("RWF", "RWF");
        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,### RWF");
        return(decimalFormat.format(value));
    }

    public String textTobeConverted(String data)
    {
        String newData="D";
        int length=data.length();
        if(length>0)
        {
            newData=data.substring(0,1);
        }
        return newData;
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    //function to check if network is avalaible or not
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
