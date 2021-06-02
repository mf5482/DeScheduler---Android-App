//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ScheduleActivity extends SingleFragmentActivity {

    //string extra for schedule id
    private static final String EXTRA_SCHEDULE_ID = "com.bignerdranch.android.descheduler.schedule_id";

    //method to create a new intent
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ScheduleActivity.class);
        return intent;
    }

    //method to create a new fragment
    @Override
    protected Fragment createFragment() {
        return new ScheduleFragment();
    }
}
