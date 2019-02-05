package online.madeofmagicandwires.restaurant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_ITEM_BUNDLE_KEY = "detail_item";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        Log.d("Found fragment", Boolean.toString(fragment instanceof DetailFragment));


    }

    public void updateState() {

    }

}
