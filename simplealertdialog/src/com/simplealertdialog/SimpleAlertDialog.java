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
 * Dialog managed by {@link com.simplealertdialog.SimpleAlertDialogFragment}
 * or {@link com.simplealertdialog.SimpleAlertDialogSupportFragment}.
 * When you use this dialog, create {@code DialogFragment} by
 * {@link com.simplealertdialog.SimpleAlertDialogFragment.Builder} or
 * {@link com.simplealertdialog.SimpleAlertDialogSupportFragment.Builder},
 * and manage it with {@code FragmentManager}.
 *
 * @author Soichiro Kashima
 */
public class SimpleAlertDialog extends Dialog {

    /**
     * Listener for click events of dialog buttons.
     * There is no {@code setListener()} method to make these callbacks to be called.
     * If the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface OnClickListener {
        /**
         * Called when the positive button is clicked.
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param view View of the dialog
         */
        void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                final View view);

        /**
         * Called when the negative button is clicked.
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param view View of the dialog
         */
        void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                final View view);
    }

    /**
     * Listener for cancel events of dialog.
     * There is no {@code setListener()} method to make these callbacks to be called.
     * If the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface OnCancelListener {
        /**
         * Called when the dialog is canceled.
         * Note that all of the cancel events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this cancel event
         * @param requestCode Request code set to distinguish dialogs
         * @param view View of the dialog
         */
        void onDialogCancel(final SimpleAlertDialog dialog, final int requestCode, final View view);
    }

    /**
     * Providing the custom view of the dialog.
     * Use {@code setUseView()} to indicate that the dialog has a custom view.
     * If the {@code setUseView()} is set to {@code true} and the caller
     * {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface ViewProvider {
        /**
         * Called when the dialog is created to show custom view.
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this view creation event
         * @param requestCode Request code set to distinguish dialogs
         */
        View onCreateView(final SimpleAlertDialog dialog, final int requestCode);
    }

    /**
     * Providing the custom {@code ListAdapter} of the dialog.
     * Use {@code setUseAdapter()} to indicate that the dialog has a custom adapter.
     * If the {@code setUseAdapter()} is set to {@code true} and the caller
     * {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface ListProvider {
        /**
         * Called when the dialog is created to show custom list.
         * Note that all of the list creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this list creation event
         * @param requestCode Request code set to distinguish dialogs
         * @return Custom list adapter
         */
        ListAdapter onCreateList(final SimpleAlertDialog dialog, final int requestCode);

        /**
         * Called when the item in the custom adapter is clicked.
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param position Position of the list items (from 0)
         */
        void onListItemClick(final SimpleAlertDialog dialog, final int requestCode,
                final int position);
    }

    /**
     * Providing the custom {@code SingleChoice} list of the dialog.
     * Use {@code setSingleChoiceCheckedItem()} to indicate that the dialog
     * has a single choice item list.
     * If the {@code setSingleChoiceCheckedItem()} is set to {@code true}
     * and the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface SingleChoiceArrayItemProvider {
        /**
         * Called when the single choice items are created.
         * Note that all of the creation events of the single choice items from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own single choice items creation event
         * @param requestCode Request code set to distinguish dialogs
         * @return Char sequences of the single choice items
         */
        CharSequence[] onCreateSingleChoiceArray(final SimpleAlertDialog dialog,
                final int requestCode);

        /**
         * Called when the item in the custom single choice items is clicked.
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param position Position of the list items (from 0)
         */
        void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, final int requestCode,
                final int position);
    }

    static final String ARG_THEME_RES_ID = "argThemeResId";
    static final String ARG_TITLE = "argTitle";
    static final String ARG_TITLE_RES_ID = "argTitleResId";
    static final String ARG_ICON = "argIcon";
    static final String ARG_MESSAGE = "argMessage";
    static final String ARG_MESSAGE_RES_ID = "argMessageResId";
    static final String ARG_POSITIVE_BUTTON = "argPositiveButton";
    static final String ARG_POSITIVE_BUTTON_RES_ID = "argPositiveButtonResId";
    static final String ARG_NEGATIVE_BUTTON = "argNegativeButton";
    static final String ARG_NEGATIVE_BUTTON_RES_ID = "argNegativeButtonResId";
    static final String ARG_REQUEST_CODE = "argRequestCode";
    static final String ARG_CANCELABLE = "argCancelable";
    static final String ARG_CANCELED_ON_TOUCH_OUTSIDE = "argCanceledOnTouchOutside";
    static final String ARG_SINGLE_CHOICE_CHECKED_ITEM = "argSingleChoiceCheckedItem";
    static final String ARG_USE_VIEW = "argUseView";
    static final String ARG_USE_ADAPTER = "argUseAdapter";

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
    private Drawable mListSelectorBackground;
    private Drawable mTitleSeparatorBackground;
    private int mTitleSeparatorHeight;
    private Drawable mButtonTopDividerBackground;
    private Drawable mButtonVerticalDividerBackground;
    private Drawable mBackgroundFull;
    private Drawable mBackgroundTop;
    private Drawable mBackgroundMiddle;
    private Drawable mBackgroundBottom;

    /**
     * Creates the new dialog.
     * Users should not directly call this.
     *
     * @param context Dialog owner context
     * @param themeResId Dialog theme resource ID
     */
    public SimpleAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        obtainStyles();
    }

    /**
     * Creates the new dialog.
     * Users should not directly call this.
     *
     * @param context Dialog owner context
     */
    public SimpleAlertDialog(Context context) {
        super(context);
        obtainStyles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sad__dialog_simple);
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
                        R.dimen.sad__dialog_title_separator_height);
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
                    setBackground(c, mListSelectorBackground);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        Resources res = getContext().getResources();
                        c.setPadding(
                                res.getDimensionPixelSize(R.dimen.sad__simple_list_item_padding_left),
                                0,
                                res.getDimensionPixelSize(R.dimen.sad__simple_list_item_padding_right),
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
                R.styleable.SimpleAlertDialogStyle_sadListChoiceIndicatorSingle, 0);
        mTitleTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_sadTitleTextStyle, 0);
        mMessageTextStyle = a.getResourceId(R.styleable.SimpleAlertDialogStyle_sadMessageTextStyle,
                0);
        mButtonTextStyle = a
                .getResourceId(R.styleable.SimpleAlertDialogStyle_sadButtonTextStyle, 0);
        mListItemTextStyle = a.getResourceId(
                R.styleable.SimpleAlertDialogStyle_sadListItemTextStyle,
                0);
        mListSelectorBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_sadListSelectorBackground);
        mTitleSeparatorBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_sadTitleSeparatorBackground);
        mTitleSeparatorHeight = a.getLayoutDimension(
                R.styleable.SimpleAlertDialogStyle_sadTitleSeparatorHeight,
                getContext().getResources().getDimensionPixelSize(
                        R.dimen.sad__dialog_title_separator_height));
        mButtonTopDividerBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_sadButtonTopDividerBackground);
        mButtonVerticalDividerBackground = a
                .getDrawable(R.styleable.SimpleAlertDialogStyle_sadButtonVerticalDividerBackground);
        mBackgroundFull = a.getDrawable(R.styleable.SimpleAlertDialogStyle_sadBackgroundFull);
        mBackgroundTop = a.getDrawable(R.styleable.SimpleAlertDialogStyle_sadBackgroundTop);
        mBackgroundMiddle = a.getDrawable(R.styleable.SimpleAlertDialogStyle_sadBackgroundMiddle);
        mBackgroundBottom = a.getDrawable(R.styleable.SimpleAlertDialogStyle_sadBackgroundBottom);

        a.recycle();
    }

    private void setBackground(final int resId, final Drawable d) {
        if (resId == 0 || d == null) {
            return;
        }
        View view = findViewById(resId);
        setBackground(view, d);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private void setBackground(final View view, final Drawable d) {
        if (view == null || d == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d.getConstantState().newDrawable());
        } else {
            view.setBackgroundDrawable(d.getConstantState().newDrawable());
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

        public Dialog createDialog(Bundle args) {
            final SimpleAlertDialog dialog = newInstance(args);
            setTitle(args, dialog);
            setIcon(args, dialog);
            setMessage(args, dialog);
            final int requestCode = getRequestCode(args);
            setView(args, dialog, requestCode);
            setAdapter(args, dialog, requestCode);
            setSingleChoiceItems(args, dialog, requestCode);
            setPositiveButton(args, dialog, requestCode);
            setNegativeButton(args, dialog, requestCode);
            setCancelable(args, dialog);
            return dialog;
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
            if (has(args, SimpleAlertDialog.ARG_USE_VIEW)) {
                boolean useView = args.getBoolean(SimpleAlertDialog.ARG_USE_VIEW);
                if (useView) {
                    if (fragmentImplements(SimpleAlertDialog.ViewProvider.class)) {
                        dialog.setView(((SimpleAlertDialog.ViewProvider) getTargetFragment())
                                .onCreateView(dialog, requestCode));
                    }
                    if (activityImplements(SimpleAlertDialog.ViewProvider.class)) {
                        dialog.setView(((SimpleAlertDialog.ViewProvider) getActivity())
                                .onCreateView(dialog, requestCode));
                    }
                }
            }
        }

        private void setAdapter(Bundle args, final SimpleAlertDialog dialog, final int requestCode) {
            if (has(args, SimpleAlertDialog.ARG_USE_ADAPTER)) {
                boolean useAdapter = args.getBoolean(SimpleAlertDialog.ARG_USE_ADAPTER);
                if (useAdapter) {
                    if (fragmentImplements(SimpleAlertDialog.ListProvider.class)) {
                        dialog.setAdapter(((SimpleAlertDialog.ListProvider) getTargetFragment())
                                        .onCreateList(dialog, requestCode),
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position,
                                                            long id) {
                                        F targetFragment = getTargetFragment();
                                        if (fragmentImplements(SimpleAlertDialog.ListProvider.class)) {
                                            ((SimpleAlertDialog.ListProvider) targetFragment)
                                                    .onListItemClick(dialog, requestCode,
                                                            position);
                                        }
                                    }
                                });
                    }
                    if (activityImplements(SimpleAlertDialog.ListProvider.class)) {
                        dialog.setAdapter(((SimpleAlertDialog.ListProvider) getActivity())
                                        .onCreateList(dialog, requestCode),
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position,
                                                            long id) {
                                        if (activityImplements(SimpleAlertDialog.ListProvider.class)) {
                                            ((SimpleAlertDialog.ListProvider) getActivity())
                                                    .onListItemClick(dialog, requestCode, position);
                                        }
                                    }
                                });
                    }
                }
            }
        }

        private void setSingleChoiceItems(Bundle args, final SimpleAlertDialog dialog, final int requestCode) {
            if (has(args, SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM)) {
                int checkedItem = args.getInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM);
                if (fragmentImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
                    dialog.setSingleChoiceItems(
                            ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getTargetFragment())
                                    .onCreateSingleChoiceArray(dialog, requestCode),
                            checkedItem,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position,
                                                        long id) {
                                    F targetFragment = getTargetFragment();
                                    if (fragmentImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
                                        ((SimpleAlertDialog.SingleChoiceArrayItemProvider) targetFragment)
                                                .onSingleChoiceArrayItemClick(
                                                        dialog, requestCode, position);
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
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position,
                                                        long id) {
                                    if (activityImplements(SimpleAlertDialog.SingleChoiceArrayItemProvider.class)) {
                                        ((SimpleAlertDialog.SingleChoiceArrayItemProvider) getActivity())
                                                .onSingleChoiceArrayItemClick(
                                                        dialog, requestCode, position);
                                    }
                                }
                            });
                }
            }
        }

        private void setPositiveButton(Bundle args, SimpleAlertDialog dialog, final int requestCode) {
            CharSequence positiveButton = null;
            if (has(args, SimpleAlertDialog.ARG_POSITIVE_BUTTON)) {
                positiveButton = args.getCharSequence(SimpleAlertDialog.ARG_POSITIVE_BUTTON);
            } else if (has(args, SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID)) {
                positiveButton = getActivity().getString(args
                        .getInt(SimpleAlertDialog.ARG_POSITIVE_BUTTON_RES_ID));
            }
            if (positiveButton != null) {
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
        }

        private void setNegativeButton(Bundle args, SimpleAlertDialog dialog, final int requestCode) {
            CharSequence negativeButton = null;
            if (has(args, SimpleAlertDialog.ARG_NEGATIVE_BUTTON)) {
                negativeButton = args.getCharSequence(SimpleAlertDialog.ARG_NEGATIVE_BUTTON);
            } else if (has(args, SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID)) {
                negativeButton = getActivity().getString(args
                        .getInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID));
            }
            if (negativeButton != null) {
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
        }

        private void setCancelable(Bundle args, SimpleAlertDialog dialog) {
            boolean cancelable = true;
            if (has(args, SimpleAlertDialog.ARG_CANCELABLE)) {
                cancelable = args.getBoolean(SimpleAlertDialog.ARG_CANCELABLE);
            }
            boolean canceledOnTouchOutside = cancelable;
            if (cancelable && has(args, SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE)) {
                canceledOnTouchOutside = args
                        .getBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE);
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

    /**
     * Dialog builder like {@link android.app.AlertDialog.Builder}.
     * To show your custom dialog, construct the dialog style with this builder,
     * and show with {@code FragmentManager}.
     *
     * @param <T> Type of the {@code SimpleAlertDialog} fragment
     * @param <F> Type of the basic {@code Fragment}
     */
    public static abstract class Builder<T, F> {

        private int mThemeResId;
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
        private boolean mCanceledOnTouchOutside = true;
        private int mSingleChoiceCheckedItem = -1;
        private boolean mUseView;
        private boolean mUseAdapter;

        /**
         * Sets the theme of the dialog.
         *
         * @param resId Theme(style) resource ID
         * @return Builder itself
         */
        public Builder<T, F> setTheme(final int resId) {
            mThemeResId = resId;
            return this;
        }

        /**
         * Sets the title of the dialog.
         *
         * @param title Title char sequence or string
         * @return Builder itself
         */
        public Builder<T, F> setTitle(final CharSequence title) {
            mTitle = title;
            return this;
        }

        /**
         * Sets the title of the dialog.
         *
         * @param resId Title string resource ID
         * @return Builder itself
         */
        public Builder<T, F> setTitle(final int resId) {
            mTitleResId = resId;
            return this;
        }

        /**
         * Sets the icon for the title of the dialog.
         *
         * @param resId Icon drawable resource ID
         * @return Builder itself
         */
        public Builder<T, F> setIcon(final int resId) {
            mIcon = resId;
            return this;
        }

        /**
         * Sets the message of the dialog.
         *
         * @param message Message char sequence or string
         * @return Builder itself
         */
        public Builder<T, F> setMessage(final CharSequence message) {
            mMessage = message;
            return this;
        }

        /**
         * Sets the message of the dialog.
         *
         * @param resId Message string resource ID
         * @return Builder itself
         */
        public Builder<T, F> setMessage(final int resId) {
            mMessageResId = resId;
            return this;
        }

        /**
         * Sets the positive button's char sequence or string.
         * This also enables callback of the click event of the positive button.
         *
         * @param positiveButton Char sequence or string of the positive button
         * @return Builder itself
         */
        public Builder<T, F> setPositiveButton(final CharSequence positiveButton) {
            mPositiveButton = positiveButton;
            return this;
        }

        /**
         * Sets the positive button's char sequence or string.
         * This also enables callback of the click event of the positive button.
         *
         * @param resId String resource ID of the positive button
         * @return Builder itself
         */
        public Builder<T, F> setPositiveButton(final int resId) {
            mPositiveButtonResId = resId;
            return this;
        }

        /**
         * Sets the negative button's char sequence or string.
         * This also enables callback of the click event of the negative button.
         *
         * @param negativeButton Char sequence or string of the negative button
         * @return Builder itself
         */
        public Builder<T, F> setNegativeButton(final CharSequence negativeButton) {
            mNegativeButton = negativeButton;
            return this;
        }

        /**
         * Sets the negative button's char sequence or string.
         * This also enables callback of the click event of the negative button.
         *
         * @param resId String resource ID of the negative button
         * @return Builder itself
         */
        public Builder<T, F> setNegativeButton(final int resId) {
            mNegativeButtonResId = resId;
            return this;
        }

        /**
         * Sets the request code of the callbacks.
         * This code will be passed to the callbacks to distinguish
         * other dialogs.
         *
         * @param requestCode Request code
         * @return Builder itself
         */
        public Builder<T, F> setRequestCode(final int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        /**
         * Sets the dialog to be cancelable or not.
         * Default value is {@code true}.
         * If you set this to {@code false}, pressing back button
         * and touching outside of the dialog don't cancel the dialog.
         *
         * @param cancelable {@code true} if the dialog is cancelable
         * @return Builder itself
         */
        public Builder<T, F> setCancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the dialog to be cancelable on touching outside of the dialog.
         * Default value is {@code true}.
         *
         * @param canceledOnTouchOutside {@code true} if the dialog should be canceled on touch outside
         * @return Builder itself
         */
        public Builder<T, F> setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
            mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        /**
         * Sets the default checked item of the single choice items.
         * This also enables callback of the
         * {@link com.simplealertdialog.SimpleAlertDialog.SingleChoiceArrayItemProvider}.
         *
         * @param checkedItem Position of the checked item
         * @return Builder itself
         */
        public Builder<T, F> setSingleChoiceCheckedItem(final int checkedItem) {
            mSingleChoiceCheckedItem = checkedItem;
            return this;
        }

        /**
         * Sets the dialog to use custom view provided by
         * {@link com.simplealertdialog.SimpleAlertDialog.ViewProvider}.
         *
         * @param useView {@code true} if you provide a custom view for this dialog
         * @return Builder itself
         */
        public Builder<T, F> setUseView(final boolean useView) {
            mUseView = useView;
            return this;
        }

        /**
         * Sets the dialog to use custom {@code ListProvider} provided by
         * {@link com.simplealertdialog.SimpleAlertDialog.ListProvider}.
         *
         * @param useAdapter {@code true} if you provide a custom adapter for this dialog
         * @return Builder itself
         */
        public Builder<T, F> setUseAdapter(final boolean useAdapter) {
            mUseAdapter = useAdapter;
            return this;
        }

        /**
         * Creates the arguments of the {@code SimpleAlertDialog} as a {@code Bundle}.
         * In most cases, you don't have to call this method directly.
         *
         * @return Created arguments bundle
         */
        public Bundle createArguments() {
            Bundle args = new Bundle();
            if (mThemeResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_THEME_RES_ID, mThemeResId);
            }
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
            args.putBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE,
                    mCanceledOnTouchOutside);
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
            }.createDialog(createArguments());
        }

        /**
         * Sets the target fragment of this {@code DialogFragment}.
         * Use this method to tell the dialog that {@code fragment} of the argument
         * is the callback instance for some of the events of dialogs.
         *
         * @param fragment Target fragment instance
         * @return Builder itself
         */
        public abstract Builder<T, F> setTargetFragment(F fragment);

        /**
         * Creates the new dialog instance with styles set by this builder.
         *
         * @return dialog instance
         */
        public abstract T create();
    }
}
