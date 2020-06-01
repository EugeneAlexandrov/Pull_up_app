package com.mybclym.pull_upapp.database;

import android.os.CountDownTimer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timers")
public class TimerEntry {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private int time;
    private int repeats;

    @Ignore
    public TimerEntry(int time, int repeats) {
        this.time = time;
        this.repeats = repeats;

    }

    public TimerEntry(int id, int time, int repeats) {
        this.id = id;
        this.time = time;
        this.repeats = repeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

}
