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
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * Internal helper class to build dialog.
 *
 * @param <F> Fragment class
 * @param <A> Activity class
 */
abstract class InternalHelper<F, A extends Context> {
    public abstract A getActivity();

    public abstract F getTargetFragment();

    public Dialog createDialog(Bundle args) {
        final SimpleAlertDialog dialog = newInstance(args);
        setTitle(args, dialog);
        setIcon(args, dialog);
        setMessage(args, dialog);
        final int requestCode = getRequestCode(args);
        setView(args, dialog, requestCode);
        setItems(args, dialog, requestCode);
        setAdapter(args, dialog, requestCode);
        setSingleChoiceItems(args, dialog, requestCode);
        setPositiveButton(args, dialog, requestCode);
        setNegativeButton(args, dialog, requestCode);
        setCancelable(args, dialog);
        return dialog;
    }

    public boolean hasItemClickListener() {
        return (fragmentImplements(SimpleAlertDialog.OnItemClickListener.class)
                || activityImplements(SimpleAlertDialog.OnItemClickListener.class));
    }

    public boolean hasListProvider(Bundle args) {
        boolean useAdapter = false;
        if (has(args, SimpleAlertDialog.ARG_USE_ADAPTER)) {
            useAdapter = args.getBoolean(SimpleAlertDialog.ARG_USE_ADAPTER);
        }
        return useAdapter
                && (fragmentImplements(SimpleAlertDialog.ListProvider.class)
                || activityImplements(SimpleAlertDialog.ListProvider.class));
    }

