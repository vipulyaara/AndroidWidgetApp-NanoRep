package com.nanorep.nanoclient;

import android.net.Uri;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Connection.NRUtilities;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRFAQAnswerItem;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.util.HashMap;


/**
 * Created by nissimpardo on 07/08/2016.
 */

public interface Nanorep {
    interface OnSearchResultsFetchedListener {
        void onSearchResponse(NRSearchResponse response, NRError error);
    }

    interface OnSuggestionsFetchedListener {
        void onSuggestionsFetched(NRSuggestions suggestions, NRError error);
    }

    interface OnLikeSentListener {
        void onLikeSent(String resultId, int type, boolean success);
    }

    interface OnFAQAnswerFetchedListener {
        void onFAQAnswerFetched(NRFAQAnswerItem faqAnswer, NRError error);
    }

    interface OnConfigurationFetchedListener {
        void onConfigurationFetched(NRError error);
    }

    AccountParams getAccountParams();

    void searchText(String text, OnSearchResultsFetchedListener onSearchResultsFetchedListener);

    void suggestionsForText(String text, OnSuggestionsFetchedListener onSuggestionsFetchedListener);

    void likeForSearchResult(NRSearchLikeParams likeParams, OnLikeSentListener onLikeSentListener);

    void fetchFAQAnswer(String answerId, Integer answerHash, OnFAQAnswerFetchedListener onFAQAnswerFetchedListener);

    void likeForFAQResult(NRFAQLikeParams likeParams, OnLikeSentListener onLikeSentListener);

    void fetchConfiguration(OnConfigurationFetchedListener onConfigurationFetchedListener);

    NRConfiguration getNRConfiguration();


    public class AccountParams {
        private String mAccount;
        private String mKnowledgeBase;
        private HashMap<String, String> mContext;
        private String mReferrer;

        public String getAccount() {
            return mAccount;
        }

        public void setAccount(String account) {
            mAccount = account;
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
            uri.authority(getAccount() + ".nanorep.co");
            uri.appendQueryParameter("referer", NRUtilities.buildReferer(getReferrer()));
            return uri;
        }
    }
}
