//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable {
    private int mMonth; //month of date
    private int mDay; //day of date
    private int mYear; //year of date

    //Constructor
    public Day(int m, int d, int y) {
        mMonth = m;
        mDay = d;
        mYear = y;
    }

    //Default constructor
    public Day(){
        this.mMonth = 0;
        this.mDay = 0;
        this.mYear =0;
    }

    // Getters & setters
    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    //to string
    public String toString(){
        String s = mMonth + "/" + mDay + "/" + mYear;
        return s;
    }

    //Functions for parcelable object
    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMonth);
        dest.writeInt(mDay);
        dest.writeInt(mYear);
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {

        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    protected Day(Parcel in) {
        mMonth = in.readInt();
        mDay = in.readInt();
        mYear = in.readInt();
    }
}
