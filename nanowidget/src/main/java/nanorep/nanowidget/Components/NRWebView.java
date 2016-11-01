package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Connection.NRUtilities;
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
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        WebView.setWebContentsDebuggingEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(new NRWebClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && mLoadingView.getVisibility() == VISIBLE) {
                    mLoadingView.setVisibility(INVISIBLE);
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(getContext());
                addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d("tst", "work");
            }
        });
        mLoadingView = (RelativeLayout) child.findViewById(R.id.webLoadingView);
    }

    public void setListener(NRWebView.Listener listener) {
        mListener = listener;
    }



    public void loadData(String data, String mimeType, String encoding) {
//        data = "<iframe width=\"420\" height=\"315\" src=\"http://example.com\" frameborder=\"5\" allowfullscreen></iframe>";
        mLoadingView.setVisibility(VISIBLE);
        NRHtmlParser parser = new NRHtmlParser(data);
        String parsed = parser.getParsedHtml();
        String script = "\n" +
                "<style>\n" +
                "        img {\n" +
                "            max-width: 100% !important;\n" +
                "            height: auto !important;\n" +
                "            }\n" +
                "</style>\n" +
                "\n" +
                "\n" +
                "<script>\n" +
                "\t\t(function() {\n" +
                "\t\t\tvar embeds = document.querySelectorAll('iframe');\n" +
                "\t\t\tfor (var i = 0, embed, content, width, height, ratio, wrapper; i < embeds.length; i++) {\n" +
                "\t\t\t\tembed = embeds[i];\n" +
                "\t\t\t\twidth = embed.getAttribute('width'),\n" +
                "\t\t\t\theight = embed.getAttribute('height')\n" +
                "\t\t\t\tratio = width / height;\n" +
                "\n" +
                "\t\t\t\t// skip frames with relative dimensions\n" +
                "\t\t\t\tif (isNaN(ratio)) continue;\n" +
                "\n" +
                "\t\t\t\t// set wrapper styles\n" +
                "\t\t\t\twrapper = document.createElement('div');\n" +
                "\t\t\t\twrapper.style.position = 'relative';\n" +
                "\t\t\t\twrapper.style.width = width.indexOf('%') < 0 ? parseFloat(width) + 'px' : width;\n" +
                "\t\t\t\twrapper.style.maxWidth = '100%';\n" +
                "\n" +
                "\t\t\t\t// set content styles\n" +
                "\t\t\t\tcontent = document.createElement('div');\n" +
                "\t\t\t\tcontent.style.paddingBottom = 100 / ratio + '%';\n" +
                "\n" +
                "\t\t\t\t// set embed styles\n" +
                "\t\t\t\tembed.style.position = 'absolute';\n" +
                "\t\t\t\tembed.style.width = '100%';\n" +
                "\t\t\t\tembed.style.height = '100%';\n" +
                "\n" +
                "\t\t\t\t// update DOM structure\n" +
                "\t\t\t\tembed.parentNode.insertBefore(wrapper, embed);\n" +
                "\t\t\t\tcontent.appendChild(embed);\n" +
                "\t\t\t\twrapper.appendChild(content);\n" +
                "\t\t\t}\n" +
                "\t\t}());\n" +
                "\t</script>";
        parsed += script;
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
