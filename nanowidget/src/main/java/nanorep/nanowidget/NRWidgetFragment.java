package nanorep.nanowidget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.Components.ChannelPresenters.NRChannelStrategy;
import nanorep.nanowidget.Components.ChannelPresenters.NRWebContentFragment;
import nanorep.nanowidget.Components.DislikeDialog;
import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRContentItem;
import nanorep.nanowidget.Components.NRLikeView;
import nanorep.nanowidget.Components.NRResultFragment;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Components.NRSuggestionsView;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.Utilities.NRItemAnimator;
import nanorep.nanowidget.Utilities.NRLinearLayoutManager;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRSearchBarListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.NRViewHolder;
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
    private ArrayList<NRResult> mQueryCopyResults;
    private NRResutlsAdapter mResutlsAdapter;
    private RecyclerView mResultsRecyclerView;
    private NRWidgetFragmentListener mListener;
    private NRResultFragment mResultFragment;
    private RelativeLayout mLoadingView;
    private ArrayList<ArrayList<NRResult>> mResultStack;
    private ArrayList<String> mSearchStrings;

    private NRResult mUnfoldedResult;

    private boolean resetSuggestions = false;

    private TextView mNoTitleView;
    private RelativeLayout mNotitleViewHolder;


    public NRWidgetFragment() {
        // Required empty public constructor
    }

    private ArrayList<String> getSearchStrings() {
        if (mSearchStrings == null) {
            mSearchStrings = new ArrayList<>();
            mSearchStrings.add("");
        }
        return mSearchStrings;
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        String url = NRChannelStrategy.presentor(getContext(), channelItem.getChanneling(), mNanoRep).getUrl();
        if (url != null) {
            onLinkClicked(url);
        }
    }

    @Override
    public void onLinkedArticleClicked(String articleId) {
        mFetchedDataManager.faqAnswer(articleId, null, new OnFAQAnswerFetched() {
            @Override
            public void onAnswerFetched(final NRQueryResult result) {
                clearResults();
                mResultsRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSearchStrings().add("");
                        NRResult newResult = new NRResult(result);
                        newResult.getFetchedResult().setIsCNF(true);
                        newResult.setHeight((int) Calculate.pxFromDp(getContext(), 62));
                        ArrayList<NRResult> linkedArray = new ArrayList<NRResult>();
                        linkedArray.add(newResult);
                        loadResults(linkedArray, true);
                    }
                }, 500);

            }
        });
    }

    @Override
    public void onLinkClicked(String url) {
        final RelativeLayout holder = (RelativeLayout) getView().findViewById(R.id.fragment_place_holder);
        holder.setVisibility(View.VISIBLE);
        NRWebContentFragment webContentFragment = NRWebContentFragment.newInstance(url, null);
        webContentFragment.setListener(new NRWebContentFragment.Listener() {
            @Override
            public void onDismiss() {
                getChildFragmentManager().popBackStack();
                holder.setVisibility(View.INVISIBLE);
                getView().requestFocus();
            }
        });
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).add(R.id.fragment_place_holder, webContentFragment).addToBackStack("linked").commit();
    }


    @Override
    public void onLikeClicked(final NRLikeView likeView, String resultId, boolean isLike) {
        if (mQueryResults == null || mQueryResults.size() == 0) {
            return;
        }
        final NRResult likedResult = mQueryResults.get(0);
        if (!likedResult.getFetchedResult().getId().equals(resultId)) {
            return;
        }
        if (isLike) {
            likedResult.getFetchedResult().setLikeState(NRQueryResult.LikeState.positive);
            mFetchedDataManager.sendLike(NRLikeType.POSITIVE, likedResult.getFetchedResult(), new Nanorep.OnLikeSentListener() {
                @Override
                public void onLikeSent(String resultId, int type, boolean success) {
                    for (NRResult result: mQueryResults) {
                        if (result.getFetchedResult().getId().equals(resultId) && !success) {
                            result.getFetchedResult().setLikeState(NRQueryResult.LikeState.notSelected);
                            break;
                        }
                    }
                }
            });
        } else {
            String reasons[] = new String[] {"Incorrect answer", "Missing or incorrect information", "Didn't find what I was looking for"};
            DislikeDialog dislikeAlert = new DislikeDialog(getContext());
            dislikeAlert.setTitle("What's wrong with this answer");
            dislikeAlert.setListener(new DislikeDialog.Listener() {
                @Override
                public void onCancel() {
                    likeView.resetLikeView();
                }

                @Override
                public void onDislike(NRLikeType type) {
                    likedResult.getFetchedResult().setLikeState(NRQueryResult.LikeState.negative);
                    mFetchedDataManager.sendLike(type, likedResult.getFetchedResult(), new Nanorep.OnLikeSentListener() {
                        @Override
                        public void onLikeSent(String resultId, int type, boolean success) {
                            for (NRResult result: mQueryResults) {
                                if (result.getFetchedResult().getId().equals(resultId) && !success) {
                                    result.getFetchedResult().setLikeState(NRQueryResult.LikeState.notSelected);
                                    break;
                                }
                            }
                        }
                    });
                }
            });
            dislikeAlert.setDislikeOptions(reasons);
        }
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

    private NRResultFragment getResultFragment() {
        if (mResultFragment == null) {
            mResultFragment = new NRResultFragment();
        }
        return mResultFragment;
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
        View nanoView = inflater.inflate(R.layout.fragment_nrwidget, container, false);
        mLoadingView = (RelativeLayout) nanoView.findViewById(R.id.fragment_place_holder);
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
                mLoadingView.setVisibility(View.INVISIBLE);
//                mResutlsAdapter.setShouldResetLikeView(true);
                if (rows == null && mSearchBar.getText() != null) {
                    mNotitleViewHolder.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 120);
                    mNoTitleView.setText(mFetchedDataManager.getConfiguration().getCustomNoAnswersTextContext(mSearchBar.getText()));
                    if (mResultStack != null) {
                        rows = mResultStack.get(0);
                    }
                }
                loadResults(rows, true);

            }

            @Override
            public void noFAQs() {
                mLoadingView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void presentSuggestion(String query, ArrayList<String> suggestions) {
                if (!resetSuggestions && mSearchBar.getText().equals(query)) {
                    mSuggestionsView.setSuggestions(suggestions);
                }
            }

            @Override
            public void onConnectionFailed(HashMap<String, Object> errorParams) {

            }
        });

        mSearchBar = (NRSearchBar) nanoView.findViewById(R.id.searchBar);
        mSearchBar.setListener(this);
        mNotitleViewHolder = (RelativeLayout) nanoView.findViewById(R.id.noTiltleView);
        mNoTitleView = (TextView) nanoView.findViewById(R.id.noTitleTextView);
        mSuggestionsView = (NRSuggestionsView)nanoView.findViewById(R.id.suggestions);
        mSuggestionsView.setListener(this);
        mResultsRecyclerView = (RecyclerView) nanoView.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new NRLinearLayoutManager(getContext()));
        mResultsRecyclerView.setAdapter(mResutlsAdapter);
        NRItemAnimator animator = new NRItemAnimator();
        animator.setListener(new NRItemAnimator.OnAnimation() {
            @Override
            public void onItemRemoved(NRResultItem item) {
                if (item instanceof NRContentItem) {
                    ((NRContentItem)item).resetBody();
                }
            }
        });
        mResultsRecyclerView.setItemAnimator(animator);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mResultsRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        }
        return nanoView;
    }

    private void loadResults(ArrayList<NRResult> rows, boolean addToStack) {
        if (rows == null) {
            return;
        }
        if (mQueryResults == null) {
            mQueryResults = new ArrayList<NRResult>();
        }
        if (mResultStack != null && rows.size() == 1) {
            rows.get(0).setSingle(true);
        }
        for (NRResult addedResult : rows) {
            mQueryResults.add(addedResult);
            mResutlsAdapter.notifyItemInserted(mQueryResults.size());
        }
        if (rows.size() == 1) {
            mQueryResults.get(0).setUnfolded(false);
            unfoldItem(mQueryResults.get(0), false);
        }
        if (addToStack) {
            if (mResultStack == null) {
                mResultStack = new ArrayList<ArrayList<NRResult>>();
            }
            if (!mResultStack.contains(mQueryResults)) {
                mResultStack.add(new ArrayList<NRResult>(mQueryResults));
            }
        }
        mQueryCopyResults = new ArrayList<NRResult>(mQueryResults);
        if (getView() != null) {
            getView().requestFocus();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mResultStack != null && mResultStack.size() > 1) {
                            ((NRResultItem)mResultsRecyclerView.findViewHolderForAdapterPosition(0)).resetArrow();
                            clearResults();
                            mResultsRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mUnfoldedResult != null) {
                                        mUnfoldedResult = null;
                                    }
                                    if (mResultStack.size() >= 2) {
                                        loadResults(mResultStack.get(mResultStack.size() - 2), false);
                                        mSearchBar.updateText(getSearchStrings().get(getSearchStrings().size() - 2), true);
                                    }
                                    getSearchStrings().remove(getSearchStrings().size() - 1);
                                    mResultStack.remove(mResultStack.size() - 1);
                                }
                            }, 500);

                        } else if (mUnfoldedResult != null && mUnfoldedResult.isUnfolded()) {
                            unfoldItem(mUnfoldedResult, false);
                        } else {
                            return false;
                        }
                        return true;
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
        if (mNotitleViewHolder.getLayoutParams().height > 0) {
            mNotitleViewHolder.getLayoutParams().height = 0;
            clearResults();
        }
        resetSuggestions = false;
        mFetchedDataManager.searchSuggestion(text);
        if (mUnfoldedResult != null) {
            unfoldItem(mUnfoldedResult, true);
        } else {
            clearResults();
        }

    }

    private void clearResults() {
        if (mQueryResults != null) {
            ArrayList<NRResult> temp = new ArrayList<>(mQueryResults);
            if (mUnfoldedResult != null) {
                unfoldItem(mUnfoldedResult, true);
            }
            for (NRResult queryResult : temp) {
                int index = mQueryResults.indexOf(queryResult);
                mQueryResults.remove(queryResult);
                mResutlsAdapter.notifyItemRemoved(index);
            }
            temp.clear();
            mQueryResults = null;
        }
    }

    @Override
    public void searchForText(String text) {
        mLoadingView.setVisibility(View.VISIBLE);

        // clear autocomplete view
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);

        // check the last search and search only if it is different 
        if (!getSearchStrings().get(getSearchStrings().size() - 1).equals(text)) {
            getSearchStrings().add(text);
            mFetchedDataManager.searchText(text);
        }
    }

    @Override
    public void onClearClicked(boolean byUser) {
        if (mNotitleViewHolder.getLayoutParams().height > 0) {
            mNotitleViewHolder.getLayoutParams().height = 0;
        }
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);


        if (byUser) {
            if (mUnfoldedResult != null) {
                unfoldItem(mUnfoldedResult, true);
            }
            clearResults();
            mResultsRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mResultStack != null && mResultStack.size() > 0) {
                        loadResults(mResultStack.get(0), false);
                        mQueryResults = mResultStack.get(0);
                    }
                    if (mResultStack != null) {
                        mResultStack.clear();
                        getSearchStrings().clear();
                        mResultStack.add(new ArrayList<NRResult>(mQueryResults));
                        getSearchStrings().add("");
                    }
                }
            }, 500);

        }
    }

    @Override
    public void onEmptyQuery() {
        mSuggestionsView.setSuggestions(null);
    }

    @Override
    public void onSelectSuggestion(String suggestion) {
        mLoadingView.setVisibility(View.VISIBLE);
        getSearchStrings().add(suggestion);
        mSearchBar.dismissKeyboard();
        mSearchBar.updateText(suggestion);
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);
        mFetchedDataManager.searchText(suggestion);
    }

    @Override
    public void unfoldItem(NRResult result, boolean clear) {
        if (result.isUnfolded()) {
            mUnfoldedResult.setUnfolded(false);
            mUnfoldedResult = null;
            mQueryResults.remove(1);
            mResutlsAdapter.notifyItemRemoved(1);
            if (clear) {
                mQueryResults.remove(0);
                mResutlsAdapter.notifyItemRemoved(0);
            } else {
                int pos = mQueryCopyResults.indexOf(result);
                for (int i = 0; i < mQueryCopyResults.size(); i++) {
                    if (i != pos && mQueryCopyResults.size() >= i) {
                        mQueryResults.add(i, mQueryCopyResults.get(i));
                        mResutlsAdapter.notifyItemInserted(i);
                    } else {
                        ((NRResultItem)mResultsRecyclerView.findViewHolderForAdapterPosition(i)).resetArrow();
                    }
                }
            }
        } else {
            ArrayList<NRResult> temp = new ArrayList<>(mQueryResults);
            for (NRResult item1 : temp) {
                int pos = mQueryResults.indexOf(item1);
                if (!item1.equals(result)) {
                    mQueryResults.remove(item1);
                    mResutlsAdapter.notifyItemRemoved(pos);
                }
            }
            if (mQueryResults != null && mQueryResults.size() > 0) {
                mUnfoldedResult = mQueryResults.get(0);
                mUnfoldedResult.setUnfolded(true);
                temp.clear();
                NRResult content = new NRResult(result.getFetchedResult());
                content.setRowType(NRViewHolder.RowType.unfolded);
                mQueryResults.add(content);
                mResutlsAdapter.notifyItemInserted(1);
            }

        }
    }

    private void deleteResult(NRResult result) {

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
    public void fetchBodyForResult(final NRContentItem item, String resultID, Integer resultHash) {
        mFetchedDataManager.faqAnswer(resultID, resultHash, new OnFAQAnswerFetched() {
            @Override
            public void onAnswerFetched(NRQueryResult result) {
                if (result != null) {
                    item.setBody(result.getBody());
                    item.setChanneling(result.getChanneling());
                }
            }
        });
    }


    private class NRResutlsAdapter extends RecyclerView.Adapter<NRResultItem> {
        private boolean mShouldResetLikeView = false;

        public void setShouldResetLikeView(boolean shouldResetLikeView) {
            mShouldResetLikeView = shouldResetLikeView;
        }

        @Override
        public NRResultItem onCreateViewHolder(ViewGroup parent, int viewType) {
            int viewId = 0;
            switch (viewType) {
                case 0:
                    viewId = R.layout.content_item;
                    break;
                case 1:
                    viewId = R.layout.result_item;
                    break;
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
            NRResultItem item = null;
            switch (viewType) {
                case 0:
                    item = new NRContentItem(view, mResultsRecyclerView.getHeight());
                    break;
                case 1:
                    item = new NRResultItem(view, mResultsRecyclerView.getHeight());
                    break;
            }

            if (item != null) {
                item.setListener(NRWidgetFragment.this);
            }
            return item;
        }

        @Override
        public void onBindViewHolder(NRResultItem holder, int position) {
            if (mQueryResults != null && mQueryResults.size() >= position) {
                holder.setResult(mQueryResults.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mQueryResults == null || mQueryResults.size() < position) {
                return 0;
            }
            switch (mQueryResults.get(position).getRowType()) {
                case standard:
                    return 1;
            }
            return 0;
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
