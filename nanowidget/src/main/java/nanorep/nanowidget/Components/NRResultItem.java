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

    public enum RowType {
        TITLE, CONTENT, LIKE, CHANNELING
    }

    public NRResultItem(View itemView, NRResultItemListener listener, int maxHeight, NRConfiguration config) {
        super(itemView);

        initObjectsView(itemView, maxHeight);

        configViewObjects(config);

        setListener(listener);
    }

    protected abstract void configViewObjects(NRConfiguration config);

    protected abstract void initObjectsView(View view, int maxHeight);

    public abstract void setResult(NRResult result);

    protected void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

}
