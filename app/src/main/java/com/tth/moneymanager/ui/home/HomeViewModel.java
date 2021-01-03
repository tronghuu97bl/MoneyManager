package com.tth.moneymanager.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Shopping;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private DbHelper db;
    private MutableLiveData<String> tv_welcome, tv_amount, tv_debt;
    private MutableLiveData<List<Transaction>> transactions;
    private MutableLiveData<List<Shopping>> listShopping;

    public HomeViewModel() {
        tv_welcome = new MutableLiveData<>();
        tv_amount = new MutableLiveData<>();
        tv_debt = new MutableLiveData<>();
        transactions = new MutableLiveData<>();
        listShopping = new MutableLiveData<>();
    }

    public LiveData<String> getTextWelcome() {
        tv_welcome.setValue("Welcome to Money Manager");
        return tv_welcome;
    }

    public LiveData<String> getTextAmount(Context context, int userId) {
        db = new DbHelper(context);
        User user = db.getUserById(userId);
        tv_amount.setValue(String.valueOf(user.getRemained_amount()));
        return tv_amount;
    }

    public LiveData<String> getTextDebt(Context context, int userId) {
        db = new DbHelper(context);
        User user = db.getUserById(userId);
        tv_debt.setValue(String.valueOf(user.getRemained_debt()));
        return tv_debt;
    }

    public LiveData<List<Transaction>> getListTransaction(Context context, int userId) {
        db = new DbHelper(context);
        transactions.setValue(db.getListTransaction(userId));
        return transactions;
    }
}