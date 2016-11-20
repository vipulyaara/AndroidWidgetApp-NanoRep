package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.widget.LinearLayout;

import nanorep.nanowidget.interfaces.NRSearchBarListener;

/**
 * Created by nanorep on 29/09/2016.
 */

public abstract class NRCustomSearchBarView extends LinearLayout{

    protected NRSearchBarListener mListener;

    public NRCustomSearchBarView(Context context) {
        super(context);
    }

    public void setListener(NRSearchBarListener listener) {
        mListener = listener;
    }

    public abstract String getText();

    public abstract void dismissKeyboard();

    public abstract void updateText(String text);

    public abstract void updateEditTextView(String text);

    public abstract void setHint(String text);
}
