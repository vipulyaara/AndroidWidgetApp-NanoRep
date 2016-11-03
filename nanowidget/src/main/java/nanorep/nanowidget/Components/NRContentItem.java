package nanorep.nanowidget.Components;

import android.view.View;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRAnswer;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRViewHolder;

/**
 * Created by nissimpardo on 30/08/2016.
 */

public class NRContentItem extends NRResultItem  {
    private NRWebView mWebView;
    private RelativeLayout mFeedbackView;
    private NRLikeView mLikeView;
    private NRChannelingView mChannelingView;
    private NRResultItemListener mListener;
    private NRResult mResult;
    private String mDomain = "http://my.nanorep.com";


    public NRContentItem(View itemView, int height) {
        super(itemView);
        itemView.getLayoutParams().height = height - (int) Calculate.pxFromDp(itemView.getContext(), 80);
        mWebView = (NRWebView) itemView.findViewById(R.id.cv_webview);
        mFeedbackView = (RelativeLayout) itemView.findViewById(R.id.cv_feedbackView);
        mLikeView = (NRLikeView) itemView.findViewById(R.id.cv_likeView);
        mChannelingView = (NRChannelingView) itemView.findViewById(R.id.cv_channelingView);
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public void setListener(NRResultItemListener listener) {
        mListener = listener;
        mWebView.setListener(listener);
        mLikeView.setListener(listener);
        mChannelingView.setListener(listener);
    }

    public void setBody(String body) {
        mResult.getFetchedResult().setBody(body);
        mWebView.loadData(body, "text/html", "UTF-8", mDomain);
    }

    public void setChanneling(ArrayList<NRChanneling> channelings) {
        mResult.getFetchedResult().setChanneling(channelings);
        if (mFeedbackView != null) {
            RelativeLayout.LayoutParams params = null;
            if (channelings == null) {
                params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                params.height = (int) Calculate.pxFromDp(itemView.getContext(), 50);
            }else {
                if (mChannelingView != null) {
//                    ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();
                    for (NRChanneling channeling: channelings) {
                        channeling.setQueryResult(mResult.getFetchedResult());
                    }
                    mChannelingView.setChannelings(channelings);
                    params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                    params.height = (int) Calculate.pxFromDp(itemView.getContext(), 100);
                }
            }
            mFeedbackView.setLayoutParams(params);
            mFeedbackView.requestLayout();
        }
    }

    public void resetBody() {
        mWebView.loadUrl("about:blank");
    }

    public void setResult(NRResult result) {
        mResult = result;
        mLikeView.setResultId(result.getFetchedResult().getId());
        if (result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
            mLikeView.resetLikeView();
        } else {
            mLikeView.updateLikeButton(result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
        }
        if (mResult.getFetchedResult() instanceof NRAnswer) {
            setBody(mResult.getFetchedResult().getBody());
        } else {
            mListener.fetchBodyForResult(this, mResult.getFetchedResult().getId(), mResult.getFetchedResult().getHash());
            return;
        }
        if (mResult.getFetchedResult().getChanneling() != null) {
            setChanneling(mResult.getFetchedResult().getChanneling());
        } else {
            RelativeLayout.LayoutParams params = null;
            params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
            params.height = (int) Calculate.pxFromDp(itemView.getContext(), 50);
            mFeedbackView.setLayoutParams(params);
            mFeedbackView.requestLayout();
        }
    }

    public NRViewHolder.RowType getRowType() {
        return NRViewHolder.RowType.unfolded;
    }
}
