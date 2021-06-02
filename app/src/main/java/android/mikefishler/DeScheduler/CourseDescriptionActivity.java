//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.mikefishler.DeScheduler.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class CourseDescriptionActivity extends AppCompatActivity {

    Course mCourse;

    //strings for extras and keys
    public static final String COURSE_EXTRA = "CourseDescriptionActivity.CourseExtra";
    public static final String COURSE_BUNDLE_KEY = "CourseDescriptionActivity.CourseBundleKey";
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";
    private static final String EXTRA_CALLING_ACTIVITY = "CourseDescriptionActivity.CallingActivity";

    private String mCallingActivity;

    //vars to hold textviews and buttons
    private TextView mCourseTitleTextView;
    private TextView mCreditsTextView;
    private TextView mStartDateTextView;
    private TextView mTypeTextView;
    private TextView mEndDateTextView;
    private TextView mMeetingInfoTextView;
    private TextView mProfessorTextView;
    private TextView mNotesTextView;
    private Button mAddButton;

    //vars to hold times
    private double mStartTime;
    private double mEndTime;

    //vars to hold the schedule and list of courses
    private Schedule mSchedule;
    private ArrayList<Course> mCourses;

    //method to create new intent
    public static Intent newIntent(Context packageContext, Course course, UUID scheduleId, String f) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(COURSE_EXTRA, course);
        Intent intent = new Intent(packageContext, CourseDescriptionActivity.class);
        intent.putExtra(COURSE_BUNDLE_KEY, bundle);
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        intent.putExtra(EXTRA_CALLING_ACTIVITY, f);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_description);

        //get info from bundle and intent
        Bundle bundle = getIntent().getBundleExtra(COURSE_BUNDLE_KEY);
        mCourse = bundle.getParcelable(COURSE_EXTRA);
        mCallingActivity = getIntent().getStringExtra(EXTRA_CALLING_ACTIVITY);
        final UUID mScheduleId = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);

        //get the schedule from schedule lab
        mSchedule = ScheduleLab.get(this).getSchedule(mScheduleId);
        //get courses for given schedule
        mCourses = mSchedule.getCourses();

        //set the times accordingly
        if(mCourse.getMeetingInfo().getStartTime() != null) {
            mStartTime = mCourse.getMeetingInfo().getStartTime().getHours() + ((mCourse.getMeetingInfo().getStartTime().getMinutes()) / 100.00);
            mEndTime = mCourse.getMeetingInfo().getEndTime().getHours() + ((mCourse.getMeetingInfo().getEndTime().getMinutes()) / 100.00);
        }
        else{
            mStartTime = 0;
            mEndTime = 0;
        }

        //get widgets by id
        mCourseTitleTextView = (TextView) findViewById(R.id.course_title);
        mCreditsTextView = (TextView) findViewById(R.id.course_credits);
        mStartDateTextView = (TextView) findViewById(R.id.startDate);
        mTypeTextView = (TextView) findViewById(R.id.type);
        mEndDateTextView = (TextView) findViewById(R.id.endDate);
        mMeetingInfoTextView = (TextView) findViewById(R.id.meeting_info);
        mProfessorTextView = (TextView) findViewById(R.id.professor);
        mNotesTextView = (TextView) findViewById(R.id.notes);

        //set the text of the various widgets with given info
        mCourseTitleTextView.setText(mCourse.getCourse() + ": " + mCourse.getTitle());
        mCreditsTextView.setText(String.format("%s %.2f", getResources().getString(R.string.credits_title), mCourse.getCredits()));
        if (mCourse.getMeetingInfo().getStartDate() != null) {
            mStartDateTextView.setText(String.format("%s %s", getResources().getString(R.string.start_date_title), mCourse.getMeetingInfo().getStartDate().toString()));
        } else {
            mStartDateTextView.setText(String.format("%s %s", getResources().getString(R.string.start_date_title), getResources().getString(R.string.tbd_text)));
        }
        if (!(mCourse.getMeetingInfo().getType().equals(""))) {
            mTypeTextView.setText(String.format("%s %s", getResources().getString(R.string.course_type_title), mCourse.getMeetingInfo().getType()));
        } else {
            mTypeTextView.setText(String.format("%s %s", getResources().getString(R.string.course_type_title), getResources().getString(R.string.tbd_text)));
        }

        if (mCourse.getMeetingInfo().getEndDate() != null) {
            mEndDateTextView.setText(String.format("%s %s", getResources().getString(R.string.end_date_title), mCourse.getMeetingInfo().getEndDate().toString()));
        } else {
            mEndDateTextView.setText(String.format("%s %s", getResources().getString(R.string.end_date_title), getResources().getString(R.string.tbd_text)));
        }

        String meetingInfoString = mCourse.createMeetingInfoString();

        if (!(meetingInfoString.equals(""))) {
            mMeetingInfoTextView.setText(String.format("%s %s", getResources().getString(R.string.meeting_info_title), meetingInfoString));
        } else {
            mMeetingInfoTextView.setText(String.format("%s %s", getResources().getString(R.string.meeting_info_title), getResources().getString(R.string.tbd_text)));
        }

        if (mCourse.getStaff() != null) {
            mProfessorTextView.setText(String.format("%s %s", getResources().getString(R.string.professor_title), mCourse.getStaff()));
        } else {
            mProfessorTextView.setText(String.format("%s %s", getResources().getString(R.string.professor_title), R.string.tbd_text));
        }

        if (mCourse.getNotes() != null) {
            mNotesTextView.setText(String.format("%s %s", getResources().getString(R.string.notes_title), mCourse.getNotes()));
        } else {
            mNotesTextView.setText(String.format("%s %s", getResources().getString(R.string.notes_title), R.string.tbd_text));
        }


        //check which activity called this activity
        if(mCallingActivity.equals(CourseDatabaseListFragment.FUNCTION)) {
            mAddButton = (Button) findViewById(R.id.add_lose_button);
            mAddButton.setText(R.string.add_course_button_text);
            mAddButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //vars to hold info regarding clourse conflicts
                    boolean courseOK = true;
                    boolean sameCourseOK = true;

                    //loop through all courses
                    for(Course course : mCourses) {

                        double courseStartTime = 0;
                        double courseEndTime = 0;

                        if(course.getMeetingInfo().getStartTime() != null) {

                            courseStartTime = course.getMeetingInfo().getStartTime().getHours() + ((course.getMeetingInfo().getStartTime().getMinutes()) / 100.00);
                            courseEndTime = course.getMeetingInfo().getEndTime().getHours() + ((course.getMeetingInfo().getEndTime().getMinutes()) / 100.00);
                        }

                        //checks if the same course is being added twice
                        if (course.getcId() == mCourse.getcId()) {
                            sameCourseOK = false;
                            break;
                        }
                        if (mStartTime != 0 && courseStartTime != 0) {
                            //checks if there is a time conflict for that given day
                            if (course.getMeetingInfo().getMeetingDays().toString().contains("MONDAY") && mCourse.getMeetingInfo().getMeetingDays().toString().contains("MONDAY")) {
                                if ((mStartTime >= courseStartTime && mStartTime <= courseEndTime) || (mEndTime >= courseStartTime && mEndTime <= courseEndTime)) {
                                    courseOK = false;
                                    break;
                                }
                            }
                            //checks if there is a time conflict for that given day
                            if (course.getMeetingInfo().getMeetingDays().toString().contains("TUESDAY") && mCourse.getMeetingInfo().getMeetingDays().toString().contains("TUESDAY")) {
                                if ((mStartTime >= courseStartTime && mStartTime <= courseEndTime) || (mEndTime >= courseStartTime && mEndTime <= courseEndTime)) {
                                    courseOK = false;
                                    break;
                                }
                            }
                            //checks if there is a time conflict for that given day
                            if (course.getMeetingInfo().getMeetingDays().toString().contains("WEDNESDAY") && mCourse.getMeetingInfo().getMeetingDays().toString().contains("WEDNESDAY")) {
                                if ((mStartTime >= courseStartTime && mStartTime <= courseEndTime) || (mEndTime >= courseStartTime && mEndTime <= courseEndTime)) {
                                    courseOK = false;
                                    break;
                                }
                            }
                            //checks if there is a time conflict for that given day
                            if (course.getMeetingInfo().getMeetingDays().toString().contains("THURSDAY") && mCourse.getMeetingInfo().getMeetingDays().toString().contains("THURSDAY")) {
                                if ((mStartTime >= courseStartTime && mStartTime <= courseEndTime) || (mEndTime >= courseStartTime && mEndTime <= courseEndTime)) {
                                    courseOK = false;
                                    break;
                                }
                            }
                            //checks if there is a time conflict for that given day
                            if (course.getMeetingInfo().getMeetingDays().toString().contains("FRIDAY") && mCourse.getMeetingInfo().getMeetingDays().toString().contains("FRIDAY")) {
                                if ((mStartTime >= courseStartTime && mStartTime <= courseEndTime) || (mEndTime >= courseStartTime && mEndTime <= courseEndTime)) {
                                    courseOK = false;
                                    break;
                                }
                            }
                        }
                    }

                    //if there are no conflicts, add the course
                    if(courseOK && sameCourseOK){
                        mSchedule.addCourse(mCourse);
                        ScheduleLab.get(getApplication()).updateSchedule(mSchedule);
                        finish();
                    }
                    //if you try to add the same course twice
                    else if(courseOK && sameCourseOK == false){
                        Toast.makeText(getBaseContext(),"Error! Cannot add same course twice!", Toast.LENGTH_SHORT).show();
                    }
                    //if you try to add a course that has a time conflict
                    else if(courseOK == false && sameCourseOK){
                        Toast.makeText(getBaseContext(),"Error! Time Slot Already Occupied!", Toast.LENGTH_SHORT).show();
                    }
                    //if some other conflict ocurred
                    else
                    {
                        Toast.makeText(getBaseContext(),"Error! Cannot add course!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(mCallingActivity.equals(CourseListFragment.FUNCTION)) {
            mAddButton = (Button) findViewById(R.id.add_lose_button);
            mAddButton.setText(R.string.delete_course_button_text);
            mAddButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //deletes course
                    mSchedule.deleteCourse(mCourse);
                    ScheduleLab.get(getApplication()).updateSchedule(mSchedule);
                    finish();
                }
            });
        }
    }

}