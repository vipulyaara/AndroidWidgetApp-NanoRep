package nanorep.nanowidget.Components;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.ArrayList;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;
import nanorep.nanowidget.interfaces.OnLinkedArticle;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NRLinkedArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NRLinkedArticleFragment extends Fragment implements NRWebView.Listener, OnFAQAnswerFetched {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnLinkedArticle mListener;
    private NRResultTitleView mTitleView;
    private NRWebView mWebView;
    private NRLinkedArticlesBrowserView mBrowserView;
    private ArrayList<NRQueryResult> mLinkedArticles = new ArrayList<>();
    private int mIndex;

    public NRLinkedArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NRLinkedArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NRLinkedArticleFragment newInstance(String param1, String param2) {
        NRLinkedArticleFragment fragment = new NRLinkedArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setQueryResult(NRQueryResult queryResult) {
        mIndex = 0;
        mLinkedArticles.add(queryResult);
    }

    public void setListener(OnLinkedArticle listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nrlinked_article, container, false);
        mTitleView = (NRResultTitleView) view.findViewById(R.id.linkedArtTitle);
        mTitleView.setTitle(mLinkedArticles.get(0).getTitle());
        mWebView = (NRWebView) view.findViewById(R.id.linkedArtWebView);
        mWebView.setListener(this);
        mWebView.loadData(mLinkedArticles.get(0).getBody(), "text/html", "UTF-8");
        mBrowserView = (NRLinkedArticlesBrowserView) view.findViewById(R.id.linkedArtBrowser);
        mBrowserView.setListener(new NRLinkedArticlesBrowserView.Listener() {
            @Override
            public void onNextClicked() {
                mIndex++;
                updateArticle(mLinkedArticles.get(mIndex));
            }

            @Override
            public void onPrevClicked() {
                mIndex--;
                updateArticle(mLinkedArticles.get(mIndex));
            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateArticle(NRQueryResult result) {
        mTitleView.setTitle(result.getTitle());
        mWebView.loadData(result.getBody(), "html/text", "UTF-8");
        if (mLinkedArticles.size() > 1) {
            if (mIndex < mLinkedArticles.size() - 1 && mIndex > 0) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasNextAndPrev);
            } else if (mIndex == 0) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasNext);
            } else if (mIndex == mLinkedArticles.size() - 1) {
                mBrowserView.setState(NRLinkedArticlesBrowserView.State.hasPrev);
            }
        }
    }

    @Override
    public void onAnswerFetched(NRQueryResult result) {
        Log.d("Test", result.getTitle());
        if (result != null) {
            mIndex++;
            mLinkedArticles.add(mIndex, result);
            updateArticle(result);
        }
    }

    @Override
    public void onLinkedArticleClicked(String articleId) {
        mListener.onLinkedArticleClicked(this, articleId);
    }
}
