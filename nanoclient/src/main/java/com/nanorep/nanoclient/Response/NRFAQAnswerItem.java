package com.nanorep.nanoclient.Response;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nissopa on 9/13/15.
 */
public class NRFAQAnswerItem implements NRQueryResult {
    private HashMap<String, Object> mParams;
    private LikeState mLikeState = LikeState.notSelected;
    private String mBody;
    private ArrayList<NRChanneling> mChanneling;
    /**
     * Converts JSON string to NRFAQAnswerItem object
     *
     * @param params HashMap generated from json string
     */
    public NRFAQAnswerItem(HashMap<String, Object> params) {
        mParams = params;
    }

    /**
     *
     * @return
     */
    public int getCount() {
        return Integer.parseInt((String)mParams.get("count"));
    }

    /**
     *
     * @return
     */
    public int getData() {
        return Integer.parseInt((String)mParams.get("data"));
    }

    /**
     *
     * @return
     */
    public String getLabel() {
        return (String)mParams.get("label");
    }

    /**
     *
     * @return
     */
    public String getLikes() {
        return mParams.get("likes").toString();
    }

    /**
     *
     * @return
     */
    public String getObjectId() {
        return (String)mParams.get("objectId");
    }

    /**
     *
     * @return
     */
    public float getPercent() {
        return Float.parseFloat((String)mParams.get("percent"));
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getTitle() {
        return getLabel();
    }

    @Override
    public void setBody(String body) {
        mBody = body;
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
        return (String)mParams.get("body");
    }

    @Override
    public Integer getHash() {
        return (Integer) mParams.get("titleAndBodyHash");
    }

    @Override
    public boolean isCNF() {
        return true;
    }

    @Override
    public ArrayList<NRChanneling> getChanneling() {
//        ArrayList<HashMap<String, ?>> channels = (ArrayList)mParams.get("rechanneling");
//        if (channels != null && channels.size() > 0) {
//            ArrayList<NRChanneling> channeling = new ArrayList<NRChanneling>();
//            for (HashMap channel : channels) {
//                channeling.add(NRChanneling.channelForParams(channel));
//            }
//            return channeling;
//        } else {
//            return null;
//        }
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
    public String getKeywordSetId() {
        return null;
    }

    @Override
    public void setIsCNF(boolean isCNF) {

    }

    @Override
    public void setChanneling(ArrayList<NRChanneling> channeling) {

    }

    @Override
    public HashMap<String, Object> getParams() {
        return mParams;
    }
}
