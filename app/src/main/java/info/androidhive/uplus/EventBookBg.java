package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import info.androidhive.uplus.activity.HomeActivity;

/**
     * Created by user on 02/02/2018.
     */

public class EventBookBg extends AsyncTask<String,Void,String> {
    Context context;
    String method,pullNumber,eventId,seatCode,userId,result="",ticketVerCode="",eventName, eventLocation;
    ProgressDialog progressDialog;
    int counter = 0;
    public EventBookBg(Context ctx,ProgressDialog progressDialog, String eventName, String eventLocation){
        context=ctx;
        this.progressDialog = progressDialog;
        this.eventName      = eventName;
        this.eventLocation  = eventLocation;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        method       = params[0];
        pullNumber   = params[1];
        eventId      = params[2];
        seatCode     = params[3];
        userId       = params[4];

        String login_url="http://67.205.139.137/api/index.php";
        if(method.equals("eventBook"))
        {
            try{
                String action     = method;
                String pullNumber = params[1];
                String eventId    = params[2];
                String seatCode   = params[3];
                String userId     = params[4];

                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("pullNumber","UTF-8")+"="+URLEncoder.encode(pullNumber,"UTF-8")+"&"
                        +URLEncoder.encode("eventId","UTF-8")+"="+URLEncoder.encode(eventId,"UTF-8")+"&"
                        +URLEncoder.encode("seatCode","UTF-8")+"="+URLEncoder.encode(seatCode,"UTF-8")+"&"
                        +URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpCon.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                JSONArray array = new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject = array.getJSONObject(i);
                    ticketVerCode=jsonObject.getString("ticketCode");
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
    protected void onProgressUpdate(Void... values) {
        //super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(progressDialog!=null){
            EventsDb eventsDb = new EventsDb(context);
            Log.e("Tccc Resluts ", "of"+result);
            boolean updateEvt = eventsDb.updateEventTicket(eventId,ticketVerCode);
            if(updateEvt == true){
                Log.e("Ticcc", "Updated the purchase ("+ticketVerCode+") of ("+eventId+")");
            }
            else {
                Log.e("Ticcc", "Updated Not the purchase ("+ticketVerCode+") of ("+eventId+")");
            }
            progressDialog.dismiss();
            Intent intent = new Intent(context, EventBokedQrPage.class);
            intent.putExtra("qrCode",ticketVerCode);
            intent.putExtra("EventLocation",eventLocation);
            intent.putExtra("EventName",eventName);
            HomeActivity.homepage.finish();
            EventPage.eventPage.finish();
            EventPage.eventPage.startActivity(intent);
        }
    }
}