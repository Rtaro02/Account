package application.rtaro02.com.myaccount;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.room.Room;
import android.content.DialogInterface;
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

        ArrayList<String> data = makeListViewData();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1, data);
        ListView list = findViewById(R.id.favorite_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new FavoriteShortClickListener(this));
        list.setOnItemLongClickListener(new FavoriteLongClickListener(this, adapter));
    }

    private ArrayList<String> makeListViewData() {
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
        return data;
    }

    // textViewからUIDを抽出する
    private Integer getUid(View view) {
        String str = ((TextView) view).getText().toString();
        return Integer.parseInt(str.split(", ")[0]);
    }

    private class FavoriteShortClickListener implements AdapterView.OnItemClickListener {
        FavoriteListActivity favoriteListActivity;

        FavoriteShortClickListener(FavoriteListActivity favoriteListActivity) {
            this.favoriteListActivity = favoriteListActivity;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer uid = getUid(view);
            Intent intent = new Intent(favoriteListActivity, SendSheetActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
    }

    private class FavoriteLongClickListener implements AdapterView.OnItemLongClickListener {
        FavoriteListActivity favoriteListActivity;
        ArrayAdapter<String> adapter;

        FavoriteLongClickListener(FavoriteListActivity favoriteListActivity, ArrayAdapter<String> adapter) {
            this.favoriteListActivity = favoriteListActivity;
            this.adapter = adapter;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer uid = getUid(view);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(favoriteListActivity);
            alertDialogBuilder.setTitle("DELETE?");
            alertDialogBuilder.setMessage("Do you delete this item?");
            alertDialogBuilder.setPositiveButton("Yes", new RespondYesListener(uid, adapter));
            alertDialogBuilder.setNegativeButton("No",new RespondNoListener());
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            // trueを返すことで、shortClickListenerが動作しない
            return true;
        }
    }

    private class RespondYesListener implements DialogInterface.OnClickListener {
        private int uid;
        ArrayAdapter<String> adapter;

        public RespondYesListener(int uid, ArrayAdapter<String> adapter) {
            this.uid = uid;
            this.adapter = adapter;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .build();
            List<PurchasingData> purchasingDataList = db.getPurchasingDataDao().loadData(uid);
            PurchasingData purchasingData = purchasingDataList.get(0);
            db.getPurchasingDataDao().delete(purchasingData);
            adapter.clear();
            adapter.addAll(makeListViewData());
            adapter.notifyDataSetChanged();
        }
    }

    private class RespondNoListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Do nothing
        }
    }
}
