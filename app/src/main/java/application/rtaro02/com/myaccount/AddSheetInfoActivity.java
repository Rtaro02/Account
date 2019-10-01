package application.rtaro02.com.myaccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import application.rtaro02.com.myaccount.model.SheetInfo;

public class AddSheetInfoActivity extends Activity {

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sheet_info);

        SharedPreferences data = getSharedPreferences("SpreadData", Context.MODE_PRIVATE);
        String spreadsheetId = data.getString("SpreadsheetId", "");
        Integer sheetId = data.getInt("SheetId", -1);
        if(!notExistSpreadData(spreadsheetId, sheetId)) {
            setSheetInfo(spreadsheetId, sheetId);
            Intent intent = new Intent(this, SendSheetActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.inputSheetInfoButton).setOnClickListener(new SetSheetInfoListener(this));
    }

    private boolean notExistSpreadData(String spreadsheetId, Integer sheetId) {
        return spreadsheetId.equals("") && sheetId == -1;
    }

    private void setSheetInfo(String spreadsheetId, Integer sheetId) {
        SheetInfo sheetInfo = SheetInfo.getInstance();
        sheetInfo.setSpreadsheetId(spreadsheetId);
        sheetInfo.setSheetId(sheetId);
    }

    private class SetSheetInfoListener implements View.OnClickListener {
        AddSheetInfoActivity inputSheetInfoActivity;

        SetSheetInfoListener(AddSheetInfoActivity inputSheetInfoActivity){
            this.inputSheetInfoActivity = inputSheetInfoActivity;
        }

        @Override
        public void onClick(View v) {
            EditText spId = findViewById(R.id.spreadsheetId);
            String spreadsheetId = spId.getText().toString();
            EditText shId = findViewById(R.id.sheetId);
            String sheetIdStr = shId.getText().toString();
            Integer sheetId;
            if(spreadsheetId.isEmpty() || sheetIdStr.isEmpty()) {
                // When one side params is empty
                Toast.makeText(inputSheetInfoActivity.getApplicationContext(),
                        "Both parameters are required",
                        Toast.LENGTH_SHORT).show();
            } else {
                try {
                    sheetId = Integer.parseInt(sheetIdStr);
                    SharedPreferences data = getSharedPreferences("SpreadData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString("SpreadsheetId", spreadsheetId);
                    editor.putInt("SheetId", sheetId);
                    editor.apply();
                    Toast.makeText(inputSheetInfoActivity.getApplicationContext(),
                            "Spreadsheet params are saved!",
                            Toast.LENGTH_SHORT).show();
                    setSheetInfo(spreadsheetId, sheetId);
                    Intent intent = new Intent(inputSheetInfoActivity, SendSheetActivity.class);
                    startActivity(intent);
                } catch(NumberFormatException e) {
                    // Failed format sheetId.
                    Toast.makeText(inputSheetInfoActivity.getApplicationContext(),
                            "SheetId should be number.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
