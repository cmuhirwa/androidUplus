package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.uplus.activity.SharedPrefManager;

public class Transactions extends AppCompatActivity {

    DatabaseHelper myDb;
    ImageButton addBtn;
    private static final String DATA_URL = "http://67.205.139.137/api/index.php";
    RequestQueue requestQueue;
    DatabaseHelper database;
    RecyclerView recyclerView;
    TransactionsAdapter recycler;
    List<TransationsList> Listitem;
    View Statusbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);
        myDb = new DatabaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Listitem =new ArrayList<TransationsList>();
        addBtn = (ImageButton) findViewById(R.id.addbtn);





        AddData();
        //requestTransactions();
        ViewData();

    }

    public void AddData(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG).show();
                requestTransactions();
            }
        });
    }

    public  void ViewData(){

        database = new DatabaseHelper(Transactions.this);
        Listitem=  database.getdata();
        recycler =new TransactionsAdapter(Listitem);

        Log.i("HIteshdata",""+Listitem);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);


    }

    public void requestTransactions(){
        if(isNetworkAvailable()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Refreshing Transactions...");
            //progressDialog.setCancelable(false);
            progressDialog.show();

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DATA_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    progressDialog.dismiss();

                    //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                    try {
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
                                    o.getString("transactionColor"));
                        }

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

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
                    String userId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                    String action = "transactions";
                    parametres.put("userId", userId);
                    parametres.put("action", action);
                    return parametres;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(Transactions.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
