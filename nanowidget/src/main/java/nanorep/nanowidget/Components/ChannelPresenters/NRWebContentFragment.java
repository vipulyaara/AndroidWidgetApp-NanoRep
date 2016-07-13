package nanorep.nanowidget.Components.ChannelPresenters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.HesitateInterpolator;

/**
 * A simple {@link Fragment} subclass.
 */
public class NRWebContentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WebView mWebView;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nrweb_content, container, false);

        mWebView = (WebView) view.findViewById(R.id.webContentView);
        mWebView.loadUrl(getArguments().getString(ARG_PARAM1));
        mWebView.getSettings().setJavaScriptEnabled(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mWebView.stopLoading();
                        getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).remove(NRWebContentFragment.this).commit();
                        return true;
                    }
                }

                return false;
            }
        });
    }
}
