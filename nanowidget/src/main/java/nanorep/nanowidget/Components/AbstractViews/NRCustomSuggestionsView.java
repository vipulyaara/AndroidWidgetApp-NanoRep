package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import nanorep.nanowidget.Components.NRSuggestionItem;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;

/**
 * Created by nanorep on 28/09/2016.
 */

public abstract class NRCustomSuggestionsView extends LinearLayout{

    protected ArrayList<Spannable> mSuggestions;

    protected RecyclerView.Adapter mAdapter;

    protected RecyclerView mRecyclerView;

    protected NRSuggestionsListener mListener;

    public NRCustomSuggestionsView(Context context) {
        super(context);
    }

    abstract  public void setSuggestions(ArrayList<Spannable> suggestions);

    public void setListener(NRSuggestionsListener listener) {
        mListener = listener;
    }

    public class SuggestionsAdapter extends RecyclerView.Adapter<NRSuggestionItem> implements NRSuggestionItem.OnSuggestionSelectedListener {

        @Override
        public NRSuggestionItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);
            NRSuggestionItem item = new NRSuggestionItem(view);
            item.setListener(this);
            return item;
        }

        @Override
        public void onBindViewHolder(NRSuggestionItem holder, int position) {
            holder.setText(mSuggestions.get(position));
        }

        @Override
        public int getItemCount() {
            if (mSuggestions != null) {
                return mSuggestions.size();
            }
            return 0;
        }

        @Override
        public void onSuggestionSelected(String text) {
            mListener.onSelectSuggestion(text);
        }
    }
}
