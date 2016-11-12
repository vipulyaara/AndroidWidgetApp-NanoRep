package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRCategoriesAdapter;
import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRCustomViewAdapter;

/**
 * Created by noat on 08/11/2016.
 */

public class NRCategoriesView extends LinearLayout {

    private RecyclerView mResultsRecyclerView;
    private NRCategoriesAdapter mCategoriesAdapter;

    public interface Listener {
        void onCategorySelected(NRFAQGroupItem groupItem);
    }

    public NRCategoriesView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.categories_view, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        mCategoriesAdapter = new NRCategoriesAdapter();

        mResultsRecyclerView = (RecyclerView) child.findViewById(R.id.resultsView);
        mResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mResultsRecyclerView.setAdapter(mCategoriesAdapter);
    }

    public void setListener(Listener listener) {
        mCategoriesAdapter.setListener(listener);
    }

    public void setCategories(ArrayList<NRFAQGroupItem> categories, NRCustomViewAdapter viewAdapter)  {
        mCategoriesAdapter.setViewAdapter(viewAdapter);
        mCategoriesAdapter.addItems(categories);
    }
}
