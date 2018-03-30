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
import java.util.List;
import java.util.Map;


/**
 * Created by user on 02/02/2018.
 */

public class EventWorker extends AsyncTask<String,Void,String> {
    List<Map<String, String>> listData=new ArrayList<Map<String, String>>();
    Context context;
    ArrayList<String>eventId=new ArrayList<String>();
    ArrayList<String>eventName=new ArrayList<String>();
    ArrayList<String>eventDesc=new ArrayList<String>();
    ArrayList<String>eventCover=new ArrayList<String>();
    ArrayList<String>eventLocation =new ArrayList<String>();
    ArrayList<String>eventContact=new ArrayList<String>();
    ArrayList<String>eventStart=new ArrayList<String>();
    ArrayList<String>eventEnd=new ArrayList<String>();
    RecyclerView recyclerView;
    Intent intent;
    String result="";
    RecyclerView.Adapter adapter;
    ProgressDialog progress;

    public EventWorker(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String url="http://67.205.139.137/api/index.php";
        String method=params[0];
        //Log.e("method",method);
        //check methos passed
        if(method.equals("eventList1"))
        {
            try {

                URL conn_url=new URL(url);
                String action="eventList1";
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create an OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpURLConnection.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line= buffReader.readLine())!=null){
                    result+=line;
                }
                JSONArray array=new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject=array.getJSONObject(i);



                    String memberPhone1=jsonObject.getString("eventId");
                    String memberId1 =jsonObject.getString("eventName");
                    String memberName1=jsonObject.getString("eventDesc");
                    String memberImage1=jsonObject.getString("eventCover");
                    String groupId1 =jsonObject.getString("eventLocation");
                    String updatedDate1 =jsonObject.getString("eventContact");
                    String memberType1 =jsonObject.getString("eventStart");
                    String memberContribution1 =jsonObject.getString("eventEnd");

                    eventId.add(memberPhone1);
                    eventName.add(memberId1);
                    eventDesc.add(memberName1);
                    eventCover.add(memberImage1);
                    eventLocation.add(groupId1);
                    eventContact.add(updatedDate1);
                    eventStart.add(memberType1);
                    eventEnd.add(memberContribution1);

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
        if (eventName.size() > 0) {
            try
            {
                Log.e("We got events (", eventName+")");
                EventsDb eventsDb = new EventsDb(context);
                for (int i = 0; i < eventName.size(); i++) {
                    String eventTicketStatus = "";
                    String eventTicketCode = "";

                    if (eventsDb.checkEventExists(eventId.get(i)) == false) {
                        boolean state = eventsDb.SaveEvents(eventId.get(i), eventName.get(i), eventDesc.get(i), eventCover.get(i), eventLocation.get(i), eventContact.get(i), eventStart.get(i), eventEnd.get(i), eventTicketStatus, eventTicketCode);
                        if (state == false) {
                            Log.e("D3 EventData Added:", eventName.get(i));
                        } else {
                            Log.e("D3 EventData Not:", eventName.get(i));
                        }
                    }
                    else
                    {
                        Log.e("EventData Already in:", eventName.get(i));
                    }
                    GetTickets getTickets = new GetTickets(context);
                    getTickets.execute("eventSeatsList", eventId.get(i).toString());


                }
            } catch (Exception ex) {
                // Toast.makeText(context, "D4 Wapi Wapi Wapi ", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
                Log.e("Event save", "Yanze",ex);
            }

        } else {
            Log.e("Yes No", eventName.toString()+" of "+result);
        }
    }

}