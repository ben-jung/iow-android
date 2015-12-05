package kaist.cs492c_2015.washerbrowser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TableViewActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView washerListView;
    private WasherAdapter washerAdapter;
    private String macAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableview);
        Intent intent = getIntent();
        macAddress = intent.getStringExtra("macAddress");
        washerListView = (ListView) findViewById(R.id.washerListView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWasherList();
            }
        });
        getWasherList();
    }

    protected void getWasherList() {
        fab.setClickable(false);
        GetStringFromUrl getStringFromUrl = new GetStringFromUrl();
        getStringFromUrl.execute(getResources().getString(R.string.main_activity_input_url)+macAddress);
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
                Toast.makeText(TableViewActivity.this, getResources().getString(R.string.table_activity_failed), Toast.LENGTH_SHORT).show();
            } else {
                washerAdapter = new WasherAdapter(getApplicationContext(), result);
                washerListView.setAdapter(washerAdapter);
            }
            fab.setClickable(true);
        }
    }

}
