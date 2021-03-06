package com.nanorep.nanoclient.RequestParams;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.HashMap;


/**
 * Created by nissopa on 9/13/15.
 */
public class NRRequestParams {
    private HashMap<String, String> mParams;
    protected NRQueryResult mResult;

    public NRRequestParams(NRQueryResult result) {
        mResult = result;
    }

    public HashMap<String, String> getParams() {
        if (mParams == null) {
            mParams = new  HashMap<String, String>();
        }
        return mParams;
    }

    public void setValue(String value, String forKey) {
        if (value != null && forKey != null) {
            getParams().put(forKey, value);
        }
    }
}
