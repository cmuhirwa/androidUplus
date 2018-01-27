package info.androidhive.uplus;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by RwandaFab on 6/15/2017.
 */
public class DirectTransFer extends AsyncTask<String,Void,String> {
    Context context;
    Dialog dialog;
    AlertDialog alert;
    String result="";
    TextView returnId;
    String type;
    String transactionId,Status;
    ProgressDialog progress;
    String receiverPhone;
    String amount;
    Intent intent1;

    //submitted data
    String  senderId,senderName,senderPhone,senderBank,receiverName,receiverBank,action;

    //end of submitted data
    public static final String STATUS="NETWORK ERROR";
    public DirectTransFer(Context ctx, ProgressDialog progress, String receiverPhone, String amount, Dialog dialog,Intent intent1){
        context= ctx;
        this.progress=progress;
        this.receiverPhone=receiverPhone;
        this.amount=amount;
        this.dialog=dialog;
        this.intent1=intent1;
    }
    @Override
    protected String doInBackground(String... params) {
        type=params[0];
        String login_url="http://67.205.139.137/api/index.php";
        if(type.equals("transfer"))
        {
            try{
                 action="directtransfer";
                 amount=params[1];
                 senderId=params[2];
                 senderName=params[3];
                 senderPhone=params[4];
                 senderBank=params[5];
                 receiverName=params[6];
                 receiverPhone=params[7];
                 receiverBank=params[8];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8")+"&"
                        +URLEncoder.encode("senderId","UTF-8")+"="+URLEncoder.encode(senderId,"UTF-8")+"&"
                        +URLEncoder.encode("senderName","UTF-8")+"="+URLEncoder.encode(senderName,"UTF-8")+"&"
                        +URLEncoder.encode("senderPhone","UTF-8")+"="+URLEncoder.encode(senderPhone,"UTF-8")+"&"
                        +URLEncoder.encode("senderBank","UTF-8")+"="+URLEncoder.encode(senderBank,"UTF-8")+"&"
                        +URLEncoder.encode("receiverName","UTF-8")+"="+URLEncoder.encode(receiverName,"UTF-8")+"&"
                        +URLEncoder.encode("receiverPhone","UTF-8")+"="+URLEncoder.encode(receiverPhone,"UTF-8")+"&"
                        +URLEncoder.encode("receiverBank","UTF-8")+"="+URLEncoder.encode(receiverBank,"UTF-8");
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
                    transactionId=jsonObject.getString("transactionId");
                    Status=jsonObject.getString("status");
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
        dialog.dismiss();

        TransactionDB transactionDB=new TransactionDB(context);
        transactionDB.RecreateTable();
        boolean state=transactionDB.SaveTransaction(transactionId,amount,provideCurrentDate(),Status,receiverPhone);
        if(state==true)
        {
          Toast.makeText(context,"Transaction in Progress"+Status,Toast.LENGTH_LONG).show();
            UserProfile.fa.finish();
            UserProfile.fa.startActivity(intent1);
        }
        else
        {
            Toast.makeText(context,"System transaction Not Saved",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    public String resultReturned()
    {
        return result;
    }

    public String provideCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
        String strDate = sdf.format(c.getTime());
        return  strDate;
    }


}
