package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.widget.LinearLayout;

import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.interfaces.OnFeedBackListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by noat on 30/11/2016.
 */

public abstract class NRCustomFeedbackView extends LinearLayout implements OnFeedBackListener{

    protected OnFeedBackListener mListener;
    protected NRCustomLikeView customLikeView;
    protected NRCustomChannelView customChannelView;


    public void setCustomLikeView(NRCustomLikeView customLikeView) {
        this.customLikeView = customLikeView;
    }

    public void setCustomChannelView(NRCustomChannelView customChannelView) {
        this.customChannelView = customChannelView;
    }

    public NRCustomChannelView getCustomChannelView() {
        return customChannelView;
    }

    public NRCustomLikeView getCustomLikeView() {
        return customLikeView;
    }

    public NRCustomFeedbackView(Context context) {
        super(context);
    }

    public void setListener(OnFeedBackListener listener) {
        mListener = listener;
    }

    public abstract void addLikeView();

    public abstract void addChannelView();
}
