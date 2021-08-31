package com.example.android.myexpenses;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myexpenses.data.Expense;
import com.example.android.myexpenses.data.ExpenseViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyExpensesFragment extends Fragment {

    final ExpenseAdapter expenseAdapter = new ExpenseAdapter();
    private EditText dateEditText;
    private Toolbar toolbar;
    public static final int ADD_EXPENSE = 1;
    public static final int EDIT_EXPENSE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_expenses, container, false);
        toolbar = rootView.findViewById(R.id.total_amount);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(requireContext(), R.style.TextAppearance_AppCompat_Medium);
        setHasOptionsMenu(true);
        requireActivity().setTitle("Daily Expenses");

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.floating_button_add_expense);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AddEditExpenseActivity.class);
                startActivityForResult(intent, ADD_EXPENSE);
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.daily_expenses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(expenseAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(rootView.getContext(), 0);
        recyclerView.addItemDecoration(itemDecor);

        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.DeleteExpense(expenseAdapter.getExpense(viewHolder.getAdapterPosition()));
                Toast.makeText(requireActivity(), "Expense Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(requireActivity(), AddEditExpenseActivity.class);
                intent.putExtra(AddEditExpenseActivity.EXTRA_ID, expense.getId());
                intent.putExtra(AddEditExpenseActivity.EXTRA_TO, expense.getReceiver());
                intent.putExtra(AddEditExpenseActivity.EXTRA_AMOUNT, expense.getAmount());
                intent.putExtra(AddEditExpenseActivity.EXTRA_DESCRIPTION, expense.getDescription());
                startActivityForResult(intent, EDIT_EXPENSE);
            }
        });

        dateEditText = rootView.findViewById(R.id.date_picker);
        final Calendar myCalendar = Calendar.getInstance();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateEditText.setText(sdf.format(myCalendar.getTime()));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                dateEditText.setText(sdf.format(myCalendar.getTime()));

                String date = sdf.format(myCalendar.getTime());
                ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
                viewModel.getAllExpenses(date, date).observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
                    @Override
                    public void onChanged(List<Expense> expenses) {
                        expenseAdapter.submitList(expenses);
                        double total = 0;
                        for(Expense currentExpense : expenses){
                            total += currentExpense.getAmount();
                        }
                        toolbar.setTitle("TOTAL : "+BigDecimal.valueOf(total).toPlainString());
                    }
                });
            }
        };

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXPENSE && resultCode == AddEditExpenseActivity.RESULT_OK) {
            assert data != null;
            String to_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_TO);
            String description_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_DESCRIPTION);
            String amount_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_AMOUNT);
            double amount_float = new BigDecimal(amount_string).setScale(2, BigDecimal.ROUND_UP).doubleValue();

            ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

            String date = dateEditText.getText().toString();
            Expense expense = new Expense(amount_float, description_string, to_string, date);
            viewModel.InsertExpense(expense);

            Toast.makeText(requireActivity(), "Expense added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXPENSE && resultCode == AddEditExpenseActivity.RESULT_OK) {
            assert data != null;
            String to_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_TO);
            String description_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_DESCRIPTION);
            String amount_string = data.getStringExtra(AddEditExpenseActivity.EXTRA_AMOUNT);
            int id = data.getIntExtra(AddEditExpenseActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(requireActivity(), "Expense can not be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount_float = new BigDecimal(amount_string).setScale(2, BigDecimal.ROUND_UP).doubleValue();

            ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

            String date = dateEditText.getText().toString();
            Expense expense = new Expense(amount_float, description_string, to_string, date);
            expense.setId(id);
            viewModel.UpdateExpense(expense);

            Toast.makeText(requireActivity(), "Expense successfully edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Expense not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String date = dateEditText.getText().toString();
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        viewModel.getAllExpenses(date, date).observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                expenseAdapter.submitList(expenses);
                double total = 0;
                for(Expense currentExpense : expenses){
                    total += currentExpense.getAmount();
                }
                toolbar.setTitle("TOTAL : "+BigDecimal.valueOf(total).toPlainString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.daily_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        if (item.getItemId() == R.id.delete_all_expenses) {
            String date = dateEditText.getText().toString();
            viewModel.DeleteAllExpenses(date);
            Toast.makeText(requireActivity(), "All Expenses Deleted", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
