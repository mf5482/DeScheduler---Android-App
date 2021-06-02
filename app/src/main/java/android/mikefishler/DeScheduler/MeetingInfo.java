//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MeetingInfo implements Parcelable {

    public static String MEETING_DAYS_KEY = "MeetingInfo.DaysOfWeek";

    private Day mStartDate; //start date of course
    private Day mEndDate; //end date of course
    private String mBuilding; //building course is located in
    private String mRoom; //room course is located in

    private String mType; //Lab vs Lecture
    private Time mStartTime; //start time of course
    private Time mEndTime; //end time of course
    private ArrayList<DaysOfWeek> mMeetingDays = new ArrayList<DaysOfWeek>(); //days class will meet

    //String objects for writing daysOfWeek to screen
    private static String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "TBA"};

    //Default constructor
    public MeetingInfo() {
        mStartDate = null;
        mEndDate = null;
        mBuilding = "";
        mRoom = "";
        mType = "";
        mStartTime = null;
        mEndTime = null;
    }

    // Getters & Setters
    public Day getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Day startDate) {
        this.mStartDate = startDate;
    }

    public Day getEndDate() { return mEndDate; }

    public void setEndDate(Day endDate) {
        this.mEndDate = endDate;
    }

    public String getBuilding() {
        return mBuilding;
    }

    public void setBuilding(String building) {
        this.mBuilding = building;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        this.mRoom = room;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public Time getStartTime() {return mStartTime;}

    public void setStartTime(Time startTime) {
        this.mStartTime = startTime;
    }

    public Time getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Time endTime) {
        this.mEndTime = endTime;
    }

    public ArrayList<DaysOfWeek> getMeetingDays() {
        return mMeetingDays;
    }

    //Add a meeting day to meeting day array
    public void addMeetingDays(DaysOfWeek meetingDay) {
        mMeetingDays.add(meetingDay);
    }

    //delete meeting days
    public void deleteMeetingDays(){
        mMeetingDays = new ArrayList<DaysOfWeek>();
        mMeetingDays.add(DaysOfWeek.TBA);
    }

    //to string
    public String toString(){
        String s = "";

        s += "Start Date: ";
        if(mStartDate != null){
            s+= mStartDate.toString();
        }
        else{
            s+= "None";
        }
        s+= "\n";

        s += "End Date: ";
        if(mEndDate != null){
            s+= mEndDate.toString();
        }
        else{
            s+= "None";
        }
        s+= "\n";

        s += "Building: ";
        s += mBuilding;
        s+= "\n";

        s += "Room: ";
        s += mRoom;
        s += "\n";

        s += "Type: ";
        s += mType;
        s += "\n";

        s += "Meeting Days:";
        if(mMeetingDays != null){
            for(int i = 0; i < mMeetingDays.size(); i++){
                s+= " " + daysOfWeek[mMeetingDays.get(i).ordinal()];
            }
        }
        s+= "\n";

        s += "Start Time: ";
        if(mStartTime != null){
            s+= mStartTime.toString();
        }
        else{
            s+= "None";
        }
        s+= "\n";

        s += "End Time: ";
        if(mEndTime != null){
            s+= mEndTime.toString();
        }
        else{
            s+= "None";
        }
        s+= "\n";


        return s;
    }


    //Parcelable functions
    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(mStartDate, flags);
        dest.writeParcelable(mEndDate, flags);
        dest.writeString(mBuilding);
        dest.writeString(mRoom);
        dest.writeString(mType);
        dest.writeParcelable(mStartTime, flags);
        dest.writeParcelable(mEndTime, flags);
        dest.writeSerializable(mMeetingDays);

    }

    public static final Creator<MeetingInfo> CREATOR = new Creator<MeetingInfo>() {

        @Override
        public MeetingInfo createFromParcel(Parcel in) {
            return new MeetingInfo(in);
        }

        @Override
        public MeetingInfo[] newArray(int size) {
            return new MeetingInfo[size];
        }
    };

    protected MeetingInfo(Parcel in) {
        mStartDate = in.readParcelable(MeetingInfo.class.getClassLoader());
        mEndDate = in.readParcelable(MeetingInfo.class.getClassLoader());
        mBuilding = in.readString();
        mRoom = in.readString();
        mType = in.readString();
        mStartTime = in.readParcelable(MeetingInfo.class.getClassLoader());
        mEndTime = in.readParcelable(MeetingInfo.class.getClassLoader());
        mMeetingDays = (ArrayList<DaysOfWeek>) in.readSerializable();

    }

    public String timeToString(){
        return (mStartTime + "-" + mEndTime);
    }


}
