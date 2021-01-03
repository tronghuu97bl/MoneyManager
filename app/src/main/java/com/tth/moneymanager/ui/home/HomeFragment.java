package com.tth.moneymanager.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Dialog.AddTransactionDialog;
import com.tth.moneymanager.LoginActivity;
import com.tth.moneymanager.Model.Shopping;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.R;
import com.tth.moneymanager.Util;
import com.tth.moneymanager.adapter.TransactionAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tv_welcome, tv_amount, tv_debt;
    private BarChart barChart;
    private LineChart lineChart;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private TransactionAdapter adapter;
    private List<Transaction> listTransaction;
    private DbHelper db;
    private Util util;
    private User currentUser;
    private GetProfit getProfit;
    private GetSpending getSpending;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
        listTransaction = new ArrayList<>();
        homeViewModel.getTextWelcome().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv_welcome.setText(s);
            }
        });
        homeViewModel.getTextAmount(getContext(), currentUser.getId()).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv_amount.setText(s + " $");
                adapter.notifyDataSetChanged();
            }
        });
        homeViewModel.getTextDebt(getContext(), currentUser.getId()).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv_debt.setText(s + " $");
                adapter.notifyDataSetChanged();
            }
        });
        homeViewModel.getListTransaction(getContext(), currentUser.getId()).observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                listTransaction.addAll(transactions);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new TransactionAdapter(listTransaction);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setHasOptionsMenu(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTransactionDialog addTransactionDialog = new AddTransactionDialog();
                addTransactionDialog.show(getParentFragmentManager(), "add transaction dialog");
            }

        });
        getProfit.execute();
        getSpending.execute();
        return root;
    }

    public void initView(View view) {
        tv_welcome = view.findViewById(R.id.home_tv_welcome);
        tv_amount = view.findViewById(R.id.home_tv_amount);
        tv_debt = view.findViewById(R.id.home_tv_debt);
        barChart = view.findViewById(R.id.dailySpentChart);
        lineChart = view.findViewById(R.id.profitChar);
        recyclerView = view.findViewById(R.id.home_rv_transaction);
        floatingActionButton = view.findViewById(R.id.floating_add);
        db = new DbHelper(getContext());
        getProfit = new GetProfit();
        getSpending = new GetSpending();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_logout, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).
                        setTitle("Log out")
                        .setMessage("You want log out?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                util.signOutUser();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.show();
                break;
            default:
                break;
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    private class GetProfit extends AsyncTask<Void, Void, List<Transaction>> {
        private List<Transaction> transactions = new ArrayList<>();

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            transactions = db.getListTransaction("profit", currentUser.getId());
            return transactions;
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            super.onPostExecute(transactions);
            if (transactions != null) {
                ArrayList<Entry> entries = new ArrayList<>();
                for (Transaction t : transactions) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDate());
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        calendar.setTime(date);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        if (calendar.get(Calendar.YEAR) == year) {
                            boolean doesMonthExist = false;
                            for (Entry e : entries) {
                                if (e.getX() == month) {
                                    doesMonthExist = true;
                                } else {
                                    doesMonthExist = false;
                                }
                            }
                            if (!doesMonthExist) {
                                entries.add(new Entry(month, (float) t.getAmount()));
                            } else {
                                for (Entry e : entries) {
                                    if (e.getX() == month) {
                                        e.setY(e.getY() + (float) t.getAmount());
                                    }
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                /*for (Entry e : entries) {
                    Log.d("test", "onPostExecute: x: " + e.getX() + " y: " + e.getY());
                }*/
                LineDataSet dataSet = new LineDataSet(entries, "Profit chart");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(Color.GREEN);
                LineData data = new LineData(dataSet);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setSpaceMin(1);
                xAxis.setSpaceMax(1);
                xAxis.setAxisMaximum(12);
                xAxis.setEnabled(false);
                YAxis yAxis = lineChart.getAxisRight();
                yAxis.setEnabled(false);
                YAxis leftAxis = lineChart.getAxisLeft();
                //leftAxis.setAxisMaximum(100);
                leftAxis.setAxisMinimum(0);
                leftAxis.setDrawGridLines(false);
                lineChart.setDescription(null);
                lineChart.animateY(1000);
                lineChart.setData(data);
                lineChart.invalidate();
            }
        }
    }

    private class GetSpending extends AsyncTask<Void, Void, List<Shopping>> {
        private List<Shopping> shoppings = new ArrayList<>();

        @Override
        protected List<Shopping> doInBackground(Void... voids) {
            shoppings = db.getListShopping_DatePrice(currentUser.getId());
            return shoppings;
        }

        @Override
        protected void onPostExecute(List<Shopping> shoppings) {
            super.onPostExecute(shoppings);
            if (null != shoppings) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (Shopping s : shoppings) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(s.getDate());
                        Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH) + 1;
                        calendar.setTime(date);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        if (calendar.get(Calendar.MONTH) + 1 == month) {
                            boolean doesDayExist = false;
                            for (BarEntry e : entries) {
                                if (e.getX() == day) {
                                    doesDayExist = true;
                                } else {
                                    doesDayExist = false;
                                }
                            }
                            if (!doesDayExist) {
                                entries.add(new BarEntry(day, (float) s.getPrice()));
                            } else {
                                for (BarEntry e : entries) {
                                    if (e.getX() == day) {
                                        e.setY(e.getY() + (float) +s.getPrice());
                                    }
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for (BarEntry e : entries) {
                    Log.d("test", "onPostExecute: x: " + e.getX() + " y: " + e.getY());
                }
                BarDataSet dataSet = new BarDataSet(entries, "Shopping chart");
                dataSet.setColor(Color.RED);
                BarData data = new BarData(dataSet);
                barChart.getAxisRight().setEnabled(false);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setAxisMinimum(1);
                xAxis.setAxisMaximum(31);
                xAxis.setEnabled(false);
                YAxis yAxis = barChart.getAxisLeft();
                //yAxis.setAxisMaximum(40);
                yAxis.setAxisMinimum(0);
                yAxis.setDrawGridLines(false);
                barChart.setData(data);
                barChart.setDescription(null);
                barChart.invalidate();
            }
        }
    }
}