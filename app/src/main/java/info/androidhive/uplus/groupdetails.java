package info.androidhive.uplus;


import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.GetMembers;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.ModifyGroup;
import info.androidhive.uplus.activity.RecyclerAdapter;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.OneFragment;

import static android.R.id.progress;
import static info.androidhive.uplus.R.id.action_Modify;
import static info.androidhive.uplus.R.id.imageView;
import static info.androidhive.uplus.R.id.txtCurrent;


public class groupdetails extends AppCompatActivity
{
    SaveMembers saveMembers;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Intent shareIntent;
    String Name, holdGroupId, Ids, Bankid,url, Amount, CurrentRealMoney, groupBalance;
    Double currentMoney;
    ImageButton btnShare;
    Button btnContribute, appBtn, rejBtn, btnWithDraw;
    ImageButton btnGroupChat;
    ProgressBar progressBar;
    ProgressDialog progress;
    Dialog dialog;
    Spinner spnWithDrawAccount;
    EditText txtWithDrawAmount,txtWithDrawPhone;
    TextView txtError,textView3,txtCurrent,txtPercentage,txtGroupAmount,outAmount;
    ImageView profile_id;
    EditText editAmount,editAccount;
    ArrayList<String> memberAmount=new ArrayList<String>();
    ArrayList<String>memberName=new ArrayList<String>();
    ArrayList<String>memberImage=new ArrayList<String>();
    ArrayList<String>memberType=new ArrayList<String>();
    ArrayList<String>memberId=new ArrayList<String>();

