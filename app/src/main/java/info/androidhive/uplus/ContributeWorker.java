package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;

/**
 * Created by RwandaFab on 7/30/2017.
 */

public class ContributeWorker extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alert;
    String result="" ,transaction="", status="", groupName,groupImage, Amount, groupBalance;
    private static int a;
    private static final String STATUS_APPROVED="APPROVED";
    private static final String STATUS_COMPLETE="COMPLETE";
    private static final String STATUS_PENDING="PENDING";
    ProgressDialog progress;
    String groupId;
    public ContributeWorker(Context context,ProgressDialog progress,String groupId,String groupName,String groupImage,String Amount, String groupBalance)
    {
        this.context=context;
        this.progress=progress;
        this.groupId=groupId;
        this.Amount=Amount;
        this.groupImage=groupImage;
        this.groupName=groupName;
        this.groupBalance = groupBalance;
    }
    @Override
    protected String doInBackground(String... params) {
        String api_url="http://67.205.139.137/api/index.php";
        String type=params[0];
        if(type.equals("contribute")){
            try{
                String action       ="contribute";
                String memberId     =params[1];
                String groupId      =params[2];
                String amount       =params[3];
                String fromPhone    =params[4];
                String bankId       =params[5];

                URL url=new URL(api_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("memberId","UTF-8")+"="+URLEncoder.encode(memberId,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8")+"&"
                        +URLEncoder.encode("pushnumber","UTF-8")+"="+URLEncoder.encode(fromPhone,"UTF-8")+"&"
                        +URLEncoder.encode("senderBank","UTF-8")+"="+URLEncoder.encode(bankId,"UTF-8");
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
                    transaction=jsonObject.getString("transactionId");
                    status=jsonObject.getString("status");
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
        alert=new AlertDialog.Builder(context).create();
        alert.setTitle("Return status.......");
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Result returned");
        dialog.setMessage(result);
        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
        if(result!=""){
            ContributeHelper dbHelper=new ContributeHelper(context);
            dbHelper.recreateTable();
            boolean state=dbHelper.saveToLocalDatabase(transaction,status,provideCurrentDate());
            String saveStatus="";
            if(state==true){
                Intent intent=new Intent(context,groupdetails.class);
                intent.putExtra("Id",groupId);
                intent.putExtra("Name",groupName);
                intent.putExtra("Amount",Amount);
                intent.putExtra("Image",groupImage);
                intent.putExtra("groupBalance",groupBalance);
                progress.dismiss();
                groupdetails.ffaa.finish();
                groupdetails.ffaa.startActivity(intent);
                saveStatus="Successfully contribute transaction saved";
            }
            else{
                saveStatus="Not saved locally";
            }
            //Toast.makeText(context,saveStatus,Toast.LENGTH_LONG).show();
        }
    }

    public String provideCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
        String strDate = sdf.format(c.getTime());
        return  strDate;
    }

}
