package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;

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

import info.androidhive.uplus.activity.GetMembers;

/**
 * Created by user on 02/02/2018.
 */

public class EventWorker extends AsyncTask<String,Void,String> {
    ProgressDialog progress;
    Context ctx;
    String url = "http://67.205.139.137/api/index.php",result = "", xeventId, xeventName, xeventDesc, xeventCover, xeventLocation, xeventContact, xeventStart, xeventEnd;
    int counter = 0;
    static ArrayList<String> eventId        = new ArrayList<String>();
    static ArrayList<String> eventName      = new ArrayList<String>();
    static ArrayList<String> eventDesc      = new ArrayList<String>();
    static ArrayList<String> eventCover     = new ArrayList<String>();
    static ArrayList<String> eventLocation  = new ArrayList<String>();
    static ArrayList<String> eventContact   = new ArrayList<String>();
    static ArrayList<String> eventStart     = new ArrayList<String>();
    static ArrayList<String> eventEnd       = new ArrayList<String>();



    Intent intent;

    public EventWorker(Context ctx, ProgressDialog progress) {
        this.ctx = ctx;
        this.progress = progress;
    }


    @Override
    protected String doInBackground(String... params) {
        //get method passed

        String method = params[0];
        //check methos passed
        if (method.equals("eventList")) {
            try {
                URL conn_url = new URL(url);
                String action = "eventList";
                //create httpUrlConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8");
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
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);

                    xeventId        = String.valueOf(jsonObject.get("eventId"));
                    xeventName      = String.valueOf(jsonObject.get("eventName"));
                    xeventDesc      = String.valueOf(jsonObject.get("eventDesc"));
                    xeventCover     = String.valueOf(jsonObject.get("eventCover"));
                    xeventLocation  = String.valueOf(jsonObject.get("eventLocation"));
                    xeventContact   = String.valueOf(jsonObject.get("eventContact"));
                    xeventStart     = String.valueOf(jsonObject.get("eventStart"));
                    xeventEnd       = String.valueOf(jsonObject.get("eventEnd"));

                    //add data to array list
                    eventId.add(xeventId);
                    eventName.add(xeventId);
                    eventDesc.add(xeventId);
                    eventCover.add(xeventId);
                    eventLocation.add(xeventId);
                    eventContact.add(xeventId);
                    eventStart.add(xeventId);
                    eventEnd.add(xeventId);
                    counter     = counter + 1;

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
    protected void onPostExecute(String result) {
        if (eventId.size() > 0) {

            Log.e("Yes Yes", eventId.toString());

        } else {
            Log.e("Yes No", eventId.toString());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //super.onProgressUpdate(values);
    }

}