    String requestId;
    Uri Shortlink;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    public static Activity ffaa;
    String ContributeRequest="";
    String WithdrawRequest="";
    String groupImage="";
    ContributeHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupinfo);
        //get extra data passed
        Intent intent=getIntent();
        recyclerView    =(RecyclerView)findViewById(R.id.recyclerSam);
        textView3       =(TextView)findViewById(R.id.textView3);
        txtCurrent      =(TextView)findViewById(R.id.txtCurrent);
        txtPercentage   =(TextView)findViewById(R.id.txtPercentage);
        outAmount       =(TextView)findViewById(R.id.outAmount);
        progressBar     = (ProgressBar)findViewById(R.id.progressBar);
        appBtn          = (Button) findViewById(R.id.approveReq);
        rejBtn          = (Button) findViewById(R.id.rejReq);
        ffaa=this;
        Ids=intent.getStringExtra("Id");
        Name=intent.getStringExtra("Name");
        Amount=intent.getStringExtra("Amount");
        groupImage=intent.getStringExtra("Image");
        groupBalance=intent.getStringExtra("groupBalance");
        currentMoney = Double.parseDouble(groupBalance);
        helper=new ContributeHelper(this);
        helper.recreateTable();
        getTransactionData();
        final String DEEP_LINK_URL = "https://uplus.rw/f/i"+Ids;

        appBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {
                    approveWithdraw();
                }
                else
                {
                    Toast.makeText(ffaa, "Please make sure you have Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {
                    rejectWitdraw();
                }
                else
                {
                    Toast.makeText(ffaa, "Please make sure you have Network", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // IF FINISHED CONTRIBUTING UPDATE THE LOCAL DB
        ContributeRequest=intent.getStringExtra("request");
        if(ContributeRequest!="" && ContributeRequest!=null)
        {
            if(ContributeRequest=="1")
            {
                final int[] num = {0};
                final Timer timers  = new Timer();
                timers.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +" sam",Toast.LENGTH_SHORT );
                               // toast.show();
                                if(isNetworkAvailable())
                                {
                                    //Toast.makeText(getApplicationContext(),Ids+" groupId",Toast.LENGTH_LONG).show();
                                    GetMembers getMembers=new GetMembers(getApplicationContext());
                                    getMembers.execute("members",Ids);
                                }
                            }
                        });
                    }
                },0,5000);
            }
            // Toast.makeText(getApplicationContext(),"Resend request",Toast.LENGTH_LONG).show();
        }


        if(Ids!=null)
        {
            holdGroupId=Ids;
                // showLoader();
            //getRequests();
            //requestWithDrawList();
            if(isNetworkAvailable()==false)
            {
                //Toast.makeText(getApplicationContext(),"No Network available",Toast.LENGTH_LONG).show();
            }
            else
            {
                //loopData();
            }
            url="http://67.205.139.137/groupimg/"+Ids+".jpg";
            saveMembers=new SaveMembers(getApplicationContext());
            saveMembers.recreateTable();

            textView3.setText(currencyConverter(Amount));
            //outAmount.setText(currencyConverter(Amount));
            loopLocal();
            //viewData1();
            profile_id=(ImageView)findViewById(R.id.profile_id);
            btnGroupChat=(ImageButton) findViewById(R.id.btnGroupChat);
            btnGroupChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //prepare some intent input extras
                    String userName = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
                    Intent intent1=new Intent(getApplicationContext(),Chat_Room.class);
                    intent1.putExtra("user_name",userName);
                    intent1.putExtra("room_name",Name);
                    intent1.putExtra("room_id",Ids);
                    startActivity(intent1);

                }
            });
            //Toast.makeText(this,Ids,Toast.LENGTH_LONG).show();
            Picasso.with(getApplicationContext())
                    .load(groupImage)
                    .into(profile_id, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(groupdetails.this).load(groupImage).into(profile_id);
                        }

                        @Override
                        public void onError() {
                            //image of group not valid
                            ColorGenerator generator = ColorGenerator.MATERIAL;
                            int color1 = generator.getRandomColor();

                            TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(Name), color1);
                            profile_id.setImageDrawable(drawable);
                        }
                    });
            /// viewData1();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"there was an error",Toast.LENGTH_LONG).show();
        }

        try
        {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(Name);

            dynamicToolbarColor();

            toolbarTextAppernce();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        final Uri deepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL));

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(Uri.parse("https://xms9d.app.goo.gl/?apn=info.androidhive.uplus&link="+DEEP_LINK_URL))
            .buildShortDynamicLink()
            .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                @Override
                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                    if (task.isSuccessful()) {
                        // Short link created
                        Shortlink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();

                        Log.i("CHEOK", "shortLink = " + Shortlink);

                    } else {
                        // Error
                        // ...
                    }
                }
            });

        if(isNetworkAvailable())
        {
            shortLinkTask.addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                @Override
                public void onSuccess(ShortDynamicLink authResult) {
                btnShare = (ImageButton) findViewById(R.id.btnShare);
                btnShare.setOnClickListener(new View.OnClickListener() {
                    String shareBody = "Checkout our contribution group on uplus called (" + Name + "): " + Shortlink;

                    @Override
                    public void onClick(View view) {
                        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                    }
                });
                }
            });
        }
        else
        {
            btnShare = (ImageButton) findViewById(R.id.btnShare);
            btnShare.setOnClickListener(new View.OnClickListener() {
                String shareBody = "Checkout our contribution group on uplus called (" + Name + "): https://xms9d.app.goo.gl/?apn=info.androidhive.uplus&link="+DEEP_LINK_URL;

                @Override
                public void onClick(View view) {
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            });
        }

        btnContribute=(Button)findViewById(R.id.btnContribute);
        contributeActivity(btnContribute);

    }

    public Uri buildDeepLink(@NonNull Uri deepLink) {
        String domain = getString(R.string.app_code) + ".app.goo.gl";
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDynamicLinkDomain("xms9d.app.goo.gl")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setLink(deepLink);

        // Build the dynamic link
        final DynamicLink link = builder.buildDynamicLink();

        // Return the dynamic link as a URI
        return link.getUri();
    }

    private class ApproveVote extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(groupdetails.this, "Done, thanks", Toast.LENGTH_LONG).show();
            progress.cancel();
        }

    }

    private class RejectVote extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(groupdetails.this, "Done, thanks", Toast.LENGTH_LONG).show();
            progress.cancel();
        }

    }

    public String currencyConverter(String data)
    {
        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,### RWF");
        return(decimalFormat.format(value));
    }

    public void loadImage()
    {
        URL urls = null;
        try {
            urls = new URL(url);
            Bitmap bmp = BitmapFactory.decodeStream(urls.openConnection().getInputStream());
            profile_id.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void contributeActivity(Button btn)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNotification();
                contributeNow();
            }
        });
    }

    private void dynamicToolbarColor()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wed);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

            }
        });
    }

    private void toolbarTextAppernce()
    {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int clicked=item.getItemId();
        if(clicked==R.id.action_settings){
            try
            {
                Intent intent=new Intent(getApplicationContext(),InviteMember.class);
                intent.putExtra("addedId",holdGroupId);
                intent.putExtra("Name",Name);
                intent.putExtra("Amount",Amount);
                intent.putExtra("Image",groupImage);
                intent.putExtra("groupBalance",groupBalance);
                finish();
                startActivity(intent);
            }
            catch (Exception ex)
            {

            }
        }
        /*else if(clicked==R.id.action_refresh)
        {
            Intent intent=getIntent();
            finish();
            startActivity(intent);
        }*/
        else if(clicked==R.id.action_WithDraw)
        {
            WithDraw();
        }else if(clicked == R.id.action_Modify){
            try{
                Intent intent  = new Intent(getApplicationContext(), ModifyGroup.class);
                intent.putExtra("ModGroupName",Name);
                intent.putExtra("ModGroupId",holdGroupId);
                intent.putExtra("ModGroupAmount",Amount);
                startActivity(intent);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }else if(clicked == R.id.action_exit){
            if(isNetworkAvailable())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit Group");
                builder.setMessage("Are you sure you want to exit "+Name+" Group?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                        progress = new ProgressDialog(groupdetails.this);
                        progress.setTitle("Waiting Status");
                        progress.setCancelable(false);
                        progress.setMessage("Please wait while we are Removing You...");
                        progress.show();


                        ExitGroup exitGroup=new ExitGroup(getApplicationContext(),progress);
                        exitGroup.execute("exit",Ids,userId);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Thanks for being with us",Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                AlertDialog exit = builder.create();
                exit.show();
            }
            else
            {
                Toast.makeText(ffaa, "Please make sure you have Network", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //function to create custom dialog
    public void WithDraw()
    {
        dialog=new Dialog(this);
        dialog.setTitle("WithDraw Money");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.withdraw_layout);
        dialog.show();
        txtError=(TextView)dialog.findViewById(R.id.txtError);

        spnWithDrawAccount= (Spinner) dialog.findViewById(R.id.spnWithdraw);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
        spnWithDrawAccount.setAdapter(adapter);
        spnWithDrawAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=spnWithDrawAccount.getSelectedItemPosition();
                if(index==0)
                {
                    Bankid="1";
                }
                else if(index==1)
                {
                    Bankid="2";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtWithDrawAmount=(EditText)dialog.findViewById(R.id.txtWithDrawAmount);
        txtWithDrawPhone=(EditText)dialog.findViewById(R.id.txtWithDrawPhone);
        txtGroupAmount=(TextView)dialog.findViewById(R.id.txtGroupAmount);
        txtGroupAmount.setText("Group Amount  " + txtCurrent.getText().toString());
        btnWithDraw=(Button)dialog.findViewById(R.id.withdrawBtn);
        btnWithDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate data
                String error="";
                String amount=txtWithDrawAmount.getText().toString();
                String phone=txtWithDrawPhone.getText().toString();
                if((Bankid!="" && Bankid!=null))
                {
                    if(amount.length()>=3)
                    {
                        Integer Gamount = Integer.parseInt(Amount);
                        Integer Wamount = Integer.parseInt(amount);
                        if(Gamount > Wamount)
                        {
                            //Toast.makeText(groupdetails.this,Gamount+">"+Wamount,Toast.LENGTH_LONG).show();
                            if (phone.length() >= 10) {
                                if (Integer.parseInt(amount) <= Integer.parseInt(Amount)) {
                                    String finalData = "GroupId:" + Ids + "\n memberId:" + 1 + "\n BankId:" + Bankid + "\n Amount:" + amount + "\n Phone:" + phone;
                                    String method = "withdrawrequest";


                                    //Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();
                                    progress = new ProgressDialog(groupdetails.this);
                                    progress.setTitle("Withdrawing");
                                    progress.setCancelable(false);
                                    progress.setMessage("Please wait...");
                                    dialog.dismiss();
                                    progress.show();
                                    SharedPreferences sharedPreferences = groupdetails.this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                    String memberId = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                                    WithDrawWorker withDrawWorker = new WithDrawWorker(getApplicationContext(), progress);

                                    withDrawWorker.execute(method, Ids, amount, memberId, phone, Bankid, Name,Amount,groupImage, groupBalance);

                                } else {
                                    error = "Please select Valid Amount";
                                }
                            } else {
                                error = "Please enter valid Phone";
                            }
                        }
                        else
                        {
                            error = "Please you can only request what the group has: "+Amount;
                        }
                    }
                    else
                    {
                        error="Please enter valid Amount";
                    }
                }
                else
                {
                    error="Please choose with an account";
                }
                txtError.setText(error);
            }
        });
    }

    public void contributeNow()
    {
        try
        {
            dialog=new Dialog(this);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_contribute);
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
                            progress = new ProgressDialog(groupdetails.this);
                            progress.setTitle("Contributing");
                            progress.setCancelable(false);
                            progress.setMessage("Please wait...");
                            progress.setCancelable(false);
                            dialog.dismiss();
                            progress.show();
                            SharedPreferences sharedPreferences=groupdetails.this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                            String userName=sharedPreferences.getString("userName",null);
                            ContributeWorker contributeWorker=new ContributeWorker(getApplicationContext(),progress,Ids,Name,groupImage,Amount, groupBalance);

                            contributeWorker.execute(method,userId,Ids,amount,account,Bankid);
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

    //method to return the firstcolor of every string
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

    // Going to get members list data from url
    public void loopData()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +" sam",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                            //Toast.makeText(getApplicationContext(),Ids+" groupId",Toast.LENGTH_LONG).show();
                            GetMembers getMembers=new GetMembers(getApplicationContext());
                            getMembers.execute("members",Ids);
                        }
                    }
                });
            }
        },0,10000);
    }

    // Getting data from the db to the page
    public void loopLocal()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0],Toast.LENGTH_SHORT );
                        //toast.show();
                        retrieveMember();
                    }
                });
            }
        },0,10000);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void viewData1()
    {
        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
        try
        {
            SaveMembers saveMembers=new SaveMembers(getApplicationContext());
            Cursor cursor=saveMembers.getAllData(Ids);
            if(cursor.getCount()==0)
            {
                Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
                cursor.close();
            }
            StringBuffer buffer=new StringBuffer();

            while(cursor.moveToNext())
            {
                buffer.append("memberName:"+cursor.getString(2)+"\n");
                buffer.append("memberAmount:"+cursor.getString(3)+"\n\n");
                //formatted numers
            }
            cursor.close();
           // Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void showLoader()
    {
        Intent intent=getIntent();

        String Load=intent.getStringExtra("Load");
        final ProgressDialog progress;
        progress = new ProgressDialog(groupdetails.this);
        progress.setTitle("Loading Contents now");
        progress.setCancelable(false);
        progress.setMessage("Please wait while we are Downloading Data...");
        progress.setCancelable(false);
        progress.show();
//                BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
//                backgroundTask.execute("add");



    }
    //FUNCTION TO LOAD DATA FROM DATABASE

    public void retrieveMember()
    {
        //Double currentMoney=0.0;
        Cursor cursor=saveMembers.getAllData(Ids);
        if(memberName.size()>0)
        {
            memberName.clear();
            memberImage.clear();
            memberType.clear();
            memberAmount.clear();
        }
        try
        {

            // read data from the cursor in here

            while(cursor.moveToNext())
            {
                memberName.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_NAME)));
                memberImage.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_IMAGE)));
                memberAmount.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_CONTRIBUTION)));
                memberType.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_TYPE)));
                memberId.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_ID)));
                //currentMoney=currentMoney+Double.parseDouble(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_CONTRIBUTION)));

                String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                String memberTestId = cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_ID));
                String memberTestType = cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_TYPE));

               if(userId.equals(memberTestId) && memberTestType.equals("Group treasurer")){
                   //Toast.makeText(getApplicationContext(),"Admins",Toast.LENGTH_LONG).show();
                   getRequests();
               }else if(userId.equals(memberTestId))
               {
                   //Toast.makeText(getApplicationContext(),"Members",Toast.LENGTH_LONG).show();
                   getRequestsForMembers();
               }
            }
            // if($checkType == Group Tresuluer)

        } finally {
            cursor.close();
        }
        //Create recyclerview adapter
        recyclerView=(RecyclerView)findViewById(R.id.recyclerSam);
        adapter     =new RecyclerAdapter(memberName,memberImage,memberAmount,memberType,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        txtCurrent.setText(currencyConverter(String.valueOf(currentMoney)));
        CurrentRealMoney = String.valueOf(currentMoney);
        int progressValue= (int) ((currentMoney*100)/Integer.parseInt(Amount));
        //Toast.makeText(getApplicationContext(),String.valueOf(progressValue),Toast.LENGTH_LONG).show();
        progressBar.setProgress(progressValue);
        txtPercentage.setText(String.valueOf(progressValue)+"%");
//        progress.dismiss();
    }

    //PENDING WITHDRAW
    public void getRequests()
    {
        TextView txt1,txt2,txt3;
        //txt1=(TextView)findViewById(R.id.txtRequestId);
        CardView cardView=(CardView)findViewById(R.id.card_request);
        txt2=(TextView)findViewById(R.id.txtRequestAmount);
        txt3=(TextView)findViewById(R.id.txtMemberName);

        RequestHelper helper=new RequestHelper(groupdetails.this);

        //helper.recreateTable();

        Cursor cursor=helper.getAllData(Ids);
        if(cursor.getCount()>0)
        {
            if(cardView!=null)
            {
                cardView.setVisibility(View.VISIBLE);
                appBtn.setVisibility(View.VISIBLE);
                rejBtn.setVisibility(Button.VISIBLE);
                while(cursor.moveToNext())
                {
                    //txt1.setText(cursor.getString(cursor.getColumnIndex(RequestContractor.REQUEST_ID)));
                    txt2.setText(currencyConverter(cursor.getString(cursor.getColumnIndex(RequestContractor.AMOUNT))));
                    txt3.setText(cursor.getString(cursor.getColumnIndex(RequestContractor.MEMBER_NAME)));
                    requestId = cursor.getString(cursor.getColumnIndex(RequestContractor.REQUEST_ID));
                 //   Toast.makeText(groupdetails.this,"From Local for: ("+requestId+")",Toast.LENGTH_SHORT).show();

                    //showNotification(cursor.getString(cursor.getColumnIndex(RequestContractor.MEMBER_NAME)), currencyConverter(cursor.getString(cursor.getColumnIndex(RequestContractor.AMOUNT))));
                }
                cursor.close();
                helper.close();
            }
        }
        else
        {
           // Toast.makeText(groupdetails.this,"Disaperd for: ("+requestId+")",Toast.LENGTH_SHORT).show();
        //   cardView.setVisibility(View.INVISIBLE);
//            ((ViewGroup) cardView.getParent()).removeView(cardView);
        }
    }

