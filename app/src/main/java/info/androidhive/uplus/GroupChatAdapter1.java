package info.androidhive.uplus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RwandaFab on 8/20/2017.
 */

public class GroupChatAdapter1 extends RecyclerView.Adapter<GroupChatAdapter1.ViewHolder> {
    private ArrayList<String> chatNumbers=new ArrayList<String>();
    private ArrayList<String> OtherSenderMessage=new ArrayList<String>();
    public GroupChatAdapter1(ArrayList<String> OtherSenderMessage,ArrayList<String> chatNumbers)
    {
        this.OtherSenderMessage=OtherSenderMessage;
        this.chatNumbers=chatNumbers;
    }
    @Override
    public GroupChatAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbubble1, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageText.setText(OtherSenderMessage.get(position));
        holder.chatterName.setText(chatNumbers.get(position));
    }

    @Override
    public int getItemCount() {
        return OtherSenderMessage.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public TextView chatterName;
        public ViewHolder(View itemView) {
            super(itemView);
            messageText= (TextView) itemView.findViewById(R.id.message_text1);
            chatterName=(TextView)itemView.findViewById(R.id.txtChatPhone);
        }
    }
}
