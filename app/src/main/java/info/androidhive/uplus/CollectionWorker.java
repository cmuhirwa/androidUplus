package info.androidhive.uplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
 * Created by RwandaFab on 8/19/2017.
 */

public class CollectionWorker extends AsyncTask<String,Void,String> {
    String type;
    String login_url="http://67.205.139.137/api/index.php";
    String result="";
    Context context;
    ProgressDialog progress;
    public CollectionWorker(Context context,ProgressDialog progress)
    {
        this.context=context;
        this.progress=progress;
    }
    @Override
    protected String doInBackground(String... params) {
        type=params[0];
        if(type.equals("collection"))
        {
            try{
                String action="createcollection";
                String groupId=params[1];
                String accountNumber=params[2];
                String bankId=params[3];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream=httpCon.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("accountNumber","UTF-8")+"="+URLEncoder.encode(accountNumber,"UTF-8")+"&"
                        +URLEncoder.encode("bankId","UTF-8")+"="+URLEncoder.encode(bankId,"UTF-8");
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
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        if(result!="" && result!=null)
        {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(context,InviteMember.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "We 've Encountered an Error Please try again later", Toast.LENGTH_LONG).show();
        }

    }
}
