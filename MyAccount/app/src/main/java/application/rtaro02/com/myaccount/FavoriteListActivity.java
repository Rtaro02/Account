package application.rtaro02.com.myaccount;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FavoriteListActivity extends Activity {

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        SharedPreferences data = getSharedPreferences("SpreadData", Context.MODE_PRIVATE);
    }

}
