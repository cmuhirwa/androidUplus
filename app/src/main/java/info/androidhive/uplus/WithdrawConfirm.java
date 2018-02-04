package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONException;

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

/**
 * Created by user on 30/11/2017.
 */


public class WithdrawConfirm extends AsyncTask<String,Void,String> {
    ProgressDialog progress;
    Context ctx;
    String url = "http://67.205.139.137/api/index.php";int counter = 0;
    String result = "";

    public WithdrawConfirm(Context ctx, ProgressDialog progress) {
        this.ctx = ctx;
        this.progress = progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //get method passed

        String method = params[0];
        //check method passed
        if (method.equals("confirm")) {
            try {
                URL conn_url = new URL(url);
                String action       = "withdrawvote";
                String requestId    = params[1];
                String groupId      = params[2];
                String treasurerId  = params[3];
                String vote         = params[4];

                //create httpUrlConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&"
                                + URLEncoder.encode("requestId", "UTF-8") + "=" + URLEncoder.encode(requestId, "UTF-8") + "&"
                                + URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8") + "&"
                                + URLEncoder.encode("treasurerId", "UTF-8") + "=" + URLEncoder.encode(treasurerId, "UTF-8") + "&"
                                + URLEncoder.encode("vote", "UTF-8") + "=" + URLEncoder.encode(vote, "UTF-8") + "&" ;
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpURLConnection.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));

                String line="";
                while((line= buffReader.readLine())!=null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpURLConnection.disconnect();
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
    protected void onProgressUpdate(Void... values) {
        //super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }

}