package info.androidhive.uplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by user on 02/02/2018.
 */

public class EventPage extends AppCompatActivity {
    String EventName ,EventLocation;
    TextView eventTitle, eventLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_page);
        eventTitle      =(TextView)findViewById(R.id.eventName);
        eventLocation   =(TextView)findViewById(R.id.eventLocation);
        Intent intent=getIntent();
        EventName       = intent.getStringExtra("EventName");
        EventLocation   = intent.getStringExtra("EventLocation");
        eventTitle.setText("Event Name: "+EventName);
        eventLocation.setText("Event Locarion: "+EventLocation);

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
    }
}
