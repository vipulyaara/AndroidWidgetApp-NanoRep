package nanorep.nanowidget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.Components.ChannelPresenters.NRChannelStrategy;
import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRResultFragment;
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



public class NRWidgetFragment extends Fragment implements NRSearchBarListener, NRSuggestionsListener, NRResultItemListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Nanorep mNanoRep;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private NRFetchedDataManager mFetchedDataManager;

    private NRSearchBar mSearchBar;
    private NRSuggestionsView mSuggestionsView;

    private ArrayList<NRResult> mQueryResults;
    private NRResutlsAdapter mResutlsAdapter;
    private RecyclerView mResultsRecyclerView;
    private NRWidgetFragmentListener mListener;

    public NRWidgetFragment() {
        // Required empty public constructor
    }

    public interface NRWidgetFragmentListener {
        void onCancelWidget(NRWidgetFragment widgetFragment);
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

    public void setNanoRep(Nanorep nanoRep) {
        mNanoRep = nanoRep;
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
//        assert ((AppCompatActivity)getActivity()).getSupportActionBar() != null;
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mResutlsAdapter = new NRResutlsAdapter();

        mFetchedDataManager = new NRFetchedDataManager(mNanoRep, getContext());
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
                mResutlsAdapter.setShouldResetLikeView(true);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mResultsRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        }
        return nanoView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mListener != null) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                                getChildFragmentManager().popBackStack();
                            } else {
                                mListener.onCancelWidget(NRWidgetFragment.this);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setListener(NRWidgetFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nano_menu, menu);
    }



    @Override
    public void onStartRecording(final ImageButton button) {
//        mFetchedDataManager.startSpeech(new NRSpeechRecognizerCompletion() {
//            @Override
//            public void speechReconitionResults(String speechToText) {
//                button.setEnabled(true);
//                onSelectSuggestion(speechToText);
//            }
//        });
    }

    @Override
    public void fetchSuggestionsForText(String text) {
        mFetchedDataManager.searchSuggestion(text);
    }

    @Override
    public void searchForText(String text) {
        mSuggestionsView.setSuggestions(null);
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
    public void unfoldItem(NRResultItem item) {
        NRResultFragment resultFragment = new NRResultFragment();
        resultFragment.setListener(new NRResultFragment.Listener() {
            @Override
            public void onResultFragmentDismissed(NRResultFragment resultFragment) {

            }

            @Override
            public void resultFragmentWillDismiss(NRResultFragment resultFragment) {
//                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).remove(resultFragment).commit();
            }

            @Override
            public void onLikeSelected(final NRResultFragment resultFragment, final NRLikeType likeType, NRResult currentResult) {
                mFetchedDataManager.sendLike(likeType, currentResult, new Nanorep.OnLikeSentListener() {
                    @Override
                    public void onLikeSent(int type, boolean success) {
                        resultFragment.setLikeState(likeType == NRLikeType.POSITIVE);
                    }
                });
            }

            @Override
            public void fetchBodyForResult(final NRResultFragment resultFragment, String resultID) {
                mFetchedDataManager.faqAnswer(resultID, new OnFAQAnswerFetched() {
                    @Override
                    public void onAnsweFetced(String answerBody) {
                        resultFragment.setBody(answerBody);
                    }
                });
            }

            @Override
            public void onChannelSelected(NRResultFragment resultFragment, NRChannelItem channelItem) {
                NRChannelStrategy.presentor(channelItem.getChanneling(), resultFragment, mFetchedDataManager.getNanoRep()).present();
            }

            @Override
            public void onLinkedArticleClicked(String articleId) {
                mFetchedDataManager.faqAnswer(articleId, new OnFAQAnswerFetched() {
                    @Override
                    public void onAnsweFetced(String answerBody) {

                    }
                });
            }
        });
        resultFragment.setResult(item.getResult());
        getView().findViewById(R.id.fragment_place_holder).setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left).add(R.id.fragment_place_holder, resultFragment).addToBackStack("test").commit();
    }

    @Override
    public void onShareClicked(NRResultItem item, String linkToShare) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "nanorep Result");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, linkToShare);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onLikeClicked(final NRResultItem item) {

    }

    private class NRResutlsAdapter extends RecyclerView.Adapter<NRResultItem> {
        private boolean mShouldResetLikeView = false;

        public void setShouldResetLikeView(boolean shouldResetLikeView) {
            mShouldResetLikeView = shouldResetLikeView;
        }

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
