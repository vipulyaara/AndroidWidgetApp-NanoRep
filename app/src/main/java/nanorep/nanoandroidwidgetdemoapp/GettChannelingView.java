package nanorep.nanoandroidwidgetdemoapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
    private TextView tv;

    public GettChannelingView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.channeling_gett, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        tv = (TextView) child.findViewById(R.id.feedbackReply);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChannelSelected(mChannelings.get(0));
            }
        });
    }

    @Override
    public void setChannelings(ArrayList<NRChanneling> channelings) {
       mChannelings = channelings;
    }

    @Override
    public void onChannelSelected(NRChanneling channeling) {

    }

    public boolean isChannelingEmpty() {
        return mChannelings == null || mChannelings.size() == 0;
    }
}
