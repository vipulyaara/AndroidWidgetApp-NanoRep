package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import nanorep.nanowidget.Components.NRCategoriesView;
import nanorep.nanowidget.interfaces.CRUDAdapterInterface;

import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;
import java.util.List;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRTitleView;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRTitleListener;

/**
 * Created by noat on 03/11/2016.
 */

public class NRCategoriesAdapter extends RecyclerView.Adapter<NRCategoriesAdapter.ViewHolder> implements CRUDAdapterInterface<NRFAQGroupItem> {

    private ArrayList<NRFAQGroupItem> mGroupResults;
    private NRCustomViewAdapter viewAdapter;
    private NRCategoriesView.Listener listener;

    public NRCategoriesAdapter() {
        this.mGroupResults = new ArrayList<NRFAQGroupItem>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements NRTitleListener{
        private NRCustomTitleView titleView;

        public ViewHolder(View view, NRCustomTitleView titleView) {
            super(view);

            this.titleView = titleView;
        }

        @Override
        public void onTitleClicked() {
            showItem(mGroupResults.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onTitleCollapsed(int height) {

        }

        @Override
        public void onShareClicked() {

        }
    }


    @Override
    public NRCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);

        NRCustomTitleView titleView = viewAdapter.getTitle(context);

        if(titleView == null){
            titleView = new NRTitleView(context);
        }

        LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.title_container);

        titleContainer.addView(titleView);

//        view.getLayoutParams().height = (int) Calculate.pxFromDp(context, NRFetchedDataManager.ROW_HEIGHT);

        ViewHolder holder = new ViewHolder(view, titleView);

        return holder;
    }

    @Override
    public void onBindViewHolder(NRCategoriesAdapter.ViewHolder holder, final int position) {
        final NRFAQGroupItem groupItem = getItem(position);

        holder.titleView.setTitleText(groupItem.getTitle());

        holder.titleView.setListener(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {

        return mGroupResults.size();
    }

    @Override
    public NRFAQGroupItem getItem(int position) {
        return mGroupResults.get(position);
    }

    @Override
    public void addItem(NRFAQGroupItem item) {

    }

    @Override
    public void removeItem(NRFAQGroupItem item) {

    }

    @Override
    public void addItems(List<NRFAQGroupItem> items) {
        mGroupResults.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void clearList() {

    }

    @Override
    public void updateItem(int pos, NRFAQGroupItem item) {

    }

    @Override
    public void showItem(NRFAQGroupItem item, int itemPosition) {
        listener.onCategorySelected(item);
    }

    @Override
    public List<NRFAQGroupItem> getItems() {
        return null;
    }

    @Override
    public void removeItem(int currentItemPosition) {

    }

    public void setListener(NRCategoriesView.Listener listener) {
        this.listener = listener;
    }

    public void setViewAdapter(NRCustomViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }
}