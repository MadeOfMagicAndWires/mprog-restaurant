package online.madeofmagicandwires.restaurant;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_ITEM_BUNDLE_KEY = "detail_item";

    private RestaurantMenuItem menuItem = null;


    /**
     * OnCreate method. Called when the activity views are about to be drawn
     *
     * @param savedInstanceState data saved from the previous instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        /* Retrieve menu item to show from either saved instance state or intent **/
        if(savedInstanceState != null && savedInstanceState.containsKey(DETAIL_ITEM_BUNDLE_KEY)) {
            menuItem = (RestaurantMenuItem) savedInstanceState.getSerializable(DETAIL_ITEM_BUNDLE_KEY);
        }
        if(menuItem == null) {
            menuItem = (RestaurantMenuItem) getIntent().getSerializableExtra(DETAIL_ITEM_BUNDLE_KEY);
        }

        Log.d("DetailActivity", menuItem.toString());

        /* update toolbar based on data **/
        CollapsingToolbarLayout toolbarRoot = findViewById(R.id.toolbar_root);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(menuItem != null) {
            toolbarRoot.setTitle(menuItem.getName());
            ImageView img = findViewById(R.id.toolbar_img);
            MenuItemsRequest.attachImageTo(menuItem.getImgUrl(), img);
        } else {
            toolbarRoot.setTitle(getString(R.string.detail_no_menu_item_error));
        }
        setSupportActionBar(toolbar);


        FragmentTransaction changes = getSupportFragmentManager().beginTransaction();
        changes.replace(R.id.detail_fragment, DetailFragment.newInstance(menuItem));
        changes.commit();
    }

    /**
     * Saves the state of the menu item to be retrieved at a later point in the activity lifecycle
     *
     * @param outState data saved for the next instance
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(DETAIL_ITEM_BUNDLE_KEY, menuItem);
    }
}
