package application.rtaro02.com.myaccount.model;

/**
 * Created by ryotaro on 2018/04/22.
 */

public class DefaultRequest {

    /**
     * 購買情報を登録した時間
     */
    private String timestamp;

    /**
     * 購入日
     */
    private String buyDate;

    /**
     * 収支か支出かの分類
     */
    private String incomeOrPayment;

    /**
     * 収支分類
     */
    private String typeOfBuy;

    /**
     * 支払い分類
     */
    private String typeOfPayment;

    /**
     * Suica支払いフラグ
     */
    private boolean suicaPayFlg;

    /**
     * 支払い金額
     */
    private int price;

    /**
     * 概要
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

    public String getIncomeOrPayment() {
        return incomeOrPayment;
    }

    public void setIncomeOrPayment(String incomeOrPayment) {
        this.incomeOrPayment = incomeOrPayment;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public boolean isSuicaPayFlg() {
        return suicaPayFlg;
    }

    public void setSuicaPayFlg(boolean suicaPayFlg) {
        this.suicaPayFlg = suicaPayFlg;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
