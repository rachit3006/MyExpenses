package com.example.android.myexpenses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myexpenses.data.Expense;
import com.example.android.myexpenses.data.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DailyExpensesFragment extends Fragment {

    final ExpenseAdapter expenseAdapter = new ExpenseAdapter();
    private long epoch;
    private ExpenseViewModel viewModel;
    private EditText dateEditText;
    private Toolbar toolbar;
    private DateFormat dateFormat;
    public static final int ADD_EXPENSE = 1;
    public static final int EDIT_EXPENSE = 2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_expenses, container, false);
        toolbar = rootView.findViewById(R.id.total_amount);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(requireContext(), R.style.TextAppearance_AppCompat_Medium);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

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
        Calendar myCalendar = Calendar.getInstance();

        LocalDate localDate = LocalDateTime.ofInstant(myCalendar.toInstant(), myCalendar.getTimeZone().toZoneId()).toLocalDate();
        ZoneId zoneId = ZoneId.systemDefault();
        epoch = localDate.atStartOfDay(zoneId).toEpochSecond();

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateEditText.setText(sdf.format(myCalendar.getTime()));

        Log.v("date test", epoch+"");
        viewModel.setStartEndFilter(epoch, epoch);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                dateEditText.setText(sdf.format(myCalendar.getTime()));

                LocalDate localDate = LocalDateTime.ofInstant(myCalendar.toInstant(), myCalendar.getTimeZone().toZoneId()).toLocalDate();
                ZoneId zoneId = ZoneId.systemDefault();
                epoch = localDate.atStartOfDay(zoneId).toEpochSecond();

                viewModel.setStartEndFilter(epoch, epoch);
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

            Expense expense = new Expense(amount_float, description_string, to_string, epoch);
            viewModel.InsertExpense(expense);

            Toast.makeText(requireActivity(), "Expense added", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_EXPENSE && resultCode == AddEditExpenseActivity.RESULT_OK) {
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

            Expense expense = new Expense(amount_float, description_string, to_string, epoch);
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
        viewModel.getAllExpenses().observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
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
        if (item.getItemId() == R.id.delete_all_expenses) {
            String date = dateEditText.getText().toString();
            Toast.makeText(requireActivity(), "All Expenses Deleted", Toast.LENGTH_SHORT).show();
            viewModel.DeleteAllExpenses(epoch);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
