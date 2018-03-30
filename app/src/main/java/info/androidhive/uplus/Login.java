package info.androidhive.uplus;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.uplus.activity.AddGroup;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.activity.SignUp;
import info.androidhive.uplus.fragments.OneFragment;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText txtPhone;
    ProgressDialog progress;
    CardView cardError;
    CountryCodePicker ccp;
    SharedPrefManager sharedPrefManager;
    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);

        if(SharedPrefManager.getInstance(getApplication()).isLoggedIn()){
            Intent i =  new Intent(getApplicationContext(),HomeActivity.class);
            finish();
            startActivity(i);
        }


        //setContentView(R.layout.activity_login);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        cardError=(CardView)findViewById(R.id.cardError);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        askPermission();
        //    Dexter.withActivity(this)
//          .withPermission(Manifest.permission.CAMERA)
//	.withListener(new PermissionListener() {
//        @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
//        @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
//        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
//    }).check();

        listenNetwork();
        loginAccount(btnLogin);
        fa=this;

    }

    //FUNCTION TO LISTEN FOR NETWORK
    public void listenNetwork()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        //toast.show();
                        if(isNetworkAvailable())
                        {
                            btnLogin.setEnabled(true);
                            cardError.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            btnLogin.setEnabled(false);
                            cardError.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        },0,5000);
    }


    //FUNCTION TO ASK FOR PERMISSION
    public void askPermission() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                //call the function to ask for permission
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){

        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},100);
            }
        }
    }





    //function to login
    public void loginAccount(Button btn)
    {
        try
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open next solided activity
                    String phoneNumber=txtPhone.getText().toString();
                    //String countryCode=ccp.getSelectedCountryCode();

                    phoneNumber= 0 + phoneNumber;

                    //check data being submitted
                    if(isNetworkAvailable())
                    {
                        if(phoneNumber!="" && phoneNumber.length()==10)
                        {
                            //create instance of signup class
                            if(isNetworkAvailable())
                            {
                                progress = new ProgressDialog(Login.this);
                                progress.setTitle("Login");
                                progress.setMessage("Please wait...");
                                progress.isIndeterminate();
                                progress.setCancelable(false);
                                progress.show();
                                String method="signup";
                                SignUp signUp=new SignUp(fa,progress);
                                signUp.execute(method,phoneNumber);
                                SharedPrefManager.getInstance(getApplicationContext()).number(phoneNumber);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Check Internet",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            txtPhone.setFocusable(true);
                            txtPhone.setText("");
                            Toast.makeText(getApplicationContext(),"Please enter a valid Phone",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Network Error");
                        builder.setMessage("There is an error");
                        builder.show();
                    }


                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isNetworkAvailable() {
                        try
                        {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            isNetworkAvailable();
                                        }
                                    }, 1000);

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
