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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
        try {
            initView();
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

    public void initView() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        util = new Util(getContext());
        currentUser = util.userAuthenLogin();
    }
}
