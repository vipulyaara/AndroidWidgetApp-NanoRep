package nanorep.nanowidget.Components.ChannelPresenters;


import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Nanorep;

import nanorep.nanowidget.Components.NRResultFragment;


/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRChannelStrategy {
    public static NRChannelPresentor presentor(NRChanneling channeling, NRResultFragment fragment, Nanorep nanoRep) {
        NRChannelPresentor presentor = null;
        switch (channeling.getType()) {
            case OpenCustomURL:
            case ChatForm:
            case ContactForm:
                presentor = new NRWebContentChannelPresentor(fragment, nanoRep);
                break;
            case PhoneNumber:
                presentor = new NRPhoneChannelPresentor(fragment.getContext());
                break;
        }
        presentor.setChannel(channeling);
        return presentor;
    }

}
