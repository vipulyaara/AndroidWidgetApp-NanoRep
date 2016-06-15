package nanorep.nanowidget.interfaces;

/**
 * Created by nissimpardo on 07/06/16.
 */
public interface NRSearchBarListener {
    void onStartRecording();
    void fetchSuggestionsForText(String text);
    void searchForText(String text);
    void onClear();
}
