package nanorep.nanowidget.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRConfiguration;

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
    private NRResult mResult;


    public NRContentItem(View view, int maxHeight, NRConfiguration config) {
        super(view, maxHeight, config);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void initObjectsView(View view, int maxHeight) {
        itemView.getLayoutParams().height = maxHeight - (int) Calculate.pxFromDp(itemView.getContext(), 80);
        mWebView = (NRWebView) itemView.findViewById(R.id.cv_webview);
        mFeedbackView = (RelativeLayout) itemView.findViewById(R.id.cv_feedbackView);
        mLikeView = (NRLikeView) itemView.findViewById(R.id.cv_likeView);
        mChannelingView = (NRChannelingView) itemView.findViewById(R.id.cv_channelingView);

        mWebView.setListener(mListener);
        mLikeView.setListener(mListener);
        mChannelingView.setListener(mListener);
    }

    public void setBody(String body) {
        mResult.getFetchedResult().setBody(body);
        mWebView.loadData(body, "text/html", "UTF-8");
    }

    public void resetBody() {
        mWebView.loadUrl("about:blank");
    }

    public void setResult(NRResult result) {
        mResult = result;
        if (mResult.getFetchedResult().getBody() != null) {
            setBody(mResult.getFetchedResult().getBody());
        } else {
            mListener.fetchBodyForResult(this, mResult.getFetchedResult().getId(), mResult.getFetchedResult().getHash());
        }
        mLikeView.setResultId(result.getFetchedResult().getId());
        if (result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
            mLikeView.resetLikeView();
        } else {
            mLikeView.updateLikeButton(result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
        }
        if (mFeedbackView != null) {
            RelativeLayout.LayoutParams params = null;
            if (mResult.getFetchedResult().getChanneling() == null) {
                params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                params.height = (int) Calculate.pxFromDp(itemView.getContext(), 50);
            }else {
                if (mChannelingView != null) {
                    ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();
                    for (NRChanneling channeling: channelings) {
                        channeling.setQueryResult(mResult.getFetchedResult());
                    }
                    mChannelingView.setChannelings(channelings);
                    params = (RelativeLayout.LayoutParams) mFeedbackView.getLayoutParams();
                    params.height = (int) Calculate.pxFromDp(itemView.getContext(), 100);
                }
            }
            mFeedbackView.setLayoutParams(params);
        }
    }

    public NRViewHolder.RowType getRowType() {
        return NRViewHolder.RowType.unfolded;
    }


}
