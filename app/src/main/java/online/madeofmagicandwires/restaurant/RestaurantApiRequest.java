package online.madeofmagicandwires.restaurant;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Abstract superclass containing methods and members for generic JSON requests from the API.
 * Needs to be extended for more specific request methods
 */
abstract class RestaurantApiRequest implements Response.ErrorListener, Response.Listener<JSONObject> {

    /** the root url of the API **/
    private static final String API_URL = "https://resto.mprog.nl/";


    /** API endpoint enumerated annotation **/
    static final String ENDPOINT_CATEGORIES = "categories";
    static final String ENDPOINT_MENU = "menu";
    static final String ENDPOINT_ORDER = "order";

    @Retention(SOURCE)
    @StringDef({
            ENDPOINT_CATEGORIES,
            ENDPOINT_MENU,
            ENDPOINT_ORDER})
    public @interface EndPoint {}

    /**
     * superclass interface to be extended in request subclasses
     */
    interface Callback {

    }

    /** context to be used by Volley **/
    private final RequestQueue queue;


    /**
     * Standard Constructor
     * @param context application context used to create a requestqueue
     */
    RestaurantApiRequest(@NonNull Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * OnMenuRequest method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error exception containing information on what went wrong
     */
    @SuppressWarnings("WeakerAccess")
    @Override
    public abstract void onErrorResponse(VolleyError error);

    /**
     * Called when a response is received.
     * Parses the JSON Response into an array of Strings representing each category.
     *
     * @param response response object
     */
    @SuppressWarnings("unused")
    @Override
    abstract public void onResponse(JSONObject response);


    /**
     * Make a request of the Restaurant API
     * @param endPoint the API endpoint
     * @param params possible POST Parameters; can be null
     */
    void makeRequest(int method,  @EndPoint String endPoint, @Nullable JSONObject params) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    method,
                    API_URL + endPoint,
                    params,
                    this,
                    this
            );
            queue.add(request);


        } catch (NullPointerException e) {
            onErrorResponse(new VolleyError(e.getMessage()));
        }

    }

    void makeRequest(@EndPoint String endPoint, String queryParams) {
        makeRequest(com.android.volley.Request.Method.GET, endPoint + queryParams, null);
    }


}
