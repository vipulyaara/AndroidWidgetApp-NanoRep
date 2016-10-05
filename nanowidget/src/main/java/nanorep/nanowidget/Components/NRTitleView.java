package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.R;

/**
 * Created by nanorep on 29/09/2016.
 */

public class NRTitleView extends NRCustomTitleView{

    private Button mTitleButton;
    private ImageButton mUnFoldButton;
    private ImageButton mShareButton;

    public NRTitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.title, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mTitleButton = (Button) child.findViewById(R.id.titleButton);

        mUnFoldButton = (ImageButton) child.findViewById(R.id.unFoldButton);
        mUnFoldButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUnfoldClicked();
            }
        });

        mTitleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });

        mShareButton = (ImageButton) child.findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShareClicked();
            }
        });
    }

    @Override
    public void setTitleText(String text, boolean unfolded) {
        mTitleButton.setText(text);

        int maxLines = 100;

        if(!unfolded) { // answer is closed, max 2 lines
            maxLines = 2;
        }

        mTitleButton.setMaxLines(maxLines);
    }

//    @Override
//    public void hideUnfoldButton(boolean isSingle) {
//        mUnFoldButton.setVisibility(isSingle ? View.INVISIBLE : View.VISIBLE);
//    }

//    @Override
//    public int getTitleMeasuredHeight() {
//        mTitleButton.measure( View.MeasureSpec.makeMeasureSpec(mTitleButton.getWidth(), View.MeasureSpec.AT_MOST),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        return mTitleButton.getMeasuredHeight();
//    }

    public ImageButton getUnFoldButton() {
        return mUnFoldButton;
    }

    @Override
    public Button getTitleButton() {
        return mTitleButton;
    }

    //    @Override
//    public void setTitleColor(String color) {
//        mTitleButton.setTextColor(Color.parseColor(color));
//    }
}
