package online.madeofmagicandwires.restaurant;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements OrderRequest.OnOrderRequest {

    /**
     * event listener that updates the total cost of the potential order when the amount of items has been changed
     *
     */
    private static class OnOrderAmountChangeListener implements NumberPicker.OnValueChangeListener {

        private TextView costView;
        private double itemPrice;

        /**
         * standard constructor for this event listener
         * @param costView a textview in which the total cost of the order is to be displayed
         * @param price the price of the ordered item
         */
        public OnOrderAmountChangeListener(TextView costView, double price) {
            this.costView = costView;
            this.itemPrice = price;
        }

        /**
         * Called upon a change of the current value. Updates the total when called
         *
         * @param picker The NumberPicker associated with this listener.
         * @param oldVal The previous value.
         * @param newVal The new value.
         */
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if(newVal != oldVal) {
                costView.setText(
                        String.format(
                                costView.getContext().getString(R.string.item_price_format), // %s%.2f
                                costView.getContext().getString(R.string.valuta), // e.g. "€"
                                (newVal * itemPrice) // total price of all items
                        )
                );
            }

        }

    }

    /**
     * Event listener that makes an order request when a view is clicked
     */
    private static class OrderItemOnClick implements View.OnClickListener {

        private OrderRequest.OnOrderRequest callback;
        private NumberPicker amount;
        private RestaurantMenuItem mItem;

        /**
         * Most precise constructor.
         * Use this if your observable view does not have tag with a RestaurantMenuItem.
         *
         * @param callback callback interface to notify when the request is resolved.
         * @param item the item to order
         * @param amount numberpicker representing the amount of items to order
         */
        public OrderItemOnClick(@NonNull OrderRequest.OnOrderRequest callback, RestaurantMenuItem item, NumberPicker amount) {
            this.callback = callback;
            this.amount = amount;
            this.mItem = item;

        }

        /**
         * Standard constructor.
         *
         * @param callback the callback interface to notify when the request is resolved
         * @param amount a numberpicker representing the amount of items to order
         */
        public OrderItemOnClick(@NonNull OrderRequest.OnOrderRequest callback, NumberPicker amount) {
            this(callback, null, amount);
        }

        /**
         * Called when a view has been clicked.
         *
         * Orders the menu item associated with the view, makes a request to order the item,
         * and shows the preparation time.
         *
         * @param v The view that was clicked. Should have a tag containing a
         *          RestaurantMenuItem on key {@link R.id.order_item_key}
         */
        @Override
        public void onClick(View v) {
            // retrieve associated item
            RestaurantMenuItem item = (mItem != null) ? mItem : (RestaurantMenuItem) v.getTag(R.id.order_item_key);
            if(item != null) {
                OrderRequest req = OrderRequest.getInstance(v.getContext());
                req.makeOrder(callback, item.getId(), amount.getValue());
            }
        }
    }

    private RestaurantMenuItem mItem;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(RestaurantMenuItem item) {

        Bundle args = new Bundle();
        args.putSerializable(DetailActivity.DETAIL_ITEM_BUNDLE_KEY, item);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments() != null) {
            mItem = (RestaurantMenuItem) getArguments().getSerializable(DetailActivity.DETAIL_ITEM_BUNDLE_KEY);
        } else {
            mItem = null;
        }
        Log.d("DetailActivity", mItem.toString());


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_detail, container, false);
        bindData(v, mItem);

        return v;
    }


    /**
     * Shows  the data from the menu item in their respective views.
     * @param v the root view created by OnCreateView
     * @param item the menu item returned from the parent activity if passed along.
     */
    public void bindData(@NonNull View v, @Nullable RestaurantMenuItem item) {
        TextView description = v.findViewById(R.id.detailText);
        NumberPicker orderNo = v.findViewById(R.id.orderNumber);
        Button btn = v.findViewById(R.id.orderButton);
        TextView cost = v.findViewById(R.id.orderPrice);

        // init numberpicker
        orderNo.setMinValue(0);
        orderNo.setMaxValue(40);


        if(item != null) {
            // show the description
            description.setText(item.getDesc());


            // change the amount to be ordered and add a listener to update the total price
            orderNo.setMinValue(1);
            orderNo.setValue(1);
            OnOrderAmountChangeListener listener = new OnOrderAmountChangeListener(cost, item.getPrice());
            orderNo.setOnValueChangedListener(listener);

            // manually call listener to update costView the first time
            listener.onValueChange(orderNo, 0, 1);

            // set listener for order button.
            btn.setTag(R.id.order_item_key, item);
            btn.setOnClickListener(new OrderItemOnClick(this, orderNo));


        } else  {
            // show lack of data.
            description.setText(getString(R.string.detail_no_menu_item_error));
            orderNo.setValue(0);
        }
    }


    @Override
    public void onReceivedOrderResponse(int remainingTime) {
        if(isAdded()) {
            Snackbar sb = Snackbar.make(
                    getView(), // view necessary for creation
                    String.format(
                            getString(R.string.order_sent_msg),
                            remainingTime
                    ),
                    Snackbar.LENGTH_LONG);
            sb.show();
        }
    }

    @Override
    public void onReceivedOrderError(String errorMsg) {
        if(isAdded()) {
            Snackbar sb = Snackbar.make(
                    getView(), // view necessary for creation, we happen to have a numberpicker on hand
                    errorMsg,
                    Snackbar.LENGTH_LONG
            );

            sb.setAction(
                    "Retry",
                    new OrderItemOnClick(
                            this,
                            mItem, // global restaurant item associated with this fragment
                            (NumberPicker) getView().findViewById(R.id.orderNumber)
                    )
            );
        }
    }

}
