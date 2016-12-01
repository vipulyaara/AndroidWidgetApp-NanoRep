package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomFeedbackView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.NRChannelItem;

/**
 * Created by noat on 30/11/2016.
 */

public class GettFeedbackView extends NRCustomFeedbackView {

    LinearLayout feedbackLayout;

    public GettFeedbackView(NRCustomLikeView customLikeView, NRCustomChannelView customChannelView, Context context) {
        super(customLikeView, customChannelView, context);
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
        addLikeView();
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {

    }

    @Override
    public void onLikeClicked(NRCustomLikeView likeView, String resultId, boolean isLike) {
        if(customChannelView != null) {
            addChannelView();
        }
    }

}
