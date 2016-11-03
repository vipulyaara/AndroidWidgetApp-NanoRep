package nanorep.nanowidget.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.DataClasse.NRResultsAdapter;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.NRLinearLayoutManager;
import nanorep.nanowidget.interfaces.NRConfigFetcherListener;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRFetcherListener;

/**
 * Created by noat on 03/11/2016.
 */

public class NRWidgetCategoriesFragment extends Fragment {

    public static final String TAG = NRWidgetCategoriesFragment.class.getName();

    private Nanorep mNanoRep;

    private NRFetchedDataManager mFetchedDataManager;
    private RecyclerView mResultsRecyclerView;
    private NRResultsAdapter mResultsAdapter;
    NRCustomViewAdapter viewAdapter;
    private RelativeLayout mLoadingView;

    public static NRWidgetCategoriesFragment newInstance() {

        Bundle args = new Bundle();

        NRWidgetCategoriesFragment fragment = new NRWidgetCategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nrwidgetcategories, container, false);

        mLoadingView = (RelativeLayout) view.findViewById(R.id.fragment_place_holder);

        mResultsAdapter = new NRResultsAdapter(viewAdapter, getActivity());

        mResultsRecyclerView = (RecyclerView) view.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new NRLinearLayoutManager(getContext()));
        mResultsRecyclerView.setAdapter(mResultsAdapter);

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFetchedDataManager = new NRFetchedDataManager(mNanoRep, getContext());

        mFetchedDataManager.setConfigFetcherListener(new NRConfigFetcherListener() {
            @Override
            public void onConfigurationReady() {
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void insertRows(ArrayList<NRFAQGroupItem> groups) {

                mResultsAdapter.addItems(groups);
            }
        });
    }

    public void setViewAdapter(NRCustomViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

    public void setNanoRep(Nanorep nanoRep) {
        mNanoRep = nanoRep;
    }

    public NRFetchedDataManager getmFetchedDataManager() {
        return mFetchedDataManager;
    }
}
