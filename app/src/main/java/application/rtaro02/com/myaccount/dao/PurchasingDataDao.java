package application.rtaro02.com.myaccount.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
