package nanorep.nanoandroidwidgetdemoapp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.crittercism.app.Crittercism;
import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.NanorepBuilder;
import com.nanorep.nanoclient.Response.NRConfiguration;


import java.util.HashMap;

import nanorep.nanowidget.NRWidgetFragment;

public class MainActivity extends AppCompatActivity implements NRWidgetFragment.NRWidgetFragmentListener {

    private NRWidgetFragment nanoFragment;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        nanoFragment = NRWidgetFragment.newInstance();
        nanoFragment.setListener(this);
        Crittercism.initialize(getApplicationContext(), "d59e30ede3c34d0bbf19d0237c2f1bc800444503");
        Button loadButton = (Button)findViewById(R.id.button);
        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setVisibility(View.INVISIBLE);
                    Nanorep.AccountParams accountParams = new Nanorep.AccountParams();
//                    accountParams.setAccount("yatra");
//                    accountParams.setKnowledgeBase("79848779");
                    EditText accountName = (EditText) findViewById(R.id.accountNameId);
                    EditText kb = (EditText) findViewById(R.id.kbId);
                    accountParams.setAccount(accountName.getText().toString());
                    accountParams.setKnowledgeBase(accountName.getText().toString());
//                    HashMap<String, String> channel = new HashMap();
//                    channel.put("channel", "mobile");
//                    accountParams.setContext(channel);
                    Nanorep test = NanorepBuilder.createNanorep(getApplicationContext(), accountParams);

                    test.getNRConfiguration().getTitle().setTitleBGColor("#FF7F23");
                    test.getNRConfiguration().setAutocompleteEnabled("false");
                    test.getNRConfiguration().getSearchBar().setInitialText("noa noa");

                    nanoFragment.setNanoRep(test);
                    getSupportFragmentManager().beginTransaction().add(R.id.root_layout, nanoFragment, "test").commit();
//                    test.fetchConfiguration(new Nanorep.OnConfigurationFetchedListener() {
//                        @Override
//                        public void onConfigurationReady(NRConfiguration configuration, NRError error) {
//
//                        }
//                    });
                }
            });
        }
    }

    @Override
    public void onCancelWidget(NRWidgetFragment widgetFragment) {
        ((Button)findViewById(R.id.button)).setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(nanoFragment).commit();
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
