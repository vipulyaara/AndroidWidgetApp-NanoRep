package nanorep.nanowidget.interfaces;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.RequestParams.NRLikeType;


/**
 * Created by nissimpardo on 22/08/2016.
 */

public interface OnLinkedArticle {
    void onLinkedArticleClicked(OnFAQAnswerFetched listener, String articleId);
    void onLikeSelected(NRResultView resultView, NRLikeType likeType, NRQueryResult currentResult);
}