    public boolean hasSingleChoiceArrayItemProvider(Bundle args) {
        int singleChoiceCheckedItem = -1;
        if (has(args, SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM)) {
            singleChoiceCheckedItem = args.getInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
        }
        return singleChoiceCheckedItem >= 0
                && (fragmentImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)
                || activityImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class));
    }

    private SimpleAlertDialog newInstance(Bundle args) {
        return hasTheme(args)
                ? new SimpleAlertDialog(getActivity(), args.getInt(SimpleAlertDialog.ARG_THEME_RES_ID))
                : new SimpleAlertDialog(getActivity());
    }

    private boolean has(Bundle args, String key) {
        return args != null && args.containsKey(key);
    }

    private boolean hasTheme(Bundle args) {
        return has(args, SimpleAlertDialog.ARG_THEME_RES_ID);
    }

    private void setTitle(Bundle args, SimpleAlertDialog dialog) {
        if (has(args, SimpleAlertDialog.ARG_TITLE)) {
            dialog.setTitle(args.getCharSequence(SimpleAlertDialog.ARG_TITLE));
        } else if (has(args, SimpleAlertDialog.ARG_TITLE_RES_ID)) {
            dialog.setTitle(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID));
        }
    }

    private void setIcon(Bundle args, SimpleAlertDialog dialog) {
        if (has(args, SimpleAlertDialog.ARG_ICON)) {
            dialog.setIcon(args.getInt(SimpleAlertDialog.ARG_ICON));
        }
    }

    private void setMessage(Bundle args, SimpleAlertDialog dialog) {
        if (has(args, SimpleAlertDialog.ARG_MESSAGE)) {
            dialog.setMessage(args.getCharSequence(SimpleAlertDialog.ARG_MESSAGE));
        } else if (has(args, SimpleAlertDialog.ARG_MESSAGE_RES_ID)) {
            dialog.setMessage(args.getInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID));
        }
    }

    private int getRequestCode(Bundle args) {
        if (has(args, SimpleAlertDialog.ARG_REQUEST_CODE)) {
            return args.getInt(SimpleAlertDialog.ARG_REQUEST_CODE);
        }
        return 0;
    }

    private void setView(Bundle args, SimpleAlertDialog dialog, int requestCode) {
        if (!has(args, SimpleAlertDialog.ARG_USE_VIEW) || !args.getBoolean(SimpleAlertDialog.ARG_USE_VIEW)) {
            return;
        }
        if (fragmentImplements(SimpleAlertDialog.ViewProvider.class)) {
            dialog.setView(((SimpleAlertDialog.ViewProvider) getTargetFragment())
                    .onCreateView(dialog, requestCode));
        }
        if (activityImplements(SimpleAlertDialog.ViewProvider.class)) {
            dialog.setView(((SimpleAlertDialog.ViewProvider) getActivity())
                    .onCreateView(dialog, requestCode));
        }
    }

    private void setItems(Bundle args, final SimpleAlertDialog dialog, final int requestCode) {
        if (!hasItemClickListener()) {
            return;
        }
        CharSequence[] items;
        if (has(args, SimpleAlertDialog.ARG_ITEMS) && Build.VERSION_CODES.ECLAIR <= Build.VERSION.SDK_INT) {
            items = args.getCharSequenceArray(SimpleAlertDialog.ARG_ITEMS);
        } else if (has(args, SimpleAlertDialog.ARG_ITEMS_RES_ID)) {
            items = getActivity().getResources().getTextArray(args.getInt(SimpleAlertDialog.ARG_ITEMS_RES_ID));
        } else {
            return;
        }
        if (fragmentImplements(SimpleAlertDialog.OnItemClickListener.class)
                || activityImplements(SimpleAlertDialog.OnItemClickListener.class)) {
            dialog.setItems(items,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (fragmentImplements(SimpleAlertDialog.OnItemClickListener.class)) {
                                ((SimpleAlertDialog.OnItemClickListener) getTargetFragment())
                                        .onItemClick(dialog, requestCode, position);
                            }
                            if (activityImplements(SimpleAlertDialog.OnItemClickListener.class)) {
                                ((SimpleAlertDialog.OnItemClickListener) getActivity())
                                        .onItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
    }

    private void setAdapter(Bundle args, final SimpleAlertDialog dialog, final int requestCode) {
        if (!hasListProvider(args)) {
            return;
        }
        if (fragmentImplements(SimpleAlertDialog.ListProvider.class)) {
            dialog.setAdapter(((SimpleAlertDialog.ListProvider) getTargetFragment())
                            .onCreateList(dialog, requestCode),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (fragmentImplements(SimpleAlertDialog.ListProvider.class)) {
                                ((SimpleAlertDialog.ListProvider) getTargetFragment())
                                        .onListItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
        if (activityImplements(SimpleAlertDialog.ListProvider.class)) {
            dialog.setAdapter(((SimpleAlertDialog.ListProvider) getActivity())
                            .onCreateList(dialog, requestCode),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (activityImplements(SimpleAlertDialog.ListProvider.class)) {
                                ((SimpleAlertDialog.ListProvider) getActivity())
                                        .onListItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
    }

    private void setSingleChoiceItems(Bundle args, final SimpleAlertDialog dialog, final int requestCode) {
        if (!hasSingleChoiceArrayItemProvider(args)) {
            return;
        }
        int checkedItem = args.getInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
        if (fragmentImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
            dialog.setSingleChoiceItems(
                    ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getTargetFragment())
                            .onCreateSingleChoiceArray(dialog, requestCode),
                    checkedItem,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (fragmentImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
                                ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getTargetFragment())
                                        .onSingleChoiceArrayItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
        if (activityImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
            dialog.setSingleChoiceItems(
                    ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getActivity())
                            .onCreateSingleChoiceArray(dialog, requestCode),
                    checkedItem,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (activityImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
                                ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getActivity())
                                        .onSingleChoiceArrayItemClick(dialog, requestCode, position);
                            }
                        }
                    });
        }
    }

    private void setPositiveButton(Bundle args, SimpleAlertDialog dialog, final int requestCode) {
        CharSequence positiveButton = null;
        if (has(args, SimpleAlertDialog.ARG_POSITIVE_BUTTON)) {
            positiveButton = args.getCharSequence(SimpleAlertDialog.ARG_POSITIVE_BUTTON);
        } else if (has(args, SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID)) {
            positiveButton = getActivity().getString(args.getInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID));
        }
        if (positiveButton == null) {
            return;
        }
        dialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (fragmentImplements(SimpleAlertDialog.OnClickListener.class)) {
                    ((SimpleAlertDialog.OnClickListener) getTargetFragment())
                            .onDialogPositiveButtonClicked((SimpleAlertDialog) dialog,
                                    requestCode,
                                    ((SimpleAlertDialog) dialog).getView());
                }
                if (activityImplements(SimpleAlertDialog.OnClickListener.class)) {
                    ((SimpleAlertDialog.OnClickListener) getActivity())
                            .onDialogPositiveButtonClicked((SimpleAlertDialog) dialog,
                                    requestCode,
                                    ((SimpleAlertDialog) dialog).getView());
                }
            }
        });
    }

    private void setNegativeButton(Bundle args, SimpleAlertDialog dialog, final int requestCode) {
        CharSequence negativeButton = null;
        if (has(args, SimpleAlertDialog.ARG_NEGATIVE_BUTTON)) {
            negativeButton = args.getCharSequence(SimpleAlertDialog.ARG_NEGATIVE_BUTTON);
        } else if (has(args, SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID)) {
            negativeButton = getActivity().getString(args.getInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID));
        }
        if (negativeButton == null) {
            return;
        }
        dialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (fragmentImplements(SimpleAlertDialog.OnClickListener.class)) {
                    ((SimpleAlertDialog.OnClickListener) getTargetFragment())
                            .onDialogNegativeButtonClicked((SimpleAlertDialog) dialog,
                                    requestCode,
                                    ((SimpleAlertDialog) dialog).getView());
                }
                if (activityImplements(SimpleAlertDialog.OnClickListener.class)) {
                    ((SimpleAlertDialog.OnClickListener) getActivity())
                            .onDialogNegativeButtonClicked((SimpleAlertDialog) dialog,
                                    requestCode,
                                    ((SimpleAlertDialog) dialog).getView());
                }
            }
        });
    }

    private void setCancelable(Bundle args, SimpleAlertDialog dialog) {
        boolean cancelable = true;
        if (has(args, SimpleAlertDialog.ARG_CANCELABLE)) {
            cancelable = args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE);
        }
        boolean canceledOnTouchOutside = cancelable;
        if (cancelable && has(args, SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE)) {
            canceledOnTouchOutside = args.getBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE);
        }
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    private boolean fragmentImplements(Class<?> c) {
        return getTargetFragment() != null && c != null && c.isAssignableFrom(getTargetFragment().getClass());
    }

    private boolean activityImplements(Class<?> c) {
        return getActivity() != null && c != null && c.isAssignableFrom(getActivity().getClass());
    }
}
