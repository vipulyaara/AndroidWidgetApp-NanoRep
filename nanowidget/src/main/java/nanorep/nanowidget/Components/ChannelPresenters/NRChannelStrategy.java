package nanorep.nanowidget.Components.ChannelPresenters;


import android.content.Context;

import NanoRep.Chnneling.NRChanneling;

/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRChannelStrategy {
    public static NRChannelPresentor presentor(NRChanneling channeling, Context context) {
        NRChannelPresentor presentor = null;
        switch (channeling.getType()) {
            case ChatForm:
            case ContactForm:
                presentor = new NRWebContentChannelPresentor();
                break;
            case PhoneNumber:
                presentor = new NRPhoneChannelPresentor(context);
                break;
        }
        presentor.setChannel(channeling);
        return presentor;
    }

}
