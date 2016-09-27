package nanorep.nanowidget.Components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRTitleItem extends NRResultItem implements View.OnClickListener {
    private View mItemView;
    private Button mTitleButton;
    private ImageButton mUnFoldButton;
    private ImageButton mShareButton;
    private RelativeLayout topView;

    @Override
    protected void bindViews(View view) {
        mItemView = view;
        mTitleButton = (Button) view.findViewById(R.id.titleButton);

        mUnFoldButton = (ImageButton) view.findViewById(R.id.unFoldButton);
        mUnFoldButton.setOnClickListener(this);

        mTitleButton.setOnClickListener(this);
        mShareButton = (ImageButton) view.findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(this);

        topView = (RelativeLayout) view.findViewById(R.id.topView);
    }


    public NRTitleItem(View view, NRResultItemListener listener, NRConfiguration config) {
        super(view, listener, config);
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
            mTitleButton.setText(result.getFetchedResult().getTitle());
        }

        int height = result.getHeight();

        if(mResult.isUnfolded()) { // title's height should wrap content
            int titleMeasuredHeight = getTitleMeasuredHeight();
            if(titleMeasuredHeight > result.getHeight()) {
                height = titleMeasuredHeight;
            }
        }

        setHeight(height);

        mUnFoldButton.setVisibility(result.isSingle() ? View.INVISIBLE : View.VISIBLE);
    }

    private void setHeight(final int height) {
        ValueAnimator animator = ValueAnimator.ofInt(mItemView.getHeight(), height);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                topView.getLayoutParams().height = height;
                mItemView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mItemView.requestLayout();
            }
        });
        animator.start();
    }

    public String getText() {
        return mTitleButton.getText().toString();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.titleButton && mResult.isSingle()) {
            return;
        }
        if (v.getId() == R.id.titleButton || v.getId() == R.id.unFoldButton) {
            ObjectAnimator.ofFloat(mUnFoldButton, "rotation", 0, mResult.isUnfolded() ? 0 : -180).start();
            mListener.unfoldItem(mResult, false);
//            mResult.setUnfolded(!mResult.isUnfolded());
        } else if (v.getId() == R.id.shareButton) {
            mListener.onShareClicked(this, mResult.getFetchedResult().getTitle());
        }
    }

    /**
     *
     * @return
     */
    public int getTitleMeasuredHeight() {
        mTitleButton.measure( View.MeasureSpec.makeMeasureSpec(mTitleButton.getWidth(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        return mTitleButton.getMeasuredHeight();
    }
}
