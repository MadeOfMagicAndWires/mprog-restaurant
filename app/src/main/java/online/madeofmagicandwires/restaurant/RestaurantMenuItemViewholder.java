package online.madeofmagicandwires.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

public class RestaurantMenuItemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public RestaurantMenuItemViewholder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        PopupMenu overflow = new PopupMenu(itemView.getContext(), v);
        overflow.inflate(R.menu.list_entry_overflow);
        overflow.setOnMenuItemClickListener(this);
        overflow.show();
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
                Context c = itemView.getContext();
                Intent intent = new Intent(c, DetailActivity.class);
                if(itemView.getTag() instanceof RestaurantMenuItem) {
                    RestaurantMenuItem rItem = (RestaurantMenuItem) itemView.getTag();
                    intent.putExtra(DetailActivity.DETAIL_ITEM_BUNDLE_KEY, rItem);
                }
                if(itemView.getContext() instanceof Activity) {
                    Activity a = (Activity) c;
                    a.startActivity(intent);
                }
                return true;
            case R.id.menu_item_action_order:
                return false;
        }
        return false;
    }
}
