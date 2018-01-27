package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RwandaFab on 10/12/2017.
 */

public class RequestHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static  final String DATABASE_NAME = "uplusRequests";
    public static  final String TABLE_NAME = "groupRequest";
    public static final String CREATE_TABLE="create table if not exists groupRequest(requestId varchar(50),groupId varchar(50),amount text,memberName varchar(200))";
    public static final String DROP_TABLE="drop table if exists "+RequestContractor.TABLE_NAME;
    //
    public RequestHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+"(requestId varchar(50),groupId varchar(50),amount text,memberName varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //create a method to save data to the databse
    public boolean saveToLocalDatabase(String requestId,String groupId,String amount,String memberName)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(RequestContractor.REQUEST_ID,requestId);
        contentValues.put(RequestContractor.GROUP_ID,groupId);
        contentValues.put(RequestContractor.AMOUNT,amount);
        contentValues.put(RequestContractor.MEMBER_NAME,memberName);
        //now add data to databse
        long result=database.insert(RequestContractor.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor getAllData(String id)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from groupRequest where groupId='"+id+"'",null);
        return cursor;
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


    public boolean checkRequest(String gid)
    {
        String query="select * from groupRequest where groupId='"+gid+"'";
        SQLiteDatabase database=this.getWritableDatabase();;
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter++;
        }
        cursor.close();
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

    public boolean cleantable(String gid)
    {
        String query="DELETE FROM groupRequest WHERE groupId='"+gid+"'";
        SQLiteDatabase database=this.getWritableDatabase();;
        //create cursor to return data
        database.execSQL(query);
//        if(cursor) {
//            return true;
//        }
//        else{
//            return false;
//        }
        return true;

    }

}
