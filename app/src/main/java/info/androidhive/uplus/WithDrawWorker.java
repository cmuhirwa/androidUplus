package info.androidhive.uplus;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by RwandaFab on 6/15/2017.
 */
public class WithDrawWorker extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alert;
    String result="";
    TextView returnId;
    String type,groupId,backGroupName,backGroupAmount,backGroupImage,backGroupBalance;
    ProgressDialog progressDialog;
    public WithDrawWorker(Context ctx,ProgressDialog progressDialog){
        context=ctx;
        this.progressDialog=progressDialog;
    }
    @Override
    protected String doInBackground(String... params) {
        type                =params[0];
        backGroupName       =params[6];
        backGroupAmount     =params[7];
        backGroupImage      =params[8];
        backGroupBalance    =params[9];

        String login_url="http://67.205.139.137/api/index.php";

        String register_url="http://192.168.177.205/android/register.php";
        String data_url="http://192.168.177.205/android/data.php";
        if(type.equals("withdrawrequest"))
        {
            try{
                String action="withdrawrequest";
                groupId  =params[1];
                String amount   =params[2];
                String memberId=params[3];
                String withdrawAccount=params[4];
                String withdrawBank=params[5];

                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data=URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8")+"&"
                        +URLEncoder.encode("memberId","UTF-8")+"="+URLEncoder.encode(memberId,"UTF-8")+"&"
                        +URLEncoder.encode("withdrawAccount","UTF-8")+"="+URLEncoder.encode(withdrawAccount,"UTF-8")+"&"
                        +URLEncoder.encode("withdrawBank","UTF-8")+"="+URLEncoder.encode(withdrawBank,"UTF-8");
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
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        WithDrawListWorker withDrawListWorker = new WithDrawListWorker(context);
        withDrawListWorker.execute("withdrawlist",groupId,backGroupName,backGroupAmount,backGroupImage,backGroupBalance);
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
    }
    //function to return the id
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
