package online.madeofmagicandwires.restaurant;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("WeakerAccess,unused")
class CategoryAdapter extends ArrayAdapter<String> {

    private final List<String> data;
    private final LayoutInflater inflater;
    private @LayoutRes
    final int layout;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views. Requires a textview with the id android.R.id.text1
     * @param objects  The objects to represent in the ListView.
     */
    public CategoryAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.data = objects;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
    }

    /**

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param objects  The objects to represent in the ListView.
     */

    public CategoryAdapter(@NonNull Context context,
                           @NonNull List<String> objects) {
        this(context, android.R.layout.simple_list_item_1, objects);
    }

    /**
     * Inflate a view if necessary and bind necessary data to it
     *
     * @param position the position of the view/data within the adapter
     * @param convertView an old view to be reused if available
     * @param parent the parent listview
     * @return the view with its data bound
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        // bind data
        if(position < getCount()) {
            TextView text = convertView.findViewById(android.R.id.text1);
            text.setText(capitaliseItem(position));
        }
        return convertView;
    }

    private String capitaliseItem(int position) {
        if(getItem(position) instanceof String) {
            StringBuilder item = new StringBuilder(data.get(position));
            if(item.length() != 0) {
                item.setCharAt(0, Character.toUpperCase(item.charAt(0)));
            }
            return item.toString();
        }
        return null;
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     */
    @Override
    public void addAll(@NonNull Collection<? extends String> collection) {
        data.addAll(collection);
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    public void insert(@Nullable String object, int index) {
        data.add(index, object);
    }
}
