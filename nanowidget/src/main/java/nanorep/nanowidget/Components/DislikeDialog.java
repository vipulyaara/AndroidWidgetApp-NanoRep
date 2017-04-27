package nanorep.nanowidget.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 22/08/2016.
 */

public class DislikeDialog extends AlertDialog.Builder {

    private ListView reasonsList;
    private Listener mListener;
    private TextView closeButton;
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
        closeButton = (TextView) dislikeView.findViewById(R.id.closeButton);

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

    public class NRLikeAdapter extends ArrayAdapter<String> implements View.OnClickListener {
        private String[] mObjects;
        private ArrayList<ImageView> bullets;
        private NRLikeType mSelection = NRLikeType.INCORRECT_ANSWER;

        public NRLikeAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            mObjects = objects;
        }

        public NRLikeType getSelection() {
            return mSelection;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dislike_row, null);
                convertView.setTag(position);
                convertView.setOnClickListener(this);
                TextView titleView = (TextView) convertView.findViewById(R.id.dislike);
                titleView.setText(mObjects[position]);
                if (bullets == null) {
                    bullets = new ArrayList<>();
                }
                bullets.add((ImageView) convertView.findViewById(R.id.imageView));
            }
            return convertView;
        }

        private int resId(String resName) {
            return getContext().getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
        }


        @Override
        public void onClick(View v) {
            switch ((int)v.getTag()) {
//                case 0:
//                    mSelection = NRLikeType.INCORRECT_ANSWER;
//                    break;
                case 0:
                    mSelection = NRLikeType.MISSING_INFORMATION;
                    break;
                case 1:
                    mSelection = NRLikeType.IRRELEVANT;
                    break;
            }

            okButton.setEnabled(true);
            okButton.setTextColor(Color.parseColor("#0aa0ff"));

            for (int i = 0; i < bullets.size(); i++) {
                int id;
                if ((int) v.getTag() == i) {
                    id = resId("bullet_on");
                } else {
                    id = resId("bullet_off");
                }
                bullets.get(i).setImageResource(id);
            }
        }
    }

}
