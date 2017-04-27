package nanorep.nanowidget.Components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.Response.NRConfiguration;

import java.util.ArrayList;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomFeedbackView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRTitleListener;
import nanorep.nanowidget.interfaces.OnFeedBackListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nanorep on 27/10/2016.
 */

public class NRResultTopView extends RelativeLayout implements NRTitleListener, View.OnClickListener, OnFeedBackListener {

//    NRResultItemListener mListener;

    private Listener topViewListener;

    private NRResult mResult;
    private LinearLayout viewTitleContainer;
    private FrameLayout viewContentContainer;
    private LinearLayout viewLikeContainer;
    private LinearLayout viewChannelingContainer;

    private LinearLayout answerLayout;
    private RelativeLayout layoutAnimated;

    //opened layout
    private LinearLayout viewTitleContainerOpened;
    private FrameLayout viewContentContainerOpened;
    private LinearLayout viewFeedbackContainerOpened;
    private LinearLayout viewLikeContainerOpened;
    private LinearLayout viewChannelingContainerOpened;
    private LinearLayout layoutOpened;

    private NRCustomChannelView channelView;
    private NRCustomContentView contentView;
    private NRCustomLikeView likeView;
    private NRCustomTitleView titleView;
    private NRCustomFeedbackView feedbackView;

    private int y;


    @Override
    public void onLikeClicked(NRCustomLikeView likeView, String resultId, boolean isLike) {
        topViewListener.onLikeClicked(NRResultTopView.this, likeView, resultId, isLike);
        if(feedbackView != null) {
            feedbackView.onLikeClicked(null, null, isLike);
        }
    }

    @Override
    public void onClick(View v) {
        int i=0;
    }

    @Override
    public void onChannelSelected(NRChanneling channeling) {

    }

    public interface Listener {
        void onFoldItemFinished(boolean beforeGoingDown);
        void fetchBodyForResult(NRCustomContentView view, String resultID, Integer resultHash);
        void closeAnswer();
        void onLikeClicked(NRResultTopView view, NRCustomLikeView likeView, String resultId, boolean isLike);
    }


    public NRResultTopView(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.nrresult_top_view, this);

        //animated
        viewTitleContainer = (LinearLayout) view.findViewById(R.id.title_container);
        viewContentContainer = (FrameLayout) view.findViewById(R.id.content_container);
        viewLikeContainer = (LinearLayout) view.findViewById(R.id.like_container);
        viewChannelingContainer = (LinearLayout) view.findViewById(R.id.channel_container);
        answerLayout = (LinearLayout) view.findViewById(R.id.answerLayout);
        layoutAnimated = (RelativeLayout) view.findViewById(R.id.layoutAnimated);
        layoutAnimated.setOnClickListener(this);

        // opened
        viewTitleContainerOpened = (LinearLayout) view.findViewById(R.id.title_container_opened);
        viewContentContainerOpened = (FrameLayout) view.findViewById(R.id.content_container_opened);
        viewFeedbackContainerOpened = (LinearLayout) view.findViewById(R.id.feedback_container_opened);
        viewLikeContainerOpened = (LinearLayout) view.findViewById(R.id.like_container_opened);
        viewChannelingContainerOpened = (LinearLayout) view.findViewById(R.id.channel_container_opened);
        layoutOpened = (LinearLayout) view.findViewById(R.id.layoutOpened);
        layoutOpened.setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        topViewListener = listener;
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

    public void openOpenedView(NRResult result) {
        layoutAnimated.setVisibility(View.GONE);

        mResult = result;

//        mResult.setSingle(true);

        titleView.setTitleText(mResult.getFetchedResult().getTitle());
        titleView.setTitleMaxLines(100);
        titleView.unfold(true);
        viewTitleContainerOpened.addView(titleView);

        setContent();

        setLike();

        if(mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
            setChannel();
        }

        viewContentContainerOpened.addView(contentView);

        if(feedbackView == null) {

            viewLikeContainerOpened.addView(likeView);

            if (mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {

                viewChannelingContainerOpened.addView(channelView);
            }
        } else {

            viewFeedbackContainerOpened.addView(feedbackView);
        }
    }

    public void openView(int y, NRResult result) {

        layoutOpened.setVisibility(View.GONE);

        mResult = result;
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

//        if(!mResult.isSingle()) {
            setTitleYAnimation(y, 0, mResult.isUnfolded());
//        } else {
//            titleView.unfold(true);
//        }
    }

    private void remvoeAllViews(ViewGroup view) {
        if(view.getChildCount() > 0) {
            view.removeAllViews();
        }
    }


    public void openViewAnimation(int titleHeight) {

        Animation fadeInContent = new AlphaAnimation(0, 1);
        fadeInContent.setDuration(700);

        Animation fadeInLike = new AlphaAnimation(0, 1);
        fadeInLike.setDuration(700);
        fadeInLike.setStartOffset(350);

        Animation fadeInChannel = new AlphaAnimation(0, 1);
        fadeInChannel.setDuration(700);
        fadeInChannel.setStartOffset(350);

        answerLayout.setTranslationY(0);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(answerLayout, "TranslationY", titleHeight);
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
        fadeOutContent.setDuration(400);
        removeViewListener(fadeOutContent, viewContentContainer);

        Animation fadeOutLike = new AlphaAnimation(1, 0);
        fadeOutLike.setDuration(400);
        removeViewListener(fadeOutLike, viewLikeContainer);

        Animation fadeOutChannel = new AlphaAnimation(1, 0);
        fadeOutChannel.setDuration(400);
        removeViewListener(fadeOutChannel, viewChannelingContainer);

        // create translation animation
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(answerLayout, "TranslationY", 0);
        objectAnimator.setDuration(400);
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
        objectAnimator.setStartDelay(200);
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
            likeView.showThankYouMsg();
        }
    }

