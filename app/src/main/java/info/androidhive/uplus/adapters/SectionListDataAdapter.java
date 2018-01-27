package info.androidhive.uplus.adapters;

/**
 * Created by RwandaFab on 8/28/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import info.androidhive.uplus.R;
import info.androidhive.uplus.models.SingleItemModel;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SingleItemModel singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());
        holder.eventLoaction.setText(singleItem.getEventLocation());
        Context context = holder.itemImage.getContext();
        if(singleItem.getUrl()=="12")
        {
            Picasso.with(context).load(R.drawable.meddy).into(holder.itemImage);

        }else {
            Picasso.with(context).load(singleItem.getUrl()).into(holder.itemImage);

        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle, eventLoaction;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);
            this.tvTitle        = (TextView) view.findViewById(R.id.tvTitle);
            this.eventLoaction  = (TextView) view.findViewById(R.id.eventLocation);
            this.itemImage      = (ImageView) view.findViewById(R.id.itemImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}