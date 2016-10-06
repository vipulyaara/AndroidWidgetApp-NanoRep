package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nissimpardo on 18/06/16.
 */
public class NRLikeView extends NRCustomLikeView implements View.OnClickListener {

    private TextView mLikeButton;
    private TextView mDislikeButton;
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
            mLikeButton.setTextColor(getResources().getColor(R.color.nr_like_color));
            mDislikeButton.setTextColor(getResources().getColor(R.color.nr_text_color));
        } else {
            mLikeButton.setTextColor(getResources().getColor(R.color.nr_text_color));
            mDislikeButton.setTextColor(getResources().getColor(R.color.nr_dislike_color));
        }
        mLikeButton.setEnabled(false);
        mDislikeButton.setEnabled(false);
        mLikeSelection = isLike;
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
    }

    private int resId(String resName) {
        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mLikeButton = (TextView) child.findViewById(R.id.likeButton);
        mDislikeButton = (TextView) child.findViewById(R.id.dislikeButton);
        mLikeButton.setOnClickListener(this);
        mDislikeButton.setOnClickListener(this);
    }

    @Override
    public boolean getLikeSelection() {
        return mLikeSelection;
    }

//    public void cancelLike() {
//        mLikeButton.setEnabled(true);
//        mDislikeButton.setEnabled(true);
//    }


    @Override
    public void onClick(View v) {
        mLikeSelection = v.getId() == R.id.likeButton;
        updateLikeButton(mLikeSelection);
        mListener.onLikeClicked(this, null, mLikeSelection);
    }
}
