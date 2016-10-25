package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    private LinearLayout title_container;

    @Override
    protected void bindViews(View view) {
        mItemView = view;
    }


    public NRTitleItem(View view, NRResultItemListener listener, NRConfiguration config, NRCustomTitleView titleView) {
        super(view, listener, config);

        this.titleView = titleView;
        this.titleView.setListener(this);

        title_container = (LinearLayout) view.findViewById(R.id.title_container);
    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);
    }

    @Override
    public void resetBody() {

    }

    @Override
    public void updateBody() {

        // set text color, style etc...
        titleView.unfold(mResult.isUnfolded());

        // set item margins
//        setItemMargins(mResult.isUnfolded());

        // set the correct height - to wrap the content
        int height = mItemView.getHeight();

        if(!mResult.isUnfolded()) {
            height = mResult.getHeight();
        } else {
            int titleMeasuredHeight = titleView.getTitleHeight();
            if (titleMeasuredHeight > height) {
                height = titleMeasuredHeight;
            } else {
                mListener.unfoldItem(mResult, false, true);
                return;
            }
        }
        setHeight(height);

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

        if (result.getFetchedResult() != null) {
            titleView.setTitleText(result.getFetchedResult().getTitle());
        }

        int height = result.getHeight();

        if (mResult.isUnfolded()) { // title's height should wrap content
            int titleMeasuredHeight = titleView.getTitleHeight();
            if (titleMeasuredHeight > height) {
                height = titleMeasuredHeight;
            }
        }

        setHeight(height);
    }

    private void setItemMargins(boolean unfolded) {
        int startMargin = (int) Calculate.pxFromDp(title_container.getContext(), 0);
        int endMargin = (int) Calculate.pxFromDp(title_container.getContext(), 15);

        if(unfolded) {
            startMargin = (int) Calculate.pxFromDp(title_container.getContext(), 15);
            endMargin = (int) Calculate.pxFromDp(title_container.getContext(), 0);
        }

        if(((ViewGroup.MarginLayoutParams) title_container.getLayoutParams()).rightMargin == endMargin) {
            return;
        }

        ValueAnimator varl = ValueAnimator.ofInt(startMargin,endMargin);
        varl.setDuration(80);
        varl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((ViewGroup.MarginLayoutParams) title_container.getLayoutParams()).rightMargin = (Integer) animation.getAnimatedValue();
                ((ViewGroup.MarginLayoutParams) title_container.getLayoutParams()).leftMargin = (Integer) animation.getAnimatedValue();
                title_container.requestLayout();
            }
        });

        varl.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(mResult.isUnfolded()) {
                    mListener.unfoldItem(mResult, false, true);
                }
            }
        });

        varl.start();
    }

    private void setHeight(final int height) {
        ValueAnimator animator = ValueAnimator.ofInt(mItemView.getHeight(), height);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                titleView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mItemView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mItemView.requestLayout();
            }
        });

        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(mResult.isUnfolded()) {
                    mListener.unfoldItem(mResult, false, true);
                }
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

    @Override
    public void onTitleClicked() {

        if(!mResult.isSingle()) {
            mListener.unfoldItem(mResult, false, false);
        }
    }

    @Override
    public void onShareClicked() {
        mListener.onShareClicked(this, mResult.getFetchedResult().getTitle());
    }
}
