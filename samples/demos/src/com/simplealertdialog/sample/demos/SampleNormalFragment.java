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

package com.simplealertdialog.sample.demos;

import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogFragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SampleNormalFragment extends Fragment
        implements SimpleAlertDialog.OnClickListener,
        SimpleAlertDialog.SingleChoiceArrayItemProvider,
        SimpleAlertDialog.ListProvider,
        SimpleAlertDialog.ViewProvider {

    // XXX Warning: Don't use codes which the parent activity uses.
    // If you do so, both the activity's and fragment's handler will be
    // executed.
    private static final int REQUEST_CODE_BUTTONS = -1;
    private static final int REQUEST_CODE_SINGLE_CHOICE_LIST = -2;
    private static final int REQUEST_CODE_ADAPTER = -3;
    private static final int REQUEST_CODE_VIEW = -4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_support, container, false);

        view.findViewById(R.id.btn_frag_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .setTargetFragment(SampleNormalFragment.this)
                        .create().show(getActivity().getFragmentManager(), "dialog");
            }
        });

        view.findViewById(R.id.btn_frag_message_title).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new SimpleAlertDialogFragment.Builder()
                                .setTitle("Hello world!")
                                .setMessage("Hello world!")
                                .setPositiveButton(android.R.string.ok)
                                .setTargetFragment(SampleNormalFragment.this)
                                .create().show(getActivity().getFragmentManager(), "dialog");
                    }
                });

        view.findViewById(R.id.btn_frag_buttons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Hello world!")
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .setNegativeButton(android.R.string.cancel)
                        .setRequestCode(REQUEST_CODE_BUTTONS)
                        .setTargetFragment(SampleNormalFragment.this)
                        .create().show(getActivity().getFragmentManager(), "dialog");
            }
        });

        view.findViewById(R.id.btn_frag_single_choice_list).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new SimpleAlertDialogFragment.Builder()
                                .setTitle("Choose one")
                                .setSingleChoiceCheckedItem(0)
                                .setRequestCode(REQUEST_CODE_SINGLE_CHOICE_LIST)
                                .setTargetFragment(SampleNormalFragment.this)
                                .create().show(getActivity().getFragmentManager(), "dialog");
                    }
                });

        view.findViewById(R.id.btn_frag_adapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Choose your favorite")
                        .setUseAdapter(true)
                        .setRequestCode(REQUEST_CODE_ADAPTER)
                        .setTargetFragment(SampleNormalFragment.this)
                        .create().show(getActivity().getFragmentManager(), "dialog");
            }
        });

        view.findViewById(R.id.btn_frag_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Enter something")
                        .setUseView(true)
                        .setRequestCode(REQUEST_CODE_VIEW)
                        .setTargetFragment(SampleNormalFragment.this)
                        .create().show(getActivity().getFragmentManager(), "dialog");
            }
        });

        return view;
    }

    @Override
    public void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, int requestCode,
            View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(getActivity(), "Fragment: OK button clicked", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_VIEW) {
            String text = ((EditText) view.findViewById(R.id.text)).getText().toString();
            Toast.makeText(getActivity(), "Fragment: You typed: " + text, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, int requestCode,
            View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(getActivity(), "Fragment: Cancel button clicked", Toast.LENGTH_SHORT)
                    .show();
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
            Toast.makeText(
                    getActivity(),
                    "Fragment: " + getResources().getTextArray(R.array.single_choice)[position]
                            + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ListAdapter onCreateList(SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_ADAPTER) {
            return new SweetsAdapter(getActivity(), Sweets.SWEETS_LIST);
        }
        return null;
    }

    @Override
    public void onListItemClick(SimpleAlertDialog dialog, int requestCode, int position) {
        if (requestCode == REQUEST_CODE_ADAPTER) {
            Toast.makeText(getActivity(),
                    "Fragment: " + Sweets.SWEETS_LIST.get(position).name + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_VIEW) {
            final View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.view_editor, null);
            ((EditText) view.findViewById(R.id.text)).setText("Sample");
            return view;
        }
        return null;
    }

}
