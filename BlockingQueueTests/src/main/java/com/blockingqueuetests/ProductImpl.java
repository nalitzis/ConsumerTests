package com.blockingqueuetests;

import android.util.Log;

/**
 * Created by ado on 15/06/13.
 */
public class ProductImpl implements Product {

    private static final String TAG = "ProductImpl";

    private final String mName;

    public ProductImpl(String name){
        mName = name;
    }

    @Override
    public void consume() throws InterruptedException{
        Log.d(TAG, "product "+this.toString()+" starts to be consumed");
        Thread.sleep(Settings.TIME_FOR_PROCESSING);
        Log.d(TAG, "product "+this.toString()+" is consumed");

    }

    public String toString(){
        return mName;
    }
}
