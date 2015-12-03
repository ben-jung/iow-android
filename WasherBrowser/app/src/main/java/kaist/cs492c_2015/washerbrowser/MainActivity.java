package kaist.cs492c_2015.washerbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private String macAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        macAddress = getMacId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (macAddress != null) {
                    Log.i("Address: ", macAddress);
                    Intent intent = new Intent(MainActivity.this, TableViewActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_activity_failed), Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private String getMacId() {

        String macAddress = null;

        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            macAddress = wifiInfo.getBSSID();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return macAddress;
    }
}
