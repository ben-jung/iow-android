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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private final int MIN_SPLASH_DISPLAY_LENGTH = 2000;

    private String macAddress = null;

    private final GetStringFromUrl getStringFromUrl = (GetStringFromUrl) new GetStringFromUrl();
    private String washerList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        macAddress = getMacId();

        getStringFromUrl.execute(getResources().getString(R.string.main_activity_input_url));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (macAddress != null || !washerList.equals("")) {
                    Log.i("Address: ", macAddress);
                    Log.i("List: ", washerList);
                    Intent intent = new Intent(MainActivity.this, TableViewActivity.class);
                    intent.putExtra("washerList", washerList);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_activity_failed), Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
            }
        }, MIN_SPLASH_DISPLAY_LENGTH);
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

    public class GetStringFromUrl extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sURL) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(sURL[0]);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity buf = new BufferedHttpEntity(entity);

                InputStream is = buf.getContent();

                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line + "\n");
                }
                String fileList = total.toString();
                Log.i("Get URL", "Downloaded string: " + fileList);
                return fileList;
            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else {
                washerList = result;
            }
        }
    }
}
