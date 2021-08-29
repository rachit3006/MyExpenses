package com.example.android.myexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myexpenses.data.Expense;

import java.math.BigDecimal;

public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ExpenseHolder> {

    private OnItemClickListener listener;

    protected ExpenseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Expense> DIFF_CALLBACK = new DiffUtil.ItemCallback<Expense>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getAmount() == newItem.getAmount() && oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getReceiver().equals(newItem.getReceiver()) && oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        Expense currentExpense = getItem(position);

        holder.receiver.setText(currentExpense.getReceiver());
        holder.description.setText(currentExpense.getDescription());
        holder.amount.setText(BigDecimal.valueOf(currentExpense.getAmount()).toPlainString());
    }

    public Expense getExpense(int position) {
        return getItem(position);
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        private final TextView receiver;
        private final TextView amount;
        private final TextView description;

        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            receiver = itemView.findViewById(R.id.receiver_textView);
            amount = itemView.findViewById(R.id.amount_textView);
            description = itemView.findViewById(R.id.description_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
