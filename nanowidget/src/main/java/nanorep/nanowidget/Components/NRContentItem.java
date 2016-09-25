package nanorep.nanowidget.Components;

import android.view.View;
import android.view.ViewGroup;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nissimpardo on 30/08/2016.
 */

public class NRContentItem extends NRResultItem  {
    private NRWebView mWebView;
    private int mMaxHeight;

    public NRContentItem(View view, NRResultItemListener listener, NRConfiguration config, int maxHeight) {
        super(view, listener, config);

        mMaxHeight = maxHeight;
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void bindViews(View view) {

        mWebView = (NRWebView) itemView.findViewById(R.id.cv_webview);
    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);

        mWebView.setListener(mListener);
    }

    public void setBody(String body) {
        mResult.getFetchedResult().setBody(body);
        mWebView.loadData(body, "text/html", "UTF-8");
    }

    public void resetBody() {
        mWebView.loadUrl("about:blank");
    }

    public void setData(NRResult result) {
        mResult = result;
        
        calculateItemViewHeight();

        if (mResult.getFetchedResult().getBody() != null) {
            setBody(mResult.getFetchedResult().getBody());
        } else {
            mListener.fetchBodyForResult(this, mResult.getFetchedResult().getId(), mResult.getFetchedResult().getHash());
        }
//        mLikeView.setResultId(result.getFetchedResult().getId());
//        if (result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
//            mLikeView.resetLikeView();
//        } else {
//            mLikeView.updateLikeButton(result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
//        }
//        if (mFeedbackView != null) {
//            RelativeLayout.LayoutParams params = null;
//            if (mResult.getFetchedResult().getChanneling() == null) {
//                params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
//                params.height = (int) Calculate.pxFromDp(itemView.getContext(), 50);
//            }else {
//                if (mChannelingView != null) {
//                    ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();
//                    for (NRChanneling channeling: channelings) {
//                        channeling.setQueryResult(mResult.getFetchedResult());
//                    }
//                    mChannelingView.setChannelings(channelings);
//                    params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
//                    params.height = (int) Calculate.pxFromDp(itemView.getContext(), 100);
//                }
//            }
//            mFeedbackView.setLayoutParams(params);
//        }
    }

    private void calculateItemViewHeight() {

        int delta = 60; // 50 for like's height
        if(mResult.getFetchedResult().getChanneling() != null) { //channeling height
            delta += 50;
        }

        ViewGroup.LayoutParams lp = mWebView.getLayoutParams();
        lp.height = mMaxHeight - (int) Calculate.pxFromDp(itemView.getContext(), delta);

        mWebView.setLayoutParams(lp);
    }
}
