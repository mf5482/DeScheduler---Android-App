//Created by : Jesse and Mike

package android.mikefishler.DeScheduler;

import java.util.ArrayList;
import java.util.UUID;

public class Schedule {

    //member variables
    private UUID mId;
    private String mTitle;
    private String mScheduleDetails;
    private ArrayList<Course> mCourses;

    //constructor
    public Schedule(){
        this(UUID.randomUUID());
    }

    //constructor when given an id
    public Schedule(UUID id){

        mId = id;
        mTitle = "";
        mScheduleDetails = "";
        mCourses = new ArrayList<>();

    }

    //getters and setters
    public UUID getId(){
        return mId;
    }

    public void setTitle(String mTitle){
        this.mTitle = mTitle;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setScheduleDetails(String mScheduleDetails){
        this.mScheduleDetails = mScheduleDetails;
    }

    public String getScheduleDetails(){
        return mScheduleDetails;
    }

    public void setCourses(ArrayList<Course> mCourses){
        this.mCourses = mCourses;
    }

    public ArrayList<Course> getCourses() {

        return mCourses;
    }

    //method to get a specific course given a course id
    public Course getSpecificCourse(UUID courseId){
        for(Course course : mCourses){
            if(course.getcId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    //method to add course
    public void addCourse(Course course){

        mCourses.add(course);
    }

    //method to delete a course
    public void deleteCourse(Course course){
        for(int i = 0; i < mCourses.size(); i++) {
            if(course.getId()== mCourses.get(i).getId()){
                mCourses.remove(i);
                return;
            }
        }
    }
}
