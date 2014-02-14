
package com.simplealertdialog.sample.demos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SweetsAdapter extends ArrayAdapter<Sweets> {

    public SweetsAdapter(Context context, List<Sweets> objects) {
        super(context, R.layout.list_item_support_sweets, R.id.id, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Sweets s = getItem(position);
        ((TextView) view.findViewById(R.id.id)).setText(s.id);
        ((TextView) view.findViewById(R.id.name)).setText(s.name);
        return view;
    }

}
