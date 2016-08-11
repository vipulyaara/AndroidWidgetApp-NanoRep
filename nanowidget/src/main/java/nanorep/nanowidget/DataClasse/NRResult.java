package nanorep.nanowidget.DataClasse;


import nanorep.Interfaces.NRQueryResult;
import nanorep.nanowidget.Components.NRResultItem;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResult {
    private NRQueryResult mFetchedResult;
    private boolean mIsUnfolded = false;
    private NRResultItem.RowType mRowType = NRResultItem.RowType.standard;

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
    private NRResultType mType;

    public enum NRResultType {
        search,
        suggestion,
        faqAnswer,
    }

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

    public NRResultItem.RowType getRowType() {
        return mRowType;
    }

    public void setRowType(NRResultItem.RowType rowType) {
        mRowType = rowType;
    }
}
