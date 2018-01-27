package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class SaveGroupLocal extends SQLiteOpenHelper {
    public static final String CREATE_GROUP_DATA="create table if not exists groupinfo(id varchar(100),groupName text,targetType varchar(50),targetAmount varchar(100),perPersonType varchar(50),perPerson text,adminId varchar(10),collectionType varchar(10),collectionAccount text,status varchar(10))";
    public static final String DROP_GROUP_INFO="drop table if exists groupinfo";
    public static final String GROUP_NAME="groupinfo";
    private static final int DATABASE_VERSION=1;
    public SaveGroupLocal(Context context)
    {
        super(context,DbContract.DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_GROUP_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_GROUP_INFO);
        onCreate(db);
    }
    //function to add group to sqlite datbase
    public boolean addGroupLocal(String id,String groupName,String targetType,String targetAmount,String perPersonType,String perPerson,String adminId,String collectionType,String collectionAccount,String status)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(SaveGroupContract.ID,id);
        contentValues.put(SaveGroupContract.GROUP_NAME,groupName);
        contentValues.put(SaveGroupContract.TARGET_TYPE,targetType);
        contentValues.put(SaveGroupContract.TARGET_AMOUNT,targetAmount);
        contentValues.put(SaveGroupContract.PER_PERSON_TYPE,perPersonType);
        contentValues.put(SaveGroupContract.PER_PERSON,perPerson);
        contentValues.put(SaveGroupContract.ADMIN_ID,adminId);
        contentValues.put(SaveGroupContract.COLLECTION_TYPE,collectionType);
        contentValues.put(SaveGroupContract.COLLECTION_ACCOUNT,collectionAccount);
        contentValues.put(SaveGroupContract.SYNC_STATUS,status);
        //save data to database
        long result=database.insert(SaveGroupContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    //function to update sync status locally
    public void updateSyncStatus(String sync_value,String groupId)
    {
        String query="UPDATE groupinfo SET status='"+sync_value+"' WHERE id='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
    }

    public boolean updateGroupTable(String sync_value,String groupId)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(SaveGroupContract.SYNC_STATUS,sync_value);
        String selection=SaveGroupContract.ID+" = ?";
        String[] selection_args={groupId};

        long result=database.update(SaveGroupContract.TABLE_NAME,contentValues,selection,selection_args);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    //function recreate table
    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(CREATE_GROUP_DATA);
    }
    //function to drop table manually
    public void dropManually()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(DROP_GROUP_INFO);
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from groupinfo",null);
        return cursor;
    }
    //function to return current id
    public int returnCurrentId()
    {
        int result=0;
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=getAllData();
        while(cursor.moveToNext())
        {
            result=result+1;
        }
        return result;
    }
}
