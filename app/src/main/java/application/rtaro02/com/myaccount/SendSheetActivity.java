package application.rtaro02.com.myaccount;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import application.rtaro02.com.myaccount.exception.NoInputException;
import application.rtaro02.com.myaccount.listener.AddFavoriteClickListener;
import application.rtaro02.com.myaccount.listener.DatePickerDialogListener;
import application.rtaro02.com.myaccount.listener.Move2FavoriteListener;
import application.rtaro02.com.myaccount.model.DefaultRequest;
import application.rtaro02.com.myaccount.model.PurchasingData;
import application.rtaro02.com.myaccount.request.MakeRequestTasks;
import application.rtaro02.com.myaccount.util.Util;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * The objective of this class is sending purchase information to google spreadshjeet.
 */
public class SendSheetActivity extends GoogleAPIActivity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static public final int REQUEST_AUTHORIZATION = 1001;
    static public final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    final String DATE_FORMAT = "yyyy/MM/dd";

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Sheets API ...");
        setContentView(R.layout.activity_send_sheet);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Integer uid = bundle.getInt("uid");
            final ArrayList<String> data = new ArrayList<>();

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .build();
            List<PurchasingData> purchasingDataList = db.getPurchasingDataDao().loadData(uid);
            PurchasingData purchasingData = purchasingDataList.get(0);
            int buyPosition = getArrayPosition(purchasingData.getTypeOfBuy(), R.array.buy);
            int paymentPosition = getArrayPosition(purchasingData.getTypeOfPayment(), R.array.payment);

            // 収支分類の設定
            ((Spinner)findViewById(R.id.typeOfBuy)).setSelection(buyPosition);
            // 支払い分類の設定
            ((Spinner)findViewById(R.id.typeOfPayment)).setSelection(paymentPosition);
            // 概要の設定
            ((EditText)findViewById(R.id.overview)).setText(purchasingData.getOverview());
            // 金額の設定
            ((EditText)findViewById(R.id.price)).setText(purchasingData.getPrice().toString());
        }

        // デフォルトの購買日を設定する
        setDefaultBuyDate();

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        // Listenerを設定する
        findViewById(R.id.sendSheetButton).setOnClickListener(new SendClickListener());
        findViewById(R.id.add2favorite).setOnClickListener(new AddFavoriteClickListener(this));
        findViewById(R.id.move2favorite).setOnClickListener(new Move2FavoriteListener(this));
    }

    private int getArrayPosition(String target, int id) {
        String[] array = getResources().getStringArray(id);
        int i = 0;
        for(String x: array) {
            if(x.equals(target)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    // デフォルトの購買日を設定する
    private void setDefaultBuyDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        EditText editText = findViewById(R.id.buyDate);
        editText.setText(String.format("%d/%02d/%02d", year, month + 1, dayofmonth));
        editText.setOnClickListener(new DatePickerDialogListener(this));
    }

    private class SendClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getResultsFromApi();
        }
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            System.out.println("いけるで！Googleサービス");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            System.out.println("アカウントやで、、、");
            chooseAccount();
        } else if (!isDeviceOnline()) {
            System.out.println("オフラインやで、、、");
            mOutputText.setText("No network connection available.");
        } else {
            System.out.println("ぜんぶおｋ");
            DefaultRequest dr = new DefaultRequest();
            try {
                dr.setRequestData(this);
                if(Util.getInstance().getRefundFlag(this)) {
                    // Refund
                    sendRequest(mCredential, mOutputText, mProgress, dr);
                    sendRequest(mCredential, mOutputText, mProgress, dr.getRefundParameter());
                } else if (Util.getInstance().getPaymentFlag(this)) {
                    // Pay
                    sendRequest(mCredential, mOutputText, mProgress, dr.getPaymentParameter());
                } else {
                    // Default
                    sendRequest(mCredential, mOutputText, mProgress, dr);
                }
                if(Util.getInstance().payByPasmo(this)) {
                    sendRequest(mCredential, mOutputText, mProgress, dr.balancePasmo());
                }
            } catch(NumberFormatException e) {
                Toast.makeText(this, "Price should be number", Toast.LENGTH_SHORT).show();
            } catch(NoInputException e) {
                Toast.makeText(this, "All Params sholud be set.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendRequest(GoogleAccountCredential mCredential, TextView mOutputText, ProgressDialog mProgress, DefaultRequest dr) {
        MakeRequestTasks makeRequestTasks = new MakeRequestTasks(mCredential, dr);
        makeRequestTasks.setMOutputText(mOutputText);
        makeRequestTasks.setMProgress(mProgress);
        makeRequestTasks.setMainActivity(this);
        makeRequestTasks.execute();
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automaticaly whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }
}