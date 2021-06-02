//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.content.Intent;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseListFragment extends Fragment {

    //string for bundle args
    private static final String ARG_SCHEDULE_ID = "schedule_id";

    public static final String FUNCTION = "CourseListFragment";
    private static final String OPTION = "options";

    //vars for recyclerview, adapter, and linearlayout
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;
    private LinearLayout mVisibleLayout;

    //vars for the schedule, schedule id, and menu
    private Schedule mSchedule;
    private UUID mScheduleId;
    private Menu mMenu;

    //method to create a new instance for course list fragment
    public static CourseListFragment newInstance(UUID scheduleId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCHEDULE_ID, scheduleId);

        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //get the schedule id and schedule
        mScheduleId = (UUID) getArguments().getSerializable(ARG_SCHEDULE_ID);
        mSchedule = ScheduleLab.get(getActivity()).getSchedule(mScheduleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        //get the recyclerview and visible layout and inflate it
        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mVisibleLayout = (LinearLayout) view.findViewById(R.id.empty_course_list);

        //call method to update the UI
        updateUI();

        return view;
    }

    //holder for the recyclerview
    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCourseTitleTextView;
        private TextView mCourseDetailsTextView;

        private Course mCourse;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_course, parent, false));
            //set listener for list of courses
            itemView.setOnClickListener(this);

            mCourseTitleTextView = (TextView) itemView.findViewById(R.id.course_title);
            mCourseDetailsTextView = (TextView) itemView.findViewById(R.id.course_details);
        }

        //bind the data to the views
        public void bind(Course course) {
            mCourse = course;
            mCourseTitleTextView.setText("" + mCourse.getCourse() + " " + mCourse.getTitle());
            if(!(mCourse.createMeetingInfoString().equals(""))) {
                mCourseDetailsTextView.setText(mCourse.createMeetingInfoString());
            }
            else{
                mCourseDetailsTextView.setText(R.string.tbd_text);
            }
        }

        //start the course description activity when course in list is pressed
        @Override
        public void onClick(View view){
            Intent intent = CourseDescriptionActivity.newIntent(getActivity(), mCourse, mScheduleId, FUNCTION);
            startActivity(intent);
        }
    }

    //adapter for recyclerview
    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses){
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CourseHolder(layoutInflater, parent);
        }

        //bind the data to the holder
        @Override
        public void onBindViewHolder(CourseHolder holder, int position){
            Course course = mCourses.get(position);
            holder.bind(course);
        }

        //get the number of items in the list
        @Override
        public int getItemCount(){
            return mCourses.size();
        }
    }

    //inflates the options on the toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        mMenu = menu;

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_course_calendar, menu);
        inflater.inflate(R.menu.fragment_course_list, menu);
        updateMenuBar(menu);
    }

    //update the UI when fragment is resumed
    @Override
    public void onResume() {
        super.onResume();

        updateUI();
        if(mMenu != null) {
            updateMenuBar(mMenu);
        }

    }

    //handle which button in the toolbar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_course:
                Course course = new Course();
                Intent intent = CourseDatabaseListActivity.newIntent(getActivity(), mSchedule.getId());
                startActivity(intent);
                updateUI();
                return true;
            case R.id.delete_schedule:
                FragmentManager manager = getFragmentManager();
                ScheduleOptionsFragmentDialog options = ScheduleOptionsFragmentDialog.newInstance(mSchedule.getId());
                options.show(manager, OPTION);
                return true;
            case R.id.view_calendar:
                Intent cIntent = CalendarActivity.newIntent(getActivity(), mSchedule.getId());
                startActivity(cIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //method to update the UI
    private void updateUI() {
        mSchedule = ScheduleLab.get(getActivity()).getSchedule(mScheduleId);
        ArrayList<Course> courses = mSchedule.getCourses();

        mAdapter = new CourseAdapter(courses);
        mCourseRecyclerView.setAdapter(mAdapter);

        if(courses.size() > 0) {
            mVisibleLayout.setVisibility(View.GONE);
          //  mCalendarButton.setVisibility(View.VISIBLE);
        }
        else{
            mVisibleLayout.setVisibility(View.VISIBLE);
          //  mCalendarButton.setVisibility(View.GONE);
        }


    }

    //method to update the visibility of the calendar button
    private void updateMenuBar(Menu menu){
        if(mSchedule.getCourses().size() == 0){
            menu.findItem(R.id.view_calendar).setVisible(false);
        }
        else{
            menu.findItem(R.id.view_calendar).setVisible(true);
        }
    }


}
