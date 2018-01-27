package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;

public class loginPassWord extends AppCompatActivity {
    Button btnPassword;
    EditText txtProfile;
    String pin,userId,userName;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pass_word);
        btnPassword=(Button)findViewById(R.id.btnPassWord);
       // txtPin=(EditText)findViewById(R.id.txtPin);
        txtProfile=(EditText)findViewById(R.id.txtProfile);
        Pinview pinview = (Pinview)findViewById(R.id.pinView);

        if(SharedPrefManager.getInstance(getApplication()).isLoggedIn()){
            Intent i =  new Intent(getApplicationContext(),HomeActivity.class);
            finish();
            startActivity(i);
        }
        //get shared preference data
        try
        {
            final SharedPreferences sharedPreferenced=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
            pin=sharedPreferenced.getString("pin","");
            userId=sharedPreferenced.getString("userId","");
            userName=sharedPreferenced.getString("userName","");
           // Toast.makeText(getApplicationContext(),pin,Toast.LENGTH_LONG).show();
            if(pin!="")
            {
                if(checkData()==true)
                {

                }
                else
                {
                    finish();
                }
            }
            else
            {
                //Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//                finish();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                //Toast.makeText(loginPassWord.this, ""+pinview.getValue(),Toast.LENGTH_LONG).show();
                loginTodata();
            }
        });


        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(loginPassWord.this, "Please Make that sure PIN is correct",Toast.LENGTH_LONG).show();
            }

        });
    }

    public void loginTodata()
    {

        Pinview pinview = (Pinview)findViewById(R.id.pinView);




        if(pin!="")
        {
            //String enteredPin=txtPin.getText().toString();
            //check if are the same
            if(Integer.parseInt(pin)==Integer.parseInt(pinview.getValue()))
            {
                //Toast.makeText(getApplicationContext(),"PIN ARE EQUAL",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),Profile.class);
                intent.putExtra("userId",userId);
                intent.putExtra("userName",userName);
                finish();
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Make that sure PIN is correct",Toast.LENGTH_LONG).show();

            }

        }
        else
        {
            Intent i=getIntent();
            finish();
            startActivity(i);
        }
    }
    //function to check if the null values are not submitted
    public boolean checkData()
    {
        boolean state;
        if(pin.length()<4)
        {
            state=false;
        }
        else if(pin==null)
        {
            state=false;
        }
        else if(userId==null)
        {
            state=false;
        }
        else
        {
            state=true;
        }
        return state;
    }

    }
