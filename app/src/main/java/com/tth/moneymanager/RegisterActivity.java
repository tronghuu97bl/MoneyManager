package com.tth.moneymanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tth.moneymanager.Database.DbHelper;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.Security.AES;
import com.tth.moneymanager.Security.RSAUtil;

import net.sqlcipher.database.SQLiteDatabase;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RegisterActivity extends AppCompatActivity {
    private Button button_register;
    private TextView tvWarning, tvLogin, tvInfo;
    private EditText edtEmail, edtPassword, edtName, edtAddress;
    private DbHelper dbHelper;
    private String image_url = "";
    private ImageView imgv1, imgv2, imgv3, imgv4, imgv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SQLiteDatabase.loadLibs(this);
        dbHelper = new DbHelper(this);
        button_register = findViewById(R.id.button_rgt);
        tvWarning = findViewById(R.id.tv_warning);
        tvInfo = findViewById(R.id.tv_info);
        tvLogin = findViewById(R.id.tv_login);
        edtEmail = findViewById(R.id.edittext_email);
        edtPassword = findViewById(R.id.edittext_password);
        edtName = findViewById(R.id.edittext_name);
        edtAddress = findViewById(R.id.edittext_address);
        imgv1 = findViewById(R.id.img1);
        imgv2 = findViewById(R.id.img2);
        imgv3 = findViewById(R.id.img3);
        imgv4 = findViewById(R.id.img4);
        imgv5 = findViewById(R.id.img5);

        //choose image -> image_url user
        handleChooseImage();

        //click button register
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRegister();// validation input
            }
        });

        //click textView Login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void handleChooseImage() {
        imgv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_url = "first";
            }
        });
        imgv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_url = "second";
            }
        });
        imgv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_url = "third";
            }
        });
        imgv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_url = "fourth";
            }
        });
        imgv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_url = "fifth";
            }
        });
    }

    private void initRegister() {
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();
        if (email.equals("") || pass.equals("")) {
            tvWarning.setText("Please enter the Email and Password");
            tvWarning.setVisibility(View.VISIBLE);
        } else {
            tvWarning.setVisibility(View.GONE);
            DoesUserExist doesUserExist = new DoesUserExist();//check user exist
            doesUserExist.execute(edtEmail.getText().toString());
        }
    }

    private class DoesUserExist extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return dbHelper.CheckUserExist(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                tvWarning.setVisibility(View.VISIBLE);
                tvWarning.setText("There is user with this email, please try another email");
            } else {
                tvWarning.setVisibility(View.GONE);
                //register new user from info
                RegisterUser registerUser = new RegisterUser();
                registerUser.execute();
            }
        }
    }

    private class RegisterUser extends AsyncTask<Void, Void, User> {
        private String email, password, lastName, firstName, address, name;

        @Override
        protected void onPreExecute() {
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();
            name = edtName.getText().toString();
            address = edtAddress.getText().toString();

            String[] names = name.split(" ");
            if (names.length > 1) {
                firstName = names[0];
                for (int i = 1; i < names.length; i++) {
                    lastName += names[i];
                    if (i != names.length - 1) {
                        lastName += " ";
                    }
                }
            } else {
                firstName = names[0];
                lastName = "";
            }
        }

        @Override
        protected User doInBackground(Void... voids) {
            User user = new User(email, password, firstName, lastName, address, image_url, 0.0, 0.0);
            int id_user = (int) dbHelper.addUser(user);
            user = dbHelper.getUserById(id_user);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                Util util = new Util(RegisterActivity.this);
                if (!util.getRSAKey()) {
                    try {
                        String[] key = RSAUtil.generateKey();
                        String privateKey = RSAUtil.base64Encode(AES.encrypt(RSAUtil.base64Decode(key[1]), "helloworld"));
                        util.setPrivateKey(privateKey);
                        util.setPublicKey(key[0]);
                        util.setRSAKey();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    util.addUserToPreference(user);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}