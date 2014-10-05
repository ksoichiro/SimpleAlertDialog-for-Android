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
import android.content.Context;
import android.test.InstrumentationTestCase;

/**
 * This test case is testing internal methods including package private fields.
 * Do not move this class to {@code .test} package.
 */
public class InternalHelperTest extends InstrumentationTestCase {

    public void testInternalHelperCreate() {
        Dialog dialog = new InternalHelper<android.app.Fragment, Context>() {
            @Override
            public Context getActivity() {
                return getInstrumentation().getContext();
            }

            @Override
            public android.app.Fragment getTargetFragment() {
                return null;
            }
        }.createDialog(null);
        assertNotNull(dialog);

        try {
            new InternalHelper<android.app.Fragment, Context>() {
                @Override
                public Context getActivity() {
                    return null;
                }

                @Override
                public android.app.Fragment getTargetFragment() {
                    return null;
                }
            }.createDialog(null);
            fail();
        } catch (NullPointerException e) {
            // Can't create dialog without context(getActivity)
        }
    }
}
