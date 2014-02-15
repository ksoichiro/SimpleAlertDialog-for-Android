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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Soichiro Kashima
 */
public class SimpleAlertDialog extends Dialog {

    public static interface OnClickListener {
        void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                final View view);

        void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                final View view);
    }

    public static interface ViewProvider {
        View onCreateView(final SimpleAlertDialog dialog, final int requestCode);
    }

    public static interface ListProvider {
        ListAdapter onCreateList(final SimpleAlertDialog dialog, final int requestCode);

        void onListItemClick(final SimpleAlertDialog dialog, final int requestCode,
                final int position);
    }

    public static interface SingleChoiceArrayItemProvider {
        CharSequence[] onCreateSingleChoiceArray(final SimpleAlertDialog dialog,
                final int requestCode);

        void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, final int requestCode,
                final int position);
    }

    public static final String ARG_TITLE = "argTitle";
    public static final String ARG_TITLE_RES_ID = "argTitleResId";
    public static final String ARG_ICON = "argIcon";
    public static final String ARG_MESSAGE = "argMessage";
    public static final String ARG_MESSAGE_RES_ID = "argMessageResId";
    public static final String ARG_POSITIVE_BUTTON = "argPositiveButton";
    public static final String ARG_POSITIVE_BUTTON_RES_ID = "argPositiveButtonResId";
    public static final String ARG_NEGATIVE_BUTTON = "argNegativeButton";
    public static final String ARG_NEGATIVE_BUTTON_RES_ID = "argNegativeButtonResId";
    public static final String ARG_REQUEST_CODE = "argRequestCode";
    public static final String ARG_CANCELABLE = "argCancelable";
    public static final String ARG_SINGLE_CHOICE_CHECKED_ITEM = "argSingleChoiceCheckedItem";
    public static final String ARG_USE_VIEW = "argUseView";
    public static final String ARG_USE_ADAPTER = "argUseAdapter";

    private CharSequence mMessage;
    private CharSequence mTitle;
    private int mIcon;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private DialogInterface.OnClickListener mPositiveButtonListener;
    private DialogInterface.OnClickListener mNegativeButtonListener;
    private View mView;
    private ListAdapter mAdapter;
    private boolean mSingleChoice = true;
    private int mCheckedItem;
    private AdapterView.OnItemClickListener mListItemListener;

    private int mListChoiceIndicatorSingle;
    private int mTitleTextStyle;
    private int mMessageTextStyle;
    private int mButtonTextStyle;
    private int mListItemTextStyle;
    private Drawable mTitleSeparatorBackground;
    private int mTitleSeparatorHeight;
    private Drawable mButtonTopDividerBackground;
    private Drawable mButtonVerticalDividerBackground;
    private Drawable mBackgroundFull;
    private Drawable mBackgroundTop;
    private Drawable mBackgroundMiddle;
    private Drawable mBackgroundBottom;

    public SimpleAlertDialog(Context context) {
        super(context);
        obtainStyles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_simple);
        getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Background
        setBackground(R.id.header, mBackgroundTop);
        setBackground(R.id.bar_wrapper, mBackgroundMiddle);
        setBackground(R.id.body, mBackgroundBottom);

        // Title
        if (TextUtils.isEmpty(mTitle)) {
            findViewById(R.id.header).setVisibility(View.GONE);
            findViewById(R.id.bar_wrapper).setVisibility(View.GONE);
            findViewById(R.id.title).setVisibility(View.GONE);
            findViewById(R.id.icon).setVisibility(View.GONE);
            setBackground(R.id.body, mBackgroundFull);
        } else {
            ((TextView) findViewById(R.id.title)).setText(mTitle);
            if (mTitleTextStyle != 0) {
                ((TextView) findViewById(R.id.title)).setTextAppearance(getContext(),
                        mTitleTextStyle);
            }
            if (mIcon > 0) {
                ((ImageView) findViewById(R.id.icon)).setImageResource(mIcon);
                findViewById(R.id.title).setPadding(
                        findViewById(R.id.title).getPaddingLeft() / 2,
                        findViewById(R.id.title).getPaddingTop(),
                        findViewById(R.id.title).getPaddingRight(),
                        findViewById(R.id.title).getPaddingBottom());
            } else {
                findViewById(R.id.icon).setVisibility(View.GONE);
            }
            setBackground(R.id.bar, mTitleSeparatorBackground);
            if (mTitleSeparatorHeight == 0) {
                mTitleSeparatorHeight = getContext().getResources().getDimensionPixelSize(
                        R.dimen.dialog_title_separator_height);
            }
            FrameLayout.LayoutParams lpBar = new FrameLayout.LayoutParams(
                    getMatchParent(),
                    mTitleSeparatorHeight);
            findViewById(R.id.bar).setLayoutParams(lpBar);
            findViewById(R.id.bar).requestLayout();
            LinearLayout.LayoutParams lpBarWrapper = new LinearLayout.LayoutParams(
                    getMatchParent(),
                    mTitleSeparatorHeight);
            findViewById(R.id.bar_wrapper).setLayoutParams(lpBarWrapper);
            findViewById(R.id.bar_wrapper).requestLayout();
        }

        // Message
        if (TextUtils.isEmpty(mMessage)) {
            findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.message)).setText(mMessage);
            if (mMessageTextStyle != 0) {
                ((TextView) findViewById(R.id.message)).setTextAppearance(getContext(),
                        mMessageTextStyle);
            }
        }

        // Custom View
        if (mView != null) {
            LinearLayout group = (LinearLayout) findViewById(R.id.view);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    getMatchParent(),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            group.addView(mView, lp);
        } else {
            findViewById(R.id.view).setVisibility(View.GONE);
        }

        // Custom Adapter
        if (mAdapter != null) {
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(mAdapter);
            if (mSingleChoice) {
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
            if (mSingleChoice && 0 <= mCheckedItem && mCheckedItem < mAdapter.getCount()) {
                list.setItemChecked(mCheckedItem, true);
                list.setSelectionFromTop(mCheckedItem, 0);
            }
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListItemListener != null) {
                        mListItemListener.onItemClick(parent, view, position, id);
                    }
                    dismiss();
                }
            });
        } else {
            findViewById(R.id.list).setVisibility(View.GONE);
        }

        // Positive Button
        boolean hasPositiveButton = false;
        if (mPositiveButtonText != null) {
            hasPositiveButton = true;
            ((TextView) findViewById(R.id.button_positive_label)).setText(mPositiveButtonText);
            if (mButtonTextStyle != 0) {
                ((TextView) findViewById(R.id.button_positive_label)).setTextAppearance(
                        getContext(), mButtonTextStyle);
            }
        }
        if (mPositiveButtonListener != null) {
            hasPositiveButton = true;
            findViewById(R.id.button_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(SimpleAlertDialog.this, 0);
                    }
                    dismiss();
                }
            });
        }
        if (!hasPositiveButton) {
            findViewById(R.id.button_positive).setVisibility(View.GONE);
        }

        // Negative Button
        boolean hasNegativeButton = false;
        if (mNegativeButtonText != null) {
            hasNegativeButton = true;
            ((TextView) findViewById(R.id.button_negative_label)).setText(mNegativeButtonText);
            if (mButtonTextStyle != 0) {
                ((TextView) findViewById(R.id.button_negative_label)).setTextAppearance(
                        getContext(), mButtonTextStyle);
            }
        }
        if (mNegativeButtonListener != null) {
            hasNegativeButton = true;
            findViewById(R.id.button_negative).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(SimpleAlertDialog.this, 1);
                    }
                    dismiss();
                }
            });
        }
        if (!hasNegativeButton) {
            findViewById(R.id.button_negative).setVisibility(View.GONE);
        }

        if (!hasPositiveButton && !hasNegativeButton) {
            findViewById(R.id.button_divider_top).setVisibility(View.GONE);
            findViewById(R.id.button_divider).setVisibility(View.GONE);
        } else if (!hasPositiveButton || !hasNegativeButton) {
            findViewById(R.id.button_divider).setVisibility(View.GONE);
            setBackground(R.id.button_divider_top, mButtonTopDividerBackground);
        } else {
            setBackground(R.id.button_divider_top, mButtonTopDividerBackground);
            setBackground(R.id.button_divider, mButtonVerticalDividerBackground);
        }
    }

    public void setMessage(final CharSequence message) {
        if (message == null) {
            return;
        }
        mMessage = message;
    }

    public void setMessage(final int resId) {
        setMessage(getContext().getText(resId));
    }

    public void setTitle(final CharSequence title) {
        if (title == null) {
            return;
        }
        mTitle = title;
    }

    public void setTitle(final int resId) {
        setTitle(getContext().getText(resId));
    }

    public void setIcon(final int resId) {
        if (resId <= 0) {
            return;
        }
        mIcon = resId;
    }

    public void setView(final View view) {
        if (view == null) {
            return;
        }
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public void setAdapter(final ListAdapter adapter,
            final AdapterView.OnItemClickListener listener) {
        if (adapter == null) {
            return;
        }
        mAdapter = adapter;
        mListItemListener = listener;
    }

    public void setSingleChoiceItems(final CharSequence[] items, final int checkedItem,
            final AdapterView.OnItemClickListener listener) {
        if (items == null) {
            return;
        }
        mAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_list_item_single_choice, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view != null) {
                    CheckedTextView c = (CheckedTextView) view.findViewById(android.R.id.text1);
                    if (mListChoiceIndicatorSingle != 0) {
                        c.setCheckMarkDrawable(mListChoiceIndicatorSingle);
                    }
                    if (mListItemTextStyle != 0) {
                        c.setTextAppearance(getContext(), mListItemTextStyle);
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        Resources res = getContext().getResources();
                        c.setPadding(
                                res.getDimensionPixelSize(R.dimen.simple_list_item_padding_left),
                                0,
                                res.getDimensionPixelSize(R.dimen.simple_list_item_padding_right),
                                0);
                    }
                }
                return view;
            }
        };
        mSingleChoice = true;
        mCheckedItem = checkedItem;
        mListItemListener = listener;
    }

    public void setPositiveButton(final CharSequence text,
            final DialogInterface.OnClickListener listener) {
        if (text == null) {
            return;
        }
        mPositiveButtonText = text;
        mPositiveButtonListener = listener;
    }

    public void setPositiveButton(final int resId,
            final DialogInterface.OnClickListener listener) {
        setPositiveButton(getContext().getText(resId), listener);
    }

    public void setNegativeButton(final CharSequence text,
            final DialogInterface.OnClickListener listener) {
        if (text == null) {
            return;
        }
        mNegativeButtonText = text;
        mNegativeButtonListener = listener;
    }

    public void setNegativeButton(final int resId,
            final DialogInterface.OnClickListener listener) {
        setNegativeButton(getContext().getText(resId), listener);
    }

    private void obtainStyles() {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
                R.styleable.SimpleAlertDialogStyle, R.attr.simpleAlertDialogStyle,
                R.style.Theme_SimpleAlertDialog);

        mListChoiceIndicatorSingle = a.getResourceId(
                R.styleable.SimpleAlertDialogStyle_listChoiceIndicatorSingle, 0);
        mTitleTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_titleTextStyle, 0);
        mMessageTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_messageTextStyle, 0);
        mButtonTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_buttonTextStyle, 0);
        mListItemTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_listItemTextStyle,
                0);
        mTitleSeparatorBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_titleSeparatorBackground);
        mTitleSeparatorHeight = a.getLayoutDimension(
                R.styleable.SimpleAlertDialogStyle_titleSeparatorHeight,
                getContext().getResources().getDimensionPixelSize(
                        R.dimen.dialog_title_separator_height));
        mButtonTopDividerBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_buttonTopDividerBackground);
        mButtonVerticalDividerBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_buttonVerticalDividerBackground);
        mBackgroundFull = a.getDrawable(R.styleable.SimpleAlertDialogStyle_backgroundFull);
        mBackgroundTop = a.getDrawable(R.styleable.SimpleAlertDialogStyle_backgroundTop);
        mBackgroundMiddle = a.getDrawable(R.styleable.SimpleAlertDialogStyle_backgroundMiddle);
        mBackgroundBottom = a.getDrawable(R.styleable.SimpleAlertDialogStyle_backgroundBottom);

        a.recycle();
    }

    @SuppressWarnings("deprecation")
    private void setBackground(final int resId, final Drawable d) {
        if (resId == 0 || d == null) {
            return;
        }
        View view = findViewById(resId);
        if (view != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                view.setBackground(d);
            } else {
                view.setBackgroundDrawable(d);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private int getMatchParent() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return ViewGroup.LayoutParams.FILL_PARENT;
        } else {
            return ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    protected static abstract class Helper<T, F, A extends Context> {
        public abstract A getActivity();

        public abstract F getTargetFragment();

        public Dialog createDialog(Bundle args, F targetFragment, A activity) {
            final SimpleAlertDialog dialog = new SimpleAlertDialog(activity);
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_TITLE)) {
                dialog.setTitle(args.getCharSequence(SimpleAlertDialog.ARG_TITLE));
            } else if (args != null && args.containsKey(SimpleAlertDialog.ARG_TITLE_RES_ID)) {
                dialog.setTitle(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID));
            }
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_TITLE_RES_ID)) {
                dialog.setTitle(args.getInt(SimpleAlertDialog.ARG_TITLE_RES_ID));
            }
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_ICON)) {
                dialog.setIcon(args.getInt(SimpleAlertDialog.ARG_ICON));
            }
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_MESSAGE)) {
                dialog.setMessage(args.getCharSequence(SimpleAlertDialog.ARG_MESSAGE));
            } else if (args != null && args.containsKey(SimpleAlertDialog.ARG_MESSAGE_RES_ID)) {
                dialog.setMessage(args.getInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID));
            }
            final int requestCode;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_REQUEST_CODE)) {
                requestCode = args.getInt(SimpleAlertDialog.ARG_REQUEST_CODE);
            } else {
                requestCode = 0;
            }
            boolean useView = false;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_USE_VIEW)) {
                useView = args.getBoolean(SimpleAlertDialog.ARG_USE_VIEW);
                if (useView) {
                    if (targetFragment != null
                            && targetFragment instanceof SimpleAlertDialog.ViewProvider) {
                        dialog.setView(((SimpleAlertDialog.ViewProvider) targetFragment)
                                .onCreateView(dialog, requestCode));
                    }
                    if (activity != null
                            && activity instanceof SimpleAlertDialog.ViewProvider) {
                        dialog.setView(((SimpleAlertDialog.ViewProvider) activity)
                                .onCreateView(dialog, requestCode));
                    }
                }
            }
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_USE_ADAPTER)) {
                boolean useAdapter = args.getBoolean(SimpleAlertDialog.ARG_USE_ADAPTER);
                if (useAdapter) {
                    if (targetFragment != null
                            && targetFragment instanceof SimpleAlertDialog.ListProvider) {
                        dialog.setAdapter(((SimpleAlertDialog.ListProvider) targetFragment)
                                .onCreateList(dialog, requestCode),
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position,
                                            long id) {
                                        F targetFragment = getTargetFragment();
                                        if (targetFragment != null
                                                && targetFragment instanceof SimpleAlertDialog.ListProvider) {
                                            ((SimpleAlertDialog.ListProvider) targetFragment)
                                                    .onListItemClick(dialog, requestCode,
                                                            position);
                                        }
                                    }
                                });
                    }
                    if (activity != null
                            && activity instanceof SimpleAlertDialog.ListProvider) {
                        dialog.setAdapter(((SimpleAlertDialog.ListProvider) activity)
                                .onCreateList(dialog, requestCode),
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position,
                                            long id) {
                                        if ((getActivity() != null && getActivity() instanceof SimpleAlertDialog.ListProvider)) {
                                            ((SimpleAlertDialog.ListProvider) getActivity())
                                                    .onListItemClick(dialog, requestCode, position);
                                        }
                                    }
                                });
                    }
                }
            }
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM)) {
                int checkedItem = args.getInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
                if (targetFragment != null
                        && targetFragment instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider) {
                    dialog.setSingleChoiceItems(
                            ((SimpleAlertDialog.SingleChoiceArrayItemProvider) targetFragment)
                                    .onCreateSingleChoiceArray(dialog, requestCode),
                            checkedItem,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                        int position,
                                        long id) {
                                    F targetFragment = getTargetFragment();
                                    if (targetFragment != null
                                            && targetFragment instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider) {
                                        ((SimpleAlertDialog.SingleChoiceArrayItemProvider) targetFragment)
                                                .onSingleChoiceArrayItemClick(
                                                        dialog, requestCode, position);
                                    }
                                }
                            });
                }
                if (getActivity() != null
                        && getActivity() instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider) {
                    dialog.setSingleChoiceItems(
                            ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getActivity())
                                    .onCreateSingleChoiceArray(dialog, requestCode),
                            checkedItem,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                        int position,
                                        long id) {
                                    if ((getActivity() != null && getActivity() instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider)) {
                                        ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getActivity())
                                                .onSingleChoiceArrayItemClick(
                                                        dialog, requestCode, position);
                                    }
                                }
                            });
                }
            }
            CharSequence positiveButton = null;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_POSITIVE_BUTTON)) {
                positiveButton = args.getCharSequence(SimpleAlertDialog.ARG_POSITIVE_BUTTON);
            } else if (args != null
                    && args.containsKey(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID)) {
                positiveButton = activity.getString(args
                        .getInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID));
            }
            if (positiveButton != null
                    || (!hasListProvider(args) && !hasSingleChoiceArrayItemProvider(args))) {
                if (positiveButton == null) {
                    positiveButton = activity.getResources().getText(android.R.string.ok);
                }
                dialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        F targetFragment = getTargetFragment();
                        if (targetFragment != null
                                && targetFragment instanceof SimpleAlertDialog.OnClickListener) {
                            ((SimpleAlertDialog.OnClickListener) targetFragment)
                                    .onDialogPositiveButtonClicked((SimpleAlertDialog) dialog,
                                            requestCode,
                                            ((SimpleAlertDialog) dialog).getView());
                        }
                        if (getActivity() != null
                                && getActivity() instanceof SimpleAlertDialog.OnClickListener) {
                            ((SimpleAlertDialog.OnClickListener) getActivity())
                                    .onDialogPositiveButtonClicked((SimpleAlertDialog) dialog,
                                            requestCode,
                                            ((SimpleAlertDialog) dialog).getView());
                        }
                    }
                });
            }
            CharSequence negativeButton = null;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_NEGATIVE_BUTTON)) {
                negativeButton = args.getCharSequence(SimpleAlertDialog.ARG_NEGATIVE_BUTTON);
            } else if (args != null
                    && args.containsKey(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID)) {
                negativeButton = activity.getString(args
                        .getInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID));
            }
            if (negativeButton != null) {
                dialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        F targetFragment = getTargetFragment();
                        if (targetFragment != null
                                && targetFragment instanceof SimpleAlertDialog.OnClickListener) {
                            ((SimpleAlertDialog.OnClickListener) targetFragment)
                                    .onDialogNegativeButtonClicked((SimpleAlertDialog) dialog,
                                            requestCode,
                                            ((SimpleAlertDialog) dialog).getView());
                        }
                        if (getActivity() != null
                                && getActivity() instanceof SimpleAlertDialog.OnClickListener) {
                            ((SimpleAlertDialog.OnClickListener) getActivity())
                                    .onDialogNegativeButtonClicked((SimpleAlertDialog) dialog,
                                            requestCode,
                                            ((SimpleAlertDialog) dialog).getView());
                        }
                    }
                });
            }

            boolean cancelable = true;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_CANCELABLE)) {
                cancelable = args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE);
            }
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            return dialog;
        }

        public boolean hasListProvider(Bundle args) {
            boolean useAdapter = false;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_USE_ADAPTER)) {
                useAdapter = args.getBoolean(SimpleAlertDialog.ARG_USE_ADAPTER);
            }
            F targetFragment = getTargetFragment();
            A activity = getActivity();
            return useAdapter
                    && ((targetFragment != null && targetFragment instanceof SimpleAlertDialog.ListProvider)
                    || (activity != null && activity instanceof SimpleAlertDialog.ListProvider));
        }

        public boolean hasSingleChoiceArrayItemProvider(Bundle args) {
            int singleChoiceCheckedItem = -1;
            if (args != null && args.containsKey(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM)) {
                singleChoiceCheckedItem = args
                        .getInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
            }
            F targetFragment = getTargetFragment();
            A activity = getActivity();
            return singleChoiceCheckedItem >= 0
                    && ((targetFragment != null && targetFragment instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider)
                    || (activity != null && activity instanceof SimpleAlertDialog.SingleChoiceArrayItemProvider));
        }
    }

    public static abstract class Builder<T, F> {

        private CharSequence mTitle;
        private int mTitleResId;
        private int mIcon;
        private CharSequence mMessage;
        private int mMessageResId;
        private CharSequence mPositiveButton;
        private int mPositiveButtonResId;
        private CharSequence mNegativeButton;
        private int mNegativeButtonResId;
        private int mRequestCode;
        private boolean mCancelable = true;
        private int mSingleChoiceCheckedItem = -1;
        private boolean mUseView;
        private boolean mUseAdapter;

        public Builder<T, F> setTitle(final CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder<T, F> setTitle(final int resId) {
            mTitleResId = resId;
            return this;
        }

        public Builder<T, F> setIcon(final int resId) {
            mIcon = resId;
            return this;
        }

        public Builder<T, F> setMessage(final CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder<T, F> setMessage(final int resId) {
            mMessageResId = resId;
            return this;
        }

        public Builder<T, F> setPositiveButton(final CharSequence positiveButton) {
            mPositiveButton = positiveButton;
            return this;
        }

        public Builder<T, F> setPositiveButton(final int resId) {
            mPositiveButtonResId = resId;
            return this;
        }

        public Builder<T, F> setNegativeButton(final CharSequence negativeButton) {
            mNegativeButton = negativeButton;
            return this;
        }

        public Builder<T, F> setNegativeButton(final int resId) {
            mNegativeButtonResId = resId;
            return this;
        }

        public Builder<T, F> setRequestCode(final int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        public Builder<T, F> setCancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder<T, F> setSingleChoiceCheckedItem(final int checkedItem) {
            mSingleChoiceCheckedItem = checkedItem;
            return this;
        }

        public Builder<T, F> setUseView(final boolean useView) {
            mUseView = useView;
            return this;
        }

        public Builder<T, F> setUseAdapter(final boolean useAdapter) {
            mUseAdapter = useAdapter;
            return this;
        }

        public Bundle createArguments() {
            Bundle args = new Bundle();
            if (mTitle != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_TITLE, mTitle);
            } else if (mTitleResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_TITLE_RES_ID, mTitleResId);
            }
            if (mIcon > 0) {
                args.putInt(SimpleAlertDialog.ARG_ICON, mIcon);
            }
            if (mMessage != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_MESSAGE, mMessage);
            } else if (mMessageResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_MESSAGE_RES_ID, mMessageResId);
            }
            if (mPositiveButton != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_POSITIVE_BUTTON, mPositiveButton);
            } else if (mPositiveButtonResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID, mPositiveButtonResId);
            }
            if (mNegativeButton != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_NEGATIVE_BUTTON, mNegativeButton);
            } else if (mNegativeButtonResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID, mNegativeButtonResId);
            }
            args.putBoolean(SimpleAlertDialog.ARG_CANCELABLE, mCancelable);
            if (mSingleChoiceCheckedItem >= 0) {
                args.putInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM,
                        mSingleChoiceCheckedItem);
            }
            args.putBoolean(SimpleAlertDialog.ARG_USE_VIEW, mUseView);
            args.putBoolean(SimpleAlertDialog.ARG_USE_ADAPTER, mUseAdapter);
            args.putInt(SimpleAlertDialog.ARG_REQUEST_CODE, mRequestCode);
            return args;
        }

        public Dialog createDialog(final Activity activity) {
            return new Helper<T, F, Context>() {
                @Override
                public Context getActivity() {
                    return activity;
                }

                @Override
                public F getTargetFragment() {
                    return null;
                }
            }.createDialog(createArguments(), null, activity);
        }

        public abstract Builder<T, F> setTargetFragment(F fragment);

        public abstract T create();
    }
}
