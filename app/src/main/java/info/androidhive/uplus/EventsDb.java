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

public class EventsDb extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=2;
    public static  final String DATABASE_NAME = "uplusEvents";
    public static  final String TABLE_NAME = "events";
    public static  final String CREATE_TABLE="create table if not exists "+TABLE_NAME+"(eventId varchar(100),eventName text,eventDesc text,eventCover text,eventLocation text, eventContact text, eventStart text, eventEnd text, eventTicketStatus text, eventTicketCode text)";
    public static  final String DROP_TABLE="drop table if exists "+EventsContract.TABLE_NAME;
    public EventsDb(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+"(eventId varchar(100),eventName text,eventDesc text,eventCover text,eventLocation text, eventContact text, eventStart text, eventEnd text, eventTicketStatus text, eventTicketCode text)");
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
    public boolean SaveEvents(String eventId,String eventName,String eventDesc,String eventCover,String eventLocation, String eventContact, String eventStart, String eventEnd, String eventTicketStatus, String eventTicketCode)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(EventsContract.EVENT_ID,eventId);
        contentValues.put(EventsContract.EVENT_NAME,eventName);
        contentValues.put(EventsContract.EVENT_DESC,eventDesc);
        contentValues.put(EventsContract.EVENT_COVER,eventCover);
        contentValues.put(EventsContract.EVENT_LOCATION,eventLocation);
        contentValues.put(EventsContract.EVENT_CONTACT,eventContact);
        contentValues.put(EventsContract.EVENT_START,eventStart);
        contentValues.put(EventsContract.EVENT_END,eventEnd);
        contentValues.put(EventsContract.EVENTTICKETSTATUS,eventTicketStatus);
        contentValues.put(EventsContract.EVENTTICKETCODE,eventTicketCode);
        //now add data to databse
        long result=database.insert(EventsContract.TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void clearTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("delete from events");
    }

    //method to retrieve data from database
    public Cursor getAllEvents()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from events",null);
        return cursor;
    }

    public void dropTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(DROP_TABLE);
    }

    //FUNCTION TO CHECK IF DATA IS AVAILABLE
    public boolean checkEventExists(String eventId)
    {
        String query="SELECT * FROM events WHERE eventId='"+eventId+"'";
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

    //TELL ME THAT I PURCHESSED
    public boolean updateEventTicket(String ticketEventId,String eventTicketCode)
    {
        String query="UPDATE events SET eventTicketCode='"+eventTicketCode+"' WHERE eventId='"+ticketEventId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL(query);
        return true;
    }
}
