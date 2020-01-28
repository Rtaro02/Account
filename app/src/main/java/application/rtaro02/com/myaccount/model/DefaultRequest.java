package application.rtaro02.com.myaccount.model;

import android.app.Activity;

import java.sql.Timestamp;

import application.rtaro02.com.myaccount.R;
import application.rtaro02.com.myaccount.exception.NoInputException;
import application.rtaro02.com.myaccount.util.Util;

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

    public String getTypeOfBuy() { return typeOfBuy; }
    public String getTimestamp() { return timestamp; }
    public String getBuyDate() { return buyDate; }
    public String getTypeOfPayment() { return typeOfPayment; }
    public Integer getPrice() { return price; }
    public String getOverview() { return overview; }
    public void setTypeOfBuy(String typeOfBuy) { this.typeOfBuy = typeOfBuy; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setTypeOfPayment(String typeOfPayment) { this.typeOfPayment = typeOfPayment; }
    public void setBuyDate(String buyDate) { this.buyDate = buyDate; }
    public void setPrice(Integer price) { this.price = price; }
    public void setOverview(String overview) { this.overview = overview; }


    public void setRequestData(Activity activity) throws NumberFormatException, NoInputException {
        // 購買日の取得
        String buyDate = Util.getInstance().getEditTextString(activity, R.id.buyDate);
        // 収支分類の取得
        String typeOfBuyStr = Util.getInstance().getSpinnerString(activity, R.id.typeOfBuy);
        // 支払い分類の取得
        String typeOfPaymentStr = Util.getInstance().getSpinnerString(activity, R.id.typeOfPayment);
        // 概要の取得
        String overviewStr = Util.getInstance().getEditTextString(activity, R.id.overview);
        // 金額の取得
        String priceStr = Util.getInstance().getEditTextString(activity, R.id.price);
        if(Util.getInstance().isAllParamSet(buyDate, typeOfBuyStr, typeOfPaymentStr, overviewStr, priceStr)) {
            this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
            this.buyDate = buyDate;
            this.typeOfBuy = typeOfBuyStr;
            this.typeOfPayment = typeOfPaymentStr;
            this.overview = overviewStr;
            this.price = Integer.parseInt(priceStr);
        } else {
            throw new NoInputException();
        }
    }

    public DefaultRequest getRefundParameter() {
        DefaultRequest dr = new DefaultRequest();
        StringBuilder sb = new StringBuilder();
        dr.setOverview(sb.append(overview).append(" (返金)").toString());
        dr.setBuyDate(this.buyDate);
        dr.setPrice(price * -1/3);
        dr.setTimestamp(this.timestamp);
        dr.setTypeOfPayment("キャッシュ");
        dr.setTypeOfBuy(this.typeOfBuy);
        return dr;
    }
}