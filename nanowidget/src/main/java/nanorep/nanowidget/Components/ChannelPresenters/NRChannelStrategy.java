package nanorep.nanowidget.Components.ChannelPresenters;


import android.content.Context;

import NanoRep.Chnneling.NRChanneling;
import nanorep.nanowidget.NRWidgetFragment;

/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRChannelStrategy {
    public static NRChannelPresentor presentor(NRChanneling channeling, NRWidgetFragment fragment) {
        NRChannelPresentor presentor = null;
        switch (channeling.getType()) {
            case OpenCustomURL:
            case ChatForm:
            case ContactForm:
                presentor = new NRWebContentChannelPresentor(fragment);
                break;
            case PhoneNumber:
                presentor = new NRPhoneChannelPresentor(fragment.getContext());
                break;
        }
        presentor.setChannel(channeling);
        return presentor;
    }

}
