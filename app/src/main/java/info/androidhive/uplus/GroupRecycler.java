package info.androidhive.uplus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by RwandaFab on 7/27/2017.
 */

public class GroupRecycler extends RecyclerView.Adapter<GroupRecycler.GroupViewHolder> {
    private ArrayList<GroupData>arrayList=new ArrayList<GroupData>();
    private String initialData1,initialData2;
    Context context;

    public GroupRecycler(ArrayList<GroupData>arrayList,String initialData1,String initialData2)
    {
        this.arrayList=arrayList;
        this.initialData1=initialData1;
        this.initialData2=initialData2;
    }
    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        GroupViewHolder groupViewHolder=new GroupViewHolder(view);

        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {

        GroupData groupData=new GroupData(this.initialData1,this.initialData2);
        holder.txtGroup.setText(groupData.getGroupName());
        holder.txtTargetAmount.setText(groupData.getGroupTargetAmount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView txtGroup,txtTargetAmount;


        public GroupViewHolder(View view)
        {
           super(view);
            txtGroup=(TextView)view.findViewById(R.id.tv_text);
            txtTargetAmount=(TextView)view.findViewById(R.id.tv_blah);



        }
    }
}
