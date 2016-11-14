package nanorep.nanowidget.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by nissimpardo on 04/09/2016.
 */

public class MyWebView extends WebView implements View.OnKeyListener {


    private Listener mListener;

    public interface Listener {
        void onDismiss();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, String url, MyWebView.Listener listener) {
        super(context);

        mListener = listener;

        setOnKeyListener(this);
        setWebViewClient(new WebViewClient());
        loadUrl(url);
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

                if(canGoBack()) {

                    goBack();
                } else {
                    mListener.onDismiss();
                }

                return true;
            }
        }

        return false;
    }
}
