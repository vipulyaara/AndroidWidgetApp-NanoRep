package nanorep.nanowidget.DataClasse;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRResultsView;
import nanorep.nanowidget.Components.NRTitleItem;
import nanorep.nanowidget.Components.NRTitleView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.CRUDAdapterInterface;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRTitleListener;

/**
 * Created by noat on 08/11/2016.
 */

public class NRResultsAdapter extends RecyclerView.Adapter<NRResultsAdapter.ViewHolder> implements CRUDAdapterInterface<NRResult> {

    private ArrayList<NRResult> results;
    private NRCustomViewAdapter viewAdapter;
    private NRResultsAdapter.Listener listener;

    public interface Listener {
        void onResultItemSelected(ViewHolder titleViewHolder, int pos);
    }

    public NRResultsAdapter() {
        results = new ArrayList<NRResult>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements NRTitleListener{


        private NRCustomTitleView titleView;
        private LinearLayout title_container;
        private View view;
        private NRResult result;

        public ViewHolder(View view, NRCustomTitleView titleView, LinearLayout titleContainer) {
            super(view);

            this.titleView = titleView;

            this.title_container = titleContainer;

            this.view = view;
        }

        private void setHeight(final int height) {
            ValueAnimator animator = ValueAnimator.ofInt(view.getHeight(), height);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    titleView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    view.requestLayout();
                }
            });

            animator.start();
        }

        public void setResult(NRResult result) {
            this.result = result;
        }

        public NRResult getResult() {
            return result;
        }

        public NRCustomTitleView getTitleView() {
            return titleView;
        }

        public LinearLayout getTitle_container() {
            return title_container;
        }

        @Override
        public void onTitleClicked() {

            listener.onResultItemSelected(this, getAdapterPosition());
        }

        @Override
        public void onTitleCollapsed(int height) {

        }

        @Override
        public void onShareClicked() {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false);

        NRCustomTitleView titleView = viewAdapter.getTitle(context);

        if(titleView == null){
            titleView = new NRTitleView(context);
        }

        LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.title_container);

        titleContainer.addView(titleView);

        return new ViewHolder(view, titleView, titleContainer);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        final NRResult result = getItem(position);

        holder.titleView.setTitleText(result.getFetchedResult().getTitle());

        holder.setHeight(result.getHeight());

        holder.setResult(result);

        holder.titleView.setListener(holder);
    }


    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public NRResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public void addItem(NRResult item) {

    }

    @Override
    public void removeItem(NRResult item) {

    }

    @Override
    public void addItems(List<NRResult> items) {
        results.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void clearList() {

    }

    @Override
    public void updateItem(int pos, NRResult item) {

    }

    @Override
    public void showItem(NRResult result, int itemPosition) {
//        listener.onResultSelected(result);
    }

    @Override
    public List<NRResult> getItems() {
        return null;
    }

    @Override
    public void removeItem(int currentItemPosition) {

    }

    public void setViewAdapter(NRCustomViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

    public void setListener(NRResultsAdapter.Listener listener) {
        this.listener = listener;
    }
}
