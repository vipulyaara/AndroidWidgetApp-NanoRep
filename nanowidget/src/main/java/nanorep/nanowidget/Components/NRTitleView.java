package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.R;

/**
 * Created by nanorep on 29/09/2016.
 */

public class NRTitleView extends NRCustomTitleView{

    private Button mTitleButton;
    private ImageButton mUnFoldButton;
    private ImageButton mShareButton;
    private boolean closed = true;

    public NRTitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.title, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mTitleButton = (Button) child.findViewById(R.id.titleButton);

        mUnFoldButton = (ImageButton) child.findViewById(R.id.unFoldButton);
        mUnFoldButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });

        mTitleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });

        mShareButton = (ImageButton) child.findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShareClicked();
            }
        });
    }

    private void setUnfoldButtonImage() {
        if(mUnFoldButton != null) {
            if (mUnFoldButton.getRotation() == -180 && closed || mUnFoldButton.getRotation() == 0 && !closed)  {
                ObjectAnimator.ofFloat(mUnFoldButton, "rotation", 0, !closed ? -180 : 0).start();
            }
        }
    }

    @Override
    public void setTitleText(final String text) {
        mTitleButton.setText(text);

        mTitleButton.setMaxLines(2);
    }

    @Override
    public void unfold(boolean closed) {
        this.closed = !closed;

        setTitleColor();
        setTitleFont();
        setUnfoldButtonImage();
        setShareImage();

        if(!this.closed) {
            collapseTextView();
        }
    }

    private void setTitleFont() {
        String font = "sans-serif-light";

        if(!closed) {
            font = "sans-serif-medium";
        }

        mTitleButton.setTypeface(Typeface.create(font, Typeface.NORMAL));
    }

    private void setShareImage() {
        if(closed) { // answer is closed, max 2 lines
            mShareButton.setVisibility(View.GONE);
        } else
        {
            mShareButton.setVisibility(View.VISIBLE);
        }
    }


    private void setTitleColor() {

        String color = "#4a4a4a";

        if(!closed) {
            color = "#0aa0ff";
        }

        mTitleButton.setTextColor(Color.parseColor(color));
    }

    @Override
    public int getTitleHeight() {

//        mTitleButton.setMaxLines(100);
//        mTitleButton.measure( View.MeasureSpec.makeMeasureSpec(mTitleButton.getWidth(), View.MeasureSpec.AT_MOST),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

//        return mTitleButton.getHeight();
        return 0;
    }

    private void collapseTextView(){
        ObjectAnimator animation = ObjectAnimator.ofInt(mTitleButton, "maxLines", 100);
        animation.setDuration(1000).start();

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mListener.onTitleCollapsed();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
