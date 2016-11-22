package com.nanorep.nanoclient;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;


import com.nanorep.nanoclient.Connection.NRCacheManager;
import com.nanorep.nanoclient.Connection.NRConnection;
import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Connection.NRUtilities;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Log.NRLogger;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRFAQAnswerItem;
import com.nanorep.nanoclient.Response.NRFAQGroupItem;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by nissimpardo on 07/08/2016.
 */

public class Nanorep {

    private AccountParams mAccountParams;
    private NRConfiguration mCnf;
    private NRLogger nrLogger;
    private Context mContext;

    private String mSessionId;
    private long mDelay;
    private HashMap<String, NRSearchResponse> mCachedSearches;
    private HashMap<String, NRSuggestions> mCachedSuggestions;
    private HashMap<String, ArrayList<OnFAQAnswerFetchedListener>> faqRequestListenersMap;
    private Handler mHandler;

    private static Nanorep INSTANCE;

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

    public void reset() {
//        mCnf = null;
        mSessionId = null;
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public static Nanorep getInstance() {
        if(INSTANCE == null)
        {
            synchronized (Nanorep.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Nanorep();
                }
            }
        }
        return INSTANCE;
    }

    private HashMap<String, NRSearchResponse> getCachedSearches() {
        if (mCachedSearches == null) {
            mCachedSearches = new HashMap<>();
        }
        return mCachedSearches;
    }

    private HashMap<String, NRSuggestions> getCachedSuggestions() {
        if (mCachedSuggestions == null) {
            mCachedSuggestions = new HashMap<>();
        }
        return mCachedSuggestions;
    }

    private HashMap<String, ArrayList<OnFAQAnswerFetchedListener>> getFaqRequestListenersMap() {
        if (faqRequestListenersMap == null) {
            faqRequestListenersMap = new HashMap<>();
        }
        return faqRequestListenersMap;
    }

    private void fetchFaqList(NRConnection.Listener listener) {
        final Uri.Builder uri = mAccountParams.getUri();
        uri.appendEncodedPath("api/faq/v1/list.json");
        uri.appendQueryParameter("account", mAccountParams.getAccount());
        if (mAccountParams.getKnowledgeBase() != null) {
            uri.appendQueryParameter("kb", mAccountParams.getKnowledgeBase());
        }
        if (mAccountParams.getNanorepContext() != null) {
            uri.appendQueryParameter("context", mAccountParams.getKnowledgeBase());
        }
        NRConnection.getInstance().connectionWithRequest(uri.build(), listener);
    }

    private void startKeepAlive() {
        Uri.Builder uri = mAccountParams.getUri();
        uri.appendEncodedPath("api/widget/v1/keepAlive.js");
        executeRequest(uri, new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    keepAlive(10000);
                    Log.d("Keep Alive Error", error.getDescription());
                } else  if (status != 200) {
                    mSessionId = null;
                    mHandler.removeCallbacksAndMessages(null);
                } else if (responseParam != null) {
                    keepAlive(mDelay);
                }
            }

            @Override
            public void log(String tag, String msg) {
                nrLogger.log(tag, msg);
            }
        });
    }

    private void keepAlive(long interval) {
        mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            public void run() {
                Nanorep.this.startKeepAlive();
            }
        }, interval);
    }

    private void executeRequest(final Uri.Builder uriBuilder, final NRConnection.Listener listener) {
        if (mSessionId != null) {
            uriBuilder.appendQueryParameter("sid", mSessionId);
            NRConnection.getInstance().connectionWithRequest(uriBuilder.build(), listener);
        } else {
            hello(new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    executeRequest(uriBuilder, listener);
                }

                @Override
                public void log(String tag, String msg) {
                    nrLogger.log(tag, msg);
                }
            });
        }
    }

    private void hello(final NRConnection.Listener listener) {
        final Uri.Builder _uriBuilder = mAccountParams.getUri();
        _uriBuilder.appendQueryParameter("kb", mAccountParams.getKnowledgeBase());
        _uriBuilder.appendEncodedPath("api/widget/v1/hello.js");
        _uriBuilder.appendQueryParameter("nostats", "false");
        _uriBuilder.appendQueryParameter("url", "mobile");
        NRConnection.getInstance().connectionWithRequest(_uriBuilder.build(), new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    Log.e("Nanorep", error.getDescription());
                } else {
                    mSessionId = (String) ((HashMap) responseParam).get("sessionId");
                    mDelay = ((Integer) ((HashMap) responseParam).get("timeout")).longValue() * 500;
                    Handler timer = new Handler();
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Nanorep.this.startKeepAlive();
                        }
                    }, mDelay);

                    listener.response(responseParam, status, error);
                }
            }

            @Override
            public void log(String tag, String msg) {
                nrLogger.log(tag, msg);
            }
        });
    }

