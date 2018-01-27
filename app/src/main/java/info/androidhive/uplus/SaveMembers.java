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

public class SaveMembers extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=3;
    public static  final String DATABASE_NAME = "uplusmembers";
    public static  final String TABLE_NAME = "members";

    public static  final String MEMBER_PHONE        = "memberPhone";
    public static  final String MEMBER_ID           = "memberId";
    public static  final String MEMBER_NAME         = "memberName";
    public static  final String MEMBER_IMAGE        = "memberImage";
    public static  final String GROUP_ID            = "groupId";
    public static  final String UPDATED_DATE        = "updatedDate";
    public static  final String MEMBER_TYPE         = "memberType";
    public static  final String MEMBER_CONTRIBUTION = "memberContribution";
    public static  final String DROP_TABLE          = "drop table if exists "+MemberContract.TABLE_NAME;

    //gui components
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> memberContribution=new ArrayList<String>();
    ArrayList<String> memberName=new ArrayList<String>();
    ArrayList<String> memberImage=new ArrayList<String>();
    ArrayList<String> memberType=new ArrayList<String>();
    public SaveMembers(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME+"( "+MEMBER_PHONE+" TEXT, "+MEMBER_ID+" TEXT, "+MEMBER_NAME+" TEXT, "+MEMBER_IMAGE+" TEXT, "+GROUP_ID+" TEXT, "+UPDATED_DATE+" TEXT, "+MEMBER_TYPE+" TEXT, "+MEMBER_CONTRIBUTION+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void recreateTable()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        database.execSQL("create table if not exists "+TABLE_NAME+"( "+MEMBER_PHONE+" TEXT, "+MEMBER_ID+" TEXT, "+MEMBER_NAME+" TEXT, "+MEMBER_IMAGE+" TEXT, "+GROUP_ID+" TEXT, "+UPDATED_DATE+" TEXT, "+MEMBER_TYPE+" TEXT, "+MEMBER_CONTRIBUTION+" TEXT)");
    }

    //method save members offline
    public boolean saveMembers(String memberPhone, String memberId, String memberName, String memberImage, String groupId, String updatedDate, String memberType, String memberContribution)
    {
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MemberContract.MEMBER_PHONE,memberPhone);
        contentValues.put(MemberContract.MEMBER_ID,memberId);
        contentValues.put(MemberContract.MEMBER_NAME,memberName);
        contentValues.put(MemberContract.MEMBER_IMAGE,memberImage);
        contentValues.put(MemberContract.GROUP_ID,groupId);
        contentValues.put(MemberContract.UPDATED_DATE,updatedDate);
        contentValues.put(MemberContract.MEMBER_TYPE,memberType);
        contentValues.put(MemberContract.MEMBER_CONTRIBUTION,memberContribution);
        long result=database.insert(MemberContract.TABLE_NAME,null,contentValues);
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
    public Cursor getAllData(String groupId)
    {
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+GROUP_ID+"='"+groupId+"' ORDER BY "+MEMBER_CONTRIBUTION+" DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;


    }

    //function to clear data from databse
    public void clearMembers()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" ");
    }

    /*function to fill member recyclerview
    public void fillMemberRecyclerView(String id,RecyclerView recyclerView,Context context)
    {
        String query="SELECT * FROM members WHERE groupId='"+id+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext())
        {
            while(cursor.moveToNext())
            {
                memberName.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_NAME)));
                memberImage.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_IMAGE)));
                memberContribution.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_CONTRIBUTION)));
                memberType.add(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_TYPE)));
            }
            cursor.close();
        }

        //Create recyclerview adapter
        adapter= new RecyclerAdapter(memberName,memberContribution,memberType,memberImage, );
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    */

    //FUNCTION TO CHECK IF DATA IS AVAILABLE
    public boolean checkGroupId(String memberId, String groupId)
    {
        String query="SELECT * FROM members WHERE memberId='"+memberId+"' AND groupId='"+groupId+"'";
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
    public boolean checkMemberChange(Integer memberCount, String groupId)
    {
        String query="SELECT * FROM members WHERE groupId='"+groupId+"'";
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

        if(memberCount == counter){
             return true;
        }
        else
        {
            return false;
        }


    }

    //FUNCTION TO CLEAN THE GROUP
    public boolean cleanGroupBeforeCreate(String groupId)
    {
        String query="DELETE FROM members WHERE groupId='"+groupId+"'";
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

    //update member amount function
    public boolean updateMemberAmount(String memberId, String groupId)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" WHERE memberId='"+memberId+"' AND groupId='"+groupId+"'");
        return true;
    }

    //check if amount is different function
    public boolean checkAmount(String memberId,String newAmount)
    {
        Double recentAmount=0.0;
        String query="SELECT * FROM members WHERE memberId='"+memberId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            recentAmount=Double.parseDouble(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_CONTRIBUTION)));
        }
        //check if Data are Different
        if(Double.parseDouble(newAmount)==recentAmount)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //SUM UP GROUP MEMBER AMOUNT
    public String sumGroupAmount(String groupId)
    {
        String query="SELECT * FROM members WHERE groupId='"+groupId+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        Double total=0.0;
        while(cursor.moveToNext())
        {
          total+=Double.parseDouble(cursor.getString(cursor.getColumnIndex(MemberContract.MEMBER_CONTRIBUTION)));
        }

        return String.valueOf(total);
    }

    //METHOD TO DELETE DATA FROM LOCALSTORAGE
    public boolean deleteFromLocalGroup(String groupId,String memberId)
    {
        String query="DELETE FROM members WHERE groupId='"+groupId+"' AND memberId='"+memberId+"'";
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

    public boolean checkUpdatedDate(String memberId,String memberContribution)
    {
        String query="SELECT * FROM members WHERE memberId='"+memberId+"' AND memberContribution='"+memberContribution+"'";
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int counter=0;
        while(cursor.moveToNext())
        {
            counter++;
        }
        database.close();
        cursor.close();
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateUser(String memberPhone, String memberId, String memberName, String memberImage, String groupId, String updatedDate, String memberType, String memberContribution){
        String query="UPDATE members SET "+MEMBER_PHONE+"="+memberPhone+", "+MEMBER_ID+"="+memberId+", "+MEMBER_NAME+"="+memberName+", "+MEMBER_IMAGE+"="+memberImage+", "+GROUP_ID+"="+groupId+","+UPDATED_DATE+"="+updatedDate+","+MEMBER_TYPE+"="+memberType+","+MEMBER_CONTRIBUTION+"="+memberContribution+" WHERE memberId='"+memberId+"' AND groupId='"+groupId+"'";
        SQLiteDatabase database= this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        int counter = 0;
        while(cursor.moveToNext())
        {
            counter=counter+1;
        }
        cursor.close();
        if(counter>0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    //Function to delete group from localdb
}