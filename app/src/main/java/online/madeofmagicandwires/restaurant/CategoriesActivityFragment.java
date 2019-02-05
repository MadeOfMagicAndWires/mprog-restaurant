package online.madeofmagicandwires.restaurant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment used to show the menu's categories.
 */
@SuppressWarnings("ConstantConditions") // isAdded() makes sure getActivity != null
public class CategoriesActivityFragment extends ListFragment implements CategoriesRequest.Callback {

    public static final String CATEGORY_BUNDLE_KEY = "category";

    /** empty list to used by the ListView and added to at a later point **/
    private static List<String> mCategories = new ArrayList<>();



    public CategoriesActivityFragment() {
        super();
    }

    /**
     * Draw and bind the fragment layout
     * @param inflater used to inflate fragment layout
     * @param container root view fragment is to be eventually inflated inside; Nullable
     * @param savedInstanceState saved Data used to bind to views
     * @return inflated Fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_categories, container, false);
        ListView lv = v.findViewById(android.R.id.list);
        if(getListAdapter() == null) {
            CategoryAdapter adapter = new CategoryAdapter(
                    inflater.getContext(),
                    android.R.layout.simple_list_item_1,
                    mCategories);
            setListAdapter(adapter);
        }
        lv.setEmptyView(v.findViewById(R.id.emptyState));

        return v;
    }

    /**
     * Updates the adapter's data when this fragment is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        if(isAdded()) {
            CategoriesRequest request = CategoriesRequest.getInstance(getActivity());
            request.getCategories(this);
        }
    }

    /**
     * Callback method called when the Categories Request from the API resolved successfully
     * Binds the returned data to ListView.
     *
     * @param categories list of categories returned by the Categories Request
     */

    @Override
    public void onReceivedCategories(List<String> categories) {
        // only update data if returned categories differ
        if(mCategories != null || !mCategories.equals(categories)) {
            mCategories = categories;

            if (getListAdapter() instanceof CategoryAdapter) {
                CategoryAdapter adapter = (CategoryAdapter) getListAdapter();
                adapter.clear();
                // Add "All" category in order to show all menu items, regardless of category;
                // this is not saved in the categories variable, so it does not mess up the equals()
                adapter.insert(MenuItemsRequest.MENU_NO_FILTER, 0);
                adapter.addAll(categories);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Callback method called when the Categories Request from the API failed
     * Updates the related views if possible or simply logs the error otherwise
     *
     * @param errorMsg the returned error message explaining what went wrong.
     */
    @Override
    public void onReceivedCategoriesError(String errorMsg) {
        // if this fragment is added to an activity update views, else just log error
        if(isAdded()) {
            // create popup message
            Snackbar msg = Snackbar.make(getListView(), errorMsg, Snackbar.LENGTH_LONG);
            // add retry action
            //noinspection ConstantConditions
            msg.setAction(R.string.retry,
                    new RestaurantApiRequest.RetryRequestOnClick(
                            CategoriesRequest.getInstance(getActivity()),
                            this));

            // show popup
            msg.show();

            // update emptyStateView and clear results
            View emptyState = getListView().getEmptyView();
            emptyState.findViewById(R.id.emptyStateProgress).setVisibility(View.GONE);
            TextView emptyStateText = emptyState.findViewById(R.id.emptyStateText);
            emptyStateText.setText(getString(R.string.categories_loading_error));
            ((ArrayAdapter) getListAdapter()).clear();
        } else {
            Log.e("onReceivedCategoriesError", errorMsg);
        }
    }


    /**
     * Opens MenuActivity and passes along the clicked category as filter.
     * If the clicked filter is {@link MenuItemsRequest#MENU_NO_FILTER} it will show all menu items
     * @param l listView containing clicked item
     * @param v clicked item
     * @param position position of clicked item within the ListView
     * @param id id of the clicked item within the data array
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (getListAdapter() instanceof CategoryAdapter) {
            CategoryAdapter adapter = (CategoryAdapter) getListAdapter();
            String category = (String) adapter.getItem(position);

            if (isAdded()) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                if (category != null) {
                    intent.putExtra(CATEGORY_BUNDLE_KEY, category);
                }
                getActivity().startActivity(intent);
            }
        }
    }
}

