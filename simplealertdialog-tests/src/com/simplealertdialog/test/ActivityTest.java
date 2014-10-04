package com.simplealertdialog.test;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.simplealertdialog.SimpleAlertDialogFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActivityTest extends ActivityInstrumentationTestCase2<NormalActivity> {

    private NormalActivity activity;

    public ActivityTest() {
        super(NormalActivity.class);
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
                activity.findViewById(com.simplealertdialog.test.R.id.btn_message).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogFragment) f).getDialog();
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
                activity.findViewById(com.simplealertdialog.test.R.id.btn_buttons).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
        Dialog d = ((SimpleAlertDialogFragment) f).getDialog();
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

    public void testSingleChoiceItems() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(com.simplealertdialog.test.R.id.btn_single_choice_list).performClick();
                activity.getFragmentManager().executePendingTransactions();
            }
        });
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);

        Dialog d = ((SimpleAlertDialogFragment) f).getDialog();
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
}
