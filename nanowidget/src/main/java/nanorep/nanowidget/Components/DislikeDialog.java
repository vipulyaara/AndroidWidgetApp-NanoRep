package nanorep.nanowidget.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.nanorep.nanoclient.RequestParams.NRLikeType;

import nanorep.nanowidget.DataClasse.NRLikeAdapter;
import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 22/08/2016.
 */

public class DislikeDialog extends AlertDialog.Builder {

    private ListView reasonsList;
    private Listener mListener;
    private ImageView closeButton;
    private Button okButton;
    private NRLikeAdapter adapter;
    private AlertDialog alert;

    public interface Listener {
        void onCancel();
        void onDislike(NRLikeType type);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public DislikeDialog(@NonNull Context context, View dislikeView) {
        super(context);

        setView(dislikeView);
        reasonsList = (ListView) dislikeView.findViewById(R.id.reasonsList);
        closeButton = (ImageView) dislikeView.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                alert.dismiss();
            }
        });

        okButton = (Button) dislikeView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDislike(adapter.getSelection());
                alert.dismiss();
            }
        });
    }

    public void setDislikeOptions(String[] options) {
        adapter = new NRLikeAdapter(getContext(), R.layout.dislike_row, options);
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
        reasonsList.setAdapter(adapter);
//        setPositiveButton("OK", dialogClickListener);
//        setNegativeButton("Cancel", dialogClickListener);
        alert = create();
        alert.show();
//        show();
    }
}
