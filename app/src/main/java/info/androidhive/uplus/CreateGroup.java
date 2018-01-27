package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.BackgroundWorker;

public class CreateGroup extends AppCompatActivity {
    Button btnCreateGroup,btnSKip;
    String groupId;
    ProgressDialog progress;
    Spinner spnCollection;
    EditText txtAccount;
    String bankId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent=getIntent();

            groupId=intent.getStringExtra("groupId");
        if(groupId!="")
        {
            //Toast.makeText(getApplicationContext(),groupId,Toast.LENGTH_LONG).show();
        }
        spnCollection=(Spinner)findViewById(R.id.spnCollectionAccount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
        spnCollection.setAdapter(adapter);
        spnCollection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=spnCollection.getSelectedItemPosition();
                if(index==0)
                {
                 bankId="1";
                }
                else if(index==1)
                {
                    bankId="2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtAccount=(EditText)findViewById(R.id.txtPhoneCollection);
        btnCreateGroup=(Button)findViewById(R.id.btnCreateGroup);
        btnSKip=(Button)findViewById(R.id.btnSkip);
        btnSKip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent=new Intent(getApplicationContext(),InviteMember.class);
                    intent.putExtra("addedId",groupId);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollection();
            }
        });
    }

    private void createCollection() {
        String error="";
        int errc=0;
        String accountNumber=txtAccount.getText().toString();
      if(groupId!="" && groupId!=null)
      {
         if(accountNumber.length()==10)
         {
            if(bankId!="" && bankId!=null)
            {
                String finalData="groupId:"+groupId+"\n accountNumber: "+accountNumber+"\n bankId: "+bankId;
                String method="collection";
                //Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();

                progress = new ProgressDialog(CreateGroup.this);
                progress.setTitle("Creating Collection");
                progress.setMessage("Please wait...");
                progress.show();
                CollectionWorker collectionWorker=new CollectionWorker(getApplicationContext(),progress);
                collectionWorker.execute(method,groupId,accountNumber,bankId);
            }
            else
            {
                error="Please select Account";
                errc++;
            }
         }
         else
         {
             error="Please enter valid Phone";
             errc++;
             txtAccount.setFocusable(true);
             txtAccount.setText("");
         }
      }
      else
      {
          error="Something wrong try gain later";
          errc++;
      }
      if(errc>0)
      {
          Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG);
      }

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i=getIntent();
//        finish();
//        startActivity(i);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//    }

    @Override
    public void onBackPressed() {
    }
}
