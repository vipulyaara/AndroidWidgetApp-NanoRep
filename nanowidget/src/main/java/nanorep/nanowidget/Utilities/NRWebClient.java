package nanorep.nanowidget.Utilities;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by nissimpardo on 02/07/16.
 */
public class NRWebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("URL", url);
        return false;
    }
}
