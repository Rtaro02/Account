package application.rtaro02.com.myaccount.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class PurchasingData {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    /**
     * 収支分類
     */
    @ColumnInfo(name = "type_pf_buy")
    private String typeOfBuy;

    /**
     * 支払い分類
     */
    @ColumnInfo(name = "type_of_payment")
    private String typeOfPayment;

    /**
     * 支払い金額
     */
    @ColumnInfo(name = "price")
    private Integer price;

    /**
     * 概要
     */
    @ColumnInfo(name = "overview")
    private String overview;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTypeOfBuy() {
        return typeOfBuy;
    }

    public void setTypeOfBuy(String typeOfBuy) {
        this.typeOfBuy = typeOfBuy;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
