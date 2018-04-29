package application.rtaro02.com.myaccount.request;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import application.rtaro02.com.myaccount.SendSheetActivity;
import application.rtaro02.com.myaccount.model.DefaultRequest;
import application.rtaro02.com.myaccount.model.SheetInfo;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by ryotaro on 2018/04/21.
 */

public class MakeRequestTasks extends AsyncTask<Void, Void, Void> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;

    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private ProgressDialog mProgress;
    private DefaultRequest dr;
    private String spreadsheetId;
    private int sheetId;
    private Activity activity;

    private MakeRequestTasks(){}

    public MakeRequestTasks(GoogleAccountCredential credential, DefaultRequest dr) {
        this.dr = dr;
        this.mCredential = credential;
        spreadsheetId = SheetInfo.getInstance().getSpreadsheetId();
        sheetId = SheetInfo.getInstance().getSheetId();

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Sheets API Android Quickstart")
                .build();
    }

    public void setMainActivity(Activity activity) {
        this.activity = activity;
    }

    public void setMOutputText(TextView mOutputText) {
        this.mOutputText = mOutputText;
    }

    public void setMProgress(ProgressDialog mProgress) {
        this.mProgress = mProgress;
    }

    /**
     * Background task to call Google Sheets API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            getRangeFromApi();
            putDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }

    /**
     * データを書き込むメソッド
     * @throws IOException
     */
    private void putDataFromApi() throws IOException {
        String range = "List!a2:h2";
        ValueRange valueRange = new ValueRange();
        List row = new ArrayList<>();
        List col = new ArrayList<>();
        col.add(dr.getTimestamp());
        col.add(dr.getBuyDate());
        col.add(dr.getIncomeOrPayment());
        col.add(dr.getTypeOfBuy());
        col.add(dr.getTypeOfPayment());
        col.add(dr.isSuicaPayFlg());
        col.add(dr.getPrice());
        col.add(dr.getOverview());
        row.add(col);
        valueRange.setValues(row);
        valueRange.setRange(range);
        this.mService.spreadsheets().values()
                .update(spreadsheetId, range, valueRange)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    /**
     * 列を挿入するメソッド
     * @throws IOException
     */
    private void getRangeFromApi() throws IOException, GeneralSecurityException {
        List<Request> requests = new ArrayList<>();
        DimensionRange dimensionRange = new DimensionRange();
        dimensionRange.setSheetId(sheetId);
        dimensionRange.setDimension("ROWS");
        dimensionRange.setStartIndex(1);
        dimensionRange.setEndIndex(2);
        InsertDimensionRequest insertDimension = new InsertDimensionRequest();
        insertDimension.setRange(dimensionRange);
        Request r = new Request();
        r.setInsertDimension(insertDimension);
        requests.add(r);
        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
        requestBody.setRequests(requests);

        Sheets.Spreadsheets.BatchUpdate request =
                this.mService.spreadsheets().batchUpdate(spreadsheetId, requestBody);

        BatchUpdateSpreadsheetResponse response = request.execute();

        // TODO: Change code below to process the `response` object:
        System.out.println(response);

        System.out.println(this.mService.spreadsheets().values().batchGet(spreadsheetId).execute());
    }

//    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//        // TODO: Change placeholder below to generate authentication credentials. See
//        // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
//        //
//        // Authorize using one of the following scopes:
//        //   "https://www.googleapis.com/auth/drive"
//        //   "https://www.googleapis.com/auth/drive.file"
//        //   "https://www.googleapis.com/auth/spreadsheets"
//        GoogleCredential credential = null;
//
//        return new Sheets.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName("Google-SheetsSample/0.1")
//                .build();
//    }

    @Override
    protected void onPreExecute() {
        //mOutputText.setText("");
        mProgress.show();
    }

    @Override
    protected void onPostExecute(Void tmp) {
        mProgress.hide();
    }

    @Override
    protected void onCancelled() {
        //mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                // show google dialog
                showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                startActivityForResult(activity, ((UserRecoverableAuthIOException) mLastError).getIntent(), SendSheetActivity.REQUEST_AUTHORIZATION, null);
            } else {
                mOutputText.setText("The following error occurred:\n" + mLastError.getMessage());
            }
        } else {
            mOutputText.setText("Request cancelled.");
        }
    }
    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                SendSheetActivity.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}
