package nanorep.nanowidget;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.ChannelPresenters.NRChannelStrategy;
import nanorep.nanowidget.Components.ChannelPresenters.NRWebContentFragment;
import nanorep.nanowidget.Components.DislikeDialog;
import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRChannelingItem;
import nanorep.nanowidget.Components.NRChannelingView;
import nanorep.nanowidget.Components.NRContentItem;
import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.Components.NRLikeItem;
import nanorep.nanowidget.Components.NRLikeView;
import nanorep.nanowidget.Components.NRResultFragment;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRResultTopView;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Components.NRSuggestionsView;
import nanorep.nanowidget.Components.NRTitleItem;
import nanorep.nanowidget.Components.NRTitleView;
import nanorep.nanowidget.Components.NRViewAdapter;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.Utilities.NRItemAnimator;
import nanorep.nanowidget.Utilities.NRLinearLayoutManager;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRSearchBarListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;
import nanorep.nanowidget.interfaces.OnLikeListener;


public class NRWidgetFragment extends Fragment implements NRSearchBarListener, NRSuggestionsListener, NRResultItemListener, OnLikeListener {
    private Nanorep mNanoRep;

    private NRFetchedDataManager mFetchedDataManager;

    // search bar
    private NRCustomSearchBarView mSearchBar;
    private LinearLayout mSearchBarContainer;

    // suggestion view
    private NRCustomSuggestionsView mSuggestionsView;
    private LinearLayout mSuggestionViewContainer;

    // frame container
    FrameLayout frameLayout;

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
    private boolean autocompleteEnabled = true;

    private RelativeLayout frequentlyQuestions;
    private TextView frequentlyQuestionsTv;

    private RecyclerView.ItemDecoration simpleDividerItemDecoration;

    private NRCustomViewAdapter viewAdapter;

    private NRResultTopView nrResultTopView;

    int y;

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
                nrResultTopView.removeTopView(true);
                fadeViews(nrResultTopView, 1.0f, 500, false);

                mResultsRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSearchStrings().add("");
                        NRResult newResult = new NRResult(result, NRResultItem.RowType.TITLE);
                        newResult.setHeight((int) Calculate.pxFromDp(getContext(), 45));
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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

                final ViewGroup _container = container;
                showSuggestionsView(_container);
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
                    mNotitleViewHolder.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mNoTitleView.setText(mNanoRep.getNRConfiguration().getCustomNoAnswersTextContext(mSearchBar.getText()));
                    rows = mResultStack.get(0);
                }
                frequentlyQuestions.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                loadResults(rows, true);

            }

            @Override
            public void presentSuggestion(String query, ArrayList<String> suggestions) {

                if (!resetSuggestions && mSearchBar.getText().length() - query.length() <= 1 && autocompleteEnabled) {
                    mSuggestionsView.setSuggestions(suggestions);
                }
            }

            @Override
            public void onConnectionFailed(HashMap<String, Object> errorParams) {

            }
        });
        
        setViews(nanoView);

        frameLayout = (FrameLayout) nanoView.findViewById(R.id.frameLayoutFragment);

        nrResultTopView = (NRResultTopView) nanoView.findViewById(R.id.resultTopView);
        setTopView();

        mNotitleViewHolder = (RelativeLayout) nanoView.findViewById(R.id.noTiltleView);
        mNoTitleView = (TextView) nanoView.findViewById(R.id.noTitleTextView);

        frequentlyQuestions = (RelativeLayout) nanoView.findViewById(R.id.frequentlyQuestions);
        frequentlyQuestionsTv = (TextView) nanoView.findViewById(R.id.frequentlyQuestionsTv);

        mResultsRecyclerView = (RecyclerView) nanoView.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new NRLinearLayoutManager(getContext()));

