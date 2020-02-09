package com.lina.securify.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.ListItemVolunteerBinding;
import com.lina.securify.utils.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class VolunteersAdapter
        extends FirestoreRecyclerAdapter<Volunteer, VolunteersAdapter.VolunteerViewHolder> {

    private SelectionTracker<Long> tracker;

    public VolunteersAdapter(
            FirestoreRecyclerOptions<Volunteer> options,
            RecyclerView recyclerView) {

        super(options);

        setHasStableIds(true);

        recyclerView.setAdapter(this);

        // Build the SelectionTracker
        tracker = new SelectionTracker.Builder<Long>(
                Constants.VOLUNTEERS_SELECTION_ID,
                recyclerView,
                new VolunteerItemKeyProvider(recyclerView, this),
                new VolunteerItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage() )
                .withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
                .build();
    }

    public SelectionTracker<Long> getTracker() {
        return tracker;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemVolunteerBinding binding = ListItemVolunteerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        );

        return new VolunteerViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position, @NonNull Volunteer model) {
        holder.bind(model, tracker.isSelected(model.getId()));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public List<String> getSelectedPhones() {

        if (tracker.hasSelection()) {

            List<String> phones = new ArrayList<String>();

            for (Volunteer volunteer : getSnapshots()) {

                if (tracker.isSelected(volunteer.getId()))
                    phones.add(volunteer.getPhone());
            }

            return phones;
        }
        else
            return null;
    }

    // Classes for SelectionTracker
    public static class VolunteerItemDetailsLookup extends ItemDetailsLookup<Long> {

        private RecyclerView recyclerView;

        public VolunteerItemDetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {

            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());

            if (view != null) {

                if (recyclerView.getChildViewHolder(view) instanceof VolunteerViewHolder) {

                    return ((VolunteerViewHolder) recyclerView.getChildViewHolder(view))
                            .itemDetails();

                }
            }

            return null;
        }

    }

    public static class VolunteerItemKeyProvider extends ItemKeyProvider<Long> {

        private RecyclerView recyclerView;

        public VolunteerItemKeyProvider(
                RecyclerView recyclerView,
                VolunteersAdapter adapter) {
            super(SCOPE_MAPPED);

            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public Long getKey(int position) {

            if (recyclerView.getAdapter() != null)
                return recyclerView.getAdapter().getItemId(position);
            else
                return null;
        }

        @Override
        public int getPosition(@NonNull Long key) {

            RecyclerView.ViewHolder viewHolder = recyclerView
                    .findViewHolderForItemId(key);

            if (viewHolder != null)
                return viewHolder.getLayoutPosition();
            else
                return RecyclerView.NO_POSITION;
        }
    }

    /**
     * A custom ViewHolder for holding the Volunteer view.
     */
    static class VolunteerViewHolder extends RecyclerView.ViewHolder {

        private ListItemVolunteerBinding binding;

        VolunteerViewHolder(ListItemVolunteerBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(Volunteer volunteer, boolean isActivated) {
            binding.setVolunteer(volunteer);
            binding.getRoot().setActivated(isActivated);
        }

        ItemDetailsLookup.ItemDetails<Long> itemDetails() {

            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };

        }
    }

}
