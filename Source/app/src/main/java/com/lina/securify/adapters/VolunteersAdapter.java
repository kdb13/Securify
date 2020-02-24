package com.lina.securify.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.ListItemVolunteerBinding;

import java.util.ArrayList;
import java.util.List;

public class VolunteersAdapter
        extends FirestoreRecyclerAdapter<Volunteer, VolunteersAdapter.VolunteerViewHolder> {

    private SelectionTracker<String> selectionTracker;

    public VolunteersAdapter(FirestoreRecyclerOptions<Volunteer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position, @NonNull Volunteer model) {

        holder.bind(model, selectionTracker.isSelected(getItem(position).getPhone()));

    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemVolunteerBinding binding = ListItemVolunteerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new VolunteerViewHolder(binding);
    }

    public void setSelectionTracker(SelectionTracker<String> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public List<String> getSelectedPhones() {

        if (selectionTracker.hasSelection()) {

            List<String> phones = new ArrayList<String>();

            for (Volunteer volunteer : getSnapshots()) {

                if (selectionTracker.isSelected(volunteer.getPhone()))
                    phones.add(volunteer.getPhone());
            }

            return phones;
        }
        else
            return null;
    }

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

        ItemDetailsLookup.ItemDetails<String> getItemDetails() {

            return new ItemDetailsLookup.ItemDetails<String>() {

                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
                @Override
                public String getSelectionKey() {
                    return binding.getVolunteer().getPhone();
                }

            };

        }
    }

    public static class VolunteerKeyProvider extends ItemKeyProvider<String> {

        private VolunteersAdapter adapter;

        public VolunteerKeyProvider(VolunteersAdapter adapter) {
            super(SCOPE_CACHED);
            this.adapter = adapter;
        }

        @Nullable
        @Override
        public String getKey(int position) {

            return adapter.getItem(position).getPhone();

        }

        @Override
        public int getPosition(@NonNull String key) {

            ObservableSnapshotArray<Volunteer> volunteers = adapter.getSnapshots();

            for (int i = 0; i < volunteers.size(); i++) {

                if (volunteers.get(i).getPhone().equals(key)) {
                    return i;
                }

            }

            return RecyclerView.NO_POSITION;
        }

    }

    public static class VolunteerDetailsLookup extends ItemDetailsLookup<String> {

        private RecyclerView recyclerView;

        public VolunteerDetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {

            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());

            if (view != null) {

                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);

                if (holder instanceof VolunteerViewHolder) {
                    return ((VolunteerViewHolder) holder).getItemDetails();
                }

            }

            return null;
        }
    }
}
