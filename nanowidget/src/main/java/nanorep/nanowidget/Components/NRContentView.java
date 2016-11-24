package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Response.NRHtmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.R;


/**
 * Created by nissimpardo on 21/08/2016.
 */

public class NRContentView extends NRCustomContentView implements View.OnKeyListener{

//    private NRContentView.Listener mListener;
    private WebView mWebView;
    private RelativeLayout mLoadingView;
    boolean loadingFinished = true;
    boolean redirect = false;
    private Pattern mPattern;

    public interface Listener {
        void onLinkedArticleClicked(String articleId);
        void onLinkClicked(String url);
        void onDismiss();
    }

//    public NRContentView(Context context) {
//        super(context);
//        LayoutInflater.from(context).inflate(R.layout.content, this);
//    }

    public NRContentView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content, this);
        mPattern = Pattern.compile("(?<=src=\")[^\"]*(?<!\")");
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mWebView = (WebView) child.findViewById(R.id.nrWebview);

        mWebView.setOnKeyListener(this);

        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(new NRWebClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                mLoadingView.setVisibility(VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                    mLoadingView.setVisibility(INVISIBLE);
//                    view.clearCache(true);
                } else{
                    redirect = false;
                }

            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100 && mLoadingView.getVisibility() == VISIBLE) {
//                    mLoadingView.setVisibility(INVISIBLE);
//                }
//            }
        });

        mLoadingView = (RelativeLayout) child.findViewById(R.id.webLoadingView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mWebView.requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

    public void setListener(NRContentView.Listener listener) {
        mListener = listener;
    }


    @Override
    public void loadData(final String data, final String mimeType, final String encoding) {
//        mLoadingView.setVisibility(VISIBLE);
//        Matcher matcher = mPattern.matcher(data);
//        String domain = null;
//        while (matcher.find()) {
//            domain = matcher.group();
//        }

        String iframe = isFrameExist(data);
        String domain = null;

        if(iframe != null) {
            domain = getDomain(iframe);
        }

        if (domain == null) {
            domain = "file://";
            loadRedirectedUrl(domain, data, mimeType, encoding);
        } else {
            HandlerThread fetchRedirect = new HandlerThread("fetchRedirect");
            fetchRedirect.start();
            Handler handler = new Handler(fetchRedirect.getLooper());
            final String finalDomain = domain;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        final URLConnection con = new URL(finalDomain).openConnection();
                        con.connect();
                        InputStream is = con.getInputStream();
                        is.close();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                loadRedirectedUrl(con.getURL().toString(), data, mimeType, encoding);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private String isFrameExist(String data) {
        String iframe = null;

        int start = data.indexOf("<iframe");

        if(start != -1) { // exist
            iframe = data.substring(start);
            int end = iframe.indexOf("</iframe>");

            if(end == -1){
                end = iframe.indexOf("/>");
            }

            if(end != -1) {
                iframe = iframe.substring(0, end);
            }
        }

        return iframe;
    }

    private String getDomain(String iframe) {
        String domain = null;

        Matcher matcher = mPattern.matcher(iframe);

        while (matcher.find()) {
            domain = matcher.group();
        }

        return domain;
    }

    private void loadRedirectedUrl(String url, String data, String mimeType, String encoding) {
        NRHtmlParser parser = new NRHtmlParser(data);
        String parsed = parser.getParsedHtml();

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
        mWebView.loadDataWithBaseURL(url, parsed, mimeType, encoding, "file://");
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mListener.onDismiss();
                return true;
            }
        }

        return false;
    }

}
