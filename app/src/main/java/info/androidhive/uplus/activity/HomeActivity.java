package info.androidhive.uplus.activity;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.ChatHelp;
import info.androidhive.uplus.DatabaseHelper;
import info.androidhive.uplus.EventWorker;
import info.androidhive.uplus.Invitation;
import info.androidhive.uplus.PublicInviteWorker;
import info.androidhive.uplus.Settings;
import info.androidhive.uplus.BackgroundTask;
import info.androidhive.uplus.Contact;
import info.androidhive.uplus.ContactsAdapter;
import info.androidhive.uplus.DbHelper;
import info.androidhive.uplus.FirebaseMessagingService;
import info.androidhive.uplus.IntroActivity;
import info.androidhive.uplus.Login;
import info.androidhive.uplus.MyAdapter;
import info.androidhive.uplus.Profile;
import info.androidhive.uplus.R;
import info.androidhive.uplus.SaveGroupLocal;
import info.androidhive.uplus.SaveMembers;
import info.androidhive.uplus.Settings;
import info.androidhive.uplus.SettingsActivity;
import info.androidhive.uplus.Testingapp;
import info.androidhive.uplus.Transactions;
import info.androidhive.uplus.fragments.OneFragment;
//import info.androidhive.uplus.fragments.FiveFragment;
import info.androidhive.uplus.fragments.ThreeFragment;
import info.androidhive.uplus.fragments.TwoFragment;
import info.androidhive.uplus.groupdetails;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class HomeActivity extends AppCompatActivity {

    String Token;
    DatabaseHelper myDb;
    boolean thread_running = true;
    //RelativeLayout layoutHomeGuide;
    RequestQueue requestQueue;
    String insertUrl = "http://67.205.139.137/api/index.php";
    ContactsAdapter adapter;
    ImageView myImageButton;
    EditText helptext;
    public int count;
    static int counter=0;
    private String temp_key;
    OneFragment oneFragment;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static DatabaseReference root ;
    public static Activity homepage ;
    ProgressDialog progress;
    private ListView list;
    private String myString = "0";
    private int[] tabIcons = {
            R.drawable.groupicon,
            R.drawable.ic_tab_contacts,
            R.drawable.events
    };
    ArrayList<Contact>arrayList=new ArrayList<>();
    static boolean calledAlready = false;
    MaterialSearchView searchView;
    private FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        homepage = this;
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (BuildConfig.DEBUG)
        {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        root = mFirebaseDatabase.getReference().child("help");

        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();

        myDb = new DatabaseHelper(this);
        Intent intent=getIntent();
        if(intent.getStringExtra("invited")=="0")
        {
            Toast.makeText(getApplicationContext(), "Invitation sent.", Toast.LENGTH_LONG).show();
        }

        FirebaseInstanceId.getInstance().getToken();

        recyclerView=(RecyclerView)findViewById(R.id.rv_recycler_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        //WRITE FUNCTION TO VALIDATE IF A USER IS LOGGED IN HERE

        // [START get_deep_link]
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String webOfLink = deepLink.toString();
                            //Log.e("DeepLink", webOfLink);
                            //Keep it in the shared preference
                            if (deepLink != null) {
                                String publicGid = deepLink.toString().replaceAll("[^\\d.]", "");
                                publicGid = publicGid.replaceAll("[.]", "");
                                publicGid = publicGid.replace('\'', '.');
                                Log.e("DeepLinkId", publicGid);
                                Log.e("DeepLinkId21", "is "+webOfLink);
                                if(publicGid != null || publicGid != ""){
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                    sharedPreferences.edit().putString("deepLink", "YES").apply();
                                    sharedPreferences.edit().putString("sharedLink", publicGid).apply();
                                }
                                else {
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                    sharedPreferences.edit().putString("deepLink", "").apply();
                                    sharedPreferences.edit().putString("sharedLink", "").apply();
                                    //Toast.makeText(HomeActivity.this,"Changed to: Not Stored",Toast.LENGTH_LONG).show();

                                }
                            }
                            else {
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("deepLink", "").apply();
                                sharedPreferences.edit().putString("sharedLink", "").apply();
                                //Toast.makeText(HomeActivity.this,"Changed to: Not Stored",Toast.LENGTH_LONG).show();

                            }
                            if(!SharedPrefManager.getInstance(getApplication()).isLoggedIn()){
                                Intent i =  new Intent(getApplicationContext(),Login.class);
                                finish();
                                startActivity(i);
                            }
                            else
                            {
                                Intent intentLoad=getIntent();

                                String Load=intentLoad.getStringExtra("Load");

                                if(Load!="" && Load!=null)
                                {
                                   // Toast.makeText(HomeActivity.this,"First Call",Toast.LENGTH_LONG).show();
                                }
                                else
                                {

                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                                    final String deepSLink = sharedPreferences.getString("deepLink", null);
                                    //Toast.makeText(HomeActivity.this,"And it is on: "+deepSLink,Toast.LENGTH_LONG).show();

                                    if (deepSLink != "") {
                                        final String publicGid = sharedPreferences.getString("sharedLink", null);
                                        //Toast.makeText(HomeActivity.this,"Which gave: "+publicGid,Toast.LENGTH_LONG).show();
                                        Log.e("DeepLink4", "is "+publicGid);

                                        if(publicGid != null &&  publicGid != "" ) {
                                            //Get it from the shared pref
                                            Log.e("DeepLink7", "is "+publicGid);

                                            String method = "publicInvite";
                                            progress = new ProgressDialog(HomeActivity.this);
                                            progress.setCancelable(false);
                                            progress.setMessage("Getting Invitation");
                                            progress.show();
                                            sharedPreferences.edit().putString("deepLink", "").apply();
                                            sharedPreferences.edit().putString("sharedLink", "").apply();
                                            Intent intent1 = getIntent();
                                            intent1.putExtra("Load", "2");

                                            //sharedPreferences.edit().putString("deepLink", "NO").apply();
                                            PublicInviteWorker publicInviteWorker = new PublicInviteWorker(getApplicationContext(), progress, publicGid);
                                            publicInviteWorker.execute(method, publicGid);

                                        }
                                        else{
                                            sharedPreferences.edit().putString("deepLink", "").apply();
                                            sharedPreferences.edit().putString("sharedLink", "").apply();

                                            showLoader();
                                        }

                                    }else{

                                        //Toast.makeText(HomeActivity.this,"But gave: "+deepSLink,Toast.LENGTH_LONG).show();

                                    }

                                }

                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(HomeActivity.this,"getDynamicLink: failed",Toast.LENGTH_LONG).show();
                    }
                });
        // [END get_deep_link]

        if(!SharedPrefManager.getInstance(getApplication()).isLoggedIn()){
            Intent i =  new Intent(getApplicationContext(),Login.class);
            finish();
            startActivity(i);
        }
        showLoader();
        addToken();

    }


    public String getMyData() {
        return myString;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        //tabLayout.getTabAt(4).setIcon(R.drawable.your_camera_icon);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Groups");
        adapter.addFrag(new TwoFragment(), "Transfers");
        adapter.addFrag(new ThreeFragment(), " Events ");
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.helpMenu:
                Intent intent3 = new Intent(HomeActivity.this, ChatHelp.class);
                startActivity(intent3);
                return true;


            case R.id.profileMenu:
                 Intent intent2 = new Intent(HomeActivity.this, Testingapp.class);
                 startActivity(intent2);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.menu_item).setEnabled(false);

        return super.onPrepareOptionsMenu(menu);
    }

    //FUNCTION TO CHECK USER SESSION

    public void checkUserSession()
    {
        SharedPreferences preferences=this.getSharedPreferences("info.androidhive.uplus",Context.MODE_PRIVATE);
        try
        {
                    //check value of shared preference
                    if(preferences.getString("Login","")!="")
                    {
                        if(Integer.parseInt(preferences.getString("Login",""))!=5)
                        {
                            Intent intent=new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"not logged in",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Please Login",Toast.LENGTH_LONG).show();
                    }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //END OF SESSION CHECKER

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.uplus",Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("Contacts","1");
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},100);
            }
        }
        //setupTabIcons();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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
                        if(isNetworkAvailable()) {
                            final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +": Online",Toast.LENGTH_SHORT );
                            //toast.show();

                            Intent intent=getIntent();

                            String memberId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                            //Toast.makeText(HomeActivity.this, "groups of ("+memberId+")", Toast.LENGTH_LONG).show();
                            BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
                            backgroundTask.execute("add",memberId);

                            EventWorker eventWorker = new EventWorker(getApplicationContext());
                            eventWorker.execute("eventList1");

                        }

                    }
                });
            }


        },0,40000);
        requestTransactions();
    }

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
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +": Local",Toast.LENGTH_SHORT );
                        //toast.show();
                        DbHelper dbHelper=new DbHelper(getApplicationContext());
                        dbHelper.fillAdapter(recyclerView,getApplicationContext());
                    }
                });
            }
        },0,5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showLoader()
    {
        Intent intent=getIntent();
        String Load=intent.getStringExtra("Load");

        if(Load!="" && Load!=null)
        {
            if(Integer.parseInt(Load)==1)
            {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                final String deepSLink = sharedPreferences.getString("deepLink", null);
                //Toast.makeText(HomeActivity.this,"And it is on: "+deepSLink,Toast.LENGTH_LONG).show();

                if (deepSLink != "" && deepSLink!=null) {
                    final String publicGid = sharedPreferences.getString("sharedLink", null);
                    //Toast.makeText(HomeActivity.this,"Which gave: "+publicGid,Toast.LENGTH_LONG).show();

                    if(publicGid != null || publicGid != "") {
                        //Get it from the shared pref
                        Log.e("DeepLink5", "is "+publicGid);

                        String method = "publicInvite";
                        progress = new ProgressDialog(HomeActivity.this);
                        progress.setCancelable(false);
                        progress.setMessage("Getting Invitation");
                        progress.show();

                        sharedPreferences.edit().putString("deepLink", "").apply();
                        sharedPreferences.edit().putString("sharedLink", "").apply();
                        Intent intent1 = getIntent();
                        intent1.putExtra("Load", "2");

                        String memberId = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), progress);
                        backgroundTask.execute("add", memberId);

                        EventWorker eventWorker = new EventWorker(getApplicationContext());
                        eventWorker.execute("eventList1");

                        PublicInviteWorker publicInviteWorker = new PublicInviteWorker(getApplicationContext(), progress, publicGid);
                        publicInviteWorker.execute(method, publicGid);
                    }else{
                        final ProgressDialog progress;
                        progress = new ProgressDialog(this);
                        progress.setTitle("Loading Content now");
                        progress.setMessage("Please wait while we are setting up your account...");
                        progress.show();

                        String memberId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                        BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
                        backgroundTask.execute("add",memberId);

                        EventWorker eventWorker = new EventWorker(getApplicationContext());
                        eventWorker.execute("eventList1");

                        progress.setCancelable(false);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        progress.dismiss();
                                        Intent intent1=getIntent();
                                        intent1.putExtra("Load","2");
                                        finish();
                                        startActivity(intent1);
                                        Toast.makeText(getApplicationContext(),"Done, Enjoy!",Toast.LENGTH_SHORT).show();
                                    }
                                }, 20000);
                    }
                }
                else
                {

                    final ProgressDialog progress;
                    progress = new ProgressDialog(this);
                    progress.setTitle("Loading Content now");
                    progress.setMessage("Please wait while we are setting up your account...");
                    progress.show();
                    String memberId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                    BackgroundTask backgroundTask=new BackgroundTask(getApplicationContext(),progress);
                    backgroundTask.execute("add",memberId);

                    EventWorker eventWorker = new EventWorker(getApplicationContext());
                    eventWorker.execute("eventList1");

                    progress.setCancelable(false);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progress.dismiss();
                                    Intent intent1=getIntent();
                                    intent1.putExtra("Load","2");
                                    finish();
                                    startActivity(intent1);
                                    Toast.makeText(getApplicationContext(),"Done, Enjoy!",Toast.LENGTH_SHORT).show();
                                }
                            }, 20000);
                }
            }
        }

    }

    public void addToken()
    {
        //FireBase Notifications
        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        String a = FirebaseInstanceId.getInstance().getToken();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {

                while (thread_running)
                {
                    Token = FirebaseInstanceId.getInstance().getToken();
                    if (Token != null) {

                        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("We saved some data "+response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                System.out.println("We din't saved any data "+Token);
                            }
                        })

                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> parametres = new HashMap<String, String>();
                                String userId = "1";
                                String action = "attachdvc";
                                parametres.put("Token",Token);
                                parametres.put("userId",userId);
                                parametres.put("action",action);
                                return parametres;
                            }
                        };
                        requestQueue.add(request);

                        System.out.println("Saved Device Token is "+Token);


                        thread_running = false;
                    }
                    else {
                        System.out.println("- Token not loaded - ");

                    }
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    public void requestTransactions()
    {
        if(isNetworkAvailable()) {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                   try
                   {
                        myDb.cleanData();
                        JSONArray array = new JSONArray(s);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);

                            myDb.insertData(
                                o.getString("amount"),
                                o.getString("pullName"),
                                o.getString("phone"),
                                o.getString("status"),
                                o.getString("transactionDate"),
                                o.getString("transactionColor")
                            );
                        }
                        //Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();
                   } catch (JSONException e) {
                        e.printStackTrace();
                   }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametres = new HashMap<String, String>();
                    String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                    String action = "transactions";
                    parametres.put("userId", userId);
                    parametres.put("action", action);
                    return parametres;
                }
            };
            requestQueue.add(stringRequest);
        }else{
        }
    }

    @Override
    public void onBackPressed()
    {
    }
}
