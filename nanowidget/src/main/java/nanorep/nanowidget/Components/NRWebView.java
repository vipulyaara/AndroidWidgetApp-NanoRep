package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nanorep.nanoclient.Response.NRHtmlParser;


/**
 * Created by nissimpardo on 21/08/2016.
 */

public class NRWebView extends WebView {

    private NRWebView.Listener mListener;

    public interface Listener {
        void onLinkedArticleClicked(String articleId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public NRWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new NRWebClient());
    }

    public void setListener(NRWebView.Listener listener) {
        mListener = listener;
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        NRHtmlParser parser = new NRHtmlParser(data);
        String parsed = parser.getParsedHtml();
        super.loadDataWithBaseURL("file://", parsed, mimeType, encoding, "file://");
    }

    private class NRWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return linkedArticle(url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return linkedArticle(request.getUrl().toString());
        }

        private boolean linkedArticle(String link) {
            if (link.startsWith("nanorep")) {
                String comps[] = link.split("/");
                if (comps != null && comps.length > 0) {
                    mListener.onLinkedArticleClicked(comps[comps.length - 1]);
                }
                return true;
            }
            return false;
        }
    }
}
