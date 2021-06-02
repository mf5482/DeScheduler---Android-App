//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.support.v4.app.Fragment;

public class ScheduleListActivity extends SingleFragmentActivity {

    //creates a new schedule list fragment
    @Override
    protected Fragment createFragment() {
        return new ScheduleListFragment();
    }

}
