package com.mybclym.pull_upapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mybclym.pull_upapp.database.AppDataBase;
import com.mybclym.pull_upapp.database.TimerEntry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditTimeEntryFragment extends DialogFragment {

    private static final String ARGS_TIMER_TIME = "Timer_time";
    private static final String ARGS_TIMER_REPEATS = "Timer_repeats";
    public static final String EXTRA_TIMER_DATE = "com.mybclym.pull_upapp.timer";

    AppDataBase mDB;

    TimerEntry timer;

    int mID;
//    protected static AddTimeEntryFragment newInstance(int time, int repeats) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ARGS_CRIME_DATE, date);
//        DatePickerFragment fragment = new DatePickerFragment();
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }


    public EditTimeEntryFragment(int id) {
        mID = id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDB = AppDataBase.getInstance(getContext());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_addtimer, null);
        final EditText et_time = v.findViewById(R.id.dialog_et_time);
        final EditText et_repeats = v.findViewById(R.id.dialog_et_repeats);
        final Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                timer = mDB.taskDao().loadTaskById(mID);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle(R.string.edit_timer);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v);
        et_time.setText(String.valueOf(timer.getTime()));
        et_repeats.setText(String.valueOf(timer.getRepeats()));
        builder.setPositiveButton(R.string.add_timer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int time = Integer.parseInt(et_time.getText().toString());
                int repeats = Integer.parseInt(et_repeats.getText().toString());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDB.taskDao().updateTask(timer);
                    }
                });
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTimeEntryFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
