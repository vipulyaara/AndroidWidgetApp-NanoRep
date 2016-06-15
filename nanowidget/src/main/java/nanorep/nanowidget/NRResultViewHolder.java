package nanorep.nanowidget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 04/06/16.
 */
public class NRResultViewHolder extends RecyclerView.ViewHolder {
    private NRResult mResult;

    public NRResultViewHolder(View itemView) {
        super(itemView);
    }

    public NRResult getResult() {
        return mResult;
    }

    public void setResult(NRResult result) {
        mResult = result;
    }
}
