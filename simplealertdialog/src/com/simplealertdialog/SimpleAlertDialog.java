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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
 * <p>Dialog managed by {@link com.simplealertdialog.SimpleAlertDialogFragment}
 * or {@link com.simplealertdialog.SimpleAlertDialogSupportFragment}.<br/>
 * When you use this dialog, create {@code DialogFragment} by
 * {@link com.simplealertdialog.SimpleAlertDialogFragment.Builder} or
 * {@link com.simplealertdialog.SimpleAlertDialogSupportFragment.Builder},
 * and manage it with {@code FragmentManager}.<br/>
 * Do not instantiate or use this class directly.</p>
 * <p>Basic usage for {@code android.app.Fragment}:</p>
 * <pre>    new SimpleAlertDialogFragment.Builder()
 *         .setMessage("Hello world!")
 *         .setPositiveButton(android.R.string.ok)
 *         .create().show(getFragmentManager(), "dialog");</pre>
 * <p>Basic usage for {@code android.support.v4.app.Fragment}:</p>
 * <pre>    new SimpleAlertDialogSupportFragment.Builder()
 *         .setMessage("Hello world!")
 *         .setPositiveButton(android.R.string.ok)
 *         .create().show(getSupportFragmentManager(), "dialog");</pre>
 *
 * @author Soichiro Kashima
 */
public class SimpleAlertDialog extends Dialog {

