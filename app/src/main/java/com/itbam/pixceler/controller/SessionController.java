package com.itbam.pixceler.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.itbam.pixceler.util.Formatters;

import java.text.NumberFormat;

public class SessionController {

    private static volatile SessionController instance;
    private SharedPreferences sharedPreferences;
    private float balance;

    public SessionController(Context context) {
        sharedPreferences = context
                .getSharedPreferences("balanceStore", Context.MODE_PRIVATE);

        setBalance();
    }

    public static SessionController getInstance(Context context) {
        if (instance == null) {
            instance = new SessionController(context);
        }
        return instance;
    }

    public void automaticRecharge() {
        update(100.0f);
    }

    public void setBalance() {
        balance = sharedPreferences.getFloat("balance", 0f);
    }

    public float getBalance() {
        return balance;
    }

    public void save() {
        sharedPreferences.edit()
                .putFloat("balance", balance)
                .apply();
    }

    public void update(float newBalance) {
        balance += newBalance;
        save();
    }

    public void remove(float sub) {
        balance -= sub;
        save();
    }
}
