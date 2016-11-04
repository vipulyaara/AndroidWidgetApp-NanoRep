package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;

import static nanorep.nanowidget.R.id.textView;

/**
 * Created by nanorep on 29/09/2016.
 */

public class NRTitleView extends NRCustomTitleView{

    private Button mTitleButton;
    private ImageButton mUnFoldButton;
    private ImageButton mShareButton;
    private LinearLayout titleLayout;
    private Context context;

    private boolean closed = true;

    public NRTitleView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.title, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        titleLayout = (LinearLayout) child.findViewById(R.id.titleLayout);

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
            mListener.onTitleCollapsed(getCollapsedHeight(mTitleButton.getText()));
            collapseTextView(100, 1000);
        } else {
            collapseTextView(2, 100);
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
    public int getTitleHeight(String text) {

        return titleLayout.getHeight();
    }

    @Override
    public void resetView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)titleLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        titleLayout.setLayoutParams(params);
    }

    private void collapseTextView(final int lines, long duration){
        ObjectAnimator animation = ObjectAnimator.ofInt(mTitleButton, "maxLines", lines);
        animation.setDuration(duration).start();

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(closed) {
                    mListener.onTitleCollapsed(mTitleButton.getHeight());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public int getCollapsedHeight(CharSequence text) {

        int mMeasuredHeight = (new StaticLayout(text, mTitleButton.getPaint(), mTitleButton.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)).getHeight();

        if(mMeasuredHeight < mTitleButton.getHeight()) {
            mMeasuredHeight = mTitleButton.getHeight();
        }
        return mMeasuredHeight;
    }

}
