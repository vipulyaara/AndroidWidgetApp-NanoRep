package nanorep.nanoandroidwidgetdemoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nanorep.nanoclient.AccountParams;
import com.nanorep.nanoclient.Nanorep;

import java.util.HashMap;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomContentView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomFeedbackView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomLikeView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSearchBarView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomSuggestionsView;
import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRChannelingView;
import nanorep.nanowidget.Components.NRContentView;
import nanorep.nanowidget.Fragments.NRMainFragment;
import nanorep.nanowidget.Utilities.FragmentUtils;
import nanorep.nanowidget.interfaces.NRApplicationContentListener;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

public class MainActivity extends AppCompatActivity implements NRCustomViewAdapter, NRApplicationContentListener {

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
        final EditText server = (EditText) findViewById(R.id.serverId);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        final Button loadButton = (Button)findViewById(R.id.button);
        final Button prepareButton = (Button)findViewById(R.id.prepareButton);
        final TextView versionName = (TextView)findViewById(R.id.versionName);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        versionName.setText(version);

        prepareButton.setVisibility(View.VISIBLE);

        prepareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);

                String _accountName = accountName.getText().toString();
                String _kb = kb.getText().toString();
                String _server = server.getText().toString();

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

                AccountParams accountParams = new AccountParams(_accountName, _kb);

                if(!isEmpty(_server)) {
                    accountParams.setmHost(_server);
                }

                Nanorep.getInstance().init(getApplicationContext(), accountParams);
                pb.setVisibility(View.VISIBLE);



                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(Nanorep.getInstance().getNRConfiguration().getmParams() == null ||
                                (Nanorep.getInstance().getNRConfiguration().getmParams() != null && Nanorep.getInstance().getNRConfiguration().getmParams().size() == 0)) {
//                            NRImpl.getInstance().reset();
                            Toast.makeText(MainActivity.this, "Wrong account or kb", Toast.LENGTH_LONG).show();
                            prepareButton.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        } else {
                            loadButton.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                            versionName.setVisibility(View.GONE);
                        }
                    }
                }, 8000);

            }
        });


        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setVisibility(View.INVISIBLE);
                    mainFragment = NRMainFragment.newInstance();
                    mainFragment.setApplicationContentListener(MainActivity.this);


                    Nanorep.getInstance().getNRConfiguration().getTitle().setTitleColor("#212121");
                    Nanorep.getInstance().getNRConfiguration().getTitle().setTitleBGColor("#ffffff");
                    Nanorep.getInstance().getNRConfiguration().getTitle().setTitle("Search for an answer");
                    Nanorep.getInstance().getNRConfiguration().getSearchBar().setInitialText("Search a question");
                    Nanorep.getInstance().getNRConfiguration().getContent().setAnswerTitleColor("#212121");


                    FragmentUtils.openFragment(mainFragment, R.id.content_main,
                            NRMainFragment.TAG, MainActivity.this, false);
                }
            });
        }
    }

    private void openMainFragment() {
        mainFragment = NRMainFragment.newInstance();
        mainFragment.setApplicationContentListener(this);
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
        return null;
    }

    @Override
    public NRCustomSuggestionsView getSuggestionsView(Context context) {
        return  null;
    }

    @Override
    public NRCustomTitleView getTitle(Context context) {
        return null;
    }

    @Override
    public NRCustomContentView getContent(Context context) {
        return null;
    }

    @Override
    public NRCustomLikeView getLikeView(Context context) {
        return null;
    }

    @Override
    public NRCustomChannelView getChannelView(Context context) {
        return null;
    }

    @Override
    public NRCustomFeedbackView getFeedbackView(Context context) {
        return null;
    }

    @Override
    public boolean onLinkClicked(String url) {
//        MyFragment myFragment = MyFragment.newInstance();
//        FragmentUtils.addFragment(mainFragment,myFragment, R.id.content_main,MainActivity.this);
//        return true;
        return false;
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
