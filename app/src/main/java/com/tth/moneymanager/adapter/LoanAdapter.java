package com.tth.moneymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Model.Loan;
import com.tth.moneymanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    private List<Loan> loans = new ArrayList<>();
    private Context context;

    public LoanAdapter(List<Loan> loans, Context context) {
        this.loans = loans;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int initMonth, finishMonth, distance = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy=MM-dd");
        try {
            Date init = sdf.parse(loans.get(position).getInit_date());
            calendar.setTime(init);
            initMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
            Date finish = sdf.parse(loans.get(position).getFinish_date());
            calendar.setTime(finish);
            finishMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
            distance = finishMonth - initMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.name.setText(loans.get(position).getName());
        holder.initDate.setText(loans.get(position).getInit_date());
        holder.finishDate.setText(loans.get(position).getFinish_date());
        holder.roi.setText(String.valueOf(loans.get(position).getMonthly_roi()));
        holder.amount.setText(String.valueOf(loans.get(position).getInit_amount()));
        holder.payment.setText(String.valueOf(loans.get(position).getMonthly_payment()));
        holder.loss.setText(String.valueOf(distance * loans.get(position).getMonthly_roi()));
        holder.remainedAmount.setText(String.valueOf(loans.get(position).getRemained_amount()));

        if (position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
        }
    }

    @Override
    public int getItemCount() {
        if (loans.size() > 0) {
            return loans.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, initDate, finishDate, roi, payment, amount, remainedAmount, loss;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewLoan);
            name = itemView.findViewById(R.id.tv_name_item_loan);
            initDate = itemView.findViewById(R.id.tv_init_date_item_loan);
            finishDate = itemView.findViewById(R.id.tv_finish_date_item_loan);
            roi = itemView.findViewById(R.id.tv_roi_item_loan);
            payment = itemView.findViewById(R.id.tv_payment_item_loan);
            amount = itemView.findViewById(R.id.tv_amount_item_loan);
            remainedAmount = itemView.findViewById(R.id.tv_remained_amount_item_loan);
            loss = itemView.findViewById(R.id.tv_loss_item_loan);
        }
    }
}
