package nanorep.nanowidget.Components;

import android.content.Context;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBar;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

/**
 * Created by nanorep on 28/09/2016.
 */

public class NRViewAdapter implements NRCustomViewAdapter {

    @Override
    public NRCustomSearchBar getSearchBar(Context context) {
        return new NRSearchBar(context);
    }

    @Override
    public NRCustomSuggestionsView getSuggestionsView(Context context) {
        return new NRSuggestionsView(context);
    }
}
