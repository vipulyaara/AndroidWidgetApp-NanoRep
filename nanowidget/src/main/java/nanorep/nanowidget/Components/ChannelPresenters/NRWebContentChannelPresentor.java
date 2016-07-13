package nanorep.nanowidget.Components.ChannelPresenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import NanoRep.Chnneling.NRChanneling;
import NanoRep.Chnneling.NRChannelingChatForm;
import NanoRep.Chnneling.NRChannelingContactForm;
import NanoRep.Chnneling.NRChannelingOpenCustomURL;
import NanoRep.NanoRep;
import nanorep.nanowidget.Components.NRResultFragment;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.NRWidgetFragment;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 28/06/16.
 */
public class NRWebContentChannelPresentor implements NRChannelPresentor{
    private NRResultFragment mResultFragment;
    private NanoRep mNanoRep;
    private NRChanneling mChanneling;

    public NRWebContentChannelPresentor(NRResultFragment resultFragment, NanoRep nanoRep) {
        mResultFragment = resultFragment;
        mNanoRep = nanoRep;
    }

    @Override
    public void present() {
//        mNanoWidget.getChannelingWebView().setVisibility(View.VISIBLE);
        Uri.Builder channelUri = new Uri.Builder();
        channelUri.scheme("http").authority("dev4.nanorep.com");
        String url = null;
        switch (mChanneling.getType()) {
            case ContactForm:
                channelUri.appendPath("sdk/mobile/contactform.html");
                channelUri.appendQueryParameter("account", mNanoRep.getAccountName());
                channelUri.appendQueryParameter("articleId", mChanneling.getQueryResult().getId());
                channelUri.appendQueryParameter("context", "null").appendQueryParameter("host", "my.nanorep.com");
                channelUri.appendQueryParameter("kb", mNanoRep.getKnowledgeBase());
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
        String passUrl = url == null ? channelUri.toString() : url;
        mResultFragment.getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left).add(R.id.content_id, NRWebContentFragment.newInstance(passUrl, null)).addToBackStack("test2").commit();
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
