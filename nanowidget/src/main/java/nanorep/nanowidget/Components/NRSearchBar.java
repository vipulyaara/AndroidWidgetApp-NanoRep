package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRSearchBarListener;



/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSearchBar extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private NRSearchBarListener mListener;
    private ImageButton mSpeechButton;
    private EditText mSearchEditText;

    public NRSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mSearchEditText = (EditText) child.findViewById(R.id.searchText);
        mSearchEditText.addTextChangedListener(this);
        mSearchEditText.setHint("Type Your Question Here");
        mSearchEditText.setTextColor(Color.WHITE);
        mSearchEditText.setHintTextColor(Color.LTGRAY);
        mSearchEditText.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
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

    @Override
    public void onClick(View v) {
        if ((Boolean) v.getTag()) {
            mSearchEditText.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mListener.fetchSuggestionsForText(charSequence.toString());
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mSpeechButton.setVisibility(charSequence.length() > 0 ? VISIBLE : INVISIBLE);
        mSpeechButton.setImageResource(resId(charSequence.length() > 0 ? "searchbar_x_icon" : "searchbar_mic_icon"));
        Boolean state = Boolean.valueOf(charSequence.length() > 0);
        mSpeechButton.setTag(state);
        if (charSequence.length() == 0) {
            mSearchEditText.clearFocus();
            dismissKeyboard();
            mListener.onClear();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
