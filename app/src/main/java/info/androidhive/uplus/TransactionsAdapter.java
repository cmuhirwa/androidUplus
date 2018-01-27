package info.androidhive.uplus;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>{
    List<TransationsList> listitems;
    public TransactionsAdapter(List<TransationsList> listitems) {
        this.listitems = listitems;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView amount,phone,userName,transactionDate;
        View status;

        public ViewHolder(View itemView) {
            super(itemView);

            amount          = (TextView) itemView.findViewById(R.id.amount);
            phone           = (TextView) itemView.findViewById(R.id.phone);
            userName        = (TextView) itemView.findViewById(R.id.userName);
            transactionDate = (TextView) itemView.findViewById(R.id.transactionDate);
            status          = (View) itemView.findViewById(R.id.separator);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionitem,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TransationsList listItem = listitems.get(position);

        holder.amount.setText(listItem.getAmount());
        holder.phone.setText(listItem.getPhone());
        holder.userName.setText(listItem.getUserName());
        holder.transactionDate.setText(listItem.getTransactionDate());
        holder.status.setBackgroundColor(Color.parseColor(listItem.getTransactionColor()));
    }


    @Override
    public int getItemCount() {
        return listitems.size();
    }

}
