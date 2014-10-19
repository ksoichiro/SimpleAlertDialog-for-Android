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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.simplealertdialog.SimpleAlertDialogFragment;
import com.simplealertdialog.SimpleAlertDialogSupportFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentSupportActivityTest extends ActivityInstrumentationTestCase2<FragmentSupportActivity> {

    private FragmentSupportActivity activity;

    public FragmentSupportActivityTest() {
        super(FragmentSupportActivity.class);
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
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_message).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
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
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_buttons).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
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

    public void test3Buttons() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_3_buttons).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
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

    public void test3Buttons_Neutral() throws Throwable {
        getInstrumentation().waitForIdleSync();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_3_buttons).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);
        final View neutral = d.findViewById(R.id.button_neutral);
        assertNotNull(neutral);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                neutral.performClick();
            }
        });
    }

    public void testItems() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_items).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);

        final ListView lv = (ListView) d.findViewById(R.id.list);
        assertNotNull(lv);
        assertTrue(lv.getAdapter() instanceof ArrayAdapter<?>);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.performItemClick(lv, 0, 0);
            }
        });
    }

    public void testItemsWithIcons() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_icon_items).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogSupportFragment) f).getDialog();
        assertNotNull(d);

        final ListView lv = (ListView) d.findViewById(R.id.list);
        assertNotNull(lv);
        assertTrue(lv.getAdapter() instanceof ArrayAdapter<?>);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.performItemClick(lv, 0, 0);
            }
        });
    }

    public void testAdapter() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_adapter).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
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
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_single_choice_list).performClick();
                activity.getFragmentManager().executePendingTransactions();
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
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_view).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        sendKeys(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
    }

    public void testEditText() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_edit_text).performClick();
                activity.getFragmentManager().executePendingTransactions();
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
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_sample);
                assertNotNull(f);
                assertNotNull(f.getView());
                f.getView().findViewById(R.id.btn_frag_themed).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }
}
