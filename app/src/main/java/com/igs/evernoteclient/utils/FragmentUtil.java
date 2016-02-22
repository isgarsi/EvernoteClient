package com.igs.evernoteclient.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by isgarsi on 26/4/15.
 */
public class FragmentUtil {

    private AppCompatActivity activity;

    public FragmentUtil(AppCompatActivity activity){
        this.activity = activity;
    }

    public void changeFragment(int container, Fragment fragment, boolean addBackStack)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if(addBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
