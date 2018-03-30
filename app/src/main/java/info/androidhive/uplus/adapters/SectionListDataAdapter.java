package info.androidhive.uplus.adapters;

/**
 * Created by RwandaFab on 8/28/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import info.androidhive.uplus.EventBokedQrPage;
import info.androidhive.uplus.EventPage;
import info.androidhive.uplus.R;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.models.SingleItemModel;

import java.io.ByteArrayOutputStream;
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
        SingleItemRowHolder mh = new SingleItemRowHolder(v, mContext);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        SingleItemModel singleItem = itemsList.get(i);
        holder.tvTitle.setText(singleItem.getName());
        holder.eventLoaction.setText(singleItem.getEventLocation());
        holder.eventIdHidden.setText(singleItem.getSectionType());
        holder.eventTicketStatus.setText(singleItem.getEventTicketStatus());
        holder.eventTicketCode.setText(singleItem.getEventTicketCode());
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

        protected TextView tvTitle, eventLoaction, eventIdHidden, eventTicketStatus, eventTicketCode;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view,final Context context){
            super(view);
            this.tvTitle        = (TextView) view.findViewById(R.id.tvTitle);
            this.eventLoaction  = (TextView) view.findViewById(R.id.eventLocation);
            this.itemImage      = (ImageView) view.findViewById(R.id.itemImage);
            this.eventIdHidden  = (TextView) view.findViewById(R.id.eventIdHidden);
            this.eventTicketStatus  = (TextView) view.findViewById(R.id.eventTicketStatus);
            this.eventTicketCode    = (TextView) view.findViewById(R.id.eventTicketCode);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventTicketCodePass = eventTicketCode.getText().toString();
                    itemImage.buildDrawingCache();
                    Bitmap bitmap = itemImage.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();

                    if(eventTicketCodePass != null && !eventTicketCodePass.isEmpty()) {

                        Intent intent = new Intent(context, EventBokedQrPage.class);
                        intent.putExtra("EventName", tvTitle.getText());
                        intent.putExtra("EventLocation", eventLoaction.getText());
                        intent.putExtra("EventId", eventIdHidden.getText());
                        intent.putExtra("qrCode", eventTicketCode.getText());
                        intent.putExtra("eventCover", b);
                        // intent.putExtra("EventImage",imageUrl);
                        HomeActivity.homepage.startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(context, EventPage.class);
                        intent.putExtra("EventName", tvTitle.getText());
                        intent.putExtra("EventLocation", eventLoaction.getText());
                        intent.putExtra("EventId", eventIdHidden.getText());
                        intent.putExtra("eventCover", b);
                        // intent.putExtra("EventImage",imageUrl);
                        HomeActivity.homepage.startActivity(intent);

                    }
                    //Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}