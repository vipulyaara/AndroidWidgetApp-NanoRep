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

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 18/06/16.
 */
public class NRChannelingView extends RelativeLayout implements NRChannelItem.OnChannelSelectedListener {
    private ArrayList<NRChanneling> mChannelings;
    private RecyclerView mChannelingsRecycleView;
    private ChannelingAdapter mAdapter;
    private NRChannelItem.OnChannelSelectedListener mListener;

    public NRChannelingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new ChannelingAdapter();
    }

    public void setChannelings(ArrayList<NRChanneling> channelings) {
        mChannelings = channelings;
        mChannelingsRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), mChannelings.size()));
        mAdapter.notifyDataSetChanged();
    }

    public void setListener(NRChannelItem.OnChannelSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() == R.id.channelingTabs) {
            mChannelingsRecycleView = (RecyclerView) child;
            mChannelingsRecycleView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        mListener.onChannelSelected(channelItem);
    }

    private class ChannelingAdapter extends RecyclerView.Adapter<NRChannelItem> {

        @Override
        public NRChannelItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, null);
            NRChannelItem item = new NRChannelItem(view);
            item.setListener(NRChannelingView.this);
            return item;
        }

        @Override
        public void onBindViewHolder(NRChannelItem holder, int position) {
            holder.setChanneling(mChannelings.get(position));
        }

        @Override
        public int getItemCount() {
            if (mChannelings != null) {
                return mChannelings.size();
            }
            return 0;
        }
    }
}
