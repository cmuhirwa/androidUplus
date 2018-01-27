package info.androidhive.uplus;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import info.androidhive.uplus.activity.HomeActivity;

/**
 * Created by RwandaFab on 6/15/2017.
 */
public class ExitGroup extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alert;
    String result="";
    String type;
    String groupId;
    ProgressDialog progress;
    public ExitGroup(Context ctx,ProgressDialog progress){
        context=ctx;
        this.progress=progress;
    }
    @Override
    protected String doInBackground(String... params) {

        type=params[0];
        String login_url="http://67.205.139.137/api/index.php";
        if(type.equals("exit"))
        {
            try{
                String action="exitGroup";
                groupId=params[1];
                String memberId=params[2];
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
                        +URLEncoder.encode("memberId","UTF-8")+"="+URLEncoder.encode(memberId,"UTF-8");
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




        //delete members
        SaveMembers saveMembers = new SaveMembers(context);
        saveMembers.cleanGroupBeforeCreate(groupId);

        // delete group

        DbHelper dbHelper=new DbHelper(context);
        dbHelper.deleteGroup(groupId);

        deleteCache(context);
        progress.dismiss();

        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        Intent i = new Intent().setClass(context, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        HomeActivity.homepage.finish();
        groupdetails.ffaa.finish();
        groupdetails.ffaa.startActivity(i);
    }
    //function to return the id
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    //function to return result returned
    public String resultReturned()
    {
        return result;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
