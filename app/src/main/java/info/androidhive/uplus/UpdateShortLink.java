package info.androidhive.uplus;
    import android.content.Context;
    import android.os.AsyncTask;
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
 * Created by RwandaFab on 8/16/2017.
 */

public class UpdateShortLink extends AsyncTask<String,Void,String> {
    String result="";
    Context context;
    String groupId = "";
    public UpdateShortLink(Context context)
    {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String type=params[0];
        String login_url="http://67.205.139.137/api/index.php";
        if(type.equals("updateGroupShortLink"))
        {
            try{
                String action="updateGroupShortLink";
                groupId=params[1];
                String shortLink=params[2];
                URL url=new URL(login_url);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                //set POST as request method
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStream = httpCon.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
                String post_data = URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8")+"&"
                        +URLEncoder.encode("groupId","UTF-8")+"="+URLEncoder.encode(groupId,"UTF-8")+"&"
                        +URLEncoder.encode("shortLink","UTF-8")+"="+URLEncoder.encode(shortLink,"UTF-8");
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
    protected void onPostExecute(String result) {
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
    }
}

