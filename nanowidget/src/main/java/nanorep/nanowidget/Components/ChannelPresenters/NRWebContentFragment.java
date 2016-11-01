package nanorep.nanowidget.Components.ChannelPresenters;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.NRChannelDismissType;

/**
 * A simple {@link Fragment} subclass.
 */
public class NRWebContentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WebView mWebView;
    private Button mCloseButton;
    private Listener mListener;
    private ValueCallback<Uri[]> mValueCallback;
    private WebChromeClient.FileChooserParams mFileChooserParams;

    public interface Listener {
        void onDismiss();
    }



    // TODO: Rename and change types and number of parameters
    public static NRWebContentFragment newInstance(String param1, String param2) {
        NRWebContentFragment fragment = new NRWebContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public NRWebContentFragment() {
        // Required empty public constructor
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nrweb_content, container, false);

        mWebView = (WebView) view.findViewById(R.id.webContentView);
        mWebView.loadUrl(getArguments().getString(ARG_PARAM1));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mValueCallback = filePathCallback;
                mFileChooserParams = fileChooserParams;
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                }
                Intent intent = mFileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, 100);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
        mWebView.setWebViewClient(new NRPresentorWebClient());
        mCloseButton = (Button) view.findViewById(R.id.closeChannelButton);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            mValueCallback.onReceiveValue(new Uri[] {data.getData()});
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mWebView.stopLoading();
    }

    public class NRPresentorWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("nanorep")) {
                switch (NRChannelDismissType.fromUrl(url)) {
                    case Cancelled:
                        mListener.onDismiss();
                        break;

                }
                return true;
            }
            return false;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl().getAuthority().equals("nanorep")) {
                return true;
            }
            return false;
        }
    }
}
