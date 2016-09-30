package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import nanorep.nanowidget.interfaces.NRResultItemListener;
import nanorep.nanowidget.interfaces.NRSuggestionsListener;
import nanorep.nanowidget.interfaces.NRTitleListener;
import nanorep.nanowidget.interfaces.OnLikeListener;

/**
 * Created by nanorep on 29/09/2016.
 */

public abstract class NRCustomTitleView extends LinearLayout{

    protected NRTitleListener mListener;

    public NRCustomTitleView(Context context) {
        super(context);
    }

    public void setListener(NRTitleListener listener) {
        mListener = listener;
    }

    abstract public void setTitleText(String text, boolean unfolded);

    abstract public void hideUnfoldButton(boolean fold);

    abstract public String getTitleText();

    abstract public int getTitleMeasuredHeight();

    abstract public ImageButton getUnFoldButton();
}
