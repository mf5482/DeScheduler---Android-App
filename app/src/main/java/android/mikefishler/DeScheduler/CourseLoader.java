//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class CourseLoader extends AppCompatActivity{

    private static CourseLoader sCourseLoader; //static object

    private static DatabaseHelper myDbHelper; //Database helper that will assist in loading courses from database

    private ArrayList<Course> masterClassList = new ArrayList<Course>(); //list of all courses

    //Returns instance of CourseLoader with a database helper
    public static CourseLoader get(Context context, DatabaseHelper db){
        if(sCourseLoader == null){

            sCourseLoader = new CourseLoader(context, db);
        }

        return sCourseLoader;
    }

    //Returns instance of CourseLoader with only a context
    public static CourseLoader get(Context context){
        if(sCourseLoader != null){
            sCourseLoader = new CourseLoader(context, myDbHelper);
        }

        return sCourseLoader;
    }

    //private CourseLoader that reads in courses
    private CourseLoader(Context context, DatabaseHelper myDbHelper){

        Cursor c = null; //cursor for reading database


        this. myDbHelper = myDbHelper;
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        //Query database
        c = myDbHelper.query("masterClass", null, null, null, null, null, null);

        //read database
        if (c.moveToFirst()) {
            do {

                Course newCourse = readCourse(c);
                if(newCourse != null) {
                    masterClassList.add(newCourse);
                }

            } while (c.moveToNext());
        }

    }

    //Customized function for reading a single course
    private Course readCourse(Cursor c) {
        Course newCourse = new Course(); //temporary course object
        newCourse.setId(Integer.parseInt(c.getString(0)));
        newCourse.setCourse(c.getString(1));
        if (masterClassList.size() == 0 || !(newCourse.getCourse().equals(masterClassList.get(masterClassList.size() - 1).getCourse()))) {
            newCourse.setTitle(c.getString(2));
            newCourse.setCredits(Float.parseFloat(c.getString(3)));
            newCourse.setMeetingInfo(readMeetingInfo(c));
            if(c.getString(12) != null) {
                newCourse.setStaff(c.getString(12));
            }
            else{
                newCourse.setStaff("None");
            }

            if(c.getString(13) != null){
                newCourse.setNotes(c.getString(13));
            }
            else{
                newCourse.setNotes("None");
            }

            return newCourse;
        }
        else {
            newCourse = masterClassList.get(masterClassList.size()-2);
            newCourse.setMeetingInfo2(readMeetingInfo(c));
        }

        return null;


    }


    //Customized function for reading meeting info
    private MeetingInfo readMeetingInfo (Cursor c) {
        MeetingInfo newMeetingInfo = new MeetingInfo();

        if (c.getString(4) != null) {


            String tempInts[] = c.getString(4).split("/");
            Day tempDate = new Day(Integer.parseInt(tempInts[0]), Integer.parseInt(tempInts[1]), Integer.parseInt(tempInts[2]));
            newMeetingInfo.setStartDate(tempDate);

            tempInts = c.getString(5).split("/");
            tempDate = new Day(Integer.parseInt(tempInts[0]), Integer.parseInt(tempInts[1]), Integer.parseInt(tempInts[2]));
            newMeetingInfo.setEndDate(tempDate);

            newMeetingInfo.setBuilding(c.getString(6));
            newMeetingInfo.setRoom(c.getString(7));
            newMeetingInfo.setType(c.getString(8));
            String temp = c.getString(9);

            for (int i = 0; i < temp.length(); i++) {

                switch (temp.charAt(i)) {
                    case 'M':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.MONDAY);
                        break;
                    case 'T':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.TUESDAY);
                        break;
                    case 'W':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.WEDNESDAY);
                        break;
                    case 'R':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.THURSDAY);
                        break;
                    case 'F':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.FRIDAY);
                        break;
                    case 'S':
                        newMeetingInfo.addMeetingDays(DaysOfWeek.SATURDAY);
                        break;
                    case 'B':
                        newMeetingInfo.deleteMeetingDays();
                        i = temp.length();
                        break;
                    default:
                        break;
                }
            }


            tempInts = new String[3];
            if (newMeetingInfo.getMeetingDays().get(0) != DaysOfWeek.TBA) {

                if (!(c.getString(10).equals("TBA"))) {

                    temp = c.getString(10);
                    String tempString = Character.toString(temp.charAt(0));
                    tempString += Character.toString(temp.charAt(1));
                    tempInts[0] = tempString;

                    tempString = Character.toString(temp.charAt(3));
                    tempString += Character.toString(temp.charAt(4));
                    tempInts[1] = tempString;

                    tempString = Character.toString(temp.charAt(5));
                    tempString += Character.toString(temp.charAt(6));
                    tempInts[2] = tempString;

                    Time newTime = new Time(Integer.parseInt(tempInts[0]), Integer.parseInt(tempInts[1]));
                    if (tempInts[2].equals("PM") && newTime.getHours() != 12) {
                        newTime.setHours(newTime.getHours() + 12);
                    }
                    newMeetingInfo.setStartTime(newTime);

                    tempInts = new String[3];
                    temp = c.getString(11);
                    tempString = Character.toString(temp.charAt(0));
                    tempString += Character.toString(temp.charAt(1));
                    tempInts[0] = tempString;

                    tempString = Character.toString(temp.charAt(3));
                    tempString += Character.toString(temp.charAt(4));
                    tempInts[1] = tempString;

                    tempString = Character.toString(temp.charAt(5));
                    tempString += Character.toString(temp.charAt(6));
                    tempInts[2] = tempString;

                    newTime = new Time(Integer.parseInt(tempInts[0]), Integer.parseInt(tempInts[1]));
                    if (tempInts[2].equals("PM") && newTime.getHours() != 12) {
                        newTime.setHours(newTime.getHours() + 12);
                    }
                    newMeetingInfo.setEndTime(newTime);

                } else {
                    newMeetingInfo.setStartTime(null);
                    newMeetingInfo.setEndTime(null);
                }
            } else {
                newMeetingInfo.setStartTime(null);
                newMeetingInfo.setEndTime(null);
            }

        }

        return newMeetingInfo;
    }

    //returns a course using an id
    public Course getCourse(int id){
       for (Course course : masterClassList){
           if (course.getId() == id){
                return course;
           }
       }

       return null;
    }

    //returns array of all courses
    public ArrayList<Course> getCourses(){return masterClassList;};

}
