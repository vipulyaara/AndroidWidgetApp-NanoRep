package nanorep.nanowidget.interfaces;

import android.text.Spannable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 06/06/16.
 */
public interface NRFetcherListener {
//    void onConfigurationReady();
    void reloadWithAimation();
    void reload();
    void insertRows(ArrayList<NRResult> rows);
    void presentSuggestion(String querytext, ArrayList<Spannable> suggestions);
    void onConnectionFailed(HashMap<String, Object> errorParams);
}
