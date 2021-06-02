//Created by: Jesse and Mike

package android.mikefishler.DeScheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.mikefishler.DeScheduler.database.ScheduleDbSchema;
import android.mikefishler.DeScheduler.database.CourseListConverter;
import android.mikefishler.DeScheduler.database.ScheduleBaseHelper;
import android.mikefishler.DeScheduler.database.ScheduleCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleLab {
    private static ScheduleLab sScheduleLab; //static object of class

    private List<Schedule> mSchedules; //list of all schedules

    private Context mContext; //context of calling activity
    private SQLiteDatabase mDatabase; //database being read from

    //add schedule to database
    public void addSchedule(Schedule s){

        ContentValues values = getContentValues(s);
        mDatabase.insert(ScheduleDbSchema.ScheduleTable.NAME, null, values);
    }

    //delete schedule from database
    public void deleteSchedule(Schedule s){

        mDatabase.delete(ScheduleDbSchema.ScheduleTable.NAME, "uuid=?", new String[]{String.valueOf(s.getId())});
        sScheduleLab = null;
        ScheduleLab.get(mContext);
    }


//returns instance of ScheduleLab
    public static ScheduleLab get(Context context) {
        if(sScheduleLab == null) {
            sScheduleLab = new ScheduleLab(context);
        }
        return sScheduleLab;
    }

    //Initializes context and database objects
    private ScheduleLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ScheduleBaseHelper(mContext).getWritableDatabase();
    }


    //returns list of schedules from database
    public List<Schedule> getSchedules(){


        List<Schedule> schedules = new ArrayList<>();

        ScheduleCursorWrapper cursor = querySchedules(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                schedules.add(cursor.getSchedule(mContext));
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }

        return schedules;
    }

    //get schedule by id
    public Schedule getSchedule(UUID id){

        ScheduleCursorWrapper cursor = querySchedules(ScheduleDbSchema.ScheduleTable.Cols.UUID + " = ?", new String[] {id.toString()});

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSchedule(mContext);
        }
        finally{
            cursor.close();
        }
    }

    //Used to write to the database
    private static ContentValues getContentValues(Schedule schedule){
        ContentValues values = new ContentValues();
        values.put(ScheduleDbSchema.ScheduleTable.Cols.UUID, schedule.getId().toString());
        values.put(ScheduleDbSchema.ScheduleTable.Cols.TITLE, schedule.getTitle());
        values.put(ScheduleDbSchema.ScheduleTable.Cols.DETAILS, schedule.getScheduleDetails());

        values.put(ScheduleDbSchema.ScheduleTable.Cols.COURSES, CourseListConverter.ListToString(schedule));

        return values;
    }

    //Updates a schedule in the database
    public void updateSchedule(Schedule schedule){
        String uuidString = schedule.getId().toString();
        ContentValues values = getContentValues(schedule);

        mDatabase.update(ScheduleDbSchema.ScheduleTable.NAME, values, ScheduleDbSchema.ScheduleTable.Cols.UUID + " =?", new String[] {uuidString
        });
    }

    //Used to read a schedule from the database
    private ScheduleCursorWrapper querySchedules(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(ScheduleDbSchema.ScheduleTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new ScheduleCursorWrapper(cursor);
    }


}
