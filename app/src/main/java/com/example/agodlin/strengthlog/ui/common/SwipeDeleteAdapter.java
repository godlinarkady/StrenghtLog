package com.example.agodlin.strengthlog.ui.common;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.example.agodlin.strengthlog.R;
import com.example.agodlin.strengthlog.ui.exercises.ExerciseNameRecyclerViewAdapter;
import com.example.agodlin.strengthlog.ui.exercises.ExercisesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by agodlin on 3/3/2018.
 */

public class SwipeDeleteAdapter<T> extends RecyclerView.Adapter{
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private final ExercisesFragment.OnListFragmentInteractionListener mListener;
    List<T> mItems;
    List<T> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more mItems for testing purposes
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    int mLayoutId;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<T, Runnable> pendingRunnables = new HashMap<>(); // map of mItems to pending runnables, so we can cancel a removal if need be

    public SwipeDeleteAdapter(List<T> items, ExercisesFragment.OnListFragmentInteractionListener listener, int layoutId) {
        mItems = items;
        itemsPendingRemoval = new ArrayList<>();
        mListener = listener;
        mLayoutId = layoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swipe_delete, parent, false);
        return new TestViewHolder(view, mLayoutId);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestViewHolder viewHolder = (TestViewHolder)holder;
        final T item = mItems.get(position);
        viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item.toString());
                }
            }
        });
        if (itemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.stub.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mItems.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.stub.setVisibility(View.VISIBLE);
            viewHolder.titleTextView.setText(item.toString());
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final T item = mItems.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mItems.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        T item = mItems.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (mItems.contains(item)) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        T item = mItems.get(position);
        return itemsPendingRemoval.contains(item);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ViewStub stub;
        Button undoButton;

        public TestViewHolder(View parent, int layoutId) {
            super(parent);
            undoButton = (Button) parent.findViewById(R.id.undo_button);
            stub = (ViewStub) parent.findViewById(R.id.stub_import);
            stub.setLayoutResource(layoutId);
            stub.inflate(); // inflate 1st layout
            titleTextView = (TextView)parent.findViewById(R.id.title_text_view);
        }
    }
}
