package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 18/08/2016.
 */

public class NRLinkedArticlesBrowserView extends RelativeLayout {
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private State mState = State.single;
    private Listener mListener;


    public enum State {
        single,
        hasNext,
        hasPrev,
        hasNextAndPrev
    }

    public interface Listener {
        void onNextClicked();
        void onPrevClicked();
    }

    public NRLinkedArticlesBrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.linked_articles_browser_view, this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setState(State state) {
        if (mState != state) {
            String nextImageName = "disabled_right_arrow";
            String prevImageName = "disabled_left_arrow";
            mState = state;
            switch (state) {
                case hasNext:
                    nextImageName = "right_arrow";
                    break;
                case hasPrev:
                    prevImageName = "left_arrow";
                    break;
                case hasNextAndPrev:
                    nextImageName = "right_arrow";
                    prevImageName = "left_arrow";
                    break;
            }
            mNextButton.setImageResource(resId(nextImageName));
            mPrevButton.setImageResource(resId(prevImageName));
        }
    }

    private int resId(String resName) {
        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mNextButton = (ImageButton) child.findViewById(R.id.rightArrow);
        mPrevButton = (ImageButton) child.findViewById(R.id.leftArrow);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNextClicked();
            }
        });
        mPrevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPrevClicked();
            }
        });
    }
}
