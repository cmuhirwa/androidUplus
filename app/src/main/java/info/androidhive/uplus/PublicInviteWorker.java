package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import info.androidhive.uplus.activity.HomeActivity;

/**
 * Created by user on 28/12/2017.
 */

public class PublicInviteWorker extends AsyncTask<String,Void,String> {
    Context context;
    ArrayList<String> memberImage=new ArrayList<String>();
    ArrayList<String> memberName=new ArrayList<String>();

    AlertDialog alert;
    String result="", members="";
    ProgressDialog progress;
    String groupId, targetAmount, groupBalance, groupName, groupImage, groupIdBack, adminName;
    public PublicInviteWorker(Context context,ProgressDialog progress,String publicGid)
    {
        this.context=context;
        this.progress=progress;
        this.groupId=publicGid;
    }
    @Override
    protected String doInBackground(String... params) {
        String api_url="http://67.205.139.137/api/index.php";
        String type=params[0];
        if(type.equals("publicInvite")){
            try{
                String action="publicInvite";
                String groupId=params[1];
                URL url=new URL(api_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
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
                    groupIdBack           = jsonObject.getString("groupId");
                    groupImage            = jsonObject.getString("groupImage");
                    groupName             = jsonObject.getString("groupName");
                    targetAmount          = jsonObject.getString("targetAmount");
                    adminName             = jsonObject.getString("adminName");
                    groupBalance          = jsonObject.getString("groupBalance");
                    JSONArray members     = jsonObject.getJSONArray("members");
                    for(int k=0;k<members.length();k++)
                    {
                        JSONObject jsonMembers  = members.getJSONObject(k);
                        String memberName1      = jsonMembers.getString("memberName");
                        String memberImage1     = jsonMembers.getString("memberImage");

                        memberName.add(memberName1);
                        memberImage.add(memberImage1);

                    }
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
        //
        //Toast.makeText(context,"Result Back",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context,  memberName.size(),Toast.LENGTH_LONG).show();
        //Log.e("BackShit: ", "Members are: "+memberName.get(0)+" And "+memberName.get(3));
        if(result!=""){
            Log.e("BackShit: ", result+" of "+groupId);
            Intent intent   =new Intent(context,Invitation.class);
            intent.putExtra("groupId",groupIdBack);
            intent.putExtra("groupImage",groupImage);
            intent.putExtra("groupName",groupName);
            intent.putExtra("targetAmount",targetAmount);
            intent.putExtra("adminName",adminName);
            intent.putExtra("groupBalance",groupBalance);
            intent.putExtra("array_list", memberImage);
            progress.dismiss();
            HomeActivity.homepage.finish();
            HomeActivity.homepage.startActivity(intent);
        }else
        {
            Log.e("BackShit: ", "nothing");
        }
        if(result!=""){

            }
            else{

            }
            //Toast.makeText(context,saveStatus,Toast.LENGTH_LONG).show();
        }
}
