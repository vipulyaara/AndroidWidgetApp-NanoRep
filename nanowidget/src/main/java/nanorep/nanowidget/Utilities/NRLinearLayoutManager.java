package nanorep.nanowidget.Utilities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by nissimpardo on 30/08/2016.
 */

public class NRLinearLayoutManager extends LinearLayoutManager {

    public NRLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
