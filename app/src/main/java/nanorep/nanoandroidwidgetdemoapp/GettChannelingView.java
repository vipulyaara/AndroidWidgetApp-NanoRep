package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.nanorep.nanoclient.Channeling.NRChanneling;

import java.util.ArrayList;

import nanorep.nanowidget.Components.AbstractViews.NRCustomChannelView;
import nanorep.nanowidget.Components.NRChannelItem;
import nanorep.nanowidget.Components.NRChannelingView;

/**
 * Created by noat on 30/11/2016.
 */

public class GettChannelingView extends NRCustomChannelView  {
    private ArrayList<NRChanneling> mChannelings;
    private RecyclerView mChannelingsRecycleView;
    private NRChannelingView.ChannelingAdapter mAdapter;


    public GettChannelingView(Context context) {
        super(context);
        mAdapter = new NRChannelingView.ChannelingAdapter();
        LayoutInflater.from(context).inflate(nanorep.nanowidget.R.layout.channeling_view, this);
    }

    @Override
    public void setChannelings(ArrayList<NRChanneling> channelings) {

    }

    @Override
    public void onChannelSelected(NRChannelItem channelItem) {
        mListener.onChannelSelected(channelItem);
    }
}
