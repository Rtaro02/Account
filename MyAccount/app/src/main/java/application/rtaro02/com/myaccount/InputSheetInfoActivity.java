package application.rtaro02.com.myaccount;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InputSheetInfoActivity extends Activity {

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
        Toast.makeText(this.getApplicationContext(), spreadsheetId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this.getApplicationContext(), sheetId.toString(), Toast.LENGTH_SHORT).show();

        findViewById(R.id.inputSheetInfoButton).setOnClickListener(new SetSheetInfoListener(this));
    }

    private class SetSheetInfoListener implements View.OnClickListener {
        InputSheetInfoActivity inputSheetInfoActivity;

        SetSheetInfoListener(InputSheetInfoActivity inputSheetInfoActivity){
            this.inputSheetInfoActivity = inputSheetInfoActivity;
        }

        @Override
        public void onClick(View v) {
            EditText spId = findViewById(R.id.spreadsheetId);
            String spreadSheetId = spId.getText().toString();
            EditText shId = findViewById(R.id.sheetId);
            String sheetIdStr = shId.getText().toString();
            Integer sheetId;
            if(spreadSheetId.isEmpty() || sheetIdStr.isEmpty()) {
                // When one side params is empty
                Toast.makeText(inputSheetInfoActivity.getApplicationContext(),
                        "Both parameters are required",
                        Toast.LENGTH_SHORT).show();
            } else {
                try {
                    sheetId = Integer.parseInt(sheetIdStr);
                    SharedPreferences data = getSharedPreferences("SpreadData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString("SpreadsheetId", spreadSheetId);
                    editor.putInt("SheetId", sheetId);
                    editor.apply();
                    Toast.makeText(inputSheetInfoActivity.getApplicationContext(),
                            "Spreadsheet params are saved!",
                            Toast.LENGTH_SHORT).show();
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
