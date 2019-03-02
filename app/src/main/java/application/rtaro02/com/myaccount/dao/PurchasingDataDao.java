package application.rtaro02.com.myaccount.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import application.rtaro02.com.myaccount.model.PurchasingData;

@Dao
public interface PurchasingDataDao {
    @Query("SELECT * FROM purchasingdata")
    List<PurchasingData> getAll();

    @Query("SELECT * FROM purchasingdata WHERE uid = :uid LIMIT 1")
    List<PurchasingData> loadData(int uid);

    @Insert
    void insertAll(PurchasingData... users);

    @Delete
    void delete(PurchasingData user);
}
