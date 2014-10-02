package com.simplealertdialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

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
                .setTitle("Test")
                .setMessage("Test")
                .create().show(getActivity().getFragmentManager(), "dialog");
        getInstrumentation().waitForIdleSync();
        Fragment f = getActivity().getFragmentManager().findFragmentByTag("dialog");
        assertNotNull(f);
    }

}
