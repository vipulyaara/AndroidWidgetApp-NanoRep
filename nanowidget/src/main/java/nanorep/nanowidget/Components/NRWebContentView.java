package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.NRWebClient;

/**
 * Created by nissimpardo on 04/07/16.
 */
public class NRWebContentView extends LinearLayout {
    private WebView mWebView;
    private Button mCloseButton;

    public NRWebContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() == R.id.channelingWebview) {
            mWebView = (WebView) child;
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new NRWebClient());
        } else if (child instanceof RelativeLayout) {
            mCloseButton = (Button) child.findViewById(R.id.closeWebviewButton);
            mCloseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWebView.stopLoading();
                    setVisibility(INVISIBLE);
                }
            });
        }
    }
}
