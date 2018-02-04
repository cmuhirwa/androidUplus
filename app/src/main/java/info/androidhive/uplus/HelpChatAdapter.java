package info.androidhive.uplus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.uplus.activity.SharedPrefManager;

public class HelpChatAdapter extends RecyclerView.Adapter<HelpChatAdapter.MyViewHolder> {

    private List<HelpChats> chatList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Context context;
    private String userId;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, msg, name;

        public MyViewHolder(View view) {
            super(view);
            title   = (TextView) view.findViewById(R.id.sender);
            msg     = (TextView) view.findViewById(R.id.msg);
            name    = (TextView) view.findViewById(R.id.name);
        }
    }

    public HelpChatAdapter(List<HelpChats> chatList,String userId,Context context) {
        this.context    = context;
        this.userId     = userId;
        this.chatList   = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        HelpChats message = (HelpChats) chatList.get(position);

        if(message.getChatMsg().matches(userId) ){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_help_row, parent, false);
            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HelpChats chatHelp = chatList.get(position);
        holder.title.setText(chatHelp.getChatUserName());
        holder.msg.setText(chatHelp.getChatMsg());
        holder.name.setText(chatHelp.getChatName());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}