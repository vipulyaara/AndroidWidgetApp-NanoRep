package nanorep.nanowidget.Components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nanorep.Chnneling.NRChanneling;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRChannelItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnChannelSelectedListener mListener;
    private NRChanneling mChanneling;

    private ImageView mIcon;
    private TextView mTextView;

    public interface OnChannelSelectedListener {
        void onChannelSelected(NRChannelItem channelItem);
    }
    public NRChannelItem(View itemView) {
        super(itemView);
        mIcon = (ImageView) itemView.findViewById(R.id.channelIcon);
        mTextView = (TextView) itemView.findViewById(R.id.channelName);
        itemView.setOnClickListener(this);
    }

    public void setListener(OnChannelSelectedListener listener) {
        mListener = listener;
    }

    public NRChanneling getChanneling() {
        return mChanneling;
    }

    public void setChanneling(NRChanneling channeling) {
        mChanneling = channeling;
        mTextView.setText(channeling.getButtonText());
        String resName = null;
        switch (channeling.getType()) {
            case PhoneNumber:
                resName = "channel_call_icon";
                break;
            case ChatForm:
                resName = "channel_chat_icon";
                break;
            case OpenCustomURL:
            case ContactForm:
                resName = "channel_form_icon";
                break;

        }
        if (resName != null) {
            mIcon.setImageResource(itemView.getResources().getIdentifier(resName, "drawable", itemView.getContext().getPackageName()));
        }
    }


    @Override
    public void onClick(View v) {
        mListener.onChannelSelected(this);
    }
}
