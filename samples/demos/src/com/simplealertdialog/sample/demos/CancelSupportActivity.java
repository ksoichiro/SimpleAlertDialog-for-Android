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
import com.simplealertdialog.SimpleAlertDialogSupportFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class CancelSupportActivity extends FragmentActivity
        implements SimpleAlertDialog.OnCancelListener {

    private static final int REQUEST_CODE_CANCELABLE = 1;
    private static final int REQUEST_CODE_DISALLOW_CANCEL_ON_TOUCH_OUTSIDE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
    }

    protected void initLayout() {
        setContentView(R.layout.activity_cancel_support);
        initButtons();
    }

    protected void initButtons() {
        findViewById(R.id.btn_cancelable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setMessage("Cancelable")
                        .setPositiveButton(android.R.string.ok)
                        .setRequestCode(REQUEST_CODE_CANCELABLE)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_not_cancelable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new SimpleAlertDialogSupportFragment.Builder()
                        .setMessage("Not cancelable")
                        .setPositiveButton(android.R.string.ok)
                        .setCancelable(false)
                        .create().show(getSupportFragmentManager(), "dialog");
            }
        });

        findViewById(R.id.btn_disallow_cancel_on_touch_outside).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new SimpleAlertDialogSupportFragment.Builder()
                                .setMessage("Cancelable on press back key, "
                                        + "but not cancelable on touch outside")
                                .setPositiveButton(android.R.string.ok)
                                .setCanceledOnTouchOutside(false)
                                .setRequestCode(REQUEST_CODE_DISALLOW_CANCEL_ON_TOUCH_OUTSIDE)
                                .create().show(getSupportFragmentManager(), "dialog");
                    }
                });
    }

    @Override
    public void onDialogCancel(SimpleAlertDialog dialog, int requestCode, View view) {
        if (requestCode == REQUEST_CODE_CANCELABLE) {
            Toast.makeText(this, "Canceled: cancelable dialog", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_DISALLOW_CANCEL_ON_TOUCH_OUTSIDE) {
            Toast.makeText(this, "Canceled: not cancelable on touch outside dialog", Toast.LENGTH_SHORT).show();
        }
    }
}
