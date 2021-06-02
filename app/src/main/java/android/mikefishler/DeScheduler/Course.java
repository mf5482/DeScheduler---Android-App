//Created by: Jesse and Mike

package android.mikefishler.DeScheduler;

import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

    //member variables
    private UUID cId;
    private String mCourse;
    private String mTitle;
    private float mCredits;
    private MeetingInfo mMeetingInfo;
    private MeetingInfo mMeetingInfo2;
    private String mStaff;
    private String mNotes;
    private int mId;

    //constructor
    public Course() {
        cId = UUID.randomUUID();
        mCourse = "";
        mTitle = "";
        mCredits = 0;
        mMeetingInfo = null;
        mMeetingInfo2 = null;
        mStaff = "";
        mNotes = "";
    }

    //setters and getters
    public UUID getcId (){
        return cId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getCourse() {
        return mCourse;
    }

    public void setCourse(String course) {
        this.mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public float getCredits() {
        return mCredits;
    }

    public void setCredits(float credits) {
        this.mCredits = credits;
    }

    public MeetingInfo getMeetingInfo() {
        return mMeetingInfo;
    }

    public void setMeetingInfo(MeetingInfo meetingInfo) {
        this.mMeetingInfo = meetingInfo;
    }

    public MeetingInfo getMeetingInfo2() {
        return mMeetingInfo2;
    }

    public void setMeetingInfo2(MeetingInfo meetingInfo2) {
        this.mMeetingInfo2 = meetingInfo2;
    }

    public String getStaff() {
        return mStaff;
    }

    public void setStaff(String staff) {
        this.mStaff = staff;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    //method to create string
    public String toString(){
        String s = "";

        s+= "Course: ";
        s+= mCourse;
        s+= "\n";

        s+= "Title: ";
        s+= mTitle;
        s+= "\n";

        s+= "Credits: ";
        s+= mCredits;
        s+= "\n";

        s+= "Meeting Info: " + "\n";
        if(mMeetingInfo != null) {
            s+= mMeetingInfo.toString();
        }
        s+= "\n";

        if(mMeetingInfo2 != null){
            s += "Meeting Info 2: \n";
            s += mMeetingInfo2.toString();
        }
        s+= "\n";

        s+= "Staff: ";
        s+= mStaff;
        s+= "\n";

        s+= "Notes: ";
        s+= mNotes;

        return s;
    }

    //Needed for parcelable object
    @Override
    public int describeContents() {
        return hashCode();
    }

    //Writes class info to parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mCourse);
        dest.writeString(mTitle);
        dest.writeFloat(mCredits);
        dest.writeParcelable(mMeetingInfo, flags);
        dest.writeParcelable(mMeetingInfo2, flags);
        dest.writeString(mStaff);
        dest.writeString(mNotes);
    }

    //Needed for parcelable object
    public static final Creator<Course> CREATOR = new Creator<Course>() {

        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    //Read in from a parcel
    protected Course(Parcel in) {
        mId = in.readInt();
        mCourse = in.readString();
        mTitle = in.readString();
        mCredits = in.readFloat();
        mMeetingInfo = in.readParcelable(Course.class.getClassLoader());
        mMeetingInfo2 = in.readParcelable(Course.class.getClassLoader());
        mStaff = in.readString();
        mNotes = in.readString();

    }

    //MeetingInfo to String
    public String createMeetingInfoString(){
        String s = "";
        MeetingInfo temp = getMeetingInfo();
        int numOfTimes = 1;

        if(getMeetingInfo2() != null){
            numOfTimes = 2;
        }
        for(int i = 0; i < numOfTimes; i++) {
            for (int j = 0; j < temp.getMeetingDays().size(); j++) {
                if (temp.getMeetingDays().get(j).equals(DaysOfWeek.MONDAY)) {
                    s += "M";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.TUESDAY)) {
                    s += "T";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.WEDNESDAY)) {
                    s += "W";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.THURSDAY)) {
                    s += "R";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.FRIDAY)) {
                    s += "F";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.SATURDAY)) {
                    s += "S";
                } else if (temp.getMeetingDays().get(j).equals(DaysOfWeek.TBA)) {
                    s += "TBA";
                    break;
                } else {
                }
            }
            if (temp.getStartTime() != null) {
                s += " " + temp.getStartTime().toString() + "-" + temp.getEndTime().toString();
            }

            if(getMeetingInfo2() != null && i != (numOfTimes-1)){
                s+= ", ";
                temp = getMeetingInfo2();
            }
        }

        return s;
    }
}

