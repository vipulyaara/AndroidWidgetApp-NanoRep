package nanorep.nanowidget.interfaces;

import android.content.Context;

import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBar;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;

/**
 * Created by nanorep on 28/09/2016.
 */

public interface NRCustomViewAdapter {

    NRCustomSearchBar getSearchBar(Context context);

    NRCustomSuggestionsView getSuggestionsView(Context context);
}
