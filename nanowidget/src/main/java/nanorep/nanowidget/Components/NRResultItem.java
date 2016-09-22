package nanorep.nanowidget.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanorep.nanoclient.Response.NRConfiguration;

import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nanorep on 22/09/2016.
 */

public abstract class NRResultItem extends RecyclerView.ViewHolder {

    protected NRResultItemListener mListener;

    public NRResultItem(View itemView, int maxHeight, NRConfiguration config) {
        super(itemView);

        initObjectsView(itemView, maxHeight);

        configViewObjects(config);
    }

    protected abstract void configViewObjects(NRConfiguration config);

    protected abstract void initObjectsView(View view, int maxHeight);

    public abstract void setResult(NRResult result);

    public void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

}
