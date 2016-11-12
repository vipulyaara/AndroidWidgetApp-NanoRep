package com.nanorep.nanoclient.Interfaces;

import com.nanorep.nanoclient.Channeling.NRChanneling;

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
    boolean isCNF();
    ArrayList<NRChanneling> getChanneling();

    enum LikeState {
        notSelected,
        positive,
        negative
    }
}
