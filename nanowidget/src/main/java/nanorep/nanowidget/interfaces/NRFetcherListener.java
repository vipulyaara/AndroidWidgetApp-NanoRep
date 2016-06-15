package nanorep.nanowidget.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import NanoRep.Interfaces.NRQueryResult;

/**
 * Created by nissimpardo on 06/06/16.
 */
public interface NRFetcherListener {
    void updateTitle(String title);
    void reloadWithAimation();
    void reload();
    void insertRows(ArrayList<NRQueryResult> rows);
    void presentSuggestion(ArrayList<String> suggestions);
    void onConnectionFailed(HashMap<String, Object> errorParams);
}
