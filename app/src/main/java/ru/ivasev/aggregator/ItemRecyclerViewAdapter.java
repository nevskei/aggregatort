package ru.ivasev.aggregator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ru.ivasev.aggregator.ItemFragment.OnListFragmentInteractionListener;
import ru.ivasev.aggregator.data.Card;

import java.util.List;

import static ru.ivasev.aggregator.controller.FragmentController.ARG_PARAM_ID;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<Card> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public ItemRecyclerViewAdapter(Context context, List<Card> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
        Log.i("list",""+mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.name);
        if(holder.mItem.color != "")
            holder.mView.setBackgroundColor(Integer.parseInt(holder.mItem.color));
        Glide.with(context)
            .load(holder.mItem.logo)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(holder.mContentView);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Card image = mValues.get(position);

            MainActivity mainActivity = (MainActivity)context;
            Bundle args = new Bundle();
            args.putLong(ARG_PARAM_ID, image.getId());
            mainActivity.showForm(v, args);
            }
        });

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card image = mValues.get(position);
                if (null != image) {
                    mValues.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mValues.size());
                    image.delete();
                }
            }
        });
        holder.mIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card image = mValues.get(position);

                MainActivity mainActivity = (MainActivity)context;
                Bundle args = new Bundle();
                args.putLong(ARG_PARAM_ID, image.getId());
                mainActivity.showPhoto(v, args);
            }
        });
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card image = mValues.get(position);

                MainActivity mainActivity = (MainActivity)context;
                Bundle args = new Bundle();
                args.putLong(ARG_PARAM_ID, image.getId());
                mainActivity.showPhoto(v, args);
            }
        });



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView mContentView;
        public final ImageButton editButton;
        public final ImageButton delButton;
        public Card mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.nameCard);
            mContentView = (ImageView) view.findViewById(R.id.logoCard);
            editButton = (ImageButton) view.findViewById(R.id.editCard);
            delButton = (ImageButton) view.findViewById(R.id.delCard);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
