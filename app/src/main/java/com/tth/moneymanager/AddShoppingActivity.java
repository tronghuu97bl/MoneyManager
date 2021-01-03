package com.tth.moneymanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Dialog.SelectItemDialog;
import com.tth.moneymanager.Model.Item;
import com.tth.moneymanager.Model.Shopping;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.adapter.ItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddShoppingActivity extends AppCompatActivity implements ItemAdapter.GetItem {
    private EditText editPrice, editDate, editShop, editDes, editName;
    private DbHelper db;
    private String date, des, type, receipt;
    private Double amount;
    private Util util;
    private User currentUser;
    private TextView warning, tvName;
    private Calendar calendar = Calendar.getInstance();
    private Button btPickDate, btAddItem, btAdd, btPickItem;
    private Item selectedItem;
    private AddTransaction addTransaction;
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            editDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping);
        initView();
        SetOnClickListener();
    }

    public void initView() {
        editDate = findViewById(R.id.edit_add_shopping_date);
        editDes = findViewById(R.id.edit_add_shopping_des);
        editName = findViewById(R.id.edit_add_shopping_name);
        editPrice = findViewById(R.id.edit_add_shopping_price);
        editShop = findViewById(R.id.edit_add_shopping_shop);
        db = new DbHelper(this);
        warning = findViewById(R.id.tv_add_shopping_warning);
        btAddItem = findViewById(R.id.button_add_shopping_add_item);
        btAdd = findViewById(R.id.button_add_shopping_add);
        btPickDate = findViewById(R.id.button_add_shopping_pick_date);
        btPickItem = findViewById(R.id.button_add_shopping_pick_item);
        tvName = findViewById(R.id.tv_add_shopping_name);
        util = new Util(this);
        currentUser = util.userAuthenLogin();
        addTransaction = new AddTransaction();
    }

    public void SetOnClickListener() {
        btPickItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectItemDialog selectItemDialog = new SelectItemDialog();
                selectItemDialog.show(getSupportFragmentManager(), "select item dialog");
            }
        });
        btPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddShoppingActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add new Items
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationInput()) {
                    warning.setVisibility(View.GONE);
                    addTransaction.execute();
                } else {
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("Please enter input shopping !!!");
                }
            }
        });
    }

    public boolean validationInput() {
        if (editShop.getText().toString().equals("") || editPrice.getText().toString().equals("")
                || editDes.getText().toString().equals("") || editDate.getText().toString().equals("")
                || editName.getText().toString().equals("")) {
            return false;
        }
        amount = Double.valueOf(editPrice.getText().toString());
        date = editDate.getText().toString();
        des = editDes.getText().toString();
        type = "Shopping";
        receipt = editShop.getText().toString();
        return true;
    }

    @Override
    public void OnGettingItemResult(Item item) {
        selectedItem = item;
        tvName.setVisibility(View.VISIBLE);
        editName.setVisibility(View.VISIBLE);
        editName.setText(selectedItem.getName());
    }

    public class AddTransaction extends AsyncTask<Void, Void, Integer> {
        private Transaction transaction;
        private Shopping shopping;

        @Override
        protected Integer doInBackground(Void... voids) {
            transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setUser_id(currentUser.getId());
            transaction.setDate(date);
            transaction.setDescription(des);
            transaction.setType(type);
            transaction.setRecipient(receipt);
            return (int) db.addTransaction(transaction);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0) {
                shopping = new Shopping();
                shopping.setUser_id(currentUser.getId());
                shopping.setTransaction_id(integer);
                shopping.setItem_id(selectedItem.getId());
                shopping.setDescription(des);
                shopping.setDate(date);
                shopping.setPrice(-amount);
                if (db.addShopping(shopping) > 0) {
                    Intent intent = new Intent(AddShoppingActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("result", 1);
                    startActivity(intent);
                }
            }
        }
    }
}