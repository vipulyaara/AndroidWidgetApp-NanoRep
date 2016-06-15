package nanorep.nanowidget.DataClasse;

import android.util.Log;

import com.nanorep.nanorepsdk.Connection.NRError;

import java.util.ArrayList;

import NanoRep.Interfaces.NRDefaultFAQCompletion;
import NanoRep.Interfaces.NRLikeCompletion;
import NanoRep.Interfaces.NRQueryResult;
import NanoRep.Interfaces.NRSpeechRecognizerCompletion;
import NanoRep.NanoRep;
import NanoRep.ResponseParams.NRFAQAnswerItem;
import NanoRep.ResponseParams.NRFAQCnf;
import NanoRep.ResponseParams.NRFAQData;
import NanoRep.ResponseParams.NRSearchResponse;
import NanoRep.ResponseParams.NRSuggestions;
import nanorep.nanowidget.interfaces.NRFetcherListener;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRFetchedDataManager {
    private NanoRep mNanoRep;
    private NRFAQData mFaqData;
    private NRFetcherListener mFetcherListener;

    private int mRows;


    public NRFetchedDataManager(NanoRep nanoRep) {
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
            mRows = answers.size();
            mFetcherListener.insertRows(answers);
        }
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
                    mFetcherListener.insertRows(response.getAnswerList());
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

    }

    public void sendLike(boolean like, NRResult result, NRLikeCompletion completion) {

    }

    private void onRequestError(NRError error) {

    }

}
