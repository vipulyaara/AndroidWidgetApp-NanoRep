package nanorep.nanowidget.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.nanorep.nanoclient.RequestParams.NRLikeType;

import nanorep.nanowidget.DataClasse.NRLikeAdapter;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 22/08/2016.
 */

public class DislikeDialog extends AlertDialog.Builder {
    private Listener mListener;

    public interface Listener {
        void onCancel();
        void onDislike(NRLikeType type);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public DislikeDialog(@NonNull Context context) {
        super(context);
    }

    public void setDislikeOptions(String[] options) {
        final NRLikeAdapter adapter = new NRLikeAdapter(getContext(), R.layout.dislike_row, options);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE && adapter.getSelection() != NRLikeType.POSITIVE) {
                    mListener.onDislike(adapter.getSelection());
                } else {
                    mListener.onCancel();
                }
            }
        };
        setAdapter(adapter, null);
        setPositiveButton("OK", dialogClickListener);
        setNegativeButton("Cancel", dialogClickListener);
        AlertDialog alert = create();
        alert.show();
    }
}
