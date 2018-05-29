package application.rtaro02.com.myaccount.model;

public class SheetInfo {

    private SheetInfo() {}
    private String spreadsheetId;
    private Integer sheetId;
    private static SheetInfo singleton = new SheetInfo();

    public static SheetInfo getInstance() {
        return singleton;
    }

    public Integer getSheetId() {
        return sheetId;
    }

    public void setSheetId(Integer sheetId) {
        this.sheetId = sheetId;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }
}
