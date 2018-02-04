package info.androidhive.uplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import info.androidhive.uplus.activity.SharedPrefManager;

/**
 * Created by user on 28/12/2017.
 */

public class Invitation extends AppCompatActivity {

    ProgressDialog progress;
    ProgressBar progressBar;
    public ProgressBar progressImage;
    ImageView imgCover;
    TextView groupHolder, amountHolder, adminHolder;
    String groupId;
    public static Activity invitationpage;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> mDataset;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.invitation);
        Button rejectBtn = (Button) findViewById(R.id.btnReject);
        Button btnInvite = (Button) findViewById(R.id.btnJoin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressImage = (ProgressBar) findViewById(R.id.progressImage);
        imgCover = (ImageView) findViewById(R.id.imgBack);
        invitationpage = this;
        groupHolder = (TextView) findViewById(R.id.groupName);
        amountHolder = (TextView) findViewById(R.id.groupAmount);
        adminHolder = (TextView) findViewById(R.id.adminName);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        final String groupImage     = intent.getStringExtra("groupImage");
        final String groupName      = intent.getStringExtra("groupName");
        final String targetAmount   = intent.getStringExtra("targetAmount");
        final String groupBalance   = intent.getStringExtra("groupBalance");
        final String adminName      = intent.getStringExtra("adminName");
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> groupMembers = (ArrayList<String>) bundle.getStringArrayList("array_list");



        mDataset = new ArrayList<>();

        for (int i = 0; i < groupMembers.size(); i++) {
            mDataset.add(groupMembers.get(i));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
       // mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InvitationAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        //Toast.makeText(invitationpage, groupMembers.get(2)+" And "+groupMembers.get(3), Toast.LENGTH_SHORT).show();
        Log.e("ShitO: ", groupImage);
        groupHolder.setText(groupName);
        adminHolder.setText(adminName);
        amountHolder.setText(currencyConverter(targetAmount));

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteMember();
            }
        });
        if (Integer.parseInt(groupBalance) > 0) {
            int progressValue = (int) ((Integer.parseInt(targetAmount) * 100) / Integer.parseInt(groupBalance));
            if (progressValue > 10) {
                progressBar.setProgress(progressValue);
            } else {
                progressBar.setProgress(12);
            }
        } else {
            progressBar.setProgress(12);
        }

        if (isNetworkAvailable()) {
            //Toast.makeText(Invitation.this,"it must work",Toast.LENGTH_LONG).show();
            progressImage.setIndeterminate(true);
            progressImage.setVisibility(View.VISIBLE);
            if (groupImage != null) {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();
                //Toast.makeText(Invitation.this,"Image not null",Toast.LENGTH_LONG).show();

                Picasso.with(this).load(groupImage).into(imgCover, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        // Toast.makeText(Invitation.this,"Picaso Success",Toast.LENGTH_LONG).show();

                        Picasso.with(Invitation.this).load(groupImage).into(imgCover);
                        progressImage.setVisibility(View.INVISIBLE);
                        //Toast.makeText(Invitation.this,"Image loaded",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError() {
                        // Toast.makeText(Invitation.this,"Picaso Failed",Toast.LENGTH_LONG).show();

                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        int color1 = generator.getRandomColor();
                        progressImage.setVisibility(View.INVISIBLE);
                        TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted("Uplus"), color1);
                        imgCover.setImageDrawable(drawable);
                    }
                });

                TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(groupName), color1);
                imgCover.setImageDrawable(drawable);
            }
        } else {
            //Toast.makeText(Invitation.this,"No Network",Toast.LENGTH_LONG).show();

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(groupName), color1);
            imgCover.setImageDrawable(drawable);
        }
    }

    public void showMember(){

    }
    //method to invite members
    public void inviteMember() {
        String invitePhone = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
        String adminId = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        String method = "invite";
        //create instance og backgroundWorker
        //check the dat submit
        String status = "";
        int counter = 0;
        if (invitePhone != "") {
            if (invitePhone.length() < 20) {
                progress = new ProgressDialog(Invitation.this);
                progress.setTitle("Inviting now");
                progress.setMessage("Please wait while we are Inviting user...");
                progress.setCancelable(false);
                progress.show();
                AcceptInvitation acceptInvitation = new AcceptInvitation(this, progress);
                acceptInvitation.execute(method, groupId, adminId, invitePhone);
            } else {
                status = "Please Enter valid Phone Number";
            }
        } else {
            status = "Please Enter Phone Number of you Invitee";

        }
        if (counter > 0) {
            // Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
        }

    }

    public String currencyConverter(String data) {
        Double value = Double.parseDouble(data);
        DecimalFormat decimalFormat = new DecimalFormat("#,### RWF");
        return (decimalFormat.format(value));
    }

    //function to check if network is avalaible or not
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String textTobeConverted(String data) {
        String newData = "D";
        int length = data.length();
        if (length > 0) {
            newData = data.substring(0, 1);

        }
        return newData;
    }

}
