package online.madeofmagicandwires.restaurant;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("WeakerAccess,unused")
class RestaurantMenuItemAdapter extends RecyclerView.Adapter<RestaurantMenuItemViewholder> {

    private static final @LayoutRes int DEFAULT_LAYOUT = R.layout.menu_items_list_entry;

    private final List<RestaurantMenuItem> items;
    private final LayoutInflater inflater;
    private @LayoutRes
    final int layout;


    /**
     * Class that implements a call to be made when the adapter's state changes;
     * implements emptyStateView functionality for RecyclerView
     */
    public static class OnMenuItemAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        private final RecyclerView mRecycler;
        private final View mEmptyState;

        public OnMenuItemAdapterDataObserver(@NonNull RecyclerView aRecyclerView, View emptyState) {
            super();
            mRecycler = aRecyclerView;
            mEmptyState = emptyState;
        }

        @Override
        public void onChanged() {
            RecyclerView.Adapter<?> adapter = mRecycler.getAdapter();
            if(adapter instanceof RestaurantMenuItemAdapter && mEmptyState != null) {
                if(((RestaurantMenuItemAdapter) adapter).isEmpty()) {
                    mRecycler.setVisibility(View.GONE);
                    mEmptyState.setVisibility(View.VISIBLE);
                } else {
                    mRecycler.setVisibility(View.VISIBLE);
                    mEmptyState.setVisibility(View.GONE);
                }

            }
            super.onChanged();
        }
    }


    public RestaurantMenuItemAdapter(@NonNull Context context, @LayoutRes int layout, List<RestaurantMenuItem> objs) {
        super();
        this.items = objs;
        inflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    @NonNull
    @Override
    public RestaurantMenuItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v;
        if(layout != 0) {
            v = inflater.inflate(layout, parent, false);
        } else {
            v = inflater.inflate(DEFAULT_LAYOUT, parent, false);
        }
        return new RestaurantMenuItemViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantMenuItemViewholder vHolder, int position) {
        TextView name = vHolder.itemView.findViewById(R.id.itemTitle);
        TextView desc  = vHolder.itemView.findViewById(R.id.itemDesc);
        TextView price = vHolder.itemView.findViewById(R.id.itemPrice);
        ImageButton overflowMenu = vHolder.itemView.findViewById(R.id.itemOverflowMenu);
        ImageView img = vHolder.itemView.findViewById(R.id.itemImg);

        RestaurantMenuItem item = items.get(position);
        if(item != null) {
            // Bind text to view holder
            name.setText(item.getName());
            desc.setText(item.getDesc());
            Context c = vHolder.itemView.getContext();
            String itemPrice = String.format(
                    c.getString(R.string.item_price_format),
                    c.getString(R.string.valuta),
                    item.getPrice());
            price.setText(itemPrice);

            // bind RestaurantMenuItem as tag and add the overflow menu onClickListener
            vHolder.itemView.setTag(item);

            // bind RestaurantMenuItem's image
            MenuItemsRequest.attachImageTo(item.getImgUrl(), img);


        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Add items to the inner List
     * @param item the item to be added to the list
     * @return always true
     */
    public boolean add(RestaurantMenuItem item) {
        return items.add(item);
    }

    /**
     * Concat a collection into the inner list
     * @param collection A Collection of items to be added to the adapter's list
     * @return true if the inner list was modified, false otherwise
     */
    public boolean addAll(Collection<? extends RestaurantMenuItem> collection) {
        return items.addAll(collection);
    }

    /**
     * Remove all child elements from the inner list
     */
    public void clear() {
        items.clear();
    }

    /**
     * checks if the inner list is empty
     * @return true if the list is empty, false otherwise
     */
    private boolean isEmpty() {
        return items.isEmpty();
    }
}

