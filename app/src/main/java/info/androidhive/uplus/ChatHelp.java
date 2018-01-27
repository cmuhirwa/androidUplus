package info.androidhive.uplus;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.Chats;

/**
 * Created by user on 05/11/2017.
 */

public class ChatHelp extends AppCompatActivity {
    ImageView myImageButton, callHelpBtn;
    private static DatabaseReference mDatabaseReference;
    private String temp_key;
    EditText helptext;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mChildEventListener;
    private List<HelpChats> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HelpChatAdapter mAdapter;
    private String userId;
    private static final int REQUEST_PHONE_CALL = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        mDatabaseReference = mFirebaseDatabase.getReference().child("help");
        myImageButton   = (ImageView) findViewById(R.id.btnhelp);
        callHelpBtn     = (ImageView) findViewById(R.id.callBtn);
        helptext = (EditText) findViewById(R.id.helptext);

        recyclerView = (RecyclerView) findViewById(R.id.fragment_chat_recycler_view);

        mAdapter = new HelpChatAdapter(chatList, userId, getApplication());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        callHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "About to call help desk on : +250784848236",Snackbar.LENGTH_LONG).setAction("Action",null).show();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+250784848236"));
                if (ActivityCompat.checkSelfPermission(ChatHelp.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(ChatHelp.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    return;
                }
                else
                {
                    startActivity(callIntent);
                }

            }
        });

        myImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String helptexts = helptext.getText().toString();
                if(helptexts.equals("") || helptexts.equals(null) || helptexts.equals(" ")) {


                }else{
                    Map<String,Object> map = new HashMap<String, Object>();
                    temp_key = mDatabaseReference.push().getKey();
                    mDatabaseReference.updateChildren(map);

                    DatabaseReference message_root = mDatabaseReference.child(temp_key);
                    Map<String,Object> map2 = new HashMap<String, Object>();
                    map2.put("name", userId);
                    SharedPreferences sharedPreferences= getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
                    String userName=sharedPreferences.getString("userName",null);
                    map2.put("userName",userName);
                    map2.put("msg",helptext.getText().toString());
                    helptext.setText("");
                    recyclerView.scrollToPosition(chatList.size()-1);
                    message_root.updateChildren(map2);
                    Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();

                }
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);


    }

    /*
       public void initiateCall(){
           if(checkifAlreadyhaveCallPermission()){
               makeCall();
           }else{
               requestCallPermission();
           }

       }

       public boolean checkifAlreadyhaveCallPermission(){
           int result = ContextCompat.checkSelfPermission(ChatHelp.this, android.Manifest.permission.CALL_PHONE);
           if(result== PackageManager.PERMISSION_GRANTED){
               return true;
           }
           else{
               return false;
           }

       }

       public void requestCallPermission(){
           ActivityCompat.requestPermissions(ChatHelp.this, new String[]{Manifest.permission.CALL_PHONE},1);
       }

       public void onRequestPermissionResult(int requestCode, String permission[], int[] grantResult){
           switch (requestCode){
               case 1:{
                   if(grantResult.length> 0 && grantResult[0] ==  PackageManager.PERMISSION_GRANTED){
                       makeCall();
                   }else{
                       Toast.makeText(ChatHelp.this, "You have denied use of call feature", Toast.LENGTH_SHORT).show();
                   }
                   return;
               }
           }

       }

       public void makeCall(){

           String telephoneNumber = "0784848236";
           Intent callIntent = new Intent(Intent.ACTION_CALL);
           callIntent.setData(Uri.parse("tel:"+ telephoneNumber));
           startActivity(callIntent);

       }
   */
    private String chat_msg,chat_time, chat_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        int meMessage=0,otherMessage=0;
        while (i.hasNext()){

            chat_time  = (String) ((DataSnapshot)i.next()).getValue();
            chat_msg   = (String) ((DataSnapshot)i.next()).getValue();
            chat_name  = (String) ((DataSnapshot)i.next()).getValue();

            HelpChats helpChats = new HelpChats(chat_time,chat_msg,chat_name);
            chatList.add(helpChats);
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(chatList.size()-1);

            //Toast.makeText(ChatHelp.this, "Data is: "+chat_user_name, Toast.LENGTH_SHORT).show();

        }
    }

}