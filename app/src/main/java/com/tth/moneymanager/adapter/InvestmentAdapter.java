package com.tth.moneymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Model.Investment;
import com.tth.moneymanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InvestmentAdapter extends RecyclerView.Adapter<InvestmentAdapter.ViewHolder> {
    private List<Investment> investments = new ArrayList<>();
    private Context context;

    public InvestmentAdapter(List<Investment> investments, Context context) {
        this.investments = investments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(investments.get(position).getName());
        holder.amount.setText(String.valueOf(-investments.get(position).getAmount()));
        holder.initDate.setText(investments.get(position).getInit_date());
        holder.finishDate.setText(investments.get(position).getFinish_date());
        holder.roi.setText(String.valueOf(investments.get(position).getMonthly_roi()));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int initMonth, finishMonth, distance = 0;
        try {
            Date init = sdf.parse(investments.get(position).getInit_date());
            calendar.setTime(init);
            initMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
            Date finish = sdf.parse(investments.get(position).getFinish_date());
            calendar.setTime(finish);
            finishMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
            distance = finishMonth - initMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.profit.setText(String.valueOf(distance * investments.get(position).getMonthly_roi()));
        if (position % 2 == 0) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
        }
    }

    @Override
    public int getItemCount() {
        if (investments.size() > 0) {
            return investments.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, initDate, finishDate, roi, amount, profit;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewInvest);
            name = itemView.findViewById(R.id.tv_name_item_invest);
            initDate = itemView.findViewById(R.id.tv_init_date_item_invest);
            finishDate = itemView.findViewById(R.id.tv_finish_date_item_invest);
            profit = itemView.findViewById(R.id.tv_profit_item_invest);
            roi = itemView.findViewById(R.id.tv_roi_item_invest);
            amount = itemView.findViewById(R.id.tv_amount_item_invest);
        }
    }
}
