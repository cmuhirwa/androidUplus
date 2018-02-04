package info.androidhive.uplus;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 29/12/2017.
 */

class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    public InvitationAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public InvitationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitemember,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(InvitationAdapter.ViewHolder holder, int position) {
        //holder.mTitle.setText(mDataset.get(position));
        Uri uri = Uri.parse(mDataset.get(position));

        Context context = holder.imageViewIcon.getContext();
        Picasso.with(context).load(uri).into(holder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView mTitle;
        public ImageView imageViewIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            //mTitle = (TextView) itemView.findViewById(R.id.title);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.memberImage);
        }
    }
}
