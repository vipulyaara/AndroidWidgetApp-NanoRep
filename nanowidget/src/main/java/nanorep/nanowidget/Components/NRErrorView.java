package nanorep.nanowidget.Components;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Handlers.NRErrorHandler;
import com.nanorep.nanoclient.Response.NRHtmlParser;

import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.R;


/**
 * Created by nissimpardo on 21/08/2016.
 */

public class NRErrorView extends RelativeLayout{

    Button button;
    NRErrorView.Listener listener;

    public interface Listener {
        void tryAgain();
    }

    public NRErrorView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.error_view, this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        button = (Button) child.findViewById(R.id.buttonCloud);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.tryAgain();
            }
        });
    }

    public void setListener(NRErrorView.Listener listener) {
        this.listener = listener;
    }
}
