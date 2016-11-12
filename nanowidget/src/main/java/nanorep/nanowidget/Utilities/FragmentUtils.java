package nanorep.nanowidget.Utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by noat on 03/11/2016.
 */

public class FragmentUtils {

    public static void addFragment(Fragment fromFragment, Fragment toFragment, int where, Context context) {
        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(where,toFragment, toFragment.getClass().getName());
        transaction.hide(fromFragment);
        transaction.addToBackStack(toFragment.getClass().getName());
        transaction.commit();
    }

    public static void addChildFragment(Fragment parentFragment, Fragment fromFragment, Fragment toFragment, int where) {
        FragmentManager manager = parentFragment.getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(where,toFragment, toFragment.getClass().getName());
        transaction.hide(fromFragment);
        transaction.addToBackStack(toFragment.getClass().getName());
        transaction.commit();
    }

    public static void openFragment(Fragment which, int where, String tag, Context context, Boolean isAddToBackStack){

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(tag) == null) {

            FragmentTransaction t = fragmentManager.beginTransaction();

            t.add(where, which, tag);
            if (isAddToBackStack)
                t.addToBackStack(tag);
            t.commit();
        }
    }

    public static void openChildFragment(Fragment which, int where, String tag, Fragment fragment, Boolean isAddToBackStack){

        FragmentManager fragmentManager = fragment.getChildFragmentManager();

        if (fragmentManager.findFragmentByTag(tag) == null) {

            FragmentTransaction t = fragmentManager.beginTransaction();

            t.add(where, which, tag);
            if (isAddToBackStack)
                t.addToBackStack(tag);
            t.commit();
        }
    }

}
