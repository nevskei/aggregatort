package ru.ivasev.aggregator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

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
        Long id = getArguments().getLong(ARG_PARAM_ID);
        List<Card> listCard = Card.getList(mainActivity,null, Card.CardEntry._ID + "= ?",new String[]{Long.toString(id)});
        if (card == null || card.getId() != id) {
            if (!listCard.isEmpty())
                card = listCard.get(0);
            else
                card = new Card(mainActivity, "", "", "", "", "", "");
        }

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
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editlogo);
        Glide.with(mainActivity)
                .load(card.barcode)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editBarcode);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                int before, int count) {
                if(s.length() != 0)
                    card.name = editName.getText().toString();
            }
        });
        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    card.code = editCode.getText().toString();
            }
        });
        editDescritpion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    card.description = editDescritpion.getText().toString();
            }
        });

        editBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card.dispatchTakePictureIntent(Card.REQUEST_TAKE_BARCODE);
            }
        });


        editlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card.dispatchTakePictureIntent(Card.REQUEST_TAKE_LOGO);
            }
        });

        Button save = (Button)mainActivity.findViewById(R.id.save_card);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.save();
                getFragmentManager().popBackStack();

            }
        });
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setFile(Bitmap thumbnailBitmap, int code) {
        MainActivity mainActivity = (MainActivity)getActivity();
        card.setFile(thumbnailBitmap, code);

        if (code == Card.REQUEST_TAKE_LOGO)
            Glide.with(mainActivity)
                .load(card.logo)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editlogo);
        else if (code == Card.REQUEST_TAKE_BARCODE)
            Glide.with(mainActivity)
                .load(card.barcode)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(editBarcode);
    }

}
