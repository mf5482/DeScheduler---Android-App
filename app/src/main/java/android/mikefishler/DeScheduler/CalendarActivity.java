//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class CalendarActivity extends AppCompatActivity {

    //string to hold extra for schedule id
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";

    //variables to hold the schedule and list of courses
    private Schedule mSchedule;
    private ArrayList<Course> mCourses;

    //variables to check days of week
    private boolean monday = false;
    private boolean tuesday = false;
    private boolean wednesday = false;
    private boolean thursday = false;
    private boolean friday = false;

    //variables to hold start and end times of courses
    private int startTimeIndex;
    private int endTimeIndex;

    //variables to hold different views
    private TableLayout t;
    private TableRow tRow;
    private TextView v;

    //function to create intent
    public static Intent newIntent(Context packageContext, UUID scheduleId) {
        Intent intent = new Intent(packageContext, CalendarActivity.class);
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_schedule);

        //gets schedule id from intent
        UUID mScheduleId = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);

        //gets schedule from schedule lab using the id
        mSchedule = ScheduleLab.get(this).getSchedule(mScheduleId);

        //gets the list of courses associated with given schedule
        mCourses = mSchedule.getCourses();

        //grabs hold of the table layout
        t = (TableLayout) findViewById(R.id.calendar);

        //loop through each course in a schedule
        for(Course course : mCourses) {

            if (course.getMeetingInfo().getStartTime() != null) {
                //generate a random color for the boxes
                int color = generateColor();

                //checks what days each course is offered
                if (course.getMeetingInfo().getMeetingDays().toString().contains("MONDAY")) {
                    monday = true;
                }
                if (course.getMeetingInfo().getMeetingDays().toString().contains("TUESDAY")) {
                    tuesday = true;
                }
                if (course.getMeetingInfo().getMeetingDays().toString().contains("WEDNESDAY")) {
                    wednesday = true;
                }
                if (course.getMeetingInfo().getMeetingDays().toString().contains("THURSDAY")) {
                    thursday = true;
                }
                if (course.getMeetingInfo().getMeetingDays().toString().contains("FRIDAY")) {
                    friday = true;
                }

                //if a course is offered at a certain starting time, set the start time index to the
                // appropriate corresponding "cell" in the calendar
                switch (course.getMeetingInfo().getStartTime().getHours()) {
                    case 8:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 2;
                        } else {
                            startTimeIndex = 1;
                        }
                        break;
                    case 9:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 4;
                        } else {
                            startTimeIndex = 3;
                        }
                        break;
                    case 10:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 6;
                        } else {
                            startTimeIndex = 5;
                        }
                        break;
                    case 11:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 8;
                        } else {
                            startTimeIndex = 7;
                        }
                        break;
                    case 12:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 10;
                        } else {
                            startTimeIndex = 9;
                        }
                        break;
                    case 13:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 12;
                        } else {
                            startTimeIndex = 11;
                        }
                        break;
                    case 14:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 14;
                        } else {
                            startTimeIndex = 13;
                        }
                        break;
                    case 15:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 16;
                        } else {
                            startTimeIndex = 15;
                        }
                        break;
                    case 16:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 18;
                        } else {
                            startTimeIndex = 17;
                        }
                        break;
                    case 17:
                        if (course.getMeetingInfo().getStartTime().getMinutes() == 30) {
                            startTimeIndex = 20;
                        } else {
                            startTimeIndex = 19;
                        }
                        break;
                    default:
                        break;
                }

                //if a course is offered at a certain ending time, set the end time index to the
                //appropriate corresponding "cell" in the calendar

                switch (course.getMeetingInfo().getEndTime().getHours()) {

                    case 8:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 1;
                        } else {
                            endTimeIndex = 2;
                        }
                        break;
                    case 9:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 3;
                        } else {
                            endTimeIndex = 4;
                        }
                        break;
                    case 10:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 5;
                        } else {
                            endTimeIndex = 6;
                        }
                        break;
                    case 11:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 7;
                        } else {
                            endTimeIndex = 8;
                        }
                        break;
                    case 12:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 9;
                        } else {
                            endTimeIndex = 10;
                        }
                        break;
                    case 13:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 11;
                        } else {
                            endTimeIndex = 12;
                        }
                        break;
                    case 14:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 13;
                        } else {
                            endTimeIndex = 14;
                        }
                        break;
                    case 15:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 15;
                        } else {
                            endTimeIndex = 16;
                        }
                        break;
                    case 16:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 17;
                        } else {
                            endTimeIndex = 18;
                        }
                        break;
                    case 17:
                        if (course.getMeetingInfo().getEndTime().getMinutes() == 15 || course.getMeetingInfo().getEndTime().getMinutes() == 20
                                || course.getMeetingInfo().getEndTime().getMinutes() == 29 || course.getMeetingInfo().getEndTime().getMinutes() == 30) {
                            endTimeIndex = 19;
                        } else {
                            endTimeIndex = 20;
                        }
                        break;
                    default:
                        break;
                }

                //if the course is offered on a given, color the boxes in the calendar
                //from the starting index to the ending index
                if (monday) {
                    for (int index = startTimeIndex; index <= endTimeIndex; index++) {
                        tRow = (TableRow) t.getChildAt(index);
                        v = (TextView) tRow.getChildAt(1);
                        v.setBackgroundColor(color);
                        v.setText(course.getCourse());
                    }
                    monday = false;
                }

                if (tuesday) {
                    for (int index = startTimeIndex; index <= endTimeIndex; index++) {
                        tRow = (TableRow) t.getChildAt(index);
                        v = (TextView) tRow.getChildAt(2);
                        v.setBackgroundColor(color);
                        v.setText(course.getCourse());
                    }
                    tuesday = false;
                }

                if (wednesday) {
                    for (int index = startTimeIndex; index <= endTimeIndex; index++) {
                        tRow = (TableRow) t.getChildAt(index);
                        v = (TextView) tRow.getChildAt(3);
                        v.setBackgroundColor(color);
                        v.setText(course.getCourse());
                    }
                    wednesday = false;
                }

                if (thursday) {
                    for (int index = startTimeIndex; index <= endTimeIndex; index++) {
                        tRow = (TableRow) t.getChildAt(index);
                        v = (TextView) tRow.getChildAt(4);
                        v.setBackgroundColor(color);
                        v.setText(course.getCourse());
                    }
                    thursday = false;
                }

                if (friday) {
                    for (int index = startTimeIndex; index <= endTimeIndex; index++) {
                        tRow = (TableRow) t.getChildAt(index);
                        v = (TextView) tRow.getChildAt(5);
                        v.setBackgroundColor(color);
                        v.setText(course.getCourse());
                    }
                    friday = false;
                }


            }
        }
    }

    //method to generate a random color
    public int generateColor(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        return color;
    }

}