    /**
     * Listener for click events of dialog buttons.<br/>
     * There is no {@code setListener()} method to make these callbacks to be called.<br/>
     * If the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface OnClickListener {
        /**
         * Called when the positive button is clicked.<br/>
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param view        View of the dialog
         */
        void onDialogPositiveButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                                           final View view);

        /**
         * Called when the negative button is clicked.<br/>
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param view        View of the dialog
         */
        void onDialogNegativeButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                                           final View view);
    }

    /**
     * Listener for click events of dialog buttons.<br/>
     * There is no {@code setListener()} method to make these callbacks to be called.<br/>
     * If the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface OnNeutralButtonClickListener {
        /**
         * Called when the neutral button is clicked.<br/>
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param view        View of the dialog
         */
        void onDialogNeutralButtonClicked(final SimpleAlertDialog dialog, final int requestCode,
                                           final View view);
    }

    /**
     * Listener for cancel events of dialog.<br/>
     * There is no {@code setListener()} method to make these callbacks to be called.<br/>
     * If the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface OnCancelListener {
        /**
         * Called when the dialog is canceled.<br/>
         * Note that all of the cancel events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this cancel event
         * @param requestCode Request code set to distinguish dialogs
         * @param view        View of the dialog
         */
        void onDialogCancel(final SimpleAlertDialog dialog, final int requestCode, final View view);
    }

    /**
     * Listener for item click events of dialog.<br/>
     * There is no {@code setListener()} method to make these callbacks to be called.<br/>
     * If the caller {@code Activity} or {@code Fragment} implements this interface
     * and {@code setItems()} method of builder is called,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.<br/>
     */
    public static interface OnItemClickListener {
        /**
         * Called when the negative button is clicked.<br/>
         * Note that all of the click events from the {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param which       Selected item index that begins from 0
         */
        void onItemClick(final SimpleAlertDialog dialog,
                         final int requestCode, final int which);
    }

    /**
     * Providing the custom view of the dialog.<br/>
     * Use {@code setUseView()} to indicate that the dialog has a custom view.<br/>
     * If the {@code setUseView()} is set to {@code true} and the caller
     * {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface ViewProvider {
        /**
         * Called when the dialog is created to show custom view.<br/>
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this view creation event
         * @param requestCode Request code set to distinguish dialogs
         */
        View onCreateView(final SimpleAlertDialog dialog, final int requestCode);
    }

    /**
     * Providing the custom {@code ListAdapter} of the dialog.<br/>
     * Use {@code setUseAdapter()} to indicate that the dialog has a custom adapter.<br/>
     * If the {@code setUseAdapter()} is set to {@code true} and the caller
     * {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface ListProvider {
        /**
         * Called when the dialog is created to show custom list.<br/>
         * Note that all of the list creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this list creation event
         * @param requestCode Request code set to distinguish dialogs
         * @return Custom list adapter
         */
        ListAdapter onCreateList(final SimpleAlertDialog dialog, final int requestCode);

        /**
         * Called when the item in the custom adapter is clicked.<br/>
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param position    Position of the list items (from 0)
         */
        void onListItemClick(final SimpleAlertDialog dialog, final int requestCode,
                             final int position);
    }

    /**
     * Providing the custom {@code SingleChoice} list of the dialog.<br/>
     * Use {@code setSingleChoiceCheckedItem()} to indicate that the dialog
     * has a single choice item list.<br/>
     * If the {@code setSingleChoiceCheckedItem()} is set to {@code true}
     * and the caller {@code Activity} or {@code Fragment} implements this interface,
     * {@linkplain com.simplealertdialog.SimpleAlertDialog} will
     * automatically call back.
     */
    public static interface SingleChoiceArrayItemProvider {
        /**
         * Called when the single choice items are created.<br/>
         * Note that all of the creation events of the single choice items from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own single choice items creation event
         * @param requestCode Request code set to distinguish dialogs
         * @return Char sequences of the single choice items
         */
        CharSequence[] onCreateSingleChoiceArray(final SimpleAlertDialog dialog,
                                                 final int requestCode);

        /**
         * Called when the item in the custom single choice items is clicked.<br/>
         * Note that all of the view creation events from the
         * {@linkplain com.simplealertdialog.SimpleAlertDialog}
         * will be sent to this method, so you should set {@code requestCode}
         * to distinguish each dialogs.
         *
         * @param dialog      Dialog that own this click event
         * @param requestCode Request code set to distinguish dialogs
         * @param position    Position of the list items (from 0)
         */
        void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, final int requestCode,
                                          final int position);
    }

    private class IconListItem {
        public int iconResId;
        public CharSequence text;

        public IconListItem(final int iconResId, CharSequence text) {
            this.iconResId = iconResId;
            this.text = text;
        }
    }

    static final String ARG_THEME_RES_ID = "argThemeResId";
    static final String ARG_TITLE = "argTitle";
    static final String ARG_TITLE_RES_ID = "argTitleResId";
    static final String ARG_ICON = "argIcon";
    static final String ARG_MESSAGE = "argMessage";
    static final String ARG_MESSAGE_RES_ID = "argMessageResId";
    static final String ARG_POSITIVE_BUTTON = "argPositiveButton";
    static final String ARG_POSITIVE_BUTTON_RES_ID = "argPositiveButtonResId";
    static final String ARG_NEUTRAL_BUTTON = "argNeutralButton";
    static final String ARG_NEUTRAL_BUTTON_RES_ID = "argNeutralButtonResId";
    static final String ARG_NEGATIVE_BUTTON = "argNegativeButton";
    static final String ARG_NEGATIVE_BUTTON_RES_ID = "argNegativeButtonResId";
    static final String ARG_ITEMS = "argItems";
    static final String ARG_ITEMS_RES_ID = "argItemsResId";
    static final String ARG_ICONS = "argIcons";
    static final String ARG_REQUEST_CODE = "argRequestCode";
    static final String ARG_CANCELABLE = "argCancelable";
    static final String ARG_CANCELED_ON_TOUCH_OUTSIDE = "argCanceledOnTouchOutside";
    static final String ARG_SINGLE_CHOICE_CHECKED_ITEM = "argSingleChoiceCheckedItem";
    static final String ARG_USE_VIEW = "argUseView";
    static final String ARG_EDIT_TEXT_INITIAL_TEXT = "argEditTextInitialText";
    static final String ARG_EDIT_TEXT_INPUT_TYPE = "argEditTextInputType";
    static final String ARG_USE_ADAPTER = "argUseAdapter";

    private CharSequence mMessage;
    private CharSequence mTitle;
    private int mIcon;
    private CharSequence mPositiveButtonText;
    private CharSequence mNeutralButtonText;
    private CharSequence mNegativeButtonText;
    private DialogInterface.OnClickListener mPositiveButtonListener;
    private DialogInterface.OnClickListener mNeutralButtonListener;
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
     * Creates the new dialog.<br/>
     * Users should not directly call this.
     *
     * @param context    Dialog owner context
     * @param themeResId Dialog theme resource ID
     */
    public SimpleAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        obtainStyles();
    }

    /**
     * Creates the new dialog.<br/>
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

        // Neutral Button
        boolean hasNeutralButton = false;
        if (mNeutralButtonText != null) {
            hasNeutralButton = true;
            ((TextView) findViewById(R.id.button_neutral_label)).setText(mNeutralButtonText);
            if (mButtonTextStyle != 0) {
                ((TextView) findViewById(R.id.button_neutral_label)).setTextAppearance(
                        getContext(), mButtonTextStyle);
            }
        }
        if (mNeutralButtonListener != null) {
            hasNeutralButton = true;
            findViewById(R.id.button_neutral).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mNeutralButtonListener != null) {
                        mNeutralButtonListener.onClick(SimpleAlertDialog.this, 0);
                    }
                    dismiss();
                }
            });
        }
        if (!hasNeutralButton) {
            findViewById(R.id.button_neutral).setVisibility(View.GONE);
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
        if (hasNeutralButton) {
            setBackground(R.id.button_divider_neutral, mButtonVerticalDividerBackground);
        } else {
            findViewById(R.id.button_divider_neutral).setVisibility(View.GONE);
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

    @Override
    public void setTitle(final CharSequence title) {
        if (title == null) {
            return;
        }
        mTitle = title;
    }

    @Override
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

    public void setItems(final CharSequence[] items,
                         final AdapterView.OnItemClickListener listener) {
        mAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view != null) {
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    if (mListItemTextStyle != 0) {
                        tv.setTextAppearance(getContext(), mListItemTextStyle);
                    }
                    tv.setText(items[position]);
                }
                return view;
            }
        };
        mListItemListener = listener;
    }

    public void setItems(final CharSequence[] items,
                         final int[] iconResIds,
                         final AdapterView.OnItemClickListener listener) {
        if (iconResIds == null || items == null) {
            return;
        }
        final IconListItem[] iconListItems = new IconListItem[Math.min(iconResIds.length, items.length)];
        for (int i = 0; i < iconResIds.length && i < items.length; i++) {
            iconListItems[i] = new IconListItem(iconResIds[i], items[i]);
        }
        mAdapter = new ArrayAdapter<IconListItem>(getContext(), android.R.layout.simple_list_item_1, iconListItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view != null) {
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    if (mListItemTextStyle != 0) {
                        tv.setTextAppearance(getContext(), mListItemTextStyle);
                    }
                    tv.setText(iconListItems[position].text);
                    tv.setCompoundDrawablesWithIntrinsicBounds(iconListItems[position].iconResId, 0, 0, 0);
                    int padding = (int) (8 * getContext().getResources().getDisplayMetrics().density);
                    tv.setCompoundDrawablePadding(padding);
                }
                return view;
            }
        };
        mListItemListener = listener;
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

    public void setNeutralButton(final CharSequence text,
                                  final DialogInterface.OnClickListener listener) {
        if (text == null) {
            return;
        }
        mNeutralButtonText = text;
        mNeutralButtonListener = listener;
    }

    public void setNeutralButton(final int resId,
                                  final DialogInterface.OnClickListener listener) {
        setNeutralButton(getContext().getText(resId), listener);
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

    /**
     * Dialog builder like {@link android.app.AlertDialog.Builder}.<br/>
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
        private CharSequence mNeutralButton;
        private int mNeutralButtonResId;
        private CharSequence mNegativeButton;
        private int mNegativeButtonResId;
        private int mItemsResId;
        private CharSequence[] mItems;
        private int[] mIcons;
        private int mRequestCode;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private int mSingleChoiceCheckedItem = -1;
        private CharSequence mEditTextInitialText;
        private int mEditTextInputType;
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
         * Sets the positive button's char sequence or string.<br/>
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
         * Sets the positive button's char sequence or string.<br/>
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
         * Sets the neutral button's char sequence or string.<br/>
         * This also enables callback of the click event of the neutral button.
         *
         * @param neutralButton Char sequence or string of the neutral button
         * @return Builder itself
         */
        public Builder<T, F> setNeutralButton(final CharSequence neutralButton) {
            mNeutralButton = neutralButton;
            return this;
        }

        /**
         * Sets the neutral button's char sequence or string.<br/>
         * This also enables callback of the click event of the neutral button.
         *
         * @param resId String resource ID of the neutral button
         * @return Builder itself
         */
        public Builder<T, F> setNeutralButton(final int resId) {
            mNeutralButtonResId = resId;
            return this;
        }

        /**
         * Sets the negative button's char sequence or string.<br/>
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
         * Sets the negative button's char sequence or string.<br/>
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
         * Sets the char sequence array items.<br/>
         * This also enables callback of the click event of the list items.
         *
         * @param items Char sequence array for items
         * @return Builder itself
         */
        @TargetApi(Build.VERSION_CODES.ECLAIR)
        public Builder<T, F> setItems(final CharSequence[] items) {
            mItems = items;
            return this;
        }

        /**
         * Sets the char sequence array items.<br/>
         * This also enables callback of the click event of the list items.
         *
         * @param resId Char sequence array resource ID for items
         * @return Builder itself
         */
        public Builder<T, F> setItems(final int resId) {
            mItemsResId = resId;
            return this;
        }

        /**
         * Sets the char sequence array items.<br/>
         * This also enables callback of the click event of the list items.
         *
         * @param items Char sequence array for items
         * @param icons Icon resource ID array
         * @return Builder itself
         */
        @TargetApi(Build.VERSION_CODES.ECLAIR)
        public Builder<T, F> setItems(final CharSequence[] items, final int[] icons) {
            mItems = items;
            mIcons = icons;
            return this;
        }

        /**
         * Sets the char sequence array items.<br/>
         * This also enables callback of the click event of the list items.
         *
         * @param resId Char sequence array resource ID for items
         * @param icons Icon resource ID array
         * @return Builder itself
         */
        public Builder<T, F> setItems(final int resId, final int[] icons) {
            mItemsResId = resId;
            mIcons = icons;
            return this;
        }

        /**
         * Sets the request code of the callbacks.<br/>
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
         * Sets the dialog to be cancelable or not.<br/>
         * Default value is {@code true}.<br/>
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
         * Sets the dialog to be cancelable on touching outside of the dialog.<br/>
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
         * Sets the default checked item of the single choice items.<br/>
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
         * Sets the dialog to use {@code EditText} widget.<br/>
         * Same as {@code setEditText(initialText, InputType.TYPE_CLASS_TEXT)}.
         *
         * @param initialText initial value of the {@code EditText}
         * @return Builder itself
         */
        public Builder<T, F> setEditText(final CharSequence initialText) {
            mEditTextInitialText = initialText;
            mEditTextInputType = InputType.TYPE_CLASS_TEXT;
            return this;
        }

        /**
         * Sets the dialog to use {@code EditText} widget.
         *
         * @param initialText initial value of the {@code EditText}
         * @param inputType   input types of the {@code EditText} defined in {@linkplain android.text.InputType}
         * @return Builder itself
         */
        public Builder<T, F> setEditText(final CharSequence initialText, final int inputType) {
            mEditTextInitialText = initialText;
            mEditTextInputType = inputType;
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
         * Creates the arguments of the {@code SimpleAlertDialog} as a {@code Bundle}.<br/>
         * In most cases, you don't have to call this method directly.
         *
         * @return Created arguments bundle
         */
        @TargetApi(Build.VERSION_CODES.FROYO)
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
            if (mNeutralButton != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_NEUTRAL_BUTTON, mNeutralButton);
            } else if (mNeutralButtonResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_NEUTRAL_BUTTON_RES_ID, mNeutralButtonResId);
            }
            if (mNegativeButton != null) {
                args.putCharSequence(SimpleAlertDialog.ARG_NEGATIVE_BUTTON, mNegativeButton);
            } else if (mNegativeButtonResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_NEGATIVE_BUTTON_RES_ID, mNegativeButtonResId);
            }
            if (mItems != null && Build.VERSION_CODES.ECLAIR <= Build.VERSION.SDK_INT) {
                args.putCharSequenceArray(SimpleAlertDialog.ARG_ITEMS, mItems);
            } else if (mItemsResId > 0) {
                args.putInt(SimpleAlertDialog.ARG_ITEMS_RES_ID, mItemsResId);
            }
            if (mIcons != null) {
                args.putIntArray(SimpleAlertDialog.ARG_ICONS, mIcons);
            }
            args.putBoolean(SimpleAlertDialog.ARG_CANCELABLE, mCancelable);
            args.putBoolean(SimpleAlertDialog.ARG_CANCELED_ON_TOUCH_OUTSIDE,
                    mCanceledOnTouchOutside);
            if (mSingleChoiceCheckedItem >= 0) {
                args.putInt(SimpleAlertDialog.ARG_SINGLE_CHOICE_CHECKED_ITEM,
                        mSingleChoiceCheckedItem);
            }
            if (mEditTextInitialText != null || 0 < mEditTextInputType) {
                args.putCharSequence(SimpleAlertDialog.ARG_EDIT_TEXT_INITIAL_TEXT, mEditTextInitialText);
                args.putInt(SimpleAlertDialog.ARG_EDIT_TEXT_INPUT_TYPE, mEditTextInputType);
            }
            args.putBoolean(SimpleAlertDialog.ARG_USE_VIEW, mUseView);
            args.putBoolean(SimpleAlertDialog.ARG_USE_ADAPTER, mUseAdapter);
            args.putInt(SimpleAlertDialog.ARG_REQUEST_CODE, mRequestCode);
            return args;
        }

        /**
         * Sets the target fragment of this {@code DialogFragment}.<br/>
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
