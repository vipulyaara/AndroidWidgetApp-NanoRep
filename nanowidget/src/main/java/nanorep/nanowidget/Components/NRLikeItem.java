package nanorep.nanowidget.Components;

import android.view.View;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nanorep on 23/09/2016.
 */

public class NRLikeItem extends NRResultItem implements OnLikeListener{

    private NRCustomLikeView mLikeView;

    public NRLikeItem(View view, NRResultItemListener listener, NRConfiguration config, NRCustomLikeView likeView) {
        super(view, listener, config);

        mLikeView = likeView;
        mLikeView.setListener(this);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);

    }

    @Override
    public void setData(NRResult result) {
//        mLikeView.setResultId(result.getFetchedResult().getId());
        mResult = result;

        if (result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
            mLikeView.resetLikeView();
        } else {
            mLikeView.updateLikeButton(result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
        }
    }

    @Override
    public void onLikeClicked(NRLikeView likeView, String resultId,boolean isLike) {
        mListener.onLikeClicked(likeView, mResult.getFetchedResult().getId(), isLike);
    }
}
