package application.rtaro02.com.myaccount.model;

/**
 * Created by ryotaro on 2018/04/22.
 */

public class DefaultRequest {

    /**
     * Timestamp of registration time
     */
    private String timestamp;

    /**
     * The date of purchase
     */
    private String buyDate;

    /**
     * The type of purchase
     */
    private String typeOfBuy;

    /**
     * The type of payment
     */
    private String typeOfPayment;

    /**
     * Price
     */
    private Integer price;

    /**
     * Overview
     */
    private String overview;

    public String getTypeOfBuy() {
        return typeOfBuy;
    }

    public void setTypeOfBuy(String typeOfBuy) {
        this.typeOfBuy = typeOfBuy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
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
