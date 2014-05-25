package com.simplealertdialog.sample.demos.test;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import android.view.KeyEvent;

import com.simplealertdialog.sample.demos.AboutActivity;
import com.simplealertdialog.sample.demos.MainActivity;

public class MainActivityTest extends InstrumentationTestCase {

    private MainActivity mainActivity;

    public void testLaunch() {
        mainActivity = launchActivity("com.simplealertdialog.sample.demos", MainActivity.class, null);
        getInstrumentation().waitForIdleSync();
        assertNotNull(mainActivity);
        mainActivity.finish();
    }

    public void testLaunchAboutActivity() {
        Instrumentation.ActivityMonitor monitorAbout = new Instrumentation.ActivityMonitor(AboutActivity.class.getCanonicalName(), null, false);
        getInstrumentation().addMonitor(monitorAbout);

        mainActivity = launchActivity("com.simplealertdialog.sample.demos", MainActivity.class, null);
        getInstrumentation().waitForIdleSync();

        mainActivity.openOptionsMenu();
        getInstrumentation().waitForIdleSync();

        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        getInstrumentation().waitForIdleSync();

        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        getInstrumentation().waitForIdleSync();

        AboutActivity aboutActivity = (AboutActivity) getInstrumentation().waitForMonitorWithTimeout(monitorAbout, 3000);
        assertNotNull(aboutActivity);
        assertTrue(getInstrumentation().checkMonitorHit(monitorAbout, 1));

        aboutActivity.finish();
    }
}
