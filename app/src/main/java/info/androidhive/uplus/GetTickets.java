package info.androidhive.uplus;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
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
 * Created by RwandaFab on 7/17/2017.
 */

public class GetTickets extends AsyncTask<String,Void,String> {
    List<Map<String, String>> listData=new ArrayList<Map<String, String>>();
    Context context;
    ArrayList<String>eventBack=new ArrayList<String>();
    ArrayList<String>ticketCode=new ArrayList<String>();
    ArrayList<String>price=new ArrayList<String>();
    ArrayList<String>ticketsName=new ArrayList<String>();
    Intent intent;
    String result="";
    ProgressDialog progress;
    public GetTickets(Context context)
    {
        this.context=context;
    }


    @Override
    protected String doInBackground(String... params) {
        String url="http://67.205.139.137/api/index.php";
        String method=params[0];
        String eventId=params[1];

        Log.i("Wegot this: ", eventId);
        //check methos passed
        if(method.equals("eventSeatsList"))
        {
            try {
                URL conn_url=new URL(url);
                String action="eventSeatsList";
                //String groupId=params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create an OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"+URLEncoder.encode("eventId","UTF-8")+"="+URLEncoder.encode(eventId,"UTF-8");
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

                    String eventBack1=jsonObject.getString("eventBack");
                    String memberPhone1=jsonObject.getString("ticketCode");
                    String memberId1 =jsonObject.getString("price");
                    String memberName1=jsonObject.getString("ticketsName");

                    eventBack.add(eventBack1);
                    ticketCode.add(memberPhone1);
                    price.add(memberId1);
                    ticketsName.add(memberName1);
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
    protected void onPostExecute(String resulst) {

        //Toast.makeText(context,"Members We got: "+memberName.size(),Toast.LENGTH_LONG).show();
        //create listview array Adapter
        if(ticketCode.size()>=1) {
            try
            {
                Integer ticketCount = ticketCode.size();
                SaveTickets saveTickets = new SaveTickets(context);
                if (saveTickets.checkTicketChange(ticketCount, eventBack.get(0)) == false) {
                    saveTickets.cleanTicketBeforeCreate(eventBack.get(0));
                    // Toast.makeText(context,"Table Cleaned for:"+groupIdBack.get(0),Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < ticketCode.size(); i++) {
                    if (saveTickets.checkTicketId(eventBack.get(i), ticketCode.get(i)) == false) {
                        //final String dataTobesaved = eventBack.get(i), ticketCode.get(i), price.get(i), ticketsName.get(i);
                        boolean state = saveTickets.saveTickets(eventBack.get(i), ticketCode.get(i), price.get(i), ticketsName.get(i));
                        if (state == false) {
                            Log.e("D3 TicketData Added:", ticketCode.get(i));
                        } else {
                            Log.e("D3 TicketData Not:", ticketCode.get(i));
                        }
                    }
                    else
                    {
                        Log.i("EventData Updateding:", ticketCode.get(i));
                        /*boolean saveState = saveTickets.checkUpdatedDate(memberId.get(i), memberContribution.get(i));
                        if (saveState == false) {
                            boolean updated = saveMembers.updateMemberAmount(memberId.get(i), groupIdBack.get(i));
                            if(updated == true){
                                boolean state = saveMembers.saveMembers(memberPhone.get(i), memberId.get(i), memberName.get(i), memberImage.get(i), groupIdBack.get(i), updatedDate.get(i), memberType.get(i), memberContribution.get(i));
                                if (state == false)
                                {
                                    // Toast.makeText(context,"Member Added: "+memberName.get(i),Toast.LENGTH_LONG).show();
                                }
                                // Toast.makeText(context,"Now: "+memberName.get(i)+" on "+memberContribution.get(i),Toast.LENGTH_LONG).show();
                            }else{
                                // Toast.makeText(context,"Member Not Added: "+memberName.get(i),Toast.LENGTH_LONG).show();
                            }
                        }*/
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Itike", "Yanze",ex);
            }
        }
        else
        {
            Log.e("We got ","No Event");
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    //function to return
    public ArrayList<String> returnMember()
    { return ticketCode; }

    public ArrayList<String> returnAmount()
    {
        return price;
    }

}
