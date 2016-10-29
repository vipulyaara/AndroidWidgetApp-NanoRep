package nanorep.nanowidget.Components;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.ArrayList;

import nanorep.nanowidget.Components.ChannelPresenters.NRWebContentFragment;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultView;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;
import nanorep.nanowidget.interfaces.OnLikeListener;
import nanorep.nanowidget.interfaces.OnLinkedArticle;


public class NRResultFragment extends Fragment implements View.OnClickListener, OnLikeListener, NRChannelItem.OnChannelSelectedListener, NRContentView.Listener, OnFAQAnswerFetched, NRResultView {

    private NRResult mResult;

    private NRContentView mWebView;
    private RelativeLayout mFeedbackView;
    private NRResultTitleView mTitle;
    private ImageButton mShareButton;
    private NRLikeView mLikeView;
    private NRChannelingView mChannelingView;
    private Listener mListener;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLinkedArticleClicked(String articleId) {
        mListener.onLinkedArticleClicked(this, articleId);
    }

    @Override
    public void onLinkClicked(String url) {
        NRWebContentFragment webContentFragment = NRWebContentFragment.newInstance(url, null);
        webContentFragment.setListener(new NRWebContentFragment.Listener() {
            @Override
            public void onDismiss() {
                getChildFragmentManager().popBackStack();
            }
        });
        getView().findViewById(R.id.linkedArtHolder).setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).add(R.id.linkedArtHolder, webContentFragment).addToBackStack("linked").commit();
    }

    @Override
    public void onAnswerFetched(NRQueryResult result) {
        getView().findViewById(R.id.linkedArtHolder).setVisibility(View.VISIBLE);
        NRLinkedArticleFragment linkedArticleFragment = new NRLinkedArticleFragment();
        linkedArticleFragment.setListener(mListener);
        linkedArticleFragment.setDismissListener(new NRLinkedArticleFragment.OnDismissListener() {
            @Override
            public void onBackClicked() {
                getChildFragmentManager().popBackStack();
            }
        });
        linkedArticleFragment.setQueryResult(result);
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).add(R.id.linkedArtHolder, linkedArticleFragment).addToBackStack("linked").commit();
    }


    public interface Listener extends OnLinkedArticle {
        void onResultFragmentDismissed(NRResultFragment resultFragment);
        void resultFragmentWillDismiss(NRResultFragment resultFragment);
        void fetchBodyForResult(NRResultFragment resultFragment, String resultID);
        void onChannelSelected(NRResultFragment resultFragment, NRChannelItem channelItem);
        void onLikeFailed(String resultId);
    }

    public NRResultFragment() {
        // Required empty public constructor
    }

    public void setResult(NRResult result) {
        mResult = result;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void setLikeState(String resultId, boolean isPositive) {
        if (!isPositive) {
            if (mResult.getFetchedResult().getId().equals(resultId)) {
                mLikeView.resetLikeView();
                mResult.getFetchedResult().setLikeState(NRQueryResult.LikeState.notSelected);
            } else {
                mListener.onLikeFailed(resultId);
            }
        }
    }

    public void setBody(String htmlString) {
        mResult.getFetchedResult().setBody(htmlString);
        mWebView.loadData(htmlString, "text/html", "UTF-8");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nrresult, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
            mTitle = (NRResultTitleView) view.findViewById(R.id.titleView);
            if (mTitle != null) {
                mTitle.setTitle(mResult.getFetchedResult().getTitle());
                mTitle.setListener(new NRResultTitleView.Listener() {
                    @Override
                    public void onSharePressed() {

                    }
                });
            }

            mShareButton = (ImageButton) view.findViewById(R.id.shareButton);
            if (mShareButton != null) {
                mShareButton.setOnClickListener(NRResultFragment.this);
            }

            mWebView = (NRContentView) view.findViewById(R.id.resultWebView);
            if (mWebView != null) {
                mWebView.setListener(NRResultFragment.this);
                if (mResult.getFetchedResult().getBody() != null) {
                    setBody(mResult.getFetchedResult().getBody());
                } else {
                    mListener.fetchBodyForResult(NRResultFragment.this, mResult.getFetchedResult().getId());
                }
            }

            mLikeView = (NRLikeView) view.findViewById(R.id.likeView);
            if (mLikeView != null) {
                if (mResult.getFetchedResult().getLikeState() != NRQueryResult.LikeState.notSelected) {
                    mLikeView.updateLikeButton(mResult.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
                }
                mLikeView.setListener(NRResultFragment.this);
            }
            mFeedbackView = (RelativeLayout) view.findViewById(R.id.feedbackView);
            if (mFeedbackView != null) {
                RelativeLayout.LayoutParams params = null;
                if (mResult.getFetchedResult().getChanneling() == null) {
                    params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                    params.height = (int) Calculate.pxFromDp(getContext(), 50);
                }else {
                    mChannelingView = (NRChannelingView) view.findViewById(R.id.channelingView);
                    if (mChannelingView != null) {
                        mChannelingView.setListener(NRResultFragment.this);
                        ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();
                        for (NRChanneling channeling: channelings) {
                            channeling.setQueryResult(mResult.getFetchedResult());
                        }
                        mChannelingView.setChannelings(channelings);
                        params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                        params.height = (int) Calculate.pxFromDp(getContext(), 100);
                    }
                }
                mFeedbackView.setLayoutParams(params);
            }
            }
        });
    }




    @Override
    public void onLikeClicked(NRLikeView likeView, String resultId, boolean isLike) {
//        if (mLikeView.getLikeSelection()) {
//            mResult.getFetchedResult().setLikeState(NRQueryResult.LikeState.positive);
//            mListener.onLikeSelected(this, NRLikeType.POSITIVE, mResult.getFetchedResult());
//        } else {
//            String reasons[] = new String[] {"Incorrect answer", "Missing or incorrect information", "Didn't find what I was looking for"};
//            DislikeDialog dislikeAlert = new DislikeDialog(getContext());
//            dislikeAlert.openView("What's wrong with this answer");
//            dislikeAlert.setListener(new DislikeDialog.Listener() {
//                @Override
//                public void onCancel() {
//                    mLikeView.resetLikeView();
//                }
//
//                @Override
//                public void onDislike(NRLikeType type) {
//                    mResult.getFetchedResult().setLikeState(NRQueryResult.LikeState.negative);
//                    mListener.onLikeSelected(NRResultFragment.this, type, mResult.getFetchedResult());
//                }
//            });
//            dislikeAlert.setDislikeOptions(reasons);
//        }
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        mListener.onChannelSelected(this, channelItem);
    }
}
