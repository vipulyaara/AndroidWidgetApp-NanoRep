package com.nanorep.nanoclient.Interfaces;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Response.NRConfiguration;

/**
 * Created by nissopa on 9/14/15.
 */
public interface NRDefaultFAQCompletion {
    void fetchDefaultFAQ(NRConfiguration cnf, NRError error);
}
