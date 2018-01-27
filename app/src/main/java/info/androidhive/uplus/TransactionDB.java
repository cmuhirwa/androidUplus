package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by RwandaFab on 8/10/2017.
 */

public class TransactionDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    public static final String CREATE_TABLE="create table if not exists transactions(id varchar(10),amount text,transaction_time text,status varchar(50),receiverPhone text)";
    public static final String DROP_TABLE="drop table if exists "+TransactionContract.TABLE_NAME;
    public TransactionDB(Context context)
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
    //METHOD to RECREATE TABLE
    public void RecreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(CREATE_TABLE);
    }
    //method to add data to table
    public boolean SaveTransaction(String id,String amount,String transactionTime,String status,String receiverPhone)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(TransactionContract.ID,id);
        contentValues.put(TransactionContract.TRANSACTION_AMOUNT,amount);
        contentValues.put(TransactionContract.TRANSACTION_TIME,provideCurrentDate());
        contentValues.put(TransactionContract.STATUS,status);
        contentValues.put(TransactionContract.RECEIVER,receiverPhone);
        //now add data to databse
        long result=database.insert(TransactionContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            if(status=="Pending")
            {

            }
            return true;

        }
    }
    public void clearTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("delete from transactions");
    }
    //method to retrieve data from database
    public Cursor retrieveTransactions(String receiverPhone)
    {
        SQLiteDatabase db=this.getWritableDatabase();
       String query="select * from transactions where receiverPhone='"+receiverPhone+"' order by transaction_time desc";
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    //update transaction
    public void updateTransaction(String id,String status)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="update transactions set status='"+status+"' where id='"+id+"'";
        db.execSQL(query);
    }
    public String provideCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
        String strDate = sdf.format(c.getTime());
        return  strDate;
    }
    public void dropTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(DROP_TABLE);
    }
}
