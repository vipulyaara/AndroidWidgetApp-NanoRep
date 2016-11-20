package nanorep.nanowidget.Components;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
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


    public NRTitleItem(View view, NRCustomTitleView titleView, NRResultItemListener listener) {
        super(view, listener);

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
    protected void configViewObjects(NRConfiguration config) {
//        String titleBGColor = config.getTitle().getTitleBGColor();
//
//        if(titleBGColor != null && !"".equals(titleBGColor)) {
//            mItemView.setBackgroundColor(Color.parseColor(titleBGColor));
//        }
    }

    public void setData(NRResult result) {
        mResult = result;

        if (result.getFetchedResult() != null) {
            titleView.setTitleText(result.getFetchedResult().getTitle());
        }

        int height = result.getHeight();

        setHeight(height);
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

        animator.start();
    }

    public LinearLayout getTitle_container() {
        return title_container;
    }

    @Override
    public void onTitleClicked() {
        if(!mResult.isSingle()) {
            mListener.unfoldItem(mResult, false);
        }
    }

    @Override
    public void onTitleCollapsed(int height) {

    }

    @Override
    public void onShareClicked() {
        mListener.onShareClicked(this, mResult.getFetchedResult().getTitle());
    }

    public NRCustomTitleView getTitleView() {
        return titleView;
    }
}
