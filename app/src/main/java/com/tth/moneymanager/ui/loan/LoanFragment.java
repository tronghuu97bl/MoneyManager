package com.tth.moneymanager.ui.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Model.Loan;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.R;
import com.tth.moneymanager.Util;
import com.tth.moneymanager.adapter.LoanAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoanFragment extends Fragment {
    private LoanViewModel loanViewModel;
    private RecyclerView recyclerView;
    private LoanAdapter adapter;
    private Util util;
    private List<Loan> loans = new ArrayList<>();
    private User currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loanViewModel = ViewModelProviders.of(this).get(LoanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loan, container, false);
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
        loanViewModel.getListLoan(getContext(), currentUser.getId()).observe(getViewLifecycleOwner(), new Observer<List<Loan>>() {
            @Override
            public void onChanged(List<Loan> s) {
                loans.addAll(s);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView = root.findViewById(R.id.rcv_loan);
        adapter = new LoanAdapter(loans, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }
}
