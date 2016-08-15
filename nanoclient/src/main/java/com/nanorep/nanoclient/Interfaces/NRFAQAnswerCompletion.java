package com.nanorep.nanoclient.Interfaces;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Response.NRFAQAnswer;

/**
 * Created by nissopa on 9/14/15.
 */
public interface NRFAQAnswerCompletion {
    void fetchAnswer(NRFAQAnswer answer, NRError error);
}
