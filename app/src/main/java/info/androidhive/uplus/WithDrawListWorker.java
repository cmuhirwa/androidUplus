package info.androidhive.uplus;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by RwandaFab on 6/15/2017.
 */
public class WithDrawListWorker extends AsyncTask<String,Void,String>
{
    Context context;
    AlertDialog alert;
    String result="";
    TextView returnId;
    String type, groupId, NgroupName, NgroupAmount, NgroupImage, NgroupBalance;
    ProgressDialog progress;
    CardView cardView;
    String requestId,gid,Amount,memberName;
    public WithDrawListWorker(Context ctx){
        context=ctx;
        //this.cardView=cardView;
    }
    @Override
    protected String doInBackground(String... params) {
        type            =params[0];
        NgroupName      =params[2];
        NgroupAmount    =params[3];
        NgroupImage     =params[4];
        NgroupBalance   =params[5];
        //Log.e("GroupBalance ", NgroupBalance);
        String login_url="http://67.205.139.137/api/index.php";
        if(type.equals("withdrawlist"))
        {
            try{
                String action="withdrawlist";
                groupId=params[1];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                JSONArray array = new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject = array.getJSONObject(i);
                    requestId=jsonObject.getString("requestId");
                    gid=jsonObject.getString("groupId");
                    Amount=jsonObject.getString("amount");
                    memberName=jsonObject.getString("memberName");
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //alert=new AlertDialog.Builder(context).create();
        //alert.setTitle("Return status.......");
    }

    @Override
    protected void onPostExecute(String result) {
       // Toast.makeText(context, "About to save: " + result+ " Or ("+groupId+")", Toast.LENGTH_LONG).show();

        //alert.setMessage(result);
        //alert.show();
        //save data offline
        if(gid != null) {

            try {
              //  Toast.makeText(context, "About to Check Request for: " + gid, Toast.LENGTH_LONG).show();

                RequestHelper helper = new RequestHelper(context);
                if (helper.checkRequest(gid) == false){
                    //Toast.makeText(context, "New incoming Request", Toast.LENGTH_LONG).show();

                    boolean save = helper.saveToLocalDatabase(requestId, gid, Amount, memberName);
                    if (save) {
                        showNotification(memberName, Amount);

                        // Toast.makeText(context, "Request saved" + result, Toast.LENGTH_LONG).show();
//                       groupdetails groupdet = new groupdetails();
//                       groupdet.getRequests();
//                       Toast.makeText(context, "Refreshed the UI Record" + result, Toast.LENGTH_LONG).show();

                    }
                } else {
                   // Toast.makeText(context, "Request Exists", Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
              // Toast.makeText(context, "Error: " + ex, Toast.LENGTH_LONG).show();
              //  Log.e("YOUR_APP_LOG_TAG", "I got an error", ex);
            }
        }
        else {
            RequestHelper helper = new RequestHelper(context);
            boolean clean = helper.cleantable(groupId);
            if (clean) {
             //   Toast.makeText(context, "Clear Fix for: " + groupId, Toast.LENGTH_LONG).show();
            }

        }
    }
    //function to return the id
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void showNotification(String NmemberName, String Namount)
    {
        int uniqueID = Integer.parseInt(groupId);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(context);

        notification.setAutoCancel(true);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon));
        notification.setSmallIcon(R.drawable.icon);
        notification.setTicker("Withdraw request");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Withdraw Request");
        notification.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(NmemberName+" Is requesting to withdraw "+Namount+" Rwf in "+NgroupName+", if you are the treasurer of this group, please Approve or Reject his/her request within 48 Hours."));
        notification.setContentText(NmemberName+" Is requesting to withdraw "+Namount+" Rwf");
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);

        Intent intent = new Intent(context, groupdetails.class);
        intent.putExtra("Id",groupId);
        intent.putExtra("Name",NgroupName);
        intent.putExtra("Amount",NgroupAmount);
        intent.putExtra("Image",NgroupImage);
        intent.putExtra("groupBalance",NgroupBalance);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and isuing it
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

    }
}
