package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Nanorep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;

/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSuggestionsView extends NRCustomSuggestionsView {

    public NRSuggestionsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.suggestion_view, this);
    }


    public void setSuggestions(ArrayList<Spannable> suggestions) {
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



}
