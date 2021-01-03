package com.tth.moneymanager;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InvestmentWorker extends Worker {
    private DbHelper db;

    public InvestmentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = new DbHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("test", "doWork: called");

        Data data = getInputData();
        //get data to add profit transaction of investment
        Transaction transaction = new Transaction();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        double amount = data.getDouble("amount", 0.0);
        int userId = data.getInt("user_id", -1);
        transaction.setAmount(amount);
        transaction.setUser_id(userId);
        transaction.setRecipient(data.getString("recipient"));
        transaction.setType("profit");
        transaction.setDescription(data.getString("description"));
        transaction.setDate(date);

        try {
            long id = db.addTransaction(transaction);
            if (id != -1) {
                db.updateRemainedAmount(userId, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success();
    }
}
