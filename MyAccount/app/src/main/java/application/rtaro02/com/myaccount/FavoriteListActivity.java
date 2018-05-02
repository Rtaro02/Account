package application.rtaro02.com.myaccount;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.rtaro02.com.myaccount.model.PurchasingData;

public class FavoriteListActivity extends Activity {

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        final ArrayList<String> data = new ArrayList<>();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .allowMainThreadQueries()
                .build();

        List<PurchasingData> purchasingDataList = db.getPurchasingDataDao().getAll();
        for(PurchasingData x: purchasingDataList) {
            StringBuilder sb = new StringBuilder();
            sb.append(x.getUid())
                    .append(", ")
                    .append(x.getOverview())
                    .append(", ")
                    .append(x.getPrice())
                    .append(", ")
                    .append(x.getTypeOfBuy())
                    .append(", ")
                    .append(x.getTypeOfPayment());
            data.add(sb.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1, data);
        ListView list = findViewById(R.id.favorite_list);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new FavoriteLongClickListener(this));
    }

    private class FavoriteLongClickListener implements AdapterView.OnItemLongClickListener {
        FavoriteListActivity favoriteListActivity;

        FavoriteLongClickListener(FavoriteListActivity favoriteListActivity) {
            this.favoriteListActivity = favoriteListActivity;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            String str = ((TextView) view).getText().toString();
            Integer uid = Integer.parseInt(str.split(", ")[0]);
            //Toast.makeText(favoriteListActivity, uid, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(favoriteListActivity, SendSheetActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            return false;
        }
    }
}
