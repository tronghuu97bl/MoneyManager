package com.tth.moneymanager.ui.transaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.R;
import com.tth.moneymanager.Util;
import com.tth.moneymanager.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private RadioGroup radioGroup;
    private Button button;//search
    private EditText editText;//min amount
    private TextView textView;//no transactions
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private Util util;
    private User currentUser;
    private String type = "all";
    private double min;
    private DbHelper db;
    private getListTransaction getListTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        radioGroup = root.findViewById(R.id.rgType);
        button = root.findViewById(R.id.btnSearch);
        editText = root.findViewById(R.id.edtTxtMin);
        textView = root.findViewById(R.id.txtNoTransaction);
        recyclerView = root.findViewById(R.id.transactionRecView);
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
        db = new DbHelper(getContext());
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getListTransaction = new getListTransaction();
        getListTransaction.execute();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                getListTransaction = new getListTransaction();
                getListTransaction.execute();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTransaction = new getListTransaction();
                getListTransaction.execute();
            }
        });
        return root;
    }

    private class getListTransaction extends AsyncTask<Void, Void, List<Transaction>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rbAll:
                    type = "all";
                    break;
                case R.id.rbInvestment:
                    type = "investment";
                    break;
                case R.id.rbLoan:
                    type = "loan";
                    break;
                case R.id.rbProfit:
                    type = "profit";
                    break;
                case R.id.rbReceive:
                    type = "receive";
                    break;
                case R.id.rbSend:
                    type = "send";
                    break;
                case R.id.rbShopping:
                    type = "shopping";
                    break;
            }
            if (editText.getText().toString().equals("")) {
                min = -99999999999999999d;
            } else {
                min = Double.valueOf(editText.getText().toString());
            }
        }

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            List<Transaction> listTransaction = new ArrayList<>();
            if (type.equals("all")) {
                listTransaction = db.getListTransaction(currentUser.getId(), min);
            } else {
                listTransaction = db.getListTransaction(type, currentUser.getId(), min);
            }
            return listTransaction;
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            super.onPostExecute(transactions);
            if (transactions != null) {
                textView.setVisibility(View.GONE);
                adapter.setTransactions(transactions);
                adapter.notifyDataSetChanged();
            } else {
                textView.setVisibility(View.VISIBLE);
                adapter.setTransactions(new ArrayList<Transaction>());
                adapter.notifyDataSetChanged();
            }
        }
    }
}