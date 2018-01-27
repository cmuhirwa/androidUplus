package info.androidhive.uplus.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

import info.androidhive.uplus.Login;
import info.androidhive.uplus.loginPassWord;

/**
 * Created by RwandaFab on 7/17/2017.
 */

public class SignUp extends AsyncTask<String,Void,String> {
    Context context;
    String pin,userId,userName;
    ProgressDialog progress;
    ArrayList<String> data=new ArrayList<String>();
    public SignUp(Context context, ProgressDialog progress)
    {
        this.context=context;
        this.progress=progress;
    }


    @Override
    protected String doInBackground(String... params) {
        String url="http://67.205.139.137/api/index.php";
        String method=params[0];

        //check methos passed
        if(method.equals("signup"))
        {
            try {
                URL conn_url=new URL(url);
                String action="signup";
                String phoneNumber=params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"+URLEncoder.encode("phoneNumber","UTF-8")+"="+URLEncoder.encode(phoneNumber,"UTF-8");
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

                JSONArray array=new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject=array.getJSONObject(i);
                    pin= jsonObject.getString("pin");
                    userId=jsonObject.getString("userId");
                    userName=jsonObject.getString("userName");

                    if(userName == "null"){
                        userName = "";
                    }

                    Log.d("pin",pin);
                    Log.d("userId",pin);
                    Log.d("userName",pin);
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
        else if(method.equals("verify"))
        {
            try {
                URL conn_url=new URL(url);
                String action="updateProfile";
                String userId=params[1];
                String userName=params[1];
                //create httpUrlConnection
                HttpURLConnection httpURLConnection=(HttpURLConnection)conn_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //create a OutPutStream object
                OutputStream outStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferWriter=new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data= URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"+URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8")
                        +"&"+URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(userName,"UTF-8");
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

                JSONArray array=new JSONArray(result);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObject=array.getJSONObject(i);
                    pin= jsonObject.getString("pin");
                    userId=jsonObject.getString("userId");
                    userName=jsonObject.getString("userName");

                    Log.d("pin",pin);
                    Log.d("userId",pin);
                    Log.d("userName",pin);
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
    protected void onPostExecute(String result) {
        try
        {
            //keep data in shared Prefrences
            progress.dismiss();
           // Toast.makeText(context,pin,Toast.LENGTH_LONG).show();

         if(pin=="" || pin==null)
          {
               AlertDialog.Builder builder=new AlertDialog.Builder(context);
               builder.setTitle("Network Error");
               builder.setMessage("There is an error");
              builder.show();
         }
          else
          {
               SharedPreferences sharedPreferences=context.getSharedPreferences("info.androidhive.materialtabs",Context.MODE_PRIVATE);
               sharedPreferences.edit().putString("pin",pin).apply();
               sharedPreferences.edit().putString("userId",userId).apply();
               sharedPreferences.edit().putString("userName",userName).apply();
             Intent intent=new Intent(context,loginPassWord.class);
              Login.fa.finish();
              Login.fa.startActivity(intent);


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
    //function to return userid
    public String returnUserId()
    {
        return userId;
    }
    //function to return pin created
    public String returnPin()
    {
        return pin;
    }
    //function to return userName
    public String returnUserName()
    {
        return userName;
    }
}
