package cl.sebastianmena.funnytrivia.views;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import cl.sebastianmena.funnytrivia.R;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements PhoneListener {

    TextView versionTv;
    PackageInfo packageInfo = null;
    public static final int READ_PHONE_STATE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        versionTv = (TextView) findViewById(R.id.versionTv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new PhoneValidation(this, this).check_api_version();

        new PhoneValidation(this, this).send_data_phone();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted do what you want from this point
                } else {
                    Toasty.error(this, "You must authorize the permissions to use the app.", Toast.LENGTH_SHORT, true).show();
                    finish();
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION);
    }

    @Override
    public void versionapp(String nameapp) {
        versionTv.setText("FunnyTrivia v" + nameapp + " developed by \nSebastián Mena P.");
    }
}
