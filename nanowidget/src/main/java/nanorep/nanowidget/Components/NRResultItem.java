package nanorep.nanowidget.Components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanorepsdk.Connection.NRConnection;

import org.w3c.dom.Text;

import java.util.ArrayList;

import NanoRep.Chnneling.NRChanneling;
import NanoRep.Interfaces.NRQueryResult;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.Utilities.NRWebClient;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRResultItem extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {
    private NRResultItemListener mListener;
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

    public enum RowType {
        standard, shrinked, unfolded
    }

    public NRResultItem(View view, int maxHeight) {
        super(view);
        mItemView = view;
        setHeight(maxHeight);
        mTitleButton = (Button) view.findViewById(R.id.titleButton);


        mUnFoldButton = (ImageButton) view.findViewById(R.id.unFoldButton);
        mUnFoldButton.setOnClickListener(this);

        mTitleButton.setOnClickListener(this);
        mShareButton = (ImageButton) view.findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(this);


    }

    public void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

    public void setResult(NRResult result) {
        mResult = result;
        mTitleButton.setText(result.getFetchedResult().getTitle());
        setHeight(result.getHeight());
        int visibility = result.getRowType() == RowType.shrinked ? View.INVISIBLE : View.VISIBLE;
        mTitleButton.setVisibility(visibility);
        mUnFoldButton.setVisibility(visibility);
        mShareButton.setVisibility(visibility);
    }

    public NRResult getResult() {
        return mResult;
    }

    public NRLikeView getLikeView() {
        return mLikeView;
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
        if (v.getId() == R.id.titleButton || v.getId() == R.id.unFoldButton) {
//            if (!mResult.isUnfolded()) {
//                if (mResult.getFetchedResult().getBody() == null) {
//                    mListener.shouldFetchFAQAnswerBody(this, mResult.getFetchedResult().getId());
//                } else {
//                    setBody(mResult.getFetchedResult().getBody());
//                }
//            }
//            ObjectAnimator.ofFloat(mUnFoldButton, "rotation", 0, mResult.isUnfolded() ? 0 : -180).start();
//            mListener.unfoldItem(this);
//            if (mResult.getFetchedResult().getChanneling() == null) {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mActionView.getLayoutParams();
//                params.height = (int) Calculate.pxFromDp(mItemView.getContext(), 50);
//            } else {
//                ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();
//                for (NRChanneling channeling: channelings) {
//                    channeling.setQueryResult(mResult.getFetchedResult());
//                }
//                mNRChannelingView.setChannelings(channelings);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mActionView.getLayoutParams();
//                params.height = (int) Calculate.pxFromDp(mItemView.getContext(), 100);
//            }
            mListener.unfoldItem(this);
        } else if (v.getId() == R.id.shareButton) {
            mListener.onShareClicked(this, mResult.getFetchedResult().getTitle());
        }
    }

    @Override
    public void onLikeClicked() {
        mListener.onLikeClicked(this);
    }
}
