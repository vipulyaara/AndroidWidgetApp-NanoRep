package nanorep.nanowidget.Components;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Nanorep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;


/**
 * Created by nissimpardo on 08/06/16.
 */
public class NRSuggestionItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTextView;
    private OnSuggestionSelectedListener mListener;
    private View itemView;
    private LinearLayout divider;

    @Override
    public void onClick(View v) {
        mListener.onSuggestionSelected(mTextView.getText().toString());
    }

    public interface OnSuggestionSelectedListener {
        void onSuggestionSelected(String text);
    }

    public NRSuggestionItem(View itemView) {
        super(itemView);
        this.itemView = itemView;
        int height = Nanorep.getInstance().getNRConfiguration().getAutoComplete().getSuggestionRowHeight();
        this.itemView.getLayoutParams().height = (int) Calculate.pxFromDp(itemView.getContext(), height);
        this.itemView.requestLayout();
        mTextView = (TextView) itemView.findViewById(R.id.suggestion);
        this.itemView.setOnClickListener(this);
        divider = (LinearLayout) itemView.findViewById(R.id.divider);
        if(Nanorep.getInstance().getNRConfiguration().getAutoComplete().isDividerVisible()) {
            divider.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
        }

        Integer maxLines = Nanorep.getInstance().getNRConfiguration().getAutoComplete().getMaxLines();
        if(maxLines != null) {
            mTextView.setMaxLines(maxLines);
        }
    }

    public void setListener(OnSuggestionSelectedListener listener) {
        mListener = listener;
    }

    public void setText(Spannable text) {

        mTextView.setText(text);
    }
}
