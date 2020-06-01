package com.mybclym.pull_upapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inspector.StaticInspectionCompanionProvider;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mybclym.pull_upapp.database.AppDataBase;
import com.mybclym.pull_upapp.database.TimerEntry;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements MyTimer.TickListener, CountAdapter.ItemClickListener {

    boolean start_key = false;
    SharedPreferences sp;

    MyTimer timer;

    private RecyclerView mRecyclerView;
    private CountAdapter mAdapter;
    private ImageButton add_button;
    private TextView start_tv, tv_millis;

    private AppDataBase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) start_key = savedInstanceState.getBoolean("start_key");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CountAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        //    DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        //   mRecyclerView.addItemDecoration(decoration);
        mDB = AppDataBase.getInstance(this);

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddTimeEntryFragment();
                newFragment.show(getSupportFragmentManager(), "addtimer");
            }
        });

        tv_millis = findViewById(R.id.tv_millis);


        start_tv = findViewById(R.id.start_tv);
        start_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start_key = !start_key;
                if (start_key) timer.startFirst();
                else timer.stop();
                showStartStopButton(start_key);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView
                                          recyclerView, @NonNull RecyclerView.ViewHolder
                                          viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                Executor ex = Executors.newSingleThreadExecutor();
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        mDB.taskDao().deleteTask(mAdapter.getTimers().get(position));
                    }
                });

            }
        }).

                attachToRecyclerView(mRecyclerView);

        mDB = AppDataBase.getInstance(

                getApplicationContext());

        retrieveDBchanges();
        showStartStopButton(start_key);
    }

    private void retrieveDBchanges() {
        LiveData<List<TimerEntry>> timers = mDB.taskDao().loadAllTasks();
        timers.observe(this, new Observer<List<TimerEntry>>() {
            @Override
            public void onChanged(List<TimerEntry> timerEntryList) {
                mAdapter.setTimerEntries(timerEntryList);
                timer = MyTimer.getInstance(mAdapter.getTimers());
                timer.setTickListener(MainActivity.this);
            }
        });

    }

    @Override
    public void onTick(String seconds) {
        tv_millis.setText(seconds);
    }

    @Override
    public void onFinishAllTimers() {
        tv_millis.setVisibility(View.INVISIBLE);
        start_key = !start_key;
        start_tv.setText("Start");
    }

    @Override
    public void onItemClickListener(int itemId) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        DialogFragment newFragment = new EditTimeEntryFragment(itemId);
        newFragment.show(getSupportFragmentManager(), "editTimer");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) outState.putBoolean("start_key", start_key);
    }

    public void showStartStopButton(boolean key) {
        if (key) {
            tv_millis.setVisibility(View.VISIBLE);
            start_tv.setText("STOP");
        } else {
            tv_millis.setVisibility(View.INVISIBLE);
            start_tv.setText("START");
        }
    }
}
