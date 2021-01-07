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
import com.tth.moneymanager.Model.Loan;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AddLoanActivity extends AppCompatActivity {

    private TextView warning;
    private Button btAdd, btPickInitDate, btPickFinishDate;
    private EditText editName, editAmount, editROI, editMonthlyPayment, editInitDate, editFinishDate;
    private Calendar initCalendar = Calendar.getInstance();
    private Calendar finishCalendar = Calendar.getInstance();
    private Util util;
    private User currentUser;
    private DbHelper db;
    private String name, initDate, finishDate;
    private Double amount, payment, roi;
    private AddLoan addLoan;
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
            finishCalendar.set(Calendar.YEAR, i);
            finishCalendar.set(Calendar.MONTH, i1);
            finishCalendar.set(Calendar.DAY_OF_MONTH, i2);
            editFinishDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(finishCalendar.getTime()));
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);
        try {
            intiView();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        setOnClickListener();
    }

    public void intiView() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        warning = findViewById(R.id.tv_add_loan_warning);
        editAmount = findViewById(R.id.edit_add_loan_initial_amount);
        editFinishDate = findViewById(R.id.edit_add_loan_finish_date);
        editInitDate = findViewById(R.id.edit_add_loan_init_date);
        editMonthlyPayment = findViewById(R.id.edit_add_loan_monthly_payment);
        editName = findViewById(R.id.edit_add_loan_name);
        editROI = findViewById(R.id.edit_add_loan_monthly_roi);
        btAdd = findViewById(R.id.button_add_loan_add);
        btPickInitDate = findViewById(R.id.button_add_loan_pick_init_date);
        btPickFinishDate = findViewById(R.id.button_add_loan_pick_finish_date);
        util = new Util(this);
        db = new DbHelper(this);
        currentUser = util.userAuthenLogin();
        addLoan = new AddLoan();
    }

    public boolean validationInput() {
        if (editROI.getText().toString().equals("") || editName.getText().toString().equals("")
                || editMonthlyPayment.getText().toString().equals("") || editInitDate.getText().toString().equals("")
                || editFinishDate.getText().toString().equals("") || editAmount.getText().toString().equals("")) {
            return false;
        } else {
            name = editName.getText().toString();
            initDate = editInitDate.getText().toString();
            finishDate = editFinishDate.getText().toString();
            amount = Double.valueOf(editAmount.getText().toString());
            payment = Double.valueOf(editMonthlyPayment.getText().toString());
            roi = Double.valueOf(editROI.getText().toString());
            return true;
        }
    }

    public void setOnClickListener() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationInput()) {
                    addLoan.execute();
                }
            }
        });
        btPickInitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddLoanActivity.this, initDateSetListener, initCalendar.get(Calendar.YEAR),
                        initCalendar.get(Calendar.MONTH), initCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btPickFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddLoanActivity.this, finishDateSetListener, finishCalendar.get(Calendar.YEAR),
                        finishCalendar.get(Calendar.MONTH), finishCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public class AddLoan extends AsyncTask<Void, Void, Integer> {
        private Transaction transaction = new Transaction();
        private Loan loan = new Loan();
        int loanId;

        @Override
        protected Integer doInBackground(Void... voids) {
            transaction.setAmount(amount);
            transaction.setRecipient(name);
            transaction.setType("loan");
            transaction.setDescription("Received amount for " + name + " Loan");
            transaction.setDate(initDate);
            transaction.setUser_id(currentUser.getId());
            int tranID = (int) db.addTransaction(transaction);
            if (tranID > -1) {
                loan.setUser_id(currentUser.getId());
                loan.setTransaction_id(tranID);
                loan.setInit_amount(amount);
                loan.setRemained_amount(amount);
                loan.setMonthly_payment(payment);
                loan.setMonthly_roi(roi);
                loan.setInit_date(initDate);
                loan.setFinish_date(finishDate);
                loan.setName(name);
                loanId = (int) db.addLoan(loan);
                if (loanId > -1) {
                    db.updateRemainedAmount(currentUser.getId(), amount);
                    db.updateRemainedDebt(currentUser.getId(), amount);
                }
            }
            return loanId;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > -1) {
                //worker
                try {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date init = sdf.parse(initDate);
                    calendar.setTime(init);
                    int initMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
                    Date finish = sdf.parse(finishDate);
                    calendar.setTime(finish);
                    int finishMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
                    int distance = finishMonth - initMonth;
                    int days = 0;
                    for (int i = 0; i < distance; i++) {
                        days += 30;
                        Data data = new Data.Builder()
                                .putInt("loan_id", integer)
                                .putInt("user_id", currentUser.getId())
                                .putDouble("monthly_payment", payment)
                                .putDouble("roi", roi)
                                .putString("name", name)
                                .build();
                        Constraints constraints = new Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build();

                        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoanWorker.class)
                                .setInputData(data)
                                .setConstraints(constraints)
                                .setInitialDelay(days, TimeUnit.SECONDS)
                                .addTag("loan_payment")
                                .build();
                        WorkManager.getInstance(AddLoanActivity.this).enqueue(request);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //back to mainActivity
                Intent intent = new Intent(AddLoanActivity.this, MainActivity.class);
                intent.putExtra("result", 3);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}