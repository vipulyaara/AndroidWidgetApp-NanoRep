package nanorep.nanowidget.Components;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;

import nanorep.nanowidget.Components.ChannelPresenters.NRWebContentFragment;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultView;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;
import nanorep.nanowidget.interfaces.OnLikeListener;
import nanorep.nanowidget.interfaces.OnLinkedArticle;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NRLinkedArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NRLinkedArticleFragment extends Fragment implements NRWebView.Listener, OnFAQAnswerFetched, NRChannelItem.OnChannelSelectedListener, Nanorep.OnLikeSentListener, NRResultView, OnLikeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnLinkedArticle mListener;
    private NRResultTitleView mTitleView;
    private NRWebView mWebView;
    private NRLinkedArticlesBrowserView mBrowserView;
    private ArrayList<NRQueryResult> mLinkedArticles = new ArrayList<>();
    private int mIndex;
    private LinearLayout mFeedbackView;
    private NRChannelingView mChannelingView;
    private NRLikeView mLikeView;
    private Button mBackButton;
    private OnDismissListener mDismissListener;
    private NRQueryResult mResult;

    @Override
    public void onLikeSent(String resultId, int type, boolean success) {

    }

    public interface OnDismissListener {
        void onBackClicked();
    }

    public NRLinkedArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NRLinkedArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NRLinkedArticleFragment newInstance(String param1, String param2) {
        NRLinkedArticleFragment fragment = new NRLinkedArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setQueryResult(NRQueryResult queryResult) {
        mIndex = 0;
        mLinkedArticles.add(queryResult);
    }

    public void setDismissListener(OnDismissListener listener) {
        mDismissListener = listener;
    }

    public void setListener(OnLinkedArticle listener) {
        mListener = listener;
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
        View view = inflater.inflate(R.layout.fragment_nrlinked_article, container, false);
        mTitleView = (NRResultTitleView) view.findViewById(R.id.linkedArtTitle);
        mBackButton = (Button) view.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDismissListener.onBackClicked();
            }
        });
        mWebView = (NRWebView) view.findViewById(R.id.linkedArtWebView);
        mWebView.setListener(this);
        mFeedbackView = (LinearLayout) view.findViewById(R.id.linkedArtFeedback);
        mLikeView = (NRLikeView) view.findViewById(R.id.linkedArtLikeView);
        mLikeView.setListener(this);
        mBrowserView = (NRLinkedArticlesBrowserView) view.findViewById(R.id.linkedArtBrowser);
        mBrowserView.setListener(new NRLinkedArticlesBrowserView.Listener() {
            @Override
            public void onNextClicked() {
                if (mLinkedArticles.size() > 1) {
                    mIndex++;
                    updateArticle(mLinkedArticles.get(mIndex));
                }
            }

            @Override
            public void onPrevClicked() {
                if (mLinkedArticles.size() > 1) {
                    mIndex--;
                    updateArticle(mLinkedArticles.get(mIndex));
                }
            }
        });
        updateArticle(mLinkedArticles.get(0));

        if (mFeedbackView != null) {
            RelativeLayout.LayoutParams params = null;
            if (mResult.getChanneling() == null) {
                params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                params.height = (int) Calculate.pxFromDp(getContext(), 50);
            }else {
                mChannelingView = (NRChannelingView) view.findViewById(R.id.linkedArtChanneling);
                if (mChannelingView != null) {
                    mChannelingView.setListener(this);
                    ArrayList<NRChanneling> channelings = mResult.getChanneling();
                    for (NRChanneling channeling: channelings) {
                        channeling.setQueryResult(mResult);
                    }
                    mChannelingView.setChannelings(channelings);
                    params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                    params.height = (int) Calculate.pxFromDp(getContext(), 100);
                }
            }
            mFeedbackView.setLayoutParams(params);
        }
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateArticle(NRQueryResult result) {
        mResult = result;
        mTitleView.setTitle(result.getTitle());
        mWebView.loadData(result.getBody(), "html/text", "UTF-8", mParam1);
        if (mLinkedArticles.size() > 1) {
            if (mIndex < mLinkedArticles.size() - 1 && mIndex > 0) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasNextAndPrev);
            } else if (mIndex == 0) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasNext);
            } else if (mIndex == mLinkedArticles.size() - 1) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasPrev);
            }
        }
        if (result.getLikeState() == NRQueryResult.LikeState.notSelected) {
            mLikeView.resetLikeView();
        } else {
            mLikeView.updateLikeButton(result.getLikeState() == NRQueryResult.LikeState.positive);
        }
    }

    @Override
    public void onAnswerFetched(NRQueryResult result) {
        if (result != null) {
            mIndex++;
            mLinkedArticles.add(mIndex, result);
            updateArticle(result);
        }
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
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).add(R.id.webContentHolder, webContentFragment).addToBackStack("linked").commit();
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {

    }

    @Override
    public void onLikeClicked(NRLikeView likeView, String resultId, boolean isLike) {
        if (mLikeView.getLikeSelection()) {
            mLinkedArticles.get(mIndex).setLikeState(NRQueryResult.LikeState.positive);
            mListener.onLikeSelected(this, NRLikeType.POSITIVE, mLinkedArticles.get(mIndex));
        } else {
            String reasons[] = new String[] {"Incorrect answer", "Missing or incorrect information", "Didn't find what I was looking for"};
            DislikeDialog dislikeAlert = new DislikeDialog(getContext());
            dislikeAlert.setTitle("What's wrong with this answer");
            dislikeAlert.setListener(new DislikeDialog.Listener() {
                @Override
                public void onCancel() {
                    mLikeView.resetLikeView();
                }

                @Override
                public void onDislike(NRLikeType type) {
                    mLinkedArticles.get(mIndex).setLikeState(NRQueryResult.LikeState.negative);
                    mListener.onLikeSelected(NRLinkedArticleFragment.this, type, mLinkedArticles.get(mIndex));
                }
            });
            dislikeAlert.setDislikeOptions(reasons);
        }
    }

    @Override
    public void setLikeState(String resultId, boolean isPositive) {
        if (isPositive) {
            mLinkedArticles.get(mIndex).setLikeState(mLikeView.getLikeSelection() ? NRQueryResult.LikeState.positive : NRQueryResult.LikeState.negative);
        } else {
            mLinkedArticles.get(mIndex).setLikeState(NRQueryResult.LikeState.notSelected);
            mLikeView.resetLikeView();
        }
    }
}
