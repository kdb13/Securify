package com.lina.securify.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.ItemVolunteerBinding;

import java.util.Iterator;

public class VolunteersAdapter extends FirestoreRecyclerAdapter<Volunteer, VolunteersAdapter.VolunteerViewHolder> {

    private static final String TAG = "VolunteersAdapter";

    private SelectionTracker<String> selectionTracker;

    public VolunteersAdapter(FirestoreRecyclerOptions<Volunteer> options) {
        super(options);
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVolunteerBinding binding = ItemVolunteerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VolunteerViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull VolunteerViewHolder holder,
                                    int position, @NonNull Volunteer volunteer) {
        String key = getSnapshots().getSnapshot(position).getId();

        holder.bind(volunteer, selectionTracker.isSelected(key));
    }

    public void setSelectionTracker(SelectionTracker<String> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public Iterator<String> getSelectedKeys() {
        MutableSelection<String> selection = new MutableSelection<>();
        selectionTracker.copySelection(selection);

        return selection.iterator();
    }

    public static final class MyItemKeyProvider extends ItemKeyProvider<String> {

        private VolunteersAdapter adapter;

        public MyItemKeyProvider(VolunteersAdapter adapter) {
            super(SCOPE_CACHED);
            this.adapter = adapter;
        }

        @Override
        public String getKey(int position) {
            return adapter.getSnapshots().getSnapshot(position).getId();
        }

        @Override
        public int getPosition(@NonNull String key) {
            for (int i = 0; i < adapter.getSnapshots().size(); i++) {
                if (adapter.getSnapshots().getSnapshot(i).getId().equals(key)) {
                    return i;
                }
            }

            return RecyclerView.NO_POSITION;
        }

    }

    public static final class MyItemDetailsLookup extends ItemDetailsLookup<String> {

        private final RecyclerView recyclerView;

        public MyItemDetailsLookup(final RecyclerView recyclerView) {
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

    /**
     * Holds a Volunteer list item.
     */
    class VolunteerViewHolder extends RecyclerView.ViewHolder {

        private ItemVolunteerBinding binding;

        VolunteerViewHolder(ItemVolunteerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Volunteer volunteer, boolean isActivated) {
            binding.textViewName.setText(volunteer.getName());
            binding.textViewPhone.setText(volunteer.getPhone());
            binding.getRoot().setActivated(isActivated);
        }

        ItemDetailsLookup.ItemDetails<String> getItemDetails() {

            return new ItemDetailsLookup.ItemDetails<String>() {

                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public String getSelectionKey() {
                    return getSnapshots().getSnapshot(getAdapterPosition()).getId();
                }

            };

        }
    }

}
