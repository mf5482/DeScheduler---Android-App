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

public class ScheduleFragment extends Fragment {

    //string for bundle argument for schedule id
    private static final String ARG_SCHEDULE_ID = "schedule_id";

    //mvars to hold schedule, button, and textfields
    private Schedule mSchedule;
    private EditText mScheduleTitleField;
    private EditText mScheduleDetailsField;
    private Button mCreateScheduleButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //create a new schedule
        mSchedule = new Schedule();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the view
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        //get the schedule title text field, set the text, and add a listener
        mScheduleTitleField = (EditText) v.findViewById(R.id.schedule_title);
        mScheduleTitleField.setText(mSchedule.getTitle());
        mScheduleTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //set the schedule title when text is changed
                mSchedule.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get the schedule details text field, set the text, and add a listener
        mScheduleDetailsField = (EditText) v.findViewById(R.id.schedule_details);
        mScheduleDetailsField.setText(mSchedule.getScheduleDetails());
        mScheduleDetailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //set the schedule details when text is changed
                mSchedule.setScheduleDetails(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get the create schedule button and add a on click listener
        mCreateScheduleButton = (Button) v.findViewById(R.id.create_schedule_button);
        mCreateScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if a text field is left blank, notify the user
                if(mScheduleTitleField.getText().toString().equals("") || mScheduleDetailsField.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You must enter data for both fields!", Toast.LENGTH_SHORT).show();
                } else {
                    //add the schedule to the schedule lab
                    ScheduleLab.get(getActivity()).addSchedule(mSchedule);
                    //go back to previous activity
                    getActivity().onBackPressed();
                }
            }
        });

        return v;
    }

    //update the schedule in schedule lab when paused
    @Override
    public void onPause(){
        super.onPause();

        ScheduleLab.get(getActivity()).updateSchedule(mSchedule);
    }

}
