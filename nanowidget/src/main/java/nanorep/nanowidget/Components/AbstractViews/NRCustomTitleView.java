package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    abstract public void setTitleText(String text);

    abstract public void unfold(boolean closed);

    abstract public int getTitleHeight(String text);

    public abstract void resetView();

//    abstract public ImageButton getUnFoldButton();

//    abstract public Button getTitleButton();

//    abstract public void setTitleColor(String color);
}
