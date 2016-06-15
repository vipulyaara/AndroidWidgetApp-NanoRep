package nanorep.nanowidget.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import NanoRep.Interfaces.NRQueryResult;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 15/06/16.
 */
public class NRResultItem extends RecyclerView.ViewHolder {
    private TextView mTitleTextView;

    private NRQueryResult mResult;

    public NRResultItem(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleView);
    }

    public void setResult(NRQueryResult result) {
        mResult = result;
        mTitleTextView.setText(result.getTitle());
    }
}
