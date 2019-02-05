package online.madeofmagicandwires.restaurant;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

    public static final int ORDER_ITEM_KEY = 15;
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


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_detail, container, false);
        bindData(v, mItem);

        return v;
    }



    public void bindData(@NonNull View v, RestaurantMenuItem item) {
        TextView description = v.findViewById(R.id.detailText);
        NumberPicker orderNo = v.findViewById(R.id.orderNumber);
        Button btn = v.findViewById(R.id.orderButton);
        TextView price = v.findViewById(R.id.orderPrice);
        if(item != null) {
            description.setText(item.getDesc());
            orderNo.setValue(1);
            price.setText(
                    String.format(
                        getString(R.string.item_price_format), // %s%.2f
                        getString(R.string.valuta), // e.g. "€"
                        (orderNo.getValue() * item.getPrice()) // total price of all items
                    )
            );

            // TODO: set btn onCLickListener

            v.setTag(ORDER_ITEM_KEY, item);
        } else  {
            description.setText(getString(R.string.detail_no_menu_item_error));
            orderNo.setValue(0);
            price.setText(
                    String.format(
                            getString(R.string.item_price_format), // %s%%.2f
                            getString(R.string.valuta), // e.g. "€"
                            0.0
                    )
            );
        }

    }

}
