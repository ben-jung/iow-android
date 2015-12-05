package kaist.cs492c_2015.washerbrowser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by ben on 2015. 12. 2..
 */
public class WasherAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    private ArrayList<Washer> mWasherList = new ArrayList<Washer>();
    SharedPreferences mPref;
    SharedPreferences.Editor editor;

    public WasherAdapter(Context context, String washerList) {
        this.context = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = mPref.edit();
        this.mInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        try {
            JSONArray jsonArray = new JSONArray(washerList);
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Washer washer = new Washer();
                washer.id = jsonObject.getInt("id");
                washer.floor = jsonObject.getInt("floor");
                washer.dorm = jsonObject.getString("dorm");
                washer.state = jsonObject.getString("state");
                washer.isClicked = mPref.getBoolean(String.valueOf(washer.id), false);
                this.mWasherList.add(washer);
            }
        } catch (JSONException e) {
            Log.getStackTraceString(e);
        }
    }

    @Override
    public int getCount() {
        return mWasherList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWasherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Context context = parent.getContext();

        if ( convertView == null ) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.washer_item, parent, false);

            viewHolder.washerState = (ImageView) convertView.findViewById(R.id.washerImageView);
            viewHolder.location = (TextView) convertView.findViewById(R.id.locationTextView);
            viewHolder.alarmButton = (ImageButton) convertView.findViewById(R.id.alarmImageButton);
            viewHolder.alarmButton.setClickable(true);
            setButton(viewHolder);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Washer w = mWasherList.get(position);
        if (w.state.equals("idle")) {
            viewHolder.washerState.setImageResource(R.drawable.washer_blue);
        } else {
            viewHolder.washerState.setImageResource(R.drawable.washer_red);
        }
        if (w.isClicked) {
            viewHolder.alarmButton.setBackgroundResource(R.drawable.alarm_on);
        } else {
            viewHolder.alarmButton.setBackgroundResource(R.drawable.alarm_off);
        }
        String leftRight = "Left";
        if (w.id % 2 == 0) {
            leftRight = "Right";
        }
        viewHolder.location.setText(w.dorm.toUpperCase() + " " + w.floor + "F " + leftRight);
        viewHolder.position = position;

        return convertView;
    }

    private void setButton (final ViewHolder viewHolder) {
        viewHolder.alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Washer washer = mWasherList.get(viewHolder.position);
                washer.isClicked = !washer.isClicked;
                editor.putBoolean(String.valueOf(washer.id), washer.isClicked);
                editor.commit();

                String state = "";
                if (washer.isClicked) {
                    state = "on";
                } else {
                    state = "off";
                }

                String url = String.format(context.getResources().getString(R.string.table_activity_tracking_url),
                        washer.id, state, RegistrationIntentService.TOKEN);

                SendTracking sendTracking = new SendTracking();
                sendTracking.execute(url);

                notifyDataSetChanged();
            }
        });
    }

    private class Washer
    {
        public int id;
        public int floor;
        public String dorm;
        public String state;
        public boolean isClicked;
    }

    private class ViewHolder
    {
        public ImageView washerState;
        public TextView location;
        public ImageButton alarmButton;
        public int position;
    }

    public class SendTracking extends AsyncTask<String, Void, String> {

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
        protected void onPostExecute(String result) { super.onPostExecute(result); }
    }
}
