package nanorep.nanowidget.DataClasse;

import NanoRep.Interfaces.NRQueryResult;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResult {
    private NRQueryResult mFetchedResult;
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
