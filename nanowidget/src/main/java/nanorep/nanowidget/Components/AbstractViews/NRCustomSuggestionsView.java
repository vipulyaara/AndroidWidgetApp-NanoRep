package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.text.Spannable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import nanorep.nanowidget.interfaces.NRSuggestionsListener;

/**
 * Created by nanorep on 28/09/2016.
 */

public abstract class NRCustomSuggestionsView extends LinearLayout{

    protected NRSuggestionsListener mListener;

    public NRCustomSuggestionsView(Context context) {
        super(context);
    }

    abstract  public void setSuggestions(ArrayList<Spannable> suggestions);

    public void setListener(NRSuggestionsListener listener) {
        mListener = listener;
    }

}
