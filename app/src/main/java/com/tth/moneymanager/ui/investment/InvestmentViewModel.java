package com.tth.moneymanager.ui.investment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Investment;
import com.tth.moneymanager.Model.Transaction;

import java.util.List;

public class InvestmentViewModel extends ViewModel {
    private MutableLiveData<List<Investment>> investments;
    private DbHelper db;

    public InvestmentViewModel() {
        investments=new MutableLiveData<>();
    }
    public LiveData<List<Investment>> getListInvestment(Context context, int userId) {
        db = new DbHelper(context);
        investments.setValue(db.getListInvestment(userId));
        return investments;
    }
}
