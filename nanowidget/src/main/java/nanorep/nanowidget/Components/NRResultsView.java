package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.DataClasse.NRResultsAdapter;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;


/**
 * Created by noat on 08/11/2016.
 */

public class NRResultsView extends LinearLayout implements NRResultsAdapter.Listener{

    private RecyclerView mResultsRecyclerView;
    private NRResultsAdapter adapter;
    private Listener listener;
    private boolean isAnimated;

    public void setIsAnimated(boolean isAnimated) {
        this.isAnimated = isAnimated;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean isAnimated) {
        this.isAnimated = isAnimated;
    }


    public interface Listener{
        void onResultSelected(int y, NRResultsAdapter.ViewHolder titleViewHolder);
    }

    public NRResultsView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.results_view, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        adapter = new NRResultsAdapter();

        mResultsRecyclerView = (RecyclerView) child.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mResultsRecyclerView.setAdapter(adapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setResults(ArrayList<NRResult> results, NRCustomViewAdapter viewAdapter)  {
        adapter.setListener(this);
        adapter.setViewAdapter(viewAdapter);
        adapter.addItems(results);
    }

    @Override
    public void onResultItemSelected(NRResultsAdapter.ViewHolder titleViewHolder, int pos) {
        int marginTop = titleViewHolder.getTitle_container().getHeight() * pos;

        int offSet = mResultsRecyclerView.computeVerticalScrollOffset();

        int divider = (int) Calculate.pxFromDp(getContext(), 5) * pos;

        int y = marginTop - offSet + (int) Calculate.pxFromDp(getContext(), 16) + divider;

        if(isAnimated) {
            titleViewHolder.getTitleView().setVisibility(View.INVISIBLE);
        }

        listener.onResultSelected(y, titleViewHolder);
    }

}
