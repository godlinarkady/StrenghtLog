package com.example.agodlin.strengthlog.ui.weight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agodlin.strengthlog.ui.common.SwipeViewHolder;
import com.example.agodlin.strengthlog.ui.weight.BodyWeightFragment.OnListFragmentInteractionListener;
import com.example.agodlin.strengthlog.R;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BodyWeightItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BodyWeightRecyclerViewAdapter extends RecyclerView.Adapter<BodyWeightRecyclerViewAdapter.ViewHolder> {
    private OnListFragmentInteractionListener mListener;
    private List<BodyWeightItem> mItems;

    public BodyWeightRecyclerViewAdapter(List<BodyWeightItem> items, OnListFragmentInteractionListener mListener) {
        mItems = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_swipe_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.mItem = this.mItems.get(position);
        holder.mIdView.setText(holder.mItem.date.toString());
        holder.mContentView.setText(String.valueOf(holder.mItem.weight));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(BodyWeightItem item, int position) {
        mItems.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class ViewHolder extends SwipeViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public BodyWeightItem mItem;
        public ViewHolder(View view)
        {
            super(view, R.layout.fragment_bodyweight);
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
