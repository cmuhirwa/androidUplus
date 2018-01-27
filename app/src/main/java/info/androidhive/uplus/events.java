package info.androidhive.uplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import info.androidhive.uplus.adapters.ReciclerViewDataAdapter;
import info.androidhive.uplus.models.SectionDataModel;
import info.androidhive.uplus.models.SingleItemModel;

import java.util.ArrayList;

public class events extends AppCompatActivity {
    private Toolbar toolbar;


    ArrayList<SectionDataModel> allSampleData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        allSampleData = new ArrayList<SectionDataModel>();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("G PlayStore");

        }


    }


}

