package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import java.util.ArrayList;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 18/06/16.
 */
public class NRChannelingView extends NRCustomChannelView {

    private RecyclerView mChannelingsRecycleView;

    public NRChannelingView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.channeling_view, this);
    }

    @Override
    public void setChannelings(ArrayList<NRChanneling> channelings) {
        mChannelings = channelings;
        mChannelingsRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), mChannelings.size()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mChannelingsRecycleView = (RecyclerView) child.findViewById(R.id.channelingTabs);
        mChannelingsRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void onChannelSelected(NRChanneling channeling) {
        mListener.onChannelSelected(channeling);
    }

}
