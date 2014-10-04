/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplealertdialog.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SweetsAdapter extends ArrayAdapter<Sweets> {

    public SweetsAdapter(Context context, List<Sweets> objects) {
        super(context, com.simplealertdialog.test.R.layout.list_item_support_sweets, com.simplealertdialog.test.R.id.id, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Sweets s = getItem(position);
        ((TextView) view.findViewById(com.simplealertdialog.test.R.id.id)).setText(s.id);
        ((TextView) view.findViewById(com.simplealertdialog.test.R.id.name)).setText(s.name);
        return view;
    }

}
