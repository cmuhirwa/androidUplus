package info.androidhive.uplus;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
 * Created by RwandaFab on 8/20/2017.
 */

public class checkTransferStatus extends AsyncTask<String,Void,String > {
    String result="";
    String type;
    String transactionId;
    String transaction="";
    String status="";
    Context context;
    Intent intent1;
    public checkTransferStatus(Context context,Intent intent1)
    {
        this.intent1=intent1;
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        type=params[0];
        String api_url="http://67.205.139.137/api/index.php";
        if(type.equals("resend"))
        {
            try{
                String action="checktransferstatus";
                        transactionId=params[1];
                URL url=new URL(api_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("transactionId","UTF-8")+"="+URLEncoder.encode(transactionId,"UTF-8");
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
    protected void onPostExecute(String sresult) {
        if(isNetworkAvailable())
        {
               // Toast.makeText(context,status,Toast.LENGTH_SHORT).show();
                TransactionDB transactionDB=new TransactionDB(context);
                if(status.equals("Pending"))
                {
                    //Toast.makeText(context,"agian",Toast.LENGTH_SHORT).show();
                    transactionDB.updateTransaction(transaction,status);
                    checkTransferStatus checkTransferStatus=new checkTransferStatus(context,intent1);
                    checkTransferStatus.execute("resend",transactionId);
                }
                else
                {
                    transactionDB.updateTransaction(transaction,status);
                    UserProfile.fa.finish();
                    UserProfile.fa.startActivity(intent1);
                }
                result="";
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
