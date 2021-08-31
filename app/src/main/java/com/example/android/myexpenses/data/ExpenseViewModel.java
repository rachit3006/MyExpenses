package com.example.android.myexpenses.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
    }

    public LiveData<List<Expense>> getAllExpenses(long start_date, long end_date){
        return repository.getAllExpenses(start_date, end_date);
    }

    public void InsertExpense(Expense expense){
        repository.InsertExpense(expense);
    }

    public void UpdateExpense(Expense expense){
        repository.UpdateExpense(expense);
    }

    public void DeleteExpense(Expense expense){
        repository.DeleteExpense(expense);
    }

    public void DeleteAllExpenses(long date){
        repository.DeleteAllExpenses(date);
    }
}
