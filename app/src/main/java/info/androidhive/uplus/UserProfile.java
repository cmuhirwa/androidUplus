package info.androidhive.uplus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.format.Time;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.Chats;

public class UserProfile extends AppCompatActivity {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> Money=new ArrayList<String>();
    ArrayList<String> TransactionTime=new ArrayList<String>();
    ArrayList<String> Status=new ArrayList<String>();
    ArrayList<String> transId=new ArrayList<String>();
    private TextView textView1,textView2,userPhone,userMobile;
    final Context context = this;
    private Button btnSend;
    private ImageButton btnCloseProfileDialog;
    private ImageButton btnReq;
    ImageView imgAvatar;
    private GestureDetectorCompat gestureObject;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    EditText editView,txtAmountToSend,txtFromPhone;
    public Toolbar contactToolbar;
    ImageView imgContact;
    RelativeLayout relativeLayout;
    String name;
    String receiverPhone;
    ProgressDialog progress;
    TransactionDB transactionDB;
    public static Activity fa;
    ImageButton myChatButton;
    Button dialogButtonOk;
    //network state variable
    boolean networkState=false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_user);
        Bundle bundle = getIntent().getExtras();
        String uname = bundle.getString("user");
        String phones = bundle.getString("phone");
        final String states = bundle.getString("state");
        fa=this;
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        imgAvatar=(ImageView)findViewById(R.id.imgAvatar);
        textView1 = (TextView)findViewById(R.id.username) ;
        userMobile  =(TextView)findViewById(R.id.txtMobile);
        userPhone = (TextView)findViewById(R.id.userPhone);
        myChatButton = (ImageButton) findViewById(R.id.personChat);
        btnReq = (ImageButton) findViewById(R.id.reqbtn);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Request and pay is coming in the next update.",Toast.LENGTH_LONG).show();
            }
        });


        myChatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              /* Intent i = new Intent(UserProfile.this, Activity2.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
               */
              Toast.makeText(getApplicationContext(),"One on One chat feature is coming in the next update.",Toast.LENGTH_LONG).show();
            }
        });
        if(bundle.getString("user") != null){
            name = bundle.getString("user");
            collapsingToolbarLayout.setTitle(name);
            //textView1.setText(uname);
            userPhone.setText(phones);
            userMobile.setText(states);

        }
        receiverPhone=removeSpaceInPhone(userPhone.getText().toString());
        transactionDB=new TransactionDB(this);
        transactionDB.RecreateTable();
        //transactionDB.clearTable();
        getData();
        requestUnfinishedJobs();
        try
        {
            contactToolbar= (Toolbar) findViewById(R.id.contactToolbar);
            setSupportActionBar(contactToolbar);
            contactToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        relativeLayout=(RelativeLayout)findViewById(R.id.layoutImage);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        contactToolbar.setBackgroundColor(color1);
        TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(name), color1);
