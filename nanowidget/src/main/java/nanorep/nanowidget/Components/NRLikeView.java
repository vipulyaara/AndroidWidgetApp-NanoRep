package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nissimpardo on 18/06/16.
 */
public class NRLikeView extends LinearLayout implements View.OnClickListener {
    private OnLikeListener mListener;
    private ImageButton mLikeButton;
    private ImageButton mDislikeButton;
    private boolean mLikeSelection;


    public NRLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(OnLikeListener listener) {
        mListener = listener;
    }

    public void updateLikeButton(boolean isLike) {
        if (isLike) {
            mLikeButton.getBackground().setColorFilter(0xff35d691, PorterDuff.Mode.MULTIPLY);
            mLikeButton.setImageResource(resId("white_like_icon"));
        } else {
            mDislikeButton.getBackground().setColorFilter(0xfff46f64, PorterDuff.Mode.MULTIPLY);
            mDislikeButton.setImageResource(resId("white_dislike_icon"));
        }
    }

    public void resetLikeView() {
        mLikeButton.setEnabled(true);
        mDislikeButton.setEnabled(true);
        mLikeButton.setImageResource(resId("grey_like_icon"));
        mDislikeButton.setImageResource(resId("grey_dislike_icon"));
        mLikeButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        mDislikeButton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
    }

    private int resId(String resName) {
        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() == R.id.likeButton) {
            mLikeButton = (ImageButton) child;
        } else if (child.getId() == R.id.dislikeButton) {
            mDislikeButton = (ImageButton) child;
        }
        child.setOnClickListener(this);
    }

    public boolean getLikeSelection() {
        return mLikeSelection;
    }

    public void cancelLike() {
        mLikeButton.setEnabled(true);
        mDislikeButton.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        mLikeSelection = v.getId() == R.id.likeButton;
        mLikeButton.setEnabled(false);
        mDislikeButton.setEnabled(false);
        mListener.onLikeClicked();
    }
}