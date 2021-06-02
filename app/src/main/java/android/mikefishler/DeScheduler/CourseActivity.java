//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CourseActivity extends SingleFragmentActivity {

    //string extra for schedule id
    public static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.course_id";

    //method to create intent
    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, CourseActivity.class);
        return intent;
    }

    //creates a new course fragment
    @Override
    protected Fragment createFragment() {
        return new CourseFragment();
    }
}
