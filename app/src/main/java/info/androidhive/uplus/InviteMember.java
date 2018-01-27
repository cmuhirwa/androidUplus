package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.uplus.activity.BackgroundWorker;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;

public class InviteMember extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    String addedId,Name,Amount,groupImage, groupBalance;
    EditText txtInvited;
    String user;
    ProgressDialog progress;
    Button btnDOne;
    Button btnInvite;
    CircleImageView add4nebtn;

    private static final String TAG = InviteMember.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    boolean state=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        btnInvite = (Button) findViewById(R.id.invitebtn);
        add4nebtn =(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.add4nebtn);

        //check if in activity there are some data that come together
        Intent intent=getIntent();
        addedId     =intent.getStringExtra("addedId");
        Name        =intent.getStringExtra("Name");
        Amount      =intent.getStringExtra("Amount");
        groupImage  =intent.getStringExtra("Image");
        groupBalance= intent.getStringExtra("groupBalance");
        if(addedId!="")
        {
            user=addedId;
            state=false;
        }
        else
        {
            Toast.makeText(getApplicationContext(),addedId,Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
            user=sharedPreferences.getString("groupId","");
            Log.i("groupId",user);
            state=true;
        }

        txtInvited=(EditText)findViewById(R.id.txtInvited);
        try {

            toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state==true)
                    {
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else
                    {
                        goBack();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteMember();
            }
        });

        btnDOne=(Button)findViewById(R.id.btnDOne);
        btnDOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),groupdetails.class);
                intent.putExtra("Id",addedId);
                intent.putExtra("Name",Name);
                intent.putExtra("Amount",Amount);
                intent.putExtra("Image",groupImage);
                intent.putExtra("groupBalance",groupBalance);
                finish();
                startActivity(intent);
            }
        });
        }

    //Pull from phone
    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();

        }
    }

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                add4nebtn.setImageBitmap(photo);
            }else{
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_addp);

                add4nebtn.setImageBitmap(icon);
            }

            assert inputStream != null;
            if(inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        txtInvited.setText(contactNumber);
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        btnInvite.setText("INVITE " +contactName);

    }

        //method to invite members
    public void inviteMember()
    {
        String invitePhone=txtInvited.getText().toString();
        String adminId= SharedPrefManager.getInstance(getApplicationContext()).getUserId();
        String method="invite";
        //create instance og backgroundWorker
        //check the dat submit
        String status="";
        int counter=0;
        if(invitePhone!="")
        {
            if(invitePhone.length()<20)
            {
                progress = new ProgressDialog(InviteMember.this);
                progress.setTitle("Inviting now");
                progress.setMessage("Please wait while we are Inviting user...");
                progress.setCancelable(false);
                progress.show();
//                BackgroundWorker bgWorker=new BackgroundWorker(this,progress);
//                bgWorker.execute(method,user,adminId,invitePhone);
//                //call invite worker
                InviteWorker inviteWorker=new InviteWorker(this,progress);
                inviteWorker.execute(method,user,adminId,invitePhone);
            }
            else
            {
                status="Please Enter valid Phone Number";
                txtInvited.setFocusable(true);
                txtInvited.setText("");
                counter++;
            }
        }
        else
        {
            status="Please Enter Phone Number of you Invitee";
            txtInvited.setFocusable(true);
            txtInvited.setText("");
            counter++;
        }
        if(counter>0)
        {
            Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
        }

    }
    public void goBack()
    {
      try
      {
          Intent newt=new Intent(getApplicationContext(), HomeActivity.class);
          startActivity(newt);
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
    }
    @Override
    public void onBackPressed() {
    }
}
