package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 17/08/2016.
 */

public class NRResultTitleView extends RelativeLayout {
    Listener mListener;
    ImageButton mShareButton;

    interface Listener {
        void onSharePressed();
    }

    public NRResultTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.result_title_view, this);
    }



    void setListener(Listener listener) {
        mListener = listener;
    }

    void setTitle(String title) {
        ((Button)findViewById(R.id.resultTitle)).setText(title);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() == R.id.shareButton) {
            mShareButton = (ImageButton) child;
            mShareButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onSharePressed();
                }
            });
        }
    }
}
