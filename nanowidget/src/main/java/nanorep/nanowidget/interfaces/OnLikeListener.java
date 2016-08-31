package nanorep.nanowidget.interfaces;


import nanorep.nanowidget.Components.NRLikeView;

/**
 * Created by nissimpardo on 18/06/16.
 */

public interface OnLikeListener {
    void onLikeClicked(NRLikeView likeView, String resultId, boolean isLike);
}
