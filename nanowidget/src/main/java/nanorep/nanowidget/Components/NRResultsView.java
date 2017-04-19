package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nanorep.nanoclient.Nanorep;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.DataClasse.NRResultsAdapter;
import nanorep.nanowidget.Fragments.NRMainFragment;
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
    private String title;

    private RelativeLayout frequentlyQuestions;
    private TextView frequentlyQuestionsTv;

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

        frequentlyQuestions = (RelativeLayout) child.findViewById(R.id.frequentlyQuestions);
        frequentlyQuestionsTv = (TextView) child.findViewById(R.id.frequentlyQuestionsTv);

        adapter = new NRResultsAdapter();

        mResultsRecyclerView = (RecyclerView) child.findViewById(R.id.resultsView);

        String margin = Nanorep.getInstance().getNRConfiguration().getContent().getContentMarginTop();

        if(margin != null) {
            LayoutParams params = (LayoutParams) mResultsRecyclerView.getLayoutParams();
            params.setMargins(0, (int) Calculate.pxFromDp(getContext(), Float.valueOf(margin)), 0, 0);
            mResultsRecyclerView.setLayoutParams(params);
        }

        mResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mResultsRecyclerView.setAdapter(adapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setResults(ArrayList<NRResult> results, String title, NRCustomViewAdapter viewAdapter)  {

//        if(!NRMainFragment.isEmpty(title)) {
//            frequentlyQuestions.setVisibility(View.VISIBLE);
//            frequentlyQuestionsTv.setText(title);
//        }
        frequentlyQuestionsTv.setText("Popular Questions");

        this.title = title;

        adapter.setListener(this);
        adapter.setViewAdapter(viewAdapter);
        adapter.addItems(results);
    }

    public String getTitle() {
        return title;
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
