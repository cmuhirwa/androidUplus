package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class PersonalInfo extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String CREATE_TABLE="create table personal(personId text,LoggedIn text)";
    private static final String DROP_TABLE="drop table if exists personal";
    private static final String TABLE="personal";
    public PersonalInfo(Context context)
    {
        super(context,DbContract.DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    //method to save loggedin status
    public boolean saveLoggedIn(String status)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("personId","0");
        contentValues.put("LoggedIn",status);
        long result=database.insert(TABLE,null,contentValues);
        if(result==-1)
        {
           return false;
        }
        else
        {
            return true;
        }
    }
}
