package info.androidhive.uplus;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import info.androidhive.uplus.R;

/**
 * Created by delaroy on 2/13/17.
 */
public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.MyViewHolder> {

    private ArrayList<String> SenderMessage=new ArrayList<String>();
    private ArrayList<String> OtherSenderMessage=new ArrayList<String>();
    int counter;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView message_text, charterName;
        public MyViewHolder(View v){
            super(v);
            message_text= (TextView) v.findViewById(R.id.message_text);
            charterName=(TextView)v.findViewById(R.id.txtChatPhone);
        }

    }

    public GroupChatAdapter(ArrayList<String> SenderMessage,ArrayList<String>OtherSenderMessage){
        this.SenderMessage=SenderMessage;
        this.OtherSenderMessage=OtherSenderMessage;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType == 1){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatbubble, parent, false);
            return new MyViewHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatbubble1, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.message_text.setText(SenderMessage.get(position));
        holder.charterName.setText(OtherSenderMessage.get(position));
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
    public int getItemCount() {
            return SenderMessage.size();
    }

}
