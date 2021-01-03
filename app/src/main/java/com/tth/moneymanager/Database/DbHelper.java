package com.tth.moneymanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tth.moneymanager.Model.Investment;
import com.tth.moneymanager.Model.Item;
import com.tth.moneymanager.Model.Loan;
import com.tth.moneymanager.Model.Shopping;
import com.tth.moneymanager.Model.Transaction;
import com.tth.moneymanager.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "money_manager_db";
    private static final int DB_VERSION = 15;
    //table users
    private static final String TABLE_USERS = "users";
    private static final String USERS_ID = "_id";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_PASS = "password";
    private static final String USERS_FNAME = "first_name";
    private static final String USERS_LNAME = "last_name";
    private static final String USERS_ADDRESS = "address";
    private static final String USERS_IMAGE = "image_url";
    private static final String USERS_AMOUNT = "remained_amount";
    private static final String USERS_DEBT = "remained_debt";
    //table shopping
    private static final String TABLE_SHOPPING = "shopping";
    private static final String SHOPPING_ID = "_id";
    private static final String SHOPPING_USER_ID = "user_id";
    private static final String SHOPPING_ITEM_ID = "item_id";
    private static final String SHOPPING_TRANSACTION_ID = "transaction_id";
    private static final String SHOPPING_PRICE = "price";
    private static final String SHOPPING_DATE = "date";
    private static final String SHOPPING_DES = "description";
    //table investments
    private static final String TABLE_INVESTMENT = "investments";
    private static final String INVEST_ID = "_id";
    private static final String INVEST_AMOUNT = "amount";
    private static final String INVEST_MONTHLY_ROI = "monthly_roi";
    private static final String INVEST_NAME = "name";
    private static final String INVEST_INIT_DATE = "init_date";
    private static final String INVEST_FINISH_DATE = "finish_date";
    private static final String INVEST_USER_ID = "user_id";
    private static final String INVEST__TRAN_ID = "transaction_id";
    //table loans
    private static final String TABLE_LOANS = "loans";
    private static final String LOANS_ID = "_id";
    private static final String LOANS_INIT_DATE = "init_date";
    private static final String LOANS_FINISH_DATE = "finish_date";
    private static final String LOANS_INIT_AMOUNT = "init_amount";
    private static final String LOANS_REMAIN_AMOUNT = "remained_amount";
    private static final String LOANS_MONTHLY_PAYMENT = "monthly_payment";
    private static final String LOANS_MONTHLY_ROI = "monthly_roi";
    private static final String LOANS_NAME = "name";
    private static final String LOANS_USER_ID = "user_id";
    private static final String LOANS_TRAN_ID = "transaction_id";
    //table transactions
    private static final String TABLE_TRANS = "transactions";
    private static final String TRAN_ID = "_id";
    private static final String TRAN_AMOUNT = "amount";
    private static final String TRAN_DATE = "date";
    private static final String TRAN_TYPE = "type";
    private static final String TRAN_USER_ID = "user_id";
    private static final String TRAN_RECIPIENT = "recipient";
    private static final String TRAN_DES = "description";
    //table items
    private static final String TABLE_ITEM = "items";
    private static final String ITEM_ID = "_id";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_IMAGE_URL = "image_url";
    private static final String ITEM_DES = "description";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_User = "CREATE TABLE " + TABLE_USERS + " (" +
                USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERS_EMAIL + " TEXT NOT NULL," +
                USERS_PASS + " TEXT NOT NULL," +
                USERS_FNAME + " TEXT," +
                USERS_LNAME + " TEXT," +
                USERS_ADDRESS + " TEXT," +
                USERS_IMAGE + " TEXT," +
                USERS_AMOUNT + " DOUBLE," +
                USERS_DEBT + " DOUBLE)";

        String create_shopping_table = "CREATE TABLE " + TABLE_SHOPPING + " (" +
                SHOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SHOPPING_USER_ID + " INTEGER," +
                SHOPPING_ITEM_ID + " INTEGER," +
                SHOPPING_TRANSACTION_ID + " INTEGER," +
                SHOPPING_PRICE + " DOUBLE," +
                SHOPPING_DATE + " DATE," +
                SHOPPING_DES + " TEXT)";
        String create_table_investment = "CREATE TABLE " + TABLE_INVESTMENT + " (" +
                INVEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                INVEST_AMOUNT + " DOUBLE," + //số tiền đầu tư
                INVEST_MONTHLY_ROI + " DOUBLE," +//tiền lời hàng tháng Return on Investment
                INVEST_NAME + " TEXT," +
                INVEST_INIT_DATE + " DATE," +
                INVEST_FINISH_DATE + " DATE," +
                INVEST_USER_ID + " INTEGER," +
                INVEST__TRAN_ID + " INTEGER)";
        String create_table_loan = "CREATE TABLE " + TABLE_LOANS + "(" +
                LOANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOANS_INIT_DATE + " DATE," +
                LOANS_FINISH_DATE + " DATE," +
                LOANS_INIT_AMOUNT + " DOUBLE," +
                LOANS_REMAIN_AMOUNT + " DOUBLE," +
                LOANS_MONTHLY_PAYMENT + " DOUBLE," +
                LOANS_MONTHLY_ROI + " DOUBLE," +
                LOANS_NAME + " TEXT," +
                LOANS_USER_ID + " INTEGER," +
                LOANS_TRAN_ID + " INTEGER)";
        String create_table_transaction = "create table " + TABLE_TRANS + "(" +
                TRAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TRAN_AMOUNT + " DOUBLE," +
                TRAN_DATE + " DATE," +
                TRAN_TYPE + " TEXT," +
                TRAN_USER_ID + " INTEGER," +
                TRAN_RECIPIENT + " TEXT," + //người nhận
                TRAN_DES + " TEXT)";
        String create_table_item = "create table " + TABLE_ITEM + "(" +
                ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_NAME + " TEXT," +
                ITEM_IMAGE_URL + " TEXT," +
                ITEM_DES + " TEXT)";
        db.execSQL(create_table_User);
        db.execSQL(create_shopping_table);
        db.execSQL(create_table_investment);
        db.execSQL(create_table_loan);
        db.execSQL(create_table_transaction);
        db.execSQL(create_table_item);

        addInitialItems(db);
        addTestTransaction(db);
        addTestProfit(db);
        addTestShopping(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d("Test", "delete");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVESTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }


    //TABLE USERS QUERY
    public boolean CheckUserExist(String email) { //return true is have user in db
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USERS_ID, USERS_EMAIL}, USERS_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex(USERS_EMAIL)).equals(email)) {
                    cursor.close();
                    db.close();
                    return true;
                } else {
                    cursor.close();
                    db.close();
                    return false;
                }
            } else {
                cursor.close();
                db.close();
                return false;
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERS_EMAIL, user.getEmail());
        cv.put(USERS_PASS, user.getPassword());
        cv.put(USERS_FNAME, user.getFirstname());
        cv.put(USERS_LNAME, user.getLastname());
        cv.put(USERS_ADDRESS, user.getAdrress());
        cv.put(USERS_IMAGE, user.getImage_url());
        cv.put(USERS_AMOUNT, user.getRemained_amount());
        cv.put(USERS_DEBT, user.getRemained_debt());
        long result = db.insert(TABLE_USERS, null, cv);
        cv.clear();
        db.close();
        return result;
    }

    public User getUserById(int id_user) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        Cursor cursor = db.query(TABLE_USERS, null, USERS_ID + "=?", new String[]{String.valueOf(id_user)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user.setId(cursor.getInt(cursor.getColumnIndex(USERS_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USERS_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(USERS_PASS)));
                user.setFirstname(cursor.getString(cursor.getColumnIndex(USERS_FNAME)));
                user.setLastname(cursor.getString(cursor.getColumnIndex(USERS_LNAME)));
                user.setAdrress(cursor.getString(cursor.getColumnIndex(USERS_ADDRESS)));
                user.setImage_url(cursor.getString(cursor.getColumnIndex(USERS_IMAGE)));
                user.setRemained_amount(cursor.getDouble(cursor.getColumnIndex(USERS_AMOUNT)));
                user.setRemained_debt(cursor.getDouble(cursor.getColumnIndex(USERS_DEBT)));
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public User userLogin(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, USERS_EMAIL + "=? AND " + USERS_PASS + "=?", new String[]{email, pass}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                User user = new User();
                user.setRemained_amount(cursor.getDouble(cursor.getColumnIndex(USERS_AMOUNT)));
                user.setRemained_debt(cursor.getDouble(cursor.getColumnIndex(USERS_DEBT)));
                user.setImage_url(cursor.getString(cursor.getColumnIndex(USERS_IMAGE)));
                user.setAdrress(cursor.getString(cursor.getColumnIndex(USERS_ADDRESS)));
                user.setLastname(cursor.getString(cursor.getColumnIndex(USERS_LNAME)));
                user.setFirstname(cursor.getString(cursor.getColumnIndex(USERS_LNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USERS_EMAIL)));
                user.setId(cursor.getInt(cursor.getColumnIndex(USERS_ID)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(USERS_PASS)));
                cursor.close();
                db.close();
                return user;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public void updateRemainedAmount(int userId, double money) {//money maybe + or -
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USERS_AMOUNT}, USERS_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                double currentAmount = cursor.getDouble(cursor.getColumnIndex(USERS_AMOUNT));
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put(USERS_AMOUNT, currentAmount + money);
                db.update(TABLE_USERS, cv, USERS_ID + "=?", new String[]{String.valueOf(userId)});
            }
        } else {
            cursor.close();
        }
        db.close();
    }

    public void updateRemainedDebt(int userId, double money) {//money maybe + or -
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USERS_DEBT}, USERS_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                double currentDebt = cursor.getDouble(cursor.getColumnIndex(USERS_DEBT));
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put(USERS_DEBT, currentDebt + money);
                db.update(TABLE_USERS, cv, USERS_ID + "=?", new String[]{String.valueOf(userId)});
            }
        } else {
            cursor.close();
        }
        db.close();
    }

    //TABLE SHOPPING QUERY
    public long addShopping(Shopping shopping) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SHOPPING_USER_ID, shopping.getUser_id());
        cv.put(SHOPPING_TRANSACTION_ID, shopping.getTransaction_id());
        cv.put(SHOPPING_ITEM_ID, shopping.getItem_id());
        cv.put(SHOPPING_PRICE, shopping.getPrice());
        cv.put(SHOPPING_DATE, shopping.getDate());
        cv.put(SHOPPING_DES, shopping.getDescription());
        long shoppingId = db.insert(TABLE_SHOPPING, null, cv);
        cv.clear();
        db.close();
        return shoppingId;
    }

    public List<Shopping> getListShopping_DatePrice(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Shopping> listShopping = new ArrayList<>();
        Cursor cursor = db.query(TABLE_SHOPPING, new String[]{SHOPPING_DATE, SHOPPING_PRICE},
                SHOPPING_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Shopping shopping = new Shopping();
                shopping.setDate(cursor.getString(cursor.getColumnIndex(SHOPPING_DATE)));
                shopping.setPrice(cursor.getDouble(cursor.getColumnIndex(SHOPPING_PRICE)));
                listShopping.add(shopping);
            }
            cursor.close();
            db.close();
            return listShopping;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    //TABLE TRANSACTION QUERY
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRAN_AMOUNT, transaction.getAmount());
        cv.put(TRAN_DATE, transaction.getDate());
        cv.put(TRAN_TYPE, transaction.getType());
        cv.put(TRAN_USER_ID, transaction.getUser_id());
        cv.put(TRAN_DES, transaction.getDescription());
        cv.put(TRAN_RECIPIENT, transaction.getRecipient());
        long tranID = db.insert(TABLE_TRANS, null, cv);
        return tranID;
    }

    public List<Transaction> getListTransaction(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> listTransaction = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TRANS, null, TRAN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, TRAN_ID + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(TRAN_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(TRAN_AMOUNT)));
                transaction.setUser_id(cursor.getInt(cursor.getColumnIndex(TRAN_USER_ID)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(TRAN_DATE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRAN_DES)));
                transaction.setRecipient(cursor.getString(cursor.getColumnIndex(TRAN_RECIPIENT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(TRAN_TYPE)));
                listTransaction.add(transaction);
            }
            cursor.close();
            db.close();
            return listTransaction;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public List<Transaction> getListTransaction(String type, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TRANS, null, TRAN_TYPE + "=? AND " + TRAN_USER_ID + "=?",
                new String[]{type, String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(TRAN_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(TRAN_AMOUNT)));
                transaction.setUser_id(cursor.getInt(cursor.getColumnIndex(TRAN_USER_ID)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(TRAN_DATE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRAN_DES)));
                transaction.setRecipient(cursor.getString(cursor.getColumnIndex(TRAN_RECIPIENT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(TRAN_TYPE)));
                transactions.add(transaction);
            }
            cursor.close();
            db.close();
            return transactions;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public List<Transaction> getListTransaction(String type, int userId, Double min) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TRANS, null, TRAN_TYPE + "=? AND " + TRAN_USER_ID + "=?",
                new String[]{type, String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(TRAN_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(TRAN_AMOUNT)));
                transaction.setUser_id(cursor.getInt(cursor.getColumnIndex(TRAN_USER_ID)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(TRAN_DATE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRAN_DES)));
                transaction.setRecipient(cursor.getString(cursor.getColumnIndex(TRAN_RECIPIENT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(TRAN_TYPE)));
                if (transaction.getAmount() >= min) {
                    transactions.add(transaction);
                }
            }
            cursor.close();
            db.close();
            if (transactions.size() > 0) {
                return transactions;
            } else
                return null;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public List<Transaction> getListTransaction(int userId, Double min) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TRANS, null, TRAN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(TRAN_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(TRAN_AMOUNT)));
                transaction.setUser_id(cursor.getInt(cursor.getColumnIndex(TRAN_USER_ID)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(TRAN_DATE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRAN_DES)));
                transaction.setRecipient(cursor.getString(cursor.getColumnIndex(TRAN_RECIPIENT)));
                transaction.setType(cursor.getString(cursor.getColumnIndex(TRAN_TYPE)));
                if (transaction.getAmount() >= min) {
                    transactions.add(transaction);
                }
            }
            cursor.close();
            db.close();
            if (transactions.size() > 0) {
                return transactions;
            } else
                return null;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    //TABLE LOAN QUERY
    public long addLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LOANS_USER_ID, loan.getUser_id());
        cv.put(LOANS_TRAN_ID, loan.getTransaction_id());
        cv.put(LOANS_INIT_AMOUNT, loan.getInit_amount());
        cv.put(LOANS_REMAIN_AMOUNT, loan.getRemained_amount());
        cv.put(LOANS_INIT_DATE, loan.getInit_date());
        cv.put(LOANS_FINISH_DATE, loan.getFinish_date());
        cv.put(LOANS_NAME, loan.getName());
        cv.put(LOANS_MONTHLY_PAYMENT, loan.getMonthly_payment());
        cv.put(LOANS_MONTHLY_ROI, loan.getMonthly_roi());
        return db.insert(TABLE_LOANS, null, cv);
    }

    public List<Loan> getListLoan(int userId) {
        List<Loan> listLoan = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOANS, null, LOANS_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Loan loan = new Loan();
                loan.setId(cursor.getInt(cursor.getColumnIndex(LOANS_ID)));
                loan.setUser_id(cursor.getInt(cursor.getColumnIndex(LOANS_USER_ID)));
                loan.setTransaction_id(cursor.getInt(cursor.getColumnIndex(LOANS_TRAN_ID)));
                loan.setInit_amount(cursor.getDouble(cursor.getColumnIndex(LOANS_INIT_AMOUNT)));
                loan.setRemained_amount(cursor.getDouble(cursor.getColumnIndex(LOANS_REMAIN_AMOUNT)));
                loan.setMonthly_payment(cursor.getDouble(cursor.getColumnIndex(LOANS_MONTHLY_PAYMENT)));
                loan.setMonthly_roi(cursor.getDouble(cursor.getColumnIndex(LOANS_MONTHLY_ROI)));
                loan.setInit_date(cursor.getString(cursor.getColumnIndex(LOANS_INIT_DATE)));
                loan.setFinish_date(cursor.getString(cursor.getColumnIndex(LOANS_FINISH_DATE)));
                loan.setName(cursor.getString(cursor.getColumnIndex(LOANS_NAME)));
                listLoan.add(loan);
            }
            cursor.close();
            db.close();
            return listLoan;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public void updateRemainedAmountLoan(int loanId, double money) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LOANS, new String[]{LOANS_REMAIN_AMOUNT}, LOANS_USER_ID + "=?",
                new String[]{String.valueOf(loanId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                double remainedAmount = cursor.getDouble(cursor.getColumnIndex(LOANS_REMAIN_AMOUNT));
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put(LOANS_REMAIN_AMOUNT, remainedAmount - money);
                db.update(TABLE_LOANS, cv, LOANS_ID + "=?", new String[]{String.valueOf(loanId)});
                cv.clear();
            }
        }
        db.close();
    }

    //TABLE INVESTMENT QUERY
    public long addInvestment(Investment investment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(INVEST__TRAN_ID, investment.getTransaction_id());
        cv.put(INVEST_AMOUNT, investment.getAmount());
        cv.put(INVEST_INIT_DATE, investment.getInit_date());
        cv.put(INVEST_FINISH_DATE, investment.getFinish_date());
        cv.put(INVEST_NAME, investment.getName());
        cv.put(INVEST_USER_ID, investment.getUser_id());
        cv.put(INVEST_MONTHLY_ROI, investment.getMonthly_roi());
        long investmentId = db.insert(TABLE_INVESTMENT, null, cv);
        return investmentId;
    }

    public List<Investment> getListInvestment(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Investment> listInvestment = new ArrayList<>();
        Cursor cursor = db.query(TABLE_INVESTMENT, null, INVEST_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Investment investment = new Investment();
                investment.setId(cursor.getInt(cursor.getColumnIndex(INVEST_ID)));
                investment.setUser_id(cursor.getInt(cursor.getColumnIndex(INVEST_USER_ID)));
                investment.setTransaction_id(cursor.getInt(cursor.getColumnIndex(INVEST__TRAN_ID)));
                investment.setAmount(cursor.getDouble(cursor.getColumnIndex(INVEST_AMOUNT)));
                investment.setMonthly_roi(cursor.getDouble(cursor.getColumnIndex(INVEST_MONTHLY_ROI)));
                investment.setName(cursor.getString(cursor.getColumnIndex(INVEST_NAME)));
                investment.setInit_date(cursor.getString(cursor.getColumnIndex(INVEST_INIT_DATE)));
                investment.setFinish_date(cursor.getString(cursor.getColumnIndex(INVEST_FINISH_DATE)));
                listInvestment.add(investment);
            }
            cursor.close();
            db.close();
            return listInvestment;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    //TABLE ITEMS QUERY
    public long addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item.getName());
        cv.put(ITEM_IMAGE_URL, item.getImage_url());
        cv.put(ITEM_DES, item.getDescription());
        long idItem = db.insert(TABLE_ITEM, null, cv);
        cv.clear();
        db.close();
        return idItem;
    }

    public List<Item> getListItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> listItem = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ITEM, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_ID)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DES)));
                item.setImage_url(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE_URL)));
                item.setName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                listItem.add(item);
            }
            db.close();
            cursor.close();
            return listItem;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public List<Item> getListItemByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> listItem = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ITEM, null, ITEM_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                item.setImage_url(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE_URL)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DES)));
                listItem.add(item);
            }
            cursor.close();
            db.close();
            return listItem;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    //ADD ESSENTIAL
    private void addTestTransaction(SQLiteDatabase sqLiteDatabase) {
        ContentValues values = new ContentValues();
        values.put("_id", 0);
        values.put("amount", 10.5);
        values.put("date", "2019-10-04");
        values.put("type", "shopping");
        values.put("user_id", 1);
        values.put("description", "Grocery shopping");
        values.put("recipient", "Walmart");
        long newTransactionId = sqLiteDatabase.insert("transactions", null, values);
        Log.d("Test", "111");
    }

    public void addTestShopping(SQLiteDatabase db) {
        ContentValues firstValues = new ContentValues();
        firstValues.put("item_id", 1);
        firstValues.put("transaction_id", 1);
        firstValues.put("user_id", 1);
        firstValues.put("price", 10.0);
        firstValues.put("description", "some description");
        firstValues.put("date", "2020-12-29");
        db.insert("shopping", null, firstValues);

        ContentValues secondValues = new ContentValues();
        secondValues.put("item_id", 2);
        secondValues.put("transaction_id", 2);
        secondValues.put("user_id", 1);
        secondValues.put("price", 8.0);
        secondValues.put("description", "second description");
        secondValues.put("date", "2020-12-30");
        db.insert("shopping", null, secondValues);

        ContentValues thirdValues = new ContentValues();
        thirdValues.put("item_id", 2);
        thirdValues.put("transaction_id", 3);
        thirdValues.put("user_id", 1);
        thirdValues.put("price", 16.0);
        thirdValues.put("description", "third description");
        thirdValues.put("date", "2020-12-31");
        db.insert("shopping", null, thirdValues);
    }

    private void addTestProfit(SQLiteDatabase db) {
        ContentValues thirdValues = new ContentValues();
        thirdValues.put("amount", 32.0);
        thirdValues.put("type", "profit");
        thirdValues.put("date", "2020-11-11");
        thirdValues.put("description", "monthly profit stocks");
        thirdValues.put("user_id", 1);
        thirdValues.put("recipient", "Vangaurd");
        db.insert("transactions", null, thirdValues);
        ContentValues firstValues = new ContentValues();
        firstValues.put("amount", 15.0);
        firstValues.put("type", "profit");
        firstValues.put("date", "2020-12-10");
        firstValues.put("description", "monthly profit from Bank of America");
        firstValues.put("user_id", 1);
        firstValues.put("recipient", "Bank of America");
        db.insert("transactions", null, firstValues);

        ContentValues secondValues = new ContentValues();
        secondValues.put("amount", 25.0);
        secondValues.put("type", "profit");
        secondValues.put("date", "2020-12-26");
        secondValues.put("description", "monthly profit from Real Estate investment");
        secondValues.put("user_id", 1);
        secondValues.put("recipient", "Real Estate Agency");
        db.insert("transactions", null, secondValues);


    }

    private void addInitialItems(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", "Bike");
        values.put("image_url", "https://cdn.shopify.com/s/files/1/0903/4494/products/Smashing-Pumpkin-GX-Eagle-complete-front-white.jpg");
        values.put("description", "The perfect mountain bike");
        db.insert("items", null, values);
    }
}
