//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AdvancedSearchActivity extends AppCompatActivity implements OnItemSelectedListener {

    //Used to send the resulting courses back to the calling function
    public static final String EXTRA_SELECTED_COURSES = "advancedSearchActivity_selectedCourses_extra";

    //Edit texts variables
    private EditText mCourseIdEditText;
    private EditText mCourseTitleEditText;
    private EditText mProfessorEditText;

    //Spinner variables
    private Spinner mTimeSpinner;
    private Spinner mMajorSpinner;
    private Button mSearchButton;

    //Date checkboxes
    private CheckBox mMondayCheckBox;
    private CheckBox mTuesdayCheckBox;
    private CheckBox mWednesdayCheckBox;
    private CheckBox mThursdayCheckBox;
    private CheckBox mFridayCheckBox;
    private CheckBox mSaturdayCheckBox;

    //Array of strings used for matching in the mMajorSpinner
    private String[] majorAbbreviations = {"AB", "BI", "CH", "CJ", "CM", "CS", "DA", "EC", "ED", "EE", "EN", "EX",
            "FA","FN", "FR", "HC", "HI", "HS", "IL", "LG", "MA", "MG", "MK", "NU", "PE", "PH", "PL", "PO", "PS",
            "SO", "SP", "SS", "SX", "TH", "TR", "TV", "WC"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Inflate variables
        mCourseIdEditText = (EditText) findViewById(R.id.course_id_edit_text);
        mCourseTitleEditText = (EditText) findViewById(R.id.course_title_edit_text);
        mTimeSpinner = (Spinner) findViewById(R.id.time_spinner);
        mMajorSpinner = (Spinner) findViewById(R.id.major_spinner);
        mProfessorEditText = (EditText) findViewById(R.id.professor_edit_text);
        mSearchButton = (Button) findViewById(R.id.select_search_button);

        createCheckboxes();
        createSpinners();

        //Set action listener for search button
        mSearchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                //Find narrowed down results from user specifications
                ArrayList<Course> selectedCourses = collectCourses();

                //Pass back courses if there are courses to pass back
                if(selectedCourses != null) {
                    data.putExtra(EXTRA_SELECTED_COURSES, selectedCourses);
                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        });

    }

    //Method to gain matching courses according to user input
    private ArrayList<Course> collectCourses() {

        //Start off with all courses
        ArrayList<Course> selectedCourses = CourseLoader.get(this).getCourses();

        //Shows if the user put in any information, the search function won't work if this is false
        boolean somethingChecked = false;

        for (int i = 0; i < selectedCourses.size(); i++) {

            boolean remove = false; //denotes if the current course should be removed

            //If the user typed in something for course id
            if (!mCourseIdEditText.getText().toString().equals("")) {
                somethingChecked = true;
                //if what the user typed in doesn't match the course's id
                if (!selectedCourses.get(i).getCourse().toLowerCase().contains(mCourseIdEditText.getText().toString().toLowerCase())) {
                    remove = true;
                }
            }

            //If the user typed in something for course title
            if (!mCourseTitleEditText.getText().toString().equals("")) {
                somethingChecked = true;
                //if what the user typed in doesn't match the course's title
                if (!selectedCourses.get(i).getTitle().toLowerCase().contains(mCourseTitleEditText.getText().toString().toLowerCase())) {
                    remove = true;
                }
            }

            //If the user typed in something for professor
            if (!mProfessorEditText.getText().toString().equals("")) {
                somethingChecked = true;

                //if what the user typed in doesn't match the course's professor
                if (!selectedCourses.get(i).getStaff().toLowerCase().contains(mProfessorEditText.getText().toString().toLowerCase())) {
                    remove = true;
                }
            }

            //If user did not leave the spinner to the default choice
            if (!mTimeSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.none_selected_spinner))) {
                somethingChecked = true;

                //if what the user selected doesn't match the course's time
                if (!selectedCourses.get(i).getMeetingInfo().timeToString().equals(mTimeSpinner.getSelectedItem().toString())) {
                    remove = true;
                }
            }

            //If user did not leave the spinner to the default choice
            if (!(mMajorSpinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.major_array)[0]))) {
                somethingChecked = true;

                //Array of constant string values pulled from the string.xml file
                String[] array = getResources().getStringArray(R.array.major_array);

                //Go through all major options
                for (int j = 0; j < array.length; j++) {
                    //If user did not leave the spinner to the default choice
                    if (mMajorSpinner.getSelectedItem().toString().equals(array[j])) {
                        //Get the first two characters from the course's id (indicates what major the course is)
                        String courseMajor = Character.toString(selectedCourses.get(i).getCourse().charAt(0)) + Character.toString(selectedCourses.get(i).getCourse().charAt(1));

                        //If the course's major matches what the user selected on the spinner
                        if (!courseMajor.equals(majorAbbreviations[j - 1])) {
                            remove = true;
                            break;
                        }
                    }
                }
            }


            if (!remove) {
                //get meeting days for class
                ArrayList<DaysOfWeek> currentCourseMeetingDays = selectedCourses.get(i).getMeetingInfo().getMeetingDays();

                //if there are no meeting days, remove automatically
                if (currentCourseMeetingDays == null) {
                    remove = true;
                }

                //ensures that a book is checked
                if (mMondayCheckBox.isChecked() || mTuesdayCheckBox.isChecked() || mWednesdayCheckBox.isChecked() ||
                        mThursdayCheckBox.isChecked() || mFridayCheckBox.isChecked() || mSaturdayCheckBox.isChecked()) {
                    somethingChecked = true;
                }

                boolean isDayThere = true; //indicates if the day is checked and

                if (mMondayCheckBox.isChecked()) { //if Monday is chosen
                    isDayThere = false; //start with dayIsThere being false

                    //go through course's meeting days to see if it's there
                    for (int j = 0; j < currentCourseMeetingDays.size(); j++) {

                        //if it's there
                        if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.MONDAY)) {
                            isDayThere = true; //say isDayThere to true
                            break;
                        }
                    }
                }
                if (!isDayThere) {
                    remove = true;
                }

                if (!remove) { //if course is not already removed
                    if (mTuesdayCheckBox.isChecked()) { //if Tuesday is chosen
                        isDayThere = false; //start with dayIsThere being false

                        //go through course's meeting days to see if it's there
                        for (int j = 0; j < currentCourseMeetingDays.size(); j++) {
                            if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.TUESDAY)) {
                                isDayThere = true; //say isDayThere to true
                                break;
                            }
                        }
                    }
                    if (!isDayThere) {
                        remove = true;
                    }
                }

                if (!remove) { //if course is not already removed
                    if (mWednesdayCheckBox.isChecked()) { //if Wednesday is chosen
                        isDayThere = false; //start with dayIsThere being false

                        //go through course's meeting days to see if it's there
                        for (int j = 0; j < currentCourseMeetingDays.size(); j++) {
                            if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.WEDNESDAY)) {
                                isDayThere = true; //say isDayThere to true
                                break;
                            }
                        }
                    }
                    if (!isDayThere) {
                        remove = true;
                    }
                }

                if (!remove) { //if course is not already removed
                    if (mThursdayCheckBox.isChecked()) { //if Thursday is chosen
                        isDayThere = false; //start with dayIsThere being false

                        //go through course's meeting days to see if it's there
                        for (int j = 0; j < currentCourseMeetingDays.size(); j++) {
                            if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.THURSDAY)) {
                                isDayThere = true; //say isDayThere to true
                                break;
                            }
                        }
                    }
                    if (!isDayThere) {
                        remove = true;
                    }
                }

                if (!remove) { //if course is not already removed
                    if (mFridayCheckBox.isChecked()) { //if Friday is chosen
                        isDayThere = false; //start with dayIsThere being false

                        //go through course's meeting days to see if it's there
                        for (int j = 0; j < currentCourseMeetingDays.size(); j++) {
                            if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.FRIDAY)) {
                                isDayThere = true; //say isDayThere to true
                                break;
                            }
                        }
                    }
                    if (!isDayThere) {
                        remove = true;
                    }
                }

                if (!remove) { //if course is not already removed
                    if (mSaturdayCheckBox.isChecked()) { //if Friday is chosen
                        isDayThere = false; //start with dayIsThere being false

                        //go through course's meeting days to see if it's there
                        for (int j = 0; j < currentCourseMeetingDays.size(); j++) {
                            if (currentCourseMeetingDays.get(j).equals(DaysOfWeek.SATURDAY)) {
                                isDayThere = true; //say isDayThere to true
                                break;
                            }
                        }
                    }
                    if (!isDayThere) {
                        remove = true;
                    }
                }
            }

            if (remove) { //remove course from array if needed
                selectedCourses.remove(i);
                i--;
            }
        }

        if (somethingChecked){ //if a field is filled out
            return selectedCourses; //return array of selected courses
        }
        else{
            //Show toast with warning
            Toast toast = Toast.makeText(this, R.string.field_warning, Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }

    //Set up spinners and adapters
    private void createSpinners() {

        mTimeSpinner.setOnItemSelectedListener(this);
        mMajorSpinner.setOnItemSelectedListener(this);


        List<String> times =orderTimes();

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(timesAdapter);

        ArrayAdapter<CharSequence> majorAdapter = ArrayAdapter.createFromResource(this, R.array.major_array, android.R.layout.simple_spinner_item);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMajorSpinner.setAdapter(majorAdapter);

    }

    //Inflate checkboxes
    private void createCheckboxes(){
        mMondayCheckBox = (CheckBox) findViewById(R.id.monday_checkbox);
        mTuesdayCheckBox = (CheckBox) findViewById(R.id.tuesday_checkbox);
        mWednesdayCheckBox = (CheckBox) findViewById(R.id.wednesday_checkbox);
        mThursdayCheckBox = (CheckBox) findViewById(R.id.thursday_checkbox);
        mFridayCheckBox = (CheckBox) findViewById(R.id.friday_checkbox);
        mSaturdayCheckBox = (CheckBox) findViewById(R.id.saturday_checkbox);
    }

    //Needed function for checkboxes
    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()){
            case R.id.monday_checkbox:
                if(checked){

                }
                else{

                }
            case R.id.tuesday_checkbox:
                if(checked){

                }
                else{

                }
            case R.id.wednesday_checkbox:
                if(checked){

                }
                else{

                }
            case R.id.thursday_checkbox:
                if(checked){

                }
                else{

                }
            case R.id.friday_checkbox:
                if(checked){

                }
                else{

                }
            case R.id.saturday_checkbox:
                if(checked){

                }
                else{

                }

        }
    }

    //Needed function for checkbox
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    //Needed function for checkbox
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Get times and put them in order
    private List<String> orderTimes(){

        List<String> times = new ArrayList<String>();
        times.add(getResources().getString(R.string.none_selected_spinner));


        ArrayList<Course> courses = CourseLoader.get(this).getCourses();
        ArrayList<Course> timeCourses = new ArrayList<Course>();


        for (int i = 0; i < courses.size(); i++) {
            boolean alreadyInArea = false;

            if(timeCourses.size() == 0){
                timeCourses.add(courses.get(i));
                alreadyInArea = true;
            }

            for (int j = 0; j < timeCourses.size(); j++) {
                if (courses.get(i).getMeetingInfo().timeToString().equals(timeCourses.get(j).getMeetingInfo().timeToString())) {
                    alreadyInArea = true;
                }
            }

            if (!alreadyInArea && !(courses.get(i).getMeetingInfo().timeToString().equals("null-null"))) {
                timeCourses.add(courses.get(i));
            }
        }

        for (int i = 0; i < timeCourses.size()-1; i++)
        {
            int min_idx = i;
            for (int j = i+1; j < timeCourses.size(); j++){
                if (timeCourses.get(j).getMeetingInfo().getStartTime().compare(timeCourses.get(min_idx).getMeetingInfo().getStartTime()) == -1)
                {
                    min_idx = j;
                }
                else if(timeCourses.get(j).getMeetingInfo().getStartTime().compare(timeCourses.get(min_idx).getMeetingInfo().getStartTime()) == 0){
                    if(timeCourses.get(j).getMeetingInfo().getEndTime().compare(timeCourses.get(min_idx).getMeetingInfo().getEndTime()) == -1){
                        min_idx = j;
                    }
                }
            }

            // Swap the found minimum element with the first
            // element
            Course temp = timeCourses.get(min_idx);
            timeCourses.set(min_idx, timeCourses.get(i));
            timeCourses.set(i, temp);
        }

        for(int i = 0; i < timeCourses.size(); i++){
            times.add(timeCourses.get(i).getMeetingInfo().timeToString());
        }

        return times;
    }

}