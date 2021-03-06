package com.nanorep.nanoclient;

import android.net.Uri;

import com.nanorep.nanoclient.Connection.NRUtilities;

import java.util.HashMap;

/**
 * Created by noat on 24/11/2016.
 */

public class AccountParams implements Comparable<AccountParams>{

    private String mHost;
    private String mAccount;
    private String mKnowledgeBase;
    private HashMap<String, String> mContext;
    private String mReferrer;
    private String mDomain;

    public AccountParams(String account, String kb) {
        mAccount = account;
        mKnowledgeBase = kb;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getmHost() {
        return mHost;
    }

    public void setmHost(String mHost) {
        this.mHost = mHost;
    }

    public String getKnowledgeBase() {
        if (mKnowledgeBase == null) {
            return "";
        }
        return mKnowledgeBase;
    }

    public void setKnowledgeBase(String knowledgeBase) {
        mKnowledgeBase = knowledgeBase;
    }

    public void setContext(HashMap<String, String> context) {
        mContext = context;
    }

    public String getNanorepContext() {
        if (mContext != null) {
            return NRUtilities.wrappedContextBase64(mContext);
        }
        return null;
    }

    public String getContext() {
        String context = null;

        if(mContext != null) {
            context = "";
            for (String key: mContext.keySet()) {
                context = context + key + ":" + mContext.get(key) + ",";
            }
            if (context.length() > 0 ) {
                context = context.substring(0, context.length()-1);
            }
        }

        return context;
    }

    public String getReferrer() {
        if (mReferrer == null) {
            mReferrer = "mobile";
        }
        return mReferrer;
    }

    public void setReferrer(String referrer) {
        mReferrer = referrer;
    }

    public Uri.Builder getUri(boolean setReferer) {
        Uri.Builder uri = getUriNoReferer();

        if(setReferer) {
            uri.appendQueryParameter("referer", NRUtilities.buildReferer(getReferrer()));
        }

        return uri;
    }

    private Uri.Builder getUriNoReferer() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");

        if (mHost != null) {
            uri.authority(mHost + ".nanorep.com");
            uri.appendEncodedPath("~" + getAccount());
        } else {
            uri.authority(getAccount() + ".nanorep.co");
        }

        return uri;
    }

    public Uri.Builder getUri() {
        Uri.Builder uri = getUriNoReferer();
        uri.appendQueryParameter("referer", NRUtilities.buildReferer(getReferrer()));
        return uri;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    @Override
    public int compareTo(AccountParams o) {
        if(this.getAccount().equals(o.getAccount()) &&
           this.getKnowledgeBase().equals(o.getKnowledgeBase()) && stringEqual(this.getmHost(), o.getmHost()) &&
                stringEqual(this.getContext(), o.getContext())) {

            return 0;

        }
        return -1;
    }

    private boolean stringEqual(String myString, String oString) {
        if(myString == null && oString == null) {
            return true;
        } else if(myString != null && myString.equals(oString)) {
            return true;
        }
        return false;
    }

}

