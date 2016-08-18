package nanorep.nanowidget.Components;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import nanorep.nanowidget.R;
import nanorep.nanowidget.interfaces.NRSearchBarListener;

import static nanorep.nanowidget.R.id.searchView;


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
        if (child.getId() == searchView) {
            mSearchView = (SearchView) child;
            mSearchView.setOnQueryTextListener(this);


//            if (mSearchView instanceof ViewGroup) {
//                ViewGroup viewGroup = (ViewGroup) mSearchView;
//                for (int i = 0; i < viewGroup.getChildCount(); i++) {
//                    changeSearchViewTextColor(viewGroup.getChildAt(i));
//                }
//            }
//            View icon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_button);
//            ((AppCompatImageView)icon).setImageResource(R.drawable.searchbar_search_icon);
//            SearchView.SearchAutoComplete textArea = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//            textArea.setTextColor(Color.WHITE);
//            try {
//                Field searchField = SearchView.class.getDeclaredField("mCloseButton");
//                searchField.setAccessible(true);
//                ImageView closeBtn = (ImageView) searchField.get(mSearchView);
//                closeBtn.setImageResource(R.drawable.searchbar_x_icon);
//
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }


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
