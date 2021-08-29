package com.example.android.myexpenses;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myexpenses.data.Expense;
import com.example.android.myexpenses.data.ExpenseViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WeeklyExpenseFragment extends Fragment {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weekly_expenses, container, false);

        requireActivity().setTitle("Weekly expenses");

        graphView = rootView.findViewById(R.id.weekly_graph);

        startDateEditText = rootView.findViewById(R.id.start_date_picker);
        final Calendar myCalendarStart = Calendar.getInstance();

        String myFormatStart = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdfStart = new SimpleDateFormat(myFormatStart, Locale.getDefault());
        startDateEditText.setText(sdfStart.format(myCalendarStart.getTime()));

        DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormatStart = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdfStart = new SimpleDateFormat(myFormatStart, Locale.getDefault());
                startDateEditText.setText(sdfStart.format(myCalendarStart.getTime()));

                String date = sdfStart.format(myCalendarStart.getTime());
                setExpenseGraph();
            }
        };

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateStart, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEditText = rootView.findViewById(R.id.end_date_picker);
        final Calendar myCalendarEnd = Calendar.getInstance();

        String myFormatEnd = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdfEnd = new SimpleDateFormat(myFormatEnd, Locale.getDefault());
        endDateEditText.setText(sdfEnd.format(myCalendarEnd.getTime()));

        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormatEnd = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdfEnd = new SimpleDateFormat(myFormatEnd, Locale.getDefault());
                endDateEditText.setText(sdfEnd.format(myCalendarEnd.getTime()));

                String date = sdfEnd.format(myCalendarEnd.getTime());
                setExpenseGraph();
            }
        };

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateEnd, myCalendarEnd.get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setExpenseGraph();
    }

    private void setExpenseGraph() {
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        viewModel.getAllExpenses(startDate, endDate).observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                LineGraphSeries<DataPoint> series;
                HashMap<String, Double> totalExpenses = new HashMap<>();
                series = new LineGraphSeries<DataPoint>();
                for (int i = 0; i < expenses.size(); i++) {
                    Expense currentExpense = expenses.get(i);
                    totalExpenses.put(currentExpense.getDate(), 0.0);
                }
                for (int i = 0; i < expenses.size(); i++) {
                    Expense currentExpense = expenses.get(i);
                    totalExpenses.put(currentExpense.getDate(), totalExpenses.get(currentExpense.getDate()) + currentExpense.getAmount());
                }
                int totalEntries = totalExpenses.keySet().size();
                double index = 0;
                for (String date : totalExpenses.keySet()) {
                    series.appendData(new DataPoint(index, totalExpenses.get(date)), true, totalEntries);
                    Log.v("Index "+index,totalExpenses.get(date)+"");
                    index++;
                }
                graphView.addSeries(series);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(16);
                series.setColor(Color.GREEN);
//                graphView.getViewport().setScalable(true);
//                graphView.getViewport().setScrollable(true);
//                graphView.getViewport().setScalableY(true);
//                graphView.getViewport().setScrollableY(true);
            }
        });
    }
}
