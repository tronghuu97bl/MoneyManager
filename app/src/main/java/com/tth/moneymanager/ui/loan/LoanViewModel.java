package com.tth.moneymanager.ui.loan;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Loan;

import java.util.List;

public class LoanViewModel extends ViewModel {
    private DbHelper db;
    private MutableLiveData<List<Loan>> loans;

    public LoanViewModel() {
        loans = new MutableLiveData<>();
    }

    public LiveData<List<Loan>> getListLoan(Context context, int userId) {
        db = new DbHelper(context);
        loans.setValue(db.getListLoan(userId));
        return loans;
    }
}
