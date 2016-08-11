package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRSearchBarListener;


/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSearchBar extends RelativeLayout implements SearchView.OnQueryTextListener, View.OnClickListener {
    private NRSearchBarListener mListener;
    private SearchView mSearchView;
    private ImageButton mSpeechButton;

    public NRSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mSearchView = (SearchView) findViewById(R.id.searchView);
//        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() == R.id.searchView) {
            mSearchView = (SearchView) child;
            mSearchView.setOnQueryTextListener(this);
//            LinearLayout icon = (LinearLayout) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
//            TextView searchTextView = (TextView) icon.getChildAt(0);
//            searchTextView.setTextColor(Color.WHITE);
//            ImageView iconImg = (ImageView) icon.getChildAt(1);
//            iconImg.setImageResource(R.drawable.searchbar_search_icon);
        } else if (child instanceof AppCompatImageButton) {
            mSpeechButton = (ImageButton) child;
            mSpeechButton.setOnClickListener(this);
        }
    }

    public void setListener(NRSearchBarListener listener) {
        mListener = listener;
    }

    public void dismissKeyboard() {
        mSearchView.setIconified(true);
        mSearchView.post(new Runnable() {
            @Override
            public void run() {
                mSearchView.clearFocus();
            }
        });
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        mListener.searchForText(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Text", newText);
        if (newText.length() > 0) {
            mListener.fetchSuggestionsForText(newText);
        } else {
            mListener.onClear();
            dismissKeyboard();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mSpeechButton.setEnabled(false);
        mListener.onStartRecording(mSpeechButton);
    }
}
