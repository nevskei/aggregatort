package ru.ivasev.aggregator;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.ivasev.aggregator.controller.FragmentController;

public class MainActivity extends AppCompatActivity {

    private FragmentController fragmentController;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fragmentController = new FragmentController(fm);

        fragmentController.addFragment("list",null, false);
    }

     public void showForm(View view, Bundle args) {
         fragmentController.addFragment("form", args, true);
     }


    public void showPhoto(View view, Bundle args) {
        fragmentController.addFragment("detail", args, true);
    }

    @Override
    public void onBackPressed() {
        FragmentController.onBackPressed();
        super.onBackPressed();
    }
}
