package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;

/**
 * Created by noat on 29/11/2016.
 */

public class GettSearchBar extends NRCustomSearchBarView implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    private EditText mSearchEditText;
    private ImageButton mSpeechButton;
    private ImageView magnifier;

    public GettSearchBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.search_bar_gett, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        magnifier = (ImageView)  child.findViewById(R.id.magnifier);

        mSearchEditText = (EditText) child.findViewById(nanorep.nanowidget.R.id.searchText);
        mSearchEditText.addTextChangedListener(this);
        mSearchEditText.setOnEditorActionListener(this);

        mSpeechButton = (ImageButton) child.findViewById(nanorep.nanowidget.R.id.speechButton);
        mSpeechButton.setOnClickListener(this);

        mSearchEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        clear();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void clear() {
        mSearchEditText.clearFocus();
        dismissKeyboard();
        mListener.onClearClicked(true);
        updateEditTextView("");
        buttonsVisability("");
    }


    @Override
    public String getText() {
        return mSearchEditText.getText().toString();
    }

    @Override
    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    @Override
    public void updateText(String text) {
        mSearchEditText.clearFocus();
        mSearchEditText.setText(text);
    }

    @Override
    public void updateEditTextView(String text) {
        mSearchEditText.removeTextChangedListener(this);
        updateText(text);
        mSearchEditText.addTextChangedListener(this);
    }

    @Override
    public void setHint(String text) {
        mSearchEditText.setHint(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private void buttonsVisability(CharSequence charSequence) {
        mSpeechButton.setVisibility(charSequence.length() > 0 ? VISIBLE : GONE);
        magnifier.setVisibility(charSequence.length() > 0 ? GONE : VISIBLE);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        buttonsVisability(charSequence);

        mSpeechButton.setImageResource(R.drawable.searchbar_x_icon);

        Boolean state = Boolean.valueOf(charSequence.length() > 0);
        mSpeechButton.setTag(state);

        if (charSequence.length() == 0) {
            dismissKeyboard();
            mListener.onClearClicked(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && editable.length() > 0) {
            mListener.fetchSuggestionsForText(mSearchEditText.getText().toString());
        } else {
            mListener.onEmptyQuery();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mListener.searchForText(mSearchEditText.getText().toString());
            dismissKeyboard();
            return true;
        }
        return false;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
        if ((Boolean) v.getTag()) {
            clear();
        }
    }
}
