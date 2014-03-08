package com.simplealertdialog;

import android.test.InstrumentationTestCase;

public class SimpleAlertDialogFragmentTest extends InstrumentationTestCase {
    public void testBuilderSetTitle() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        assertEquals(builder.setTitle("Title"), builder);
    }
}
