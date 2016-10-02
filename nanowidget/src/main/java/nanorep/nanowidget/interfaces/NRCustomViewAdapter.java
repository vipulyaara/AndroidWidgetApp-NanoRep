package nanorep.nanowidget.interfaces;

import android.content.Context;

import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;

/**
 * Created by nanorep on 28/09/2016.
 */

public interface NRCustomViewAdapter {

    NRCustomSearchBarView getSearchBar(Context context);

    NRCustomSuggestionsView getSuggestionsView(Context context);

    NRCustomTitleView getTitle(Context context);

    NRCustomContentView getContent(Context context);
}
