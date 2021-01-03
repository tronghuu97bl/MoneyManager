package com.tth.moneymanager.ui.stats;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Loan;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.R;
import com.tth.moneymanager.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatsFragment extends Fragment {

    private StatsViewModel statsViewModel;
    private BarChart barChart;
    private PieChart pieChart;
    private DbHelper db;
    private Util util;
    private User currentUser;
    private GetLoans getLoans = new GetLoans();
    private GetTransactions getTransactions = new GetTransactions();
    private List<Loan> loans = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //statsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        barChart = root.findViewById(R.id.barChartActivities);
        pieChart = root.findViewById(R.id.pieChartLoans);
        db = new DbHelper(getContext());
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
        getLoans.execute();
        getTransactions.execute();
        return root;
    }

    private class GetLoans extends AsyncTask<Void, Void, List<Loan>> {
        @Override
        protected List<Loan> doInBackground(Void... voids) {
            loans = db.getListLoan(currentUser.getId());
            return loans;
        }

        @Override
        protected void onPostExecute(List<Loan> loans) {
            super.onPostExecute(loans);
            if (loans != null) {
                if (loans.size() > 0) {
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    double totalLoansAmount = 0.0;
                    double totalRemainedAmount = 0.0;

                    for (Loan l : loans) {
                        totalLoansAmount += l.getInit_amount();
                        totalRemainedAmount += l.getRemained_amount();
                    }

                    entries.add(new PieEntry((float) totalLoansAmount, "Total Loans"));
                    entries.add(new PieEntry((float) 5000, "Remained Loans"));
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    dataSet.setSliceSpace(5f);
                    PieData data = new PieData(dataSet);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.animateY(2000, Easing.EaseInOutCubic);
                    pieChart.setData(data);
                    pieChart.invalidate();
                }
            }
        }
    }

    private class GetTransactions extends AsyncTask<Void, Void, List<Transaction>> {

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            transactions = db.getListTransaction(currentUser.getId());
            return transactions;
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            super.onPostExecute(transactions);
            if (transactions != null) {
                if (transactions.size() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentYear = calendar.get(Calendar.YEAR);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (Transaction t : transactions) {
                        try {
                            Date date = sdf.parse(t.getDate());
                            calendar.setTime(date);
                            int month = calendar.get(Calendar.MONTH);
                            int year = calendar.get(Calendar.YEAR);
                            int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;

                            if (month == currentMonth && year == currentYear) {
                                boolean doesDayExist = false;

                                for (BarEntry e : entries) {
                                    if (e.getX() == day) {
                                        doesDayExist = true;
                                    } else {
                                        doesDayExist = false;
                                    }
                                }

                                if (doesDayExist) {
                                    for (BarEntry e : entries) {
                                        if (e.getX() == day) {
                                            e.setY(e.getY() + (float) t.getAmount());
                                        }
                                    }
                                } else {
                                    entries.add(new BarEntry(day, (float) t.getAmount()));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    /*for (BarEntry e : entries) {
                        Log.d(TAG, "onPostExecute: x: " + e.getX() + " y: " + e.getY());
                    }*/

                    BarDataSet dataSet = new BarDataSet(entries, "Account Activity");
                    dataSet.setColor(Color.GREEN);
                    BarData data = new BarData(dataSet);

                    barChart.getAxisRight().setEnabled(false);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setAxisMaximum(31);
                    xAxis.setEnabled(false);
                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setAxisMinimum(10);
                    yAxis.setDrawGridLines(false);
                    barChart.setData(data);
                    Description description = new Description();
                    description.setText("All of the account transactions");
                    description.setTextSize(12f);
                    barChart.setDescription(description);
                    barChart.invalidate();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getLoans != null) {
            if (getLoans.isCancelled()) {
                getLoans.cancel(true);
            }
        }
        if (getTransactions != null) {
            if (getTransactions.isCancelled()) {
                getTransactions.cancel(true);
            }
        }
    }
}