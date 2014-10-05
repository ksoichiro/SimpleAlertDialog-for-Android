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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.simplealertdialog.SimpleAlertDialogSupportFragment;

/**
 * Tests for using SimpleAlertDialog with FragmentActivity in support-v4 library.
 */
public class SupportActivityTest extends ActivityInstrumentationTestCase2<SupportActivity> {

    private SupportActivity activity;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SupportActivityTest() {
        super(SupportActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        activity = getActivity();
    }

    public void testMessage() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_message).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);
        final View positive = d.findViewById(R.id.button_positive);
        assertNotNull(positive);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                positive.performClick();
            }
        });
    }

    public void testButtons() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_buttons).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);
        View positive = d.findViewById(R.id.button_positive);
        assertNotNull(positive);
        final View negative = d.findViewById(R.id.button_negative);
        assertNotNull(negative);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                negative.performClick();
            }
        });
    }

    public void testAdapter() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(com.simplealertdialog.test.R.id.btn_adapter).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);

        final ListView lv = (ListView) d.findViewById(R.id.list);
        assertNotNull(lv);
        assertTrue(lv.getAdapter() instanceof SweetsAdapter);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.performItemClick(lv, 0, 0);
            }
        });
    }

    public void testSingleChoiceItems() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_single_choice_list).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);

        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);
        final ListView lv = (ListView) d.findViewById(R.id.list);
        assertNotNull(lv);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.performItemClick(lv, 0, 0);
            }
        });
    }

    public void testView() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_view).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        sendKeys(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
    }

    public void testThemed() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_themed).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }
}
