package info.androidhive.uplus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import info.androidhive.uplus.activity.GetMembers;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.fragments.OneFragment;


public class BackgroundTask extends AsyncTask<String,Void,String> {
    ProgressDialog progress;
    Context ctx;
    RequestQueue requestQueue;
    String groupId = "";
    String name;
    String[] grp;
    String url = "http://67.205.139.137/api/index.php";
    int counter = 0;
    static ArrayList<String> data = new ArrayList<String>();
    static ArrayList<String> grpName = new ArrayList<String>();
    static ArrayList<String> DataId = new ArrayList<String>();
    static ArrayList<String> groupImage = new ArrayList<String>();
    static ArrayList<String> groupBalance = new ArrayList<String>();
    Intent intent;
    String result="";
    public BackgroundTask(Context ctx,ProgressDialog progress) {

        this.ctx = ctx;
        this.progress=progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //get method passed

        String method = params[0];
        //check methos passed
        if (method.equals("add")) {
            try {
                URL conn_url = new URL(url);
                String action = "listGroups";
                String memberId =params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&" + URLEncoder.encode("memberId", "UTF-8") + "=" + URLEncoder.encode(memberId, "UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream = httpURLConnection.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = buffReader.readLine()) != null) {
                    result += line;
                }
                JSONArray array = new JSONArray(result);
                grp = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);

                    name = String.valueOf(jsonObject.get("groupName"));
                    groupId = String.valueOf(jsonObject.get("groupId"));
                    String targetAmount = String.valueOf(jsonObject.get("targetAmount"));
                    String groupImaged=String.valueOf(jsonObject.get("groupImage"));
                    String balance=String.valueOf(jsonObject.get("groupBalance"));
                    //add data to array list
                    DataId.add(groupId);
                    data.add(name);
                    groupImage.add(groupImaged);
                    grpName.add(targetAmount);
                    groupBalance.add(balance);
                    grp[i] = name;
                    counter = counter + 1;

                }
                buffReader.close();
                inStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(data.size()>0){

            try {
                //Toast.makeText(ctx,"we got ("+result+")",Toast.LENGTH_LONG).show();
                DbHelper dbHelper=new DbHelper(ctx);
                boolean test;
                for (int j = 0; j < data.size(); j++)
                {
                    //Log.i("group", data.get(j));
                    //Toast.makeText(ctx,"Balance of "+data.get(j)+". is: "+groupBalance.get(j),Toast.LENGTH_LONG).show();


                    //Check if the group is new
                        if(dbHelper.checkGroupId(DataId.get(j))==false)
                        {
                            //now data can be added
                            test = dbHelper.saveToLocalDatabase(DataId.get(j),data.get(j),grpName.get(j),groupImage.get(j),groupBalance.get(j));

                            if(test)
                            {
                                //Toast.makeText(ctx,"You are added in "+data.get(j)+".",Toast.LENGTH_LONG).show();
                                //notify gui


                                showNotification(DataId.get(j),data.get(j),grpName.get(j),groupImage.get(j),groupBalance.get(j));
                            }
                            else
                            {
                                //Toast.makeText(ctx,"Data not Added",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            // progress.dismiss();
                            // Check if the group image has changed
                            if(dbHelper.checkGroupImage(DataId.get(j))==true)
                            {
                                dbHelper.updateGroupImage(DataId.get(j),groupImage.get(j),data.get(j));
                            }
                            else
                            {
                                dbHelper.updateGroupImage(DataId.get(j),groupImage.get(j),data.get(j));
                            }
                        }
                        //Log.e("Lopping members of:", data.get(j)+" : "+groupImage.get(j));
                        final String groupId = DataId.get(j);
                        final String backGroupName = data.get(j);
                        final String backGroupAmount = grpName.get(j);
                        final String backGroupImage = groupImage.get(j);
                        final String backGroupBalance = groupBalance.get(j);
                        final SaveMembers myDb = new SaveMembers(ctx);


                        GetMembers getMembers=new GetMembers(ctx);
                        getMembers.execute("members",groupId);

                        WithDrawListWorker worker= new WithDrawListWorker(ctx);
                        worker.execute("withdrawlist",groupId,backGroupName,backGroupAmount,backGroupImage, backGroupBalance);
                }
                data.clear();
                DataId.clear();
                grpName.clear();
                dbHelper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        else{
            //Toast.makeText(ctx,"No group to loop: ("+data.size()+")",Toast.LENGTH_LONG).show();
        }
    }


    public void showNotification(String NgroupId, String NgroupName, String NgroupAmount, String NgroupImage, String NgroupBalance)
    {
        int uniqueID = Integer.parseInt(groupId);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(ctx);

        //Bitmap bitmap = getBitmapFromURL(NgroupImage);

        notification.setAutoCancel(true);
        notification.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon));
        notification.setSmallIcon(R.drawable.icon);
        notification.setTicker("Uplus Invitation");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Uplus Invitation");
        notification.setStyle(new NotificationCompat.BigTextStyle()
            .bigText("You have been added into a group called ("+NgroupName+")"));
        notification.setContentText("You have been added into a group called ("+NgroupName+")");
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);

        Intent intent = new Intent(ctx, groupdetails.class);
        intent.putExtra("Id",NgroupId);
        intent.putExtra("Name",NgroupName);
        intent.putExtra("Amount",NgroupAmount);
        intent.putExtra("Image",NgroupImage);
        intent.putExtra("groupBalance",NgroupBalance);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and isuing it
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

}
