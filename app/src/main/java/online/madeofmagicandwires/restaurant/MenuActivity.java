package online.madeofmagicandwires.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MenuActivity extends AppCompatActivity {

    public static String MENU_FILTER;

    /**
     * draws the views onto the screen
     * @param savedInstanceState state data saved from previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "was called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // retrieve category filter
        getFilter(savedInstanceState, getIntent());
        Log.d("MenuActivity", "found filter: " + MENU_FILTER);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction changes = fm.beginTransaction();

        MenuFragment main = MenuFragment.newInstance(MENU_FILTER);
        changes.replace(R.id.fragmentContainer, main);
        changes.commit();
        fm.findFragmentById(R.id.menuListFragment);
        fm.findFragmentById(R.id.fragmentContainer);

    }

    /**
     * Saves important state data to be retrieved by a new instance
     * @param outState Bundle of data to be saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(MENU_FILTER != null) {
            outState.putString(CategoriesActivityFragment.CATEGORY_BUNDLE_KEY, MENU_FILTER);
        }
    }

    /**
     * Retrieves the Menu Category filter saved from a previous Activity instance
     * @param savedInstance the bundle of saved data from the previous instance
     * @return retrieved Category filter or null if it wasn't available
     */
    public String getFilter(Bundle savedInstance) {
        String filter = null;
        if(savedInstance != null &&
                savedInstance.containsKey(CategoriesActivityFragment.CATEGORY_BUNDLE_KEY)) {
            filter = savedInstance.getString(CategoriesActivityFragment.CATEGORY_BUNDLE_KEY, null);
        }
        return filter;
    }

    /**
     * Retrieves the Menu Category filter sent along by the previous activity
     * @return the category filter packaged within the activity intent or
     * {@link MenuItemsRequest#MENU_NO_FILTER} in case it was not packaged.
     */
    public String getFilter(Intent intent) {
        String filter = null;
        if(intent.hasExtra(CategoriesActivityFragment.CATEGORY_BUNDLE_KEY)) {
            filter = intent.getStringExtra(CategoriesActivityFragment.CATEGORY_BUNDLE_KEY);
            if(filter == null) {
                filter = MenuItemsRequest.MENU_NO_FILTER;
            }
        } else {
            filter = MenuItemsRequest.MENU_NO_FILTER;
        }
        return filter;
    }

    /**
     * Retrieves the filter from a previously saved instance, or the intent if the former isn't available
     * Saves the filter as {@link MenuItemsRequest#MENU_NO_FILTER}
     * if there is no filter present in either
     * @param savedInstanceState bundle of data saved from a previous instance
     * @param intent activity intent
     */
    public void getFilter(Bundle savedInstanceState, Intent intent) {
        MENU_FILTER = getFilter(savedInstanceState);
        if(MENU_FILTER == null){
            MENU_FILTER = getFilter(intent);
        }
    }
}
