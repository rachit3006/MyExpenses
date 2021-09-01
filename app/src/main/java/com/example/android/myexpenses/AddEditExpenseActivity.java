package com.example.android.myexpenses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Objects;

public class AddEditExpenseActivity extends AppCompatActivity {

    private EditText to, description, amount;
    public static final String EXTRA_ID = "com.example.android.myexpenses.EXTRA_ID";
    public static final String EXTRA_TO = "com.example.android.myexpenses.EXTRA_TO";
    public static final String EXTRA_DESCRIPTION = "com.example.android.myexpenses.EXTRA_DESCRIPTION";
    public static final String EXTRA_AMOUNT = "com.example.android.myexpenses.EXTRA_AMOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        to = findViewById(R.id.to_EditText);
        description = findViewById(R.id.description_EditText);
        amount = findViewById(R.id.amount_EditText);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Expense");
            to.setText(intent.getStringExtra(EXTRA_TO));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            amount.setText(BigDecimal.valueOf(intent.getDoubleExtra(EXTRA_AMOUNT, 0)).toPlainString());
        } else {
            setTitle("Add New Expense");
        }
    }

    private void saveExpense() {
        String to_string = to.getText().toString();
        String description_string = description.getText().toString();
        String amount_float = amount.getText().toString();

        if (to_string.trim().isEmpty() || description_string.trim().isEmpty() || amount_float.trim().isEmpty()) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TO, to_string);
        data.putExtra(EXTRA_AMOUNT, amount_float);
        data.putExtra(EXTRA_DESCRIPTION, description_string);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_expense_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_expense) {
            saveExpense();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}