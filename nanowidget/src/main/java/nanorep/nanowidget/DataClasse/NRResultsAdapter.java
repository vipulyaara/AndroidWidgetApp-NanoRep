package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import nanorep.nanowidget.Components.NRResultItem;
import nanorep.nanowidget.Fragments.NRWidgetCategoriesFragment;
import nanorep.nanowidget.Utilities.FragmentUtils;
import nanorep.nanowidget.interfaces.CRUDAdapterInterface;

import com.nanorep.nanoclient.Interfaces.NRQueryResult;
import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;
import java.util.List;

import nanorep.nanowidget.Components.AbstractViews.NRCustomTitleView;
import nanorep.nanowidget.Components.NRTitleView;
import nanorep.nanowidget.Fragments.NRWidgetFragment;
import nanorep.nanowidget.R;
import nanorep.nanowidget.Utilities.Calculate;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;
import nanorep.nanowidget.interfaces.NRTitleListener;

/**
 * Created by noat on 03/11/2016.
 */

public class NRResultsAdapter  extends RecyclerView.Adapter<NRResultsAdapter.ViewHolder> implements CRUDAdapterInterface<NRFAQGroupItem> {

    private ArrayList<NRFAQGroupItem> mGroupResults;
    private NRCustomViewAdapter viewAdapter;
    private Context context;

    public NRResultsAdapter(NRCustomViewAdapter viewAdapter, Context context) {
        this.viewAdapter = viewAdapter;
        this.context = context;
        this.mGroupResults = new ArrayList<NRFAQGroupItem>();
    }

    private boolean mShouldResetLikeView = false;

    public void setShouldResetLikeView(boolean shouldResetLikeView) {
        mShouldResetLikeView = shouldResetLikeView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements NRTitleListener{
        private NRCustomTitleView titleView;

        public ViewHolder(View view, NRCustomTitleView titleView) {
            super(view);

            this.titleView = titleView;
        }

        @Override
        public void onTitleClicked() {
            int pos = ViewHolder.this.getAdapterPosition();
            showItem(getItem(pos), pos);
        }

        @Override
        public void onTitleCollapsed() {

        }

        @Override
        public void onShareClicked() {

        }
    }


    @Override
    public NRResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false);

        NRCustomTitleView titleView = viewAdapter.getTitle(context);

        if(titleView == null){
            titleView = new NRTitleView(context);
        }

        LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.title_container);

        titleContainer.addView(titleView);

        view.getLayoutParams().height = (int) Calculate.pxFromDp(context, 45);

        ViewHolder holder = new ViewHolder(view, titleView);

        titleView.setListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(NRResultsAdapter.ViewHolder holder, int position) {
        final NRFAQGroupItem groupItem = getItem(position);
        final int pos = position;

        holder.titleView.setTitleText(groupItem.getTitle());
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
        NRWidgetFragment nrWidgetFragment = NRWidgetFragment.newInstance();
        ArrayList<NRQueryResult> queryResults = item.getAnswers();

        if (queryResults != null) {
            ArrayList<NRResult> results = new ArrayList<>();
            for (NRQueryResult result : queryResults) {
                NRResult currentResult = new NRResult(result, NRResultItem.RowType.TITLE);
                currentResult.setHeight((int) Calculate.pxFromDp(context, 45));
                results.add(currentResult);
            }
            nrWidgetFragment.setmQueryResults(results);
        } else {
            nrWidgetFragment.setmQueryResults(null);
        }

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        NRWidgetCategoriesFragment nrWidgetCategoriesFragment = (NRWidgetCategoriesFragment)fragmentManager.findFragmentByTag(NRWidgetCategoriesFragment.TAG);

        nrWidgetFragment.setmFetchedDataManager(nrWidgetCategoriesFragment.getmFetchedDataManager());

        FragmentUtils.addFragment(nrWidgetCategoriesFragment, nrWidgetFragment, ((ViewGroup)nrWidgetCategoriesFragment.getView().getParent()).getId(), context);
    }

    @Override
    public List<NRFAQGroupItem> getItems() {
        return null;
    }

    @Override
    public void removeItem(int currentItemPosition) {

    }
}