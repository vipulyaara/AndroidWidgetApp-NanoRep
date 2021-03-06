package nanorep.nanowidget.Components.ChannelPresenters;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 26/06/16.
 */
public interface NRChannelPresentor {
    void present();
    void setChannel(NRChanneling channeling);
    NRResult getResult();
    String getUrl();
}
