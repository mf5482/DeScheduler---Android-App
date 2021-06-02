//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class CourseListActivity extends SingleFragmentActivity {

    //string extra for schedule id
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";

    //method to create a new intent
    public static Intent newIntent(Context packageContext, UUID scheduleId) {
        Intent intent = new Intent(packageContext, CourseListActivity.class);
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return intent;
    }

    //method to create a new fragment
    @Override
    protected Fragment createFragment() {
        UUID scheduleId = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);
        return CourseListFragment.newInstance(scheduleId);
    }

}
