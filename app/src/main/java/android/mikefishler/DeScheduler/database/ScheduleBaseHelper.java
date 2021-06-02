package android.mikefishler.DeScheduler.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "scheduleBase.db";

    //News up a new object
    public ScheduleBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    //Creates database if not created already
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ScheduleDbSchema.ScheduleTable.NAME + "(" + "_id integer primary key autoincrement, "
        + ScheduleDbSchema.ScheduleTable.Cols.UUID + ", " + ScheduleDbSchema.ScheduleTable.Cols.TITLE + ", " +
                ScheduleDbSchema.ScheduleTable.Cols.DETAILS + ", " + ScheduleDbSchema.ScheduleTable.Cols.COURSES + ")");
    }

    //Upgrades database if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
