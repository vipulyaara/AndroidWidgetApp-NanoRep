package nanorep.nanowidget.Components;

import android.content.Context;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomFeedbackView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

/**
 * Created by nanorep on 28/09/2016.
 */

public class NRViewAdapter implements NRCustomViewAdapter {

    NRSearchBar searchBar;
    NRSuggestionsView suggestionsView;
//    NRTitleView titleView;
//    NRContentView contentView;

    @Override
    public NRCustomSearchBarView getSearchBar(Context context) {
        if(searchBar == null) {
            searchBar = new NRSearchBar(context);
        }
        return searchBar;
    }

    @Override
    public NRCustomSuggestionsView getSuggestionsView(Context context) {
        if(suggestionsView == null) {
            suggestionsView = new NRSuggestionsView(context);
        }
        return suggestionsView;
    }

    @Override
    public NRCustomTitleView getTitle(Context context) {
//        if(titleView == null) {
//            titleView = new NRTitleView(context);
//        }
        return null;
    }

    @Override
    public NRCustomContentView getContent(Context context) {
//        if(contentView == null) {
//            contentView = new NRContentView(context);
//        }
        return new NRContentView(context);
    }

    @Override
    public NRCustomLikeView getLikeView(Context context) {
        return new NRLikeView(context);
    }

    @Override
    public NRCustomChannelView getChannelView(Context context) {
        return new NRChannelingView(context);
    }

    @Override
    public NRCustomFeedbackView getFeedbackView(Context context) {
        return null;
    }
}
