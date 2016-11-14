package nanorep.nanowidget.Fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.NRImpl;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRLikeType;
import com.nanorep.nanoclient.Response.NRAnswer;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.ChannelPresenters.NRChannelStrategy;
import nanorep.nanowidget.Components.DislikeDialog;
import nanorep.nanowidget.Components.MyWebView;
import nanorep.nanowidget.Components.NRCategoriesView;
import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRChannelingView;
import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.Components.NRLikeView;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRResultTopView;
import nanorep.nanowidget.Components.NRResultsView;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Components.NRSuggestionsView;
import nanorep.nanowidget.Components.NRTitleView;
import nanorep.nanowidget.Components.NRViewAdapter;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.DataClasse.NRResultsAdapter;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.Utilities.NRWebClient;
import nanorep.nanowidget.interfaces.NRConfigFetcherListener;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.NRSearchBarListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;

/**
 * Created by noat on 06/11/2016.
 */

public class NRMainFragment extends Fragment implements NRSearchBarListener, NRSuggestionsListener,
                                                        NRCategoriesView.Listener, NRResultsView.Listener,
                                                        NRContentView.Listener,
                                                        NRChannelItem.OnChannelSelectedListener,
                                                        NRResultTopView.Listener{

    public static final String TAG = NRMainFragment.class.getName();
    private static final int NO_TITLE_HEIGHT = 100;

    private NRFetchedDataManager mFetchedDataManager;

    private NRCustomViewAdapter viewAdapter;

    private RelativeLayout mLoadingView;

    private RelativeLayout fragmentMainLayout;

    private FrameLayout contentMain;

    // search bar
    private LinearLayout searchBarContainer;
    private NRCustomSearchBarView searchBarView;

    // suggestion view
    private NRCustomSuggestionsView mSuggestionsView;
    private LinearLayout mSuggestionViewContainer;

    private NRCategoriesView categoriesView;

    private boolean resetSuggestions = false;

    private boolean autocompleteEnabled = true;

    private View.OnKeyListener onKeyListener;

    // no answer
    private TextView mNoTitleView;
    private RelativeLayout mNotitleViewHolder;


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
    public void closeAnswer() {
        // get the last view
        View view = contentMain.getChildAt(contentMain.getChildCount() - 1);

//        if(view  instanceof NRResultTopView && !((NRResultTopView)view).getmResult().isSingle()) {
//            ((NRResultTopView)view).setResultUnFoldState(false);
//            ((NRResultTopView)view).removeTopView(false);
//        }

        view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left));

        contentMain.removeView(view);
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        String url = NRChannelStrategy.presentor(getContext(), channelItem.getChanneling(), NRImpl.getInstance()).getUrl();
        if (url != null) {
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
            getView().requestFocus();
        }
    }

    @Override
    public void onLinkedArticleClicked(String articleId) {
        mFetchedDataManager.faqAnswer(articleId, null, new OnFAQAnswerFetched() {
            @Override
            public void onAnswerFetched(final NRQueryResult result) {

                NRResult newResult = new NRResult(result, NRResultItem.RowType.TITLE);
                newResult.setHeight((int) Calculate.pxFromDp(getContext(), NRFetchedDataManager.ROW_HEIGHT));

                NRResultTopView resultTopView = getTopView();
                contentMain.addView(resultTopView);

                resultTopView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left));

                resultTopView.openOpenedView(newResult);

                getView().requestFocus();
            }
        });
    }

    @Override
    public void onLinkClicked(String url) {
        MyWebView webView = new MyWebView(getContext(), url, new MyWebView.Listener() {
            @Override
            public void onDismiss() {
                contentMain.removeViewAt(contentMain.getChildCount() - 1);
                getView().requestFocus();
            }
        });

        contentMain.addView(webView);
    }

    @Override
    public void onLikeClicked(final NRResultTopView view,final NRLikeView likeView, String resultId, boolean isLike) {
        final NRResult result = view.getmResult();

        if (isLike) {
            result.getFetchedResult().setLikeState(NRQueryResult.LikeState.positive);
            mFetchedDataManager.sendLike(NRLikeType.POSITIVE, result.getFetchedResult(), new Nanorep.OnLikeSentListener() {
                @Override
                public void onLikeSent(String resultId, int type, boolean success) {
                    if(!success) {
                        view.getmResult().getFetchedResult().setLikeState(NRQueryResult.LikeState.notSelected);
                        likeView.resetLikeView();
                    }
                }
            });
        } else {
            openDislikeDialog(result, view, likeView);
        }
    }

    private void openDislikeDialog(final NRResult result, final NRResultTopView view, final NRLikeView likeView) {
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
                result.getFetchedResult().setLikeState(NRQueryResult.LikeState.negative);
                mFetchedDataManager.sendLike(type, result.getFetchedResult(), new Nanorep.OnLikeSentListener() {
                    @Override
                    public void onLikeSent(String resultId, int type, boolean success) {
                        if(!success) {
                            view.getmResult().getFetchedResult().setLikeState(NRQueryResult.LikeState.notSelected);
                            likeView.resetLikeView();
                        }
                    }
                });
            }
        });
        dislikeAlert.setDislikeOptions(reasons);
    }

    public static NRMainFragment newInstance() {

        Bundle args = new Bundle();

        NRMainFragment fragment = new NRMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof NRCustomViewAdapter) {
            viewAdapter = (NRCustomViewAdapter) context;
        }
    }

    private void initDataManager() {
        mFetchedDataManager = new NRFetchedDataManager(getContext(), new NRConfigFetcherListener() {
            @Override
            public void onConfigurationReady() {
                updateTitleNormalText();
                updateSearchBar();
                showSuggestionsView();
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void insertRows(ArrayList<NRFAQGroupItem> groups) {

                if(groups.size() > 1) {

                    categoriesView = new NRCategoriesView(getActivity());
                    categoriesView.setListener(NRMainFragment.this);
                    categoriesView.setCategories(groups, viewAdapter);

                    contentMain.addView(categoriesView);
                } else if(groups.size() == 1){
                    // show results view immidiately
                    openNRResultView(mFetchedDataManager.generateNRResultArray(groups.get(0).getAnswers(), getContext()));
                }
            }
        });

        mFetchedDataManager.setFetcherListener(new NRFetcherListener() {
            @Override
            public void reloadWithAimation() {

            }

            @Override
            public void reload() {

            }

            @Override
            public void insertRows(ArrayList<NRResult> results) {

                if(results == null || (results != null && results.size() == 0)){
                    if (searchBarView.getText() != null) {
                        mNotitleViewHolder.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), NO_TITLE_HEIGHT);
                        mNoTitleView.setText(NRImpl.getInstance().getNRConfiguration().getCustomNoAnswersTextContext(searchBarView.getText()));
                        getView().requestFocus();
                    }
                } else if(results.size() == 1) {
                    NRResultTopView resultTopView = getTopView();

                    contentMain.setVisibility(View.VISIBLE);
                    contentMain.addView(resultTopView);

                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    for(int i = 0; i <= contentMain.getChildCount() - 2; i++) {
                                        View view = contentMain.getChildAt(i);
                                        view.setVisibility(View.INVISIBLE);
                                    }
//                                    contentMain.getChildAt(contentMain.getChildCount() -2).setVisibility(View.INVISIBLE);
                                    contentMain.getChildAt(contentMain.getChildCount() -1).setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    for(int i = 0; i <= contentMain.getChildCount() - 2; i++) {
                                        View view = contentMain.getChildAt(i);
                                        view.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                    resultTopView.setAnimation(animation);

                    resultTopView.startAnimation(animation);

                    resultTopView.openOpenedView(results.get(0));

                    getView().requestFocus();
                } else if(results.size() > 1){
                    // show results View
                    contentMain.setVisibility(View.VISIBLE);

                    openNRResultView(results);
                }
            }

            @Override
            public void presentSuggestion(String querytext, ArrayList<Spannable> suggestions) {
                if (!resetSuggestions && searchBarView.getText().length() - querytext.length() <= 1 && autocompleteEnabled) {
                    mSuggestionsView.setSuggestions(suggestions);

                    // hide content view
                    contentMain.setVisibility(View.INVISIBLE);

//                    searchBarView.requestFocus();
//                    searchBarView.setOnKeyListener(onKeyListener);
                }
            }

            @Override
            public void onConnectionFailed(HashMap<String, Object> errorParams) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(viewAdapter == null) {
            viewAdapter  = new NRViewAdapter();
        }

    }

    private NRResultTopView getTopView() {

        NRResultTopView resultTopView = new NRResultTopView(getActivity());

        resultTopView.setListener(this);

        NRCustomTitleView titleView = viewAdapter.getTitle(getContext());

        if(titleView == null){
            titleView = new NRTitleView(getContext());
            ((NRTitleView)titleView).configViewObjects(NRImpl.getInstance().getNRConfiguration());
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

        resultTopView.setTitleView(titleView);
        resultTopView.setContentView(contentView, this);
        resultTopView.setLikeView(likeView);
        resultTopView.setChannelView(channelView, this);

        return resultTopView;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        mLoadingView = (RelativeLayout) view.findViewById(R.id.fragment_place_holder);

        contentMain = (FrameLayout) view.findViewById(R.id.content_main);

        fragmentMainLayout = (RelativeLayout) view.findViewById(R.id.fragment_main_layout);

        mNotitleViewHolder = (RelativeLayout) view.findViewById(R.id.noTiltleView);
        mNoTitleView = (TextView) view.findViewById(R.id.noTitleTextView);

        setViews(view);

        initDataManager();

        return view;
    }

    private void updateSearchBar() {
        NRConfiguration.NRTitle titleConfig = NRImpl.getInstance().getNRConfiguration().getTitle();
        NRConfiguration.NRSearchBar searchBarConfig = NRImpl.getInstance().getNRConfiguration().getSearchBar();

        searchBarView.setHint(searchBarConfig.getInitialText());

        String titleBGColor = "#0aa0ff";

        // titleConfig color
        if(!isEmpty(titleConfig.getTitleBGColor())) {
            titleBGColor = titleConfig.getTitleBGColor();
        }

        searchBarView.setBackgroundColor(Color.parseColor(titleBGColor));
    }

    private void updateTitleNormalText() {
        NRConfiguration.NRTitle title = NRImpl.getInstance().getNRConfiguration().getTitle();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        View customActionBar = getActivity().getLayoutInflater().inflate(R.layout.nr_title_bar, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(customActionBar, layout);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);

        TextView tv = (TextView) actionBar.getCustomView().findViewById(R.id.titleBarTv);

        // titleConfig text
        String titleText = getString(R.string.default_title);

        if(!isEmpty(title.getTitle())) {
            titleText = title.getTitle();
        }

        tv.setText(titleText);

        // titleConfig color
        if(!isEmpty(title.getTitleColor())) {
            String titleColor = title.getTitleColor();
            tv.setTextColor(Color.parseColor(titleColor));
        }

        // titleConfig background color
        if(!isEmpty(title.getTitleBGColor())) {
            String titleBGColor = title.getTitleBGColor();
            RelativeLayout relativeLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.nrTitleBarLayout);
            relativeLayout.setBackgroundColor(Color.parseColor(titleBGColor));
        }

        // titleConfig font
        if(!isEmpty(title.getTitleFont())) {
            String titleFont = title.getTitleFont();
            tv.setTypeface(Typeface.create(titleFont, Typeface.NORMAL));
        }

    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }


    private void setViews(View nanoView) {

        // suggestion view
        mSuggestionViewContainer = (LinearLayout) nanoView.findViewById(R.id.suggestion_view_container);

        mSuggestionsView = viewAdapter.getSuggestionsView(getContext());

        if(mSuggestionsView == null){
            mSuggestionsView = new NRSuggestionsView(getContext());
        }

        mSuggestionsView.setListener(this);

        mSuggestionViewContainer.addView(mSuggestionsView);


        // search bar
        searchBarContainer = (LinearLayout) nanoView.findViewById(R.id.search_bar_container);

        searchBarView = viewAdapter.getSearchBar(getContext());

        if (searchBarView == null) {
            searchBarView = new NRSearchBar(getContext());
        }

        searchBarView.setListener(this);

        searchBarContainer.addView(searchBarView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onStartRecording(ImageButton button) {

    }

    @Override
    public void fetchSuggestionsForText(String text) {
        hideNoTitleView();

        resetSuggestions = false;
        mFetchedDataManager.searchSuggestion(text);
    }

    @Override
    public void searchForText(String text) {
        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);

        mFetchedDataManager.searchText(text);
    }

    @Override
    public void onClearClicked(boolean byUser) {

        if (mNotitleViewHolder.getLayoutParams().height > 0) {
            if(!(contentMain.getChildAt(contentMain.getChildCount()-1) instanceof NRResultsView)) {
                contentMain.removeViewAt(contentMain.getChildCount() - 1);
            }

            mNotitleViewHolder.getLayoutParams().height = 0;
        }

        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);

        while (contentMain.getChildCount() > 1) {
            contentMain.removeViewAt(contentMain.getChildCount() - 1);
        }

        // show content
        contentMain.setVisibility(View.VISIBLE);

    }

    private void hideNoTitleView() {
        if (mNotitleViewHolder.getLayoutParams().height > 0) {
            mNotitleViewHolder.getLayoutParams().height = 0;
        }
    }

    @Override
    public void onSelectSuggestion(String suggestion) {
        resetSuggestions = true;

        searchBarView.dismissKeyboard();
        searchBarView.updateEditTextView(suggestion);

        resetSuggestions = true;
        mSuggestionsView.setSuggestions(null);

        mFetchedDataManager.searchText(suggestion);
    }

    private void showSuggestionsView() {
        if(!NRImpl.getInstance().getNRConfiguration().getAutocompleteEnabled()) {
            autocompleteEnabled = false;
        }
    }

    @Override
    public void onCategorySelected(NRFAQGroupItem groupItem) {
        openNRResultView(mFetchedDataManager.generateNRResultArray(groupItem.getAnswers(), getContext()));
    }

    private void openNRResultView(ArrayList<NRResult> results) {

        NRResultsView resultsView = new NRResultsView(getActivity());
        resultsView.setListener(NRMainFragment.this);
        resultsView.setResults(results, viewAdapter);

        contentMain.addView(resultsView);

        if (getView() != null) {
            getView().requestFocus();
        }
    }

    @Override
    public void onResultSelected(int y, NRResultsAdapter.ViewHolder titleViewHolder) {
        NRResultTopView resultTopView = getTopView();

        // get last results view and fade it when answer is opened in animation

        contentMain.addView(resultTopView);

        resultTopView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left));

        animateBGColor(500, resultTopView, true, titleViewHolder);

        NRResult result = titleViewHolder.getResult();

        result.setUnfolded(true);
//        resultTopView.openView(y, result);

        resultTopView.openOpenedView(result);

        getView().requestFocus();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        searchBarView.updateEditTextView("");

                        if(NRMainFragment.this.getChildFragmentManager().getBackStackEntryCount() > 0) {
                            // channel is opened

                            NRMainFragment.this.getChildFragmentManager().popBackStack();
                            (getView().findViewById(R.id.fragment_place_holder)).setVisibility(View.INVISIBLE);
                            getView().requestFocus();
                            return true;

                        } else if(mSuggestionsView.getHeight() > 0) {
                            resetSuggestions = true;
                            mSuggestionsView.setSuggestions(null);
                            contentMain.setVisibility(View.VISIBLE);
                            return true;
                        } else if(contentMain.getChildCount() > 1) {

                            // get the last view
                            View view = contentMain.getChildAt(contentMain.getChildCount() - 1);

                            if(view  instanceof NRResultTopView) {// && !((NRResultTopView)view).getmResult().isSingle()) { // opened from results lists
//                                ((NRResultTopView)view).setResultUnFoldState(false);
//                                ((NRResultTopView)view).removeTopView(false);

                                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left));

                                //contentMain.removeViewAt(contentMain.getChildCount() - 1);

                                //updateSearchBarTextForResultTop(view);

                            } //else {

                                contentMain.removeViewAt(contentMain.getChildCount() - 1);

                                View currentView = contentMain.getChildAt(contentMain.getChildCount() - 1);
                                if(currentView instanceof NRResultTopView){

                                    updateSearchBarTextForResultTop(currentView);
                                } else {
                                    searchBarView.updateEditTextView("");
                                    getView().requestFocus();
                                }
                            //}

                            return true;
                        } else {
                            return false;
                        }

                    } else { // if (keyCode == KeyEvent.KEYCODE_BACK)
                        return false;
                    }
                } else { // if (event.getAction() == KeyEvent.ACTION_DOWN)
                    return false;
                }
            }
        };

        getView().setOnKeyListener(onKeyListener);
    }

    private void updateSearchBarTextForResultTop(View view) {
        if(((NRResultTopView)view).getmResult().getFetchedResult() instanceof NRAnswer) {
            searchBarView.updateEditTextView(((NRResultTopView)view).getmResult().getFetchedResult().getTitle());
            resetSuggestions = true;
            mSuggestionsView.setSuggestions(null);
            contentMain.setVisibility(View.VISIBLE);
            getView().requestFocus();
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
                            contentMain.removeViewAt(contentMain.getChildCount() - 1);
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

    @Override
    public void onFoldItemFinished(boolean beforeGoingDown) {
        if(beforeGoingDown) {
            animateBGColor(50, contentMain.getChildAt(contentMain.getChildCount() -1), false, null);

        } else {
            fadeViews(contentMain.getChildAt(contentMain.getChildCount()-1), 0.0f, 500, true);
        }
    }

    private void animateBGColor(int milliseconds, final View view, final boolean unfold, final NRResultsAdapter.ViewHolder titleViewHolder) {

        int colorFrom = getResources().getColor(R.color.nr_background_color);
        int colorTo = getResources().getColor(R.color.white);

        if(!unfold){
            colorFrom = Color.WHITE;
            colorTo = getResources().getColor(R.color.nr_background_color);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(milliseconds); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(unfold) {
                    titleViewHolder.getTitleView().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if(unfold) {
            colorAnimation.setStartDelay(300);
        }
        colorAnimation.start();
    }

}
