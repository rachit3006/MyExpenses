package com.example.android.myexpenses.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class}, version = 2)
public abstract class ExpenseDatabase extends RoomDatabase {
    private final static String DB_NAME = "expenses_db";
    private static ExpenseDatabase instance;
    public abstract ExpenseDao expenseDao();

    public static synchronized ExpenseDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ExpenseDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
