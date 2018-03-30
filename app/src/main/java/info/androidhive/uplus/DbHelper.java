package info.androidhive.uplus;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static  final String DATABASE_NAME = "uplus";
    public static  final String TABLE_NAME = "groups";
    public static final String CREATE_TABLE="create table if not exists groups(groupId text,groupName text,targetAmount text,fetched varchar(10),groupImage text,groupBalance varchar(20))";
    public static final String DROP_TABLE="drop table if exists "+DbContract.TABLE_NAME;
    //

    ArrayList<String>groupNames=new ArrayList<String>();
    ArrayList<String>groupIds=new ArrayList<String>();
    ArrayList<String>targetAmount=new ArrayList<String>();
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public DbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+"(groupId varchar(50),groupName text,targetAmount text,fetched varchar(10),groupImage text,groupBalance varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    //create a method to save data to the databse
    public boolean saveToLocalDatabase(String groupId,String groupName,String targetAmount,String groupImage,String groupBalance)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbContract.GROUP_ID,groupId);
        contentValues.put(DbContract.GROUP_NAME,groupName);
        contentValues.put(DbContract.TARGET_AMOUNT,targetAmount);
        contentValues.put(DbContract.GROUPIMAGE,groupImage);
        contentValues.put(DbContract.GROUP_BALANCE,groupBalance);
        //now add data to databse
        long result=database.insert(DbContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // Get All groups from the offline db
    public Cursor getAllData()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from groups",null);
        return cursor;
    }
    //public to check if there is data
    public void clearTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("delete from groups");
    }
    //remove table from databse
    public boolean removeGroup(String groupId)
    {
        String query="delete from groups where groupId='"+groupId+"'";
        boolean state=true;
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
        return state;
    }
    public void showNotifications(Context context)
    {
        String query="select * from groups where fetched='no'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int count=0;
        while(cursor.moveToNext())
        {
            count++;
        }
        cursor.close();
        if(count>=0)
        {

        }

    }
    //function to drop table
    public void dropTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(DROP_TABLE);
    }
    //function to recreate the table
    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(CREATE_TABLE);
    }
    //fill the adapter
    public void fillAdapter(RecyclerView recyclerView,Context context)
    {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from groups",null);
        while(cursor.moveToNext())
        {
            groupIds.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_ID)));
            groupNames.add(cursor.getString(cursor.getColumnIndex(DbContract.GROUP_NAME)));
            targetAmount.add(cursor.getString(cursor.getColumnIndex(DbContract.TARGET_AMOUNT)));
        }
        //update their status
        cursor.close();
        //add data to the adapter
        for(int i=0;i<groupNames.size();i++)
        {
            //Log.i("GroupNames are",groupNames.get(i));
        }
//        adapter=new MyAdapter(groupNames,targetAmount,context,recyclerView,groupIds);
////        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        layoutManager=new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layoutManager);


    }

    public boolean checkGroupId(String groupId)
    {
        String query="select * from groups where groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        //check counters returned
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //check group image if not equal to null
    public boolean checkGroupImage(String groupId,  String groupName, String groupAmount, String groupImage, String groupBalance)
    {
        String query="select * from groups where groupId='"+groupId+"' AND groupName='"+groupName+"' AND targetAmount='"+groupAmount+"' AND groupImage='"+groupImage+"' AND groupBalance='"+groupBalance+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        //check counters returned
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //update groupImage
    public void updateGroupImage(String groupId,  String groupName, String groupAmount, String groupImage, String groupBalance)
    {
        String query="UPDATE groups SET groupName='"+groupName+"', targetAmount='"+groupAmount+"', groupImage='"+groupImage+"', groupBalance='"+groupBalance+"' WHERE groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
    }
    public void notifyData()
    {
        adapter.notifyDataSetChanged();
    }


    public boolean deleteGroup(String groupId)
    {
        String query="DELETE FROM groups WHERE groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
        return true;
    }
}
