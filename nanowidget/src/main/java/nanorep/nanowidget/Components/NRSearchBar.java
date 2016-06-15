package nanorep.nanowidget.Components;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRSearchBarListener;


/**
 * Created by nissimpardo on 07/06/16.
 */
public class NRSearchBar extends RelativeLayout implements SearchView.OnQueryTextListener {
    private NRSearchBarListener mListener;
    private SearchView mSearchView;

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
}
