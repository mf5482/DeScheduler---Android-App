//Created by: Jesse and Mike

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CoursePagerActivity extends AppCompatActivity {

    //strings for extras for course and schedule ids
    private static final String EXTRA_COURSE_ID = "com.bignerdranch.android.descheduler.course_id";
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";

    private ViewPager mViewPager;
    private Schedule mSchedule;
    private List<Course> mCourses;

    //method to create a new intent
    public static Intent newIntent(Context packageContext, UUID courseId, UUID scheduleId) {
        Intent intent = new Intent(packageContext, CoursePagerActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_pager);

        //get the course and schedule IDs from the intent
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_ID);
        UUID scheduleId = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);

        //get the schedule for a given schedule id
        mSchedule = ScheduleLab.get(this).getSchedule(scheduleId);

        //find the view pager
        mViewPager = (ViewPager) findViewById(R.id.course_view_pager);

        //get the list of courses for a given schedule
        //and display it in the view pager
        mCourses = mSchedule.getCourses();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Course course = mCourses.get(position);
                return CourseFragment.newInstance(course.getcId(), mSchedule.getId());
            }

            //get the number of courses
            @Override
            public int getCount() {
                return mCourses.size();
            }
        });

        //set the correct course to the view pager
        for(int i = 0; i < mCourses.size(); i++){
            if(mCourses.get(i).getcId().equals(courseId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
