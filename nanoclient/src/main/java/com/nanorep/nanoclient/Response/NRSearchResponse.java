package com.nanorep.nanoclient.Response;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nissopa on 9/12/15.
 */
public class NRSearchResponse {
    private HashMap<String, Object> mParams;
    private String mSearchId;
    private String mLangCode;
    private String mDetectedLanguage;
    private ArrayList<NRQueryResult> mAnswerList;

    /**
     * Converts JSON string into NRSearchResponse
     *
     * @param params HashMap contains all of the NRSearchResponse params
     */
    public NRSearchResponse(HashMap<String, Object> params) {
        mParams = params;
        mSearchId = (String)params.get("requestId");
        mLangCode = (String)params.get("kbLanguageCode");
        mDetectedLanguage = (String)params.get("detectedLanguage");
    }

    /**
     *
     * @return value of search id
     */
    public String getSearchId() {
        return mSearchId;
    }

    /**
     *
     * @return value of langCode
     */
    public String getLangCode() {
        return mLangCode;
    }

    /**
     *
     * @return value of detected language
     */
    public String getDetectedLanguage() {
        return mDetectedLanguage;
    }

    /**
     *
     * @return value of answer list
     */
    public ArrayList<NRQueryResult> getAnswerList() {
        if (mAnswerList == null) {
            ArrayList<HashMap<String, Object>> answers = (ArrayList)mParams.get("answers");
            if (answers != null && answers.size() > 0) {
                mAnswerList = new ArrayList<>();
                for (HashMap<String, Object> answer: answers) {
                    mAnswerList.add(new NRAnswer(answer));
                }
            } else {
                return null;
            }
        }
        return mAnswerList;
    }
}
