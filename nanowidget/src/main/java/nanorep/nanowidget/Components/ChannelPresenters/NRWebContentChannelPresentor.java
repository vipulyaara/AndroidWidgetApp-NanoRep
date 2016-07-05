package nanorep.nanowidget.Components.ChannelPresenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import NanoRep.Chnneling.NRChanneling;
import NanoRep.Chnneling.NRChannelingChatForm;
import NanoRep.Chnneling.NRChannelingContactForm;
import NanoRep.Chnneling.NRChannelingOpenCustomURL;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.NRWidgetFragment;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 28/06/16.
 */
public class NRWebContentChannelPresentor implements NRChannelPresentor{
    private NRWidgetFragment mNanoWidget;
    private NRChanneling mChanneling;

    public NRWebContentChannelPresentor(NRWidgetFragment nanoWidget) {
        mNanoWidget = nanoWidget;
    }

    @Override
    public void present() {
//        mNanoWidget.getChildFragmentManager().beginTransaction().add(R.id.contentId, NRWebFragment.newInstance(null, null)).commit();
        mNanoWidget.getChannelingWebView().setVisibility(View.VISIBLE);
        Uri.Builder channelUri = new Uri.Builder();
        channelUri.scheme("http").authority("dev4.nanorep.com");
        String url = null;
        switch (mChanneling.getType()) {
            case ContactForm:
                channelUri.appendPath("sdk/mobile/contactform.html");
                channelUri.appendQueryParameter("account", mNanoWidget.getFetchedDataManager().getNanoRep().getAccountName());
                channelUri.appendQueryParameter("articleId", mChanneling.getQueryResult().getId());
                channelUri.appendQueryParameter("context", "null").appendQueryParameter("host", "my.nanorep.com");
                channelUri.appendQueryParameter("kb", mNanoWidget.getFetchedDataManager().getNanoRep().getKnowledgeBase());
                channelUri.appendQueryParameter("text", mChanneling.getQueryResult().getTitle());
                channelUri.appendQueryParameter("contactFormId", ((NRChannelingContactForm)mChanneling).getContactForms());
                break;
            case ChatForm:
                channelUri.appendPath("sdk/mobile/chat.html");
                channelUri.appendQueryParameter("channel.chatProvider", ((NRChannelingChatForm) mChanneling).getChatProvider());
                channelUri.appendQueryParameter("channelUri.appendQueryParameter", ((NRChannelingChatForm) mChanneling).getAccountNum());
                channelUri.appendQueryParameter("channel.chatOptions.apiKey", "c774b56ff8a64cbba27743a8d4418b26");
                break;
            case OpenCustomURL:
                channelUri = null;
                url = ((NRChannelingOpenCustomURL)mChanneling).getLinkUrl();
                break;
        }
        mNanoWidget.getChannelingWebView().loadUrl(url == null ? channelUri.toString() : url);
    }

    @Override
    public void setChannel(NRChanneling channeling) {
        mChanneling = channeling;
    }

    @Override
    public NRResult getResult() {
        return null;
    }
}
