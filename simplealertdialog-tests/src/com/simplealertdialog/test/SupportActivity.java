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

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogSupportFragment;

public class SupportActivity extends FragmentActivity
        implements SimpleAlertDialog.OnClickListener,
        SimpleAlertDialog.OnItemClickListener,
        SimpleAlertDialog.SingleChoiceArrayItemProvider,
        SimpleAlertDialog.ListProvider,
        SimpleAlertDialog.ViewProvider,
        SimpleAlertDialog.OnCancelListener {

    private static final int REQUEST_CODE_BUTTONS = 1;
    private static final int REQUEST_CODE_ITEMS = 2;
    private static final int REQUEST_CODE_SINGLE_CHOICE_LIST = 3;
    private static final int REQUEST_CODE_ADAPTER = 4;
    private static final int REQUEST_CODE_VIEW = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
    }

    protected void initLayout() {
        setContentView(R.layout.activity_support);
        initButtons();
    }

    protected void initButtons() {
        findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_message_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TypedArray a = getTheme().obtainStyledAttributes(new int[]{R.attr.icon});
                int iconResId = a.getResourceId(0, 0);
                a.recycle();
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Hello world!")
                        .setIcon(iconResId)
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_buttons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Hello world!")
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .setNegativeButton(android.R.string.cancel)
                        .setRequestCode(REQUEST_CODE_BUTTONS)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Choose one")
                        .setItems(R.array.single_choice)
                        .setRequestCode(REQUEST_CODE_ITEMS)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_single_choice_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Choose one")
                        .setSingleChoiceCheckedItem(0)
                        .setRequestCode(REQUEST_CODE_SINGLE_CHOICE_LIST)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_adapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Choose your favorite")
                        .setUseAdapter(true)
                        .setRequestCode(REQUEST_CODE_ADAPTER)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTitle("Enter something")
                        .setUseView(true)
                        .setPositiveButton(android.R.string.ok)
                        .setRequestCode(REQUEST_CODE_VIEW)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_themed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setTheme(R.style.SimpleAlertDialogCustomTheme)
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, int requestCode,
                                              View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(this, "OK button clicked", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_VIEW) {
            String text = ((EditText) view.findViewById(R.id.text)).getText().toString();
            Toast.makeText(this, "You typed: " + text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, int requestCode,
                                              View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(SimpleAlertDialog dialog, int requestCode, int which) {
        if (requestCode == REQUEST_CODE_ITEMS) {
            Toast.makeText(this,
                    getResources().getTextArray(R.array.single_choice)[which] + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public CharSequence[] onCreateSingleChoiceArray(final SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_SINGLE_CHOICE_LIST) {
            return getResources().getTextArray(R.array.single_choice);
        }
        return null;
    }

    @Override
    public void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, int requestCode,
                                             int position) {
        if (requestCode == REQUEST_CODE_SINGLE_CHOICE_LIST) {
            Toast.makeText(this,
                    getResources().getTextArray(R.array.single_choice)[position] + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ListAdapter onCreateList(SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_ADAPTER) {
            return new SweetsAdapter(this, Sweets.SWEETS_LIST);
        }
        return null;
    }

    @Override
    public void onListItemClick(SimpleAlertDialog dialog, int requestCode, int position) {
        if (requestCode == REQUEST_CODE_ADAPTER) {
            Toast.makeText(this,
                    Sweets.SWEETS_LIST.get(position).name + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_VIEW) {
            final View view = LayoutInflater.from(this).inflate(R.layout.view_editor, null);
            ((EditText) view.findViewById(R.id.text)).setText("Sample");
            return view;
        }
        return null;
    }

    @Override
    public void onDialogCancel(SimpleAlertDialog dialog, int requestCode, View view) {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
    }
}
