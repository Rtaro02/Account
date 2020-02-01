package application.rtaro02.com.myaccount.listener;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import application.rtaro02.com.myaccount.SendSheetActivity;

public class DatePickerDialogListener implements View.OnClickListener {

    SendSheetActivity sendSheetActivity;

    public DatePickerDialogListener(SendSheetActivity sendSheetActivity){
        this.sendSheetActivity = sendSheetActivity;
    }

    @Override
    public void onClick(View view) {
        final Calendar calendar = Calendar.getInstance();

        TextView textView = (TextView)view;
        final EditText editText = (EditText)textView;
        DatePickerDialog calenderPickerDialog = new DatePickerDialog(
                sendSheetActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //setした日付を取得して表示
                        editText.setText(String.format("%d/%02d/%02d", year, month+1, dayOfMonth));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );
        calenderPickerDialog.show();
    }
}

