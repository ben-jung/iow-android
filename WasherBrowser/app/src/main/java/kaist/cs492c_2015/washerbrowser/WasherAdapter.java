package kaist.cs492c_2015.washerbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ben on 2015. 12. 2..
 */
public class WasherAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    private ArrayList<String> mWasherList;

    public WasherAdapter(Context context, String washerList) {
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        try {
            JSONObject jsonObject = new JSONObject(washerList);
        } catch (JSONException e) {
            Log.getStackTraceString(e);
        }
        //this.mWasherList = washerList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Context context = parent.getContext();

        return null;
    }

    public class ViewHolder
    {
        public TextView text;
        public Button button;
        public ProgressBar bar;
        public int position;
        public String url;
    }
}
