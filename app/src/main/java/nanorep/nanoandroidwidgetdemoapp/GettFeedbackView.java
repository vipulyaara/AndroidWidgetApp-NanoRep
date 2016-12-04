package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomFeedbackView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.NRChannelItem;

/**
 * Created by noat on 30/11/2016.
 */

public class GettFeedbackView extends NRCustomFeedbackView {

    LinearLayout feedbackLayout;

    public GettFeedbackView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.feedback_gett, this);
    }

    @Override
    public void addLikeView() {
        feedbackLayout.addView(customLikeView);
    }

    @Override
    public void addChannelView() {
        feedbackLayout.addView(customChannelView);
    }


    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        feedbackLayout = (LinearLayout) child.findViewById(R.id.feedback_layout);

    }

    @Override
    public void setCustomLikeView(NRCustomLikeView customLikeView) {
        super.setCustomLikeView(customLikeView);
        addLikeView();
    }

    @Override
    public void onLikeClicked(NRCustomLikeView likeView, String resultId, boolean isLike) {
        if(customChannelView != null && customChannelView instanceof GettChannelingView && !((GettChannelingView)customChannelView).isChannelingEmpty() && !isLike) {
            addChannelView();
        }
    }

    @Override
    public void onChannelSelected(NRChanneling channeling) {

    }
}
