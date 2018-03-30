package info.androidhive.uplus;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

/**
 * Created by user on 02/02/2018.
 */

public class EventBokedQrPage extends AppCompatActivity {
    String EventName ,EventLocation, EventId, url, groupBalance, QrCode;
    TextView eventTitle, eventLocation, videoCount, qrName;
    EventAdapter adapter;
    ProgressDialog progress;
    ImageView qrimg, EventCover;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.eventbokedqrpage);
        eventTitle      =(TextView)findViewById(R.id.EventName1);
        eventLocation   =(TextView)findViewById(R.id.EventLocation1);
        videoCount      =(TextView)findViewById(R.id.videocount);
        qrName          =(TextView)findViewById(R.id.qrNameHolder);
        qrimg           = (ImageView) findViewById(R.id.eventQr);
        EventCover      = (ImageView) findViewById(R.id.eventCovers);
        Intent intent=getIntent();
        QrCode          = intent.getStringExtra("qrCode");
        EventName       = intent.getStringExtra("EventName");
        EventLocation   = intent.getStringExtra("EventLocation");
        EventId         = intent.getStringExtra("EventId");
        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("eventCover");

        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        EventCover.setImageBitmap(bmp);
        eventTitle.setText(EventName);
        eventLocation.setText("@"+EventLocation);
        qrName.setText(QrCode);
        Picasso.with(context).load("https://uplus.rw/api/qrcodes/1"+QrCode+".png").into(qrimg);
    }

}
