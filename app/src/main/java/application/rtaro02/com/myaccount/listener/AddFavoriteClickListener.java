package application.rtaro02.com.myaccount.listener;

import androidx.room.Room;
import android.view.View;
import android.widget.Toast;

import application.rtaro02.com.myaccount.AppDatabase;
import application.rtaro02.com.myaccount.SendSheetActivity;
import application.rtaro02.com.myaccount.exception.NoInputException;
import application.rtaro02.com.myaccount.model.DefaultRequest;
import application.rtaro02.com.myaccount.model.PurchasingData;

public class AddFavoriteClickListener implements View.OnClickListener{
    SendSheetActivity sendSheetActivity;

    public AddFavoriteClickListener(SendSheetActivity sendSheetActivity){
        this.sendSheetActivity = sendSheetActivity;
    }

    public void onClick(View view) {
        AppDatabase db = Room.databaseBuilder(sendSheetActivity.getApplicationContext(),
                AppDatabase.class, "database-name")
                .allowMainThreadQueries()
                .build();
        PurchasingData purchasingData= new PurchasingData();
        DefaultRequest defaultRequest = new DefaultRequest();
        try {
            defaultRequest.setRequestData(sendSheetActivity);
            purchasingData.setData(defaultRequest);
            db.getPurchasingDataDao().insertAll(purchasingData);
            Toast.makeText(sendSheetActivity.getApplicationContext(), "Add to favorite list!!", Toast.LENGTH_SHORT).show();
        } catch(NumberFormatException e) {
            Toast.makeText(sendSheetActivity.getApplicationContext(), "Price should be number", Toast.LENGTH_SHORT).show();
        } catch(NoInputException e) {
            Toast.makeText(sendSheetActivity.getApplicationContext(), "All Params should be set.", Toast.LENGTH_SHORT).show();
        }
    }
}
