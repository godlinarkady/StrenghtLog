package com.example.agodlin.strengthlog.ui.weight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agodlin.strengthlog.ui.exercise.ExerciseRecyclerViewAdapter;
import com.example.agodlin.strengthlog.ui.weight.BodyWeightFragment.OnListFragmentInteractionListener;
import com.example.agodlin.strengthlog.ui.weight.dummy.BodyWeightContent.BodyWeightItem;
import com.example.agodlin.strengthlog.R;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BodyWeightItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBodyWeightRecyclerViewAdapter extends RecyclerView.Adapter<MyBodyWeightRecyclerViewAdapter.ViewHolder> {

    private final List<BodyWeightItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBodyWeightRecyclerViewAdapter(List<BodyWeightItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bodyweight, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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
        public final TextView mContentView;
        public BodyWeightItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.weight);
            mContentView = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
