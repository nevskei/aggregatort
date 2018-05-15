package ru.ivasev.aggregator.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import ru.ivasev.aggregator.*;

public class FragmentController {
    private FragmentManager fm;

    public static final String ARG_PARAM_ID = "id_card";

    public static String showTag;

    private Map<String, Fragment> listFragment = new HashMap<String, Fragment>();

    public FragmentController(FragmentManager fm) {
        this.fm = fm;
        listFragment.put("form", new FormFragment());
        listFragment.put("detail", new DetailFragment());
        listFragment.put("list", new ItemFragment());
    }

    public void addFragment(String tag, Bundle args, boolean toBack) {
        if (showTag != tag) {
            Fragment fragment = listFragment.get(tag);
            if(fragment != null) {
                if (args != null) {
                    fragment.setArguments(args);
                }
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                deleteFragment(showTag, fragmentTransaction);
                fragmentTransaction.add(R.id.fcontainer, fragment, tag);
                if(toBack)
                    fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
                showTag = tag;
            }
        }
    }

    private void deleteFragment(String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        deleteFragment(tag, fragmentTransaction);
        fragmentTransaction.commit();
    }

    private void deleteFragment(String tag, FragmentTransaction fragmentTransaction) {
        Fragment fragment = listFragment.get(tag);
        if(fragment != null) {
            fragmentTransaction.remove(fragment);
        }
    }

    public static void onBackPressed() {
        if (showTag == "form" || showTag == "detail" )
        showTag = "list";
    }
}
