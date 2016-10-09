package nanorep.nanowidget.interfaces;

import android.widget.ImageButton;

/**
 * Created by nissimpardo on 07/06/16.
 */
public interface NRSearchBarListener {
    void onStartRecording(ImageButton button);
    void fetchSuggestionsForText(String text);
    void searchForText(String text);
    void onClearClicked(boolean byUser);
    void onEmptyQuery();
}
