package ru.ivasev.aggregator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.ivasev.aggregator.controller.FragmentController;
import ru.ivasev.aggregator.data.Card;

public class MainActivity extends AppCompatActivity {

    private FragmentController fragmentController;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fragmentController = new FragmentController(fm);
        if (fm.getFragments().isEmpty())
            fragmentController.addFragment("list",null, false);
    }

     public void showForm(View view, Bundle args) {
         fragmentController.addFragment("form", args, true);
     }


    public void showPhoto(View view, Bundle args) {
        fragmentController.addFragment("detail", args, true);
    }


    public void reloadFragment(String name) {
        fragmentController.reloadFragment(name);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FormFragment formFragment = (FormFragment) fm.findFragmentByTag("form");

        if (resultCode == RESULT_OK && formFragment != null) {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            formFragment.setFile(thumbnailBitmap, requestCode);
        }
    }

}
