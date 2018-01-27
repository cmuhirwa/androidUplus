package info.androidhive.uplus.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.androidhive.uplus.R;
import info.androidhive.uplus.adapters.ReciclerViewDataAdapter;
import info.androidhive.uplus.models.SectionDataModel;
import info.androidhive.uplus.models.SingleItemModel;
import io.reactivex.Single;


public class ThreeFragment extends Fragment{
    FloatingActionButton btn;
    private Toolbar toolbar;
    ArrayList<SectionDataModel> allSampleData;
    RecyclerView my_recycler_view;
    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.events, container, false);
        allSampleData = new ArrayList<SectionDataModel>();
        createDummyData();
        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        ReciclerViewDataAdapter adapter = new ReciclerViewDataAdapter(getActivity(), allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
        return view;
    }
    public void createDummyData() {
        for (int i = 1; i <= 3; i++) {

            SectionDataModel dm = new SectionDataModel();

            if(i == 1)
            {
                dm.setHeaderTitle("Upcoming");

//                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
//                for (int j = 1; j <= 1; j++) {
//                    singleItem.add(new SingleItemModel("East Africa Party", "http://akokanya.com/uploads/event/2017event_54cf6c9d801c23912bcb1fca4d9982503258be5e2.jpeg", "Amahoro Stadium"));
//                }
//
//                dm.setAllItemsInSection(singleItem);
            }
            else if(i == 2)
            {
                dm.setHeaderTitle("Passed");

                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                for (int j = 1; j <= 5; j++) {
                    if(j == 1) { singleItem.add(new SingleItemModel("East Africa Party", "http://akokanya.com/uploads/event/2017event_54cf6c9d801c23912bcb1fca4d9982503258be5e2.jpeg", "Amahoro Stadium")); }
                    else if(j == 2) { singleItem.add(new SingleItemModel("Holiday Cheer", "http://akokanya.com/uploads/event/2017event_1604fc66534f256c9cc2b6a0b88573b9ee5375a31.jpeg", "Convention Centre")); }
                    else if(j == 3) { singleItem.add(new SingleItemModel("Rwanda Connect Gala", "http://akokanya.com/uploads/event/2017event_fff8ec60454a3dae89c396cc84d27959c5923cad2.jpg", "Expo Ground")); }
                    else if(j == 4) { singleItem.add(new SingleItemModel("Rwanda Modesty Fashion", "http://akokanya.com/uploads/event/2017event_75e5d9ada693afe6357ccfcf12f7fc617f08f5f02.jpeg", "Serena Hotel")); }

                }
                dm.setAllItemsInSection(singleItem);
            }
            else if(i == 3)
            {
                dm.setHeaderTitle("Others");

                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                for (int j = 1; j <= 1; j++) {
                    singleItem.add(new SingleItemModel("Beer Fest", "12", "Golden Tulip"));
                }
                dm.setAllItemsInSection(singleItem);
            }


            allSampleData.add(dm);

        }
    }
}




