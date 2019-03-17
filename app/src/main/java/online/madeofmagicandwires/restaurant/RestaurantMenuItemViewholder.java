package online.madeofmagicandwires.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

public class RestaurantMenuItemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, OrderRequest.OnOrderRequest {

    private static class OpenOverFlowOnClick implements View.OnClickListener {

        private PopupMenu.OnMenuItemClickListener listener;

        /**
         * Standard constructor
         * @param listener OnMenuItemClickListener interface
         *                 implementing what to do when the overflow menu items are clicked
         */
        public OpenOverFlowOnClick(PopupMenu.OnMenuItemClickListener listener) {
            this.listener = listener;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            PopupMenu overflow = new PopupMenu(v.getContext(), v);
            overflow.inflate(R.menu.list_entry_overflow);
            overflow.setOnMenuItemClickListener(listener);
            overflow.show();

        }

    }

    public RestaurantMenuItemViewholder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        OpenOverFlowOnClick listener = new OpenOverFlowOnClick(this);
        itemView.findViewById(R.id.itemOverflowMenu).setOnClickListener(listener);
    }

    /**
     * Called when this view has been clicked.
     * Opens DetailActivity.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (itemView.getContext() instanceof Activity) {
            Context c = itemView.getContext();
            Intent intent = new Intent(c, DetailActivity.class);
            if (itemView.getTag() instanceof RestaurantMenuItem) {
                RestaurantMenuItem rItem = (RestaurantMenuItem) itemView.getTag();
                intent.putExtra(DetailActivity.DETAIL_ITEM_BUNDLE_KEY, rItem);
            }
            Activity a = (Activity) c;
            a.startActivity(intent);
        }
    }

    /**
     * Called when a menu item has been invoked.  This is the first code
     * that is executed; if it returns true, no other callbacks will be
     * executed.
     *
     * @param item The menu item that was invoked.
     * @return Return true to consume this click and prevent others from
     * executing.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_action_details:
                onClick(itemView);
                return true;
            case R.id.menu_item_action_order:
                if (itemView.getTag() instanceof RestaurantMenuItem) {
                    RestaurantMenuItem rItem = (RestaurantMenuItem) itemView.getTag();
                    OrderRequest request = OrderRequest.getInstance(itemView.getContext());
                    request.makeOrder(this, rItem.getId(), 1);
                }

        }
        return false;
    }

    /**
     * Called when an order request resolved successfully
     *
     * @param remainingTime the remaining time until the order arrives
     */
    @Override
    public void onReceivedOrderResponse(int remainingTime) {
        Snackbar sb = Snackbar.make(
                itemView,
                String.format(
                        itemView.getContext().getString(R.string.order_sent_msg),
                        remainingTime
                ),
                Snackbar.LENGTH_LONG
        );
        sb.show();

    }

    /**
     * Called when an order request returned an error.
     *
     * @param errorMsg a short description of what went wrong.
     */
    @Override
    public void onReceivedOrderError(String errorMsg) {
        Snackbar sb = Snackbar.make(
                itemView, // view necessary for creation, we happen to have a numberpicker on hand
                errorMsg,
                Snackbar.LENGTH_LONG
        );
        sb.show();

    }
}
