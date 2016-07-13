package nanorep.nanowidget.Utilities;

import android.view.animation.Interpolator;

/**
 * Created by nissimpardo on 09/07/16.
 */
public class HesitateInterpolator implements Interpolator{
    @Override
    public float getInterpolation(float input) {
        float x = 2.0f * input - 1.0f;
        return 0.5f * (x * x * x + 1.0f);
    }
}
