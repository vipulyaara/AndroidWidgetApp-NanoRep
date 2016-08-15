package com.nanorep.nanoclient.Interfaces;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Response.NRFAQDataObject;

/**
 * Created by nissopa on 9/14/15.
 */
public interface NRFAQCompletion {
    void fetchFAQ(NRFAQDataObject faq, NRError error);
}
