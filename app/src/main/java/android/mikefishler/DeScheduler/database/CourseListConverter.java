package android.mikefishler.DeScheduler.database;

import android.content.Context;
import android.mikefishler.DeScheduler.Course;
import android.mikefishler.DeScheduler.CourseLoader;
import android.mikefishler.DeScheduler.Schedule;

import java.util.ArrayList;

public class CourseListConverter {

    //Used to save courses to schedule database and read courses from database


    //Converts a course list to a string object
    public static String ListToString(Schedule mSchedule){

        ArrayList<Course> courses = mSchedule.getCourses();

        String s = "";
        for(int i=0; i< courses.size(); i++){
            s+= courses.get(i).getId();
            if(i < courses.size()-1){
                s+= " ";
            }
        }

        return s;
    }

    //Converts a course string to an array list of courses
    public static ArrayList<Course> StringToList(String arrayString, Context context) {

        if (!arrayString.equals("")) {
            String[] courseTitleIds = arrayString.split(" ");
            ArrayList<Course> scheduleCourses = new ArrayList<Course>();


            for (int i = 0; i < courseTitleIds.length; i++) {
           //     if(!(courseTitleIds[i].equals(""))) {

                    CourseLoader courseLoader = CourseLoader.get(context);


                    Course mCourse = CourseLoader.get(context).getCourse(Integer.parseInt(courseTitleIds[i]));
                    if (mCourse != null) {
                        scheduleCourses.add(mCourse);
                    }
               // }
            }
            return scheduleCourses;
        }

        return new ArrayList<Course>();
    }

}
