package android.mikefishler.DeScheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.mikefishler.DeScheduler.Schedule;

import java.util.UUID;

public class ScheduleCursorWrapper extends CursorWrapper {
    //Used to pull information from the schedule database
    public ScheduleCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //Reads in a schedule from the database
    public Schedule getSchedule(Context context){
        String uuidString = getString(getColumnIndex(ScheduleDbSchema.ScheduleTable.Cols.UUID));
        String title = getString(getColumnIndex(ScheduleDbSchema.ScheduleTable.Cols.TITLE));
        String details = getString(getColumnIndex(ScheduleDbSchema.ScheduleTable.Cols.DETAILS));
        String courses = getString(getColumnIndex(ScheduleDbSchema.ScheduleTable.Cols.COURSES));

        Schedule schedule = new Schedule(UUID.fromString(uuidString));
        schedule.setTitle(title);
        schedule.setScheduleDetails(details);
        schedule.setCourses(CourseListConverter.StringToList(courses, context));

        return schedule;
    }
}
