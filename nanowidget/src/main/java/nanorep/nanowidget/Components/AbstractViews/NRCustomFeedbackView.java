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


    public NRCustomFeedbackView(NRCustomLikeView customLikeView, NRCustomChannelView customChannelView, Context context) {
        super(context);
        this.customLikeView = customLikeView;
        this.customChannelView = customChannelView;
    }

    public void setListener(OnFeedBackListener listener) {
        mListener = listener;
    }

    public abstract void addLikeChannelViews();
}
