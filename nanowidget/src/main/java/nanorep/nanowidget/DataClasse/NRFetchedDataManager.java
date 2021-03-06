package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.util.Log;


import com.nanorep.nanoclient.Connection.NRError;
import com.nanorep.nanoclient.Handlers.NRErrorHandler;
import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Interfaces.NRSpeechRecognizerCompletion;
import com.nanorep.nanoclient.Nanorep;
import com.nanorep.nanoclient.RequestParams.NRFAQLikeParams;
import com.nanorep.nanoclient.RequestParams.NRLikeType;
import com.nanorep.nanoclient.RequestParams.NRSearchLikeParams;
import com.nanorep.nanoclient.Response.NRFAQAnswer;
import com.nanorep.nanoclient.Response.NRFAQData;
import com.nanorep.nanoclient.Response.NRSearchResponse;
import com.nanorep.nanoclient.Response.NRSuggestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Fragments.NRMainFragment;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRConfigFetcherListener;
import nanorep.nanowidget.interfaces.NRFetcherListener;
import nanorep.nanowidget.interfaces.OnFAQAnswerFetched;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRFetchedDataManager {

    private NRFAQData mFaqData;
    private NRFetcherListener mFetcherListener;
    private NRConfigFetcherListener mconfigFetcherListener;
    Context mContext;

    public NRFetchedDataManager(Context context, NRConfigFetcherListener configFetcherListener) {
        mContext = context;
        mconfigFetcherListener = configFetcherListener;

        fetchConfiguration();
    }

    public void fetchConfiguration() {
        Nanorep.getInstance().fetchConfiguration(new Nanorep.OnConfigurationFetchedListener() {
            @Override
            public void onConfigurationFetched(NRError error) {
                if (error == null) {
                    mFaqData = Nanorep.getInstance().getNRConfiguration().getFaqData();
                    if (Nanorep.getInstance().getNRConfiguration() != null) {
                        mconfigFetcherListener.onConfigurationReady();
                        prepareDatasource();
                    }
                } else if (error != null) {
                    mconfigFetcherListener.onError();
                }
            }
        }, false);
    }

    public void setFetcherListener(NRFetcherListener listener) {
        mFetcherListener = listener;
    }

    public void setConfigFetcherListener(NRConfigFetcherListener mconfigFetcherListener) {
        this.mconfigFetcherListener = mconfigFetcherListener;
    }

    private void prepareDatasource() {
        if (mFaqData != null && mFaqData.getGroups() != null && mFaqData.getGroups().size() > 0) {
            ArrayList<com.nanorep.nanoclient.Response.NRFAQGroupItem> groups = mFaqData.getGroups();//.get(0).getAnswers();
            updateCategoriesResults(groups);
        }
    }

    private void updateCategoriesResults(ArrayList<com.nanorep.nanoclient.Response.NRFAQGroupItem> groups) {
        if (groups != null) {

            mconfigFetcherListener.insertRows(groups);
        } else {
            mconfigFetcherListener.insertRows(null);
        }
    }

    public static ArrayList<NRResult> generateNRResultArray(ArrayList<NRQueryResult> queryResults, Context context) {
        if (queryResults != null) {
            int height = Integer.valueOf(Nanorep.getInstance().getNRConfiguration().getTitle().getTitleRowHeight());
            ArrayList<NRResult> results = new ArrayList<>();
            for (NRQueryResult result : queryResults) {
                NRResult currentResult = new NRResult(result, NRResultItem.RowType.TITLE);
                currentResult.setHeight((int) Calculate.pxFromDp(context, height));
                results.add(currentResult);
            }
            return results;
        }
        return null;
    }

    public void searchText(final String text) {


        Nanorep.getInstance().searchText(text, new Nanorep.OnSearchResultsFetchedListener() {
            @Override
            public void onSearchResponse(NRSearchResponse response, NRError error) {
                if (error != null) {

                } else {
                    ArrayList<NRResult> results = generateNRResultArray(response.getAnswerList(), mContext);

                    mFetcherListener.insertRows(results);
                }
            }
        });
    }

    public void searchSuggestion(final String suggestion) {
        Nanorep.getInstance().suggestionsForText(suggestion, new Nanorep.OnSuggestionsFetchedListener() {
            @Override
            public void onSuggestionsFetched(NRSuggestions suggestions, NRError error) {
                if (error != null) {
                    Log.d("Fetcher", error.getDomain());
                } else if (suggestions != null && suggestions.getSuggestions() != null) {
                    mFetcherListener.presentSuggestion(suggestion, suggestions.getSuggestions());
                }
            }
        });
    }

    public void startSpeech(NRSpeechRecognizerCompletion completion) {
//        mNanoRep.startVoiceRecognition(completion);
    }

    public void sendLike(NRLikeType likeType, NRQueryResult result, Nanorep.OnLikeSentListener completion) {
        if (true) {//result.isCNF()) {
            NRFAQLikeParams likeParams = new NRFAQLikeParams(result);
            likeParams.setLikeType(likeType);
            likeParams.setAnswerId(result.getId());
            Nanorep.getInstance().likeForFAQResult(likeParams, completion);
        } else {
            NRSearchLikeParams likeParams = new NRSearchLikeParams(result);
            likeParams.setFeedbackType(likeType);
            Nanorep.getInstance().likeForSearchResult(likeParams, completion);
        }
    }

    public void resetLike(String resultId) {
        for (NRQueryResult result : mFaqData.getGroups().get(0).getAnswers()) {
            if (result.getId().equals(resultId)) {
                result.setLikeState(NRQueryResult.LikeState.notSelected);
            }
        }
    }

    public void faqAnswer(final String answerId, Integer answerHash, final OnFAQAnswerFetched answerFetcher) {
        Nanorep.getInstance().fetchFAQAnswer(answerId, answerHash, new Nanorep.OnFAQAnswerFetchedListener() {
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
