//Created by: Jesse and Mike

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CourseDatabaseListActivity extends SingleFragmentActivity {

    //string extra for schedule id
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";

    //method to create new intent
    public static Intent newIntent(Context packageContext, UUID scheduleId) {
        Intent intent = new Intent(packageContext, CourseDatabaseListActivity.class);
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return intent;
    }

    //creates a new course database list fragment and passes the schedule id
    @Override
    protected Fragment createFragment() {
        UUID scheduleId = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);
        return CourseDatabaseListFragment.newInstance(scheduleId);
    }
}