//        simpleDividerItemDecoration = new SeparatorDecoration(getResources().getColor(R.color.nr_background_color), (int) Calculate.dpFromPx(getContext(), 62));
//        mResultsRecyclerView.addItemDecoration(simpleDividerItemDecoration);

        mResultsRecyclerView.setAdapter(mResultsAdapter);
        NRItemAnimator animator = new NRItemAnimator();
        animator.setListener(new NRItemAnimator.OnAnimation() {
            @Override
            public void onItemRemoved(NRResultItem item) {
                item.resetBody();
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

    private void setTopView() {

        nrResultTopView.setListener(this);

        NRCustomTitleView titleView = viewAdapter.getTitle(getContext());

        if(titleView == null){
            titleView = new NRTitleView(getContext());
        }

        NRCustomContentView contentView = viewAdapter.getContent(getContext());

        if(contentView == null){
            contentView = new NRContentView(getContext());
        }

        NRCustomLikeView likeView = viewAdapter.getLikeView(getContext());

        if(likeView == null) {
            likeView = new NRLikeView(getContext());
        }

        NRCustomChannelView channelView = viewAdapter.getChannelView(getContext());

        if (channelView == null) {
            channelView = new NRChannelingView(getContext());
        }

        nrResultTopView.setTitleView(titleView);
        nrResultTopView.setContentView(contentView);
        nrResultTopView.setLikeView(likeView);
        nrResultTopView.setChannelView(channelView);

        nrResultTopView.configViewObjects(mNanoRep.getNRConfiguration());
    }

    private void animateBGColor(int milliseconds, boolean unfold) {

        int colorFrom = getResources().getColor(R.color.nr_background_color);
        int colorTo = getResources().getColor(R.color.white);

//        if(mUnfoldedResult != null && mUnfoldedResult.isUnfolded()) {
        if(!unfold){
            colorFrom = getResources().getColor(R.color.white);
            colorTo = getResources().getColor(R.color.nr_background_color);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(milliseconds); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                frameLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private void setViews(View nanoView) {
        if(viewAdapter == null) {
            viewAdapter  = new NRViewAdapter();
        }

        // search bar
        mSearchBarContainer = (LinearLayout) nanoView.findViewById(R.id.search_bar_container);

        mSearchBar = viewAdapter.getSearchBar(getContext());

        if(mSearchBar == null){
            mSearchBar = new NRSearchBar(getContext());
        }

        mSearchBar.setListener(this);

        mSearchBarContainer.addView(mSearchBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        // suggestion view
        mSuggestionViewContainer = (LinearLayout) nanoView.findViewById(R.id.suggestion_view_container);

        mSuggestionsView = viewAdapter.getSuggestionsView(getContext());

        if(mSuggestionsView == null){
            mSuggestionsView = new NRSuggestionsView(getContext());
        }

        mSuggestionsView.setListener(this);

        mSuggestionViewContainer.addView(mSuggestionsView);
    }

    private void showSuggestionsView(ViewGroup container) {
        if(!mNanoRep.getNRConfiguration().getAutocompleteEnabled()) {
            autocompleteEnabled = false;
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
                            clearResults();
                            removeTopViewShowRecycleView();

                            mResultsRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mUnfoldedResult != null) {
                                        mUnfoldedResult = null;
                                    }
                                    if (mResultStack.size() >= 2) {
                                        loadResults(mResultStack.get(mResultStack.size() - 2), false);

//                                        frequentlyQuestions.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                                        mSearchBar.updateText(getSearchStrings().get(getSearchStrings().size() - 2));
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

        if(frequentlyQuestions.getLayoutParams().height == RelativeLayout.LayoutParams.WRAP_CONTENT) {
            frequentlyQuestions.getLayoutParams().height = 0;
        }

        if (mNotitleViewHolder.getLayoutParams().height == RelativeLayout.LayoutParams.WRAP_CONTENT) {
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

//        if (mQueryResults != null) {
//            ArrayList<NRResult> temp = new ArrayList<>(mQueryResults);
//            for (NRResult queryResult : temp) {
//                int index = mQueryResults.indexOf(queryResult);
//                mQueryResults.remove(queryResult);
//                mResultsAdapter.notifyItemRemoved(index);
//            }
//            temp.clear();
            mQueryResults = null;
//        }
    }

    @Override
    public void searchForText(String text) {
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
        if (mNotitleViewHolder.getLayoutParams().height == RelativeLayout.LayoutParams.WRAP_CONTENT) {
            mNotitleViewHolder.getLayoutParams().height = 0;
        }

        frequentlyQuestions.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;

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

    private void removeTopViewShowRecycleView() {
        nrResultTopView.removeTopView(false);

        if(mUnfoldedResult != null && mUnfoldedResult.isSingle()) {
            animateBGColor(300, false);
            fadeViews(mResultsRecyclerView, 1.0f, 500, false);
            fadeViews(nrResultTopView, 1.0f, 500, false);
        }

        y = 0;

        fadeViews(frequentlyQuestions, 1.0f, 300, false);

        frequentlyQuestionsTv.setText(getString(R.string.frequently_asked_questions));
    }

    @Override
    public void unfoldItem(final NRResult result, boolean clear) {

        if (result.isUnfolded()) { // close answer, show titles..

            removeTopViewShowRecycleView();

            mQueryResults.get(mQueryResults.indexOf(result)).setUnfolded(false);
            mUnfoldedResult = null;

        } else if(!result.isUnfolded()) { // title was clicked, show answer

            int unfoldItemPos = mQueryResults.indexOf(result);

            animateBGColor(unfoldItemPos * 100, true);

            // set the NRResult of the answer we clicked..
            mUnfoldedResult = mQueryResults.get(unfoldItemPos);
            mUnfoldedResult.setUnfolded(true);

//            nrResultTopView.setResult(mUnfoldedResult);

            y = 0;

            if(!mUnfoldedResult.isSingle()) { // get the y coordinate of the selected title
                NRTitleItem titleViewHolder = (NRTitleItem)mResultsRecyclerView.findViewHolderForLayoutPosition(unfoldItemPos);

                int marginTop = titleViewHolder.getTitle_container().getHeight() * unfoldItemPos;

                int offSet = mResultsRecyclerView.computeVerticalScrollOffset();

                int divider = (int) Calculate.pxFromDp(getContext(), 5) * unfoldItemPos;

                y = marginTop - offSet + frequentlyQuestions.getHeight() + divider;

                fadeViews(mResultsRecyclerView, 0.0f, 500, false);
                fadeViews(frequentlyQuestions, 0.0f, 500, false);
            } else {
                fadeViews(mResultsRecyclerView, 0.0f, 50, false);
                fadeViews(frequentlyQuestions, 0.0f, 50, false);
            }

            nrResultTopView.openView(y, mUnfoldedResult);


        }
    }

    private void fadeViews(View view, final float f, long duration, final boolean removeTopTitle) {
        view.animate()
            .alpha(f)
            .setDuration(duration)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(removeTopTitle) {
                        nrResultTopView.removeTitleView();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
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

    @Override
    public void fetchBodyForResult(final NRCustomContentView view, String resultID, Integer resultHash) {
        mFetchedDataManager.faqAnswer(resultID, resultHash, new OnFAQAnswerFetched() {
            @Override
            public void onAnswerFetched(NRQueryResult result) {
                view.loadData(result.getBody(), "text/html", "UTF-8");
            }
        });
    }

    @Override
    public void onFoldItemFinished(boolean beforeGoingDown) {

        if(beforeGoingDown) {
            animateBGColor(100, false);

        } else {
            fadeViews(mResultsRecyclerView, 1.0f, 500, true);
        }
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

                    NRCustomTitleView titleView = viewAdapter.getTitle(getContext());

                    if(titleView == null){
                        titleView = new NRTitleView(getContext());
                    }

                    LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.title_container);

                    titleContainer.addView(titleView);

                    return new NRTitleItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration(), titleView);
                case 1: // content
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);

                    NRCustomContentView contentView = viewAdapter.getContent(getContext());

                    if(contentView == null){
                        contentView = new NRContentView(getContext());
                    }

                    FrameLayout contentContainer = (FrameLayout) view.findViewById(R.id.content_container);

                    contentContainer.addView(contentView);

                    return new NRContentItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration(), contentView);
                case 2: // like
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);

                    NRCustomLikeView likeView = viewAdapter.getLikeView(getContext());

                    if(likeView == null) {
                        likeView = new NRLikeView(getContext());
                    }

                    LinearLayout likeContainer = (LinearLayout) view.findViewById(R.id.like_container);

                    likeContainer.addView(likeView);

                    return new NRLikeItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration(), likeView);
                case 3: // channeling
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channeling_item, parent, false);

                    NRCustomChannelView channelView = viewAdapter.getChannelView(getContext());

                    if(channelView == null) {
                        channelView = new NRChannelingView(getContext());
                    }

                    LinearLayout channelContainer = (LinearLayout) view.findViewById(R.id.channel_container);

                    channelContainer.addView(channelView);

                    return new NRChannelingItem(view, NRWidgetFragment.this, mNanoRep.getNRConfiguration(), channelView);
                default:
                    return null;
            }
        }

        private  int getMaxHeight(int minResultHeight) {
            NRTitleItem titleViewHolder = (NRTitleItem)mResultsRecyclerView.findViewHolderForLayoutPosition(0);
            int titleHeight = titleViewHolder.getTitleHeight();

            if(titleHeight < minResultHeight) {
                titleHeight = minResultHeight;
            } else {
                titleHeight -= 60;
            }

            int maxHeight = mResultsRecyclerView.getHeight();

            return maxHeight - titleHeight;
        }

        @Override
        public void onBindViewHolder(NRResultItem holder, int position) {
            if(holder instanceof NRContentItem) {
                ((NRContentItem) holder).setmMaxHeight(getMaxHeight(mQueryResults.get(position-1).getHeight()));
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

    public void setViewAdapter(NRCustomViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

}
