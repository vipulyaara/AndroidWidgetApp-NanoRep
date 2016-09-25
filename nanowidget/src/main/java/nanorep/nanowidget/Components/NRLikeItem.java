package nanorep.nanowidget.Components;

import android.view.View;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nanorep on 23/09/2016.
 */

public class NRLikeItem extends NRResultItem{

    public NRLikeItem(View view, NRResultItemListener listener, int maxHeight, NRConfiguration config) {
        super(view, listener, maxHeight, config);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void bindViews(View view, int maxHeight) {

    }

    @Override
    public void setResult(NRResult result) {

    }
}
