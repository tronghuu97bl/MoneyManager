package com.tth.moneymanager.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> listTransaction = new ArrayList<>();

    public TransactionAdapter(List<Transaction> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public TransactionAdapter() {
    }

    public void setTransactions(List<Transaction> listTransaction) {
        this.listTransaction = listTransaction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double amount_temp = listTransaction.get(position).getAmount();
        if (amount_temp > 0) {
            holder.amount.setText("+" + String.valueOf(amount_temp));
            holder.amount.setTextColor(Color.GREEN);
        } else {
            holder.amount.setText(String.valueOf(amount_temp));
            holder.amount.setTextColor(Color.RED);
        }
        holder.des.setText(listTransaction.get(position).getDescription());
        holder.date.setText(listTransaction.get(position).getDate());
        holder.type.setText(listTransaction.get(position).getType());
        holder.receipt.setText(listTransaction.get(position).getRecipient());
        //holder.id.setText(String.valueOf(listTransaction.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        if (listTransaction != null) {
            return listTransaction.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView amount, des, date, type, receipt, id;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewTran);
            amount = itemView.findViewById(R.id.item_tran_amount);
            des = itemView.findViewById(R.id.item_tran_des);
            type = itemView.findViewById(R.id.item_tran_type);
            date = itemView.findViewById(R.id.item_tran_date);
            receipt = itemView.findViewById(R.id.item_tran_receipt);
            //id = itemView.findViewById(R.id.item_tran_id);
        }
    }
}
