package nanorep.nanowidget.Components.ChannelPresenters;


import android.content.Context;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Nanorep;


/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRChannelStrategy {
    public static NRChannelPresentor presentor(Context context, NRChanneling channeling, Nanorep nanoRep) {
        NRChannelPresentor presentor = null;
        switch (channeling.getType()) {
            case OpenCustomURL:
            case ChatForm:
            case ContactForm:
                presentor = new NRWebContentChannelPresentor(nanoRep);
                break;
            case PhoneNumber:
                presentor = new NRPhoneChannelPresentor(context);
                break;
            case CustomScript:
                presentor = new NRCustomScriptChannelPresentor(context);
                break;
        }
        presentor.setChannel(channeling);
        return presentor;
    }

}
