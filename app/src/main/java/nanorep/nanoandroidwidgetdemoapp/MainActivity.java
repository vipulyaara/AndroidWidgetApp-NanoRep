package nanorep.nanoandroidwidgetdemoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.crittercism.app.Crittercism;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.NanorepBuilder;


import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.Components.NRSearchBar;
import nanorep.nanowidget.Fragments.NRWidgetCategoriesFragment;
import nanorep.nanowidget.Fragments.NRWidgetFragment;
import nanorep.nanowidget.Utilities.FragmentUtils;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

public class MainActivity extends AppCompatActivity implements NRWidgetFragment.NRWidgetFragmentListener, NRCustomViewAdapter {

//    private NRWidgetFragment nanoFragment;
    private NRWidgetCategoriesFragment categoriesFragment;
    private CheckBox checkBox;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0aa0ff")));

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        categoriesFragment = NRWidgetCategoriesFragment.newInstance();

//        nanoFragment = NRWidgetFragment.newInstance();
//        nanoFragment.setListener(this);
        Crittercism.initialize(getApplicationContext(), "d59e30ede3c34d0bbf19d0237c2f1bc800444503");

        categoriesFragment.setViewAdapter(this);
//        nanoFragment.setViewAdapter(this);

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
                    EditText server = (EditText) findViewById(R.id.serverId);


                    String _accountName = "qa";//"gett";//"nanorep";
                    String _kb = "qa";//"English_IL";//"English";


                    String _server = server.getText().toString();

                    if(!_server.isEmpty()) {
                        accountParams.setmHost(_server);
                    }

//                    String _accountName = accountName.getText().toString();
//                    String _kb = kb.getText().toString();

                    accountParams.setAccount(_accountName);
                    accountParams.setKnowledgeBase(_kb);
//                    HashMap<String, String> channel = new HashMap();
//                    channel.put("channel", "mobile");
//                    accountParams.setContext(channel);
                    Nanorep nanorep = NanorepBuilder.createNanorep(getApplicationContext(), accountParams);

//                    nanorep.getNRConfiguration().getTitle().setTitleBGColor("#FF7F23");
//                    nanorep.getNRConfiguration().setAutocompleteEnabled("false");
//                    nanorep.getNRConfiguration().getSearchBar().setInitialText("noa noa");

                    nanorep.setDebugMode(checkBox.isChecked());

                    categoriesFragment.setNanoRep(nanorep);
//                    nanoFragment.setNanoRep(nanorep);
                    FragmentUtils.openFragment(categoriesFragment, R.id.content_main,
                            NRWidgetCategoriesFragment.TAG, MainActivity.this, false);

//                    getSupportFragmentManager().beginTransaction().add(R.id.content_main, categoriesFragment, "nanorep").commit();
//                    nanoFragment.setNanoRep(nanorep);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.root_layout, nanoFragment, "nanorep").commit();
//                    nanorep.fetchConfiguration(new Nanorep.OnConfigurationFetchedListener() {
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
        getSupportFragmentManager().beginTransaction().remove(categoriesFragment).commit();
    }


    @Override
    public NRCustomSearchBarView getSearchBar(Context context) {
        NRCustomSearchBarView searchBar = new NRSearchBar(context);

        return searchBar;
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
