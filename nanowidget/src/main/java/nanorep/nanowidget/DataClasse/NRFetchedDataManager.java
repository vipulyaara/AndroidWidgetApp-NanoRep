package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.util.Log;

import com.nanorep.nanorepsdk.Connection.NRError;

import java.util.ArrayList;

import NanoRep.Interfaces.NRDefaultFAQCompletion;
import NanoRep.Interfaces.NRFAQAnswerCompletion;
import NanoRep.Interfaces.NRLikeCompletion;
import NanoRep.Interfaces.NRQueryResult;
import NanoRep.Interfaces.NRSpeechRecognizerCompletion;
import NanoRep.NanoRep;
import NanoRep.RequestParams.NRFAQLikeParams;
import NanoRep.RequestParams.NRLikeType;
import NanoRep.RequestParams.NRSearchLikeParams;
import NanoRep.ResponseParams.NRFAQAnswer;
import NanoRep.ResponseParams.NRFAQAnswerItem;
import NanoRep.ResponseParams.NRFAQCnf;
import NanoRep.ResponseParams.NRFAQData;
import NanoRep.ResponseParams.NRSearchResponse;
import NanoRep.ResponseParams.NRSuggestions;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRFetchedDataManager {
    private NanoRep mNanoRep;
    private NRFAQData mFaqData;
    private NRFetcherListener mFetcherListener;
    Context mContext;

    private int mRows;


    public NRFetchedDataManager(NanoRep nanoRep, Context context) {
        mContext = context;
        mNanoRep = nanoRep;
        mNanoRep.fetchDefaultFAQWithCompletion(new NRDefaultFAQCompletion() {
            @Override
            public void fetchDefaultFAQ(NRFAQCnf cnf, NRError error) {
                if (error == null && cnf != null) {
                    mFaqData = cnf.getFaqData();
                    if (cnf.getTitle() != null) {
                        mFetcherListener.updateTitle(cnf.getTitle());
                        prepareDatasource();
                    }
                } else if (error != null) {
                    onRequestError(error);
                }
            }
        });
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

    public NanoRep getNanoRep() {
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

    public void suggestionsForText(String text, NanoRep.NRSuggestionsCompletion completion) {

    }

    public void searchText(String text) {
        mNanoRep.searchText(text, new NanoRep.NRSearchCompletion() {
            @Override
            public void searchResponse(NRSearchResponse response, NRError error) {
                if (error != null) {

                } else {
                    updateResults(response.getAnswerList());
                }
            }
        });
    }

    public void searchSuggestion(final String suggestion) {
        mNanoRep.suggestionsForText(suggestion, new NanoRep.NRSuggestionsCompletion() {
            @Override
            public void suggustions(NRSuggestions suggestions, NRError error) {
                if (error != null) {
                    Log.d("Fetcher", error.getDomain());
                } else if (suggestions != null && suggestions.getSuggestions() != null){
                    mFetcherListener.presentSuggestion(suggestions.getSuggestions());
                }
            }
        });
    }

    public void startSpeech(NRSpeechRecognizerCompletion completion) {
        mNanoRep.startVoiceRecognition(completion);
    }

    public void sendLike(NRLikeType likeType, NRResult result, NRLikeCompletion completion) {
        if (result.getFetchedResult().isCNF()) {
            NRFAQLikeParams likeParams = new NRFAQLikeParams(result.getFetchedResult());
            likeParams.setLikeType(likeType);
            mNanoRep.faqLike(likeParams, completion);
        } else {
            NRSearchLikeParams likeParams = new NRSearchLikeParams(result.getFetchedResult());
            likeParams.setFeedbackType(likeType);
            mNanoRep.sendLike(likeParams, completion);
        }
    }

    public void faqAnswer(String answerId, final OnFAQAnswerFetched answerFetcher) {
        mNanoRep.answerWithId(answerId, new NRFAQAnswerCompletion() {
            @Override
            public void fetchAnswer(NRFAQAnswer answer, NRError error) {
                if (error == null) {
                    answerFetcher.onAnsweFetced(answer.getBody());
                } else {
                    answerFetcher.onAnsweFetced(null);
                }
            }
        });
    }

    private void onRequestError(NRError error) {

    }

}