    private void setContent() {
        if (mResult.getFetchedResult().getBody() != null) {
            mResult.getFetchedResult().setBody(mResult.getFetchedResult().getBody());
            contentView.loadData(mResult.getFetchedResult().getBody(), "text/html", "UTF-8");
        } else {
            topViewListener.fetchBodyForResult(contentView, mResult.getFetchedResult().getId(), mResult.getFetchedResult().getHash());
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
                    topViewListener.onFoldItemFinished(false);
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

        if(mResult != null && !mResult.isSingle()) {
            closeViewAnimation();
        } else {
            viewChannelingContainer.removeAllViews();
            viewLikeContainer.removeAllViews();
            viewContentContainer.removeAllViews();
            viewTitleContainer.removeAllViews();

            viewChannelingContainerOpened.removeAllViews();
            viewLikeContainerOpened.removeAllViews();
            viewContentContainerOpened.removeAllViews();
            viewTitleContainerOpened.removeAllViews();
        }
    }

    @Override
    public void onTitleClicked() {

//        if(layoutAnimated.getVisibility() == View.VISIBLE) {
        if(!mResult.isSingle()) {
            topViewListener.closeAnswer();
        }
    }

    @Override
    public void onTitleCollapsed(int height) {

        if(layoutAnimated.getVisibility() == View.VISIBLE) {

            if (mResult.isUnfolded()) {

                int feedbachHeight = 50;

                if (mResult.getFetchedResult().getChanneling() != null && mResult.getFetchedResult().getChanneling().size() > 0) {
                    feedbachHeight = 100;

                    viewChannelingContainer.addView(channelView);
                    viewChannelingContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);
                }

                viewContentContainer.addView(contentView);
                int contentHeight = NRResultTopView.this.getHeight() - height - (int) Calculate.pxFromDp(getContext(), feedbachHeight);
                viewContentContainer.getLayoutParams().height = contentHeight;

                viewLikeContainer.addView(likeView);

                viewLikeContainer.getLayoutParams().height = (int) Calculate.pxFromDp(getContext(), 50);

                openViewAnimation(height);
            } else { //going down

                viewTitleContainer.getLayoutParams().height = mResult.getHeight();
                viewTitleContainer.requestLayout();

                answerLayout.setTranslationY(0);

                topViewListener.onFoldItemFinished(true);

                setTitleYAnimation(0, y, false);
            }
        }
    }


    @Override
    public void onShareClicked() {

    }


    public void setChannelView(NRCustomChannelView channelView, NRChannelItem.OnChannelSelectedListener listener) {
        this.channelView = channelView;
        this.channelView.setListener(listener);
    }

    public void setContentView(NRCustomContentView contentView, NRContentView.Listener listener) {
        this.contentView = contentView;
        this.contentView.setListener(listener);
    }

    public void setLikeView(NRCustomLikeView likeView) {
        this.likeView = likeView;
        this.likeView.setListener(this);
    }

    public void setTitleView(NRCustomTitleView titleView) {
        this.titleView = titleView;
        this.titleView.setListener(this);
    }

    public void setFeedbackView(NRCustomFeedbackView feedbackView) {
        this.feedbackView = feedbackView;
        this.feedbackView.setListener(this);
    }

    public void removeTitleView () {

        viewTitleContainer.removeAllViews();
        titleView.resetView();
    }

    public void setResultUnFoldState(boolean unFoldState) {
        mResult.setUnfolded(unFoldState);
    }

    public NRResult getmResult() {
        return mResult;
    }

}
