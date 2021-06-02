//Created by: Jesse and Mike

package android.mikefishler.DeScheduler;

import android.content.Intent;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ScheduleListFragment extends Fragment {

    //vars to hold recyclerview and its adapter
    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mAdapter;

    //var to hold linear layout
    private LinearLayout mVisibleLayout;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the view
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        //get the recyclerview and set its layout manager
        mScheduleRecyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycler_view);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //get the layout if the schedule is empty
        mVisibleLayout = (LinearLayout) view.findViewById(R.id.empty_schedule_list);

        //call the method to load the UI
        loadUI();

        return view;
    }

    //holder class for the recyclerview
    private class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        //vars to hold the textviews
        private TextView mScheduleTitleTextView;
        private TextView mNumberCoursesTextView;

        //var to hold the schedule
        private Schedule mSchedule;

        //inflate the views and grab hold of the view
        public ScheduleHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_schedule, parent, false));
            //set a listener for the items in the list
            itemView.setOnClickListener(this);

            mScheduleTitleTextView = (TextView) itemView.findViewById(R.id.schedule_title);
            mNumberCoursesTextView = (TextView) itemView.findViewById(R.id.schedule_details);
        }

        //bind the info to the textviews
        public void bind(Schedule schedule) {
            mSchedule = schedule;
            mScheduleTitleTextView.setText(mSchedule.getTitle());
            mNumberCoursesTextView.setText(mSchedule.getScheduleDetails());
        }

        //handles the clisk of a schedule in the list
        @Override
        public void onClick(View view){
            //start the course list activity
            Intent intent = CourseListActivity.newIntent(getActivity(), mSchedule.getId());
            startActivity(intent);
        }

    }

    //adapter class for the recyclerview
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {

        //var to hold the list of schedules
        private List<Schedule> mSchedules;

        //adapter constructor
        public ScheduleAdapter(List<Schedule> schedules){
            mSchedules = schedules;
        }

        //creates the view holder
        @Override
        public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ScheduleHolder(layoutInflater, parent);
        }

        //binds a schedule to the holder
        @Override
        public void onBindViewHolder(ScheduleHolder holder, int position){
            Schedule schedule = mSchedules.get(position);
            holder.bind(schedule);
        }

        //gets the number of schedules
        @Override
        public int getItemCount(){
            return mSchedules.size();
        }

        public void setSchedules(List<Schedule> schedules){
            mSchedules = schedules;
        }
    }

    //update the UI when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //inflate the options on the toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_schedule_list, menu);
    }

    //handles the selection of the options in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_schedule:
                Intent intent = ScheduleActivity.newIntent(getActivity());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //method to load the UI from the database
    private void loadUI() {

        DatabaseHelper myDbHelper = new DatabaseHelper(getActivity());
        CourseLoader.get(getActivity(), myDbHelper);
        //call the update UI method
        updateUI();

    }

    //method to update the UI with the new schedules
    private void updateUI(){

        ScheduleLab scheduleLab = ScheduleLab.get(getActivity());
        List<Schedule> schedules = scheduleLab.getSchedules();


        mAdapter = new ScheduleAdapter(schedules);
        mScheduleRecyclerView.setAdapter(mAdapter);


        //checks to see if there are any created schedules and sets the layout accordingly
        if(schedules.size() > 0) {
            mVisibleLayout.setVisibility(View.GONE);
        }
        else{
            mVisibleLayout.setVisibility(View.VISIBLE);
        }

    }


}
