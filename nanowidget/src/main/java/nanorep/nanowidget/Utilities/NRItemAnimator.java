package nanorep.nanowidget.Utilities;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import nanorep.nanowidget.Components.NRResultItem;

/**
 * Created by nissimpardo on 01/09/2016.
 */

public class NRItemAnimator extends DefaultItemAnimator {
    private OnAnimation mListener;

    public NRItemAnimator() {
        setRemoveDuration(200);
    }

    public interface OnAnimation {
        void onItemRemoved(NRResultItem item);
    }

    public void setListener(OnAnimation listener) {
        mListener = listener;
    }

    @Override
    public void onRemoveFinished(RecyclerView.ViewHolder item) {
        super.onRemoveFinished(item);
        mListener.onItemRemoved((NRResultItem)item);
    }

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        return true;
    }

}
