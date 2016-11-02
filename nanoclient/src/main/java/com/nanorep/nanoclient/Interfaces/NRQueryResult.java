package com.nanorep.nanoclient.Interfaces;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nissimpardo on 07/06/16.
 */
public interface NRQueryResult {
    String getId();
    String getTitle();
    void setBody(String body);
    void setLikeState(LikeState likeState);
    LikeState getLikeState();
    String getLikes();
    String getBody();
    Integer getHash();
    HashMap<String, Object> getParams();
    String getKeywordSetId();
    boolean isCNF();
    void setIsCNF(boolean isCNF);
    ArrayList<NRChanneling> getChanneling();
    void setChanneling(ArrayList<NRChanneling> channeling);

    enum LikeState {
        notSelected,
        positive,
        negative
    }
}
