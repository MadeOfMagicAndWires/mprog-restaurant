package online.madeofmagicandwires.restaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment containing the list of Menu Items
 */
public class MenuFragment extends Fragment implements MenuItemsRequest.Callback {

    /**
     * event listener that retries the Menu request when clicked.
     */
    private static class RetryMenuRequestOnClick implements View.OnClickListener {

        private final MenuItemsRequest request;
        private final MenuItemsRequest.Callback callbackActivity;

        RetryMenuRequestOnClick(@NonNull MenuItemsRequest request, MenuItemsRequest.Callback callback) {
            this.request = request;
            this.callbackActivity = callback;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            request.getMenu(callbackActivity);
        }
    }

    private static List<RestaurantMenuItem> items;
    private static RestaurantMenuItemAdapter adapter;


    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initRecycler(v);
        return v;
    }

    /**
     * Links the recycler view with its adapter and layout manager
     */
    @SuppressWarnings("ConstantConditions")
    private void initRecycler(View root) {
        RecyclerView recycler = root.findViewById(R.id.menuItemsList);
        View emptyState = root.findViewById(R.id.emptyState);

        if(isAdded()) {
            if(adapter == null) {
                items = new ArrayList<>();
                adapter = new RestaurantMenuItemAdapter(
                        getActivity(),
                        R.layout.menu_items_list_entry,
                        items);
                adapter.registerAdapterDataObserver(
                        new RestaurantMenuItemAdapter.OnMenuItemAdapterDataObserver(
                                recycler,
                                emptyState
                        )
                );
            }
            recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            
            if(recycler.getLayoutManager() == null) {
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                recycler.setLayoutManager(manager);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        super.onResume();

        // Make a new request for the Menu's items
        if(isAdded()) {
            MenuItemsRequest request = MenuItemsRequest.getInstance(getActivity());
            request.getMenu(this, MenuActivity.MENU_FILTER);
        }
    }


    /**
     * Called when a request successfully retrieved the menu from the API
     *
     * @param restaurantMenuItems List of all the Menu
     */
    @Override
    public void onReceivedMenu(List<RestaurantMenuItem> restaurantMenuItems) {
        if(items != null && !items.equals(restaurantMenuItems)) {
            // We don't add any new fake items here so we can edit the saved list directly
            items.clear();
            items.addAll(restaurantMenuItems);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Called when a request resolved into an error
     * @param errorMsg a short description of what went wrong
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceivedMenuError(String errorMsg) {
        Log.e(this.getClass().getName(), errorMsg);
        if(isAdded()) {
            Snackbar sb = Snackbar.make(
                    getActivity().findViewById(R.id.menuItemsList),
                    errorMsg,
                    Snackbar.LENGTH_LONG);
            MenuItemsRequest request = MenuItemsRequest.getInstance(getActivity());
                sb.setAction(R.string.retry,
                        new RetryMenuRequestOnClick(request, this));
        }

    }
}
