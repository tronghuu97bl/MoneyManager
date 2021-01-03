package com.tth.moneymanager.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tth.moneymanager.AddInvestmentActivity;
import com.tth.moneymanager.AddLoanActivity;
import com.tth.moneymanager.AddSendReceiveActivity;
import com.tth.moneymanager.AddShoppingActivity;
import com.tth.moneymanager.R;

public class AddTransactionDialog extends DialogFragment {
    private RelativeLayout shopping, investment, loan, transaction;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_transaction, null);
        shopping = view.findViewById(R.id.dialog_add_tran_1);
        investment = view.findViewById(R.id.dialog_add_tran_2);
        loan = view.findViewById(R.id.dialog_add_tran_3);
        transaction = view.findViewById(R.id.dialog_add_tran_4);

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddShoppingActivity.class);
                startActivity(intent);
            }
        });
        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddInvestmentActivity.class);
                startActivity(intent);
            }
        });
        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddLoanActivity.class);
                startActivity(intent);
            }
        });
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddSendReceiveActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Add Transaction")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setView(view);
        return builder.create();
    }
}
