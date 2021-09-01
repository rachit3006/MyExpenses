package com.example.android.myexpenses.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.Objects;

public class ExpenseViewModel extends AndroidViewModel {
    private final LiveData<List<Expense>> allExpenses;
    private final MutableLiveData<StartEndFilter> startEndFilter;
    private final ExpenseRepository repository;

    static class StartEndFilter {
        final long start;
        final long end;

        StartEndFilter(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

    public void setStartEndFilter(long start, long end) {
        StartEndFilter update = new StartEndFilter(start, end);
        if (Objects.equals(startEndFilter.getValue(), update)) {
            return;
        }
        startEndFilter.setValue(update);
    }

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        startEndFilter = new MutableLiveData<>();
        allExpenses = Transformations.switchMap(startEndFilter, input -> repository.getAllExpenses(input.start, input.end));
    }

    public LiveData<List<Expense>> getAllExpenses(){
        return allExpenses;
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