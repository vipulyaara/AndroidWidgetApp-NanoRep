package nanorep.nanowidget.Components;

import android.view.View;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nanorep on 23/09/2016.
 */

public class NRLikeItem extends NRResultItem{

    private NRLikeView mLikeView;

    public NRLikeItem(View view, NRResultItemListener listener, NRConfiguration config) {
        super(view, listener, config);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void bindViews(View view) {
        mLikeView = (NRLikeView) itemView.findViewById(R.id.cv_likeView);
    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);
        mLikeView.setListener(mListener);
    }

    @Override
    public void setData(NRResult result) {
        mLikeView.setResultId(result.getFetchedResult().getId());
        if (result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
            mLikeView.resetLikeView();
        } else {
            mLikeView.updateLikeButton(result.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
        }
    }
}
