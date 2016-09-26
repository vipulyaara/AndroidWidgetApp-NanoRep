package com.nanorep.nanoclient;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;


import com.nanorep.nanoclient.Connection.NRCacheManager;
import com.nanorep.nanoclient.Connection.NRConnection;
import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Connection.NRUtilities;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nissimpardo on 07/08/2016.
 */

public class NRImpl implements Nanorep {
    private AccountParams mAccountParams;
    private Context mContext;
    private String mSessionId;
    private long mDelay;
    private HashMap<String, NRSearchResponse> mCachedSearches;
    private HashMap<String, NRSuggestions> mCachedSuggestions;
    private HashMap<String, ArrayList<OnFAQAnswerFetchedListener>> faqRequestListenersMap;
    private Handler mHandler;

    public NRImpl(Context context, AccountParams accountParams) {
        mContext = context;
        mAccountParams = accountParams;
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
        uri.appendPath("api/faq/v1/list.json");
        uri.appendQueryParameter("account", mAccountParams.getAccount());
        if (mAccountParams.getNanorepContext() != null) {
            uri.appendQueryParameter("context", mAccountParams.getKnowledgeBase());
        }
        NRConnection.getInstance().connectionWithRequest(uri.build(), listener);
    }

    private void startKeepAlive() {
        Uri.Builder uri = mAccountParams.getUri();
        uri.appendPath("api/widget/v1/keepAlive.js");
        executeRequest(uri, new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    keepAlive(10000);
                    Log.d("Keep Alive Error", error.getDescription());
                } else  if (status != 200) {
                    mSessionId = null;
                    mHandler.removeMessages(0);
                } else if (responseParam != null) {
                    keepAlive(mDelay);
                }
            }
        });
    }

    private void keepAlive(long interval) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                NRImpl.this.startKeepAlive();
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
            });
        }
    }

    private void hello(final NRConnection.Listener listener) {
        final Uri.Builder _uriBuilder = mAccountParams.getUri();
        _uriBuilder.appendPath("api/widget/v1/hello.js");
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
                            NRImpl.this.startKeepAlive();
                        }
                    }, mDelay);

                    listener.response(responseParam, status, error);
                }
            }
        });
    }


    @Override
    public AccountParams getAccountParams() {
        return mAccountParams;
    }

    @Override
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
            uriBuilder.appendPath("api/widget/v1/q.js");
            uriBuilder.appendQueryParameter("text", encodedText);
            executeRequest(uriBuilder, new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    if (error != null) {
                        onSearchResultsFetchedListener.onSearchResponse(null, error);
                    } else if (responseParam != null) {
                        NRSearchResponse response = new NRSearchResponse((HashMap)responseParam);
                        NRImpl.this.getCachedSearches().put(text, response);
                        onSearchResultsFetchedListener.onSearchResponse(response, null);
                    }
                }
            });
        }
    }

    @Override
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
            uriBuilder.appendPath("api/widget/v1/suggest.js");
            uriBuilder.appendQueryParameter("text", encodedText);
            uriBuilder.appendQueryParameter("stemming", "false");
            executeRequest(uriBuilder, new NRConnection.Listener() {
                @Override
                public void response(Object responseParam, int status, NRError error) {
                    if (responseParam != null) {
                        ArrayList<String> answers = (ArrayList) ((HashMap)responseParam).get("a");
                        if (answers != null) {
                            ArrayList<String> arr = new ArrayList<String>();
                            for (String answer : answers) {
                                String[] pipes = answer.split("\\|");
                                String parsedAnswer = "";
                                for (String comma : pipes) {
                                    String[] firstWords = comma.split(",");
                                    parsedAnswer += firstWords[0] + " ";
                                }
                                parsedAnswer = parsedAnswer.substring(0, parsedAnswer.length() - 1);
                                arr.add(parsedAnswer);
                            }
                            ((HashMap)responseParam).put("a", arr);
                            NRImpl.this.getCachedSuggestions().put(text, new NRSuggestions((HashMap)responseParam));
                            onSuggestionsFetchedListener.onSuggestionsFetched(NRImpl.this.mCachedSuggestions.get(text), null);
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
            });
        }
    }

    @Override
    public void likeForSearchResult(final NRSearchLikeParams likeParams, final OnLikeSentListener onLikeSentListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendPath("api/widget/v1/thumb.js");
        for (String key: likeParams.getParams().keySet()) {
            uriBuilder.appendQueryParameter(key, likeParams.getParams().get(key));
        }
        executeRequest(uriBuilder, new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {
                if (error != null) {
                    onLikeSentListener.onLikeSent(likeParams.getArticleId(), 0, false);
                } else if (responseParam != null) {
                    onLikeSentListener.onLikeSent(likeParams.getArticleId(), (Integer) ((HashMap) responseParam).get("type"), ((HashMap) responseParam).get("result").equals("True"));
                }
            }
        });
    }

    @Override
    public void fetchFAQAnswer(final String answerId, final Integer answerHash, final OnFAQAnswerFetchedListener onFAQAnswerFetchedListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendPath("api/faq/v1/answer.js");
        uriBuilder.appendQueryParameter("id", answerId);

        // if exist and updated in cache, fetch from cache,
        // else call to server
        HashMap<String, Object> answerParams = NRCacheManager.fetchFAQAnswer(answerId, answerHash);

        if(answerParams != null) {
            ((HashMap<String, Object>) answerParams).put("id", answerId);
            onFAQAnswerFetchedListener.onFAQAnswerFetched(new NRFAQAnswer((HashMap<String, Object>) answerParams), null);
        }
        else {

            // check if we already called fetch answer for this answer id
            ArrayList<OnFAQAnswerFetchedListener> onFAQAnswerFetchedListenerArr = NRImpl.this.getFaqRequestListenersMap().get(answerId);

            if(onFAQAnswerFetchedListenerArr == null) {// array is empty, no requests for this answer id

                onFAQAnswerFetchedListenerArr = new ArrayList<OnFAQAnswerFetchedListener>();

                onFAQAnswerFetchedListenerArr.add(onFAQAnswerFetchedListener);
                NRImpl.this.getFaqRequestListenersMap().put(answerId, onFAQAnswerFetchedListenerArr);

                final ArrayList<OnFAQAnswerFetchedListener> finalOnFAQAnswerFetchedListenerArr = onFAQAnswerFetchedListenerArr;


                executeRequest(uriBuilder, new NRConnection.Listener() {
                    @Override
                    public void response(Object responseParam, int status, NRError error) {

                        ((HashMap<String, Object>) responseParam).put("id", answerId);

                        for (OnFAQAnswerFetchedListener listener : finalOnFAQAnswerFetchedListenerArr) {
                            if (error != null) {
                                listener.onFAQAnswerFetched(null, error);
                            } else if (responseParam != null) {

                                listener.onFAQAnswerFetched(new NRFAQAnswer((HashMap<String, Object>) responseParam), null);
                            }
                        }

                        // store to cahce the answer from server
                        NRCacheManager.storeFAQAnswer((HashMap<String, Object>) responseParam);

                        NRImpl.this.getFaqRequestListenersMap().remove(answerId);
                    }
                });
            } else {
                onFAQAnswerFetchedListenerArr.add(onFAQAnswerFetchedListener);
                NRImpl.this.getFaqRequestListenersMap().put(answerId, onFAQAnswerFetchedListenerArr);
            }
        }
    }

    @Override
    public void likeForFAQResult(final NRFAQLikeParams likeParams, final OnLikeSentListener onLikeSentListener) {
        Uri.Builder uriBuilder = mAccountParams.getUri();
        uriBuilder.appendPath("api/widget/v1/thumb.js");
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
                    onLikeSentListener.onLikeSent(likeParams.getAnswerId(), 0, false);
                } else {
                    onLikeSentListener.onLikeSent(likeParams.getAnswerId(), Integer.parseInt(likeParams.getParams().get("type")), responseParam == null);
                }
            }
        });
    }

    @Override
    public void fetchConfiguration(final OnConfigurationFetchedListener onConfigurationFetchedListener) {
        if (mAccountParams != null) {
            final Uri.Builder uri = mAccountParams.getUri();
            uri.appendPath("widget/scripts/cnf.json");
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
                        HashMap<String, Object> cachedResponse = NRCacheManager.getAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getNanorepContext()));
                        if (onConfigurationFetchedListener != null) {
                            if (cachedResponse != null) {
                                onConfigurationFetchedListener.onConfigurationFetched(new NRConfiguration(cachedResponse), null);
                            } else {
                                onConfigurationFetchedListener.onConfigurationFetched(null, error);
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
                                    }
                                    if (onConfigurationFetchedListener != null) {
                                        if (error != null) {
                                            onConfigurationFetchedListener.onConfigurationFetched(null, error);
                                        } else if (responseParam != null) {
                                            onConfigurationFetchedListener.onConfigurationFetched(cnf, null);
                                            NRCacheManager.storeAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getNanorepContext()), responseParam);


                                            if(!fast) {
                                                updateFAQContentsAndCallHello(cnf);
                                            }
                                        } else {
                                            onConfigurationFetchedListener.onConfigurationFetched(null, NRError.error("com.nanorepfaq", 1002, "faqData empty"));
                                        }
                                    }
                                }
                            });
                        } else {
                            if (onConfigurationFetchedListener != null) {
                                onConfigurationFetchedListener.onConfigurationFetched(new NRConfiguration((HashMap) responseParam), null);
                            }
                            NRCacheManager.storeAnswerById(mContext, NRUtilities.md5(mAccountParams.getKnowledgeBase() + mAccountParams.getNanorepContext()), responseParam);

                            if(!fast) {
                                updateFAQContentsAndCallHello(cnf);
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateFAQContentsAndCallHello(NRConfiguration cnf)
    {
        hello(new NRConnection.Listener() {
            @Override
            public void response(Object responseParam, int status, NRError error) {

            }
        });

        // get contents for all Answers
        for (NRQueryResult queryResult : cnf.getFaqData().getGroups().get(0).getAnswers()) {
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
