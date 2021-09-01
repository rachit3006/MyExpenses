package com.example.android.myexpenses;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myexpenses.data.Expense;
import com.example.android.myexpenses.data.ExpenseViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class AnalysisExpenseFragment extends Fragment {

    private ExpenseViewModel viewModel;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private GraphView graphView;
    private DateFormat dateFormat;
    private LineGraphSeries<DataPoint> series;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.analysis_expenses, container, false);
        requireActivity().setTitle("Analysis");
        viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        graphView = rootView.findViewById(R.id.weekly_graph);
        startDateEditText = rootView.findViewById(R.id.start_date_picker);
        endDateEditText = rootView.findViewById(R.id.end_date_picker);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        final Calendar myCalendarStart = Calendar.getInstance();

        LocalDate localDateStart = LocalDateTime.ofInstant(myCalendarStart.toInstant(), myCalendarStart.getTimeZone().toZoneId()).toLocalDate();
        ZoneId zoneId = ZoneId.systemDefault();
        long epoch_start = localDateStart.atStartOfDay(zoneId).toEpochSecond();

        String myFormatStart = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdfStart = new SimpleDateFormat(myFormatStart, Locale.getDefault());
        startDateEditText.setText(sdfStart.format(myCalendarStart.getTime()));

        final Calendar myCalendarEnd = Calendar.getInstance();

        LocalDate localDateEnd = LocalDateTime.ofInstant(myCalendarEnd.toInstant(), myCalendarEnd.getTimeZone().toZoneId()).toLocalDate();
        long epoch_end = localDateEnd.atStartOfDay(zoneId).toEpochSecond();

        String myFormatEnd = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdfEnd = new SimpleDateFormat(myFormatEnd, Locale.getDefault());
        endDateEditText.setText(sdfEnd.format(myCalendarEnd.getTime()));

        viewModel.setStartEndFilter(epoch_start,epoch_end);

        DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormatStart = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdfStart = new SimpleDateFormat(myFormatStart, Locale.getDefault());
                startDateEditText.setText(sdfStart.format(myCalendarStart.getTime()));

                LocalDate localDateStart = LocalDateTime.ofInstant(myCalendarStart.toInstant(), myCalendarStart.getTimeZone().toZoneId()).toLocalDate();
                ZoneId zoneId = ZoneId.systemDefault();
                long epoch_start = localDateStart.atStartOfDay(zoneId).toEpochSecond();
                LocalDate localDateEnd = LocalDateTime.ofInstant(myCalendarEnd.toInstant(), myCalendarEnd.getTimeZone().toZoneId()).toLocalDate();
                long epoch_end = localDateEnd.atStartOfDay(zoneId).toEpochSecond();
                viewModel.setStartEndFilter(epoch_start, epoch_end);
            }
        };

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateStart, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormatEnd = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdfEnd = new SimpleDateFormat(myFormatEnd, Locale.getDefault());
                endDateEditText.setText(sdfEnd.format(myCalendarEnd.getTime()));

                LocalDate localDateStart = LocalDateTime.ofInstant(myCalendarStart.toInstant(), myCalendarStart.getTimeZone().toZoneId()).toLocalDate();
                ZoneId zoneId = ZoneId.systemDefault();
                long epoch_start = localDateStart.atStartOfDay(zoneId).toEpochSecond();
                LocalDate localDateEnd = LocalDateTime.ofInstant(myCalendarEnd.toInstant(), myCalendarEnd.getTimeZone().toZoneId()).toLocalDate();
                long epoch_end = localDateEnd.atStartOfDay(zoneId).toEpochSecond();
                viewModel.setStartEndFilter(epoch_start, epoch_end);
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
        viewModel.getAllExpenses().observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                graphView.removeAllSeries();
                HashMap<Long, Double> totalExpenses = new HashMap<>();
                series = new LineGraphSeries<DataPoint>();
                for (int i = 0; i < expenses.size(); i++) {
                    Expense currentExpense = expenses.get(i);
                    Log.v("expense test", currentExpense.getDate() + "");
                    totalExpenses.put(currentExpense.getDate(), 0.0);

                }
                for (int i = 0; i < expenses.size(); i++) {
                    Expense currentExpense = expenses.get(i);
                    totalExpenses.put(currentExpense.getDate(), totalExpenses.get(currentExpense.getDate()) + currentExpense.getAmount());
                }
                int totalEntries = totalExpenses.keySet().size();
                SortedSet<Long> keys = new TreeSet<>(totalExpenses.keySet());
                for (Long date : keys) {
                    series.appendData(new DataPoint(date, totalExpenses.get(date)), true, totalEntries);
                }
                graphView.addSeries(series);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(16);
                series.setColor(Color.GREEN);

                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        String message = "Date : " + dateFormat.format(new Date((long) dataPoint.getX() * 1000)) + "\n" + "Total Expenses : " + BigDecimal.valueOf(dataPoint.getY()).toPlainString();
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });

                graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                graphView.getViewport().setScalable(true);
                graphView.getViewport().setScrollable(true);
                graphView.getViewport().setScalableY(true);
                graphView.getViewport().setScrollableY(true);
            }
        });
    }
}
