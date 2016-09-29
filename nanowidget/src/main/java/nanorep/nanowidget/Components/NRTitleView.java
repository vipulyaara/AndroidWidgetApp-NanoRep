package nanorep.nanowidget.Components;

import android.content.Context;
import android.view.LayoutInflater;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.R;

/**
 * Created by nanorep on 29/09/2016.
 */

public class NRTitleView extends NRCustomTitleView {

    public NRTitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);
    }
}
