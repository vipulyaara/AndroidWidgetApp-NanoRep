package nanorep.nanowidget.Components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRViewHolder;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRTitleItem extends NRResultItem implements View.OnClickListener {
    private View mItemView;
    private Button mTitleButton;
    private ImageButton mUnFoldButton;
    private WebView mWebView;
    private RelativeLayout mFooterView;
    private RelativeLayout mActionView;
    private ImageButton mShareButton;
    private NRLikeView mLikeView;
    private NRResult mResult;
    private NRChannelingView mNRChannelingView;

    @Override
    protected void initObjectsView(View view, int maxHeight) {
        mItemView = view;
        setHeight(maxHeight);
        mTitleButton = (Button) view.findViewById(R.id.titleButton);

        mUnFoldButton = (ImageButton) view.findViewById(R.id.unFoldButton);
        mUnFoldButton.setOnClickListener(this);

        mTitleButton.setOnClickListener(this);
        mShareButton = (ImageButton) view.findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(this);
    }


    public NRTitleItem(View view, int maxHeight, NRConfiguration config) {
        super(view, maxHeight, config);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {
        String titleBGColor = config.getTitle().getTitleBGColor();

        if(titleBGColor != null && !"".equals(titleBGColor)) {
            mTitleButton.setBackgroundColor(Color.parseColor(titleBGColor));
        }
    }

    public void setResult(NRResult result) {
        mResult = result;
        if (result.getFetchedResult() != null) {
            mTitleButton.setText(result.getFetchedResult().getTitle());
        }
        setHeight(result.getHeight());
//        int visibility = result.getRowType() == NRViewHolder.RowType.shrinked ? View.INVISIBLE : View.VISIBLE;
//        mTitleButton.setVisibility(visibility);
        mUnFoldButton.setVisibility(result.isSingle() ? View.INVISIBLE : View.VISIBLE);
//        mShareButton.setVisibility(visibility);
    }

    public NRResult getResult() {
        return mResult;
    }

    public NRLikeView getLikeView() {
        return mLikeView;
    }

    public NRViewHolder.RowType getRowType() {
        return NRViewHolder.RowType.standard;
    }

    public void setBody(String htmlString) {
        if (mResult.getFetchedResult().getBody() == null) {
            mResult.getFetchedResult().setBody(htmlString);
        }
        mWebView.loadData(htmlString, "text/html", "UTF-8");
    }

    private void setHeight(int height) {
        ValueAnimator animator = ValueAnimator.ofInt(mItemView.getHeight(), height);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
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

}
