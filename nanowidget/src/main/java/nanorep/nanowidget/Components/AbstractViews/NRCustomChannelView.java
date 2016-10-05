package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.widget.LinearLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import java.util.ArrayList;

import nanorep.nanowidget.Components.NRChannelItem;

/**
 * Created by nanorep on 05/10/2016.
 */

public abstract class NRCustomChannelView extends LinearLayout {
    protected NRChannelItem.OnChannelSelectedListener mListener;

    public NRCustomChannelView(Context context) {
        super(context);
    }


    public void setListener(NRChannelItem.OnChannelSelectedListener listener) {
        mListener = listener;
    }

    public abstract void setChannelings(ArrayList<NRChanneling> channelings);
}
