package online.madeofmagicandwires.restaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess")
public class OrderRequest extends RestaurantApiRequest {

    private static final String ORDER_RESPONSE_KEY = "preparation_time";

    /**
     * Callback interface for order request
     */
    public interface OnOrderRequest extends Callback {
        /**
         * Called when an order request resolved successfully
         *
         * @param remainingTime the remaining time until the order arrives
         */
        void onReceivedOrderResponse(int remainingTime);

        /**
         * Called when an order request returned an error.
         *
         * @param errorMsg a short description of what went wrong.
         */
        void onReceivedOrderError(String errorMsg);
    }


    private static OrderRequest instance;
    private OrderRequest.OnOrderRequest mCallback;


    /**
     * Standard Constructor; do not use
     *
     * @param context application context used to create a requestqueue
     * @see #getInstance(Context)
     */
    private OrderRequest(@NonNull Context context) {
        super(context);
    }

    /**
     * Returns the global OrderRequest instance
     *
     * @param c application instance used to create a Volley requestqueue
     * @return the global OrderRequest instance
     */
    public static OrderRequest getInstance(@NonNull Context c) {
        if(instance == null) {
            instance = new OrderRequest(c);
        }
        return instance;
    }

    /**
     * Make an order request to the API.
     *
     * Unfortunately the documentation on the order requests was rather lacking,
     * and it seems to send the same result back no matter what data you put in,
     * so I just made something up.
     *
     *
     * @param callback callback interface to notify once the request has completed
     * @param orderId the id of the item you want to order
     * @param amount  the amount of the items you want to order
     */
    public void makeOrder(@NonNull OnOrderRequest callback, int orderId, int amount) {
        if(mCallback != callback) {
            mCallback = callback;
        }
        JSONObject data = new JSONObject();
        try {
            data.put("id", orderId);
            data.put("amount", amount);
            makeRequest(Request.Method.POST, ENDPOINT_ORDER, data);
        } catch (JSONException e) {
            if(mCallback != null) {
                mCallback.onReceivedOrderError("Could not complete request:" + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Called when a response is received.
     * Parses the JSON Response into an array of Strings representing each category.
     *
     * @param response response object
     */
    @Override
    public void onResponse(JSONObject response) {
        Log.d("onResponse", response.toString());
        if(response.has(ORDER_RESPONSE_KEY)) {
            if(mCallback != null) {
                mCallback.onReceivedOrderResponse(response.optInt(ORDER_RESPONSE_KEY));
                return;
            }
        }
        if(mCallback != null) {
            mCallback.onReceivedOrderError("Could not parse JSON Response");
        }
    }

    /**
     * OnMenuRequest method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error exception containing information on what went wrong
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMsg = "";
        if(error.networkResponse != null) {
            errorMsg += "Error error.networkResponse.statusCode: ";

        }
        errorMsg += error.getLocalizedMessage();
        if(mCallback != null) {
            mCallback.onReceivedOrderError(errorMsg);
        }
    }
}
