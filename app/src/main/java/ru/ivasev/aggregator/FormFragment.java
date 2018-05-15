package ru.ivasev.aggregator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ru.ivasev.aggregator.controller.FragmentController;
import ru.ivasev.aggregator.data.Card;

import static ru.ivasev.aggregator.controller.FragmentController.ARG_PARAM_ID;


public class FormFragment extends Fragment {


    private Card card;


    private EditText editName;
    private EditText editCode;
    private EditText editDescritpion;
    private ImageView editBarcode;
    private ImageView editlogo;

    public FormFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onResume() {
        MainActivity mainActivity = (MainActivity)getActivity();

        List<Card> listCard = Card.getList(mainActivity,null, Card.CardEntry._ID + "= ?",new String[]{Long.toString(getArguments().getLong(ARG_PARAM_ID))});
        if(!listCard.isEmpty())
            card = listCard.get(0);
        else
            card = new Card(mainActivity, "", "", "", "", "");

        editName = (EditText)mainActivity.findViewById(R.id.name);
        editCode = (EditText)mainActivity.findViewById(R.id.code);
        editDescritpion = (EditText)mainActivity.findViewById(R.id.description);
        editBarcode = (ImageView)mainActivity.findViewById(R.id.barcode);
        editlogo = (ImageView)mainActivity.findViewById(R.id.logoCard);

        editName.setText(card.name);
        editCode.setText(card.code);
        editDescritpion.setText(card.description);

        Glide.with(mainActivity)
                .load(card.logo)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editlogo);
        Glide.with(mainActivity)
                .load(card.barcode)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editBarcode);

        editlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card.dispatchTakePictureIntent();
            }
        });
/*
        Button save = (Button)mainActivity.findViewById(R.id.save_card);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.name = editName.getText().toString();
                card.code = editCode.getText().toString();
                card.description = editDescritpion.getText().toString();
                card.save();
                getFragmentManager().popBackStack();

            }
        });
        /**/
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
