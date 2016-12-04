package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;

/**
 * Created by nanorep on 05/10/2016.
 */

public class GettTitleView extends NRCustomTitleView {

    private Button mTitleButton;

    private String textColorAnswer = "#1e1e1e";

    private String textFontAnswer = "sans-serif-medium";

    private LinearLayout divider;

    public GettTitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.title_gett, this);
    }

    @Override
    public void setTitleText(String text) {
        mTitleButton.setText(text);

        mTitleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });
    }

    @Override
    public void unfold(boolean closed) {
        mTitleButton.setTextColor(Color.parseColor(textColorAnswer));
        mTitleButton.setTypeface(Typeface.create(textFontAnswer, Typeface.NORMAL));
        mTitleButton.setMaxLines(100);
        mTitleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        mTitleButton.setPadding(0,0,0,0);
        mListener.onTitleCollapsed(getCollapsedHeight(mTitleButton.getText()));
        divider.setVisibility(View.GONE);
    }

    public int getCollapsedHeight(CharSequence text) {

        int mMeasuredHeight = (new StaticLayout(text, mTitleButton.getPaint(), mTitleButton.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)).getHeight();

        if(mMeasuredHeight < mTitleButton.getHeight()) {
            mMeasuredHeight = mTitleButton.getHeight();
        }
        return mMeasuredHeight;
    }

    @Override
    public int getTitleHeight(String text) {
        return 0;
    }

    @Override
    public void resetView() {

    }

    @Override
    public void setTitleMaxLines(int lines) {
        mTitleButton.setMaxLines(lines);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mTitleButton = (Button) child.findViewById(R.id.titleButton);
        divider = (LinearLayout) child.findViewById(R.id.divider);
    }

}
