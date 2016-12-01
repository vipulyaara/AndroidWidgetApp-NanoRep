package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.widget.FrameLayout;

import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.interfaces.NRApplicationContentListener;
import nanorep.nanowidget.interfaces.NRTitleListener;

/**
 * Created by nanorep on 02/10/2016.
 */

public abstract class NRCustomContentView extends FrameLayout {

    protected NRContentView.Listener mListener;

    protected NRApplicationContentListener applicationContentListener;

    public void setListener(NRContentView.Listener listener) {
        mListener = listener;
    }

    public void setApplicationContentListener(NRApplicationContentListener applicationContentListener) {
        this.applicationContentListener = applicationContentListener;
    }

    public NRCustomContentView(Context context) {
        super(context);
    }

    public abstract void loadData(String data, String mimeType, String encoding);

    public abstract void loadUrl(String url);
}

