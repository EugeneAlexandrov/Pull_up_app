package com.mybclym.pull_upapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM timers ORDER BY id")
    LiveData<List<TimerEntry>> loadAllTasks();

    @Insert
    void insertTask(TimerEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TimerEntry taskEntry);

    @Delete
    void deleteTask(TimerEntry taskEntry);

    @Query("SELECT * FROM timers WHERE id = :id")
    TimerEntry loadTaskById(int id);
}
