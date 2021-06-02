//Created by: Jesse Stoner

package android.mikefishler.DeScheduler;

import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class CourseFragment extends Fragment {

    //strings to hold bundle extras
    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_SCHEDULE_ID = "schedule_id";

    private Course mCourse;
    private Schedule mSchedule;
    private EditText mCourseTitleField;
    private EditText mCourseDetailsField;
    private Button mAddCourseButton;

    //method to create new fragment instance
    public static CourseFragment newInstance(UUID courseId, UUID scheduleId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);
        args.putSerializable(ARG_SCHEDULE_ID, scheduleId);

        CourseFragment fragment = new CourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //get the course and schedule ids
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        UUID scheduleId = (UUID) getArguments().getSerializable(ARG_SCHEDULE_ID);

        //get the schedule and the specific course
        mSchedule = ScheduleLab.get(getActivity()).getSchedule(scheduleId);
        mCourse = mSchedule.getSpecificCourse(courseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_course, container, false);

        //listener for entering info in course title
        mCourseTitleField = (EditText) v.findViewById(R.id.course_title);
        mCourseTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //user for entering details for course
        mCourseDetailsField = (EditText) v.findViewById(R.id.course_details);
        mCourseDetailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //listener for add course button
        mAddCourseButton = (Button) v.findViewById(R.id.add_course_button);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCourseTitleField.getText().toString().equals("") || mCourseDetailsField.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You must enter data for both fields!", Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        return v;
    }
}
