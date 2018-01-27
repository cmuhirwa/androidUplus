package info.androidhive.uplus;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Build.*;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.uplus.activity.SharedPrefManager;

public class Testingapp extends AppCompatActivity {
    TextView txtUserMobile;
    CircleImageView imgProfile;
    TextView user_profile_name;
    Switch mtnSwitch, tigoSwitch, airtelSwitch, visaSwitch, masterSwitch;
    Context ctx;
    Button dangerRemove;
    ProgressDialog progress;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.ctx = ctx;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgProfile=(CircleImageView)findViewById(R.id.imgProfile);
        String number = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
        SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
        final String userImage= sharedPreferences.getString("userImage",null);
        String userName=sharedPreferences.getString("userName",null);
       // Toast.makeText(this,userImage,Toast.LENGTH_LONG).show();
        txtUserMobile=(TextView)findViewById(R.id.txtUserMobile);
        txtUserMobile.setText("MTN Mobile Money: +25"+number);
        user_profile_name=(TextView)findViewById(R.id.user_profile_name);
        mtnSwitch = (Switch)findViewById(R.id.switchMtn);
        tigoSwitch = (Switch)findViewById(R.id.switchTigo);
        airtelSwitch = (Switch)findViewById(R.id.switchAirtel);
        visaSwitch = (Switch)findViewById(R.id.switchVisa);
        masterSwitch = (Switch)findViewById(R.id.switchMaster);
        dangerRemove = (Button)findViewById(R.id.dangerRemove);
        progress = new ProgressDialog(Testingapp.this);

        dangerRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Testingapp.this);
                builder.setTitle("Are you sure you want to delete your account forever?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        progress.setTitle("Bye Bye! hope to see you soon");
                        progress.setMessage("Wait a minute, We are removing all of your Uplus data...");
                        progress.setCancelable(false);
                        progress.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (VERSION_CODES.KITKAT <= VERSION.SDK_INT) {
                                    progress.dismiss();
                                    ((ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE))
                                            .clearApplicationUserData(); // note: it has a return value!
                                    Toast.makeText(Testingapp.this, "Thanks for being with us. Bye Bye!", Toast.LENGTH_LONG).show();
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(Testingapp.this, "Dint Work!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 20000);

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Snackbar.make(view, "Thanks, keep enjoying!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        mtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Sorry! Tigo Cash is currently not available on Uplus, we shall inform you once available.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mtnSwitch.setChecked(false);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "MTN not checked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked==false)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Oops! You can not disable all you payment accounts.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mtnSwitch.setChecked(true);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Thanks.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tigoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Sorry! Tigo Cash is currently not available on Uplus, we shall inform you once available.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    tigoSwitch.setChecked(false);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        airtelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Sorry! Airtel Money is currently not available on Uplus, we shall inform you once available.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    airtelSwitch.setChecked(false);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        visaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Uplus in partnership with Bank of Kigali is facilitating Visa Card transactions, this service will start working this January 2018.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    visaSwitch.setChecked(false);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        masterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(Testingapp.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Uplus in partnership with Bank of Kigali is facilitating Mater Card transactions, this service will start working this January 2018.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    masterSwitch.setChecked(false);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        if(userName!=null){
            user_profile_name.setText(userName);
        }
        if(userImage!=null && userImage.length()>10){
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();

            Picasso.with(this).load(userImage).into(imgProfile,new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                    Picasso.with(Testingapp.this).load(userImage).into(imgProfile);
                }

                @Override
                public void onError() {
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color1 = generator.getRandomColor();

                    TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted("Uplus"), color1);
                    imgProfile.setImageDrawable(drawable);
                }
            });

            TextDrawable drawable = TextDrawable.builder().buildRect(textTobeConverted(userName), color1);
            imgProfile.setImageDrawable(drawable);
        }

    }
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

}
