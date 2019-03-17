package online.madeofmagicandwires.restaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRequest extends RestaurantApiRequest {

    /**
     * The interface that needs to be implemented when a class makes a Categories Request from an API
     */
    public interface Callback extends RestaurantApiRequest.Callback {

        /**
         * Callback method called when the Categories Request from the API resolved successfully
         * @param categories list of categories returned by the Categories Request
         */
        void onReceivedCategories(List<String> categories);

        /**
         * Callback method called when the Categories Request from the API failed
         * @param errorMsg the returned error message explaining what went wrong.
         */
        void onReceivedCategoriesError(String errorMsg);
    }

    public static final String RESPONSE_ARRAY_KEY = "categories";
    public static CategoriesRequest instance;

    private static CategoriesRequest.Callback mCallbackActivity;

    /**
     * Standard constructor
     * @param context Activity context to be used by ...
     */
    private CategoriesRequest(@NonNull Context context) {
        super(context);
    }


    /**
     * Retrieves a CategoriesRequest instance
     * @param context Application context used by Volley
     * @return the global CategoriesRequest instance
     */
    public static CategoriesRequest getInstance(@NonNull Context context) {
        if(instance == null) {
            instance = new CategoriesRequest(context);
        }
        return instance;
    }

    /**
     * Requests the restaurant's menu categories from the API
     * @param callbackActivity activity implementing {@link Callback}
     */
    public void getCategories(CategoriesRequest.Callback callbackActivity) {
        // if callbackactivity is not set already or is different, do so now
        if(!callbackActivity.equals(mCallbackActivity)) {
            mCallbackActivity = callbackActivity;
        }
        makeRequest(Request.Method.GET, RestaurantApiRequest.ENDPOINT_CATEGORIES, "");

    }


    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error exception instance containing information on what went wrong
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        // HTTP status code + message;
        // e.g. "Error 404: File Not Found."
        StringBuilder errorMsg = new StringBuilder();
        if(error.networkResponse != null) {
            errorMsg.append("Error ");
            errorMsg.append(error.networkResponse.statusCode);
            errorMsg.append(": ");
        }
        errorMsg.append(error.getLocalizedMessage());
        mCallbackActivity.onReceivedCategoriesError(errorMsg.toString());
    }

    /**
     * Called when a response is received.
     *
     * @param response JSON response
     */
    @Override
    public void onResponse(JSONObject response) {
        try {
            // retrieve and init JSON and categories arrays
            JSONArray responseCategories = response.getJSONArray(RESPONSE_ARRAY_KEY);
            List<String> categories = new ArrayList<>();

            // add each element from the response to the categories array
            for(int i=0;i<responseCategories.length();i++){
                categories.add(responseCategories.optString(i));
            }
            if(categories.size() == responseCategories.length()) {
                mCallbackActivity.onReceivedCategories(categories);

            }
        } catch (JSONException e) {
            // error message: "An error occurred parsing the JSON Response"
            mCallbackActivity.onReceivedCategoriesError("Couldn't parse the JSON Response.");
        }
    }
}
