//Created by: Mike Fishler

package android.mikefishler.DeScheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.mikefishler.DeScheduler.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.UUID;

public class ScheduleOptionsFragmentDialog extends DialogFragment {

    //Extra for passing in a schedule id into the dialog box
    public static final String EXTRA_SCHEDULE_ID = "scheduleOptionsFragmentDialog.schedule_id";

    //Generates a new instance of the dialog box
    public static ScheduleOptionsFragmentDialog newInstance(UUID scheduleId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SCHEDULE_ID, scheduleId); //puts the needed id into a bundle

        ScheduleOptionsFragmentDialog fragment = new ScheduleOptionsFragmentDialog();
        fragment.setArguments(args); //puts arguments into fragment object
        return fragment;
    }

    //Creates dialog box
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final UUID mScheduleID = (UUID) getArguments().getSerializable(EXTRA_SCHEDULE_ID); //get id from arguments
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create(); //create dialog box
        dialog.setTitle(R.string.delete_schedule_text); //Set title

        //Set up cancel button
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getText(R.string.cancel_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }

        });

        //Set up delete button
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.delete_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScheduleLab.get(getActivity()).deleteSchedule(ScheduleLab.get(getActivity()).getSchedule(mScheduleID));
                getActivity().finish();
            }

        });


        return dialog;

    }
}


