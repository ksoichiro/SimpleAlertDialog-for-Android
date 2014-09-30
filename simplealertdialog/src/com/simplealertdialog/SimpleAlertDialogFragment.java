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

package com.simplealertdialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

/**
 * Simple alert dialog fragment based on the normal {@code Activity}
 * for API level 11 and later which supports {@code Fragment}.
 * If you use android.support.v4 library,
 * use {@link com.simplealertdialog.SimpleAlertDialogSupportFragment} instead.
 *
 * @author Soichiro Kashima
 * @see com.simplealertdialog.SimpleAlertDialogSupportFragment
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SimpleAlertDialogFragment extends DialogFragment {

    /**
     * Default constructor.
     */
    public SimpleAlertDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        // Cancelable must be set to DialogFragment
        if (args != null && args.containsKey(SimpleAlertDialog.ARG_CANCELABLE)) {
            setCancelable(args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE, true));
        }
        return new SimpleAlertDialog.Helper<SimpleAlertDialogFragment, Fragment, Activity>() {
            public Activity getActivity() {
                return SimpleAlertDialogFragment.this.getActivity();
            }

            public Fragment getTargetFragment() {
                return SimpleAlertDialogFragment.this.getTargetFragment();
            }
        }.createDialog(args);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        int requestCode = 0;
        Bundle args = getArguments();
        if (args != null && args.containsKey(SimpleAlertDialog.ARG_CANCELABLE)) {
            requestCode = args.getInt(SimpleAlertDialog.ARG_REQUEST_CODE);
        }
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null
                && targetFragment instanceof SimpleAlertDialog.OnCancelListener) {
            ((SimpleAlertDialog.OnCancelListener) targetFragment)
                    .onDialogCancel((SimpleAlertDialog) dialog,
                            requestCode,
                            ((SimpleAlertDialog) dialog).getView());
        }
        if (getActivity() != null
                && getActivity() instanceof SimpleAlertDialog.OnCancelListener) {
            ((SimpleAlertDialog.OnCancelListener) getActivity())
                    .onDialogCancel((SimpleAlertDialog) dialog,
                            requestCode,
                            ((SimpleAlertDialog) dialog).getView());
        }
    }

    /**
     * {@inheritDoc}
     */
    public static class Builder extends
            SimpleAlertDialog.Builder<SimpleAlertDialogFragment, Fragment> {
        private Fragment mTargetFragment;

        @Override
        public Builder setTargetFragment(final Fragment targetFragment) {
            mTargetFragment = targetFragment;
            return this;
        }

        @Override
        public SimpleAlertDialogFragment create() {
            Bundle args = createArguments();
            SimpleAlertDialogFragment fragment = new SimpleAlertDialogFragment();
            fragment.setArguments(args);
            if (mTargetFragment != null) {
                fragment.setTargetFragment(mTargetFragment, 0);
            }
            return fragment;
        }
    }

}
