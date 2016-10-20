package com.nanorep.nanoclient.RequestParams;


import com.nanorep.nanoclient.Interfaces.NRQueryResult;

/**
 * Created by nissopa on 9/13/15.
 */
public class NRFAQLikeParams extends NRRequestParams {
    private NRLikeType mLikeType;
    private String mKeyboardName;
    private String mAnswerId;

    public NRFAQLikeParams(NRQueryResult result) {
        super(result);
        setValue(result.getId(), "id");
        setValue(result.getTitle(), "feedbackText");
    }


    /**
     *  Set the feedback type
     *
     * @param likeType feedback type
     */
    public void setLikeType(NRLikeType likeType) {
        mLikeType = likeType;
        setValue(likeType.toString(), "action");
    }

    /**
     * Set the keyboard name
     *
     * @param keyboardName
     */
    public void setKeyboardName(String keyboardName) {
        mKeyboardName = keyboardName;
        setValue(keyboardName, "kb");
    }

    /**
     * Set the answer Id for the like call
     *
     * @param answerId The liked answer id (you can get it from the faqData array)
     */
    public void setAnswerId(String answerId) {
        mAnswerId = answerId;
        setValue(answerId, "id");
    }


    public String getAnswerId() {
        return mAnswerId;
    }
}
