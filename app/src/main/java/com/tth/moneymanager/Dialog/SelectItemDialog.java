package com.tth.moneymanager.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Item;
import com.tth.moneymanager.R;
import com.tth.moneymanager.adapter.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectItemDialog extends DialogFragment implements ItemAdapter.GetItem {
    private EditText nameItem;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ItemAdapter.GetItem getItem;
    private DbHelper db;
    private List<Item> itemList = new ArrayList<>();
    private GetListItem getListItem;

    @Override
    public void OnGettingItemResult(Item item) {
        try {
            getItem = (ItemAdapter.GetItem) getActivity();
            getItem.OnGettingItemResult(item);
            dismiss();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_select_item, null);
        nameItem = view.findViewById(R.id.edit_dialog_select_item);
        recyclerView = view.findViewById(R.id.rc_dialog_select_item);
        db = new DbHelper(getActivity());
        getListItem = new GetListItem();
        getListItem.execute();
        adapter = new ItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Select an Item");
        return builder.create();
    }

    public class GetListItem extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            itemList = db.getListItem();
            adapter.setListItem(itemList);
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getListItem != null) {
            if (getListItem.isCancelled()) {
                getListItem.cancel(true);
            }
        }
    }
}
