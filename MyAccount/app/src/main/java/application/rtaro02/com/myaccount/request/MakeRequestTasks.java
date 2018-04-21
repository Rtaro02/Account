package application.rtaro02.com.myaccount.request;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ryotaro on 2018/04/21.
 */

public class MakeRequestTasks extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;

    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private ProgressDialog mProgress;

    public MakeRequestTasks(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Sheets API Android Quickstart")
                .build();
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
    protected List<String> doInBackground(Void... params) {
        try {
            putDataFromApi();
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @return List of names and majors
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException {
        String spreadsheetId = "1sXe8CICyRq3mP6SVelPfUefLTnl0hrSuHbjEAMb1Phw";
        String range = "hoge!a1:d1";
        List<String> results = new ArrayList<String>();
        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        for (List row : values) {
            results.add(
                    row.get(0) + ", " +
                            row.get(1) + ", " +
                            row.get(2) + ", " +
                            row.get(3)
            );
        }
        return results;
    }

    /**
     * データを書き込むメソッド
     * @throws IOException
     */
    private void putDataFromApi() throws IOException {
        String spreadsheetId = "1sXe8CICyRq3mP6SVelPfUefLTnl0hrSuHbjEAMb1Phw";
        String range = "hoge!a2:d2";
        ValueRange valueRange = new ValueRange();
        List row = new ArrayList<>();
        List col = Arrays.asList("This", "is", "test", "test");
        row.add(col);
        valueRange.setValues(row);
        valueRange.setRange(range);
        this.mService.spreadsheets().values()
                .update(spreadsheetId, range, valueRange)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    @Override
    protected void onPreExecute() {
        //mOutputText.setText("");
        //mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mProgress.hide();
        if (output == null || output.size() == 0) {
            mOutputText.setText("No results returned.");
        } else {
            output.add(0, "Data retrieved using the Google Sheets API:");
            //TextView textView = findViewById(R.id.output_text);
            //textView.setText(TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
        //mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                // show google dialog
                //showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                //startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(),MainActivity.REQUEST_AUTHORIZATION);
            } else {
                mOutputText.setText("The following error occurred:\n" + mLastError.getMessage());
            }
        } else {
            mOutputText.setText("Request cancelled.");
        }
    }
}
