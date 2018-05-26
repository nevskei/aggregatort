package ru.ivasev.aggregator.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ivasev.aggregator.*;

public class FragmentController {
    private FragmentManager fm;

    public static final String ARG_PARAM_ID = "id_card";

    private Map<String, Fragment> listFragment = new HashMap<String, Fragment>();

    public FragmentController(FragmentManager fm) {
        this.fm = fm;
        listFragment.put("form", new FormFragment());
        listFragment.put("detail", new DetailFragment());
        listFragment.put("list", new ItemFragment());
    }

    public void addFragment(String tag, Bundle args, boolean toBack) {
        Fragment fragmentTag =  fm.findFragmentByTag(tag);
        if (fragmentTag == null) {
            Fragment fragment = listFragment.get(tag);
            if(fragment != null) {
                if (args != null) {
                    fragment.setArguments(args);
                }
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                Fragment fragmentShow = null;
                if (tag != "list")
                    fragmentShow =  fm.findFragmentByTag("list");
                if (tag != "detail" && fragmentShow == null)
                    fragmentShow =  fm.findFragmentByTag("detail");
                if (tag != "form" && fragmentShow == null)
                    fragmentShow =  fm.findFragmentByTag("form");


                if(fragmentShow != null) {
                    fragmentTransaction.remove(fragmentShow);
                }
                fragmentTransaction.add(R.id.fcontainer, fragment, tag);
                if(toBack)
                    fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            }
        }
    }


    public void reloadFragment(String tag) {
        Fragment fragmentTag =  fm.findFragmentByTag(tag);
        if (fragmentTag != null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.detach(fragmentTag);
            fragmentTransaction.attach(fragmentTag);
            fragmentTransaction.commit();

        }

    }

}
