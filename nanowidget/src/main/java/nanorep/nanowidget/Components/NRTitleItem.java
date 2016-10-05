package nanorep.nanowidget.Components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRTitleListener;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRTitleItem extends NRResultItem implements NRTitleListener{
    private View mItemView;

    private NRCustomTitleView titleView;

    @Override
    protected void bindViews(View view) {
        mItemView = view;
    }


    public NRTitleItem(View view, NRResultItemListener listener, NRConfiguration config, NRCustomTitleView titleView) {
        super(view, listener, config);

        this.titleView = titleView;
        this.titleView.setListener(this);
    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {
        String titleBGColor = config.getTitle().getTitleBGColor();

        if(titleBGColor != null && !"".equals(titleBGColor)) {
            mItemView.setBackgroundColor(Color.parseColor(titleBGColor));
        }
    }

    public void setData(NRResult result) {
        mResult = result;

        titleView.unfold(mResult.isUnfolded());

        if (result.getFetchedResult() != null) {
            titleView.setTitleText(result.getFetchedResult().getTitle());
        }

        int height = result.getHeight();

        if(mResult.isUnfolded()) { // title's height should wrap content
            int titleMeasuredHeight = titleView.getTitleHeight();
            if (titleMeasuredHeight > height) {
                height = titleMeasuredHeight;
            }
        }
//        } else {
//            // if unfold button is UP, turn it DOWN
//            setUnfoldButtonImage();
//        }

        setItemMargins(mResult.isUnfolded());

//        setTitleColor(mResult.isUnfolded());

        setHeight(height);

//        titleView.getTitleButton().setVisibility(result.isSingle() ? View.INVISIBLE : View.VISIBLE);
    }

//    private void setTitleColor(boolean unfolded) {
//
//        String color = "#4a4a4a";
//
//        if(unfolded) {
//            color = "#0aa0ff";
//        }
//
//        titleView.getTitleButton().setTextColor(Color.parseColor(color));
//    }

    private void setItemMargins(boolean unfolded) {
        int margin = 0;

        if(!unfolded) {
            margin = 15;
        }

//        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mItemView.getLayoutParams();
//        p.leftMargin = (int) Calculate.pxFromDp(mItemView.getContext(), margin);
//        p.rightMargin = (int) Calculate.pxFromDp(mItemView.getContext(), margin);
////        mItemView.requestLayout();
//        mItemView.setLayoutParams(p);

        final int _margin = margin;

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mItemView.getLayoutParams();
                p.leftMargin = (int) Calculate.pxFromDp(mItemView.getContext(), _margin);
                p.rightMargin = (int) Calculate.pxFromDp(mItemView.getContext(), _margin);
//        mItemView.requestLayout();
                mItemView.setLayoutParams(p);
            }
        };
        a.setDuration(100); // in ms
        mItemView.startAnimation(a);
    }

    private void setHeight(final int height) {
        ValueAnimator animator = ValueAnimator.ofInt(mItemView.getHeight(), height);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                titleView.getLayoutParams().height = height;
                mItemView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mItemView.requestLayout();
            }
        });
        animator.start();
    }

    /**
     *
     * @return
     */
    public int getTitleHeight() {

        return titleView.getTitleHeight();
    }

//    private int getTitleMeasuredHeight(Button titleButton) {
//        titleButton.measure( View.MeasureSpec.makeMeasureSpec(titleButton.getWidth(), View.MeasureSpec.AT_MOST),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        return titleButton.getMeasuredHeight();
//    }

    @Override
    public void onTitleClicked() {
        if(!mResult.isSingle()) {
            mListener.unfoldItem(mResult, false);
//            setUnfoldButtonImage();
        }
    }

//    private void setUnfoldButtonImage() {
//        ImageButton imageButton = titleView.getUnFoldButton();
//        if(imageButton != null) {
//            if (imageButton.getRotation() == -180 && !mResult.isUnfolded() || imageButton.getRotation() == 0 && mResult.isUnfolded())  {
//                ObjectAnimator.ofFloat(imageButton, "rotation", 0, mResult.isUnfolded() ? -180 : 0).start();
//            }
//        }
//    }

//    @Override
//    public void onUnfoldClicked() {
//        mListener.unfoldItem(mResult, false);
//        setUnfoldButtonImage();
//
//    }
//
    @Override
    public void onShareClicked() {
        mListener.onShareClicked(this, mResult.getFetchedResult().getTitle());
    }
}
