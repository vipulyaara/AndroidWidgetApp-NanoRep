package nanorep.nanowidget.Components;

import android.view.View;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Response.NRConfiguration;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nanorep on 25/09/2016.
 */

public class NRChannelingItem extends NRResultItem{

    private NRChannelingView mChannelingView;

    public NRChannelingItem(View view, NRResultItemListener listener, NRConfiguration config) {
        super(view, listener, config);
    }

    @Override
    protected void configViewObjects(NRConfiguration config) {

    }

    @Override
    protected void bindViews(View view) {
        mChannelingView = (NRChannelingView) itemView.findViewById(R.id.cv_channelingView);
    }

    @Override
    public void setData(NRResult result) {

        ArrayList<NRChanneling> channelings = result.getFetchedResult().getChanneling();

        for (NRChanneling channeling : channelings) {
            channeling.setQueryResult(result.getFetchedResult());
        }
        mChannelingView.setChannelings(channelings);
    }

    @Override
    protected void setListener(NRResultItemListener listener) {
        super.setListener(listener);

        mChannelingView.setListener(mListener);
    }
}
