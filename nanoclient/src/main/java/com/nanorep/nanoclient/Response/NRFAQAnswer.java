package com.nanorep.nanoclient.Response;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nissopa on 9/14/15.
 */
public class NRFAQAnswer implements NRQueryResult {
    HashMap<String, Object> mParams;

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
        return null;
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
        return null;
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
    public int getLikes() {
        return Integer.parseInt((String)mParams.get("likesCount"));
    }

}
