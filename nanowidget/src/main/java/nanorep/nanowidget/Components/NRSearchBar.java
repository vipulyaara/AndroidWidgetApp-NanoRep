package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRSearchBarListener;



/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSearchBar extends NRCustomSearchBarView implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    private NRSearchBarListener mListener;
    private ImageButton mSpeechButton;
    private NREditText mSearchEditText;

    public NRSearchBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mSearchEditText = (NREditText) child.findViewById(R.id.searchText);
        mSearchEditText.addTextChangedListener(this);
//        mSearchEditText.setHint(getResources().getString(R.string.type_question_here));
//        mSearchEditText.setTextColor(Color.WHITE);
//        mSearchEditText.setHintTextColor(Color.WHITE);
//        mSearchEditText.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        mSearchEditText.setOnEditorActionListener(this);
        mSpeechButton = (ImageButton) child.findViewById(R.id.speechButton);
        mSpeechButton.setOnClickListener(this);
    }

    public void setListener(NRSearchBarListener listener) {
        mListener = listener;
    }


    private int resId(String resName) {
        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    public void updateText(String text) {
        mSearchEditText.setText(text);
        mSearchEditText.clearFocus();
    }

    public void setHint(String text) {
        mSearchEditText.setHint(text);
    }

    public void updateEditTextView(String text) {
        mSearchEditText.removeTextChangedListener(this);
        mSpeechButton.setVisibility(text.length() > 0 ? VISIBLE : INVISIBLE);
        updateText(text);
        mSearchEditText.addTextChangedListener(this);
    }

    public String getText() {
        return mSearchEditText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if ((Boolean) v.getTag()) {
            mSearchEditText.clearFocus();
            dismissKeyboard();
            mListener.onClearClicked(true);
            mSearchEditText.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mSpeechButton.setVisibility(charSequence.length() > 0 ? VISIBLE : INVISIBLE);
        mSpeechButton.setImageResource(resId(charSequence.length() > 0 ? "searchbar_x_icon" : "searchbar_mic_icon"));
        Boolean state = Boolean.valueOf(charSequence.length() > 0);
        mSpeechButton.setTag(state);
        if (charSequence.length() == 0) {

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && editable.length() > 0) {
            mListener.fetchSuggestionsForText(mSearchEditText.getText().toString());
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
}
