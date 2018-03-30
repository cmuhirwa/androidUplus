package info.androidhive.uplus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;

import java.util.ArrayList;

//import butterknife.BindView;
import info.androidhive.uplus.activity.RecyclerAdapter;
import info.androidhive.uplus.activity.SharedPrefManager;

import static info.androidhive.uplus.R.layout.adapter;

/**
 * Created by user on 02/02/2018.
 */

public class EventPage extends AppCompatActivity {
    String EventName ,EventLocation, EventId;
    String Name, Ids, Bankid,url, Amount, groupBalance;
    TextView eventTitle, eventLocation, videoCount;
    Button btnContribute;
    ImageView EventCover;
    //EvensharevideoAdapter adapter;
    EventAdapter adapter;
    private RecyclerView recyclerView;
    public static Activity eventPage;
    ProgressBar progressBar;
    ProgressDialog progress;
    Dialog dialog;
    SaveTickets saveTickets;
    Integer nGroups = 0;
    EditText editAmount,editAccount;
    ArrayList<String> ticketName=new ArrayList<String>();
    ArrayList<String>ticketPrice=new ArrayList<String>();
    ArrayList<String>ticketCode=new ArrayList<String>();
    public static Context contextOfApplication;
    //@BindView(R.id.tlUserProfileTabs)
    //TabLayout tlUserProfileTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.event_page);
        setContentView(R.layout.eventfinished);
        eventTitle      =(TextView)findViewById(R.id.EventName1);
        eventLocation   =(TextView)findViewById(R.id.EventLocation1);
        videoCount      =(TextView)findViewById(R.id.videocount);
        EventCover      = (ImageView) findViewById(R.id.eventCovers1);
        Intent intent=getIntent();
        EventName       = intent.getStringExtra("EventName");
        EventLocation   = intent.getStringExtra("EventLocation");
        EventId         = intent.getStringExtra("EventId");
        Bundle extras   = getIntent().getExtras();
        byte[] b1 = extras.getByteArray("eventCover");

        Bitmap bmp = BitmapFactory.decodeByteArray(b1, 0, b1.length);
        EventCover.setImageBitmap(bmp);

        eventTitle.setText(EventName);
        eventLocation.setText("@"+EventLocation);
        eventPage = this;

        saveTickets=new SaveTickets(getApplicationContext());
        if(ticketName.size()>0)
        {
            ticketName.clear();
            ticketPrice.clear();
            ticketCode.clear();
        }
        String buffer="";

        Cursor cursor=saveTickets.getAllData(EventId);
        while (cursor.moveToNext())
        {
            nGroups++;
            ticketName.add(cursor.getString(cursor.getColumnIndex(TicketContract.TICKETS_NAME)));
            ticketPrice.add(cursor.getString(cursor.getColumnIndex(TicketContract.PRICE)));
            ticketCode.add(cursor.getString(cursor.getColumnIndex(TicketContract.TICKET_CODE)));
            buffer+=(cursor.getString(cursor.getColumnIndex(TicketContract.TICKET_CODE)))+"\n";
        }
        cursor.close();


        //String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

       // Integer more = data.length;
        //videoCount.setText(more);
        contextOfApplication = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.rvUserProfile);
        adapter      = new EventAdapter(ticketName, ticketPrice,getApplicationContext(),recyclerView, ticketCode, EventId, EventName, EventLocation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

        //https://0dd7464a.ngrok.io/USSD-SAMPLE/ussd.php
        /*
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new EvensharevideoAdapter(this, data);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

       // setupTabs();

        /*
            TabHost tabEvent = (TabHost) findViewById(R.id.tabChurch);
            tabEvent.setup();

            TabHost.TabSpec spec1 = tabEvent.newTabSpec(" TICKETS ");
            spec1.setIndicator(" TICKETS ");
            spec1.setContent(R.id.Tickets);
            tabEvent.addTab(spec1);

            TabHost.TabSpec spec2 = tabEvent.newTabSpec(" MORE INFO ");
            spec2.setIndicator(" MORE INFO ");
            spec2.setContent(R.id.Mytickets);
            tabEvent.addTab(spec2);


        */

        LinearLayout app_layer = (LinearLayout) findViewById (R.id.buyTick);
        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "hello"+nGroups, Toast.LENGTH_LONG).show();
               // contributeNow();
            }
        });
    }

    private int getSpanCount(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int anchuraPantalla = metrics.widthPixels;

        float anchuraMinimaElemento = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                150,
                metrics
        );
        return (int) (anchuraPantalla / anchuraMinimaElemento);
    }

    private void setupTabs() {
       // tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.events));
       // tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.groupicon));
    }

    public void contributeNow()
    {

        try
        {

            dialog=new Dialog(this);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.bookticket);
            dialog.show();


            editAmount=(EditText)dialog.findViewById(R.id.editAmount);
            final Spinner spnAccount=(Spinner)dialog.findViewById(R.id.spnAccount);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
            spnAccount.setAdapter(adapter);
            spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int index=spnAccount.getSelectedItemPosition();

                    if(index==0)
                    {
                        Bankid="1";
                    }
                    else
                    {
                        Bankid="2";
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnContribute=(Button)dialog.findViewById(R.id.btnContribute);
            editAccount=(EditText)dialog.findViewById(R.id.editAccount);
            String number = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
            editAccount.setText(number);
            editAmount.setText("5,000 Rwf");
            editAmount.setFocusable(false);
            btnContribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amount=editAmount.getText().toString();
                    String account=editAccount.getText().toString();
                    //validate data
                    if(amount.length()>0 && Integer.parseInt(amount)>=100)
                    {
                        if(account.length()==10)
                        {
                            //call contribute Worker
                            String method="contribute";
                            String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                            String finaldata="memberId:"+userId+"\n GroupId:"+Ids+"\n amount:"+amount+"\n fromPhone:"+account+"\n bankId:"+Bankid;
                            //Toast.makeText(getApplicationContext(),finaldata,Toast.LENGTH_LONG).show();
                            progress = new ProgressDialog(EventPage.this);
                            progress.setTitle("Booking");
                           // progress.setCancelable(false);
                            progress.setMessage("Please wait...");
                            progress.setCancelable(false);
                            dialog.dismiss();
                            progress.show();
//                            SharedPreferences sharedPreferences=EventPage.this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
//                            String userName=sharedPreferences.getString("userName",null);
//                            ContributeWorker contributeWorker=new ContributeWorker(getApplicationContext(),progress,Ids,Name,"groupImage",Amount, groupBalance);
//
//                            contributeWorker.execute(method,userId,Ids,amount,account,Bankid);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please enter a valid phone",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter a valid amount",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
