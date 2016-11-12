package nanorep.nanowidget.Components;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import nanorep.nanowidget.R;



/**
 * Created by nissimpardo on 08/06/16.
 */
public class NRSuggestionItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTextView;
    private OnSuggestionSelectedListener mListener;

    @Override
    public void onClick(View v) {
        mListener.onSuggestionSelected(((TextView)v).getText().toString());
    }

    interface OnSuggestionSelectedListener {
        void onSuggestionSelected(String text);
    }

    public NRSuggestionItem(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.suggestion);
        mTextView.setOnClickListener(this);
    }

    public void setListener(OnSuggestionSelectedListener listener) {
        mListener = listener;
    }

    public void setText(Spannable text) {

        mTextView.setText(text);
    }
}
