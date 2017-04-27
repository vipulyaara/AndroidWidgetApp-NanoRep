package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 18/06/16.
 */
public class NRLikeView extends NRCustomLikeView {

    private TextView mLikeButton;
    private TextView mDislikeButton;
    private TextView mThankYouMsg;
    private boolean mLikeSelection;


    public NRLikeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.like_view, this);
    }

//    public void setResultId(String resultId) {
//        mResultId = resultId;
//    }

    @Override
    public void updateLikeButton(boolean isLike) {
        resetLikeView();
        if (isLike) {
            mLikeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.nr_like_color));
            mDislikeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.nr_text_color));
            showThankYouMsg();
        } else {
            mLikeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.nr_text_color));
            mDislikeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.nr_dislike_color));
        }
        mLikeButton.setEnabled(false);
        mDislikeButton.setEnabled(false);
        mLikeSelection = isLike;
    }

    @Override
    public void showThankYouMsg() {
        mThankYouMsg.setVisibility(VISIBLE);
    }

    @Override
    public void resetLikeView() {
        mLikeButton.setEnabled(true);
        mDislikeButton.setEnabled(true);
//        mLikeButton.setImageResource(resId("grey_like_icon"));
//        mDislikeButton.setImageResource(resId("grey_dislike_icon"));
//        mLikeButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
//        mDislikeButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        mLikeButton.setTextColor(getResources().getColor(R.color.nr_text_color));
        mDislikeButton.setTextColor(getResources().getColor(R.color.nr_text_color));
        mThankYouMsg.setVisibility(GONE);
    }

//    private int resId(String resName) {
//        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
//    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mLikeButton = (TextView) child.findViewById(R.id.likeButton);
        mDislikeButton = (TextView) child.findViewById(R.id.dislikeButton);
        mThankYouMsg = (TextView) child.findViewById(R.id.tv_thank_you_msg);
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mLikeSelection = true;
//                updateLikeButton(mLikeSelection);
//                mListener.onLikeClicked(NRLikeView.this, null, mLikeSelection);
                sendSelection(true);
            }
        });
        mDislikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mLikeSelection = false;
//                updateLikeButton(mLikeSelection);
//                mListener.onLikeClicked(NRLikeView.this, null, mLikeSelection);
                sendSelection(false);
            }
        });
    }

    private void sendSelection(boolean selection) {
        mLikeSelection = selection;
        updateLikeButton(mLikeSelection);
        mListener.onLikeClicked(NRLikeView.this, null, mLikeSelection);
    }

    @Override
    public boolean getLikeSelection() {
        return mLikeSelection;
    }

    @Override
    public boolean shouldOpenDialog() {
        return true;
    }

//    public void cancelLike() {
//        mLikeButton.setEnabled(true);
//        mDislikeButton.setEnabled(true);
//    }


//    @Override
//    public void onClick(View v) {
//        mLikeSelection = v.getId() == R.id.likeButton;
//        updateLikeButton(mLikeSelection);
//        mListener.onLikeClicked(this, null, mLikeSelection);
//    }
}
