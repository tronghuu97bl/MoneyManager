package com.tth.moneymanager.ui.investment;

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

import com.tth.moneymanager.Model.Investment;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.R;
import com.tth.moneymanager.Util;
import com.tth.moneymanager.adapter.InvestmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class InvestmentFragment extends Fragment {
    private List<Investment> listInvestments = new ArrayList<>();
    private User currentUser;
    private RecyclerView recyclerView;
    private Util util;
    private InvestmentAdapter adapter;
    private InvestmentViewModel investmentViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        investmentViewModel = ViewModelProviders.of(this).get(InvestmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_investment, container, false);
        initView();
        investmentViewModel.getListInvestment(getContext(), currentUser.getId()).observe(getViewLifecycleOwner(), new Observer<List<Investment>>() {
            @Override
            public void onChanged(List<Investment> investments) {
                listInvestments.addAll(investments);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView = root.findViewById(R.id.rcv_investment);
        adapter = new InvestmentAdapter(listInvestments, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    public void initView() {
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
    }
}
