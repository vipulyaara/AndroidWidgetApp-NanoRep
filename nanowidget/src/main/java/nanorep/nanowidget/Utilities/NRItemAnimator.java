package nanorep.nanowidget.Utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Components.NRTitleItem;

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
        void onItemMoveFinished(NRResultItem item);
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
    public void onMoveFinished(RecyclerView.ViewHolder item) {
        super.onMoveFinished(item);
        mListener.onItemMoveFinished((NRResultItem)item);
    }

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        return true;
    }

}
