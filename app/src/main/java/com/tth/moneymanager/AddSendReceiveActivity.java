package com.tth.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AddSendReceiveActivity extends AppCompatActivity {
    private EditText editAmount, editDate, editReceipt, editDes;
    private Button btAdd, btPickDate;
    private TextView tvWarning;
    private RadioGroup radioGroup;
    private Util util;
    private User currentUser;
    private DbHelper db;
    private String date, receipt, type, des;
    private Double amount;
    private Calendar calendar = Calendar.getInstance();
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
        setContentView(R.layout.activity_add_send_receive);
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
        btPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddSendReceiveActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationInput()) {
                    tvWarning.setVisibility(View.GONE);
                    afterAddTransaction(addTransaction());
                } else {
                    tvWarning.setVisibility(View.VISIBLE);
                    tvWarning.setText("Please enter input !!!");
                }
            }
        });
    }

    public void initView() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        tvWarning = findViewById(R.id.tv_add_tranfer_warning);
        btAdd = findViewById(R.id.button_add_tranfer_add);
        btPickDate = findViewById(R.id.button_add_tranfer_date);
        editAmount = findViewById(R.id.edit_add_tranfer_amount);
        editDate = findViewById(R.id.edit_add_tranfer_date);
        editReceipt = findViewById(R.id.edit_add_tranfer_receipt);
        editDes = findViewById(R.id.edit_add_tranfer_des);
        util = new Util(this);
        currentUser = util.userAuthenLogin();
        db = new DbHelper(this);
        radioGroup = findViewById(R.id.rdg_add_tranfer);
    }

    public boolean validationInput() {
        if (editReceipt.getText().toString().equals("") || editDate.getText().toString().equals("") || editAmount.getText().toString().equals("")) {
            return false;
        } else {
            amount = Double.valueOf(editAmount.getText().toString());
            receipt = editReceipt.getText().toString();
            date = editDate.getText().toString();
            des = editDes.getText().toString();
            int rdChoose = radioGroup.getCheckedRadioButtonId();
            if (rdChoose == R.id.rd_add_tranfer_send) {
                type = "send";
                amount = -amount;
            } else {
                type = "receive";
            }
            return true;
        }
    }

    public int addTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setRecipient(receipt);
        transaction.setType(type);
        transaction.setDescription(des);
        transaction.setDate(date);
        transaction.setUser_id(currentUser.getId());
        return (int) db.addTransaction(transaction);
    }

    public void afterAddTransaction(Integer integer) {
        if (integer > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSendReceiveActivity.this)
                    .setTitle("Add transaction success")
                    .setMessage("Continue add new send/receive ?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AddSendReceiveActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editAmount.setText("");
                            editDate.setText("");
                            editDes.setText("");
                            editReceipt.setText("");
                        }
                    });
            builder.show();
        }
    }
}