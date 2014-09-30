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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Simple alert dialog fragment based on the android.support.v4 library.<br/>
 * If you use normal {@code Activity} for API level 11 and later,
 * use {@link com.simplealertdialog.SimpleAlertDialogFragment} instead.
 *
 * @author Soichiro Kashima
 * @see com.simplealertdialog.SimpleAlertDialogFragment
 */
public class SimpleAlertDialogSupportFragment extends DialogFragment {

    /**
     * Default constructor.
     */
    public SimpleAlertDialogSupportFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        // Cancelable must be set to DialogFragment
        if (args != null && args.containsKey(SimpleAlertDialog.ARG_CANCELABLE)) {
            setCancelable(args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE, true));
        }
        return new InternalHelper<Fragment, FragmentActivity>() {
            public FragmentActivity getActivity() {
                return SimpleAlertDialogSupportFragment.this.getActivity();
            }

            public Fragment getTargetFragment() {
                return SimpleAlertDialogSupportFragment.this.getTargetFragment();
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
            SimpleAlertDialog.Builder<SimpleAlertDialogSupportFragment, Fragment> {
        private Fragment mTargetFragment;

        @Override
        public Builder setTargetFragment(final Fragment targetFragment) {
            mTargetFragment = targetFragment;
            return this;
        }

        @Override
        public SimpleAlertDialogSupportFragment create() {
            Bundle args = createArguments();
            SimpleAlertDialogSupportFragment fragment = new SimpleAlertDialogSupportFragment();
            fragment.setArguments(args);
            if (mTargetFragment != null) {
                fragment.setTargetFragment(mTargetFragment, 0);
            }
            return fragment;
        }
    }

}
