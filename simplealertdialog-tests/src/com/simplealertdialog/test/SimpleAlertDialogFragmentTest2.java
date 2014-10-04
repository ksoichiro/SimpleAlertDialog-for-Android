package com.simplealertdialog.test;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

import com.simplealertdialog.SimpleAlertDialogFragment;

public class SimpleAlertDialogFragmentTest2 extends ActivityInstrumentationTestCase2<DummyActivity> {

    @TargetApi(Build.VERSION_CODES.FROYO)
    public SimpleAlertDialogFragmentTest2() {
        super(DummyActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void testBuilderCreate() {
        new SimpleAlertDialogFragment.Builder()
                .create().show(getActivity().getFragmentManager(), "dialog");
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void testBuilderCreateHasParams() {
        new SimpleAlertDialogFragment.Builder()
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Test")
                .setMessage("Test")
                .setPositiveButton("OK")
                .setNegativeButton("Cancel")
                .create().show(getActivity().getFragmentManager(), "dialog");
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void testBuilderCreateHasParamsNoIcon() {
        new SimpleAlertDialogFragment.Builder()
                .setTitle("Test")
                .setMessage("Test")
                .setPositiveButton("OK")
                .create().show(getActivity().getFragmentManager(), "dialog");
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }

}
