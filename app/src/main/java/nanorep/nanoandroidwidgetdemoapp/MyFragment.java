package nanorep.nanoandroidwidgetdemoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nanorep.nanowidget.Fragments.NRMainFragment;

/**
 * Created by noat on 01/12/2016.
 */

public class MyFragment extends Fragment {

    public static final String TAG = MyFragment.class.getName();

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        return view;
    }
}
