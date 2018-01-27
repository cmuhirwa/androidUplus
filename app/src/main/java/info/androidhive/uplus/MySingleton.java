package info.androidhive.uplus;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by RwandaFab on 7/12/2017.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context context2){
        context=context2;
        requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getInstance(Context conx){
        if(mInstance==null){
            mInstance=new MySingleton(conx);

        }
        return mInstance;
    }
    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}
