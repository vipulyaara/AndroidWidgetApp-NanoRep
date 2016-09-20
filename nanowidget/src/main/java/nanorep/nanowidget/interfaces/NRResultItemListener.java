package nanorep.nanowidget.interfaces;

import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRContentItem;
import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRWebView;
import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 18/06/16.
 */
public interface NRResultItemListener extends NRWebView.Listener, OnLikeListener, NRChannelItem.OnChannelSelectedListener {
    void unfoldItem(NRResult result, boolean clear);
    void onShareClicked(NRResultItem item, String linkToShare);
    void fetchBodyForResult(NRContentItem item, String resultID, Integer resultHash);
}
