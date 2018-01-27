package info.androidhive.uplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Receipt extends AppCompatActivity {
    String data1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        //read shared Preference Dta
        SharedPreferences sharedPreferences=this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
        data1=sharedPreferences.getString("contributeStatus","");
    }
    //function to send data request again
    public void sendRequest()
    {
        final int[] num = {0};
        final Timer timers  = new Timer();
        timers.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Toast toast  = Toast.makeText(getApplicationContext(), ++num[0] +"",Toast.LENGTH_SHORT );
                        if(num[0]<20)
                        {
                           // ContributeWorker contributeWorker=new ContributeWorker(getApplicationContext());
                           // contributeWorker.execute("resend",data1);
                        }
                        else
                        {
                            cancel();
                        }
                        toast.show();
                    }
                });
            }
        },0,10000);
    }
}
