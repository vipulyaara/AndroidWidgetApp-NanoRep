package nanorep.nanowidget.Components.AbstractViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import java.util.ArrayList;

import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRChannelingView;
import nanorep.nanowidget.R;

/**
 * Created by nanorep on 05/10/2016.
 */

public abstract class NRCustomChannelView extends LinearLayout implements NRChannelItem.OnChannelSelectedListener {
    protected NRChannelItem.OnChannelSelectedListener mListener;
    protected ArrayList<NRChanneling> mChannelings;
    protected ChannelingAdapter mAdapter;

    public NRCustomChannelView(Context context) {
        super(context);
        mAdapter = new ChannelingAdapter();
    }


    public void setListener(NRChannelItem.OnChannelSelectedListener listener) {
        mListener = listener;
    }

    public abstract void setChannelings(ArrayList<NRChanneling> channelings);

    public class ChannelingAdapter extends RecyclerView.Adapter<NRChannelItem> {

        @Override
        public NRChannelItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
            NRChannelItem item = new NRChannelItem(view);
            item.setListener(NRCustomChannelView.this);
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
