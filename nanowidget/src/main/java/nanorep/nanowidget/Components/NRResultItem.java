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
    protected NRResult mResult;

    public enum RowType {
        TITLE, CONTENT, LIKE, CHANNELING
    }

    public NRResultItem(View itemView, NRResultItemListener listener, NRConfiguration config) {
        super(itemView);

        bindViews(itemView);

        setListener(listener);

        configViewObjects(config);
    }

    protected abstract void configViewObjects(NRConfiguration config);

    protected abstract void bindViews(View view);

    public abstract void setData(NRResult result);

    protected void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

    public abstract void resetBody();

    public abstract void updateBody();
}
