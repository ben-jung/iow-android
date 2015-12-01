package kaist.cs492c_2015.washerbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by ben on 2015. 12. 2..
 */
public class WasherAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    private ArrayList<String> mWasherList;

    public WasherAdapter(Context context, String washerList) {
        //this.mWasherList = washerList;
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
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
        return null;
    }
}
