package info.androidhive.uplus.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

import info.androidhive.uplus.Login;


public class UpdateProfile extends AsyncTask<String,Void,String> {
    Context context;
    String pin,userId,userName,userImage;
    ProgressDialog progress;
    SharedPrefManager sharedPrefManager;

    public UpdateProfile(Context context,ProgressDialog progress)
    {

        this.context=context;
        this.progress=progress;
    }


    @Override
    protected String doInBackground(String... params) {
        String url="http://67.205.139.137/api/index.php";
        String method=params[0];

        //check method passed
        if(method.equals("verify"))
        {
            try
            {
                URL conn_url=new URL(url);
                String action="updateProfile";
                    userId=params[1];
                    userName=params[2];
                    userImage=params[3];
                //Log.i("username",userName);
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")
                        +"&"+URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8")
                        +"&"+URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(userName,"UTF-8")
                        +"&"+URLEncoder.encode("userImage","UTF-8")+"="+URLEncoder.encode(userImage,"UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream=httpURLConnection.getInputStream();
                BufferedReader buffReader=new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"));
                StringBuffer buffer=new StringBuffer();
                String result="";
                String line="";
                while((line= buffReader.readLine())!=null){
                    result+=line;
                }
                buffReader.close();
                inStream.close();
                httpURLConnection.disconnect();
               // Log.i("result",result);
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        try
        {
            //check the reuslt
            //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            if(result!=null && result!="")
            {
                SharedPreferences sharedPreferences=context.getSharedPreferences("info.androidhive.materialtabs",Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("userImage",result).apply();
                //Intent intent=new Intent(context,HomeActivity.class);
                //intent.putExtra("Load","1");
                //context.startActivity(intent);
                //progress.dismiss();
                Toast.makeText(context, "Profile Created",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Image not uploaded",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {

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
}
