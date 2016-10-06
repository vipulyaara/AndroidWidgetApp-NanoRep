package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRHtmlParser;

import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.R;


/**
 * Created by nissimpardo on 21/08/2016.
 */

public class NRContentView extends NRCustomContentView {

//    private NRContentView.Listener mListener;
    private MyWebView mWebView;
    private RelativeLayout mLoadingView;

    public interface Listener {
        void onLinkedArticleClicked(String articleId);
        void onLinkClicked(String url);
    }

    public NRContentView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content, this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mWebView = (MyWebView) child.findViewById(R.id.nrWebview);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);
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

    public void setListener(NRContentView.Listener listener) {
        mListener = listener;
    }


    @Override
    public void loadData(String data, String mimeType, String encoding) {
        mLoadingView.setVisibility(VISIBLE);
        NRHtmlParser parser = new NRHtmlParser(data);
        String parsed = parser.getParsedHtml();
//        String parsed = "<div style=\"width=300px\" >" + parser.getParsedHtml() + "</div>";
        String script = "\n" +
                "<style>\n" +
                "        img {\n" +
                "            max-width: 100% !important;\n" +
                "            height: auto !important;\n" +
                "            }\n" +
                "        body {\n" +
                "            font-family: Roboto-Light;\n" +
                "            color: #6c6c6c;\n" +
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

    @Override
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
