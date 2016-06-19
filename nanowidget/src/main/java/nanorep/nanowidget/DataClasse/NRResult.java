package nanorep.nanowidget.DataClasse;

import NanoRep.Interfaces.NRQueryResult;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResult {
    private NRQueryResult mFetchedResult;

    public boolean isUnfolded() {
        return mIsUnfolded;
    }

    public void setUnfolded(boolean unfolded) {
        mIsUnfolded = unfolded;
    }

    private boolean mIsUnfolded;

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
}
