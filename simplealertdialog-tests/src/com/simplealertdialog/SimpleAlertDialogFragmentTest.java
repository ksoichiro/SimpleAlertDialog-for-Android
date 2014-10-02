package com.simplealertdialog;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

public class SimpleAlertDialogFragmentTest extends InstrumentationTestCase {

    public void testBuilderSetTheme() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_THEME_RES_ID, -1), -1);
        builder.setTheme(1);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_THEME_RES_ID, 0), 1);
    }

    public void testBuilderSetTitle() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getString(SimpleAlertDialog.ARG_TITLE), null);
        assertEquals(builder.setTitle("Title"), builder);
        args = builder.createArguments();
        assertEquals(args.getString(SimpleAlertDialog.ARG_TITLE), "Title");
    }

    public void testBuilderSetTitleByResources() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID, -1), -1);
        assertEquals(builder.setTitle(R.string.sad__icon_description), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID, -1), R.string.sad__icon_description);
    }

    public void testBuilderSetIcon() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_ICON, -1), -1);
        assertEquals(builder.setIcon(android.R.drawable.ic_dialog_alert), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_ICON, -1), android.R.drawable.ic_dialog_alert);
    }

    public void testBuilderSetMessage() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getString(SimpleAlertDialog.ARG_MESSAGE), null);
        assertEquals(builder.setMessage("Message"), builder);
        args = builder.createArguments();
        assertEquals(args.getString(SimpleAlertDialog.ARG_MESSAGE), "Message");
    }

    public void testBuilderSetMessageByResources() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID, -1), -1);
        assertEquals(builder.setMessage(R.string.sad__icon_description), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID, -1), R.string.sad__icon_description);
    }

    public void testBuilderSetPositiveButton() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getString(SimpleAlertDialog.ARG_POSITIVE_BUTTON), null);
        assertEquals(builder.setPositiveButton("POSITIVE"), builder);
        args = builder.createArguments();
        assertEquals(args.getString(SimpleAlertDialog.ARG_POSITIVE_BUTTON), "POSITIVE");
    }

    public void testBuilderSetPositiveButtonByResources() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID, -1), -1);
        assertEquals(builder.setPositiveButton(android.R.string.ok), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID, -1), android.R.string.ok);
    }

    public void testBuilderSetNegativeButton() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getString(SimpleAlertDialog.ARG_NEGATIVE_BUTTON), null);
        assertEquals(builder.setNegativeButton("NEGATIVE"), builder);
        args = builder.createArguments();
        assertEquals(args.getString(SimpleAlertDialog.ARG_NEGATIVE_BUTTON), "NEGATIVE");
    }

    public void testBuilderSetRequestCode() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_REQUEST_CODE, 1), 0);
        builder.setRequestCode(1);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_REQUEST_CODE, 0), 1);
    }

    public void testBuilderSetCancelable() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertTrue(args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE, false));
        builder.setCancelable(false);
        args = builder.createArguments();
        assertFalse(args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE, true));
    }

    public void testBuilderSetCanceledOnTouchOutside() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertTrue(args.getBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE, false));
        builder.setCanceledOnTouchOutside(false);
        args = builder.createArguments();
        assertFalse(args.getBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE, true));
    }

    public void testBuilderSetUseView() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertFalse(args.getBoolean(SimpleAlertDialog.ARG_USE_VIEW, true));
        builder.setUseView(true);
        args = builder.createArguments();
        assertTrue(args.getBoolean(SimpleAlertDialog.ARG_USE_VIEW, false));
    }

    public void testBuilderSetNegativeButtonByResources() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        Bundle args = builder.createArguments();
        assertNotNull(args);
        assertEquals(args.getInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID, -1), -1);
        assertEquals(builder.setNegativeButton(android.R.string.cancel), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID, -1), android.R.string.cancel);
    }

    public void testBuilderCreate() {
        SimpleAlertDialogFragment.Builder builder = new SimpleAlertDialogFragment.Builder();
        SimpleAlertDialogFragment fragment = builder.create();
        assertNotNull(fragment);
    }

}
