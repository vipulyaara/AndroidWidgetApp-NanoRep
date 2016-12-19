package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 04/09/2016.
 */

public class MyWebView extends FrameLayout implements View.OnKeyListener {

    WebView webView;
    RelativeLayout webLoadingView;

    private Listener mListener;

    public interface Listener {
        void onDismiss();
    }

    public MyWebView(Context context, String url, MyWebView.Listener listener) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.nr_webview_view, this);

        webView = (WebView) view.findViewById(R.id.nrWebview);
        webLoadingView = (RelativeLayout) view.findViewById(R.id.webLoadingView);

        webLoadingView.setVisibility(View.VISIBLE);

        mListener = listener;

        webView.setOnKeyListener(this);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webLoadingView.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                if(webView.canGoBack()) {

                    webView.goBack();
                } else {
                    mListener.onDismiss();
                }

                return true;
            }
        }

        return false;
    }
}
