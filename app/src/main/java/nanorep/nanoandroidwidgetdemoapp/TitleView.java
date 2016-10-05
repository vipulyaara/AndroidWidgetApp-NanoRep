package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;

/**
 * Created by nanorep on 05/10/2016.
 */

public class TitleView extends NRCustomTitleView {

    private Button mTitleButton;

    public TitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(nanorep.nanowidget.R.layout.title_test, this);
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

    }

    @Override
    public int getTitleHeight() {
        return 0;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mTitleButton = (Button) child.findViewById(nanorep.nanowidget.R.id.titleButton);
    }

}
