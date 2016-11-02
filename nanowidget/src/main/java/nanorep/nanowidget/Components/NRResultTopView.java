package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
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

public class NRResultTopView extends RelativeLayout implements NRTitleListener, OnLikeListener, NRChannelItem.OnChannelSelectedListener {

    NRResultItemListener mListener;
    private NRResult mResult;
    private LinearLayout viewTitleContainer;
    private FrameLayout viewContentContainer;
    private LinearLayout viewLikeContainer;
    private LinearLayout viewChannelingContainer;

    private LinearLayout answerLayout;

    private NRCustomChannelView channelView;
    private NRCustomContentView contentView;
    private NRCustomLikeView likeView;
    private NRCustomTitleView titleView;

    private int y;


    public NRResultTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.nrresult_top_view, this);
        viewTitleContainer = (LinearLayout) view.findViewById(R.id.title_container);
        viewContentContainer = (FrameLayout) view.findViewById(R.id.content_container);
        viewLikeContainer = (LinearLayout) view.findViewById(R.id.like_container);
        viewChannelingContainer = (LinearLayout) view.findViewById(R.id.channel_container);
        answerLayout = (LinearLayout) view.findViewById(R.id.answerLayout);
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

        this.y = y;

        remvoeAllViews(viewChannelingContainer);
        remvoeAllViews(viewLikeContainer);
        remvoeAllViews(viewContentContainer);
        remvoeAllViews(viewTitleContainer);

        titleView.resetView();

        titleView.setTitleText(mResult.getFetchedResult().getTitle());

        viewTitleContainer.addView(titleView);
        viewTitleContainer.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        this.setPadding(0, y, 0, 0);

        setContent();

        setLike();

        if(mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
            setChannel();
        }

        if(!mResult.isSingle()) {
            setTitleYAnimation(y, 0, mResult.isUnfolded());
        } else {
            titleView.unfold(true);
        }
    }

    private void remvoeAllViews(ViewGroup view) {
        if(view.getChildCount() > 0) {
            view.removeAllViews();
        }
    }


    public void openViewAnimation() {

        Animation fadeInContent = new AlphaAnimation(0, 1);
        fadeInContent.setDuration(700);

        Animation fadeInLike = new AlphaAnimation(0, 1);
        fadeInLike.setDuration(700);
        fadeInLike.setStartOffset(350);

        Animation fadeInChannel = new AlphaAnimation(0, 1);
        fadeInChannel.setDuration(700);
        fadeInChannel.setStartOffset(350);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(answerLayout, "TranslationY", viewTitleContainer.getHeight());
        objectAnimator.setDuration(700);
        objectAnimator.start();

        viewContentContainer.startAnimation(fadeInContent);
        viewLikeContainer.startAnimation(fadeInLike);
        viewChannelingContainer.startAnimation(fadeInChannel);
    }

    public void closeViewAnimation() {

        // create set of animations
        AnimationSet replaceAnimation = new AnimationSet(false);
        // animations should be applied on the finish line
        replaceAnimation.setFillAfter(true);

        Animation fadeOutContent = new AlphaAnimation(1, 0);
        fadeOutContent.setDuration(700);
        removeViewListener(fadeOutContent, viewContentContainer);

        Animation fadeOutLike = new AlphaAnimation(1, 0);
        fadeOutLike.setDuration(700);
        removeViewListener(fadeOutLike, viewLikeContainer);

        Animation fadeOutChannel = new AlphaAnimation(1, 0);
        fadeOutChannel.setDuration(700);
        removeViewListener(fadeOutChannel, viewChannelingContainer);

        // create translation animation
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(answerLayout, "TranslationY", 0);
        objectAnimator.setDuration(700);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleView.unfold(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.setStartDelay(350);
        objectAnimator.start();

        viewChannelingContainer.startAnimation(fadeOutChannel);
        viewLikeContainer.startAnimation(fadeOutLike);
        viewContentContainer.startAnimation(fadeOutContent);
    }

    private void removeViewListener(Animation animation, final ViewGroup view) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    view.removeAllViews();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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

    private void setTitleYAnimation(final int start, final int end, final boolean isUnfolded) {

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
                if(!isUnfolded) { // going down
                    mListener.onFoldItemFinished(false);
                } else { // going up
                    titleView.unfold(true);
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

    public void removeTopView() {

        if(!mResult.isSingle()) {
            closeViewAnimation();
        } else {
            viewChannelingContainer.removeAllViews();
            viewLikeContainer.removeAllViews();
            viewContentContainer.removeAllViews();
            viewTitleContainer.removeAllViews();
        }
    }

    @Override
    public void onTitleClicked() {
        if(!mResult.isSingle()) {
            mListener.unfoldItem(mResult, false);
        }
    }

    @Override
    public void onTitleCollapsed(boolean unfold) {

        if(unfold) {

            int feedbachHeight = 50;

            if (mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
                feedbachHeight = 100;

                viewChannelingContainer.addView(channelView);
                viewChannelingContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);
            }

            viewContentContainer.addView(contentView);
            int height = NRResultTopView.this.getHeight() - viewTitleContainer.getHeight() - (int) Calculate.pxFromDp(getContext(), feedbachHeight);
            viewContentContainer.getLayoutParams().height = height;

            viewLikeContainer.addView(likeView);

            viewLikeContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);

            openViewAnimation();
        } else { //going down

            viewTitleContainer.getLayoutParams().height = mResult.getHeight();
            viewTitleContainer.requestLayout();

            answerLayout.setTranslationY(0);

            mListener.onFoldItemFinished(true);

            setTitleYAnimation(0, y, false);
        }
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

    public void removeTitleView () {

        viewTitleContainer.removeAllViews();
        titleView.resetView();
    }

}
