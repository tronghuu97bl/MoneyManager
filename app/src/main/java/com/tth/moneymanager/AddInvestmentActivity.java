package com.tth.moneymanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Investment;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddInvestmentActivity extends AppCompatActivity {
    private Button btPickInitDate, btPickFinishDate, btAdd;
    private EditText editName, editAmount, editROI, editInitDate, editFinishDate;
    private String name, initDate, finishDate;
    private Double amount, roi;
    private DbHelper db;
    private Calendar initCalendar = Calendar.getInstance();
    private Calendar finishCalendar = Calendar.getInstance();
    private TextView tvWarning;
    private User currentUser;
    private Util util;
    private AddTransaction addTransaction;
    private AddInvestment addInvestment;
    private DatePickerDialog.OnDateSetListener initDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            initCalendar.set(Calendar.YEAR, i);
            initCalendar.set(Calendar.MONTH, i1);
            initCalendar.set(Calendar.DAY_OF_MONTH, i2);
            editInitDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(initCalendar.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener finishDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            initCalendar.set(Calendar.YEAR, i);
            initCalendar.set(Calendar.MONTH, i1);
            initCalendar.set(Calendar.DAY_OF_MONTH, i2);
            editFinishDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(initCalendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investment);
        initView();
        util = new Util(this);
        currentUser = util.userAuthenLogin();

        btPickInitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddInvestmentActivity.this, initDateSetListener,
                        initCalendar.get(Calendar.YEAR), initCalendar.get(Calendar.MONTH), initCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btPickFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddInvestmentActivity.this, finishDateSetListener,
                        finishCalendar.get(Calendar.YEAR), finishCalendar.get(Calendar.MONTH), finishCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationInput()) {
                    tvWarning.setVisibility(View.GONE);
                    addTransaction.execute();
                } else {
                    tvWarning.setVisibility(View.VISIBLE);
                    tvWarning.setText("Please enter input investment!!!");
                }
            }
        });
    }

    public void initView() {
        btAdd = findViewById(R.id.button_add_invest_add);
        btPickInitDate = findViewById(R.id.button_add_invest_pick_init_date);
        btPickFinishDate = findViewById(R.id.button_add_invest_pick_finish_date);
        editName = findViewById(R.id.edit_add_invest_name);
        editAmount = findViewById(R.id.edit_add_invest_amount);
        editROI = findViewById(R.id.edit_add_invest_roi);
        editInitDate = findViewById(R.id.edit_add_invest_init_date);
        editFinishDate = findViewById(R.id.edit_add_invest_finish_date);
        tvWarning = findViewById(R.id.tv_add_invest_warning);
        db = new DbHelper(this);
        addInvestment = new AddInvestment();
        addTransaction = new AddTransaction();
    }

    public boolean validationInput() {
        //return true if input ok
        if (editROI.getText().toString().equals("") || editFinishDate.getText().toString().equals("") || editInitDate.getText().toString().equals("") ||
                editAmount.getText().toString().equals("") || editName.getText().toString().equals("")) {
            return false;
        } else {
            name = editName.getText().toString();
            initDate = editInitDate.getText().toString();
            finishDate = editFinishDate.getText().toString();
            amount = -Double.valueOf(editAmount.getText().toString());
            roi = Double.valueOf(editROI.getText().toString());
            return true;
        }
    }

    public class AddTransaction extends AsyncTask<Void, Void, Integer> {
        private Transaction transaction;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setUser_id(currentUser.getId());
            transaction.setDate(initDate);
            transaction.setDescription("Initial amount for " + name + " investment.");
            transaction.setType("investment");
            transaction.setRecipient(name);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return (int) db.addTransaction(transaction);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0) {
                //update current amount
                db.updateRemainedAmount(currentUser.getId(), amount);
                //add investment
                addInvestment.execute(integer);
            }
        }
    }

    public class AddInvestment extends AsyncTask<Integer, Void, Integer> {
        private Investment investment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            investment = new Investment();
            investment.setName(name);
            investment.setInit_date(initDate);
            investment.setFinish_date(finishDate);
            investment.setAmount(amount);
            investment.setMonthly_roi(roi);
            investment.setUser_id(currentUser.getId());
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            investment.setTransaction_id(integers[0]);
            return (int) db.addInvestment(investment);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date init = sdf.parse(initDate);
                    calendar.setTime(init);
                    int initMonths = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
                    Date finish = sdf.parse(finishDate);
                    calendar.setTime(finish);
                    int finishMonths = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
                    int distance = finishMonths - initMonths;
                    int days = 0;
                    for (int i = 0; i < distance; i++) {
                        days += 30;
                        Data data = new Data.Builder()
                                //data use to add new Transaction
                                .putDouble("amount", roi)
                                .putString("description", "Profit for " + name)
                                .putInt("user_id", currentUser.getId())
                                .putString("recipient", name)
                                .build();

                        Constraints constraints = new Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build();

                        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(InvestmentWorker.class)
                                .setInputData(data)
                                .setConstraints(constraints)
                                .setInitialDelay(days, TimeUnit.SECONDS)
                                .addTag("profit")
                                .build();
                        WorkManager.getInstance(AddInvestmentActivity.this).enqueue(request);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //back mainActivity
                Intent intent = new Intent(AddInvestmentActivity.this, MainActivity.class);
                intent.putExtra("result", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addTransaction != null) {
            if (addTransaction.isCancelled()) {
                addTransaction.cancel(true);
            }
        }
        if (addInvestment != null) {
            if (addInvestment.isCancelled()) {
                addInvestment.cancel(true);
            }
        }
    }
}