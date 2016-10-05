package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.widget.LinearLayout;

import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nanorep on 05/10/2016.
 */

public abstract class NRCustomLikeView extends LinearLayout {

    protected OnLikeListener mListener;

    public void setListener(OnLikeListener listener) {
        mListener = listener;
    }

    public NRCustomLikeView(Context context) {
        super(context);
    }

    public abstract void updateLikeButton(boolean isLike);

    public abstract void resetLikeView();

    public abstract boolean getLikeSelection();
}
