package com.nanorep.nanoclient.Response;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Created by nissopa on 9/12/15.
 */
public class NRAnswer implements NRQueryResult {
    private HashMap<String, Object> mParams;
    private String mArticleId;
    private String mKeywordsetId;
    private int mLikes;
    private String mTitle;
    private String mSummary;
    private LikeState mLikeState = LikeState.notSelected;
    private ArrayList<NRChanneling> mChanneling;

    /**
     * Converts JSON string to NRAnswer object
     *
     * @param params HashMap generated from json string
     */
    public NRAnswer(HashMap<String, Object> params) {
        if (params != null) {
            mParams = params;
            mArticleId = (String) params.get("id");
            mKeywordsetId = (String) params.get("keywordsetId");
            mLikes = (int) params.get("likes");
            mTitle = (String) params.get("title");
            mSummary = (String) params.get("summary");
        }
    }

    /**
     * Fetches article Id
     *
     * @return Value of article id
     */
    public String getArticleId() {
        return mArticleId;
    }

    /**
     * Fetches Keyword Set Id
     *
     * @return Value of keyword set id
     */
    public String getKeywordsetId() {
        return mKeywordsetId;
    }

    /**
     * Fetches amount of likes
     *
     * @return Value of like param
     */
    public String getLikes() {
        return Integer.toString(mLikes);
    }

    @Override
    public String getId() {
        return getArticleId();
    }

    /**
     * Fetches Title of answer
     *
     * @return Value of answer's title
     */
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setBody(String body) {
        mSummary = body;
    }

    @Override
    public void setLikeState(LikeState likeState) {
        mLikeState = likeState;
    }

    @Override
    public LikeState getLikeState() {
        return mLikeState;
    }

    @Override
    public String getBody() {
        return getSummary();
    }

    @Override
    public Integer getHash() {
        return (Integer) mParams.get("titleAndBodyHash");
    }

    @Override
    public HashMap<String, Object> getParams() {
        return mParams;
    }

    @Override
    public boolean isCNF() {
        return false;
    }

    /**
     * Fetches short description of the answer
     *
     * @return Value of summary
     */
    public String getSummary() {
        return mSummary;
    }


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

    @Override
    public void setChanneling(ArrayList<NRChanneling> channeling) {
        mChanneling = channeling;
    }

}
