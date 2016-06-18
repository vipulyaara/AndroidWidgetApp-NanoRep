package nanorep.nanowidget.Components;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanorep.nanorepsdk.Connection.NRConnection;

import org.w3c.dom.Text;

import NanoRep.Interfaces.NRQueryResult;
import nanorep.nanowidget.DataClasse.NRResult;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRResultItemListener;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRResultItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    private NRResultItemListener mListener;
    private View mItemView;
    private Button mTitleButton;
    private WebView mWebView;
    private RelativeLayout mFooterView;

    private NRResult mResult;

    public NRResultItem(View view, int maxHeight) {
        super(view);
        mItemView = itemView;
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mItemView.getLayoutParams();
        params.height = maxHeight;
        mItemView.setLayoutParams(params);
        mTitleButton = (Button) itemView.findViewById(R.id.titleButton);
        mWebView = (WebView) itemView.findViewById(R.id.webView);
        mFooterView = (RelativeLayout) itemView.findViewById(R.id.footerView);
        mTitleButton.setOnClickListener(this);
    }

    public void setListener(NRResultItemListener listener) {
        mListener = listener;
    }

    public void setResult(NRResult result) {
        mResult = result;
        mTitleButton.setText(result.getFetchedResult().getTitle());
        setHeight(result.getHeight());
    }

    public NRResult getResult() {
        return mResult;
    }

    public void setBody(String htmlString) {
        if (mResult.getFetchedResult().getBody() == null) {
            mResult.getFetchedResult().setBody(htmlString);
        }
        mWebView.loadData(htmlString, "text/html", "UTF-8");
    }

    private void setHeight(int height) {
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) mItemView.getLayoutParams();
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, mItemView.getResources().getDisplayMetrics());
        mItemView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.titleButton) {
            if (mResult.getFetchedResult().getBody() == null) {
                mListener.shouldFetchFAQAnswerBody(this, mResult.getFetchedResult().getId());
            } else {
                setBody(mResult.getFetchedResult().getBody());
            }
            mListener.unfoldItem(this);
        }
    }
}
