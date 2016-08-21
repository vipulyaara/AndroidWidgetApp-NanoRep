package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 18/08/2016.
 */

public class NRLinkedArticlesBrowserView extends RelativeLayout {

    public NRLinkedArticlesBrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.linked_articles_browser_view, this);
    }
}
