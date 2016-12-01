package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.NRLikeView;

/**
 * Created by noat on 29/11/2016.
 */

public class GettLikeView extends NRCustomLikeView {

    LinearLayout likeLayout;
    private TextView mLikeButton;
    private TextView mDislikeButton;
    private boolean mLikeSelection;

    private final String green = "#60c300";
    private final String red = "#ff5252";
    private final String gray = "#b8b8b8";

    public GettLikeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.like_gett, this);
    }

    @Override
    public void updateLikeButton(boolean isLike) {
        resetLikeView();
        if (isLike) {
            mLikeButton.setTextColor(Color.parseColor(green));
            mLikeButton.setBackground(getResources().getDrawable(R.drawable.back_green));
        } else {
            mDislikeButton.setTextColor(Color.parseColor(red));
            mDislikeButton.setBackground(getResources().getDrawable(R.drawable.back_red));
        }
        mLikeButton.setEnabled(false);
        mDislikeButton.setEnabled(false);
        mLikeSelection = isLike;
    }

    @Override
    public void resetLikeView() {
        mLikeButton.setEnabled(true);
        mDislikeButton.setEnabled(true);
        mLikeButton.setTextColor(getResources().getColor(nanorep.nanowidget.R.color.nr_text_color));
        mDislikeButton.setTextColor(getResources().getColor(nanorep.nanowidget.R.color.nr_text_color));
        mLikeButton.setBackground(getResources().getDrawable(R.drawable.back));
        mDislikeButton.setBackground(getResources().getDrawable(R.drawable.back));
    }


    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mLikeButton = (TextView) child.findViewById(nanorep.nanowidget.R.id.likeButton);
        mDislikeButton = (TextView) child.findViewById(nanorep.nanowidget.R.id.dislikeButton);
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSelection(true);
            }
        });
        mDislikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSelection(false);
            }
        });
    }

    private void sendSelection(boolean selection) {
        mLikeSelection = selection;
        updateLikeButton(mLikeSelection);
        mListener.onLikeClicked(GettLikeView.this, null, mLikeSelection);
    }

    @Override
    public boolean getLikeSelection() {
        return mLikeSelection;
    }

}
