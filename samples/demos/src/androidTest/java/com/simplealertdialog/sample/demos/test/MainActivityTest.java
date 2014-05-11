package com.simplealertdialog.sample.demos.test;

import android.test.InstrumentationTestCase;

import com.simplealertdialog.sample.demos.MainActivity;

public class MainActivityTest extends InstrumentationTestCase {

    public void testLaunch() {
        MainActivity activity = launchActivity("com.simplealertdialog.sample.demos", MainActivity.class, null);
        getInstrumentation().waitForIdleSync();
        assertNotNull(activity);
    }

}
