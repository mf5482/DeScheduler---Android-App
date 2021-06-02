//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseDatabaseListFragment extends Fragment {
    //strings for bundle argument
    private static final String ARG_SCHEDULE_ID = "schedule_id";


    public static final String FUNCTION = "CourseDatabaseListFragment";

    //request code
    private static final int REQUEST_CODE_ADVANCED_SEARCH = 0;

    //vars to hold recyclerview and adapter
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;

    //vars to hold schedule id and linearlayout
    private UUID mScheduleId;
    private LinearLayout mVisibleLayout;

    Cursor c = null;
    ArrayList<Course> masterCourseList;

    //method to create a new instance of this fragment
    public static CourseDatabaseListFragment newInstance(UUID scheduleId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCHEDULE_ID, scheduleId);

        CourseDatabaseListFragment fragment = new CourseDatabaseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the database courses list layout file and set it invisible
        View view = inflater.inflate(R.layout.fragment_database_course_list, container, false);
        mVisibleLayout = (LinearLayout) view.findViewById(R.id.no_database_courses_list);
        mVisibleLayout.setVisibility(View.GONE);

        //get the schedule id from the bundle
        mScheduleId = (UUID) getArguments().getSerializable(ARG_SCHEDULE_ID);

        //find and inflate the recyclerview
        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.database_course_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CourseLoader courseLoader = CourseLoader.get(getActivity());
        masterCourseList = courseLoader.getCourses();

        //call method to update the UI
        updateUI();


        return view;
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Course mCourse;

        private TextView mCourseTextView;
        private TextView mDateTextView;

        //method to grab onto the views
        public CourseHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_database_course, parent, false));

            //sets a listener to the list items
            itemView.setOnClickListener(this);

            mCourseTextView = (TextView) itemView.findViewById(R.id.course_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.course_days);
        }

        //binds the data to the text views
        public void bind(Course course){
            mCourse = course;
            mCourseTextView.setText(mCourse.getCourse() + ": " + mCourse.getTitle());

            //checks if the course is listed as TBD in database
            if(!(mCourse.createMeetingInfoString().equals(""))) {
                mDateTextView.setText(mCourse.createMeetingInfoString());
            }
            else{
                mDateTextView.setText(R.string.tbd_text);
            }
        }

        //opens the course description activity when a course is pressed
        @Override
        public void onClick(View v) {
            Intent intent = CourseDescriptionActivity.newIntent(getActivity(), mCourse, mScheduleId, FUNCTION);
            startActivity(intent);

        }
    }

    //adapter for the recyclerview
    public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private ArrayList<Course> mAllCourses;


        public CourseAdapter(ArrayList<Course> courses){
            mAllCourses = courses;
        }

        //holds the view
        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CourseHolder(layoutInflater, parent);
        }

        //binds the data to the view
        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = mAllCourses.get(position);
            holder.bind(course);
        }

        //gets the number of courses in the list
        @Override
        public int getItemCount() {
            return mAllCourses.size();
        }
    }

    //creates and inflates the items in the toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_database_course_list, menu);

        //vars to hold info regarding search functionality
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        //listener to detect text change is search box
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //when the text is changed, update the database list
            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Course> newList = new ArrayList<>();
                CourseLoader courseLoader = CourseLoader.get(getActivity());
                masterCourseList = courseLoader.getCourses();

                for(int i = 0; i < masterCourseList.size(); i++){
                    if(masterCourseList.get(i).getTitle().toLowerCase().contains(userInput.toLowerCase()) ||
                            masterCourseList.get(i).getCourse().toLowerCase().contains(userInput.toLowerCase())){
                        newList.add(masterCourseList.get(i));
                    }
                }

                //calls the method to update the database list
                updateItems(newList);

                return true;
            }
        });
    }

    //method to check if the advanced search button was pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.advanced_search:
                Intent intent = new Intent(getActivity(), AdvancedSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADVANCED_SEARCH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //gets result from advanced search activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_ADVANCED_SEARCH){
            ArrayList<Course> selectedCourses = data.getParcelableArrayListExtra(AdvancedSearchActivity.EXTRA_SELECTED_COURSES);
            updateItems(selectedCourses);
        }
    }


    //method to update all items in the list and set layout accordingly
    private void updateItems(List<Course> courses) {
        masterCourseList = new ArrayList<>();
        masterCourseList.addAll(courses);

        mAdapter = new CourseAdapter(masterCourseList);
        mCourseRecyclerView.setAdapter(mAdapter);

        if(courses.size() > 0) {
            mVisibleLayout.setVisibility(View.GONE);
        }
        else{
            mVisibleLayout.setVisibility(View.VISIBLE);
        }
    }

    //method to update the UI with the new info
    private void updateUI(){
        CourseLoader courseLoader = CourseLoader.get(getActivity());
        ArrayList<Course> courses = courseLoader.getCourses();

        mAdapter = new CourseAdapter(courses);
        mCourseRecyclerView.setAdapter(mAdapter);
    }

}
