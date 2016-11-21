package com.nanorep.nanoclient;

import android.content.Context;
import android.net.Uri;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Connection.NRUtilities;
import com.nanorep.nanoclient.Log.NRLogger;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.util.HashMap;


/**
 * Created by nissimpardo on 07/08/2016.
 */

public abstract class Nanorep {

    protected AccountParams mAccountParams;
    protected NRConfiguration mCnf;
    protected NRLogger nrLogger;
    protected Context mContext;

    public interface OnSearchResultsFetchedListener {
        void onSearchResponse(NRSearchResponse response, NRError error);
    }

    public interface OnSuggestionsFetchedListener {
        void onSuggestionsFetched(NRSuggestions suggestions, NRError error);
    }

    public interface OnLikeSentListener {
        void onLikeSent(boolean success);
    }

    public interface OnFAQAnswerFetchedListener {
        void onFAQAnswerFetched(NRFAQAnswer faqAnswer, NRError error);
    }

    public interface OnConfigurationFetchedListener {
        void onConfigurationFetched(NRError error);
    }

    public abstract void searchText(String text, OnSearchResultsFetchedListener onSearchResultsFetchedListener);

    public abstract void suggestionsForText(String text, OnSuggestionsFetchedListener onSuggestionsFetchedListener);

    public abstract void likeForSearchResult(NRSearchLikeParams likeParams, OnLikeSentListener onLikeSentListener);

    public abstract void fetchFAQAnswer(String answerId, Integer answerHash, OnFAQAnswerFetchedListener onFAQAnswerFetchedListener);

    public abstract void likeForFAQResult(NRFAQLikeParams likeParams, OnLikeSentListener onLikeSentListener);

    public abstract void fetchConfiguration(OnConfigurationFetchedListener onConfigurationFetchedListener, boolean forceInit);

    public NRConfiguration getNRConfiguration() {
        if (mCnf == null)
            mCnf = new NRConfiguration();

        return mCnf;
    }

    public AccountParams getAccountParams() {
        return mAccountParams;
    }

    public void setDebugMode(boolean checked) {
//        if (nrLogger == null) {
//            nrLogger = new NRLogger();
//        }

        nrLogger.setDebug(checked);
    }

    public void init(Context context, String account, String kb) {
        this.mContext = context;
        this.mAccountParams = new AccountParams();
        this.mAccountParams.setAccount(account);
        this.mAccountParams.setKnowledgeBase(kb);
//        this.mAccountParams.setmHost("server4");
        this.nrLogger = new NRLogger();
        this.mCnf = null;

        fetchConfiguration(null, true);
    }

    public class AccountParams {

        private String mHost;
        private String mAccount;
        private String mKnowledgeBase;
        private HashMap<String, String> mContext;
        private String mReferrer;
        private String mDomain;

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

        public String getReferrer() {
            if (mReferrer == null) {
                mReferrer = "mobile";
            }
            return mReferrer;
        }

        public void setReferrer(String referrer) {
            mReferrer = referrer;
        }

        public Uri.Builder getUri() {
            Uri.Builder uri = new Uri.Builder();
            uri.scheme("https");

            if (mHost != null) {
                uri.authority(mHost + ".nanorep.com");
                uri.appendEncodedPath("~" + getAccount());
            } else {
                uri.authority(getAccount() + ".nanorep.co");
            }
            uri.appendQueryParameter("referer", NRUtilities.buildReferer(getReferrer()));
            return uri;
        }

        public String getDomain() {
            return mDomain;
        }

        public void setDomain(String domain) {
            mDomain = domain;
        }
    }
}