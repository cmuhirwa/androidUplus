package info.androidhive.uplus.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.androidhive.uplus.SaveMembers;
import info.androidhive.uplus.groupdetails;

/**
 * Created by RwandaFab on 7/17/2017.
 */

public class GetMembers extends AsyncTask<String,Void,String> {
    List<Map<String, String>> listData=new ArrayList<Map<String, String>>();
    Context context;
    ArrayList<String>memberPhone=new ArrayList<String>();
    ArrayList<String>memberId=new ArrayList<String>();
    ArrayList<String>memberName=new ArrayList<String>();
    ArrayList<String>memberImage=new ArrayList<String>();
    ArrayList<String>groupIdBack =new ArrayList<String>();
    ArrayList<String>updatedDate=new ArrayList<String>();
    ArrayList<String>memberType=new ArrayList<String>();
    ArrayList<String>memberContribution=new ArrayList<String>();
    RecyclerView recyclerView;
    Intent intent;
    String result="";
    RecyclerView.Adapter adapter;
    ProgressDialog progress;
    public GetMembers(Context context)
    {
        this.context=context;
    }


    @Override
    protected String doInBackground(String... params) {
        String url="http://67.205.139.137/api/index.php";
        String method=params[0];
        String groupId=params[1];

        Log.i("Wegot this: ", groupId);
        //check methos passed
        if(method.equals("members"))
        {
            try {
                URL conn_url=new URL(url);
                String action="listMembers";
                //String groupId=params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create an OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"+URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8");
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



                    String memberPhone1=jsonObject.getString("memberPhone");
                    String memberId1 =jsonObject.getString("memberId");
                    String memberName1=jsonObject.getString("memberName");
                    String memberImage1=jsonObject.getString("memberImage");
                    String groupId1 =jsonObject.getString("groupId");
                    String updatedDate1 =jsonObject.getString("updatedDate");
                    String memberType1 =jsonObject.getString("memberType");
                    String memberContribution1 =jsonObject.getString("memberContribution");

                    memberPhone.add(memberPhone1);
                    memberId.add(memberId1);
                    memberName.add(memberName1);
                    memberImage.add(memberImage1);
                    groupIdBack.add(groupId1);
                    updatedDate.add(updatedDate1);
                    memberType.add(memberType1);
                    memberContribution.add(memberContribution1);

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
        if(memberName.size()>=1) {
            try
            {
                Log.i("We got members (", result+")");
                Integer memberCount = memberName.size();
                SaveMembers saveMembers = new SaveMembers(context);
                if (saveMembers.checkMemberChange(memberCount, groupIdBack.get(0)) == false) {
                    saveMembers.cleanGroupBeforeCreate(groupIdBack.get(0));
                   // Toast.makeText(context,"Table Cleaned for:"+groupIdBack.get(0),Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < memberName.size(); i++) {
                    String DatatoBeSave = "name:" + memberName.get(i) + "\n groupid: " + groupIdBack.get(i);
                    //Toast.makeText(context,"D2 data to be saved: "+DatatoBeSave,Toast.LENGTH_LONG).show();
                    if (saveMembers.checkGroupId(memberId.get(i), groupIdBack.get(i)) == false) {
                        boolean state = saveMembers.saveMembers(memberPhone.get(i), memberId.get(i), memberName.get(i), memberImage.get(i), groupIdBack.get(i), updatedDate.get(i), memberType.get(i), memberContribution.get(i));
                        if (state == false) {
                            Log.i("D3 MemberData Added:", memberName.get(i));
                           // Toast.makeText(context,"Member Added: "+memberName.get(i),Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("D3 MemberData Not:", memberName.get(i));
                           // Toast.makeText(context,"Member Not Added: "+memberName.get(i),Toast.LENGTH_LONG).show();
                            //data added
                        }
                    }
                    else
                    {
                        Log.i("MemberData Updateding:", memberName.get(i));
                        //Toast.makeText(context, "Going to updated: " + memberName.get(i)+" of "+updatedDate.get(i), Toast.LENGTH_LONG).show();


                        boolean saveState = saveMembers.checkUpdatedDate(memberId.get(i), memberContribution.get(i));
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
                        }
                    }

                }
            } catch (Exception ex) {
               // Toast.makeText(context, "D4 Wapi Wapi Wapi ", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
                Log.e("Ikimina", "Cyanze",ex);

            }
           // Toast.makeText(context,"D5 finished the loop of adding data", Toast.LENGTH_LONG).show();

        }
        else
        {
            //Toast.makeText(context,"No member to loop",Toast.LENGTH_LONG).show();
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
    { return memberName; }
    public ArrayList<String> returnAmount()
    {
        return memberContribution;
    }
    //METHOD TO refresh Data
    public void refreshGUI()
    {
        groupdetails groupdetails=new groupdetails();
        groupdetails.recreate();
    }
}