//
//    @Override
//    public AccountParams getAccountParams() {
//        return mAccountParams;
//    }

    public void searchText(final String text, final OnSearchResultsFetchedListener onSearchResultsFetchedListener) {
        if (mCachedSearches != null && mCachedSearches.get(text) != null) {
            onSearchResultsFetchedListener.onSearchResponse(mCachedSearches.get(text), null);
        } else if (text != null && text.length() > 0){
            String encodedText = null;
            try {
                encodedText = URLEncoder.encode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Uri.Builder uriBuilder = mAccountParams.getUri();
            uriBuilder.appendEncodedPath("api/widget/v1/q.js");
            uriBuilder.appendQueryParameter("text", text);
            executeRequest(uriBuilder, new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    if (error != null) {
                        onSearchResultsFetchedListener.onSearchResponse(null, error);
                    } else if (responseParam != null) {
                        NRSearchResponse response = new NRSearchResponse((HashMap)responseParam);
                        Nanorep.this.getCachedSearches().put(text, response);
                        onSearchResultsFetchedListener.onSearchResponse(response, null);
                    }
                }

                @Override
                public void log(String tag, String msg) {
                    nrLogger.log(tag, msg);
                }
            });
        }
    }

    public void suggestionsForText(final String text, final OnSuggestionsFetchedListener onSuggestionsFetchedListener) {
        if (mCachedSuggestions != null && mCachedSuggestions.get(text) != null) {
            onSuggestionsFetchedListener.onSuggestionsFetched(mCachedSuggestions.get(text), null);
        } else if (text != null && text.length() > 0) {
            String encodedText = null;
            try {
                encodedText = URLEncoder.encode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Uri.Builder uriBuilder = mAccountParams.getUri();
            uriBuilder.appendEncodedPath("api/kb/v1/autoComplete");
            uriBuilder.appendQueryParameter("text", encodedText);
            uriBuilder.appendQueryParameter("stemming", "true");
            executeRequest(uriBuilder, new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    if (responseParam != null) {
                        ArrayList<String> answers = (ArrayList) ((HashMap)responseParam).get("a");
                        if (answers != null) {
                            ArrayList<Spannable> suggestions = getSpannables(answers);

                            ((HashMap)responseParam).put("a", suggestions);
                            getCachedSuggestions().put(text, new NRSuggestions((HashMap)responseParam));
                            onSuggestionsFetchedListener.onSuggestionsFetched(Nanorep.this.mCachedSuggestions.get(text), null);
                        } else {
                            onSuggestionsFetchedListener.onSuggestionsFetched(null, NRError.error("nanorep", 1002, "No suggestions"));
                        }

                    } else if (error != null) {
                        NRSuggestions storedSuggestions = getCachedSuggestions().get(text);
                        if (storedSuggestions == null) {
                            onSuggestionsFetchedListener.onSuggestionsFetched(null, NRError.error("nanorep", 1003, "No suggestions in cache"));
                        } else {
                            onSuggestionsFetchedListener.onSuggestionsFetched(storedSuggestions, null);
                        }
                    }
                }

                @Override
                public void log(String tag, String msg) {
                    nrLogger.log(tag, msg);
                }
            });
        }
    }

    private ArrayList<Spannable> getSpannables(ArrayList<String> answers) {
        ArrayList<Spannable> suggestions = new ArrayList<>();
        for (String answer : answers) {
            final SpannableStringBuilder str = new SpannableStringBuilder();
            String[] pipes = answer.split("\\|");
            for (int i = 0; i < pipes.length ; i++) {
                String[] words = pipes[i].split(",");
                String word = words[0];

                StyleSpan styleSpan;

                if(pipes[i].endsWith("*")) {
                    styleSpan = new StyleSpan(Typeface.BOLD);
                    word = word.replace("*","");

                } else {
                    styleSpan = new StyleSpan(Typeface.NORMAL);
                }

                String wordSpace = word + " ";

                if(i == pipes.length - 1) { // last
                    wordSpace = word;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    str.append(wordSpace, styleSpan , 0);
                } else {
                    int startIndex = str.length();

                    str.append(wordSpace);

                    str.setSpan(styleSpan, startIndex, str.length(), 0);
                }
            }
            suggestions.add(str);
        }

        return suggestions;
    }

    public void likeForSearchResult(final NRSearchLikeParams likeParams, final OnLikeSentListener onLikeSentListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendEncodedPath("api/widget/v1/thumb.js");
        for (String key: likeParams.getParams().keySet()) {
            uriBuilder.appendQueryParameter(key, likeParams.getParams().get(key));
        }
        executeRequest(uriBuilder, new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    onLikeSentListener.onLikeSent(false);
                } else if (responseParam != null) {
                    onLikeSentListener.onLikeSent(true);
                }
            }

            @Override
            public void log(String tag, String msg) {
                nrLogger.log(tag, msg);
            }
        });
    }

    public void fetchFAQAnswer(final String answerId, final Integer answerHash, final OnFAQAnswerFetchedListener onFAQAnswerFetchedListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendEncodedPath("api/faq/v1/answer.js");
        uriBuilder.appendQueryParameter("id", answerId);
        uriBuilder.appendQueryParameter("referer", getAccountParams().getReferrer());
        uriBuilder.appendQueryParameter("kb", getAccountParams().getKnowledgeBase());

        // if exist and updated in cache, fetch from cache,
        // else call to server
        HashMap<String, Object> answerParams = NRCacheManager.fetchFAQAnswer(answerId, answerHash);

        if(answerParams != null) {
            ((HashMap<String, Object>) answerParams).put("id", answerId);
            onFAQAnswerFetchedListener.onFAQAnswerFetched(new NRFAQAnswer((HashMap<String, Object>) answerParams), null);
        }
        else {

            // check if we already called fetch answer for this answer id
            ArrayList<OnFAQAnswerFetchedListener> onFAQAnswerFetchedListenerArr = Nanorep.this.getFaqRequestListenersMap().get(answerId);

            if(onFAQAnswerFetchedListenerArr == null) {// array is empty, no requests for this answer id

                onFAQAnswerFetchedListenerArr = new ArrayList<OnFAQAnswerFetchedListener>();

                onFAQAnswerFetchedListenerArr.add(onFAQAnswerFetchedListener);
                Nanorep.this.getFaqRequestListenersMap().put(answerId, onFAQAnswerFetchedListenerArr);

                final ArrayList<OnFAQAnswerFetchedListener> finalOnFAQAnswerFetchedListenerArr = onFAQAnswerFetchedListenerArr;


                executeRequest(uriBuilder, new NRConnection.Listener() {
                    @Override
                    public void response(Object responseParam, int status, NRError error) {
                        if (responseParam != null) {
                            ((HashMap<String, Object>) responseParam).put("id", answerId);
                        }

                        for (OnFAQAnswerFetchedListener listener : finalOnFAQAnswerFetchedListenerArr) {
                            if (error != null) {
                                listener.onFAQAnswerFetched(null, error);
                            } else if (responseParam != null) {

                                listener.onFAQAnswerFetched(new NRFAQAnswer((HashMap<String, Object>) responseParam), null);
                            }
                        }

                        // store to cahce the answer from server
                        NRCacheManager.storeFAQAnswer((HashMap<String, Object>) responseParam);

                        Nanorep.this.getFaqRequestListenersMap().remove(answerId);
                    }

                    @Override
                    public void log(String tag, String msg) {
                        nrLogger.log(tag, msg);
                    }
                });
            } else {
                onFAQAnswerFetchedListenerArr.add(onFAQAnswerFetchedListener);
                Nanorep.this.getFaqRequestListenersMap().put(answerId, onFAQAnswerFetchedListenerArr);
            }
        }
    }

    public void likeForFAQResult(final NRFAQLikeParams likeParams, final OnLikeSentListener onLikeSentListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendEncodedPath("api/analytics/v1/addFeedback");
        uriBuilder.appendQueryParameter("ignoreValidateCookie", "true");
        for (String key: likeParams.getParams().keySet()) {
            uriBuilder.appendQueryParameter(key, likeParams.getParams().get(key));
        }
        if (mSessionId != null) {
            uriBuilder.appendQueryParameter("sid", mSessionId);
        }
        NRConnection.getInstance().connectionWithRequest(uriBuilder.build(), new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    onLikeSentListener.onLikeSent(false);
                } else if (responseParam instanceof HashMap){
                    onLikeSentListener.onLikeSent(true);
                }
            }

            @Override
            public void log(String tag, String msg) {
                nrLogger.log(tag, msg);
            }
        });
    }

    public void fetchConfiguration(final OnConfigurationFetchedListener onConfigurationFetchedListener, boolean forceInit) {
        final HashMap<String, Object> cachedResponse = NRCacheManager.getAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getAccount() + mAccountParams.getNanorepContext()));

        if(!forceInit && cachedResponse != null) {
            if (onConfigurationFetchedListener != null) {
                NRConfiguration cnf = new NRConfiguration(cachedResponse);
                overrideCnfData(cnf);
                onConfigurationFetchedListener.onConfigurationFetched(null);
            }

            return;
        }

        if (mAccountParams != null) {
            final Uri.Builder uri = mAccountParams.getUri();
            uri.appendEncodedPath("widget/scripts/cnf.json");
            if (mAccountParams.getKnowledgeBase() != null) {
                uri.appendQueryParameter("kb", mAccountParams.getKnowledgeBase());
            }
            uri.appendQueryParameter("isFloat", "true");

            // check network connectivity speed
            final Long beforeCnfTs = System.currentTimeMillis()/1000;

            NRConnection.getInstance().connectionWithRequest(uri.build(), new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    Long afterCnfTs = System.currentTimeMillis()/1000;

                    final boolean fast = (afterCnfTs - beforeCnfTs) <= 4;

                    if (error != null) {
//                        HashMap<String, Object> cachedResponse = NRCacheManager.getAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getNanorepContext()));
                        if (onConfigurationFetchedListener != null) {
                            if (cachedResponse != null) {
                                NRConfiguration cnf = new NRConfiguration(cachedResponse);
                                overrideCnfData(cnf);
                                onConfigurationFetchedListener.onConfigurationFetched(null);
                            } else {
                                onConfigurationFetchedListener.onConfigurationFetched(error);
                            }
                        }

                    } else {
                        final NRConfiguration cnf = new NRConfiguration((HashMap) responseParam);
                        if (cnf.getIsContextDependent()) {
                            fetchFaqList(new NRConnection.Listener() {
                                @Override
                                public void response(Object responseParam, int status, NRError error) {
                                    if (responseParam != null) {
                                        cnf.setFaqData((ArrayList) responseParam);
                                        overrideCnfData(cnf);
                                        NRCacheManager.storeAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getAccount() + mAccountParams.getNanorepContext()), cnf.getmParams());
                                        if(!fast) {
                                            updateFAQContentsAndCallHello(cnf);
                                        }
                                    }
                                    if (onConfigurationFetchedListener != null) {
                                        if (error != null) {
                                            onConfigurationFetchedListener.onConfigurationFetched(error);
                                        } else if (responseParam != null) {
                                            onConfigurationFetchedListener.onConfigurationFetched(null);

                                        } else {
                                            onConfigurationFetchedListener.onConfigurationFetched(NRError.error("com.nanorepfaq", 1002, "faqData empty"));
                                        }
                                    }
                                }

                                @Override
                                public void log(String tag, String msg) {
                                    nrLogger.log(tag, msg);
                                }
                            });
                        } else {

                            overrideCnfData(cnf);

                            if (onConfigurationFetchedListener != null) {
                                onConfigurationFetchedListener.onConfigurationFetched(null);
                            }
                            NRCacheManager.storeAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getAccount() + mAccountParams.getNanorepContext()), cnf.getmParams());

                            if(!fast) {
                                updateFAQContentsAndCallHello(cnf);
                            }
                        }
                    }
                }

                @Override
                public void log(String tag, String msg) {
                    nrLogger.log(tag, msg);
                }
            });
        }
    }

    private void overrideCnfData(NRConfiguration nrConfiguration) {
        if(mCnf != null) {
            nrConfiguration.overrideCnfData(mCnf);
        }
        mCnf = nrConfiguration;
    }

    public NRConfiguration getNRConfiguration() {
        if(mCnf == null)
            mCnf = new NRConfiguration();

        return mCnf;
    }

//    @Override
//    public void setDebugMode(boolean checked) {
//        if (nrLogger == null) {
//            nrLogger = new NRLogger();
//        }
//
//        nrLogger.setDebug(checked);
//    }

    public AccountParams getAccountParams() {
        return mAccountParams;
    }

    private void updateFAQContentsAndCallHello(NRConfiguration cnf)
    {
        hello(new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {

            }

            @Override
            public void log(String tag, String msg) {
                nrLogger.log(tag, msg);
            }
        });

        // get contents for all Answers
//        for (NRQueryResult queryResult : cnf.getFaqData().getGroups().get(0).getAnswers()) {
        if(cnf.getFaqData().getGroups() == null) {
            return;
        }
        for (NRFAQGroupItem groupItem : cnf.getFaqData().getGroups()) {
            for (NRQueryResult queryResult : groupItem.getAnswers()) {
                fetchFAQAnswer(queryResult.getId(), queryResult.getHash(), new OnFAQAnswerFetchedListener() {
                    @Override
                    public void onFAQAnswerFetched(NRFAQAnswer faqAnswer, NRError error) {
                        // update cache with this Answer (has body now..)
                        NRCacheManager.storeFAQAnswer(faqAnswer.getParams());
                    }
                });
            }
        }
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
