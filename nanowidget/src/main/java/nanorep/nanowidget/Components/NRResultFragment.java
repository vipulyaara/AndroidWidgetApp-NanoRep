package nanorep.nanowidget.Components;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.RequestParams.NRLikeType;
import com.nanorep.nanoclient.Response.NRHtmlParser;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.OnLikeListener;



public class NRResultFragment extends Fragment implements View.OnClickListener, OnLikeListener, NRChannelItem.OnChannelSelectedListener {

    private NRResult mResult;

    private WebView mWebView;
    private RelativeLayout mFeedbackView;
    private NRResultTitleView mTitle;
    private ImageButton mShareButton;
    private NRLikeView mLikeView;
    private NRChannelingView mChannelingView;
    private Listener mListener;
    private View mView;

    @Override
    public void onClick(View v) {

    }


    public interface Listener {
        void onResultFragmentDismissed(NRResultFragment resultFragment);
        void resultFragmentWillDismiss(NRResultFragment resultFragment);
        void onLikeSelected(NRResultFragment resultFragment, NRLikeType likeType, NRResult currentResult);
        void fetchBodyForResult(NRResultFragment resultFragment, String resultID);
        void onChannelSelected(NRResultFragment resultFragment, NRChannelItem channelItem);
        void onLinkedArticleClicked(String articleId);
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

    public void setLikeState(boolean isPositive) {
        mLikeView.updateLikeButton(isPositive);
    }

    public void setBody(String htmlString) {
        NRHtmlParser parser = new NRHtmlParser(htmlString);
        String parsed = parser.getParsedHtml();
        mResult.getFetchedResult().setBody(htmlString);
        mWebView.loadData(parsed, "text/html", "UTF-8");
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

                mWebView = (WebView) view.findViewById(R.id.resultWebView);
                if (mWebView != null) {
                    mWebView.getSettings().setJavaScriptEnabled(true);
//                    mWebView.getSettings().setLoadWithOverviewMode(true);
//                    mWebView.getSettings().setUseWideViewPort(true);
                    mWebView.setWebViewClient(new NRWebClient());
                    if (mResult.getFetchedResult().getBody() != null) {
                        setBody(mResult.getFetchedResult().getBody());
                    } else {
                        mListener.fetchBodyForResult(NRResultFragment.this, mResult.getFetchedResult().getId());
                    }
                }

                mLikeView = (NRLikeView) view.findViewById(R.id.likeView);
                if (mLikeView != null) {
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
                mView = view;

//                view.setX(view.getMeasuredWidth());
//                view.animate().translationXBy(-view.getMeasuredWidth()).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            }
        });
    }




    @Override
    public void onLikeClicked() {
        if (mLikeView.getLikeSelection()) {
            mListener.onLikeSelected(this, NRLikeType.POSITIVE, mResult);
        } else {
            String reasons[] = new String[] {"Incorrect answer", "Missing or incorrect information", "Didn't find what I was looking for"};
            AlertDialog.Builder dislikeAlert = new  AlertDialog.Builder(getContext());
            dislikeAlert.setTitle("What's wrong with this answer");
            final NRLikeAdapter adapter = new NRLikeAdapter(getContext(), R.layout.dislike_row, reasons);
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE && adapter.getSelection() != NRLikeType.POSITIVE) {
                        mListener.onLikeSelected(NRResultFragment.this, adapter.getSelection(), mResult);
                    } else {
                        mLikeView.cancelLike();
                    }
                }
            };
            dislikeAlert.setAdapter(adapter, null);
            dislikeAlert.setPositiveButton("OK", dialogClickListener);
            dislikeAlert.setNegativeButton("Cancel", dialogClickListener);
            AlertDialog alert = dislikeAlert.create();
            alert.show();
        }
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        mListener.onChannelSelected(this, channelItem);
    }

    private class NRLikeAdapter extends ArrayAdapter<String> implements View.OnClickListener {
        private String[] mObjects;
        private ArrayList<ImageView> bullets;
        private NRLikeType mSelection = NRLikeType.POSITIVE;

        public NRLikeAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            mObjects = objects;
        }

        public NRLikeType getSelection() {
            return mSelection;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dislike_row, null);
                convertView.setTag(position);
                convertView.setOnClickListener(this);
                TextView titleView = (TextView) convertView.findViewById(R.id.dislike);
                titleView.setText(mObjects[position]);
                if (bullets == null) {
                    bullets = new ArrayList<>();
                }
                bullets.add((ImageView) convertView.findViewById(R.id.imageView));
            }
            return convertView;
        }

        private int resId(String resName) {
            return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
        }


        @Override
        public void onClick(View v) {
            switch ((int)v.getTag()) {
                case 0:
                    mSelection = NRLikeType.INCORRECT_ANSWER;
                    break;
                case 1:
                    mSelection = NRLikeType.MISSING_INFORMATION;
                    break;
                case 2:
                    mSelection = NRLikeType.IRRELEVANT;
                    break;
            }
            for (int i = 0; i < bullets.size(); i++) {
                int id;
                if ((int) v.getTag() == i) {
                    id = resId("bullet_on");
                } else {
                    id = resId("bullet_off");
                }
                bullets.get(i).setImageResource(id);
            }
        }
    }

    private class NRWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return linkedArticle(url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return linkedArticle(request.getUrl().toString());
        }

        private boolean linkedArticle(String link) {
            if (link.startsWith("nanorep")) {
                String comps[] = link.split("/");
                if (comps != null && comps.length > 0) {
                    mListener.onLinkedArticleClicked(comps[comps.length - 1]);
                }
                return true;
            }
            return false;
        }
    }
}
