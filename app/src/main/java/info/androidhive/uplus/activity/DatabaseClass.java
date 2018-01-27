package info.androidhive.uplus.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by RwandaFab on 7/16/2017.
 */

public class DatabaseClass {
    Context context;
    public DatabaseClass(Context context)
    {
        this.context=context;
    }
    public void addData(int groupId,String groupName,String groupTargetType,String perPersonType,int targetAmount,int perPerson,int adminId,String adminName,String syncstatus)
    {
        try
        {

            SQLiteDatabase myDb= context.openOrCreateDatabase("extracted_db",MODE_PRIVATE,null);

            //now add data to the database
            //prepare sql statememnt
            String query="INSERT INTO groups(id,groupName,adminId,adminName,groupTargetType,targetAmount,perPersonType,perPerson,syncstatus)VALUES('"+groupId+"','"+groupName+"','"+adminId+"','"+adminName+"','"+groupTargetType+"','"+targetAmount+"','"+perPersonType+"','"+perPerson+"','"+syncstatus+"')";
            //execute query
            myDb.execSQL(query);
            Log.i("Status","Data Inserted");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
