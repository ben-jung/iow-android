package kaist.cs492c_2015.washerbrowser;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        viewHolder.location.setText(w.dorm.toUpperCase() + " " + w.floor + "F " + w.id);
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
                if (washer.isClicked) {
                    // TODO: send gcm to get notification
                } else {
                    // TODO: send gcm to cancel
                }
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
}
