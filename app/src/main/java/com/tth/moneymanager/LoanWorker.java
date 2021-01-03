package com.tth.moneymanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoanWorker extends Worker {
    private DbHelper db;

    public LoanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = new DbHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        int loanId = data.getInt("loan_id", -1);
        int userId = data.getInt("user_id", -1);
        double monthlyPayment = data.getDouble("monthly_payment", 0.0);
        double monthlyROI = data.getDouble("roi", 0.0);
        String name = data.getString("name");
        if (loanId == -1 || userId == -1 || monthlyPayment == 0.0) {
            return Result.failure();
        }
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(calendar.getTime());
            Transaction transaction = new Transaction();
            transaction.setDate(date);
            transaction.setDescription("Monthly payment + ROI for " + name + " loan.");
            transaction.setType("loan_payment");
            transaction.setRecipient(name);
            transaction.setAmount(-(monthlyPayment + monthlyROI));
            transaction.setUser_id(userId);
            //add new transaction, update amount of user
            db.addTransaction(transaction);
            db.updateRemainedAmount(userId, -(monthlyPayment + monthlyROI));
            db.updateRemainedDebt(userId, -monthlyPayment);
            //update remained_amount of loan.
            db.updateRemainedAmountLoan(loanId, monthlyPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
