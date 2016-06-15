package nanorep.nanowidget.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

    public void setText(String text) {
        mTextView.setText(text);
    }
}
