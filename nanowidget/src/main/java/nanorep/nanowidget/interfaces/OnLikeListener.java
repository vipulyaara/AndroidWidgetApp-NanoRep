package nanorep.nanowidget.interfaces;


import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.NRLikeView;

/**
 * Created by nissimpardo on 18/06/16.
 */

public interface OnLikeListener {
    void onLikeClicked(NRCustomLikeView likeView, String resultId, boolean isLike);
}
