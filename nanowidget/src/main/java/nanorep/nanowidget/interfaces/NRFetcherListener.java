package nanorep.nanowidget.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 06/06/16.
 */
public interface NRFetcherListener {
    void onConfigurationReady();
    void reloadWithAimation();
    void reload();
    void insertRows(ArrayList<NRResult> rows);
    void presentSuggestion(String querytext, ArrayList<String> suggestions);
    void onConnectionFailed(HashMap<String, Object> errorParams);
}
