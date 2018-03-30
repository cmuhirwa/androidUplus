package info.androidhive.uplus.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.BackgroundTask;
import info.androidhive.uplus.ConnectionDetector;
import info.androidhive.uplus.DbContract;
import info.androidhive.uplus.DbHelper;

import info.androidhive.uplus.MyAdapter;
import info.androidhive.uplus.R;
import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;

import com.baoyz.widget.PullRefreshLayout;


public class OneFragment extends Fragment{
    RecyclerView recyclerView;
    FloatingActionButton fabbtn;
    ArrayList<String>groupNames=new ArrayList<String>();
    ArrayList<String>groupIds=new ArrayList<String>();
    ArrayList<String>targetAmount=new ArrayList<String>();
    ArrayList<String>groupImage=new ArrayList<String>();
    ArrayList<String>groupBalance=new ArrayList<String>();
    DbHelper helper;
    ImageView imageView;
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout layoutHomeGuide;
    public static int counter=0;
    public static int oGroups =0;
    PullRefreshLayout layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        HomeActivity activity       = (HomeActivity) getActivity();
        String myDataFromActivity   = activity.getMyData();
        oGroups                     = Integer.parseInt(myDataFromActivity);
        Log.e("SentData",myDataFromActivity);

        View rootView   = inflater.inflate(R.layout.fragment_one, container, false);
        layoutHomeGuide =(RelativeLayout)rootView.findViewById(R.id.layoutHomeGuide);
        fabbtn          =(FloatingActionButton)rootView.findViewById(R.id.fabbtn);
        recyclerView    =(RecyclerView)rootView.findViewById(R.id.rv_recycler_view);
        layoutManager   =new LinearLayoutManager(getActivity());
        helper          =new DbHelper(getActivity());
        layout          = (PullRefreshLayout) rootView.findViewById(R.id.pullHomeRefresh);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshGroups();
                        // start refresh
                        layout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        //helper.clearTable();
        helper.recreateTable();
        //fetchLocal();
        loopLocalData();
        if(activity instanceof HomeActivity){

        }
        Context context = getActivity();
        if(SharedPrefManager.getInstance(context).isLoggedIn()){
            if(isNetworkAvailable())
            {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here
                    if((new ConnectionDetector(getContext())).isConnectingToInternet()){
                        callLopper();
                        Log.d("internet status","Internet Access");
                    }else{
                        Log.d("internet status","no Internet Access");
                    }
                }
            }
        }

        displayGroups();
        showAlert(fabbtn);
        return rootView;
    }
    public void showAlert(FloatingActionButton btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(getActivity(), AddGroup.class);
                startActivity(t);
            }
        });

    }

    //function to check if network is avalaible or not
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo           = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //check if internet is working
    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void displayGroups()
    {
        if(groupNames.size()>0)
        {
            groupNames.clear();
            groupIds.clear();
            targetAmount.clear();
        }
        String buffer="";

        Cursor cursor=helper.getAllData();
        Integer nGroups = 0;
        Log.e("GroupsWeDHave", String.valueOf(oGroups));
        while (cursor.moveToNext())
        {
            nGroups++;
            groupIds.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
            groupNames.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)));
            targetAmount.add(cursor.getString(cursor.getColumnIndex(DbContract.TARGET_AMOUNT)));
            groupImage.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUPIMAGE)));
            groupBalance.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_BALANCE)));
            // Toast.makeText(getActivity(),String.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_BALANCE))),Toast.LENGTH_LONG).show();
            buffer+=(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)))+"\n";

        }
        cursor.close();

        if(nGroups != oGroups)
        {
            Log.e("GroupsWeHad", String.valueOf(oGroups));
            adapter=new MyAdapter(groupNames,targetAmount,groupImage,getActivity(),recyclerView,groupIds,groupBalance);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter.notifyDataSetChanged();
            oGroups = nGroups;
        }
    }

    public void refreshGroups()
    {
        if(groupNames.size()>0)
        {
            groupNames.clear();
            groupIds.clear();
            targetAmount.clear();
        }
        String buffer="";

        Cursor cursor=helper.getAllData();
        while (cursor.moveToNext())
        {
            groupIds.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
            groupNames.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)));
            targetAmount.add(cursor.getString(cursor.getColumnIndex(DbContract.TARGET_AMOUNT)));
            groupImage.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUPIMAGE)));
            groupBalance.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_BALANCE)));
            // Toast.makeText(getActivity(),String.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_BALANCE))),Toast.LENGTH_LONG).show();
            buffer+=(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)))+"\n";

            Log.e("NewAmount ",cursor.getString(cursor.getColumnIndex(DbContract.GROUP_BALANCE)));
        }
        cursor.close();


            adapter=new MyAdapter(groupNames,targetAmount,groupImage,getActivity(),recyclerView,groupIds,groupBalance);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter.notifyDataSetChanged();
    }

    public void fetchLocal()
    {
        for(int i=0;i<groupImage.size();i++)
        {
            // Toast.makeText(getActivity(),groupImage.get(i),Toast.LENGTH_LONG).show();
        }
    }

    public void callLopper()
    {
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity instanceof HomeActivity){
            try
            {
                if(isNetworkAvailable())
                {
                    activity.loopData();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void callFetcher()
    {
        HomeActivity activity = (HomeActivity) getActivity();
        if(activity instanceof HomeActivity){
            try
            {
                activity.loopLocal();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void loopLocalData()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getActivity(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(groupNames.size()>0)
                        {
                            layoutHomeGuide.setVisibility(View.INVISIBLE);
                            displayGroups();
                        }
                        else
                        {
                            layoutHomeGuide.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        },0,20000);
    }

}