//        imgContact.setImageDrawable(drawable);
        imgAvatar.setImageDrawable(drawable);
        //imgAvatar.setBackg;mnklklround(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //relativeLayout.setBackground(drawable);
        }
        if(states.equals("MTN")){
            btnSend = (Button)findViewById(R.id.btnSend);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // custom dialog

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.send_money_dialog);
                    // Custom Android Allert Dialog Title
                    dialog.setTitle("Send Instant Money");
                    //show numbers
                    String number = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
                    final EditText edit  = (EditText)dialog.findViewById(R.id.txtFromPhone);
                    edit.setText(number);
                    edit.setEnabled(true);
                    txtAmountToSend=(EditText)dialog.findViewById(R.id.txtAmountToSend);
                    dialogButtonOk = (Button) dialog.findViewById(R.id.sendMoneyDialogWithdraw);
                    //listenNetwork(dialogButtonOk);
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            progress = new ProgressDialog(UserProfile.this);
                            progress.setTitle("Sending Money");
                            progress.setCancelable(false);
                            progress.setMessage("Please wait...");
                            progress.show();
                            if(isNetworkAvailable())
                           {
                             //add code here
                               Calendar c = Calendar.getInstance();
                               SimpleDateFormat sdf = new SimpleDateFormat("dd MMM  HH:mm");
                               String strDate = sdf.format(c.getTime());
                               String BankId="";
                               if(states.equals("TIGO"))
                               {
                                   BankId="2";
                               }
                               else if(states.equals("MTN"))
                               {
                                   BankId="1";
                               }
                               String amountTosend=txtAmountToSend.getText().toString();
                               if(txtAmountToSend.getText().toString()!="" && txtAmountToSend.getText().toString()!=null)
                               {
                                   if(amountTosend!="0" && returnlenght(txtAmountToSend.getText().toString())>0)
                                   {
                                       if(edit.getText().toString()!="" && edit.getText().length()==10)
                                       {

                                           String senderId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                                           SharedPreferences sharedPreferences=UserProfile.this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                            // String senderName = SharedPrefManager.getInstance(getApplicationContext()).getUserName();
                                           String senderName =sharedPreferences.getString("userName",null);
                                           Toast.makeText(UserProfile.this,senderName,Toast.LENGTH_LONG).show();
                                           String senderPhone = edit.getText().toString();
                                           String receiverName=collapsingToolbarLayout.getTitle().toString();
                                           receiverPhone=removeSpaceInPhone(userPhone.getText().toString());
                                           String senderBank=validateMyPhone(senderPhone);
                                           if(validateMyPhoneNow(senderPhone))
                                           {
                                               if(isNetworkAvailable())
                                               {
                                                   int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                                   if (SDK_INT > 8)
                                                   {
                                                       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                               .permitAll().build();
                                                       StrictMode.setThreadPolicy(policy);
                                                       //your codes here

                                                       if((new ConnectionDetector(UserProfile.this)).isConnectingToInternet()){
                                                           Intent intent=getIntent();
                                                           DirectTransFer directTransFer=new DirectTransFer(getApplicationContext(),progress,receiverPhone,amountTosend,dialog,intent);
                                                           directTransFer.execute("transfer",amountTosend,senderId,senderName,senderPhone,senderBank,receiverName,receiverPhone,BankId);
                                                          // Log.d("internet status","Internet Access");
                                                       }else{
                                                           progress.dismiss();
                                                           //Log.d("internet status","no Internet Access");
                                                           Toast.makeText(getApplicationContext(),"no Internet Access Please check if you have internet access",Toast.LENGTH_LONG).show();
                                                       }
                                                   }

                                               }
                                               else
                                               {
                                                   Toast.makeText(getApplicationContext(),"Please Enter Valid Phone, We currently support MTN only",Toast.LENGTH_LONG).show();
                                               }
                                           }
                                           else
                                           {

                                           }
                                       }
                                       else
                                       {
                                           Toast.makeText(getApplicationContext(),"Please Enter Valid Phone",Toast.LENGTH_LONG).show();
                                       }
                                   }
                                   else
                                   {
                                       Toast.makeText(getApplicationContext(),"Please Enter Valid Amount",Toast.LENGTH_LONG).show();
                                   }

                               }
                               else
                               {
                                   Toast.makeText(getApplicationContext(),"Please Enter Valid Amount",Toast.LENGTH_LONG).show();
                                   txtAmountToSend.setFocusable(true);
                                   txtAmountToSend.setText("");
                               }

                           }
                           else
                           {
                               Toast.makeText(getApplicationContext(),"No internet Connection please check your settings",Toast.LENGTH_LONG).show();
                           }

                        }
                    });
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogMoney; //style id
                    dialog.show();

                }

            });
        }else{
            btnSend = (Button)findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.send_money_fail_dialog);
                    // Custom Android Allert Dialog Title
                    dialog.setTitle("Send Instant Money");

                    Button dialogButtonOk = (Button) dialog.findViewById(R.id.btnFail);
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogMoney; //style id
                    dialog.show();

                }

            });
        }



    }


    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocity){
            if(event2.getX() > event1.getX()){




            }
            else
            if(event2.getX() < event1.getX()){
                //Intent i = new Intent(UserProfile.this, events.class);
                //startActivity(i);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

               Toast.makeText(getApplicationContext(),"One on One feature is coming in the next update.",Toast.LENGTH_LONG).show();

            }
            return  true;
        }
    }



    public void listenNetwork(final Button button)
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                            button.setEnabled(true);
                        }
                        else
                        {
                            button.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "No internet Connection Available", Toast.LENGTH_SHORT).show();
                        }
                        if(isNetworkAvailable())
                        {
                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8)
                            {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                //your codes here
                                if((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()){
                                    networkState=true;
                                    Log.d("internet status","Internet Access");
                                }else{
                                    networkState=false;
                                    Log.d("internet status","no Internet Access");
                                }
                            }


                        }
                    }
                });
            }
        },0,10000);
    }


    //FUNCTION TO CHECK IF MY NUMBER IS MTN OR TIGO



    public String validateMyPhone(String data)
    {
        String senderBank="";
        String newd=data.substring(2,3);
        if(Integer.parseInt(newd)==8)
        {
            senderBank="1";
        }
        else if(Integer.parseInt(newd)==2)
        {
            senderBank="2";
        }
        return senderBank;
    }
    public boolean validateMyPhoneNow(String data)
    {
        boolean state=false;
        String newd=data.substring(2,3);
        if(Integer.parseInt(newd)==8)
        {

            state=true;
        }
        else if(Integer.parseInt(newd)==2)
        {
            state=false;
        }
        return state;
    }
    public String textTobeConverted(String data)
    {
        String newData="D";
        int length=data.length();
        if(length>0)
        {
            newData=data.substring(0,1);

        }
        return newData;
    }

    //function ot remove all white space between phone numbers
    public String removeSpaceInPhone(String data)
    {
        String lineWithoutSpaces = data.replaceAll("\\s+","");
        return lineWithoutSpaces;
    }
    //function to chck submitted data
    public int returnlenght(String data)
    {
        return data.length();
    }
