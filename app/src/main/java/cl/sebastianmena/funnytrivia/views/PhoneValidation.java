package cl.sebastianmena.funnytrivia.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Sebastián Mena on 30-04-2018.
 */

public class PhoneValidation {

    Context context;
    PhoneListener listener;
    PackageInfo packageInfo = null;

    public PhoneValidation(Context context, PhoneListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void check_api_version() {
        //CHECK IF VERSION >= API 23 M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            listener.requestPermissions();
        }
    }


    public void send_data_phone() {
        //CHECK PHONE DATA
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String nameapp = packageInfo.versionName;
            listener.versionapp(nameapp);

        } catch (PackageManager.NameNotFoundException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        //SEND DATA TO FABRIC
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        String datacel = Build.MODEL + "+" + String.valueOf(Build.VERSION.SDK_INT) + "+" + imei;
        String date_start = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

        Answers.getInstance().logCustom(new CustomEvent("START")
                .putCustomAttribute("DATE-INFO", date_start+datacel)
        );
    }
}
