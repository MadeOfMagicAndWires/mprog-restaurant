package online.madeofmagicandwires.restaurant;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MenuItemsRequest extends RestaurantApiRequest {

    public static final String MENU_NO_FILTER = "all";
    private static final String RESPONSE_ARRAY_KEY = "items";

    private static MenuItemsRequest instance;

    /**
     * Interface that needs to be implemented when a class makes a Menu request from the API
     */
    public interface Callback extends RestaurantApiRequest.Callback {

        /**
         * Called when a menu request successfully retrieved the menu from the API
         *
         * @param restaurantMenuItems List of all the Menu
         */
        public void onReceivedMenu(List<RestaurantMenuItem> restaurantMenuItems);

        /**
         * Called when a menu request returned an error
         * @param errorMsg a short description of what went wrong
         */
        public void onReceivedMenuError(String errorMsg);

    }

    public MenuItemsRequest.Callback mCallbackActivity;

    /**
     * Standard contstructor
     * @param context Activity context used by ...
     */
    private MenuItemsRequest(@NonNull Context context) {
        super(context);
    }


    /**
     * Retrieves the global MenuItemRequest instance
     *
     * @param c the application context needed to create a requestqueue
     * @return the global menuitemrequest instance
     */
    public static MenuItemsRequest getInstance(@NonNull Context c) {
        if(instance == null) {
            instance = new MenuItemsRequest(c);
        }
        return instance;
    }

    /**
     * Requests the restaurant's menu from the API
     *
     * @param callbackActivity acitivty implementing {@link Callback}
     */
    public void getMenu(@NonNull MenuItemsRequest.Callback callbackActivity) {
        makeRequest(Request.Method.GET, ENDPOINT_MENU, (JSONObject) null);
    }

    /**
     * Requests the restaurant's menu from the API, filtering it by category
     *
     * @param callbackActivity class which implements {@link MenuItemsRequest.Callback}
     * @param categoryFilter category from which items are *included*
     */
    public void getMenu(@NonNull MenuItemsRequest.Callback callbackActivity, String categoryFilter) {
        if(mCallbackActivity != callbackActivity) {
            mCallbackActivity = callbackActivity;
        }
        if(categoryFilter == null || categoryFilter.equals(MENU_NO_FILTER)) {
            makeRequest(Request.Method.GET, ENDPOINT_MENU, "");
        } else {
            makeRequest(Request.Method.GET, ENDPOINT_MENU, "?category=" + categoryFilter);
        }
    }

    /**
     * Called when a response is received. Parses JSON response into an array of RestaurantMenuItem objects
     *
     * @param response Successful JSON Response object
     */
    @Override
    public void onResponse(JSONObject response) {
        if(response.has("items")) {
            List<RestaurantMenuItem> items = new ArrayList<>();
            JSONArray resItems = response.optJSONArray(RESPONSE_ARRAY_KEY);
            if(resItems != null && resItems.length() != 0) {
                for (int i = 0; i < resItems.length(); i++) {
                    JSONObject resItem = resItems.optJSONObject(i);
                    items.add(
                            new RestaurantMenuItem(
                                    resItem.optInt("id", 0),
                                    resItem.optString("name"),
                                    resItem.optString("description"),
                                    resItem.optString("image_url"),
                                    resItem.optDouble("price", 0),
                                    resItem.optString("category")
                            )
                    );
                }
                if (!items.isEmpty() && items.size() == resItems.length()) {
                    if(mCallbackActivity != null) {
                        mCallbackActivity.onReceivedMenu(items);
                    }
                    return;
                }
            }

        }
        if(mCallbackActivity != null) {
            mCallbackActivity.onReceivedMenuError("Couldn't parse the JSON Response.");
        }
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error VolleyError containing message and stack
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMsg = "";
        if(error.networkResponse != null) {
            errorMsg += "Error error.networkResponse.statusCode: ";

        }
        errorMsg += error.getLocalizedMessage();
        if(mCallbackActivity != null) {
            mCallbackActivity.onReceivedMenuError(errorMsg);
        }
    }

    /**
     * Retrieves an image from a source and loads it into an ImageView
     *
     * @param source the location of the image to load
     * @param target the target view the image needs to be shown in
     *
     */
    public static void attachImageTo(String source, final ImageView target) {
        if(URLUtil.isValidUrl(source) && target != null) {

            // Target callback did not work correctly so we're pre-fetching drawables
            Drawable error = target.getContext().getDrawable(R.drawable.ic_error);
            error.setTint(target.getContext().getColor(R.color.colorError));
            AnimatedVectorDrawable placeholder = (AnimatedVectorDrawable) target.getContext().getDrawable(R.drawable.ic_loop_animated);
            placeholder.start();

            Picasso.get().load(source)
                    .placeholder(placeholder)
                    .error(error)
                    .into(target);
        }
    }
}
