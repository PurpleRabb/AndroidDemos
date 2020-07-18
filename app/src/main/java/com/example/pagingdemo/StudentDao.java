package com.example.pagingdemo;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student... students);

    @Query("DELETE FROM student_table")
    void clear();

    @Query("SELECT * FROM student_table ORDER BY id")
    DataSource.Factory<Integer,Student> getAllStudents(); //Page需要DataSource，不是livedata
}
