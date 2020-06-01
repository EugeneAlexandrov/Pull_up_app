package com.mybclym.pull_upapp;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.room.Room;

import com.mybclym.pull_upapp.database.TimerEntry;

import java.util.List;

public class MyTimer {

    private static final Object LOCK = new Object();
    static private List<TimerEntry> mTimerList;
    private static MyTimer myTimerInstance;

    private CountDownTimer newTimer;

    public TickListener mTickListener;

    int timerEntry_count;


    interface TickListener {
        void onTick(String millis);

        void onFinishAllTimers();
    }

    public static MyTimer getInstance(List<TimerEntry> list) {
        if (myTimerInstance == null) {
            synchronized (LOCK) {
                Log.d("TEST", "Creating new Mytimer instance");
                myTimerInstance = new MyTimer();
            }
        }
        Log.d("TEST", "Getting Mytimer instance");
        mTimerList = list;
        return myTimerInstance;
    }

    public void initNewTimer(int time) {
        Log.d("TEST", "Timer: " + timerEntry_count + " init");
        newTimer = new CountDownTimer(time * 1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("TEST", "" + millisUntilFinished);
                double time = Math.ceil(millisUntilFinished / 100.0);
                mTickListener.onTick(String.valueOf(time / 10));
            }

            @Override
            public void onFinish() {
                startNext();
            }
        };
    }

    public void setTickListener(TickListener listener) {
        this.mTickListener = listener;
    }

    ;

    public void startNext() {
        if (++timerEntry_count < mTimerList.size()) {

            initNewTimer(mTimerList.get(timerEntry_count).getTime());
            newTimer.start();
        } else {
            mTickListener.onFinishAllTimers();
            Log.d("TEST", "all Timers finished");
        }

    }

    public void startFirst() {
        if (mTimerList != null) {
            timerEntry_count = 0;
            initNewTimer(mTimerList.get(timerEntry_count).getTime());
            newTimer.start();
        }
    }

    public void stop() {
        newTimer.cancel();
    }
}
