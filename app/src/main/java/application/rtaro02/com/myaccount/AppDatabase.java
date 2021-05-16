package application.rtaro02.com.myaccount;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import application.rtaro02.com.myaccount.dao.PurchasingDataDao;
import application.rtaro02.com.myaccount.model.PurchasingData;

@Database(entities = {PurchasingData.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PurchasingDataDao getPurchasingDataDao();
}
