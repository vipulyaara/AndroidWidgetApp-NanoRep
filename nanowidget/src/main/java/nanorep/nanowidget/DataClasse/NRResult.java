package nanorep.nanowidget.DataClasse;


import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import nanorep.nanowidget.interfaces.NRViewHolder;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResult {
    private NRQueryResult mFetchedResult;
    private boolean mIsUnfolded = false;
    private NRViewHolder.RowType mRowType = NRViewHolder.RowType.standard;

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public boolean isSingle() {
        return mIsSingle;
    }

    public void setSingle(boolean single) {
        mIsSingle = single;
    }

    private int mHeight;
    private boolean mIsSingle;

    public NRResult(NRQueryResult result) {
        mFetchedResult = result;
    }


    public NRQueryResult getFetchedResult() {
        return mFetchedResult;
    }

    public boolean isUnfolded() {
        return mIsUnfolded;
    }

    public void setUnfolded(boolean unfolded) {
        mIsUnfolded = unfolded;
    }

    public NRViewHolder.RowType getRowType() {
        return mRowType;
    }

    public void setRowType(NRViewHolder.RowType rowType) {
        mRowType = rowType;
    }
}
