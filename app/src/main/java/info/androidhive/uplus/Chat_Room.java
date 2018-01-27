package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import info.androidhive.uplus.activity.ModifyGroup;
import info.androidhive.uplus.activity.SharedPrefManager;

public class Chat_Room  extends AppCompatActivity{

    ImageButton myImageButton;
    Toolbar toolbar;
    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation,txtChatRoom;
    ArrayList<String>messageList=new ArrayList<String>();
    ArrayList<String>otherMessageList=new ArrayList<String>();
    ArrayList<String>chatNumber=new ArrayList<String>();
    ArrayList<String>OtherchatNumber=new ArrayList<String>();
    private String user_name,room_name,room_id;
    private DatabaseReference root ;
    private String temp_key;
    RecyclerView recyclChat,recyclerChat1;
    RecyclerView.Adapter adapter,adapter1;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    LinearLayout linearLayout1,linearLayout2;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int clicked=item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);


        final SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
        final String userN =sharedPreferences.getString("userName",null);



        myImageButton       = (ImageButton) findViewById(R.id.btn_send);
        input_msg           = (EditText) findViewById(R.id.msg_input);
        chat_conversation   = (TextView) findViewById(R.id.textView);
        user_name           = getIntent().getExtras().get("user_name").toString();
        room_name           = getIntent().getExtras().get("room_name").toString();
        room_id             = getIntent().getExtras().get("room_id").toString();
        setTitle("Room - "+room_name);
        txtChatRoom         =(TextView)findViewById(R.id.txtChatRoom);
        txtChatRoom.setText(room_name);
        root                = FirebaseDatabase.getInstance().getReference().child("group"+room_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.chatToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("Chat room: "+room_name);
        myImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("Phone",user_name);
                map2.put("msg",input_msg.getText().toString());
                map2.put("userName", userN);
                input_msg.setText("");

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chat_msg,chat_user_name, chat_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        int meMessage=0,otherMessage=0;
        while (i.hasNext()){

            chat_user_name  = (String) ((DataSnapshot)i.next()).getValue();
            chat_msg        = (String) ((DataSnapshot)i.next()).getValue();
            chat_name       = (String) ((DataSnapshot)i.next()).getValue();


            if(Integer.parseInt(user_name)==Integer.parseInt(chat_user_name))
            {
                String chatPiece=chat_msg;
                messageList.add(chatPiece);
                chatNumber.add(chat_name);
                meMessage++;
            }
            else
            {
                String otherChatPiece=chat_msg;
                otherMessageList.add(otherChatPiece);
                otherMessage++;
                OtherchatNumber.add(chat_name);
            }
        }

        recyclChat=(RecyclerView)findViewById(R.id.recyclerChat);
        recyclerChat1=(RecyclerView)findViewById(R.id.recyclerChat1);
        adapter=new GroupChatAdapter(messageList,chatNumber);
        adapter1=new GroupChatAdapter1(otherMessageList,OtherchatNumber);
        recyclerChat1.setHasFixedSize(true);
        recyclChat.setHasFixedSize(true);
        recyclerChat1.setAdapter(adapter1);
        recyclChat.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(this);
        layoutManager1=new LinearLayoutManager(this);
        recyclChat.setLayoutManager(layoutManager);
        recyclerChat1.setLayoutManager(layoutManager1);
        adapter.notifyDataSetChanged();
        adapter1.notifyDataSetChanged();

    }
    public String removeWhiteSpace(String data)
    {
        String b= data.replaceAll("\\s+","");
        return b;
    }
}
