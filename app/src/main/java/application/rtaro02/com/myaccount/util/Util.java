package application.rtaro02.com.myaccount.util;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import application.rtaro02.com.myaccount.R;

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

    public boolean getRefundFlag(Activity activity) {
        return ((CheckBox)activity.findViewById(R.id.refundCheckbox)).isChecked();
    }

    public boolean getPaymentFlag(Activity activity) {
        return ((CheckBox)activity.findViewById(R.id.payCheckbox)).isChecked();
    }

    public boolean payByPasmo(Activity activity) {
        return Util.getInstance().getSpinnerString(activity, R.id.typeOfPayment).equals(Constants.TRANSPORT_EMONEY);
    }
}
