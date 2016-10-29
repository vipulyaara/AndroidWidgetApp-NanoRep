package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRConfiguration;

import java.util.ArrayList;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRTitleListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nanorep on 27/10/2016.
 */

public class NRResultTopView extends LinearLayout implements NRTitleListener, OnLikeListener, NRChannelItem.OnChannelSelectedListener {

    NRResultItemListener mListener;
    private NRResult mResult;
    private LinearLayout viewTitleContainer;
    private FrameLayout viewContentContainer;
    private LinearLayout viewLikeContainer;
    private LinearLayout viewChannelingContainer;

    private NRCustomChannelView channelView;
    private NRCustomContentView contentView;
    private NRCustomLikeView likeView;
    private NRCustomTitleView titleView;

    public NRResultTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.nrresult_top_view, this);
        viewTitleContainer = (LinearLayout) view.findViewById(R.id.title_container);
        viewContentContainer = (FrameLayout) view.findViewById(R.id.content_container);
        viewLikeContainer = (LinearLayout) view.findViewById(R.id.like_container);
        viewChannelingContainer = (LinearLayout) view.findViewById(R.id.channel_container);
//        configViewObjects(configuration);
    }

    public void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

    public void configViewObjects(NRConfiguration config) {

        // title
        String titleBGColor = config.getTitle().getTitleBGColor();

        if(titleBGColor != null && !"".equals(titleBGColor)) {
            titleView.setBackgroundColor(Color.parseColor(titleBGColor));
        }
    }

    public void setResult(NRResult result) {
        mResult = result;
    }

    public void openView(int y) {
        titleView.setTitleText(mResult.getFetchedResult().getTitle());

        viewTitleContainer.addView(titleView);
        viewTitleContainer.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        this.setPadding(0, y, 0, 0);

        setContent();

        setLike();

        if(mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
            setChannel();
        }

        setTitleAnimation(y, 0);
    }

    private void setChannel() {
        ArrayList<NRChanneling> channelings = mResult.getFetchedResult().getChanneling();

        for (NRChanneling channeling : channelings) {
            channeling.setQueryResult(mResult.getFetchedResult());
        }
        channelView.setChannelings(channelings);
    }

    private void setLike() {
        if (mResult.getFetchedResult().getLikeState() == NRQueryResult.LikeState.notSelected) {
            likeView.resetLikeView();
        } else {
            likeView.updateLikeButton(mResult.getFetchedResult().getLikeState() == NRQueryResult.LikeState.positive);
        }
    }

    private void setContent() {
        if (mResult.getFetchedResult().getBody() != null) {
            mResult.getFetchedResult().setBody(mResult.getFetchedResult().getBody());
            contentView.loadData(mResult.getFetchedResult().getBody(), "text/html", "UTF-8");
        } else {
            mListener.fetchBodyForResult(contentView, mResult.getFetchedResult().getId(), mResult.getFetchedResult().getHash());
        }
    }

    private void setTitleAnimation(int start, final int end) {

        ValueAnimator varl = ValueAnimator.ofInt(start,end);
        varl.setDuration(400);
        varl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                NRResultTopView.this.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0);
            }
        });

        varl.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(end == 0) { // going up
                    titleView.unfold(true);
                } else { // going down
                    viewTitleContainer.removeAllViews();
                    viewTitleContainer.getLayoutParams().height=0;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        varl.start();
    }

    private void setTitleHeightAnimation(int start, int end,final int y) {

        ValueAnimator varl = ValueAnimator.ofInt(start,end);
        varl.setDuration(200);
        varl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewTitleContainer.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                viewTitleContainer.requestLayout();
            }
        });

        varl.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                titleView.unfold(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setTitleAnimation(0, y);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        varl.start();
    }

    public void removeTopView(int y) {

        setTitleHeightAnimation(viewTitleContainer.getHeight(), mResult.getHeight(), y);

//        viewTitleContainer.removeAllViews();
        viewContentContainer.removeAllViews();
        viewLikeContainer.removeAllViews();
        viewChannelingContainer.removeAllViews();

//        viewTitleContainer.getLayoutParams().height=0;
        viewContentContainer.getLayoutParams().height = 0;
        viewLikeContainer.getLayoutParams().height = 0;
        viewChannelingContainer.getLayoutParams().height=0;
    }

    @Override
    public void onTitleClicked() {
        if(!mResult.isSingle()) {
            mListener.unfoldItem(mResult, false);
        }
    }

    @Override
    public void onTitleCollapsed() {
        int feedbachHeight = 50;

        if(mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
            feedbachHeight = 100;

            viewChannelingContainer.addView(channelView);
            viewChannelingContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);
        }

        viewContentContainer.addView(contentView);
        viewContentContainer.getLayoutParams().height = NRResultTopView.this.getHeight() - viewTitleContainer.getHeight() - (int) Calculate.pxFromDp(getContext(), feedbachHeight);

        viewLikeContainer.addView(likeView);
        viewLikeContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        animation.setStartOffset(0);

        contentView.startAnimation(animation);
        likeView.startAnimation(animation);
    }

    @Override
    public void onShareClicked() {

    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        mListener.onChannelSelected(channelItem);
    }

    @Override
    public void onLikeClicked(NRLikeView likeView, String resultId, boolean isLike) {
        mListener.onLikeClicked(likeView, mResult.getFetchedResult().getId(), isLike);
    }

    public void setChannelView(NRCustomChannelView channelView) {
        this.channelView = channelView;
        this.channelView.setListener(this);
    }

    public void setContentView(NRCustomContentView contentView) {
        this.contentView = contentView;
    }

    public void setLikeView(NRCustomLikeView likeView) {
        this.likeView = likeView;
        this.likeView.setListener(this);
    }

    public void setTitleView(NRCustomTitleView titleView) {
        this.titleView = titleView;
        this.titleView.setListener(this);
    }
}
