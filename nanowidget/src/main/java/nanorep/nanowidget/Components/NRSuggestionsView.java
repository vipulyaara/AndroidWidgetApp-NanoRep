package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;

/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSuggestionsView extends NRCustomSuggestionsView {

    private ArrayList<String> mSuggestions;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;

    public NRSuggestionsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.suggestion_view, this);
    }


    public void setSuggestions(ArrayList<String> suggestions) {
        if (suggestions == null) {
            setHeight(0);
        } else {
            setHeight(suggestions.size() * 40);
        }
        mSuggestions = suggestions;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mRecyclerView = (RecyclerView) child.findViewById(R.id.recyclerSuggestions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new SuggestionsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        setLayoutParams(params);
    }

    public class SuggestionsAdapter extends RecyclerView.Adapter<NRSuggestionItem> implements NRSuggestionItem.OnSuggestionSelectedListener {

        @Override
        public NRSuggestionItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, null);
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
