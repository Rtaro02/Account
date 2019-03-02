package application.rtaro02.com.myaccount.util;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

public class Util {

    private static Util util = new Util();

    private Util() {}

    public static Util getInstance() {
        return util;
    }

    public boolean isAllParamSet(String... targets) {
        for(String target: targets) {
            if(target.isEmpty()) return false;
        }
        return true;
    }

    public String getEditTextString(Activity activity, int id) {
        return ((EditText)activity.findViewById(id)).getText().toString();
    }

    public String getSpinnerString(Activity activity, int id) {
        return ((Spinner)activity.findViewById(id)).getSelectedItem().toString();
    }
}
