package nanorep.nanowidget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import nanorep.nanowidget.Components.NRChannelingItem;
import nanorep.nanowidget.Components.NRContentItem;
import nanorep.nanowidget.Components.NRLikeItem;
import nanorep.nanowidget.Components.NRLikeView;
import nanorep.nanowidget.Components.NRResultFragment;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRTitleItem;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Components.NRSuggestionsView;
import nanorep.nanowidget.Components.SimpleDividerItemDecoration;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.Utilities.NRItemAnimator;
import nanorep.nanowidget.Utilities.NRLinearLayoutManager;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRSearchBarListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;


public class NRWidgetFragment extends Fragment implements NRSearchBarListener, NRSuggestionsListener, NRResultItemListener {
    private Nanorep mNanoRep;

    private NRFetchedDataManager mFetchedDataManager;

    private NRSearchBar mSearchBar;
    private NRSuggestionsView mSuggestionsView;

    private ArrayList<NRResult> mQueryResults;
    private ArrayList<NRResult> mQueryCopyResults;
    private NRResultsAdapter mResultsAdapter;
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
                        NRResult newResult = new NRResult(result, NRResultItem.RowType.TITLE);
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
     * @return A new instance of fragment NRWidgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NRWidgetFragment newInstance() {
        NRWidgetFragment fragment = new NRWidgetFragment();
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

        mFetchedDataManager = new NRFetchedDataManager(mNanoRep, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
//        assert ((AppCompatActivity)getActivity()).getSupportActionBar() != null;
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        final View nanoView = inflater.inflate(R.layout.fragment_nrwidget, container, false);
        mLoadingView = (RelativeLayout) nanoView.findViewById(R.id.fragment_place_holder);

        mResultsAdapter = new NRResultsAdapter();

        mFetchedDataManager.setFetcherListener(new NRFetcherListener() {
            @Override
            public void onConfigurationReady() {
                updateTitleNormalText();
                updateSerchBarHint();
                showSuggestionsView(nanoView);
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
                mResultsAdapter.setShouldResetLikeView(true);
                if (rows == null && mSearchBar.getText() != null) {
                    mNotitleViewHolder.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 120);
                    mNoTitleView.setText(mNanoRep.getNRConfiguration().getCustomNoAnswersTextContext(mSearchBar.getText()));
                    rows = mResultStack.get(0);
                }
                loadResults(rows, true);

            }

            @Override
            public void presentSuggestion(String query, ArrayList<String> suggestions) {
                if (!resetSuggestions && mSearchBar.getText().length() - query.length() <= 1) {
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
        mResultsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        mResultsRecyclerView.setAdapter(mResultsAdapter);
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

    private void showSuggestionsView(View nanoView) {
        if(!mNanoRep.getNRConfiguration().getAutocompleteEnabled()) {
            
        }
    }

    private void updateSerchBarHint() {
        mSearchBar.setHint(mNanoRep.getNRConfiguration().getSearchBar().getInitialText());
    }

    private void updateTitleNormalText() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mNanoRep.getNRConfiguration().getTitleNormalText());
    }

    private void loadResults(ArrayList<NRResult> rows, boolean addToStack) {
        if (mQueryResults == null) {
            mQueryResults = new ArrayList<NRResult>();
        }
        if (rows.size() == 1) {
            rows.get(0).setSingle(true);
        }
        for (NRResult addedResult : rows) {
            mQueryResults.add(addedResult);
            mResultsAdapter.notifyItemInserted(mQueryResults.size());
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
        getView().requestFocus();
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
                            clearResults();
                            mResultsRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mUnfoldedResult != null) {
                                        mUnfoldedResult = null;
                                    }
                                    loadResults(mResultStack.get(mResultStack.size() - 2), false);
                                    mSearchBar.updateText(getSearchStrings().get(getSearchStrings().size() - 2), true);
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
            for (NRResult queryResult : temp) {
                int index = mQueryResults.indexOf(queryResult);
                mQueryResults.remove(queryResult);
                mResultsAdapter.notifyItemRemoved(index);
            }
            temp.clear();
            mQueryResults = null;
        }
    }

    @Override
    public void searchForText(String text) {
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);
        getSearchStrings().add(text);
        mFetchedDataManager.searchText(text);
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
                    loadResults(mResultStack.get(0), false);
                    mQueryResults = mResultStack.get(0);
                    mResultStack.clear();
                    getSearchStrings().clear();
                    mResultStack.add(new ArrayList<NRResult>(mQueryResults));
                    getSearchStrings().add("");
                }
            }, 500);

        }
    }

    @Override
    public void onSelectSuggestion(String suggestion) {

        getSearchStrings().add(suggestion);
        mSearchBar.dismissKeyboard();
        mSearchBar.updateText(suggestion);
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);
        mFetchedDataManager.searchText(suggestion);
    }

    @Override
    public void unfoldItem(NRResult result, boolean clear) {

        if (result.isUnfolded()) { // close answer, show titles..

            mUnfoldedResult.setUnfolded(false);
            mUnfoldedResult = null;

            // clear content, like, etc..
            ArrayList<NRResult> temp = new ArrayList<>(mQueryResults);
            for (NRResult item1 : temp) {
                int pos = mQueryResults.indexOf(item1);
                if(pos > 0) {
                    mQueryResults.remove(pos);
                    mResultsAdapter.notifyItemRemoved(pos);
                }
            }
            temp.clear();

            if (clear) { // clear title

                mQueryResults.remove(0);
                mResultsAdapter.notifyItemRemoved(0);

            } else { // show all titles, except the one that is already opened..

                int pos = mQueryCopyResults.indexOf(result);
                for (int i = 0; i < mQueryCopyResults.size(); i++) {
                    if (i != pos) {
                        mQueryResults.add(i, mQueryCopyResults.get(i));
                        mResultsAdapter.notifyItemInserted(i);
                    } else {
                        mResultsAdapter.notifyItemChanged(i);
                    }
                }
            }
        } else { // title was clicked, show answer

            ArrayList<NRResult> temp = new ArrayList<>(mQueryResults);
            for (NRResult item1 : temp) {
                int pos = mQueryResults.indexOf(item1);
                if (!item1.equals(result)) {
                    mQueryResults.remove(item1);
                    mResultsAdapter.notifyItemRemoved(pos);
                }
            }

            // set the NRResult of the answer we clicked..
            mUnfoldedResult = mQueryResults.get(0);
            mUnfoldedResult.setUnfolded(true);
            temp.clear();

            //change the title's height to wrap content
            mResultsAdapter.notifyItemChanged(0);

            //content
            NRResult content = new NRResult(result.getFetchedResult(), NRResultItem.RowType.CONTENT);
            mQueryResults.add(content);
            mResultsAdapter.notifyItemInserted(1);

            //like
            NRResult like = new NRResult(result.getFetchedResult(), NRResultItem.RowType.LIKE);
            mQueryResults.add(like);
            mResultsAdapter.notifyItemInserted(2);

            //channeling
            if(result.getFetchedResult().getChanneling() != null && result.getFetchedResult().getChanneling().size() > 0) {
                NRResult channeling = new NRResult(result.getFetchedResult(), NRResultItem.RowType.CHANNELING);
                mQueryResults.add(channeling);
                mResultsAdapter.notifyItemInserted(3);
            }
        }
    }

    private void deleteResult(NRResult result) {

    }

    @Override
    public void onShareClicked(NRTitleItem item, String linkToShare) {
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
                item.setBody(result.getBody());
            }
        });
    }

    private class NRResultsAdapter extends RecyclerView.Adapter<NRResultItem> {
        private boolean mShouldResetLikeView = false;

        public void setShouldResetLikeView(boolean shouldResetLikeView) {
            mShouldResetLikeView = shouldResetLikeView;
        }

        @Override
        public NRResultItem onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;

            switch (viewType) {
                case 0: // title
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false);
                    return new NRTitleItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration());
                case 1: // content
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
                    return new NRContentItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration());
                case 2: // like
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);
                    return new NRLikeItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration());
                case 3: // channeling
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channeling_item, parent, false);
                    return new NRChannelingItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration());
                default:
                    return null;
            }
        }

        private  int getMaxHeight() {
            NRTitleItem titleViewHolder = (NRTitleItem)mResultsRecyclerView.findViewHolderForLayoutPosition(0);
            int titleHeight = titleViewHolder.getTitleMeasuredHeight();

            int maxHeight = mResultsRecyclerView.getHeight();

            return maxHeight - titleHeight;
        }

        @Override
        public void onBindViewHolder(NRResultItem holder, int position) {
            if(holder instanceof NRContentItem) {
                ((NRContentItem) holder).setmMaxHeight(getMaxHeight());
            }
            holder.setData(mQueryResults.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            switch (mQueryResults.get(position).getRowType()) {
                case TITLE:
                    return 0;
                case CONTENT:
                    return 1;
                case LIKE:
                    return 2;
                case CHANNELING:
                    return 3;
                default:
                    return -1;
            }
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
