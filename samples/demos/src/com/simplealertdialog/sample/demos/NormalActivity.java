
package com.simplealertdialog.sample.demos;

import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NormalActivity extends Activity
        implements SimpleAlertDialog.OnClickListener,
        SimpleAlertDialog.SingleChoiceArrayItemProvider {

    private static final int REQUEST_CODE_BUTTONS = 1;
    private static final int REQUEST_CODE_SINGLE_CHOICE_LIST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .create().show(getFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_message_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Hello world!")
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .create().show(getFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_buttons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Hello world!")
                        .setMessage("Hello world!")
                        .setPositiveButton(android.R.string.ok)
                        .setNegativeButton(android.R.string.cancel)
                        .setRequestCode(REQUEST_CODE_BUTTONS)
                        .create().show(getFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_single_choice_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogFragment.Builder()
                        .setTitle("Choose one")
                        .setSingleChoiceCheckedItem(0)
                        .setRequestCode(REQUEST_CODE_SINGLE_CHOICE_LIST)
                        .create().show(getFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, int requestCode,
            final View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(this, "OK button clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, int requestCode,
            final View view) {
        if (requestCode == REQUEST_CODE_BUTTONS) {
            Toast.makeText(this, "Cancel button clicked", Toast.LENGTH_SHORT).show();
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
    public void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, int requestCode, int position) {
        if (requestCode == REQUEST_CODE_SINGLE_CHOICE_LIST) {
            Toast.makeText(this,
                    getResources().getTextArray(R.array.single_choice)[position] + " selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
