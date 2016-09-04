package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRHtmlParser;

import nanorep.nanowidget.R;


/**
 * Created by nissimpardo on 21/08/2016.
 */

public class NRWebView extends FrameLayout {

    private NRWebView.Listener mListener;
    private MyWebView mWebView;
    private RelativeLayout mLoadingView;

    public interface Listener {
        void onLinkedArticleClicked(String articleId);
        void onLinkClicked(String url);
    }

    public NRWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.webview, this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mWebView = (MyWebView) child.findViewById(R.id.nrWebview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new NRWebClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && mLoadingView.getVisibility() == VISIBLE) {
                    mLoadingView.setVisibility(INVISIBLE);
                }
            }
        });
        mLoadingView = (RelativeLayout) child.findViewById(R.id.webLoadingView);
    }

    public void setListener(NRWebView.Listener listener) {
        mListener = listener;
    }



    public void loadData(String data, String mimeType, String encoding) {
        mLoadingView.setVisibility(VISIBLE);
        NRHtmlParser parser = new NRHtmlParser(data);
        String parsed = parser.getParsedHtml();
        mWebView.loadDataWithBaseURL("file://", parsed, mimeType, encoding, "file://");
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
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
            } else if (link.startsWith("http")) {
                mListener.onLinkClicked(link);
                return true;
            }
            return false;
        }
    }
}
