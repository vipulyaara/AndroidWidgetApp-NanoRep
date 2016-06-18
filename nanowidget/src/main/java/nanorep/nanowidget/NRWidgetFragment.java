package nanorep.nanowidget;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import NanoRep.Interfaces.NRQueryResult;
import NanoRep.NanoRep;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Components.NRSuggestionsView;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRSearchBarListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NRWidgetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NRWidgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NRWidgetFragment extends Fragment implements NRSearchBarListener, NRSuggestionsListener, NRResultItemListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private NRFetchedDataManager mFetchedDataManager;

    private NRSearchBar mSearchBar;
    private NRSuggestionsView mSuggestionsView;

    private ArrayList<NRResult> mQueryResults;
    private NRResutlsAdapter mResutlsAdapter;
    private RecyclerView mResultsRecyclerView;

    public NRWidgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NRWidgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NRWidgetFragment newInstance(String param1, String param2) {
        NRWidgetFragment fragment = new NRWidgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
        assert ((AppCompatActivity)getActivity()).getSupportActionBar() != null;
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mResutlsAdapter = new NRResutlsAdapter();
        NanoRep nanoRep = new NanoRep("Main", null);
        mFetchedDataManager = new NRFetchedDataManager(nanoRep);
        mFetchedDataManager.setFetcherListener(new NRFetcherListener() {
            @Override
            public void updateTitle(String title) {

            }

            @Override
            public void reloadWithAimation() {

            }

            @Override
            public void reload() {

            }

            @Override
            public void insertRows(ArrayList<NRResult> rows) {
                mQueryResults = rows;
                mResutlsAdapter.notifyDataSetChanged();
            }

            @Override
            public void presentSuggestion(ArrayList<String> suggestions) {
                mSuggestionsView.setSuggestions(suggestions);
            }

            @Override
            public void onConnectionFailed(HashMap<String, Object> errorParams) {

            }
        });
        View nanoView = inflater.inflate(R.layout.fragment_nrwidget, container, false);
        mSearchBar = (NRSearchBar) nanoView.findViewById(R.id.searchBar);
        mSearchBar.setListener(this);
        mSuggestionsView = (NRSuggestionsView)nanoView.findViewById(R.id.suggestions);
        mSuggestionsView.setListener(this);
        mResultsRecyclerView = (RecyclerView) nanoView.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mResultsRecyclerView.setAdapter(mResutlsAdapter);
        return nanoView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nano_menu, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStartRecording() {

    }

    @Override
    public void fetchSuggestionsForText(String text) {
        mFetchedDataManager.searchSuggestion(text);
    }

    @Override
    public void searchForText(String text) {
        mFetchedDataManager.searchText(text);
    }

    @Override
    public void onClear() {
        mSuggestionsView.setSuggestions(null);
    }

    @Override
    public void onSelectSuggestion(String suggestion) {
        mSearchBar.dismissKeyboard();
        mSuggestionsView.setSuggestions(null);
        mQueryResults = null;
        mResutlsAdapter.notifyDataSetChanged();
        mFetchedDataManager.searchText(suggestion);
    }

    @Override
    public void shouldFetchFAQAnswerBody(final NRResultItem item, String answerId) {
        mFetchedDataManager.faqAnswer(answerId, new OnFAQAnswerFetched() {
            @Override
            public void onAnsweFetced(String answerBody) {
                item.setBody(answerBody);
            }
        });
    }

    @Override
    public void unfoldItem(NRResultItem item) {
        if (item.getResult().isUnfolded()) {
            item.getResult().setUnfolded(false);
            for (NRResult result: mQueryResults) {
                result.setHeight(62);
            }
        } else {
            for (int i = 0; i < mQueryResults.size(); i++) {
                if (i == item.getAdapterPosition()) {
                    mQueryResults.get(i).setHeight(500);
                    mQueryResults.get(i).setUnfolded(true);
                } else {
                    mQueryResults.get(i).setHeight(10);
                    mQueryResults.get(i).setUnfolded(false);
                }
            }
        }
        mResutlsAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class NRResutlsAdapter extends RecyclerView.Adapter<NRResultItem> {

        @Override
        public NRResultItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
            NRResultItem item = new NRResultItem(view, mResultsRecyclerView.getHeight());

            item.setListener(NRWidgetFragment.this);
            return item;
        }

        @Override
        public void onBindViewHolder(NRResultItem holder, int position) {
            holder.setResult(mQueryResults.get(position));
        }

        @Override
        public int getItemCount() {
            if (mQueryResults == null) {
                return 0;
            }
            return mQueryResults.size();
        }
    }


}
