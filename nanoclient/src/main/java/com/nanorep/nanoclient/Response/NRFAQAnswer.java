package com.nanorep.nanoclient.Response;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nissopa on 9/14/15.
 */
public class NRFAQAnswer implements NRQueryResult {
    HashMap<String, Object> mParams;
    private ArrayList<NRChanneling> mChanneling;

    private LikeState mLikeState = LikeState.notSelected;

    /**
     * Converts JSON string to NRFAQAnswer object
     *
     * @param params HashMap generated from json string
     */
    public NRFAQAnswer(HashMap<String, Object> params) {
        mParams = params;
    }

    @Override
    public String getId() {
        return (String) mParams.get("id");
    }

    /**
     * Fetches Title of answer
     *
     * @return Value of answer's title
     */
    public String getTitle() {
        return (String)mParams.get("title");
    }

    @Override
    public void setBody(String body) {

    }


    @Override
    public void setLikeState(LikeState likeState) {
        mLikeState = likeState;
    }

    @Override
    public LikeState getLikeState() {
        return mLikeState;
    }

    /**
     * Fetches short description of the answer in html string
     *
     * @return Value of body
     */
    public String getBody() {
        return (String)mParams.get("body");
    }

    @Override
    public boolean isCNF() {
        return false;
    }

    @Override
    public ArrayList<NRChanneling> getChanneling() {
        if (mChanneling == null) {
            ArrayList<HashMap<String, ?>> channels = (ArrayList)mParams.get("rechanneling");
            if (channels != null && channels.size() > 0) {
                mChanneling = new ArrayList<>();
                for (HashMap channel : channels) {
                    mChanneling.add(NRChanneling.channelForParams(channel));
                }
            } else {
                return null;
            }
        }
        return mChanneling;
    }

    /**
     *
     * @return Value of attachments
     */
    public String getAttachments() {
        return (String)mParams.get("attachments");
    }

    /**
     * Fetches amount of likes
     *
     * @return Value of like param
     */
    @Override
    public String getLikes() {
        return (String)mParams.get("likesCount");
    }

}
