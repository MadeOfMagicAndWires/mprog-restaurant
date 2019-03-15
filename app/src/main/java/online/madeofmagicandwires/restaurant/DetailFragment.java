package online.madeofmagicandwires.restaurant;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class DetailFragment extends Fragment {

    /**
     * event listener that updates the total cost of the potential order when the amount of items has been changed
     *
     */
    private static class OnOrderAmountChangeListener implements NumberPicker.OnValueChangeListener {

        private TextView costView;
        private double itemPrice;


        public OnOrderAmountChangeListener(TextView v, double price) {
            this.costView = v;
            this.itemPrice = price;
        }

        /**
         * Called upon a change of the current value.
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


    private static RestaurantMenuItem mItem;


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
        orderNo.setOnValueChangedListener(new OnOrderAmountChangeListener(cost, item.getPrice()));


        if(item != null) {
            // show the description
            description.setText(item.getDesc());


            // change the amount to be ordered and add a listener to update the total price
            orderNo.setMinValue(1);
            orderNo.setOnValueChangedListener(new OnOrderAmountChangeListener(cost, item.getPrice()));
            orderNo.setValue(1);


            //v.setTag(R.id.order_item_key, item);
        } else  {
            // show lack of data.
            description.setText(getString(R.string.detail_no_menu_item_error));
            orderNo.setValue(0);
        }
    }

}
