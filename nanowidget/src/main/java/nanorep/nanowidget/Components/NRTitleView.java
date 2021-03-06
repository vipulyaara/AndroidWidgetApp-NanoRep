package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanoclient.Response.NRConfiguration;

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
    private CardView rootCardView;
    private View viewSeparator;
    private ImageView ivBack;
    private Context context;

    private String textColorAnswer = "#4a4a4a";
    private String textColorFaq = "#4a4a4a";

    private String textFontAnswer = "sans-serif-medium";
    private String textFontFaq = "sans-serif-light";


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
        rootCardView = (CardView) child.findViewById(R.id.root_card_view);
        viewSeparator = child.findViewById(R.id.view_separator);
        ivBack = (ImageView) child.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });

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
//            if (mUnFoldButton.getRotation() == -270 && closed || mUnFoldButton.getRotation() == -90 && !closed)  {
//                ObjectAnimator.ofFloat(mUnFoldButton, "rotation", 0, !closed ? -270 : -90).start();
//            }
            if(!closed) {
                mUnFoldButton.setVisibility(View.INVISIBLE);
            } else {
                mUnFoldButton.setVisibility(View.VISIBLE);
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
            collapseTextView(100, 1000);
            mListener.onTitleCollapsed(getCollapsedHeight(mTitleButton.getText()));
        } else {
            collapseTextView(2, 100);
        }
    }

    private void setTitleFont() {
        String font = textFontFaq;

        if(!closed) {
            font = textFontAnswer;
        }

        ivBack.setVisibility(VISIBLE);
        viewSeparator.setVisibility(VISIBLE);
        setCardElevation(0);
        mTitleButton.setTypeface(Typeface.create(font, Typeface.NORMAL));
    }

    private void setShareImage() {
        if(closed) { // answer is closed, max 2 lines
            mShareButton.setVisibility(View.GONE);
        } else
        {
//            mShareButton.setVisibility(View.VISIBLE);
        }
    }


    private void setTitleColor() {

        String color = textColorFaq;

        if(!closed) {
            color = textColorAnswer;
        }

        if(!color.startsWith("#")) {
            color = "#" + color;
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

    public void configViewObjects(NRConfiguration nrConfiguration) {
        NRConfiguration.NRContent content = nrConfiguration.getContent();

        // text color
        if(content.getAnswerTitleColor() != null && !"".equals(content.getAnswerTitleColor())) {
            textColorAnswer = content.getAnswerTitleColor();
        }

        // title font
        if(content.getAnswerTextFont() != null && !"".equals(content.getAnswerTextFont())) {
            textFontAnswer = content.getAnswerTitleFont();
        }
    }

    public void setCardElevation(float elevation) {
        if (rootCardView != null) {
            rootCardView.setCardElevation(elevation);
        }
    }

    @Override
    public void setTitleMaxLines(int lines) {
        mTitleButton.setMaxLines(lines);
    }
}
