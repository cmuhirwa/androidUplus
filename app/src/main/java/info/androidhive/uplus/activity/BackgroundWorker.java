package info.androidhive.uplus.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import info.androidhive.uplus.BackgroundTask;
import info.androidhive.uplus.InviteMember;

/**
 * Created by RwandaFab on 6/15/2017.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String>
{
    Context context;
    ProgressDialog progress;
    AlertDialog alert;
    String result = "",action, type, groupName, groupImage, groupTargetType, targetAmount, perPersonType, perPerson, adminId, groupBalance = "0", groupId;
    Uri imageUri;

    public BackgroundWorker(Context ctx, ProgressDialog progress, Uri imageUri)
    {
        context = ctx;
        this.progress = progress;
        this.imageUri = imageUri;
    }

    @Override
    protected String doInBackground(String... params)
    {
        type = params[0];
        String login_url = "http://67.205.139.137/api/index.php";
        if (type.equals("register")) {
            try {
                action          = "createGroup";
                groupName       = params[1];
                groupImage      = params[2];
                groupTargetType = params[3];
                targetAmount    = params[4];
                perPersonType   = params[5];
                perPerson       = params[6];
                adminId         = params[7];
                URL url = new URL(login_url);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream = httpCon.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&"
                        + URLEncoder.encode("groupName", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&"
                        + URLEncoder.encode("groupImage", "UTF-8") + "=" + URLEncoder.encode(groupImage, "UTF-8") + "&"
                        + URLEncoder.encode("groupTargetType", "UTF-8") + "=" + URLEncoder.encode(groupTargetType, "UTF-8") + "&"
                        + URLEncoder.encode("targetAmount", "UTF-8") + "=" + URLEncoder.encode(targetAmount, "UTF-8") + "&"
                        + URLEncoder.encode("perPersonType", "UTF-8") + "=" + URLEncoder.encode(perPersonType, "UTF-8") + "&"
                        + URLEncoder.encode("perPerson", "UTF-8") + "=" + URLEncoder.encode(perPerson, "UTF-8") + "&"
                        + URLEncoder.encode("adminId", "UTF-8") + "=" + URLEncoder.encode(adminId, "UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream = httpCon.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));

                String line = "";
                while ((line = buffReader.readLine()) != null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("modify")) {
            try {
                action          = "modifyGroup";
                groupName       = params[1];
                groupImage      = params[2];
                groupTargetType = params[3];
                targetAmount    = params[4];
                perPersonType   = params[5];
                perPerson       = params[6];
                adminId         = params[7];
                URL url = new URL(login_url);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream = httpCon.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                String post_data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&"
                        + URLEncoder.encode("groupName", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&"
                        + URLEncoder.encode("groupImage", "UTF-8") + "=" + URLEncoder.encode(groupImage, "UTF-8") + "&"
                        + URLEncoder.encode("groupTargetType", "UTF-8") + "=" + URLEncoder.encode(groupTargetType, "UTF-8") + "&"
                        + URLEncoder.encode("targetAmount", "UTF-8") + "=" + URLEncoder.encode(targetAmount, "UTF-8") + "&"
                        + URLEncoder.encode("perPersonType", "UTF-8") + "=" + URLEncoder.encode(perPersonType, "UTF-8") + "&"
                        + URLEncoder.encode("perPerson", "UTF-8") + "=" + URLEncoder.encode(perPerson, "UTF-8") + "&"
                        + URLEncoder.encode("adminId", "UTF-8") + "=" + URLEncoder.encode(adminId, "UTF-8") + "&"
                        + URLEncoder.encode("adminPhone", "UTF-8") + "=" + URLEncoder.encode(adminId, "UTF-8") + "&"
                        + URLEncoder.encode("groupId", "UTF-8") + "=" + URLEncoder.encode(groupId, "UTF-8");
                bufferWriter.write(post_data);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();
                InputStream inStream = httpCon.getInputStream();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"));

                String line = "";
                while ((line = buffReader.readLine()) != null) {
                    result += line;
                }
                buffReader.close();
                inStream.close();
                httpCon.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        alert = new AlertDialog.Builder(context).create();
        alert.setTitle("Return status.......");

    }

    @Override
    protected void onPostExecute(String results)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("groupId", result).apply();
        if (result != "") {
            BackgroundTask backgroundTask = new BackgroundTask(context, progress);
            backgroundTask.execute("add", adminId);
            progress.dismiss();
            Intent intent = new Intent(context, InviteMember.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("page", "groupdetails");
            intent.putExtra("addedId", result);
            intent.putExtra("Name", groupName);
            intent.putExtra("Amount", targetAmount);
            intent.putExtra("Image", groupImage);
            intent.putExtra("groupBalance", groupBalance);
            AddGroup.fafa.finish();
            context.startActivity(intent);
        } else {
            progress.dismiss();
            Toast.makeText(context, "System Error Please try again later", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}