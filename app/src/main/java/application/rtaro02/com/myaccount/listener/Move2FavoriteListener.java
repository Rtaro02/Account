package application.rtaro02.com.myaccount.listener;

import android.content.Intent;
import android.view.View;

import application.rtaro02.com.myaccount.FavoriteListActivity;
import application.rtaro02.com.myaccount.SendSheetActivity;

public class Move2FavoriteListener implements View.OnClickListener {
    SendSheetActivity sendSheetActivity;

    public Move2FavoriteListener(SendSheetActivity sendSheetActivity){
        this.sendSheetActivity = sendSheetActivity;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(sendSheetActivity, FavoriteListActivity.class);
        sendSheetActivity.startActivity(intent);
    }
}
