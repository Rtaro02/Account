package application.rtaro02.com.myaccount.adaptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBAdaptor {

    private Context context;
    final private String LIST_SIZE = "listSize";
    final private String FAV_TYPE = "favorite_list"

    public DBAdaptor(Context context) {
        this.context = context;
    }

    public List<Set<String>> referFavoriteData() {
        SharedPreferences data = context.getSharedPreferences("favorite_list", Context.MODE_PRIVATE);
        Integer listSize = data.getInt(LIST_SIZE, 0);
        List<Set<String>> list = new ArrayList<>();
        for(Integer i = 0; i < listSize; i++) {
            Set<String> set = data.getStringSet(i.toString(), null);
            list.add(set);
        }
        return list;
    }

    public List<Set<String>> updateFavoritData() {
        SharedPreferences data = context.getSharedPreferences("favorite_list", Context.MODE_PRIVATE);
        Integer listSize = data.getInt(LIST_SIZE, 0);
        List<Set<String>> list = new ArrayList<>();
        for(Integer i = 0; i < listSize; i++) {
            Set<String> set = data.getStringSet(i.toString(), null);
            list.add(set);
        }
        return list;
    }
}
