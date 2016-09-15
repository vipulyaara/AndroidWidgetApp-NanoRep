package nanorep.nanowidget.interfaces;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 30/08/2016.
 */

public interface NRViewHolder {
    void setListener(NRResultItemListener listener);
    void setResult(NRResult result);
    RowType getRowType();

    enum RowType {
        standard, shrinked, unfolded, noResults
    }
}
