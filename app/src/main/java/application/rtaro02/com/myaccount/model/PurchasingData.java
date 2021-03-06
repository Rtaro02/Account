package application.rtaro02.com.myaccount.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public String getTypeOfPayment() { return typeOfPayment; }
    public Integer getPrice() { return price; }
    public String getOverview() { return overview; }
    public void setTypeOfBuy(String typeOfBuy) { this.typeOfBuy = typeOfBuy; }
    public void setPrice(Integer price) { this.price = price; }
    public void setTypeOfPayment(String typeOfPayment) { this.typeOfPayment = typeOfPayment; }
    public void setOverview(String overview) { this.overview = overview; }

    public void setData(DefaultRequest defaultRequest) {
        this.overview = defaultRequest.getOverview();
        this.price = defaultRequest.getPrice();
        this.typeOfBuy = defaultRequest.getTypeOfBuy();
        this.typeOfPayment = defaultRequest.getTypeOfPayment();
    }
}
