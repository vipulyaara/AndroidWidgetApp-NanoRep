package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.util.Log;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Interfaces.NRSpeechRecognizerCompletion;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRLikeType;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRConfiguration;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRFAQData;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.util.ArrayList;

import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRFetcherListener;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRFetchedDataManager {
    private Nanorep mNanoRep;
    private NRFAQData mFaqData;
    private NRFetcherListener mFetcherListener;
    Context mContext;

    private int mRows;


    public NRFetchedDataManager(Nanorep nanoRep, Context context) {
        mContext = context;
        if (nanoRep != null) {
            mNanoRep = nanoRep;
            mNanoRep.fetchConfiguration(new Nanorep.OnConfigurationFetchedListener() {
                @Override
                public void onConfigurationFetched(NRConfiguration configuration, NRError error) {
                    if (error == null && configuration != null) {
                        mFaqData = configuration.getFaqData();
                        if (configuration.getTitle() != null) {
                            mFetcherListener.updateTitle(configuration.getTitle());
                            prepareDatasource();
                        }
                    } else if (error != null) {
                        onRequestError(error);
                    }
                }
            });
        }
    }

    public void setFetcherListener(NRFetcherListener listener) {
        mFetcherListener = listener;

    }

    private void prepareDatasource() {
        if (mFaqData != null && mFaqData.getGroups() != null && mFaqData.getGroups().size() > 0) {
            ArrayList<NRQueryResult> answers = mFaqData.getGroups().get(0).getAnswers();
            updateResults(answers);
        }
    }

    private void updateResults(ArrayList<NRQueryResult> queryResults) {
        if (queryResults != null) {
            ArrayList<NRResult> results = new ArrayList<>();
            for (NRQueryResult result : queryResults) {
                NRResult currentResult = new NRResult(result);
                currentResult.setHeight((int) Calculate.pxFromDp(mContext, 62));
                results.add(currentResult);
            }
            mRows = queryResults.size();
            mFetcherListener.insertRows(results);
        }
    }

    public Nanorep getNanoRep() {
        return mNanoRep;
    }

    public int getRows() {
        return 0;
    }

    public float getHeightForRow(int row) {
        return 0;
    }

    public NRResult resultForRow(int row) {
        return null;
    }

    public void suggestionsForText(String text, Nanorep.OnSuggestionsFetchedListener completion) {

    }

    public void searchText(String text) {
        mNanoRep.searchText(text, new Nanorep.OnSearchResultsFetchedListener() {
            @Override
            public void onSearchResponse(NRSearchResponse response, NRError error) {
                if (error != null) {

                } else {
                    updateResults(response.getAnswerList());
                }
            }
        });
    }

    public void searchSuggestion(final String suggestion) {
        mNanoRep.suggestionsForText(suggestion, new Nanorep.OnSuggestionsFetchedListener() {
            @Override
            public void onSuggestionsFetched(NRSuggestions suggestions, NRError error) {
                if (error != null) {
                    Log.d("Fetcher", error.getDomain());
                } else if (suggestions != null && suggestions.getSuggestions() != null){
                    mFetcherListener.presentSuggestion(suggestions.getSuggestions());
                }
            }
        });
    }

    public void startSpeech(NRSpeechRecognizerCompletion completion) {
//        mNanoRep.startVoiceRecognition(completion);
    }

    public void sendLike(NRLikeType likeType, NRQueryResult result, Nanorep.OnLikeSentListener completion) {
        if (true){//result.isCNF()) {
            NRFAQLikeParams likeParams = new NRFAQLikeParams(result);
            likeParams.setLikeType(likeType);
            mNanoRep.likeForFAQResult(likeParams, completion);
        } else {
            NRSearchLikeParams likeParams = new NRSearchLikeParams(result);
            likeParams.setFeedbackType(likeType);
            mNanoRep.likeForSearchResult(likeParams, completion);
        }
    }

    public void faqAnswer(String answerId, final nanorep.nanowidget.interfaces.OnFAQAnswerFetched answerFetcher) {
        mNanoRep.fetchFAQAnswer(answerId, new Nanorep.OnFAQAnswerFetchedListener() {
            @Override
            public void onFAQAnswerFetched(NRFAQAnswer faqAnswer, NRError error) {
                if (error == null) {
                    answerFetcher.onAnswerFetched(faqAnswer);
                } else {
                    answerFetcher.onAnswerFetched(null);
                }
            }
        });
    }

    private void onRequestError(NRError error) {

    }

}
