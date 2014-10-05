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
import android.app.Fragment;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

import com.simplealertdialog.SimpleAlertDialogFragment;

/**
 * Basic tests that use SimpleAlertDialog directly from test cases.
 */
public class DirectUseTest extends ActivityInstrumentationTestCase2<DummyActivity> {

    @TargetApi(Build.VERSION_CODES.FROYO)
    public DirectUseTest() {
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
    public void testBuilderCreateNullParams() {
        new SimpleAlertDialogFragment.Builder()
                .setIcon(0)
                .setTitle(null)
                .setMessage(null)
                .setPositiveButton(null)
                .setNegativeButton(null)
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
