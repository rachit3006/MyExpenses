package com.example.android.myexpenses.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao{
    @Query("SELECT * FROM Expense WHERE date >= :start_date and date <= :end_date ORDER BY amount DESC ;")
    LiveData<List<Expense>> getAllRecords(long start_date, long end_date);

    @Insert
    void Insert(Expense expense);

    @Update
    void Update(Expense expense);

    @Delete
    void Delete(Expense expense);

    @Query("DELETE FROM Expense WHERE date = :date;")
    void DeleteAllRecords(long date);
}
