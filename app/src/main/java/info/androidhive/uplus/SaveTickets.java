package info.androidhive.uplus;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.widget.Toast;

        import java.util.ArrayList;

        import info.androidhive.uplus.activity.RecyclerAdapter;

/**
 * Created by RwandaFab on 7/29/2017.
 */

public class SaveTickets extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=1;
    public static  final String DATABASE_NAME = "uplustickets";
    public static  final String TABLE_NAME = "tickets";

    public static  final String EVENT_ID            = "eventId";
    public static  final String TICKET_CODE         = "ticketCode";
    public static  final String PRICE               = "price";
    public static  final String TICKETS_NAME        = "ticketsName";
    public static  final String DROP_TABLE          = "drop table if exists "+TicketContract.TABLE_NAME;

    //gui components
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public SaveTickets(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+"("+EVENT_ID+" TEXT, "+TICKET_CODE+" TEXT, "+TICKETS_NAME+" TEXT, "+PRICE+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("create table if not exists "+TABLE_NAME+"("+EVENT_ID+" TEXT, "+TICKET_CODE+" TEXT, "+TICKETS_NAME+" TEXT, "+PRICE+" TEXT)");
    }

    //method save members offline
    public boolean saveTickets(String eventId, String ticketCode, String price, String ticketsName)
    {
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(TicketContract.EVENT_ID,eventId);
        contentValues.put(TicketContract.TICKET_CODE,ticketCode);
        contentValues.put(TicketContract.PRICE,price);
        contentValues.put(TicketContract.TICKETS_NAME,ticketsName);
        long result=database.insert(TicketContract.TABLE_NAME,null,contentValues);
        database.close();
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //method to get Data from Database offline
    public Cursor getAllData(String eventId)
    {
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+EVENT_ID+"='"+eventId+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    //function to clear data from databse
    public void clearTickets()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" ");
    }



    //FUNCTION TO CHECK IF DATA IS AVAILABLE
    public boolean checkTicketId(String eventId, String ticketId)
    {
        String query="SELECT * FROM "+TABLE_NAME+" WHERE eventId='"+eventId+"' AND ticketCode='"+ticketId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);

        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        database.close();
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

    //FUNCTION TO CHECK IF MEMBERS LEFT THE GROUP
    public boolean checkTicketChange(Integer ticketCount, String eventId)
    {
        String query="SELECT * FROM "+TABLE_NAME+" WHERE eventId='"+eventId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        //create cursor to return data
        Cursor cursor=database.rawQuery(query,null);

        //check counters returned
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        database.close();
        cursor.close();

        if(ticketCount == counter){
            return true;
        }
        else
        {
            return false;
        }


    }

    //FUNCTION TO CLEAN THE GROUP
    public boolean cleanTicketBeforeCreate(String eventId)
    {
        String query="DELETE FROM "+TABLE_NAME+" WHERE eventId='"+eventId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //METHOD TO DELETE DATA FROM LOCALSTORAGE
    public boolean deleteFromLocalEvent(String eventId,String ticketId)
    {
        String query="DELETE FROM "+TABLE_NAME+" WHERE eventId='"+eventId+"' AND ticketCode='"+ticketId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void dropMember()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_TABLE);
    }

    //Function to delete group from localdb
}