public void displayTime()
{
    for(int i=0;i<Status.size();i++)
    {
        if(Status.get(i)!=null && Status.get(i)!="")
        {
            if(Status.get(i).toLowerCase()=="Pending".toLowerCase())
            {
                Toast.makeText(getApplicationContext(),"Request may be resent "+transId.get(i),Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }
}
    //function get data from Data
    public void getData()
    {
        TransactionDB transactionDB=new TransactionDB(this);
        Cursor cursor=transactionDB.retrieveTransactions(receiverPhone);
        if(Money.size()>0)
        {
            Money.clear();
            TransactionTime.clear();
            Status.clear();
        }
        while(cursor.moveToNext())
        {
            Money.add(cursor.getString(cursor.getColumnIndex(TransactionContract.TRANSACTION_AMOUNT)));
            TransactionTime.add(cursor.getString(cursor.getColumnIndex(TransactionContract.TRANSACTION_TIME)));
            Status.add(cursor.getString(cursor.getColumnIndex(TransactionContract.STATUS)));
            transId.add(cursor.getString(cursor.getColumnIndex(TransactionContract.ID)));

        }
        cursor.close();
        //CREATE GUI CALL
        recyclerView=(RecyclerView)findViewById(R.id.recyclerTransaction);
        adapter=new TransactionAdapter(getApplicationContext(),Money,TransactionTime,Status);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    public String removeSpace(String data)
    {

        if(data!="" && data.length()>0){

        }
            return data.replaceAll("\\s+","");


    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //request unfinished background job
    public void requestUnfinishedJobs()
    {
        int i;
        for(i=0;i<Status.size();i++)
        {
            if(Status.get(i)!=null && Status.get(i)!="")
            {
                Toast.makeText(getApplicationContext(),"Transaction Status: "+Status.get(i),Toast.LENGTH_LONG).show();
                if(Status.get(i).equals("Pending"))
                {
                    if(isNetworkAvailable())
                    {
                                Intent intent=getIntent();
                                checkTransferStatus checkTransferStatus=new checkTransferStatus(this,intent);
                                checkTransferStatus.execute("resend",transId.get(i));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"NO internet COnnection",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
    public void requestTransactionBackground()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                                requestUnfinishedJobs();
                        }
                    }
                });
            }
        },0,10000);
    }
}