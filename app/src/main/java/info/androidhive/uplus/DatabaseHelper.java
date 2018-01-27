package info.androidhive.uplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by user on 31/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "uplustransactions";
    public static final String TABLE_NAME = "transactions";
    public static final String COL_1 = "amount";
    public static final String COL_2 = "userName";
    public static final String COL_3 = "phone";
    public static final String COL_4 = "status";
    public static final String COL_5 = "transactionDate";
    public static final String COL_6 = "transactionColor";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_1+" TEXT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT,"+COL_6+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData (String amount,String names,String phone,String status,String tdate, String tcolor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,amount);
        contentValues.put(COL_2,names);
        contentValues.put(COL_3,phone);
        contentValues.put(COL_4,status);
        contentValues.put(COL_5,tdate);
        contentValues.put(COL_6,tcolor);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor cleanData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        return null;
    }

    public List<TransationsList> getdata(){
        // DataModel dataModel = new DataModel();
        List<TransationsList> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();
        TransationsList dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new TransationsList();
            String amount           = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
            String userName         = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
            String phone            = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String status           = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String transactionDate  = cursor.getString(cursor.getColumnIndexOrThrow("transactionDate"));
            String transactionColor = cursor.getString(cursor.getColumnIndexOrThrow("transactionColor"));
            dataModel.setAmount(amount);
            dataModel.setUserName(userName);
            dataModel.setPhone(phone);
            dataModel.setStatus(status);
            dataModel.setTransactionDate(transactionDate);
            dataModel.setTransactionColor(transactionColor);
            stringBuffer.append(dataModel);
            // stringBuffer.append(dataModel);
            data.add(dataModel);
        }

        for (TransationsList mo:data ) {

            Log.i("Hellomo",""+mo.getTransactionColor());
        }

        //

        return data;
    }
}