//  //PENDING WITHDRAW
    public void getRequestsForMembers()
    {
        TextView txt1,txt2,txt3;
        LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.approverBtns);
        //txt1=(TextView)findViewById(R.id.txtRequestId);
        CardView cardView=(CardView)findViewById(R.id.card_request);
        txt2=(TextView)findViewById(R.id.txtRequestAmount);
        txt3=(TextView)findViewById(R.id.txtMemberName);
        RequestHelper helper=new RequestHelper(groupdetails.this);

        //helper.recreateTable();

        Cursor cursor=helper.getAllData(Ids);
        if(cursor.getCount()>0)
        {
            if(cardView!=null)
            {
                cardView.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(LinearLayout.GONE);
                while(cursor.moveToNext())
                {
                    //txt1.setText(cursor.getString(cursor.getColumnIndex(RequestContractor.REQUEST_ID)));

                    txt2.setText(currencyConverter(cursor.getString(cursor.getColumnIndex(RequestContractor.AMOUNT))));
                    txt3.setText(cursor.getString(cursor.getColumnIndex(RequestContractor.MEMBER_NAME)));
                    requestId = cursor.getString(cursor.getColumnIndex(RequestContractor.REQUEST_ID));
                 //   Toast.makeText(groupdetails.this,"From Local for: ("+requestId+")",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                helper.close();
            }
        }
        else
        {
        }
    }

    //CHECK IF ID NOT EXIST IN ADapter
    public boolean checkMemberId(String data)
    {
        boolean state=false;
        for(int i=0;i<memberId.size();i++)
        {
            if(data.toLowerCase()==memberId.get(i).toLowerCase())
            {
                state=true;
                break;
            }
        }
        return state;
    }

    public void sumUpMoney()
    {
        Double currentMoney=0.0;
        for(int i=0;i<memberAmount.size();i++)
        {
            currentMoney+=Double.parseDouble(memberAmount.get(i));
          //  Toast.makeText(getApplicationContext(),memberAmount.get(i),Toast.LENGTH_LONG).show();
        }
        txtCurrent.setText(currencyConverter(String.valueOf(currentMoney)));
    }

    //get transaction data
    public void getTransactionData()
    {
        ContributeHelper helper=new ContributeHelper(groupdetails.this);
        helper.recreateTable();
        Cursor cursor=helper.getAllData();
        String transactionId="";
        String status="";
        while(cursor.moveToNext()){
            transactionId=cursor.getString(cursor.getColumnIndex(ContributeContractor.TRANSACTION_ID));
            status=cursor.getString(cursor.getColumnIndex(ContributeContractor.STATUS));
        }
        cursor.close();
        helper.close();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(status);
        //builder.show();
        if(status.equals("Pending") && status!="")
        {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here
                //Toast.makeText(groupdetails.this,"sending again",Toast.LENGTH_LONG).show();
                if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                    Intent i=getIntent();
                    CheckStatus checkStatus=new CheckStatus(groupdetails.this,i,this);
                    checkStatus.execute("resend",transactionId);
                }
            }
        }

    }

    public void rejectWitdraw()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(groupdetails.this);
        builder.setTitle("Reject Request");
        builder.setMessage("Are you sure you want to reject this request");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                progress = new ProgressDialog(groupdetails.this);
                progress.setTitle("Rejecting request");
                progress.setCancelable(false);
                progress.setMessage("Please wait while we are rejecting the request...");
                progress.show();


                WithdrawConfirm withconf = new WithdrawConfirm(getApplicationContext(), progress);
                withconf.execute("confirm", requestId ,Ids,userId,"NO");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"We wont reject it.",Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        AlertDialog exit = builder.create();
        exit.show();
    }

    public void approveWithdraw()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(groupdetails.this);
        builder.setTitle("Approve request");
        builder.setMessage("Are you sure you want to approve this request");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
                progress = new ProgressDialog(groupdetails.this);
                progress.setTitle("Approving request");
                progress.setCancelable(false);
                progress.setMessage("Please wait while we are rejecting the request...");
                progress.show();

                WithdrawConfirm withconf = new WithdrawConfirm(getApplicationContext(), progress);
                withconf.execute("confirm", requestId ,Ids,userId,"YES");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"We wont approve it.",Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        AlertDialog exit = builder.create();
        exit.show();
    }

    public void showNotification(String withName, String withAmount)
    {
        int uniqueID = 123;
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this);

        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.icon);
        notification.setTicker("Withdraw Request");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Withdraw Request");
        notification.setContentText("Member "+withName+" Is requesting to withdraw "+withAmount+" Rwf");
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and isuing it
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

    }


}