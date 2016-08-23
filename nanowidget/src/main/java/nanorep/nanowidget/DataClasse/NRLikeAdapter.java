package nanorep.nanowidget.DataClasse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanorep.nanoclient.RequestParams.NRLikeType;

import java.util.ArrayList;

import nanorep.nanowidget.R;

/**
 * Created by nissimpardo on 22/08/2016.
 */

public class NRLikeAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    private String[] mObjects;
    private ArrayList<ImageView> bullets;
    private NRLikeType mSelection = NRLikeType.POSITIVE;

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
            case 0:
                mSelection = NRLikeType.INCORRECT_ANSWER;
                break;
            case 1:
                mSelection = NRLikeType.MISSING_INFORMATION;
                break;
            case 2:
                mSelection = NRLikeType.IRRELEVANT;
                break;
        }
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
