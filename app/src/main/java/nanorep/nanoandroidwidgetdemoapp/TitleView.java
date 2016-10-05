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
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mTitleButton = (Button) child.findViewById(nanorep.nanowidget.R.id.titleButton);
    }

    @Override
    public void setTitleText(String text, boolean unfolded) {
        mTitleButton.setText(text);

        mTitleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked();
            }
        });
    }


    @Override
    public ImageButton getUnFoldButton() {
        return null;
    }

    @Override
    public Button getTitleButton() {
        return mTitleButton;
    }

}
