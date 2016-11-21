package nanorep.nanoandroidwidgetdemoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.crittercism.app.Crittercism;
import com.crashlytics.android.Crashlytics;
import com.nanorep.nanoclient.NRImpl;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.NanorepBuilder;


import io.fabric.sdk.android.Fabric;
import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.Fragments.NRMainFragment;
import nanorep.nanowidget.Utilities.FragmentUtils;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

public class MainActivity extends AppCompatActivity implements NRCustomViewAdapter {

//    private NRWidgetFragment nanoFragment;
    private NRMainFragment mainFragment;
    private CheckBox checkBox;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0aa0ff")));



//        openMainFragment();

        checkBox = (CheckBox) findViewById(R.id.checkbox);


        final EditText accountName = (EditText) findViewById(R.id.accountNameId);
        final EditText kb = (EditText) findViewById(R.id.kbId);
        EditText server = (EditText) findViewById(R.id.serverId);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);

        final String _server = server.getText().toString();
        final Button loadButton = (Button)findViewById(R.id.button);


        final Button prepareButton = (Button)findViewById(R.id.prepareButton);
        prepareButton.setVisibility(View.VISIBLE);
        prepareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);

                String _accountName = accountName.getText().toString();
                String _kb = kb.getText().toString();

                if(isEmpty(_accountName)) {
                    Toast.makeText(MainActivity.this, "Please fill in your account", Toast.LENGTH_LONG).show();
                    v.setVisibility(View.VISIBLE);
                    return;
                }

                if(isEmpty(_kb)) {
                    Toast.makeText(MainActivity.this, "Please fill in your kb", Toast.LENGTH_LONG).show();
                    v.setVisibility(View.VISIBLE);
                    return;
                }

                NRImpl.getInstance().init(getApplicationContext(), _accountName, _kb);
                pb.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(NRImpl.getInstance().getNRConfiguration().getmParams() == null || (NRImpl.getInstance().getNRConfiguration().getmParams() != null && NRImpl.getInstance().getNRConfiguration().getmParams().size() == 0)) {
//                            NRImpl.getInstance().reset();
                            Toast.makeText(MainActivity.this, "Wrong account or kb", Toast.LENGTH_LONG).show();
                            prepareButton.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        } else {
                            loadButton.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        }
                    }
                }, 5000);

            }
        });


        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setVisibility(View.INVISIBLE);
                    mainFragment = NRMainFragment.newInstance();
                    FragmentUtils.openFragment(mainFragment, R.id.content_main,
                            NRMainFragment.TAG, MainActivity.this, false);
                }
            });
        }
    }

    private void openMainFragment() {
        mainFragment = NRMainFragment.newInstance();
        FragmentUtils.openFragment(mainFragment, R.id.content_main,
                NRMainFragment.TAG, MainActivity.this, false);
    }

    private boolean isEmpty(String str) {
        return str == null || (str != null && "".equals(str));
    }


//    @Override
//    public void onCancelWidget(NRWidgetFragment widgetFragment) {
//        ((Button)findViewById(R.id.button)).setVisibility(View.VISIBLE);
//    }


    @Override
    public NRCustomSearchBarView getSearchBar(Context context) {
//        NRCustomSearchBarView searchBar = new NRSearchBar(context);
//
//        return searchBar;

        return null;
    }

    @Override
    public NRCustomSuggestionsView getSuggestionsView(Context context) {
//        NRSuggestionsView suggestionsView = new NRSuggestionsView(context);
//
//        return suggestionsView;

        return  null;
    }

    @Override
    public NRCustomTitleView getTitle(Context context) {
//        NRTitleView titleView = new NRTitleView(context);
//        TitleView titleView = new TitleView(context);

//        return titleView;

        return null;
    }

    @Override
    public NRCustomContentView getContent(Context context) {
        NRContentView contentView = new NRContentView(context);

        return contentView;
    }

    @Override
    public NRCustomLikeView getLikeView(Context context) {
        return null;
    }

    @Override
    public NRCustomChannelView getChannelView(Context context) {
        return null;
    }

    private class AppWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    private class AppChromClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }


    }
}
