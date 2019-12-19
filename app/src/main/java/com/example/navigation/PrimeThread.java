package com.example.navigation;

import android.util.Log;

class PrimeThread extends Thread {
    long minPrime;
    PrimeThread() {
        super();
    }

    public void run() {
        Log.d("msg","mdh");
        try {
            this.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
