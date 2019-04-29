package com.example.thetravlendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class ModeOfTransportationFragment extends DialogFragment {
    public interface MODDialogListener {
        void onFinishMODDialog(String mod);
    }
    private int mSelectedItem;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_MOD)
                .setSingleChoiceItems(R.array.MOD_array, 2,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedItem = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*Toast.makeText(getActivity(), "You selected !! \n "
                                + getResources().getStringArray(R.array.MOD_array)[mSelectedItem],
                                Toast.LENGTH_SHORT).show();*/
                        MODDialogListener activity = (MODDialogListener) getActivity();
                        activity.onFinishMODDialog(getResources().
                                getStringArray(R.array.MOD_array)[mSelectedItem]);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "You clicked Cancel \n No Item was selected !!", Toast.LENGTH_SHORT).show();

                    }
                });

        return builder.create();
    }
}