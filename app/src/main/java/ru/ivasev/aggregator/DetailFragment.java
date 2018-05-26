package ru.ivasev.aggregator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.ivasev.aggregator.data.Card;

import static ru.ivasev.aggregator.controller.FragmentController.ARG_PARAM_ID;

public class DetailFragment extends Fragment {

    private Card card;

    private ImageView imageBarcode;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onResume() {
        MainActivity mainActivity = (MainActivity)getActivity();
        Long id = getArguments().getLong(ARG_PARAM_ID);
        List<Card> listCard = Card.getList(mainActivity,null, Card.CardEntry._ID + "= ?",new String[]{Long.toString(id)});
        card = listCard.get(0);

        ImageView imageBarcode = (ImageView)mainActivity.findViewById(R.id.imageBarcode);
        Glide.with(mainActivity)
            .load(card.barcode)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(imageBarcode);
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
