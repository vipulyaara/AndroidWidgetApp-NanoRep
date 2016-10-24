package nanorep.nanowidget.DataClasse;


import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import nanorep.nanowidget.Components.NRResultItem;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResult {
    private NRQueryResult mFetchedResult;
    private boolean mIsUnfolded = false;
    private NRResultItem.RowType mRowType;

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

    public NRResult(NRQueryResult result, NRResultItem.RowType rowType) {
        mFetchedResult = result;
        mRowType = rowType;
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

    public NRResultItem.RowType getRowType() {
        return mRowType;
    }

    public void setRowType(NRResultItem.RowType rowType) {
        mRowType = rowType;
    }
}
