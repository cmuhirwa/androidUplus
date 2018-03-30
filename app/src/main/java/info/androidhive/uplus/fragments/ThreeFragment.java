package info.androidhive.uplus.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.EventsContract;
import info.androidhive.uplus.EventsDb;
import info.androidhive.uplus.R;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.adapters.ReciclerViewDataAdapter;
import info.androidhive.uplus.models.SectionDataModel;
import info.androidhive.uplus.models.SingleItemModel;
import io.reactivex.Single;


public class ThreeFragment extends Fragment{
    FloatingActionButton btn;
    private Toolbar toolbar;
    ArrayList<SectionDataModel> allSampleData;
    RecyclerView my_recycler_view;
    ArrayList<String>eventName=new ArrayList<String>();
    ArrayList<String>eventCover=new ArrayList<String>();
    ArrayList<String>eventLocation=new ArrayList<String>();
    EventsDb eventsDb;
    public static int oGroups =0;

    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.events, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        String myDataFromActivity = activity.getMyData();
        oGroups = Integer.parseInt(myDataFromActivity);
        Log.e("SentData",myDataFromActivity);


        allSampleData = new ArrayList<SectionDataModel>();
        eventsDb=new EventsDb(getActivity());

        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        ReciclerViewDataAdapter adapter = new ReciclerViewDataAdapter(getActivity(), allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
        createDummyData();
       // loopLocalData();
        return view;
    }
    public void createDummyData() {
        for (int i = 1; i <= 3; i++) {

            SectionDataModel dm = new SectionDataModel();

            if(i == 1)
            {
                dm.setHeaderTitle("Upcoming");

                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                if(eventName.size()>0)
                {
                    eventName.clear();
                    eventCover.clear();
                    eventLocation.clear();
                }
                String buffer="";
                Integer nGroups = 0;
                Cursor cursor=eventsDb.getAllEvents();
                while (cursor.moveToNext())
                {
                    nGroups++;

                    eventName.add(cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_NAME)));
                    singleItem.add(new SingleItemModel(cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_COVER)), cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_LOCATION)), cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_ID)), cursor.getString(cursor.getColumnIndex(EventsContract.EVENTTICKETSTATUS)), cursor.getString(cursor.getColumnIndex(EventsContract.EVENTTICKETCODE))));
                    buffer+=(cursor.getString(cursor.getColumnIndex(EventsContract.EVENT_NAME)))+"\n";
                    Log.e("Tcc EVENTTICKETCODE", cursor.getString(cursor.getColumnIndex(EventsContract.EVENTTICKETCODE)));
                    Log.e("Tcc EVENTTICKETSTATUS", cursor.getString(cursor.getColumnIndex(EventsContract.EVENTTICKETSTATUS)));
                }
                cursor.close();
                //Toast.makeText(getActivity(), "oldGroup: "+oGroups+" /New Groups: "+nGroups,Toast.LENGTH_LONG).show();
                if(nGroups != oGroups)
                {
                    dm.setAllItemsInSection(singleItem);
                    oGroups = nGroups;
                }
            }
//            else if(i == 2)
//            {
//                dm.setHeaderTitle("Passed");
//
//                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
//                for (int j = 1; j <= 5; j++) {
//                    if(j == 1) { singleItem.add(new SingleItemModel("East Africa Party", "http://akokanya.com/uploads/event/2017event_54cf6c9d801c23912bcb1fca4d9982503258be5e2.jpeg", "Amahoro Stadium", "passed")); }
//                    else if(j == 2) { singleItem.add(new SingleItemModel("Holiday Cheer", "http://akokanya.com/uploads/event/2017event_1604fc66534f256c9cc2b6a0b88573b9ee5375a31.jpeg", "Convention Centre", "passed")); }
//                    else if(j == 3) { singleItem.add(new SingleItemModel("Rwanda Connect Gala", "http://akokanya.com/uploads/event/2017event_fff8ec60454a3dae89c396cc84d27959c5923cad2.jpg", "Expo Ground", "passed")); }
//                    else if(j == 4) { singleItem.add(new SingleItemModel("Rwanda Modesty Fashion", "http://akokanya.com/uploads/event/2017event_75e5d9ada693afe6357ccfcf12f7fc617f08f5f02.jpeg", "Serena Hotel", "passed")); }
//
//                }
//                dm.setAllItemsInSection(singleItem);
//            }
//            else if(i == 3)
//            {
//                dm.setHeaderTitle("Others");
//
//                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
//                for (int j = 1; j <= 1; j++) {
//                    singleItem.add(new SingleItemModel("Beer Fest", "12", "Golden Tulip", "others"));
//                }
//                dm.setAllItemsInSection(singleItem);
//            }


            allSampleData.add(dm);

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
                        //final Toast toast  = Toast.makeText(getActivity(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        createDummyData();
                    }
                });
            }
        },0,20000);
    }

}




