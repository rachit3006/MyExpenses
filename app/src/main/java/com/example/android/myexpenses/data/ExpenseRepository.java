package com.example.android.myexpenses.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseRepository {
    private final ExpenseDao expenseDao;

    public ExpenseRepository(Application application) {
        ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(application);
        expenseDao = expenseDatabase.expenseDao();
    }

    public LiveData<List<Expense>> getAllExpenses(long start_date, long end_date) {
        return expenseDao.getAllRecords(start_date, end_date);
    }

    public void InsertExpense(Expense expense) {
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void UpdateExpense(Expense expense) {
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void DeleteExpense(Expense expense) {
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void DeleteAllExpenses(long date) {
        new DeleteAllExpensesAsyncTask(expenseDao).execute(date);
    }

    private static class InsertExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private final ExpenseDao expenseDao;

        private InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.Insert(expenses[0]);
            return null;
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private final ExpenseDao expenseDao;

        private UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.Update(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private final ExpenseDao expenseDao;

        private DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.Delete(expenses[0]);
            return null;
        }
    }

    private static class DeleteAllExpensesAsyncTask extends AsyncTask<Long, Void, Void> {
        private final ExpenseDao expenseDao;

        private DeleteAllExpensesAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            expenseDao.DeleteAllRecords(longs[0]);
            return null;
        }
    }
}