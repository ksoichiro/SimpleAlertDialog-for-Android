package com.simplealertdialog.test;

import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogFragment;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

public class SimpleAlertDialogFragmentTest extends InstrumentationTestCase {

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
        assertEquals(args.getString(SimpleAlertDialog.ARG_TITLE_RES_ID), null);
        assertEquals(builder.setTitle(R.string.title), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID), R.string.title);
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
        assertEquals(args.getString(SimpleAlertDialog.ARG_MESSAGE_RES_ID), null);
        assertEquals(builder.setMessage(R.string.message), builder);
        args = builder.createArguments();
        assertEquals(args.getInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID), R.string.message);
    }

}
