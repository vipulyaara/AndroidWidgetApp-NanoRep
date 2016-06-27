package nanorep.nanowidget.interfaces;

import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRResultItem;

/**
 * Created by nissimpardo on 18/06/16.
 */
public interface NRResultItemListener extends NRChannelItem.OnChannelSelectedListener {
    void shouldFetchFAQAnswerBody(NRResultItem item, String answerId);
    void unfoldItem(NRResultItem item);
    void onShareClicked(NRResultItem item, String linkToShare);
    void onLikeClicked(NRResultItem item);

    @Override
    void onChannelSelected(NRChannelItem channelItem);
}