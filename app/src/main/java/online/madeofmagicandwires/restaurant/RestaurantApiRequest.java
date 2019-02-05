package online.madeofmagicandwires.restaurant;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

abstract public class RestaurantApiRequest implements Response.ErrorListener, Response.Listener<JSONObject> {

    /** the root url of the API **/
    public static final String API_URL = "https://resto.mprog.nl/";


    /** API endpoint enumerated annotation **/
    public static final String ENDPOINT_CATEGORIES = "categories";
    public static final String ENDPOINT_MENU = "menu";
    public static final String ENDPOINT_ORDER = "order";

    @Retention(SOURCE)
    @StringDef({
            ENDPOINT_CATEGORIES,
            ENDPOINT_MENU,
            ENDPOINT_ORDER})
    public @interface EndPoint {}

    /**
     * superclass interface to be extended in request subclasses
     */
    public interface Callback {

    }

    /**
     * OnClickListener that retries the request when clicked.
     */
    public static class RetryRequestOnClick implements View.OnClickListener {

        private RestaurantApiRequest request;
        private RestaurantApiRequest.Callback callback;

        public RetryRequestOnClick(@NonNull RestaurantApiRequest request,
                                   Callback callback) {
            this.request = request;
            this.callback = callback;
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if(request != null) {
                if(request instanceof CategoriesRequest && callback instanceof CategoriesRequest.Callback) {
                    ((CategoriesRequest) request).getCategories((CategoriesRequest.Callback) callback);
                } else  if(request instanceof MenuItemsRequest && callback instanceof MenuItemsRequest.Callback) {
                    ((MenuItemsRequest) request).getMenu((MenuItemsRequest.Callback) callback);
                }
            }
        }
    }

    /** context to be used by Volley **/
    private RequestQueue queue;


    /**
     * Standard Constructor
     * @param context application context used to create a requestqueue
     */
    public RestaurantApiRequest(@NonNull Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error exception containing information on what went wrong
     */
    @Override
    abstract public void onErrorResponse(VolleyError error);

    /**
     * Called when a response is received.
     * Parses the JSON Response into an array of Strings representing each category.
     *
     * @param response response object
     */
    @Override
    abstract public void onResponse(JSONObject response);

    /**
     * Retrieves the API endpoint for this type of request
     * @param method the request method used; e.g. "GET"
     * @return String pointing to the API endpoint, sans preceding "/"
     */
    abstract public @EndPoint String getEndPoint(int method);


    /**
     * Make a request of the Restaurant API
     * @param endPoint the API endpoint
     * @param params possible POST Parameters; can be null
     */
    public void makeRequest(int method,  String endPoint, @Nullable  JSONObject params) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    method,
                    API_URL + endPoint,
                    null,
                    this,
                    this);
            queue.add(request);


        } catch (NullPointerException e) {
            onErrorResponse(new VolleyError(e.getMessage()));
        }

    }

    public void makeRequest(int method, String endPoint, String queryParams) {
        makeRequest(method, endPoint + queryParams, (JSONObject) null);
    }


}
