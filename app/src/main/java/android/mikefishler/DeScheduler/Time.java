//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {
    private int mHours; //hours
    private int mMinutes; //minutes

    //Constructor
    public Time(int hours, int minutes) {
        this.mHours = hours;
        this.mMinutes = minutes;
    }

    //Default constructor
    public Time(){
        this.mHours = 0;
        this.mMinutes = 0;
    }

    //Getters & Setters
    public int getHours() {
        return mHours;
    }

    public void setHours(int hours) {
        this.mHours = hours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        this.mMinutes = minutes;
    }

    //To string
    public String toString(){
        String s = "";
        if(mHours < 12) {
            s = String.format("%02d:%02dAM", mHours, mMinutes);
        }
        if(mHours >= 12 && mHours < 13){
            s = String.format("%02d:%02dPM", mHours, mMinutes);
        }
        if(mHours >=13){
            s = String.format("%02d:%02dPM", (mHours-12), mMinutes);
        }
        return s;
    }

    //Needed for parcelable object
    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHours);
        dest.writeInt(mMinutes);
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {

        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    protected Time(Parcel in) {
        mHours = in.readInt();
        mMinutes = in.readInt();
    }

    //compares times for scheduling purposes
    public int compare(Time otherTime){
        if(mHours < otherTime.getHours()){
            return -1;
        }
        else if(mHours > otherTime.getHours()){
            return 1;
        }

        else{
            if(mMinutes < otherTime.getMinutes()){
                return -1;
            }
            else if(mMinutes > otherTime.getMinutes()){
                return 1;
            }
            else{
                return 0;
            }
        }
    }

}